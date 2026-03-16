# CyclicBarrier Pattern: The Relay Race

Using `CyclicBarrier` for turn-taking is unusual but instructive. It demonstrates how to coordinate "lock-step" execution.

---

## The Concept

`CyclicBarrier` is normally used for **Group Synchronization**:
> "Everyone meet at the coffee machine at 10:00. No one leaves until everyone is there."

For **Turn-Taking (Ping-Pong)**, we need a **Relay Race**:
> "I run to Checkpoint A. You wait at Checkpoint A. When I arrive, I pass the baton, you run to Checkpoint B."

To achieve this with Barriers, we need **TWO barriers**:
1. `afterPing`: "Ping has finished"
2. `afterPong`: "Pong has finished"

---

## Implementation Pattern

```java
CyclicBarrier afterPing = new CyclicBarrier(2);
CyclicBarrier afterPong = new CyclicBarrier(2);

// Ping Thread
print("Ping");
afterPing.await();  // Wait for Pong to acknowledge
afterPong.await();  // Wait for Pong to finish

// Pong Thread
afterPing.await();  // Wait for Ping to finish
print("Pong");
afterPong.await();  // Signal I am finished
```

## Execution Flow

```
Time | Ping Thread          | Pong Thread            | State
-----|----------------------|------------------------|----------------
1    | Print "Ping"         | afterPing.await()      | Pong waiting
2    | afterPing.await()    | ...                    | Barrier 1 Breaks!
3    | afterPong.await()    | Print "Pong"           | Ping waiting
4    | ...                  | afterPong.await()      | Barrier 2 Breaks!
5    | Loop...              | Loop...                | Reset
```

---

## Why Two Barriers?

If we used only **one barrier**:

```java
// Thread A
print("Ping");
barrier.await();

// Thread B
print("Pong");
barrier.await();
```

**Race Condition:** Thread B might be faster!
- Thread B starts → Prints "Pong"
- Thread A starts → Prints "Ping"
- Both reach barrier.

Result: `PongPing` instead of `PingPong`.

By using `afterPing` barrier first, we force Pong to wait until Ping arrives (and has printed).

---

## Pros & Cons

| Aspect | Verdict | Reason |
| :--- | :--- | :--- |
| **Strict Order** | ✅ Yes | Enforced by 2 barriers |
| **Efficiency** | ⚠️ Medium | 4 context switches per cycle |
| **Complexity** | ⚠️ High | Harder to understand than Semaphore |
| **Intended Use** | ❌ No | Barriers are for groups, not turns |

---

## Best Use Case for CyclicBarrier

Not Ping-Pong! Use it for **Parallel Calculation**:

```java
// Matrix Multiplication
// 4 threads calculate 4 rows
rowCalculator.run();
barrier.await(); // Wait for all 4 rows
combineResults(); // Only runs when all 4 are done
```

For Ping-Pong, **Semaphore** or **Exchanger** is better. But this exercise proves you understand how to manipulate synchronization primitives! 🎓
