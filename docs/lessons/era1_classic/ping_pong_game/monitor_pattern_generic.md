# Monitor Pattern: Generic/DRY Approach with Lambdas

The enum-based pattern is clear, but the code for Thread A and Thread B is nearly identical—just with reversed state logic. We can eliminate this duplication using generics and lambdas.

---

## The Problem with Code Duplication

```java
// Thread 1 (Ping)
public void ping(int count) {
    for (int i = 0; i < count; i++) {
        synchronized (lock) {
            while (turn != Turn.PING && running) {
                lock.wait();
            }
            if (!running) break;
            System.out.print("Ping");
            turn = Turn.PONG;  // ← Change to PONG
            lock.notifyAll();
        }
    }
}

// Thread 2 (Pong)
public void pong(int count) {
    for (int i = 0; i < count; i++) {
        synchronized (lock) {
            while (turn != Turn.PONG && running) {  // ← Reversed!
                lock.wait();
            }
            if (!running) break;
            System.out.print("Pong");
            turn = Turn.PING;  // ← Change to PING (reversed)
            lock.notifyAll();
        }
    }
}
```

**The duplication:**
- Structure is identical
- Only differences: `PING` ↔ `PONG` and state changes are reversed
- Hard to maintain (bug in one might not exist in other)
- Violates DRY principle

---

## Solution 1: Extract to Generic Method (RECOMMENDED)

```java
public class PingPongGameGeneric {
    private final Object lock = new Object();
    private enum Turn { PING, PONG }
    private Turn turn = Turn.PING;
    private volatile boolean running = true;

    /**
     * Generic method to handle turn-based coordination.
     * 
     * @param myTurn The turn this thread should execute on
     * @param nextTurn The turn to hand off to
     * @param action The action to execute (work to do)
     * @param count How many times to repeat
     */
    private void doTurnBasedWork(Turn myTurn, Turn nextTurn, 
                                  Runnable action, int count) 
            throws InterruptedException {
        for (int i = 0; i < count; i++) {
            synchronized (lock) {
                // Wait for my turn
                while (turn != myTurn && running) {
                    lock.wait();
                }
                if (!running) break;
                
                // Do the work
                action.run();
                
                // Pass the turn
                turn = nextTurn;
                lock.notifyAll();
            }
        }
    }

    // Now the public methods are trivial
    public void ping(int count) throws InterruptedException {
        doTurnBasedWork(Turn.PING, Turn.PONG, 
                       () -> System.out.print("Ping"), count);
    }

    public void pong(int count) throws InterruptedException {
        doTurnBasedWork(Turn.PONG, Turn.PING, 
                       () -> System.out.print("Pong"), count);
    }
}
```

**Advantages:**
- ✅ No code duplication
- ✅ Single source of truth for synchronization logic
- ✅ Easy to test the core logic
- ✅ Public methods are crystal clear
- ✅ Lambdas make it concise
- ✅ Easy to add a 3rd thread

---

## Solution 2: Even More Generic - Parameterized Worker

For more complex scenarios:

```java
public class PingPongGameWithWorker {
    private final Object lock = new Object();
    private enum Turn { PING, PONG }
    private Turn turn = Turn.PING;
    private volatile boolean running = true;

    /**
     * Worker interface for turn-based tasks.
     */
    @FunctionalInterface
    interface TurnWorker {
        void execute();
    }

    /**
     * Generic turn-based coordinator.
     */
    private void coordinateTurns(Turn myTurn, Turn nextTurn, 
                                  TurnWorker worker, int iterations) 
            throws InterruptedException {
        for (int i = 0; i < iterations; i++) {
            synchronized (lock) {
                // Wait for my turn
                while (turn != myTurn && running) {
                    lock.wait();
                }
                if (!running) break;
                
                // Execute work
                worker.execute();
                
                // Hand off
                turn = nextTurn;
                lock.notifyAll();
            }
        }
    }

    public void ping(int count) throws InterruptedException {
        coordinateTurns(Turn.PING, Turn.PONG, 
                       () -> System.out.print("Ping"), count);
    }

    public void pong(int count) throws InterruptedException {
        coordinateTurns(Turn.PONG, Turn.PING, 
                       () -> System.out.print("Pong"), count);
    }
}
```

---

## Solution 3: Builder Pattern (For Complex Configuration)

```java
public class PingPongGameWithBuilder {
    private final Object lock = new Object();
    private enum Turn { PING, PONG }
    private Turn turn = Turn.PING;
    private volatile boolean running = true;

    /**
     * Builder for turn-based coordination.
     */
    public class TurnCoordinator {
        private final Turn myTurn;
        private final Turn nextTurn;
        private Runnable action = () -> {};
        private String name = "Worker";

        public TurnCoordinator(Turn myTurn, Turn nextTurn) {
            this.myTurn = myTurn;
            this.nextTurn = nextTurn;
        }

        public TurnCoordinator withAction(Runnable action) {
            this.action = action;
            return this;
        }

        public TurnCoordinator withName(String name) {
            this.name = name;
            return this;
        }

        public void execute(int count) throws InterruptedException {
            for (int i = 0; i < count; i++) {
                synchronized (lock) {
                    while (turn != myTurn && running) {
                        lock.wait();
                    }
                    if (!running) break;
                    
                    action.run();
                    turn = nextTurn;
                    lock.notifyAll();
                }
            }
        }
    }

    public void ping(int count) throws InterruptedException {
        new TurnCoordinator(Turn.PING, Turn.PONG)
            .withName("Ping")
            .withAction(() -> System.out.print("Ping"))
            .execute(count);
    }

    public void pong(int count) throws InterruptedException {
        new TurnCoordinator(Turn.PONG, Turn.PING)
            .withName("Pong")
            .withAction(() -> System.out.print("Pong"))
            .execute(count);
    }
}
```

**Advantages:**
- ✅ Very fluent, readable
- ✅ Easy to extend with more configuration
- ✅ Good for complex scenarios

---

## Solution 4: Strategy Pattern with Thread Registry

For handling N threads (not just 2):

```java
public class GenericTurnCoordinator {
    private final Object lock = new Object();
    private volatile String currentTurn;
    private volatile boolean running = true;
    private final Map<String, String> transitions;  // Map from turn to next turn

    public GenericTurnCoordinator(String initialTurn, 
                                   Map<String, String> transitions) {
        this.currentTurn = initialTurn;
        this.transitions = transitions;
    }

    /**
     * Execute work when it's your turn.
     */
    public void executeOnMyTurn(String myTurn, Runnable action, 
                                int iterations) throws InterruptedException {
        String nextTurn = transitions.get(myTurn);
        
        for (int i = 0; i < iterations; i++) {
            synchronized (lock) {
                // Wait for my turn
                while (!currentTurn.equals(myTurn) && running) {
                    lock.wait();
                }
                if (!running) break;
                
                // Do work
                action.run();
                
                // Pass to next
                currentTurn = nextTurn;
                lock.notifyAll();
            }
        }
    }

    public void stop() {
        synchronized (lock) {
            running = false;
            lock.notifyAll();
        }
    }
}

// Usage - even supports 3+ threads!
public class MultiThreadExample {
    public static void main(String[] args) throws InterruptedException {
        // Define the turn sequence: A → B → C → A
        Map<String, String> transitions = Map.of(
            "A", "B",
            "B", "C",
            "C", "A"
        );
        
        GenericTurnCoordinator coordinator = 
            new GenericTurnCoordinator("A", transitions);

        Thread threadA = new Thread(() -> {
            try {
                coordinator.executeOnMyTurn("A", 
                    () -> System.out.print("A "), 5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                coordinator.executeOnMyTurn("B", 
                    () -> System.out.print("B "), 5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadC = new Thread(() -> {
            try {
                coordinator.executeOnMyTurn("C", 
                    () -> System.out.print("C "), 5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        threadA.start();
        threadB.start();
        threadC.start();

        threadA.join();
        threadB.join();
        threadC.join();

        // Output: A B C A B C A B C A B C A B C
    }
}
```

**Advantages:**
- ✅ Works for ANY number of threads
- ✅ No duplication
- ✅ Highly maintainable
- ✅ Easy to modify turn sequence

---

## Comparison of Approaches

| Approach | Duplication | Complexity | Flexibility | Best For |
| :--- | :--- | :--- | :--- | :--- |
| **Enum (Original)** | High | Low | Low | Learning |
| **Generic Method** | None | Low | Medium | 2-3 threads |
| **Worker Interface** | None | Low | Medium | Testing |
| **Builder Pattern** | None | Medium | High | Complex config |
| **Strategy + Registry** | None | Medium | Very High | N threads |

---

## Recommended Approach by Use Case

### Learning/Understanding
```java
// Use original enum pattern
// Then refactor to generic method to see the improvement
```

### Production Code (2 threads)
```java
// Use Solution 1: Generic Method
// Simple, clear, no duplication
private void doTurnBasedWork(Turn myTurn, Turn nextTurn, 
                              Runnable action, int count) { ... }
```

### Production Code (N threads)
```java
// Use Solution 4: Generic Coordinator
// Handles any number of threads
coordinator.executeOnMyTurn(myTurn, action, count);
```

### Interview/Technical Assessment
```java
// Start with enum pattern
// Then show refactoring to generic method
// Demonstrates DRY principle understanding
```

---

## The Refactoring Journey

### Step 1: Original (with duplication)
```java
public void ping(int count) {
    synchronized (lock) {
        while (turn != Turn.PING && running) lock.wait();
        System.out.print("Ping");
        turn = Turn.PONG;
        lock.notifyAll();
    }
}

public void pong(int count) {
    synchronized (lock) {
        while (turn != Turn.PONG && running) lock.wait();
        System.out.print("Pong");
        turn = Turn.PING;
        lock.notifyAll();
    }
}
```

### Step 2: Extract common pattern
```java
private void doTurnBasedWork(Turn myTurn, Turn nextTurn, 
                              Runnable action, int count) {
    for (int i = 0; i < count; i++) {
        synchronized (lock) {
            while (turn != myTurn && running) lock.wait();
            if (!running) break;
            action.run();
            turn = nextTurn;
            lock.notifyAll();
        }
    }
}
```

### Step 3: Use the extracted method
```java
public void ping(int count) throws InterruptedException {
    doTurnBasedWork(Turn.PING, Turn.PONG, 
                   () -> System.out.print("Ping"), count);
}

public void pong(int count) throws InterruptedException {
    doTurnBasedWork(Turn.PONG, Turn.PING, 
                   () -> System.out.print("Pong"), count);
}
```

**Result:**
- Public API is crystal clear
- No duplication
- Easy to test
- Easy to extend

---

## Key Insights

1. **Identify the pattern** - The code structure is identical, only parameters differ
2. **Extract to a method** - Generic method handles all cases
3. **Use lambdas** - `Runnable` for simple actions
4. **Pass turn logic as parameters** - `myTurn` and `nextTurn`
5. **Scale up** - For N threads, use generic coordinator with turn map

---

## Summary

The best approaches are:

**For 2 threads:**
```java
// RECOMMENDED
private void doTurnBasedWork(Turn myTurn, Turn nextTurn, 
                              Runnable action, int count)
```

**For N threads:**
```java
// RECOMMENDED
coordinator.executeOnMyTurn(myTurn, action, count);
```

This eliminates all duplication while keeping the code crystal clear and testable. The lambda/Runnable parameter eliminates the need for separate methods—you just pass different actions as needed.
