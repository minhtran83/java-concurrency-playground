# Monitor Pattern: Coordinating 2 Threads with wait()/notify()

A clear structure for coordinating between two threads using monitor-based synchronization with volatile variables and shared state.

## Core Components

### 1. Shared Lock Object (for synchronization)
```java
private final Object lock = new Object();
```
- Intrinsic monitor for `synchronized` blocks
- Used with `wait()` and `notify()`
- Must be `final` and shared between threads

### 2. Volatile State Variable (for thread-safe visibility)
```java
private volatile boolean condition = true;
```
- Controls which thread should proceed
- `volatile` ensures visibility across threads
- Can be checked before entering `synchronized` block

### 3. Shared Data (if needed)
```java
private Data sharedData;
```
- Any data that needs to be safely shared between threads

---

## The Coordination Pattern

### Thread 1: Waits for condition, then acts
```java
synchronized(lock) {
    while (!condition) {      // Check condition (use while, not if!)
        lock.wait();          // Wait if condition not met
    }
    // Do work here
    condition = false;        // Change state
    lock.notifyAll();         // Wake other thread
}                             // Release lock automatically
```

### Thread 2: Waits for opposite condition, then acts
```java
synchronized(lock) {
    while (condition) {       // Check opposite condition
        lock.wait();          // Wait if condition not met
    }
    // Do work here
    condition = true;         // Change state back
    lock.notifyAll();         // Wake other thread
}                             // Release lock automatically
```

---

## Key Synchronization Methods

### `synchronized(lock)` - Acquire Monitor Lock
- Only one thread can hold the lock at a time
- Other threads block trying to acquire the lock
- Automatically releases lock when exiting block

### `lock.wait()` - Release Lock and Wait
- **CRITICAL:** Only callable from within `synchronized` block
- Releases the lock held by current thread
- Thread sleeps until notified
- Automatically re-acquires lock upon waking
- Throws `InterruptedException` if thread is interrupted

### `lock.notifyAll()` - Wake All Waiting Threads
- Wakes all threads currently waiting on this lock
- Waiting threads compete to re-acquire the lock
- Only one woken thread will acquire lock at a time
- Others go back to waiting if their condition still isn't met

### `lock.notify()` - Wake One Random Thread (AVOID)
```java
lock.notify();  // ⚠️ Dangerous - wakes only 1 thread unpredictably
lock.notifyAll();  // ✅ Better - wakes all, they compete fairly
```

---

## Critical: Use `while`, NOT `if`

### ❌ WRONG - Using `if`
```java
synchronized(lock) {
    if (!condition) lock.wait();  // DANGEROUS!
    // Do work - but condition might have changed!
}
```

**Why it's wrong:** 
- **Spurious wakeups** - Thread can wake without being notified
- **Race conditions** - Condition can change between wait and work
- **Logic errors** - Code executes even though condition failed

### ✅ CORRECT - Using `while`
```java
synchronized(lock) {
    while (!condition) lock.wait();  // CORRECT
    // Do work - guaranteed condition is true
}
```

**Why it's right:**
- Rechecks condition after waking up
- Handles spurious wakeups automatically
- Ensures condition is still valid before proceeding

---

## Real World Example: PingPongGame

```java
public class PingPongGame {
    private final Object lock = new Object();
    private boolean pingTurn = true;
    private volatile boolean running = true;

    public void ping(int count) {
        for (int i = 0; i < count; i++) {
            synchronized (lock) {                    // Acquire lock
                while (!pingTurn && running) {       // Wait if not Ping's turn
                    lock.wait();                     // Release lock & sleep
                }
                if (!running) break;
                
                System.out.print("Ping");
                pingTurn = false;                    // Switch to Pong's turn
                lock.notifyAll();                    // Wake Pong thread
            }                                        // Release lock
        }
    }

    public void pong(int count) {
        for (int i = 0; i < count; i++) {
            synchronized (lock) {                    // Acquire lock
                while (pingTurn && running) {        // Wait if Ping's turn
                    lock.wait();                     // Release lock & sleep
                }
                if (!running) break;
                
                System.out.print("Pong");
                pingTurn = true;                     // Switch to Ping's turn
                lock.notifyAll();                    // Wake Ping thread
            }                                        // Release lock
        }
    }

    public void stop() {
        synchronized (lock) {
            running = false;
            lock.notifyAll();  // Wake any waiting threads
        }
    }
}
```

---

## Execution Timeline

```
═══════════════════════════════════════════════════════════════════════

Time  Thread A (Ping)          Thread B (Pong)           State
────  ───────────────────────  ──────────────────────    ──────────
 0    Acquire lock             [Blocked waiting lock]    lock=A
 1    Check: pingTurn? ✓        [Blocked]                lock=A
 2    Print "Ping"             [Blocked]                 lock=A
 3    Set pingTurn = false      [Blocked]                lock=A
 4    notifyAll()              [Blocked]                 lock=A
 5    Release lock             Woken! Acquire lock       lock=B
 6    [Waiting at wait()]      Check: !pingTurn? ✓       lock=B
 7    [Waiting]                Print "Pong"             lock=B
 8    [Waiting]                Set pingTurn = true       lock=B
 9    [Waiting]                notifyAll()              lock=B
10    [Waiting]                Release lock             lock=free
11    Woken! Acquire lock      [Waiting at wait()]      lock=A
12    Check: pingTurn? ✓       [Waiting]                lock=A
13    Print "Ping"             [Waiting]                lock=A
...   (pattern repeats)        (pattern repeats)        ...

═══════════════════════════════════════════════════════════════════════
```

---

## Component Responsibilities

| Component | Purpose | Notes |
| :--- | :--- | :--- |
| **lock** | Provides atomic mutual exclusion | `final Object` - never changes |
| **pingTurn** | Determines whose turn it is | Boolean flag (could be enum for complex states) |
| **synchronized(lock)** | Ensures only 1 thread in critical section | Automatically acquires & releases |
| **while loop** | Safely rechecks condition after waking | Critical for correctness |
| **wait()** | Pauses execution and releases lock | Only in synchronized block |
| **notifyAll()** | Wakes all waiting threads | Safer than notify() |

---

## Common Mistakes & How to Fix Them

### Mistake 1: Using `if` instead of `while`
```java
// ❌ WRONG
synchronized(lock) {
    if (!pingTurn) lock.wait();
    // Problem: Spurious wakeup will skip the check
}

// ✅ CORRECT
synchronized(lock) {
    while (!pingTurn) lock.wait();
    // Rechecks condition after every wakeup
}
```

### Mistake 2: Not synchronizing state check
```java
// ❌ WRONG
if (!pingTurn) {  // Race condition: could change before wait()
    synchronized(lock) {
        lock.wait();
    }
}

// ✅ CORRECT
synchronized(lock) {  // Atomic: check and wait together
    while (!pingTurn) {
        lock.wait();
    }
}
```

### Mistake 3: Calling wait() outside synchronized block
```java
// ❌ WRONG
lock.wait();  // IllegalMonitorStateException!

// ✅ CORRECT
synchronized(lock) {
    lock.wait();  // Only valid inside synchronized
}
```

### Mistake 4: Using notify() instead of notifyAll()
```java
// ⚠️ RISKY
lock.notify();  // Wakes only 1 random thread
                // Other threads might never wake up

// ✅ BETTER
lock.notifyAll();  // Wakes all threads
                   // They compete fairly for the lock
```

### Mistake 5: Forgetting to recheck condition after wait()
```java
// ❌ WRONG - Could have 2 threads in critical section!
synchronized(lock) {
    if (!condition) {  // What if condition became true while waiting?
        lock.wait();
    }
    // ← Another thread could have changed condition here!
    // Both threads proceed incorrectly
}

// ✅ CORRECT
synchronized(lock) {
    while (!condition) {  // Condition is guaranteed true here
        lock.wait();      // Even after spurious wakeup
    }
    // Safe: exactly one thread in critical section
}
```

---

## Why This Pattern is Important

### 1. **Foundation for Understanding Modern APIs**
- `BlockingQueue` uses wait/notify internally
- `ReentrantLock` with `Condition` is the modern version
- Virtual threads in JDK 21+ still rely on these concepts

### 2. **Teaches Real Concurrency Problems**
- Race conditions
- Deadlocks
- Spurious wakeups
- Memory visibility

### 3. **Interview Essential**
- Common machine coding interview question
- Shows understanding of synchronization
- Demonstrates low-level concurrency knowledge

### 4. **Debugging Multithreaded Code**
- Understanding wait/notify helps debug thread hangs
- Explains why `synchronized` isn't enough
- Shows importance of proper state management

---

## When to Use This Pattern

### ✅ Use When:
- You need strict coordination between exactly 2 threads
- You understand all the pitfalls
- You can't use higher-level APIs (rare)
- You're learning concurrency fundamentals

### ❌ Don't Use When:
- You have more than 2 threads (use `Phaser`, `CountDownLatch`)
- You need a queue (use `BlockingQueue`)
- You're in production code (use `ReentrantLock` + `Condition`)
- You need performance (use lock-free structures)

---

## Modern Alternative: ReentrantLock + Condition

The same coordination can be achieved with JDK 5+ style:

```java
public class PingPongGameModern {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean pingTurn = true;

    public void ping(int count) {
        for (int i = 0; i < count; i++) {
            lock.lock();
            try {
                while (!pingTurn) {
                    condition.await();  // Like wait()
                }
                System.out.print("Ping");
                pingTurn = false;
                condition.signalAll();  // Like notifyAll()
            } finally {
                lock.unlock();
            }
        }
    }
    // Similar for pong()...
}
```

**Advantages of ReentrantLock + Condition:**
- More explicit (clearer intent)
- Multiple wait-sets (different conditions)
- Try-lock with timeout
- Better for virtual threads (JDK 21+)

---

## Summary

The monitor pattern for 2-thread coordination requires:

1. **Shared Lock** - `private final Object lock`
2. **State Variable** - `private volatile boolean condition`
3. **Synchronized Block** - `synchronized(lock) { ... }`
4. **While Loop** - Always use while, never if
5. **Wait/Notify** - `lock.wait()` and `lock.notifyAll()`

This pattern is the foundation for understanding all Java concurrency, even though you'll rarely use it in production code (modern APIs are safer and easier).

**Master this, and higher-level constructs become easy!** 🚀
