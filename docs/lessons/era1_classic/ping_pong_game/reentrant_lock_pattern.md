# ReentrantLock Pattern: Modern Mutual Exclusion

A robust, flexible alternative to `synchronized` that offers more control and better features for production code.

---

## Overview

In JDK 5, `ReentrantLock` was introduced as a modern replacement for the intrinsic `synchronized` lock. It provides the same basic behavior (mutual exclusion) but adds advanced features.

### The Comparison

**Classic (synchronized):**
```java
synchronized(lock) {
    // ... work ...
    lock.notifyAll();
}
```

**Modern (ReentrantLock):**
```java
lock.lock();
try {
    // ... work ...
    condition.signalAll();
} finally {
    lock.unlock();
}
```

---

## Core Components

### 1. The Lock
```java
ReentrantLock lock = new ReentrantLock();
// or
ReentrantLock fairLock = new ReentrantLock(true); // Fairness policy
```

### 2. The Condition (replaces wait/notify)
```java
Condition condition = lock.newCondition();
```
- `condition.await()` = `lock.wait()`
- `condition.signal()` = `lock.notify()`
- `condition.signalAll()` = `lock.notifyAll()`

---

## Key Advantages

1. **Try-Lock capability**: Can attempt to acquire lock without blocking forever
   ```java
   if (lock.tryLock(1, TimeUnit.SECONDS)) { ... }
   ```

2. **Fairness**: Can guarantee FIFO ordering for threads waiting for the lock
   ```java
   new ReentrantLock(true); // Fair lock
   ```

3. **Multiple Conditions**: Can have multiple wait-sets for one lock
   ```java
   Condition notEmpty = lock.newCondition();
   Condition notFull = lock.newCondition();
   ```

4. **Interruptibility**: Can interrupt a thread waiting for a lock
   ```java
   lock.lockInterruptibly();
   ```

---

## The PingPongGame with ReentrantLock

```java
public class PingPongGameReentrantLock {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition turnCondition = lock.newCondition();
    private enum Turn { PING, PONG }
    private Turn turn = Turn.PING;

    private void doTurnBasedWork(Turn myTurn, Turn nextTurn, Runnable action) {
        lock.lock();  // Explicit acquire
        try {
            while (turn != myTurn) {
                turnCondition.await();  // Explicit wait
            }
            
            action.run();
            
            turn = nextTurn;
            turnCondition.signalAll();  // Explicit signal
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();  // CRITICAL: Always unlock in finally
        }
    }
}
```

---

## Critical Pattern: try-finally

With `synchronized`, the JVM automatically releases the lock if an exception occurs.
With `ReentrantLock`, **you must manually release it**.

### ❌ WRONG (Dangerous)
```java
lock.lock();
doWork();  // If this throws exception...
lock.unlock();  // ...this never happens! Lock leaks!
```

### ✅ CORRECT (Safe)
```java
lock.lock();
try {
    doWork();
} finally {
    lock.unlock();  // Guaranteed to execute
}
```

---

## Multiple Conditions Example (Producer-Consumer)

This is where ReentrantLock shines. You can signal *only* consumers, or *only* producers.

```java
ReentrantLock lock = new ReentrantLock();
Condition notFull = lock.newCondition();
Condition notEmpty = lock.newCondition();

// Producer
lock.lock();
try {
    while (queue.isFull()) {
        notFull.await();  // Wait for space
    }
    queue.add(item);
    notEmpty.signal();    // Wake ONLY consumers
} finally {
    lock.unlock();
}

// Consumer
lock.lock();
try {
    while (queue.isEmpty()) {
        notEmpty.await(); // Wait for items
    }
    queue.remove();
    notFull.signal();     // Wake ONLY producers
} finally {
    lock.unlock();
}
```

With `synchronized`, `notifyAll()` wakes everyone—producers AND consumers—causing inefficient context switches.

---

## Summary

| Feature | synchronized | ReentrantLock |
| :--- | :--- | :--- |
| **Syntax** | Block-based | Explicit lock/unlock |
| **Safety** | Auto-release | Manual release (finally) |
| **Wait/Notify** | Monitor methods | Condition objects |
| **Wait Sets** | 1 per object | Multiple per lock |
| **Fairness** | No guarantee | Configurable |
| **Timeouts** | Wait only | Try-lock with timeout |
| **Performance** | Optimized by JVM | Slightly higher overhead |

**Recommendation:** Use `ReentrantLock` for production code when you need multiple conditions, fairness, or try-lock capabilities. For simple exclusions, `synchronized` is still acceptable.
