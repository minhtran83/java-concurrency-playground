# Exercise: The Ping-Pong Game (Strict Turn-Taker)

A foundational concurrency exercise that teaches thread coordination, synchronization primitives, and design pattern evolution.

---

## Overview

Two threads must alternate printing "Ping" and "Pong" in strict order.

**Expected Output (5 iterations):**
```
PingPongPingPongPingPongPingPongPingPong
```

---

## Learning Path: Multiple Implementations

This exercise is implemented using **4 different approaches**, showing the evolution from basic to advanced:

### 1. [Wait/Notify Pattern (Coordination)](monitor_pattern_coordination.md)
**Approach:** `synchronized`, `wait()`, `notify()`, boolean flag

```java
synchronized(lock) {
    while (!pingTurn) lock.wait();
    System.out.println("Ping");
    pingTurn = false;
    lock.notifyAll();
}
```

**Lesson:** Foundational understanding of monitor locks and inter-thread signaling
**Difficulty:** ⭐⭐ (Intermediate)

---

### 2. [Semaphore Pattern (Permits)](semaphore_pattern.md)
**Approach:** `Semaphore`, permits-based coordination

```java
pingTurn.acquire();
System.out.println("Ping");
pongTurn.release();
```

**Lesson:** Simpler, cleaner alternative to wait/notify
**Difficulty:** ⭐ (Beginner - Simpler than wait/notify!)

---

### 3. [Enum State Pattern (Better Alternatives)](monitor_pattern_alternatives.md)
**Approach:** Enum instead of boolean, multiple coordination options

```java
enum Turn { PING, PONG }
while (turn != Turn.PING) lock.wait();
```

**Lesson:** Self-documenting code, clearer than boolean flags
**Difficulty:** ⭐⭐ (Intermediate)

---

### 4. [ReentrantLock Pattern (Modern)](reentrant_lock_pattern.md)
**Approach:** `ReentrantLock` and `Condition`

```java
lock.lock();
try {
    while (turn != PING) condition.await();
    // ...
} finally {
    lock.unlock();
}
```

**Lesson:** Explicit locking, multiple conditions, fairness
**Difficulty:** ⭐⭐⭐ (Intermediate)

---

### 5. [CyclicBarrier Pattern (The Relay Race)](cyclic_barrier_pattern.md)
**Approach:** Two `CyclicBarrier` objects for hand-offs

```java
afterPing.await(); // Wait for Ping
print("Pong");
afterPong.await(); // Signal Pong done
```

**Lesson:** Using barriers for lock-step coordination
**Difficulty:** ⭐⭐ (Intermediate)

---

### 6. [Exchanger Pattern (Double Handshake)](exchanger_pattern.md)
**Approach:** Two `exchange()` calls per cycle for strict ordering

```java
print("Ping");
exchange("Turn"); // Hand off
exchange("Wait"); // Wait for return
```

**Lesson:** Using rendezvous primitives for serialization
**Difficulty:** ⭐⭐ (Intermediate)

---

### 7. [Generic/DRY Pattern (Advanced Refactoring)](monitor_pattern_generic.md)
**Approach:** Extract common pattern, use lambdas for actions

```java
private void turn(Turn myTurn, Turn nextTurn, Runnable action) {
    synchronized(lock) {
        while (turn != myTurn) lock.wait();
        action.run();
        turn = nextTurn;
        lock.notifyAll();
    }
}
```

**Lesson:** Eliminate duplication, SOLID principles, design patterns
**Difficulty:** ⭐⭐⭐ (Advanced)

---

## Recommended Reading Order

### For Learning Concurrency (First Time)
1. Start with **Semaphore Pattern** - Simplest approach
2. Then **Wait/Notify Pattern** - Understand foundations
3. Then **Enum State Pattern** - See improvements
4. Finally **Generic/DRY Pattern** - Advanced design

### For Understanding Evolution
1. **Wait/Notify Pattern** - Classic foundation
2. **Enum State Pattern** - Better state management
3. **Generic/DRY Pattern** - Modern refactoring
4. **Semaphore Pattern** - Alternative approach

### For Interview Preparation
1. **Wait/Notify Pattern** - Show you know the basics
2. **Semaphore Pattern** - Show you know alternatives
3. **Generic/DRY Pattern** - Show you understand design

---

## Reference Implementations

| Version | File | Location | APIs Used |
| :--- | :--- | :--- | :--- |
| **Wait/Notify** | `PingPongGame.java` | `com.lesson.concurrency.era1_classic.ping_pong_game` | `synchronized`, `wait()`, `notify()` |
| **Semaphore** | `PingPongGameSemaphore.java` | `com.lesson.concurrency.era1_classic.ping_pong_game` | `Semaphore`, `acquire()`, `release()` |

---

## Tests

Both implementations include comprehensive test suites:

- `PingPongGameTest.java` - 7 test cases for wait/notify version
- `PingPongGameSemaphoreTest.java` - 8 test cases for semaphore version

**Test Coverage:**
- ✅ Basic alternation
- ✅ Single iteration
- ✅ Multiple iterations (10, 125+)
- ✅ No deadlock
- ✅ Correct sequencing
- ✅ Stress tests

---

## Key Concepts Taught

### Synchronization Primitives
- `synchronized` keyword (intrinsic locks)
- `wait()` and `notify()` (inter-thread signaling)
- `Semaphore` (permit-based coordination)
- Spurious wakeups and while loops

### Design Patterns
- Monitor Pattern (classic)
- Semaphore Pattern (alternative)
- Enum-based State (clarity)
- Generic/DRY Pattern (advanced)

### Best Practices
- Self-documenting code (Enum over boolean)
- Eliminating duplication (generic methods)
- Thread naming for debugging
- Comprehensive testing with timeouts

---

## Common Pitfalls

### ❌ Using `if` instead of `while`
```java
if (!pingTurn) lock.wait();  // WRONG - spurious wakeup will break
```

### ✅ Use `while` loop
```java
while (!pingTurn) lock.wait();  // CORRECT
```

---

### ❌ Calling `notify()` outside synchronized
```java
synchronized(lock) { ... }
lock.notifyAll();  // ERROR - not holding lock!
```

### ✅ Call inside synchronized
```java
synchronized(lock) {
    ...
    lock.notifyAll();  // CORRECT
}
```

---

### ❌ Not handling InterruptedException
```java
lock.wait();  // Ignores exception
```

### ✅ Restore interrupt status
```java
try {
    lock.wait();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

---

## Progression Difficulty

| Step | Concept | Difficulty |
| :--- | :--- | :--- |
| 1 | Semaphore (simplest) | ⭐ |
| 2 | Wait/Notify basics | ⭐⭐ |
| 3 | Enum state (clearer) | ⭐⭐ |
| 4 | Generic/DRY (advanced) | ⭐⭐⭐ |

---

## Practice Exercise

Your implementation should go in:
```
src/main/java/com/practice/concurrency/era1_classic/ping_pong_game/
```

Start with one approach, then try others:
1. Implement with wait/notify
2. Refactor with Enum
3. Try Semaphore
4. Extract to generic method

See `docs/practice/era1_classic/ping_pong_game/README.md` for detailed guidance.

---

## Next Exercises (Related Patterns)

- **Reader-Writer Cache** - Multiple readers, exclusive writer (Semaphore pattern)
- **Bounded Buffer** - Producer-consumer with wait/notify, Locks, and BlockingQueue
- **Dining Philosophers** - Deadlock prevention and resource allocation
- **Custom Thread Pool** - Executor implementation with queue coordination

---

## Key Takeaways

After mastering this exercise, you should understand:

✅ How monitor locks work (`synchronized`)
✅ Why `wait()` and `notify()` are complex (spurious wakeups)
✅ How `Semaphore` provides a simpler alternative
✅ Why Enum-based state is clearer than boolean
✅ How to extract patterns and eliminate duplication
✅ The evolution of concurrency APIs
✅ How to write proper concurrent code tests

---

**Ready to learn? Start with the [Semaphore Pattern](semaphore_pattern.md)!** 🚀
