# Java Concurrency Exercises Repository Structure

This document outlines a recommended project structure for organizing Java concurrency exercises using Maven and JDK 25.

## Proposed Directory Structure

```
java-concurrency-exercises/
├── pom.xml                              # Root Maven POM
├── README.md                            # Project overview
├── AGENTS.md                            # Workspace memory & insights
│
├── docs/                                # Documentation
│   ├── java_concurrency_exercises.md    # Complete exercise guide
│   ├── exercises_tracking.md            # Progress tracking document
│   ├── REPOSITORY_STRUCTURE.md          # This file
│   └── SETUP.md                         # Development setup guide
│
├── src/
│   ├── main/java/
│   │   └── com/example/concurrency/
│   │       ├── util/                    # Shared utilities
│   │       │   ├── ThreadUtils.java
│   │       │   ├── BenchmarkRunner.java
│   │       │   └── TestHelper.java
│   │       │
│   │       ├── era1_classic/            # JDK 5 exercises
│   │       │   ├── monitor/
│   │       │   │   ├── PingPongGame.java
│   │       │   │   ├── GuardedSuspension.java
│   │       │   │   ├── MessageBus.java
│   │       │   │   └── PeriodicTaskTrigger.java
│   │       │   ├── synchronization/
│   │       │   │   ├── BoundedBuffer.java
│   │       │   │   ├── OddEvenPrinter.java
│   │       │   │   └── DiningPhilosophers.java
│   │       │   ├── executors/
│   │       │   │   ├── CustomThreadPool.java
│   │       │   │   └── BarrierCoordination.java
│   │       │   └── atomics/
│   │       │       ├── ABAProblemStack.java
│   │       │       └── PriorityJobProcessor.java
│   │       │
│   │       ├── era2_parallelism/        # JDK 7-8 exercises
│   │       │   ├── forkjoin/
│   │       │   │   ├── ParallelFileSearch.java
│   │       │   │   └── MatrixMultiplier.java
│   │       │   ├── completable/
│   │       │   │   ├── WebAggregator.java
│   │       │   │   └── FlakyMicroservice.java
│   │       │   ├── stamped/
│   │       │   │   └── OptimisticCache.java
│   │       │   ├── phaser/
│   │       │   │   └── MultiPhaseTask.java
│   │       │   ├── transfer/
│   │       │   │   └── GuaranteedDelivery.java
│   │       │   └── streams/
│   │       │       └── ParallelStreamProcessing.java
│   │       │
│   │       ├── era3_reactive/           # JDK 9-17 exercises
│   │       │   ├── flow/
│   │       │   │   └── TemperatureMonitor.java
│   │       │   ├── varhandle/
│   │       │   │   └── LowLevelAtomicField.java
│   │       │   └── rates/
│   │       │       └── TokenBucketLimiter.java
│   │       │
│   │       ├── era4_loom/               # JDK 21+ exercises
│   │       │   ├── virtual/
│   │       │   │   ├── MillionThreadTest.java
│   │       │   │   └── VirtualThreadBenchmark.java
│   │       │   └── structured/
│   │       │       ├── StructuredTaskOrchestration.java
│   │       │       ├── ScopedValueExample.java
│   │       │       └── StableValueExample.java
│   │       │
│   │       ├── era5_specialized/        # JDK 5-9+ utilities
│   │       │   ├── exchanger/
│   │       │   │   └── BufferSwap.java
│   │       │   ├── adder/
│   │       │   │   └── StatisticsBenchmark.java
│   │       │   ├── skiplist/
│   │       │   │   └── RealtimeLeaderboard.java
│   │       │   ├── copyonwrite/
│   │       │   │   └── PluginRegistry.java
│   │       │   ├── lockstupport/
│   │       │   │   └── DIYSemaphore.java
│   │       │   ├── delayqueue/
│   │       │   │   └── CacheExpunger.java
│   │       │   └── thread/
│   │       │       └── InheritableThreadLocalExample.java
│   │       │
│   │       ├── era6_pipeline/           # Barrier & Phaser exercises
│   │       │   ├── cyclicbarrier/
│   │       │   │   ├── AssemblyLine.java
│   │       │   │   └── FrameRenderer.java
│   │       │   └── phaser/
│   │       │       ├── WebCrawler.java
│   │       │       ├── GameOfLife.java
│   │       │       └── SearchAndCancel.java
│   │       │
│   │       ├── era7_niche/              # High-performance & diagnostic APIs
│   │       │   ├── atomic/
│   │       │   │   ├── TreiberStack.java
│   │       │   │   └── AtomicFieldUpdaterExample.java
│   │       │   ├── completion/
│   │       │   │   └── FastestQuoteAggregator.java
│   │       │   ├── readwrite/
│   │       │   │   └── DataReloader.java
│   │       │   ├── deque/
│   │       │   │   └── TaskStealingSimulation.java
│   │       │   └── diagnostics/
│   │       │       └── PinningDetective.java
│   │       │
│   │       └── senior/                  # Senior-level challenges
│   │           ├── LRUCache.java
│   │           ├── DistributedLock.java
│   │           ├── RideMatching.java
│   │           └── H2OCreator.java
│   │
│   └── test/java/
│       └── com/example/concurrency/
│           ├── era1_classic/
│           │   ├── monitor/
│           │   │   ├── PingPongGameTest.java
│           │   │   ├── BoundedBufferTest.java
│           │   │   └── GuardedSuspensionTest.java
│           │   └── ... (mirroring main structure)
│           ├── era2_parallelism/
│           │   └── ... (tests for parallelism exercises)
│           ├── ... (tests for other eras)
│           └── util/
│               └── ConcurrencyTestHelper.java
│
├── benchmarks/                          # JMH Benchmarks (separate module)
│   ├── pom.xml
│   └── src/main/java/
│       └── com/example/concurrency/benchmarks/
│           ├── AtomicVsAdderBenchmark.java
│           ├── StampedLockBenchmark.java
│           ├── VirtualThreadBenchmark.java
│           └── LongAdderBenchmark.java
│
├── scripts/                             # Utility scripts
│   ├── setup.sh                         # Development setup
│   ├── run-all-exercises.sh             # Run all exercises
│   ├── run-tests.sh                     # Run test suite
│   ├── benchmark.sh                     # Run JMH benchmarks
│   └── diagnostics.sh                   # Virtual thread diagnostics
│
└── .mvn/
    └── extensions.xml                   # Maven configuration
```

## Maven Project Structure (pom.xml)

### Root pom.xml Structure
```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>java-concurrency-exercises</artifactId>
    <version>1.0.0</version>
    <packaging>pom</packaging>
    
    <properties>
        <maven.compiler.source>25</maven.compiler.source>
        <maven.compiler.target>25</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <modules>
        <module>.</module>
        <module>benchmarks</module>
    </modules>
    
    <!-- Dependencies -->
    <!-- JUnit 5, Mockito, JMH, etc. -->
</project>
```

## File Organization Best Practices

### 1. **By JDK Era**
- Organize exercises by the JDK version they were introduced
- Each era folder contains related functionality
- Clear progression from classic to modern approaches

### 2. **By Concurrency Primitive**
- Within each era, group by the primary API/pattern
- Keeps related exercises together
- Makes it easy to find implementations using specific APIs

### 3. **Utilities Package**
- Shared helper classes for benchmarking, testing, and thread management
- Reusable components across multiple exercises

### 4. **Parallel Test Structure**
- Mirror the src/main structure in src/test
- Each exercise has corresponding unit tests
- Ensures comprehensive test coverage

### 5. **Benchmarks Module**
- Separate Maven module for JMH benchmarks
- Keeps performance testing isolated from unit tests
- Easy to run benchmarks independently

## Naming Conventions

### Exercise Classes
- **Pattern:** `<ProblemName>.java`
- **Examples:** `PingPongGame.java`, `BoundedBuffer.java`, `WebAggregator.java`
- Clear, descriptive names matching problem descriptions

### Test Classes
- **Pattern:** `<ExerciseName>Test.java`
- **Examples:** `PingPongGameTest.java`, `BoundedBufferTest.java`

### Benchmark Classes
- **Pattern:** `<Topic>Benchmark.java`
- **Examples:** `AtomicVsAdderBenchmark.java`, `VirtualThreadBenchmark.java`

## Package Naming

```
com.example.concurrency.[era][category]

Examples:
- com.example.concurrency.era1_classic.monitor
- com.example.concurrency.era2_parallelism.forkjoin
- com.example.concurrency.era4_loom.virtual
- com.example.concurrency.util
```

## Documentation Files

Each era folder should include an `INDEX.md`:

```
era1_classic/
├── INDEX.md                    # Overview of all exercises in this era
├── monitor/
├── synchronization/
├── executors/
└── atomics/
```

## Dependencies to Include

### Testing
- JUnit 5 (Jupiter)
- Mockito
- AssertJ

### Benchmarking
- JMH (Java Microbenchmark Harness)

### Logging (Optional)
- SLF4J
- Logback

### Build & Quality
- Maven Surefire (testing)
- Maven Failsafe (integration tests)
- SpotBugs (static analysis)

## Build Profiles

```xml
<profiles>
    <profile>
        <id>all-tests</id>
        <!-- Run all tests including integration tests -->
    </profile>
    <profile>
        <id>benchmarks</id>
        <!-- Run JMH benchmarks -->
    </profile>
    <profile>
        <id>diagnostics</id>
        <!-- Run with virtual thread diagnostics enabled -->
    </profile>
</profiles>
```

## Run Configurations

### Maven Commands
```bash
# Run all unit tests
mvn clean test

# Run specific exercise
mvn test -Dtest=PingPongGameTest

# Run benchmarks
mvn clean install && mvn -f benchmarks/pom.xml jmh:benchmark

# Run with virtual thread diagnostics
mvn -Djdk.tracePinnedThreads=full test
```

### IDE Run Configurations
- Main exercise runner (runs main method of each exercise)
- Test suite runner
- Benchmark runner

## Key Advantages of This Structure

1. **Scalability:** Easy to add new exercises without disrupting existing code
2. **Organization:** Clear grouping by JDK era and concurrency primitive type
3. **Discoverability:** Easy to find implementations of specific APIs
4. **Testing:** Comprehensive test coverage with parallel structure
5. **Benchmarking:** Isolated performance testing with JMH
6. **Documentation:** Each exercise is self-contained and documented
7. **Progression:** Natural learning path from classic to modern approaches
8. **Reusability:** Shared utilities reduce code duplication

## Next Steps

1. Create the directory structure
2. Set up Maven pom.xml with proper dependencies
3. Implement starter exercises (Ping-Pong, Bounded Buffer)
4. Create test fixtures and utilities
5. Add JMH benchmark configurations
6. Document setup instructions (SETUP.md)
