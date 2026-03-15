# Era 1: Classic Era (JDK 5) - Complete Learning Guide

Master the foundational concurrency patterns that form the basis for all modern Java threading.

---

## Overview

The Classic Era covers foundational concurrency concepts from JDK 5:
- Monitor locks and `wait()`/`notify()`
- Synchronization primitives (`Semaphore`, `Barrier`, etc.)
- Thread management and `ExecutorService`
- Atomic operations and lock-free programming
- Explicit locks and conditions (`ReentrantLock`)

These patterns are the building blocks for everything in modern Java concurrency.

---

## Category Structure

### 📖 [Monitor Pattern](monitor/INDEX.md)
**Learn how threads coordinate using wait() and notify()**

Three comprehensive lessons:
1. [Coordination Fundamentals](monitor/monitor_pattern_coordination.md) - Start here
2. [Better Patterns & Alternatives](monitor/monitor_pattern_alternatives.md) - Learn 6 superior approaches
3. [Generic/DRY with Lambdas](monitor/monitor_pattern_generic.md) - Eliminate duplication

**Exercises:**
- `PingPongGame` - Strict alternation between 2 threads
- Test: `PingPongGameTest` - 7 comprehensive tests

**Key Concepts:**
- `synchronized` keyword
- `Object.wait()` and `Object.notifyAll()`
- `volatile` for memory visibility
- While loops for spurious wakeup handling

---

### 🔒 [Synchronization](synchronization/)
**Coming Soon** - Other synchronization primitives

Will cover:
- `CountDownLatch` - One-time synchronization
- `CyclicBarrier` - Multi-stage coordination
- `Semaphore` - Permit-based access control
- `Phaser` - Multi-phase, dynamic coordination

---

### ⚙️ [Executors](executors/)
**Coming Soon** - Thread pool management

Will cover:
- `ExecutorService` - Basic thread pool
- `ScheduledExecutorService` - Delayed and periodic tasks
- Thread pool sizing strategies
- Work submission patterns

---

### 🔢 [Atomics](atomics/)
**Coming Soon** - Atomic operations and lock-free programming

Will cover:
- `AtomicInteger`, `AtomicLong`, `AtomicReference`
- Compare-and-set (CAS) operations
- The ABA problem
- Lock-free data structures

---

### 🔐 [Locks](locks/)
**Coming Soon** - Explicit locks and conditions

Will cover:
- `ReentrantLock` - Explicit lock management
- `Condition` variables - Multiple wait-sets
- Lock fairness
- Try-lock with timeouts

---

## Learning Paths

### Path 1: Complete Beginner
**Duration: 40-50 hours**

1. **Week 1-2: Monitor Fundamentals**
   - Read all 3 Monitor lessons
   - Run PingPongGame
   - Write PingPongGame variations

2. **Week 3-4: Other Synchronization**
   - Study Semaphore (from Alternatives lesson)
   - Study CountDownLatch
   - Implement exercises

3. **Week 5-6: Executors**
   - Learn thread pool concepts
   - Implement custom thread pool

4. **Week 7-8: Atomics & Locks**
   - Learn atomic operations
   - Study ReentrantLock
   - Compare with synchronized

### Path 2: Intermediate (Know some threading)
**Duration: 20-30 hours**

1. **Refresh Monitor Basics** (1-2 hours)
   - Read Coordination lesson quickly
   - Focus on Alternatives & Generic/DRY

2. **Deep Dive Patterns** (4-6 hours)
   - Study all synchronization primitives
   - Compare wait/notify vs modern APIs

3. **Hands-On Exercises** (15-20 hours)
   - Implement all exercises in the category
   - Compare different approaches

### Path 3: Advanced (Preparing for Interview)
**Duration: 10-15 hours**

1. **Pattern Review** (2-3 hours)
   - Scan Monitor lessons
   - Focus on Generic/DRY approach

2. **Implement All Variants** (5-7 hours)
   - Monitor pattern (synchronized version)
   - Semaphore version
   - ReentrantLock version

3. **Practice Explanation** (3-5 hours)
   - Be able to explain each pattern
   - Discuss trade-offs
   - Show code examples

---

## Key Learning Objectives

By the end of Era 1, you should be able to:

### Understanding
- [ ] Explain how `synchronized` creates a monitor lock
- [ ] Describe `wait()`, `notify()`, and `notifyAll()`
- [ ] Understand spurious wakeups and why while loops are necessary
- [ ] Explain the difference between `notify()` and `notifyAll()`
- [ ] Understand `volatile` and memory visibility

### Implementation
- [ ] Write correct 2-thread coordination with wait/notify
- [ ] Implement a bounded buffer (Producer-Consumer)
- [ ] Use `CountDownLatch` for one-time synchronization
- [ ] Use `Semaphore` for permit-based access
- [ ] Write thread-safe code without data races

### Design
- [ ] Choose appropriate synchronization for a scenario
- [ ] Recognize and fix common concurrency bugs
- [ ] Use Enum-based state instead of boolean flags
- [ ] Extract common patterns to avoid duplication
- [ ] Know when to use synchronized vs ReentrantLock

### Debugging
- [ ] Identify deadlocks using thread dumps
- [ ] Use thread naming for debugging
- [ ] Understand thread states (RUNNABLE, WAITING, BLOCKED)
- [ ] Use synchronized blocks effectively

---

## Exercise Progression

### Level 1: Foundation (Beginner)
1. **PingPongGame** - Basic alternation
2. **CountDownLatch** - One-time barrier
3. **Semaphore** - Simple permit system

### Level 2: Intermediate
4. **BoundedBuffer** - Producer-Consumer pattern
5. **DiningPhilosophers** - Deadlock prevention
6. **CustomThreadPool** - Executor implementation

### Level 3: Advanced
7. **CyclicBarrier** - Multi-stage coordination
8. **ComplexCoordination** - Multiple synchronization primitives
9. **PerformanceComparison** - Benchmark different approaches

---

## Common Mistakes to Avoid

### Synchronization Mistakes
- ❌ Using `if` instead of `while` in wait condition
- ❌ Calling `wait()` outside synchronized block
- ❌ Using `notify()` instead of `notifyAll()`
- ❌ Not holding lock when calling wait/notify

### Design Mistakes
- ❌ Using boolean flags for state (use Enum)
- ❌ Duplicating synchronization logic (extract methods)
- ❌ Not using volatile for shared mutable state
- ❌ Ignoring InterruptedException

### Performance Mistakes
- ❌ Over-synchronizing (too large critical sections)
- ❌ Using synchronized when lock-free is possible
- ❌ Not using Semaphore for simple permit systems
- ❌ Blocking threads unnecessarily

---

## Tools & Debugging

### Thread Dump Analysis
```bash
# Generate thread dump
jcmd <pid> Thread.print

# Look for:
- BLOCKED - waiting for lock
- WAITING - waiting on monitor
- TIMED_WAITING - wait with timeout
```

### Thread Naming for Debugging
```java
Thread thread = new Thread(runnable, "Worker-1");
// Better debugging: "Worker-1" in thread dumps
```

### Testing Concurrent Code
```java
@Test
@Timeout(value = 5, unit = TimeUnit.SECONDS)
void testWithTimeout() { ... }

// Prevents infinite hangs in tests
```

---

## Quick Reference

### When to Use What

| Scenario | Use |
| :--- | :--- |
| Simple turn-taking (2 threads) | `Semaphore` |
| Wait for all threads to start | `CountDownLatch` |
| Multiple stages with wait | `CyclicBarrier` |
| Complex coordination (N parties) | `Phaser` |
| Manual synchronization | `ReentrantLock` + `Condition` |
| Learning basics | `synchronized` + `wait/notify` |

---

## Resources

### Within This Repository
- `src/main/java/com/example/concurrency/era1_classic/` - Exercise implementations
- `src/test/java/com/example/concurrency/era1_classic/` - Test cases
- `docs/java_concurrency_exercises.md` - All exercises described

### External Resources
- Java Concurrency in Practice (book) - Chapters 2-3
- Oracle Java Tutorials - Concurrency section
- Stack Overflow - `java` + `concurrency` tags

---

## Progress Tracking

Track your progress through Era 1:

- [ ] Completed Monitor lessons (all 3)
- [ ] Implemented PingPongGame
- [ ] Studied Semaphore pattern
- [ ] Studied CountDownLatch
- [ ] Implemented Bounded Buffer
- [ ] Studied CyclicBarrier
- [ ] Implemented Dining Philosophers
- [ ] Studied ReentrantLock
- [ ] All exercises passing tests
- [ ] All exercises have benchmarks

---

## Next Steps

After completing Era 1:
1. Move to **Era 2: Parallelism & Async (JDK 7-8)**
2. Learn Fork/Join framework
3. Study CompletableFuture
4. Explore parallel streams

---

*Master these foundations and everything else becomes easy!* 🚀
