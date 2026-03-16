# Semaphore Pattern: Simpler Alternative to wait()/notify()

A cleaner, simpler way to coordinate turn-based execution between threads using `Semaphore`.

---

## Overview

The Semaphore is a synchronization primitive that's **much simpler than wait/notify** for turn-based coordination:

```java
// ❌ COMPLEX - wait/notify approach
synchronized(lock) {
    while (turn != currentTurn) lock.wait();
    action.run();
    switchTurn();
    lock.notifyAll();
}

// ✅ SIMPLE - Semaphore approach
myTurn.acquire();
action.run();
otherTurn.release();
```

---

## Core Concept: Permits

A `Semaphore` maintains a count of available **permits**:

```java
Semaphore pingTurn = new Semaphore(1);   // Start with 1 permit
Semaphore pongTurn = new Semaphore(0);   // Start with 0 permits
```

### How It Works

```
Permits: A "ticket" to proceed

pingTurn: 1 permit available  → Ping can proceed immediately
pongTurn: 0 permits          → Pong must wait

When Ping finishes:
  pongTurn.release()         → Gives 1 permit to Pong
  pongTurn: 1 permit now     → Pong can now proceed
  pingTurn: 0 permits        → Ping must wait

When Pong finishes:
  pingTurn.release()         → Gives 1 permit back to Ping
  pingTurn: 1 permit now     → Ping can proceed again
```

---

## Key Methods

### `acquire()` - Wait for a Permit
```java
semaphore.acquire();  // Block until permit available
```

- Waits (blocks) if no permits available
- Acquires 1 permit when available
- Throws `InterruptedException` if interrupted
- Similar to `lock.wait()` but simpler

### `release()` - Give a Permit
```java
semaphore.release();  // Release 1 permit
```

- Releases 1 permit back to the semaphore
- Wakes up one waiting thread
- Can be called from any thread (even if it didn't acquire)
- Never blocks

### `availablePermits()` - Check Status (Testing Only)
```java
int permits = semaphore.availablePermits();
```

- Returns current number of available permits
- Useful for debugging and testing only
- **Don't use in production logic**

---

## Comparison: wait/notify vs Semaphore

### wait/notify Pattern
```java
private final Object lock = new Object();
private boolean pingTurn = true;

synchronized(lock) {
    while (!pingTurn) {           // ← Check condition
        lock.wait();              // ← Wait
    }
    action.run();
    pingTurn = false;
    lock.notifyAll();             // ← Wake all
}
```

**Problems:**
- Explicit lock required
- Need while loop for spurious wakeups
- Manual state management
- Easy to get wrong

### Semaphore Pattern
```java
private final Semaphore pingTurn = new Semaphore(1);
private final Semaphore pongTurn = new Semaphore(0);

pingTurn.acquire();              // ← Wait for permit
action.run();
pongTurn.release();              // ← Give permit to other
```

**Advantages:**
- No explicit lock needed
- No while loops (handles spurious wakeups automatically)
- Built-in coordination
- Hard to misuse

---

## The PingPongGame with Semaphore

### Setup: Initialize Semaphores

```java
// Ping starts with a permit (goes first)
private final Semaphore pingTurn = new Semaphore(1);

// Pong starts with no permits (waits initially)
private final Semaphore pongTurn = new Semaphore(0);
```

### Generic Method: Handle All Coordination

```java
private void doTurnBasedWork(Semaphore myTurn, Semaphore otherTurn,
                              Runnable action, int count) {
    try {
        for (int i = 0; i < count; i++) {
            myTurn.acquire();      // ← Wait for my turn
            action.run();          // ← Do work
            otherTurn.release();   // ← Give turn to other
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}
```

### Public API: Simple and Clear

```java
public void ping(int count) {
    doTurnBasedWork(pingTurn, pongTurn,
                   () -> System.out.print("Ping"),
                   count);
}

public void pong(int count) {
    doTurnBasedWork(pongTurn, pingTurn,
                   () -> System.out.print("Pong"),
                   count);
}
```

### Usage

```java
PingPongGameSemaphore game = new PingPongGameSemaphore();

Thread pingThread = new Thread(() -> game.ping(10));
Thread pongThread = new Thread(() -> game.pong(10));

pingThread.start();
pongThread.start();

pingThread.join();
pongThread.join();
```

**Output:** `PingPongPingPongPingPongPingPongPingPongPingPongPingPongPingPongPingPongPingPong`

---

## Execution Timeline

```
═══════════════════════════════════════════════════════════════════

Time  Thread A (Ping)                Thread B (Pong)
────  ──────────────────────────────  ──────────────────────────
 0    pingTurn.acquire() ✓            pongTurn.acquire() ✗ WAIT
 1    Print "Ping"                    [Waiting]
 2    pongTurn.release()              [Woken up!]
 3    [Waiting]                       pongTurn.acquire() ✓
 4    [Waiting]                       Print "Pong"
 5    [Waiting]                       pingTurn.release()
 6    pingTurn.acquire() ✓            [Waiting]
 7    Print "Ping"                    [Waiting]
 8    pongTurn.release()              [Woken up!]
 9    [Waiting]                       pongTurn.acquire() ✓
10    [Waiting]                       Print "Pong"
11    [Waiting]                       pingTurn.release()
...   (pattern repeats)                (pattern repeats)

═══════════════════════════════════════════════════════════════════
```

---

## Why Semaphore is Better for This Problem

| Aspect | wait/notify | Semaphore |
| :--- | :--- | :--- |
| **Simplicity** | ⭐⭐⭐ Complex | ⭐⭐⭐⭐⭐ Very simple |
| **Lock Required** | Yes (`synchronized`) | No (built-in) |
| **Spurious Wakeups** | Must handle with while | Automatic handling |
| **State Management** | Manual | Built-in (permits) |
| **Easy to Misuse** | Yes (many pitfalls) | No (hard to get wrong) |
| **Code Length** | More lines | Fewer lines |
| **Readability** | Medium | High |
| **Race Conditions** | Possible with mistakes | Very rare |

---

## Common Patterns with Semaphore

### Pattern 1: Simple Mutex (Binary Semaphore)
```java
Semaphore mutex = new Semaphore(1);

// Thread A
mutex.acquire();
// critical section
mutex.release();

// Thread B
mutex.acquire();
// critical section
mutex.release();
```

### Pattern 2: Producer-Consumer
```java
Semaphore empty = new Semaphore(BUFFER_SIZE);    // Slots available
Semaphore full = new Semaphore(0);               // Items available
Lock lock = new ReentrantLock();

// Producer
empty.acquire();
lock.lock();
try {
    queue.add(item);
} finally {
    lock.unlock();
}
full.release();

// Consumer
full.acquire();
lock.lock();
try {
    queue.remove();
} finally {
    lock.unlock();
}
empty.release();
```

### Pattern 3: Pool of Resources
```java
Semaphore poolSize = new Semaphore(10);  // 10 resources available

poolSize.acquire();      // Get a resource
try {
    useResource();
} finally {
    poolSize.release();  // Return resource
}
```

### Pattern 4: Rate Limiting
```java
Semaphore rateLimit = new Semaphore(100);  // 100 requests allowed

for (int i = 0; i < totalRequests; i++) {
    rateLimit.acquire();  // Block if limit reached
    submitRequest();
    new Thread(() -> {
        try {
            processRequest();
        } finally {
            rateLimit.release();
        }
    }).start();
}
```

---

## When to Use Semaphore

### ✅ Use Semaphore When:
- Simple turn-taking (2 threads)
- Need to limit concurrent access to N resources
- Rate limiting
- Simple producer-consumer (with explicit lock for the queue)
- You want simple, clear, hard-to-misuse code

### ❌ Don't Use Semaphore When:
- Complex conditions (use `ReentrantLock` + `Condition`)
- Need to wait for multiple conditions
- Need fairness guarantees (use `ReentrantLock`)
- Need tryLock with timeout (use `ReentrantLock`)

---

## Comparison with Other Approaches

### Approach 1: Boolean Flag with wait/notify
```java
private boolean pingTurn = true;
private final Object lock = new Object();

synchronized(lock) {
    while (!pingTurn) lock.wait();
    action.run();
    pingTurn = false;  // ← Must manage state
    lock.notifyAll();
}
```
**Issues:** Confusing boolean, manual state, race conditions possible

### Approach 2: Enum with wait/notify
```java
private Turn turn = Turn.PING;
private final Object lock = new Object();

synchronized(lock) {
    while (turn != Turn.PING) lock.wait();
    action.run();
    turn = Turn.PONG;  // ← Clearer state
    lock.notifyAll();
}
```
**Better:** Clearer state, but still manual coordination

### Approach 3: Semaphore (RECOMMENDED)
```java
private Semaphore pingTurn = new Semaphore(1);
private Semaphore pongTurn = new Semaphore(0);

pingTurn.acquire();
action.run();
pongTurn.release();
```
**Best:** Simple, automatic, hard to misuse

---

## Key Insights

1. **Semaphore is a higher-level abstraction** - It builds on primitives like wait/notify but hides complexity
2. **Permits model is intuitive** - Think of them as "tickets" to proceed
3. **No explicit locking needed** - Synchronization is built-in
4. **Hard to misuse** - No spurious wakeup handling needed, no while loops
5. **Perfect for turn-based coordination** - Ideal for simple scheduling problems

---

## Testing Considerations

### Test 1: Correct Alternation
Verify Ping and Pong alternate correctly.

### Test 2: Stress Test (125+ iterations)
With wait/notify, improper implementation deadlocks.
With Semaphore, it works reliably due to permit counting.

### Test 3: No Race Conditions
Verify counts are exact (no skips or duplicates).

### Test 4: Semaphore State
Verify initial permits are correct:
- `pingTurn`: 1 permit (Ping goes first)
- `pongTurn`: 0 permits (Pong waits)

---

## Summary

**Semaphore is the right tool for turn-based coordination because:**

✅ Simple and intuitive (permits model)
✅ Automatic spurious wakeup handling
✅ No explicit locks needed
✅ Hard to misuse
✅ Scales to multiple threads easily
✅ Perfect for resource pooling and rate limiting

**For the PingPongGame specifically:**
- Semaphore is **simpler and clearer** than wait/notify
- No risk of race conditions like with improper synchronized usage
- The permit model directly maps to "whose turn is it?"

**Use this pattern in production code!**
