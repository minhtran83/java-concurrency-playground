# Monitor Pattern: Better Ways to Write 2-Thread Coordination

The basic pattern with opposite boolean conditions works, but there are several **cleaner, safer, and more maintainable** approaches.

---

## Problem with the Basic Pattern

```java
// Thread 1
synchronized(lock) {
    while (!condition) lock.wait();
    // Do work
    condition = false;           // ← Confusing: what does false mean?
    lock.notifyAll();
}

// Thread 2
synchronized(lock) {
    while (condition) lock.wait();  // ← Opposite logic - error-prone
    // Do work
    condition = true;            // ← Does true mean "A's turn" or "B's turn"?
    lock.notifyAll();
}
```

**Problems:**
1. ⚠️ **Confusing semantics** - What does `true/false` really mean?
2. ⚠️ **Easy to make mistakes** - Inverting conditions is error-prone
3. ⚠️ **Hard to extend** - Adding a 3rd thread requires major refactoring
4. ⚠️ **Poor code clarity** - Reader has to trace logic carefully

---

## Pattern 1: Named State Enum (RECOMMENDED)

Instead of boolean, use an enum with clear semantics:

```java
public class PingPongGameWithEnum {
    private final Object lock = new Object();
    private enum Turn { PING, PONG }
    private Turn turn = Turn.PING;
    private volatile boolean running = true;

    public void ping(int count) {
        for (int i = 0; i < count; i++) {
            synchronized (lock) {
                while (turn != Turn.PING && running) {
                    lock.wait();
                }
                if (!running) break;
                
                System.out.print("Ping");
                turn = Turn.PONG;  // ← Crystal clear!
                lock.notifyAll();
            }
        }
    }

    public void pong(int count) {
        for (int i = 0; i < count; i++) {
            synchronized (lock) {
                while (turn != Turn.PONG && running) {
                    lock.wait();
                }
                if (!running) break;
                
                System.out.print("Pong");
                turn = Turn.PING;  // ← Crystal clear!
                lock.notifyAll();
            }
        }
    }
}
```

**Advantages:**
- ✅ Self-documenting code
- ✅ Impossible to confuse which thread is which
- ✅ Easy to extend to 3+ threads
- ✅ Compiler helps catch mistakes

**Disadvantages:**
- More verbose than boolean

---

## Pattern 2: Extract Wait/Act Logic (RECOMMENDED)

Separate the waiting logic from the work:

```java
public class PingPongGameWithMethods {
    private final Object lock = new Object();
    private enum Turn { PING, PONG }
    private Turn turn = Turn.PING;

    public void ping(int count) {
        for (int i = 0; i < count; i++) {
            waitForMyTurn(Turn.PING);
            doWork("Ping");
            passTurn(Turn.PONG);
        }
    }

    public void pong(int count) {
        for (int i = 0; i < count; i++) {
            waitForMyTurn(Turn.PONG);
            doWork("Pong");
            passTurn(Turn.PING);
        }
    }

    private void waitForMyTurn(Turn myTurn) {
        synchronized (lock) {
            while (turn != myTurn) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void doWork(String name) {
        System.out.print(name);
    }

    private void passTurn(Turn nextTurn) {
        synchronized (lock) {
            turn = nextTurn;
            lock.notifyAll();
        }
    }
}
```

**Advantages:**
- ✅ Very clear intent
- ✅ Each method has single responsibility
- ✅ Easy to test each part
- ✅ Reusable logic

**Disadvantages:**
- More method calls (minor performance cost)

---

## Pattern 3: ReentrantLock + Condition (MODERN, RECOMMENDED)

This is the JDK 5+ way - cleaner, more explicit:

```java
import java.util.concurrent.locks.*;

public class PingPongGameWithReentrantLock {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition myTurn = lock.newCondition();
    
    private enum Turn { PING, PONG }
    private Turn turn = Turn.PING;

    public void ping(int count) {
        for (int i = 0; i < count; i++) {
            lock.lock();
            try {
                while (turn != Turn.PING) {
                    myTurn.await();  // ← More explicit than wait()
                }
                System.out.print("Ping");
                turn = Turn.PONG;
                myTurn.signalAll();  // ← More explicit than notifyAll()
            } finally {
                lock.unlock();  // ← Guaranteed to unlock
            }
        }
    }

    public void pong(int count) {
        for (int i = 0; i < count; i++) {
            lock.lock();
            try {
                while (turn != Turn.PONG) {
                    myTurn.await();
                }
                System.out.print("Pong");
                turn = Turn.PING;
                myTurn.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}
```

**Advantages:**
- ✅ More explicit (clear intent)
- ✅ try-finally guarantees unlock
- ✅ Can wait with timeout: `await(1, TimeUnit.SECONDS)`
- ✅ Better for virtual threads (JDK 21+)
- ✅ Can have multiple conditions
- ✅ Try-lock with `tryLock()`

**Disadvantages:**
- Slightly more verbose than synchronized
- Must remember try-finally pattern

---

## Pattern 4: Multiple Conditions (For Complex Scenarios)

When you have multiple wait-sets:

```java
import java.util.concurrent.locks.*;

public class ProducerConsumerWithConditions {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    
    private final Queue<Item> queue = new LinkedList<>();
    private final int capacity = 10;

    public void produce(Item item) throws InterruptedException {
        lock.lock();
        try {
            // Wait until queue is not full
            while (queue.size() == capacity) {
                notFull.await();  // ← Specific condition
            }
            
            queue.add(item);
            System.out.println("Produced: " + item);
            
            notEmpty.signalAll();  // ← Wake consumers (not full producers)
        } finally {
            lock.unlock();
        }
    }

    public Item consume() throws InterruptedException {
        lock.lock();
        try {
            // Wait until queue is not empty
            while (queue.isEmpty()) {
                notEmpty.await();  // ← Specific condition
            }
            
            Item item = queue.remove();
            System.out.println("Consumed: " + item);
            
            notFull.signalAll();  // ← Wake producers (not empty consumers)
            return item;
        } finally {
            lock.unlock();
        }
    }
}
```

**Advantages:**
- ✅ Multiple wait-sets = Better wakeup efficiency
- ✅ Producers don't wake producers
- ✅ Consumers don't wake consumers
- ✅ Scales to N producers + M consumers

---

## Pattern 5: Semaphore (For Simple Cases)

Use if you just need one thread to go at a time:

```java
import java.util.concurrent.*;

public class PingPongGameWithSemaphore {
    private final Semaphore pingTurn = new Semaphore(1);   // Ping starts
    private final Semaphore pongTurn = new Semaphore(0);   // Pong waits

    public void ping(int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            pingTurn.acquire();  // ← Wait if Ping's turn is not available
            System.out.print("Ping");
            pongTurn.release();  // ← Release Pong's turn
        }
    }

    public void pong(int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            pongTurn.acquire();  // ← Wait if Pong's turn is not available
            System.out.print("Pong");
            pingTurn.release();  // ← Release Ping's turn
        }
    }
}
```

**Advantages:**
- ✅ Extremely simple and clear
- ✅ No explicit boolean state
- ✅ Hard to misuse
- ✅ Perfect for turn-based coordination

**Disadvantages:**
- Only works for 2 parties easily
- Less flexible than ReentrantLock

---

## Pattern 6: Exchanger (For Handoff Patterns)

If threads are swapping data:

```java
import java.util.concurrent.*;

public class PingPongGameWithExchanger {
    private final Exchanger<String> exchanger = new Exchanger<>();

    public void ping(int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            System.out.print("Ping");
            exchanger.exchange("Ping");  // ← Handoff + wait for response
        }
    }

    public void pong(int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            exchanger.exchange("Pong");  // ← Receive + send
            System.out.print("Pong");
        }
    }
}
```

**Advantages:**
- ✅ Perfect for buffer swapping patterns
- ✅ Very clear intent (handoff)
- ✅ Automatic synchronization
- ✅ Great for pipeline designs

**Disadvantages:**
- Only for exactly 2 threads
- Specific use case

---

## Comparison Table

| Pattern | Clarity | Simplicity | Flexibility | Best For |
| :--- | :--- | :--- | :--- | :--- |
| **Boolean Flag** | ⭐⭐ | ⭐⭐⭐ | ⭐ | Learning only |
| **Enum Flag** | ⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐ | 2-3 threads |
| **Extracted Methods** | ⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐ | Testable code |
| **ReentrantLock** | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐ | Production code |
| **Multiple Conditions** | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ | Producer-Consumer |
| **Semaphore** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ | Simple turn-taking |
| **Exchanger** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐ | Buffer swapping |

---

## Recommendations by Scenario

### Learning/Understanding Concurrency
→ Use **Enum Flag** pattern
- Still teaches wait/notify
- Much clearer than boolean
- Easier to debug

### Production Code
→ Use **ReentrantLock + Condition**
- Industry standard
- More explicit
- Better for virtual threads
- Try-finally safety

### Simple Turn-Taking (2 threads)
→ Use **Semaphore**
- Impossible to get wrong
- Crystal clear intent
- No state to manage

### Complex Coordination (N producers, M consumers)
→ Use **Multiple Conditions**
- Efficient wakeups
- Scales well
- Each condition has clear purpose

### Buffer Swapping Pipeline
→ Use **Exchanger**
- Perfect semantic match
- No manual state management
- Built for this pattern

### Teaching/Interview
→ Use **Enum Flag** OR **Semaphore**
- Shows understanding
- Clear thinking
- Easy to explain

---

## Why Each Pattern is Better

### Enum vs Boolean
```java
// Boolean - What does this mean?
condition = true;  // ← Thread A's turn? Or B's? Or something else?

// Enum - Crystal clear
turn = Turn.PING;  // ← Explicit
```

### Extracted Methods vs Inline
```java
// Inline - Hard to follow
synchronized(lock) {
    while (turn != Turn.PING) lock.wait();
    System.out.print("Ping");
    turn = Turn.PONG;
    lock.notifyAll();
}

// Extracted - Clear intent
waitForMyTurn(Turn.PING);
doWork("Ping");
passTurn(Turn.PONG);
```

### ReentrantLock vs Synchronized
```java
// Synchronized - Exception risk
synchronized(lock) {
    lock.wait();
    doWork();  // ← If exception, lock not released in finally
}

// ReentrantLock - Safe
lock.lock();
try {
    condition.await();
    doWork();
} finally {
    lock.unlock();  // ← Guaranteed to unlock
}
```

### Semaphore vs Manual State
```java
// Manual - Easy to mess up
synchronized(lock) {
    while (turn != Turn.PING) lock.wait();
    turn = Turn.PONG;
    lock.notifyAll();
}

// Semaphore - Can't get wrong
pingTurn.acquire();
pongTurn.release();
```

---

## Summary & Recommendations

**For Learning:**
1. Start with **Enum Flag** (much clearer than boolean)
2. Then learn **ReentrantLock + Condition** (modern style)
3. Understand **Semaphore** (simple and elegant)

**For Production:**
1. **ReentrantLock + Condition** (standard)
2. **Semaphore** (if appropriate)
3. **Avoid boolean flags** (confusing)

**For Interviews:**
1. Show **Enum Flag** pattern (shows thinking)
2. Mention **Semaphore** (elegant alternative)
3. Discuss **ReentrantLock** (modern approach)

The key takeaway: **Never use a simple boolean flag for this pattern.** Use Enum, Semaphore, or ReentrantLock instead. They're all clearer and less error-prone.
