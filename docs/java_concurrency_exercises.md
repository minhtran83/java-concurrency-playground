# Java Concurrency Exercises: A Comprehensive Progression

Practicing Java concurrency requires a progression from low-level synchronization primitives to high-level asynchronous pipelines and the modern "structured" era.

Below is a comprehensive list of exercises categorized by the evolution of the JDK.

---

## Quick Reference: Java Concurrency Evolution (JDK 5 – JDK 25)

This table maps the evolution of Java concurrency from JDK 5 to the current JDK 25. It pairs classic and modern problems with the specific APIs designed to solve them.

| Era / JDK | Concurrency Problem / Exercise | Key API(s) for Practice | Core Concept |
| :--- | :--- | :--- | :--- |
| **JDK 5** (The Foundation) | **The Bounded Buffer:** Implement a thread-safe queue where producers wait if full and consumers wait if empty. | `ReentrantLock`, `Condition` | Explicit locking & signaling |
| | **The "Race Start" Signal:** 10 threads wait for a single "Go" signal from the main thread to start a task. | `CountDownLatch` | One-time synchronization |
| | **Odd-Even Sequential Printing:** Two threads printing 1, 2, 3... in strict order. | `wait()`, `notify()` / `Semaphore` | Inter-thread coordination |
| | **Secret Message Swap:** Two threads exchanging buffers of data at a specific point. | `Exchanger` | Point-to-point data swap |
| | **ABA Problem Stack:** Build a lock-free stack and simulate the ABA memory corruption. | `AtomicStampedReference` | Memory integrity & versioning |
| | **Priority Job Processing:** Build a system where high-priority tasks move to the front of the queue. | `PriorityBlockingQueue` | Thread-safe sorted storage |
| **JDK 7** (Parallelism) | **Recursive File Search:** Search a massive directory tree for a filename by splitting tasks. | `ForkJoinPool`, `RecursiveTask` | Work-stealing parallelism |
| | **Multi-Stage Build Pipeline:** Threads moving through "Compile", "Test", and "Deploy" phases. | `Phaser` | Dynamic-party barriers |
| | **Guaranteed Delivery:** A producer that blocks until a consumer *actually* takes the message. | `LinkedTransferQueue` | Synchronous hand-off |
| **JDK 8** (Async & Optimization) | **The Web Aggregator:** Call 5 APIs simultaneously; merge results or use a default if one fails. | `CompletableFuture` | Asynchronous pipelines |
| | **The Optimistic Cache:** Build a cache that allows multiple readers without locking unless a write occurs. | `StampedLock` | Optimistic read/write |
| | **High-Contention Counter:** Track website hits from 10,000 threads simultaneously. | `LongAdder` | Low-contention atomics |
| **JDK 9** (Reactive & Timing) | **Flaky Service Timeout:** Cancel a task or return a fallback if it doesn't respond in 500ms. | `CompletableFuture.orTimeout()` | Non-blocking timeouts |
| | **Temperature Monitor:** A sensor "Publisher" pushes data to multiple "Subscriber" alert systems. | `java.util.concurrent.Flow` | Reactive Streams |
| | **Low-Level Atomic Field Update:** Perform atomic state changes on a plain `volatile` field. | `VarHandle` | Safe field-level atomicity |
| **JDK 21** (The Virtual Era) | **The Million-Thread Test:** Spawn 100,000 blocking I/O tasks and compare memory usage with OS threads. | **Virtual Threads** | Lightweight concurrency |
| | **Fan-In / Fan-Out:** Run 3 tasks; if any fails, cancel the others immediately. | `StructuredTaskScope` | Structured Concurrency |
| **JDK 25** (Modern Reliability) | **Contextual Trace ID:** Pass a "Request ID" through a complex call chain without using `ThreadLocal`. | `ScopedValue` (Final) | Immutable context sharing |
| | **Safe Lazy Initialization:** Implement a singleton or config loader that is set only once and optimized by JIT. | `StableValue` (Preview) | Deferred immutability |
| | **Job with Built-in Timeout:** Submit a task to a pool that automatically cancels it after a duration. | `ForkJoinPool.submitWithTimeout` | Pool-level scheduling |

### How to Use This Table for Practice:
1. **Level 1 (Direct Implementation):** Solve the problem using the listed API.
2. **Level 2 (The Refactor):** Solve a JDK 5 problem (like the Bounded Buffer) using a JDK 21 feature (like Virtual Threads) to see how code complexity decreases.
3. **Level 3 (Performance Comparison):** Benchmark `AtomicLong` vs `LongAdder` or `ThreadLocal` vs `ScopedValue` using a tool like JMH (Java Microbenchmark Harness) to understand the "why" behind newer APIs.

---

## 1. The Classic Era (Pre-JDK 5 & JDK 5)
*Focus: Monitor locks, inter-thread signaling, and the foundation of `java.util.concurrent`.*

### Foundational: The Monitor Pattern & `wait()`/`notify()`

> **Why Practice This?** While modern Java provides high-level utilities like `BlockingQueue` or `Semaphore`, the low-level `wait()` and `notify()` (monitor-based synchronization) are still the "building blocks" of concurrency. Practicing these helps you understand **Guarded Suspension** (waiting for a specific state) and the **Monitor Pattern**. In a production environment (JDK 25), you almost never use `wait()`/`notify()`, but you practice them to understand spurious wakeups, memory efficiency, and for interview readiness.

#### The Strict Turn-Taker (Ping-Pong)
This is the most famous `wait()`/`notify()` exercise. You cannot easily solve this with a simple queue because it requires strict alternation.

**Problem:** Create two threads, "Ping" and "Pong". They must print their names alternately (Ping, then Pong, then Ping...).

**The Lesson:** You learn how to use a shared `boolean` flag as a condition and how to use `notify()` to hand over control to the specific other thread.

**Key Pattern:**
```java
synchronized(lock) {
    while (!isPingTurn) lock.wait();  // Note: while, not if!
    // ... print Ping ...
    isPingTurn = false;
    lock.notifyAll();  // Wake the other thread
}
```

#### The Manual Bounded Buffer (Producer-Consumer)
While `ArrayBlockingQueue` exists, implementing it yourself using `wait()`/`notify()` is the "Rite of Passage" for Java concurrency.

**Problem:** Implement a `MyQueue` class with `put(item)` and `take()`.
- If the queue is full, `put` must wait.
- If the queue is empty, `take` must wait.

**The Lesson:** You learn why you **must** use a `while` loop instead of an `if` statement (to handle "Spurious Wakeups") and why `notifyAll()` is safer than `notify()`.

#### The "Guarded Suspension" (The Door Gate)
This is used when a thread must wait for a specific complex state to be reached.

**Problem:** Imagine a "Manager" thread that can only start "Work" once three specific conditions are met: `isDatabaseUp`, `isConfigLoaded`, and `isNetworkReady`.

**The Lesson:** Unlike a `CountDownLatch` (which just counts down), `wait()`/`notify()` allows you to wait for a **specific boolean expression** to become true.

#### The Message Bus (One-to-Many Signaling)
**Problem:** Create one "Broadcaster" thread and five "Listener" threads. The Listeners should all be waiting. When the Broadcaster receives a specific command, all 5 Listeners must wake up and process the command simultaneously.

**The Lesson:** This teaches the power of `notifyAll()`. It shows how a single signal can broadcast to an entire set of waiting threads (unlike `notify()` which only picks one).

#### The Periodic Task Trigger (Custom Scheduler)
**Problem:** Create a thread that performs a task. However, it should only perform the task when a "Manual Trigger" is pressed (a method call), OR if 5 seconds have passed since the last execution.

**The Lesson:** You learn how to use the timed version: `wait(long timeout)`. This is a hybrid of "waiting for a signal" and "waiting for a timer."

#### Common Pitfalls to Watch For:
- **IllegalMonitorStateException:** You called `wait()` without being inside a `synchronized` block.
- **The "Lost Wakeup":** You called `notify()` before the other thread reached the `wait()` call.
- **Nested Monitor Lockout:** You are waiting on one object while holding the lock of another, causing a deadlock.

---

### The Odd-Even Printer
Create two threads (one for even numbers, one for odd) that must print numbers sequentially from 1 to 100 using `wait()` and `notify()`.

### The Bounded Buffer (Producer-Consumer)
Implement a thread-safe queue from scratch.
- **Level 1:** Use `synchronized` and `wait/notify`.
- **Level 2:** Re-implement using JDK 5 `ReentrantLock` and `Condition`.
- **Level 3:** Use `ArrayBlockingQueue` to see how it simplifies the code.

### The Dining Philosophers
A classic deadlock exercise. Implement it such that no philosopher starves and the system never deadlocks.

### Custom Thread Pool
Build a primitive `ExecutorService` using a `BlockingQueue` and a fixed number of worker threads.

### Barrier Coordination
Use a `CountDownLatch` to simulate a "Race Start" (all threads wait for a signal) or a `CyclicBarrier` to simulate a "Multi-stage Process" (threads wait for each other at checkpoints).

### The Modern Equivalent: `Condition` Variables
Once you master `wait()`/`notify()`, try refactoring them using **`java.util.concurrent.locks.Condition`**. It is the JDK 5+ version of `wait()`/`notify()` that allows for **multiple wait-sets** (e.g., one for "notFull" and one for "notEmpty") on a single lock.

---

## 2. The Parallelism & Async Era (JDK 7 & JDK 8)
*Focus: Divide-and-conquer parallelism and non-blocking pipelines.*

### Parallel File Search
Use the **Fork/Join Framework** (`RecursiveTask`) to search for a specific filename in a massive directory tree.

### The Matrix Multiplier
Implement parallel matrix multiplication using `ForkJoinPool` to split the task into smaller sub-calculations.

### The Asynchronous Web Aggregator
Use `CompletableFuture` to fetch data from three mock APIs simultaneously.
- **Requirement:** If all succeed, merge results. If one takes longer than 2 seconds, return a default value for that specific call.

### Parallel Stream Processing
Take a list of 1 million integers and perform heavy computation on them using `.parallelStream()`. Compare the performance with a standard loop.

---

## 3. The Reactive & Modern Era (JDK 9 to JDK 17)
*Focus: Flow API and specialized locks.*

### Publisher-Subscriber System
Use the `java.util.concurrent.Flow` API (JDK 9) to build a temperature monitoring system where multiple "Sensors" (Publishers) send data to "Alert Systems" (Subscribers).

### The Readers-Writers Cache
Use `StampedLock` (JDK 8/9+) to build a cache that allows high-performance optimistic reads.

### Rate Limiter
Build a "Token Bucket" rate limiter that allows only $N$ requests per second using `ScheduledExecutorService`.

---

## 4. The "Project Loom" Era (JDK 21 to JDK 25)
*Focus: Virtual Threads, Structured Concurrency, and Scoped Values.*

### The Million-Thread Test
Write a program that spawns 100,000 threads to perform a blocking I/O task (like a 1-second `Thread.sleep()`).
- **Task:** Compare memory usage between Platform Threads and **Virtual Threads** (`Executors.newVirtualThreadPerTaskExecutor()`).

### Structured Task Orchestration
Use `StructuredTaskScope` (Preview feature up to JDK 25) to fetch a "User Profile," "User Orders," and "User Recommendations."
- **Exercise 1 (ShutdownOnFailure):** If any service fails, cancel the others and throw an error.
- **Exercise 2 (ShutdownOnSuccess):** Fetch the same data from two different mirrors (Mirror A and Mirror B); return the result of the one that finishes first and cancel the other.

### Scoped Context Propagation
Replace a `ThreadLocal` with a `ScopedValue` (Finalized in JDK 25) to pass a "Transaction ID" down a deep call chain of virtual threads.

### Safe Lazy Initialization
Use the new `StableValue` API (Preview in JDK 25) to implement a singleton or a configuration loader that is guaranteed to be set only once and optimized by the JIT.

### Timeout-Aware Job
Use the updated `ForkJoinPool` (JDK 25) which now implements `ScheduledExecutorService` to submit a task with the new `submitWithTimeout` method.

---

## 5. Advanced Specialized Utilities (JDK 5 - JDK 9+)
*Focus: High-performance tuning, specific coordination patterns, and low-level framework development.*

### The "Hand-off" Coordination (`Exchanger`) – JDK 5
The `Exchanger` is a synchronization point where two threads can exchange objects. It's a very specific "point-to-point" pattern.

**Exercise: The Secret Message Swap**
Create two threads: a "Producer" that fills a buffer with data and a "Consumer" that processes it. Instead of a queue, use an `Exchanger` to swap a full buffer for an empty one. This is excellent for pipeline designs where you want to minimize garbage collection by reusing buffers.

### The Dynamic Synchronizer (`Phaser`) – JDK 7
A `Phaser` is a more flexible version of `CountDownLatch` and `CyclicBarrier`. Unlike those two, the number of registered parties in a `Phaser` can change over time.

**Exercise: Multi-Phase Task with Joining/Leaving**
Simulate a software build pipeline with 3 phases (Compile, Test, Deploy).
- Phase 1 starts with 5 "Compiler" threads.
- Only 3 threads are needed for Phase 2 ("Testing").
- Only 1 thread is needed for Phase 3 ("Deploy").
- Use a `Phaser` to coordinate these phases, having threads "deregister" themselves as the pipeline progresses.

### High-Contention Counters (`LongAdder` & `LongAccumulator`) – JDK 8
In high-concurrency scenarios, `AtomicLong` can become a bottleneck because all threads compete to update the same memory address. `LongAdder` spreads the load across multiple variables (cells).

**Exercise: The Statistics Benchmark**
Create a scenario where 200 threads are constantly incrementing a counter.
- Compare the throughput of `AtomicLong` vs. `LongAdder`.
- Use `LongAccumulator` to maintain a "Max Value" or a "Running Product" across those same 200 threads and observe the performance difference.

### Direct Field Access (`VarHandle`) – JDK 9
`VarHandle` is the modern, safe replacement for `java.util.concurrent.atomic.AtomicFieldUpdater` and the dangerous `sun.misc.Unsafe`. It allows you to perform atomic operations on fields of a class without wrapping them in an `AtomicInteger` object.

**Exercise: The Memory-Efficient Node**
Create a `Node` class for a linked list. Use a `VarHandle` to perform a `compareAndSet` on a plain `volatile` field. This exercise helps you understand how low-level libraries (like Netty or the JDK itself) achieve atomicity without the memory overhead of wrapper objects.

### Transfer Queues (`LinkedTransferQueue`) – JDK 7
This is a "dual" queue. It acts like a `LinkedBlockingQueue` but adds a `transfer()` method which blocks the producer until a consumer actually takes the item.

**Exercise: The Guaranteed Delivery System**
Implement a system where a Producer submits a "Critical Task." If a Consumer is available, the Producer hands it off and continues immediately. If no Consumer is available, the Producer must block until someone takes the task. Compare this behavior to `SynchronousQueue`.

### Concurrent Navigation (`ConcurrentSkipListMap/Set`) – JDK 6
While `ConcurrentHashMap` is common, it doesn't keep items sorted. `ConcurrentSkipListMap` provides a thread-safe way to have a sorted map.

**Exercise: The Real-Time Leaderboard**
Build a leaderboard where multiple threads update player scores. The system must always allow a thread to "View Top 10" or "Get Players with scores between 500 and 1000" concurrently without locking the whole map.

### Thread-Safe Snapshots (`CopyOnWriteArrayList/Set`) – JDK 5
These are designed for scenarios where you read often but rarely modify. Every modification creates a fresh copy of the underlying array.

**Exercise: The Plugin/Listener Registry**
Create a "System Event" dispatcher. Multiple threads can register "Listeners." The dispatcher must iterate through the listeners and notify them. Show how `CopyOnWriteArrayList` prevents `ConcurrentModificationException` if a listener tries to unregister *itself* while the notification loop is running.

### Low-Level Thread Parking (`LockSupport`) – JDK 5
This is the "atomic" building block used by `ReentrantLock`. It allows you to pause (`park`) and resume (`unpark`) a thread by its reference.

**Exercise: The DIY Semaphore**
Implement a very simple, non-reentrant "Binary Semaphore" using only a `volatile int` and `LockSupport.park()` / `LockSupport.unpark()`. This is a classic "Senior Engineer" test to see if you understand how locks are built under the hood.

### Delayed Execution (`CompletableFuture` Enhancements) – JDK 9
JDK 9 added methods like `orTimeout()` and `completeOnTimeout()` to `CompletableFuture`.

**Exercise: The Flaky Microservice**
Call a mock service that returns a `CompletableFuture`. If the service doesn't respond within 500ms, use `completeOnTimeout` to return a cached "Fallback" response. If it doesn't respond within 2 seconds, use `orTimeout` to fail the entire chain with a `TimeoutException`.

---

## 6. Pipeline Coordination: Barriers and Phasers (JDK 5 & JDK 7)
*Focus: Lock-Step Parallelism – Ensuring all parallel workers complete one stage before any proceed to the next.*

> **Key Insight:** Pipelines using **Barriers** and **Phasers** are distinct from "Producer-Consumer" pipelines (which use Queues) because they represent **Lock-Step Parallelism**. The "pipeline" isn't about moving data from A to B, but about ensuring all parallel workers finish **Stage 1** before any of them are allowed to see the data for **Stage 2**.

### The Rigid Assembly Line (`CyclicBarrier`)
**Scenario:** You are simulating an automated factory. Each "Product" must go through 3 stages: *Assembly*, *Painting*, and *Quality Testing*. You have a fixed team of 4 workers.

**The Constraint:** To keep the factory balanced, all 4 workers must finish "Assembly" before any worker can start "Painting."

**Exercise:**
1. Create 4 threads representing Workers.
2. Use a `CyclicBarrier` with a **Barrier Action**.
3. The Barrier Action should print "--- Batch Stage Complete: Moving to next phase ---" and increment a global batch counter.
4. The workers should loop through 5 batches of products, calling `barrier.await()` after each stage.

### The Video Frame Renderer (`CyclicBarrier` + Barrier Action)
**Scenario:** You are building a video engine. Each video frame is split into 4 quadrants (Top-Left, Top-Right, etc.).

**The Constraint:** You cannot display a frame until all 4 quadrants are rendered.

**Exercise:**
1. Each worker thread renders its assigned quadrant (simulated with `Thread.sleep`).
2. When all 4 arrive at the `CyclicBarrier`, use the **Barrier Action** (the Runnable passed to the constructor) to "stitch" the quadrants together and save the frame to a list.
3. Once the "Stitching" (Barrier Action) is done, the workers automatically start on the quadrants for the *next* frame.

### The Multi-Phase Web Crawler (`Phaser`)
**Scenario:** You are crawling a website in "levels" (Level 1: Homepage, Level 2: Links from homepage, Level 3: Links from those links).

**The Challenge:** Unlike `CyclicBarrier`, you don't know how many threads you'll need at each level. If a page has no links, that thread "retires." If a page has 10 links, you might spawn new threads.

**Exercise:**
1. Start a `Phaser` with 1 party (the main thread).
2. As you find links, spawn a new thread and call `phaser.register()`.
3. When a thread finishes its page, it calls `phaser.arriveAndDeregister()`.
4. Use `phaser.arriveAndAwaitAdvance()` to ensure all threads finish "Level 1" before any thread starts crawling "Level 2."
5. **Bonus:** Override `onAdvance(int phase, int registeredParties)` in the Phaser to log how many pages were found at each depth level.

### The Scientific Simulation / Game of Life (`Phaser`)
**Scenario:** In simulations like "Conway's Game of Life" or "Heat Distribution," every cell's next state depends on its neighbors' current state.

**The Constraint:** You cannot calculate "Second 2" for any cell until *every* cell has finished calculating "Second 1."

**Exercise:**
1. Divide a $1000 \times 1000$ grid into 4 chunks.
2. Assign 1 thread to each chunk.
3. The threads must run in a loop for 100 "generations."
4. At the end of each generation, threads call `phaser.arriveAndAwaitAdvance()`.
5. **Advanced Logic:** Implement a "Pause" button. The main thread can register with the Phaser, causing all worker threads to wait at the next generation until the main thread "arrives" to let them proceed.

### The "Search and Cancel" Pipeline (`Phaser` Termination)
**Scenario:** You are searching for a specific file across 4 different hard drives.

**The Challenge:** `CyclicBarrier` is hard to stop once it's looping. `Phaser` can be "terminated."

**Exercise:**
1. Create a `Phaser`.
2. 4 threads search different paths.
3. If a thread finds the file, it calls `phaser.forceTermination()`.
4. The other threads should check `phaser.isTerminated()` to stop their search immediately.

---

### Comparison Summary: `CyclicBarrier` vs `Phaser`

| Feature | `CyclicBarrier` Exercise Focus | `Phaser` Exercise Focus |
| :--- | :--- | :--- |
| **Thread Count** | Fixed (e.g., 4 workers for 4 quadrants). | Dynamic (Threads join/leave as tasks grow/shrink). |
| **Reusability** | Automatic reset after the barrier triggers. | Multi-generational, but requires manual registration. |
| **Completion Logic** | The **Barrier Action** (Runnable). | Overriding the **onAdvance** method. |
| **Stopping** | Hard to stop mid-cycle. | Supports **forceTermination** to kill the pipeline. |

### Advanced Project: The Parallel Image Processing Pipeline
If you want to combine everything:

1. **Phase 1 (Gray Scale):** 4 threads turn images to gray. (Use `Phaser`).
2. **Phase 2 (Blur):** 4 threads apply blur. (Wait for Phase 1).
3. **Phase 3 (Denoise):** Some images are skipped if they are already clear. (Threads `arriveAndDeregister`).
4. **Completion:** Once all phases for a batch are done, a single "Reporter" thread (via `onAdvance`) logs the total time taken.

---

## 7. Niche, High-Performance & Diagnostic APIs (JDK 5 - JDK 21+)
*Focus: ABA Problem solutions, result ordering, memory optimization, and modern diagnostics.*

### The ABA Problem & Memory Integrity (`AtomicStampedReference`) – JDK 5
Standard atomics (like `AtomicInteger`) are vulnerable to the **ABA Problem**: a value changes from A to B and back to A, and a thread thinks nothing has changed.

**Exercise: The Lock-Free Treiber Stack**
1. Implement a Stack using `AtomicReference`.
2. Create a scenario where a thread is interrupted between a "peek" and a "compare-and-set."
3. Show how the ABA problem causes the stack to become corrupted.
4. **The Fix:** Re-implement the stack using `AtomicStampedReference` (which attaches a "version number" or "stamp" to the pointer) to ensure the stack remains valid.

### Result-Ordering with `ExecutorCompletionService` – JDK 5
Standard `ExecutorService.invokeAll()` waits for *all* tasks to finish. If you have 10 tasks and one is slow, you wait for the slow one. `ExecutorCompletionService` gives you results in the order they **finish**.

**Exercise: The Fastest-Quote Aggregator**
1. Simulate 5 travel agencies (tasks) returning flight prices with random delays.
2. Use `ExecutorCompletionService` to process and print quotes to the console the millisecond they arrive.
3. Cancel all remaining tasks as soon as you find a quote below $500.

### Time-Based Scheduling (`DelayQueue`) – JDK 5
`DelayQueue` is a specialized `BlockingQueue` where an element can only be taken when its "delay" has expired.

**Exercise: The Custom Cache Expunger**
1. Build a simple `Map` cache.
2. When an item is added, also add a "Reference" to a `DelayQueue` with a 5-second TTL (Time-To-Live).
3. Create a background "Cleaner Thread" that polls the `DelayQueue`.
4. The cleaner thread should only wake up when an item is actually ready to be deleted from the map.

### Hierarchical Context (`InheritableThreadLocal`) – JDK 5
Standard `ThreadLocal` variables are not visible to child threads. `InheritableThreadLocal` allows a child thread to "inherit" values from its parent.

**Exercise: Distributed Tracing TraceID**
1. In a main thread, set a "Trace-ID" (a UUID) in an `InheritableThreadLocal`.
2. Spawn 3 worker threads.
3. Show how those workers can read the Trace-ID without it being passed in the constructor.
4. *Note:* Practice replacing this with **Scoped Values** in JDK 21+ for better performance with Virtual Threads.

### Lock Reentrancy & Downgrading (`ReentrantReadWriteLock`) – JDK 5
While we mentioned `StampedLock`, the older `ReentrantReadWriteLock` is unique because it allows **Lock Downgrading**.

**Exercise: The Reliable Data Reloader**
1. Acquire a **Read Lock** to check if a cache is valid.
2. If invalid, release the Read Lock and acquire the **Write Lock** to update the data.
3. **The Downgrade:** While holding the Write Lock, acquire the Read Lock, *then* release the Write Lock. This allows you to keep reading the data consistently without allowing anyone else to write in between.

### Low-Level Atomic Field Updaters – JDK 5
Before `VarHandle` (JDK 9), we used `AtomicReferenceFieldUpdater`. This is used to perform atomic updates on a field of a class without the memory overhead of an `AtomicReference` object.

**Exercise: The Memory-Optimized Web Server**
1. Create a `Connection` class with a `volatile int state`.
2. Use `AtomicIntegerFieldUpdater` to atomically change the state from `OPEN` to `CLOSED`.
3. Benchmark the memory usage of 1 million `Connection` objects using this method vs. using 1 million `AtomicInteger` objects.

### Virtual Thread "Pinning" Diagnostics – JDK 21+
In the Project Loom era, the biggest "problem" is **Pinning** (when a virtual thread gets stuck to a platform thread because of `synchronized` or `native` calls).

**Exercise: The Pinning Detective**
1. Write a Virtual Thread that performs a `Thread.sleep()` inside a `synchronized` block.
2. Run the JVM with the flag `-Djdk.tracePinnedThreads=full`.
3. Observe the stack trace in the console that explains exactly why the thread was pinned.
4. **Fix:** Replace the `synchronized` block with a `ReentrantLock` and observe the pinning report disappear.

### Work-Stealing Analysis (`LinkedBlockingDeque`) – JDK 6
`LinkedBlockingDeque` allows threads to push/pop from both ends. This is the foundation of "Work-Stealing" algorithms.

**Exercise: The Task-Stealing Simulation**
1. Create two threads, each with its own `LinkedBlockingDeque`.
2. If Thread A finishes its tasks, it should "steal" a task from the **tail** of Thread B's deque to help out.
3. Show how using the tail (LIFO vs FIFO) reduces contention with Thread B, which is working from the **head**.

---

## Senior-Level "Machine Coding" Challenges
For those looking for interview-level practice, these combine multiple JDK features:

### 1. High-Performance LRU Cache
Build a thread-safe Least Recently Used cache with an expiration policy (TTL). Use `ConcurrentHashMap` and a background `ScheduledExecutorService` for cleanup.

### 2. Distributed Lock Simulation
Implement a local version of a distributed lock using `Semaphore` or `ReentrantLock` that handles "fairness" and "re-entrancy."

### 3. The Uber/Lyft Ride Matching Problem
Threads representing "Riders" and "Drivers" must wait for each other in pairs. Once a pair is matched, they "drive" together.

### 4. The H2O Creator
Two threads provide Hydrogen and one provides Oxygen. You must coordinate them so they only proceed when they can form an $H_2O$ molecule (requires `Semaphore` or `Phaser`).

---

## Final Mastery Checklist: Java Concurrency from JDK 5 to JDK 25

To consider yourself a **Java Concurrency Expert**, you should now be able to confidently use and explain:

### 1. Coordination Primitives
- [ ] `CountDownLatch` – Single-use barrier for all threads to wait for a single event
- [ ] `CyclicBarrier` – Reusable barrier where all threads wait for each other with Barrier Actions
- [ ] `Phaser` – Multi-generational, dynamic synchronization with registration/deregistration
- [ ] `Exchanger` – Point-to-point buffer swapping between exactly two threads

### 2. Execution Frameworks
- [ ] `ExecutorService` – Basic thread pool management
- [ ] `ScheduledExecutorService` – Delayed and periodic task execution
- [ ] `ExecutorCompletionService` – Processing results in completion order
- [ ] `ForkJoinPool` – Divide-and-conquer parallelism with work-stealing
- [ ] `Executors.newVirtualThreadPerTaskExecutor()` – Virtual Threads (JDK 21+)

### 3. Scalability & Performance
- [ ] Virtual Threads – Light-weight threading for high-throughput I/O (JDK 21+)
- [ ] `StructuredTaskScope` – Task orchestration with automatic cancellation (JDK 21+)
- [ ] `LongAdder` – High-contention counter optimization
- [ ] `LongAccumulator` – Custom reduction operations without bottlenecks
- [ ] `ConcurrentHashMap` – Lock-free segmented hashing
- [ ] `ConcurrentSkipListMap` – Sorted concurrent map with logarithmic operations

### 4. Synchronization & Locking
- [ ] `synchronized` keyword – Monitor locks (foundational but avoid for new code)
- [ ] `ReentrantLock` – Explicit lock with fairness and condition variables
- [ ] `ReentrantReadWriteLock` – Multiple readers, exclusive writer with downgrading
- [ ] `StampedLock` – Ultra-fast optimistic reads (JDK 8+)
- [ ] `Semaphore` – Permits-based access control
- [ ] `LockSupport` – Low-level thread parking/unparking

### 5. Concurrent Collections
- [ ] `ArrayBlockingQueue` – Bounded FIFO queue
- [ ] `LinkedBlockingQueue` – Unbounded linked queue
- [ ] `LinkedTransferQueue` – Dual queue with guaranteed hand-off
- [ ] `SynchronousQueue` – No storage, producer waits for consumer
- [ ] `PriorityBlockingQueue` – Priority-ordered unbounded queue
- [ ] `DelayQueue` – Elements available only after their delay expires
- [ ] `LinkedBlockingDeque` – Double-ended queue for work-stealing
- [ ] `CopyOnWriteArrayList` – Immutable snapshots for read-heavy workloads

### 6. Atomic Operations & Memory Safety
- [ ] `AtomicInteger`, `AtomicLong`, `AtomicBoolean` – Basic atomics
- [ ] `AtomicReference` – Atomic pointers (vulnerable to ABA problem)
- [ ] `AtomicStampedReference` – ABA-safe atomic pointers with versioning
- [ ] `AtomicMarkableReference` – Boolean-flagged atomic references
- [ ] `VarHandle` – Modern, safe field atomicity (JDK 9+)
- [ ] `AtomicIntegerFieldUpdater`, `AtomicReferenceFieldUpdater` – Memory-efficient field updates (JDK 5-8, replaced by VarHandle)

### 7. Context & Scoping
- [ ] `ThreadLocal` – Thread-specific storage
- [ ] `InheritableThreadLocal` – Context propagation to child threads
- [ ] `ScopedValue` – Modern, virtual-thread-safe context (JDK 21+)
- [ ] `StableValue` – Guaranteed single-write optimization (JDK 25+)

### 8. Diagnostics & Monitoring
- [ ] Thread Dumps – Identifying deadlocks and blocked threads
- [ ] Java Flight Recorder (JFR) – Continuous profiling
- [ ] `-Djdk.tracePinnedThreads=full` – Virtual Thread pinning diagnostics (JDK 21+)
- [ ] Jconsole & Visual VM – Real-time thread monitoring
- [ ] Lock contention analysis – Identifying bottlenecks

### 9. Advanced Patterns
- [ ] Lock-Free Data Structures – CAS-based algorithms
- [ ] Work-Stealing Algorithms – Deque-based task distribution
- [ ] Pipeline Coordination – Barrier vs. Queue-based patterns
- [ ] Rate Limiting – Token bucket algorithms
- [ ] Distributed Tracing – Trace ID propagation

---

## Beyond Threading: The Vector API (Bonus)

The **Vector API** (JDK 16-25) handles "Data Parallelism" (SIMD - Single Instruction, Multiple Data) at the CPU register level. While technically a form of concurrency, it is usually considered a separate topic from "Threading." If you are doing heavy math, scientific computing, or AI/ML workloads, the Vector API represents the very last frontier of performance optimization.

---

## Next Steps After Mastery

1. **Contribute to High-Performance Libraries** – Try contributing to projects like Netty, Akka, or Quarkus that rely heavily on these primitives.
2. **Study Production Code** – Read the internals of `ConcurrentHashMap`, `ForkJoinPool`, and Virtual Thread implementations.
3. **Practice Low-Level Optimization** – Use JFR, async-profiler, and perf-map to identify lock contention.
4. **Explore Domain-Specific Concurrency** – Reactive Streams (Project Reactor, RxJava), Coroutines (Kotlin), or Async/Await patterns.

---

## Summary of Key Tools by Version

| Version | Key Feature to Practice |
| :--- | :--- |
| **JDK 5** | `ExecutorService`, `Locks`, `AtomicInteger`, `BlockingQueue`, `Semaphore`, `Exchanger`, `LockSupport`, `CopyOnWriteArrayList` |
| **JDK 6** | `ConcurrentSkipListMap`, `ConcurrentSkipListSet` |
| **JDK 7** | `ForkJoinPool`, `Phaser`, `LinkedTransferQueue` |
| **JDK 8** | `CompletableFuture`, `StampedLock`, `LongAdder`, `LongAccumulator` |
| **JDK 9** | `Flow` API (Reactive Streams), `VarHandle`, `CompletableFuture` timeout methods |
| **JDK 21** | **Virtual Threads** (Final), `StructuredTaskScope` (Preview) |
| **JDK 25** | **Scoped Values** (Final), **Stable Values** (Preview), `ForkJoinPool` enhancements |

---

## Updated Roadmap for Practice

1. **Low-Level Primitives:** `LockSupport`, `VarHandle`.
2. **Specialized Coordination:** `Exchanger`, `Phaser`.
3. **High-Throughput Math:** `LongAdder`, `LongAccumulator`.
4. **Specialized Queues:** `LinkedTransferQueue`, `PriorityBlockingQueue`.
5. **Modern Async Control:** `CompletableFuture` (with JDK 9+ timeout methods).
6. **Modern Threading:** Virtual Threads & `StructuredTaskScope` (JDK 21+).

---

## Getting Started

Each section builds upon previous concepts. Start with the Classic Era exercises to build foundational understanding, then progress through each era as your expertise grows. The Advanced Specialized Utilities section covers high-performance tuning and specific coordination patterns essential for framework development. The senior-level challenges are designed to combine multiple concepts for real-world interview preparation.

### Suggested Learning Path:
1. Master the **Classic Era** (JDK 5) foundations with `synchronized`, `wait/notify`, and basic `ExecutorService`
2. Explore **Specialized Utilities** (JDK 5-9) for performance tuning and coordination patterns
3. Progress through **Parallelism & Async Era** (JDK 7-8) for concurrent collections and divide-and-conquer
4. Study **Reactive & Modern Era** (JDK 9-17) for reactive programming patterns
5. Embrace **Project Loom Era** (JDK 21+) for modern virtual threading and structured concurrency
6. Practice **Senior-Level Challenges** to synthesize all concepts and prepare for technical interviews
