# Exchanger Pattern: The Double Handshake

Using `Exchanger` for strict turn-taking requires a specific "Double Handshake" pattern to prevent race conditions.

---

## The Concept

`Exchanger` is designed for **Data Swapping**:
> "I have an empty buffer, you have a full buffer. Let's meet and swap."

For **Strict Turn-Taking**, we need to ensure Thread A finishes BEFORE Thread B starts.

### The Naive Approach (Race Condition)

```java
// Thread A
print("Ping");
exchange();  // "I am done"

// Thread B
exchange();  // "Wait for A"
print("Pong");
```

**Problem:** After the exchange, **BOTH** threads are released simultaneously!
- Thread A loops back to print "Ping" (Cycle 2)
- Thread B prints "Pong" (Cycle 1)

They race! You might get `Ping Ping Pong` or `Ping Pong Ping`.

---

## The Solution: Double Handshake

We need two synchronization points per cycle:
1. **Handoff:** "I am done, your turn."
2. **Ack:** "I finished my turn, back to you."

```java
Exchanger<String> exchanger = new Exchanger<>();

// Ping Thread
print("Ping");
exchanger.exchange("PingDone"); // 1. Wake Pong
exchanger.exchange("Wait");     // 2. Wait for Pong to finish

// Pong Thread
exchanger.exchange("Wait");     // 1. Wait for Ping
print("Pong");
exchanger.exchange("PongDone"); // 2. Wake Ping
```

## Execution Flow

```
Time | Ping Thread          | Pong Thread            | State
-----|----------------------|------------------------|----------------
1    | Print "Ping"         | exchange("Wait")       | Pong blocked
2    | exchange("PingDone") | ...                    | MEET & SWAP!
3    | exchange("Wait")     | Print "Pong"           | Ping blocked
4    | ...                  | exchange("PongDone")   | MEET & SWAP!
5    | Loop...              | Loop...                | Reset
```

---

## Pros & Cons

| Aspect | Verdict | Reason |
| :--- | :--- | :--- |
| **Strict Order** | ✅ Yes | Enforced by 2 exchanges |
| **Data Transfer** | ✅ Yes | Can pass data during sync |
| **Garbage** | ✅ Zero | Reuses same objects |
| **Efficiency** | ⚠️ Medium | 4 context switches per cycle |
| **Complexity** | ⚠️ Medium | Double exchange can be confusing |

---

## Best Use Case for Exchanger

Not Ping-Pong! Use it for **Buffer Swapping**:

```java
// Producer
fill(emptyBuffer);
fullBuffer = exchanger.exchange(emptyBuffer);

// Consumer
process(fullBuffer);
emptyBuffer = exchanger.exchange(fullBuffer);
```

This pattern is cleaner because the *data dependency* naturally forces synchronization. Consumer *cannot* process until it receives the full buffer.

For Ping-Pong, **Semaphore** is simpler. But this exercise teaches you how to force serialization using rendezvous primitives! 🎓
