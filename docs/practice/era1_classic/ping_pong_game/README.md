# Exercise: The Ping-Pong Game (Strict Turn-Taker)

A foundational concurrency exercise that teaches thread coordination, synchronization primitives, and design pattern evolution.

---

## 🎯 Goal
Create two threads, "Ping" and "Pong", that must print their names alternately in strict order.

**Expected Output (5 iterations):**
```
PingPongPingPongPingPongPingPongPingPong
```

---

## 📚 Learning Resources

Before coding, study these lessons to understand the concepts:

### 1. [Coordination Fundamentals](../../../../lessons/era1_classic/ping_pong_game/monitor_pattern_coordination.md)
**Focus:** The `wait()`/`notify()` pattern
- Why `while` loops are essential (spurious wakeups)
- Why `notifyAll()` is safer than `notify()`
- How the monitor lock works

### 2. [Semaphore Pattern](../../../../lessons/era1_classic/ping_pong_game/semaphore_pattern.md)
**Focus:** A simpler alternative
- Using permits for coordination
- Why it's less error-prone than wait/notify
- Perfect for simple turn-taking

### 3. [Better Patterns](../../../../lessons/era1_classic/ping_pong_game/monitor_pattern_alternatives.md)
**Focus:** Enum state & cleaner code
- Why boolean flags are confusing
- Using Enums for self-documenting state
- Comparison of different approaches

### 4. [Advanced Refactoring](../../../../lessons/era1_classic/ping_pong_game/monitor_pattern_generic.md)
**Focus:** DRY & Generics
- Eliminating code duplication
- Using lambdas for actions
- Writing production-grade concurrent code

---

## 💻 Practice Instructions

### Step 1: Implement Basic Version
Create `PingPongGame.java` using `synchronized`, `wait()`, and `notify()`.

**Requirements:**
- Two threads (Ping and Pong)
- Strict alternation
- Configurable iterations
- Correct synchronization (no race conditions)

### Step 2: Refactor with Enum
Create `PingPongGameEnum.java` using an Enum for state.

**Why:**
- Makes state explicit (`Turn.PING` vs `true/false`)
- Easier to debug and understand
- Harder to make logic errors

### Step 3: Try Semaphore
Create `PingPongGameSemaphore.java` using `java.util.concurrent.Semaphore`.

**Why:**
- See how much simpler it is
- No explicit lock needed
- No while loops for spurious wakeups

### Step 4: Advanced Generic Version
Create `PingPongGameExtract.java` using a generic method.

**Why:**
- Eliminate code duplication (DRY)
- Pass behavior as parameters
- Professional-grade implementation

---

## 🔍 Code Comparison

After implementing, compare your solutions with the reference implementations in:
`src/main/java/com/lesson/concurrency/era1_classic/ping_pong_game/`

| Approach | Complexity | Clarity | Risk |
| :--- | :--- | :--- | :--- |
| **Wait/Notify** | ⭐⭐⭐ | ⭐⭐ | High (spurious wakeups) |
| **Enum State** | ⭐⭐⭐ | ⭐⭐⭐⭐ | Medium |
| **Semaphore** | ⭐ | ⭐⭐⭐⭐⭐ | Low (simplest) |
| **Generic/DRY** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Low (once correct) |

---

## ⚠️ Common Mistakes to Avoid

### 1. Using `if` instead of `while`
❌ **Wrong:** `if (!turn) wait();`
✅ **Right:** `while (!turn) wait();`
*Reason:* Threads can wake up without being notified (spurious wakeup). You must recheck the condition.

### 2. Calling notify() outside synchronized
❌ **Wrong:** `lock.notifyAll();`
✅ **Right:** `synchronized(lock) { lock.notifyAll(); }`
*Reason:* You must hold the monitor lock to call wait/notify.

### 3. Missing Exception Handling
❌ **Wrong:** `wait();`
✅ **Right:** `try { wait(); } catch (InterruptedException e) { ... }`
*Reason:* Threads can be interrupted. You should restore the interrupt status.

### 4. Shared State Visibility
❌ **Wrong:** `boolean running;`
✅ **Right:** `volatile boolean running;`
*Reason:* Changes in one thread might not be visible to others without synchronization or volatile.

---

## ✅ Submission Checklist

- [ ] All 4 implementations created
- [ ] `mvn test` passes for all versions
- [ ] No race conditions (tested with 100+ iterations)
- [ ] Code is clean and formatted
- [ ] Exceptions handled properly

**Ready? Start coding in `src/main/java/com/practice/concurrency/era1_classic/ping_pong_game/`!** 🚀
