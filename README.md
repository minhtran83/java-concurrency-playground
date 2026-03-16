# Java Concurrency Exercises

A comprehensive, progressive learning repository for Java concurrency from **JDK 5 to JDK 25**. This repository contains 50+ exercises organized by JDK era, covering everything from foundational monitor patterns to modern virtual threads and structured concurrency.

## 🎯 Purpose

Master Java concurrency by practicing with **real-world problems** organized by the evolution of the JDK. Learn why modern APIs exist by understanding the foundational patterns they replaced, then practice using them across different JDK versions.

## 📚 What You'll Learn

### By JDK Era:
- **JDK 5 (Classic Era)** - Monitor patterns, synchronization primitives, foundational concurrency
- **JDK 7-8 (Parallelism & Async Era)** - Fork/Join, CompletableFuture, stream parallelism
- **JDK 9-17 (Reactive & Modern)** - Flow API, VarHandle, optimistic locking
- **JDK 21+ (Project Loom)** - Virtual threads, structured concurrency, scoped values
- **Plus:** Advanced utilities, pipeline coordination, diagnostic APIs, senior-level challenges

### Total Coverage:
- ✅ 50+ exercises across 8 categories
- ✅ 70+ concurrent APIs and patterns
- ✅ Monitor locks to virtual threads progression
- ✅ Interview-ready machine coding challenges
- ✅ Performance benchmarking with JMH

## 🚀 Quick Start

### Prerequisites

- **Java:** JDK 25 or higher
  ```bash
  java -version  # Should output Java 25.x
  ```

- **Maven:** 3.9.0 or higher
  ```bash
  mvn -version  # Should output Maven 3.9.x
  ```

### Clone & Setup

```bash
# Clone the repository
git clone <repo-url>
cd java-concurrency-exercises

# Build the project
mvn clean install

# Run all tests
mvn clean test

# Run a specific exercise test
mvn test -Dtest=PingPongGameTest

# Run benchmarks
mvn -f benchmarks/pom.xml jmh:benchmark
```

## 📋 Project Structure

```
java-concurrency-exercises/
├── src/main/java/com/example/concurrency/
│   ├── era1_classic/          # JDK 5: Monitor locks, basic synchronization
│   ├── era2_parallelism/      # JDK 7-8: Fork/Join, async programming
│   ├── era3_reactive/         # JDK 9-17: Flow, reactive patterns
│   ├── era4_loom/             # JDK 21+: Virtual threads, structured concurrency
│   ├── era5_specialized/      # High-performance utilities
│   ├── era6_pipeline/         # Barrier & Phaser coordination
│   ├── era7_niche/            # Advanced APIs & diagnostics
│   ├── senior/                # Machine coding challenges
│   └── util/                  # Shared utilities & helpers
│
├── src/test/java/             # Comprehensive test coverage
├── benchmarks/                # JMH benchmarks for performance analysis
└── docs/                      # Documentation & tracking
```

## 📖 Documentation

### Core Guides:
- **[java_concurrency_exercises.md](docs/java_concurrency_exercises.md)** - Complete exercise guide with detailed problem descriptions (538 lines)
- **[exercises_tracking.md](docs/exercises_tracking.md)** - Progress tracker with checkboxes and difficulty ratings (400+ lines)

### Architecture & Development:
- **[AGENTS.md](AGENTS.md)** - Repository structure, scalability guidelines, and workspace memory
- **[ADDING_NEW_EXERCISES.md](docs/ADDING_NEW_EXERCISES.md)** - How to add new exercises as the repository grows

## 🎓 Learning Path

### Level 1: Foundation (JDK 5)
Start with these to understand core concepts:
1. **The Strict Turn-Taker (Ping-Pong)** - wait()/notify() basics
2. **The Manual Bounded Buffer** - Producer-consumer pattern
3. **Custom Thread Pool** - Building blocks of executors

### Level 2: Modern Concurrent Programming (JDK 7-8)
Practice with contemporary APIs:
1. **Parallel File Search** - Fork/Join framework
2. **The Asynchronous Web Aggregator** - CompletableFuture
3. **The Optimistic Cache** - StampedLock performance

### Level 3: Advanced Patterns (JDK 9-17)
Explore reactive and specialized APIs:
1. **Publisher-Subscriber System** - Flow API
2. **The Real-Time Leaderboard** - ConcurrentSkipListMap
3. **Rate Limiter** - ScheduledExecutorService

### Level 4: Modern Java (JDK 21+)
Master virtual threads and structured concurrency:
1. **The Million-Thread Test** - Virtual thread scalability
2. **Structured Task Orchestration** - Coordination with automatic cancellation
3. **Scoped Context Propagation** - Modern context management

### Level 5: Senior-Level Challenges
Integrate multiple concepts:
1. **High-Performance LRU Cache** - TTL, concurrency, eviction
2. **Distributed Lock Simulation** - Fairness and reentrancy
3. **The H2O Creator** - Multi-party synchronization

## 🔧 Project Configuration

### Technology Stack
- **Language:** Java 25
- **Build Tool:** Maven 3.9+
- **Testing:** JUnit 5
- **Benchmarking:** JMH (Java Microbenchmark Harness)
- **Java Target:** JDK 25

### Maven Commands

```bash
# Build and test
mvn clean package

# Run specific test
mvn test -Dtest=BoundedBufferTest

# Run all tests with coverage
mvn clean test

# Run benchmarks
mvn clean install
mvn -f benchmarks/pom.xml jmh:benchmark

# Run with virtual thread diagnostics (JDK 21+)
mvn -Djdk.tracePinnedThreads=full test

# Generate project documentation
mvn site
```

## 🧩 Concurrency Patterns Cheat Sheet

Quick reference for the patterns you'll master in this repository.

### 1. Monitor Pattern (Wait/Notify)
**Best for:** Learning foundations, simple coordination.
```java
synchronized(lock) {
    while (!condition) {
        lock.wait();      // Release lock & wait
    }
    // Do work...
    condition = false;    // Update state
    lock.notifyAll();     // Wake others
}
```

### 2. ReentrantLock Pattern (Modern)
**Best for:** Production code, fairness, multiple conditions.
```java
lock.lock();
try {
    while (!condition) {
        cond.await();     // Release lock & wait
    }
    // Do work...
    condition = false;is the
    cond.signalAll();     // Wake others
} finally {
    lock.unlock();        // Guaranteed release
}
```

### 3. Semaphore Pattern
**Best for:** Simple turn-taking, resource pooling, rate limiting.
```java
myTurn.acquire();         // Wait for permit
// Do work...
otherTurn.release();      // Give permit to other
```

### 4. Enum State Pattern
**Best for:** Clarity and self-documenting code.
```java
enum Turn { PING, PONG }
// ...
while (turn != Turn.PING) { ... }
turn = Turn.PONG;
```

---

## 📊 Exercise Organization

### By API Family:
| API | Exercises | JDK Version |
| :--- | :--- | :--- |
| `wait()`/`notify()` | 5 | JDK 5 |
| `ReentrantLock` | 3 | JDK 5 |
| `Executor`/`ExecutorService` | 4 | JDK 5 |
| `ForkJoinPool` | 3 | JDK 7 |
| `CompletableFuture` | 4 | JDK 8 |
| `StampedLock` | 2 | JDK 8 |
| `Flow` API | 1 | JDK 9 |
| `VarHandle` | 1 | JDK 9 |
| Virtual Threads | 2 | JDK 21+ |
| `StructuredTaskScope` | 2 | JDK 21+ |
| **Total** | **52** | **JDK 5-25** |

## 🏗️ Adding New Exercises

The repository is designed to **scale organically**. No structural changes needed!

### Quick Add:
1. Create `ExerciseName.java` in appropriate era/category folder
2. Create `ExerciseNameTest.java` in parallel test folder
3. Update `exercises_tracking.md`
4. Run `mvn clean test`

**That's it!** Maven auto-discovers new packages and classes.

For detailed guidance, see [ADDING_NEW_EXERCISES.md](docs/ADDING_NEW_EXERCISES.md).

## 🎯 Exercise Difficulty Levels

- ⭐ **Beginner** - Can complete in 1-2 hours with documentation
- ⭐⭐ **Intermediate** - 2-4 hours, requires some research
- ⭐⭐⭐ **Advanced** - 4-8 hours, requires deep understanding
- ⭐⭐⭐⭐ **Expert** - 8+ hours, requires mastery of related concepts

## 📈 Practice Methodology

For each exercise, aim to complete all **3 practice levels**:

1. **Level 1 - Direct Implementation**
   - Solve the problem using the specified API
   - Focus on correctness and understanding

2. **Level 2 - The Refactor**
   - Solve a JDK 5 problem using JDK 21+ features
   - See how API evolution simplifies code

3. **Level 3 - Performance Comparison**
   - Benchmark different implementations (e.g., `AtomicLong` vs `LongAdder`)
   - Use JMH to measure and understand trade-offs
   - Learn the "why" behind newer APIs

## 🔍 Diagnostics & Monitoring

### Virtual Thread Pinning Detection (JDK 21+)
```bash
mvn -Djdk.tracePinnedThreads=full test
```

### Java Flight Recorder (JFR)
```bash
# Record performance data
java -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=recording.jfr Main.java

# View recording
jfr dump --json recording.jfr
```

### Thread Analysis
```bash
# Generate thread dump
jcmd <pid> Thread.print

# Monitor threads in real-time
jconsole <pid>
```

## 🤝 Contributing

To add new exercises:
1. Follow the package naming convention: `com.example.concurrency.[era].[category]`
2. Include JavaDoc with problem description, difficulty, and APIs
3. Write comprehensive unit tests
4. Add JMH benchmark for performance-focused exercises
5. Update `docs/exercises_tracking.md`

See [ADDING_NEW_EXERCISES.md](docs/ADDING_NEW_EXERCISES.md) for detailed guidelines.

## 📚 Key References

- [Java Concurrency Exercises Guide](docs/java_concurrency_exercises.md) - Detailed problem descriptions
- [Exercise Tracking](docs/exercises_tracking.md) - Progress checklist
- [Repository Architecture](AGENTS.md#repository-structure--scalability) - Project structure & design
- [Adding Exercises](docs/ADDING_NEW_EXERCISES.md) - Extension guidelines

## 🎓 What Makes This Different

✅ **Evolutionary Learning** - Progress from classic to modern approaches  
✅ **Hands-On Exercises** - 50+ real-world problems, not just theory  
✅ **Interview Ready** - Senior-level machine coding challenges included  
✅ **Performance Focus** - JMH benchmarks to understand API trade-offs  
✅ **Modern Java** - Latest features (Virtual Threads, Structured Concurrency, Scoped Values)  
✅ **Scalable** - Designed to grow with unlimited exercises  
✅ **Well Documented** - Clear guides for every exercise and API  

## 📋 Prerequisites Checklist

Before starting, verify:
- [ ] JDK 25 installed: `java -version`
- [ ] Maven 3.9+ installed: `mvn -version`
- [ ] Git installed: `git --version`
- [ ] Repository cloned and building: `mvn clean package`
- [ ] All tests passing: `mvn clean test`

## 🚦 Getting Started Steps

1. **Read the Quick Reference** - Review the evolution table in [java_concurrency_exercises.md](docs/java_concurrency_exercises.md#quick-reference-java-concurrency-evolution-jdk-5--jdk-25)
2. **Pick a Starting Exercise** - Begin with "The Strict Turn-Taker (Ping-Pong)" from Level 1
3. **Implement & Test** - Create your implementation following the problem description
4. **Compare with Guide** - Review the expected patterns and APIs
5. **Progress Sequentially** - Move through eras as your expertise grows

## 🔗 Related Topics

- **Reactive Programming** - Study Reactor, RxJava after mastering Flow API
- **Virtual Machine Performance** - Dive into JFR and async-profiler after concurrency
- **Distributed Systems** - Apply concurrency patterns to microservices
- **Data Parallelism** - Explore Vector API (JDK 16-25) for SIMD operations

## 📝 License

This project is provided as an educational resource. Exercises are inspired by classic concurrency problems and modern API documentation.

## ❓ FAQ

**Q: Do I need to know all of this to be a good Java developer?**  
A: No, but understanding these patterns helps you write safer, more performant concurrent code and understand why modern APIs exist.

**Q: Can I do exercises out of order?**  
A: Yes, but we recommend starting with Classic Era (JDK 5) to understand foundations before jumping to modern APIs.

**Q: How long does it take to complete all exercises?**  
A: With all 3 practice levels and benchmarking, expect 100-150 hours for 50+ exercises.

**Q: Can I contribute new exercises?**  
A: Absolutely! See [ADDING_NEW_EXERCISES.md](docs/ADDING_NEW_EXERCISES.md) for guidelines.

**Q: What if I find a bug or have a question?**  
A: Check the exercise guide and AGENTS.md documentation first. If you still have questions, open an issue or discussion.

---

**Happy learning! 🚀 Master Java concurrency from the ground up.**