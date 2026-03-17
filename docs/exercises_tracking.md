# Java Concurrency Exercises - Tracking & Progress

This document tracks your progress through all Java concurrency exercises organized by JDK era and difficulty level.

---

## Quick Progress Summary

- [ ] **Classic Era (JDK 5)** - 1/11 exercises completed (PingPongGame)
- [ ] **Parallelism & Async Era (JDK 7-8)** - 0/7 exercises completed
- [ ] **Reactive & Modern Era (JDK 9-17)** - 0/3 exercises completed
- [ ] **Project Loom Era (JDK 21+)** - 0/5 exercises completed
- [ ] **Advanced Specialized Utilities** - 1/9 exercises completed (Exchanger)
- [ ] **Pipeline Coordination** - 1/5 exercises completed (CyclicBarrier)
- [ ] **Niche, High-Performance & Diagnostic APIs** - 0/8 exercises completed
- [ ] **Senior-Level Machine Coding Challenges** - 0/4 exercises completed

**Total Progress: 0/52 exercises**

---

## Era 1: Classic Era (JDK 5) - Foundation Building

### Monitor Pattern & `wait()`/`notify()`

- [x] **The Strict Turn-Taker (Ping-Pong)**
  - Status: Completed (Wait/Notify, Enum, Semaphore, ReentrantLock, Exchanger, CyclicBarrier)
  - Difficulty: ⭐ (Beginner)
  - Key Concepts: Monitor locks, boolean flags, `notify()`, Semaphore, Exchanger
  - Notes: _Implemented 6 variants including ReentrantLock and Exchanger double-handshake_

- [ ] **The Manual Bounded Buffer (Producer-Consumer)**
  - Status: Not Started
  - Difficulty: ⭐⭐ (Intermediate)
  - Key Concepts: Spurious wakeups, `while` loops, `notifyAll()`
  - Notes: _Add implementation notes here_

- [ ] **The "Guarded Suspension" (Door Gate)**
  - Status: Not Started
  - Difficulty: ⭐⭐ (Intermediate)
  - Key Concepts: Complex state waiting, `wait()` loops
  - Notes: _Add implementation notes here_

- [ ] **The Message Bus (One-to-Many Signaling)**
  - Status: Not Started
  - Difficulty: ⭐⭐ (Intermediate)
  - Key Concepts: Broadcasting, `notifyAll()`, multiple listeners
  - Notes: _Add implementation notes here_

- [ ] **The Periodic Task Trigger (Custom Scheduler)**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Timed waiting, `wait(long timeout)`, manual triggers
  - Notes: _Add implementation notes here_

### Core JDK 5 Exercises

- [ ] **The Odd-Even Printer**
  - Status: Not Started
  - Difficulty: ⭐ (Beginner)
  - Key Concepts: Sequential printing, thread coordination
  - Notes: _Add implementation notes here_

- [ ] **The Bounded Buffer (3 Levels)**
  - Level 1: `synchronized` + `wait/notify`
    - [ ] Status: Not Started
    - Difficulty: ⭐⭐ (Intermediate)
  - Level 2: `ReentrantLock` + `Condition`
    - [ ] Status: Not Started
    - Difficulty: ⭐⭐⭐ (Advanced)
  - Level 3: `ArrayBlockingQueue`
    - [ ] Status: Not Started
    - Difficulty: ⭐ (Beginner)
  - Level 4: **Two-Lock Queue** (LinkedBlockingQueue style)
    - [ ] Status: Not Started
    - Difficulty: ⭐⭐⭐⭐ (Expert)
    - Key Concepts: Split locks (`putLock`, `takeLock`), AtomicInteger count, signal cascading
  - Notes: _Add implementation notes here_

- [ ] **The Dining Philosophers**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Deadlock prevention, fairness, resource allocation
  - Notes: _Add implementation notes here_

- [ ] **Custom Thread Pool**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: `BlockingQueue`, worker threads, `ExecutorService` pattern
  - Notes: _Add implementation notes here_

- [ ] **Barrier Coordination (2 Variants)**
  - `CountDownLatch` (Race Start)
    - [ ] Status: Not Started
    - Difficulty: ⭐⭐ (Intermediate)
  - `CyclicBarrier` (Multi-stage Process)
    - [ ] Status: Not Started
    - Difficulty: ⭐⭐ (Intermediate)
  - Notes: _Add implementation notes here_

---

## Era 2: Parallelism & Async Era (JDK 7-8)

### JDK 7 Exercises

- [ ] **Parallel File Search**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: `ForkJoinPool`, `RecursiveTask`, work-stealing
  - APIs: `ForkJoinPool`, `RecursiveTask`
  - Notes: _Add implementation notes here_

- [ ] **The Matrix Multiplier**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Divide-and-conquer, parallel computation
  - APIs: `ForkJoinPool`, `RecursiveTask`
  - Notes: _Add implementation notes here_

- [ ] **Multi-Phase Task with Joining/Leaving**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Dynamic party count, phase synchronization
  - APIs: `Phaser`
  - Notes: _Add implementation notes here_

- [ ] **Guaranteed Delivery System**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Synchronous hand-off, blocking transfer
  - APIs: `LinkedTransferQueue`
  - Notes: _Add implementation notes here_

### JDK 8 Exercises

- [ ] **The Asynchronous Web Aggregator**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Async composition, timeout handling, fallback values
  - APIs: `CompletableFuture`, timeout methods
  - Notes: _Add implementation notes here_

- [ ] **The Optimistic Cache**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Optimistic reads, fast-path reads, write locks
  - APIs: `StampedLock`
  - Notes: _Add implementation notes here_

- [ ] **Parallel Stream Processing**
  - Status: Not Started
  - Difficulty: ⭐⭐ (Intermediate)
  - Key Concepts: Parallel streams, performance comparison
  - APIs: `.parallelStream()`, benchmarking
  - Notes: _Add implementation notes here_

---

## Era 3: Reactive & Modern Era (JDK 9-17)

- [ ] **Publisher-Subscriber System**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Reactive programming, backpressure, flow control
  - APIs: `java.util.concurrent.Flow`
  - Notes: _Add implementation notes here_

- [ ] **The Readers-Writers Cache**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Optimistic reads, high-performance reading
  - APIs: `StampedLock`
  - Notes: _Add implementation notes here_

- [ ] **Rate Limiter (Token Bucket)**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Rate limiting, token generation, scheduled execution
  - APIs: `ScheduledExecutorService`, custom algorithms
  - Notes: _Add implementation notes here_

---

## Era 4: Project Loom Era (JDK 21-25)

- [ ] **The Million-Thread Test**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Virtual threads, memory efficiency, scalability
  - APIs: `Executors.newVirtualThreadPerTaskExecutor()`
  - Notes: _Add implementation notes here_

- [ ] **Structured Task Orchestration (ShutdownOnFailure)**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Task cancellation, error propagation, structured concurrency
  - APIs: `StructuredTaskScope`
  - Notes: _Add implementation notes here_

- [ ] **Structured Task Orchestration (ShutdownOnSuccess)**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Race conditions (fair), best-of-N pattern
  - APIs: `StructuredTaskScope`
  - Notes: _Add implementation notes here_

- [ ] **Scoped Context Propagation**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Context variables, immutable scoping, virtual thread compatibility
  - APIs: `ScopedValue`
  - Notes: _Add implementation notes here_

- [ ] **Safe Lazy Initialization**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Single-write guarantee, JIT optimization
  - APIs: `StableValue`
  - Notes: _Add implementation notes here_

---

## Era 5: Advanced Specialized Utilities (JDK 5-9+)

- [x] **The Secret Message Swap**
  - Status: Completed (PingPong variant implemented)
  - Difficulty: ⭐⭐ (Intermediate)
  - Key Concepts: Buffer swapping, pipeline design, GC optimization
  - APIs: `Exchanger`
  - Notes: _Implemented PingPongGameExchanger using double-handshake pattern_

- [ ] **The Statistics Benchmark**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: High-contention performance, throughput comparison
  - APIs: `LongAdder`, `LongAccumulator`, `AtomicLong`
  - Notes: _Add implementation notes here_

- [ ] **The Memory-Efficient Node**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Field-level atomicity, memory optimization
  - APIs: `VarHandle`
  - Notes: _Add implementation notes here_

- [ ] **The Real-Time Leaderboard**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Sorted concurrent access, range queries
  - APIs: `ConcurrentSkipListMap`
  - Notes: _Add implementation notes here_

- [ ] **The Plugin/Listener Registry**
  - Status: Not Started
  - Difficulty: ⭐⭐ (Intermediate)
  - Key Concepts: Copy-on-write, listener patterns, safe iteration
  - APIs: `CopyOnWriteArrayList`
  - Notes: _Add implementation notes here_

- [ ] **The DIY Semaphore**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐⭐ (Expert)
  - Key Concepts: Low-level parking, lock construction
  - APIs: `LockSupport`
  - Notes: _Add implementation notes here_

- [ ] **The Flaky Microservice**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Timeout handling, fallback responses, timeout composition
  - APIs: `CompletableFuture.orTimeout()`, `completeOnTimeout()`
  - Notes: _Add implementation notes here_

- [ ] **The Custom Cache Expunger**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: TTL-based cleanup, delayed execution, background tasks
  - APIs: `DelayQueue`
  - Notes: _Add implementation notes here_

- [ ] **Distributed Tracing TraceID**
  - Status: Not Started
  - Difficulty: ⭐⭐ (Intermediate)
  - Key Concepts: Context propagation, child thread inheritance
  - APIs: `InheritableThreadLocal`, `ScopedValue` (modern)
  - Notes: _Add implementation notes here_

---

## Era 6: Pipeline Coordination: Barriers and Phasers

- [x] **The Rigid Assembly Line**
  - Status: Completed (PingPong variant implemented)
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Fixed barrier, batch processing, barrier actions
  - APIs: `CyclicBarrier`
  - Notes: _Implemented PingPongGameCyclicBarrier using 2-barrier relay race pattern_

- [ ] **The Video Frame Renderer**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Barrier actions, frame composition, work coordination
  - APIs: `CyclicBarrier` (with Barrier Action)
  - Notes: _Add implementation notes here_

- [ ] **The Multi-Phase Web Crawler**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐⭐ (Expert)
  - Key Concepts: Dynamic party registration, depth-first traversal
  - APIs: `Phaser` (register/deregister)
  - Notes: _Add implementation notes here_

- [ ] **The Scientific Simulation / Game of Life**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐⭐ (Expert)
  - Key Concepts: Generation synchronization, state-dependent waiting
  - APIs: `Phaser`, `onAdvance()` override
  - Notes: _Add implementation notes here_

- [ ] **The "Search and Cancel" Pipeline**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Pipeline termination, cooperative cancellation
  - APIs: `Phaser` (forceTermination)
  - Notes: _Add implementation notes here_

---

## Era 7: Niche, High-Performance & Diagnostic APIs

- [ ] **The Lock-Free Treiber Stack**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐⭐ (Expert)
  - Key Concepts: ABA problem, atomic references, lock-free structures
  - APIs: `AtomicReference`, `AtomicStampedReference`
  - Notes: _Add implementation notes here_

- [ ] **The Fastest-Quote Aggregator**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Completion service, result ordering, task cancellation
  - APIs: `ExecutorCompletionService`
  - Notes: _Add implementation notes here_

- [ ] **The Reliable Data Reloader**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Lock downgrading, read-write coordination
  - APIs: `ReentrantReadWriteLock`
  - Notes: _Add implementation notes here_

- [ ] **The Memory-Optimized Web Server**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Field-level atomicity, memory optimization
  - APIs: `AtomicIntegerFieldUpdater`
  - Notes: _Add implementation notes here_

- [ ] **The Pinning Detective**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Virtual thread diagnostics, pinning detection
  - APIs: Virtual Threads, `-Djdk.tracePinnedThreads=full`
  - Notes: _Add implementation notes here_

- [ ] **The Task-Stealing Simulation**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐ (Advanced)
  - Key Concepts: Work-stealing, deque operations (FIFO/LIFO)
  - APIs: `LinkedBlockingDeque`
  - Notes: _Add implementation notes here_

---

## Senior-Level Machine Coding Challenges

- [ ] **High-Performance LRU Cache**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐⭐ (Expert)
  - Key Concepts: TTL expiration, concurrent access, eviction policy
  - APIs: `ConcurrentHashMap`, `ScheduledExecutorService`, custom data structures
  - Notes: _Add implementation notes here_

- [ ] **Distributed Lock Simulation**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐⭐ (Expert)
  - Key Concepts: Fairness, reentrancy, lock ownership tracking
  - APIs: `Semaphore`, `ReentrantLock`
  - Notes: _Add implementation notes here_

- [ ] **The Uber/Lyft Ride Matching Problem**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐⭐ (Expert)
  - Key Concepts: Pair matching, fairness, coordination
  - APIs: Synchronization primitives (your choice)
  - Notes: _Add implementation notes here_

- [ ] **The H2O Creator**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐⭐ (Expert)
  - Key Concepts: Multi-party coordination, specific count requirements
  - APIs: `Semaphore`, `Phaser`, or custom synchronizer
  - Notes: _Add implementation notes here_

- [ ] **The LMAX Disruptor (Ring Buffer)**
  - Status: Not Started
  - Difficulty: ⭐⭐⭐⭐⭐ (Guru)
  - Key Concepts: Ring Buffer, Sequence Barriers, False Sharing (Padding), Single Writer Principle
  - APIs: `AtomicLong` (padded), Unsafe/VarHandle, Memory Barriers
  - Notes: _Implement a simplified lock-free ring buffer for message passing_

---

## Legend

- **Status**: Not Started | In Progress | Completed | Reviewed
- **Difficulty**: 
  - ⭐ = Beginner (can complete in 1-2 hours with documentation)
  - ⭐⭐ = Intermediate (2-4 hours, requires some research)
  - ⭐⭐⭐ = Advanced (4-8 hours, requires deep understanding)
  - ⭐⭐⭐⭐ = Expert (8+ hours, requires mastery of related concepts)

---

## Notes & Tips

- Start with **Classic Era** exercises to build foundational understanding
- Progress sequentially through eras to build upon previous knowledge
- For each exercise, implement all 3 practice levels (Direct, Refactor, Performance)
- Use **Java Flight Recorder (JFR)** for performance benchmarking
- Refer to the main `java_concurrency_exercises.md` for detailed problem descriptions and learning objectives
