# Adding New Exercises to the Repository

This guide explains how to add new exercises to the Java concurrency repository as you discover and create them.

## Core Principle: Flexible Organization

The repository structure is designed to **grow organically**. You don't need to pre-allocate space; simply add new exercises when you create them.

---

## Scenario 1: Adding an Exercise to an Existing Category

### Example: Adding a New Monitor Pattern Exercise

**Problem:** You discover a new interesting monitor pattern exercise: "The Task Queue with Priority Adjustment"

**Steps:**

1. **Create the exercise class:**
   ```
   src/main/java/com/example/concurrency/era1_classic/monitor/
   └── TaskQueueWithPriority.java
   ```

2. **Create the test class:**
   ```
   src/test/java/com/example/concurrency/era1_classic/monitor/
   └── TaskQueueWithPriorityTest.java
   ```

3. **Update the era's INDEX.md:**
   ```markdown
   # JDK 5 Monitor Pattern Exercises
   
   ## Existing Exercises
   - PingPongGame
   - GuardedSuspension
   - MessageBus
   - PeriodicTaskTrigger
   
   ## New Exercises
   - TaskQueueWithPriority (NEW)
   ```

4. **Update main exercises_tracking.md** (in docs):
   - Add to the appropriate era section
   - Update total exercise count

**That's it!** No structural changes needed.

---

## Scenario 2: Adding a New Category Within an Era

### Example: Adding "Locks" Category to JDK 5

**Problem:** You realize JDK 5 needs a dedicated "locks" subcategory for ReentrantLock exercises

**Steps:**

1. **Create the new category folder:**
   ```
   src/main/java/com/example/concurrency/era1_classic/locks/
   src/test/java/com/example/concurrency/era1_classic/locks/
   ```

2. **Add exercises to the new category:**
   ```
   src/main/java/com/example/concurrency/era1_classic/locks/
   ├── FairLockExample.java
   ├── ConditionVariableExample.java
   └── NestedLockExample.java
   ```

3. **Create INDEX.md for the category:**
   ```
   locks/
   └── INDEX.md
   ```

4. **Update era's INDEX.md:**
   ```markdown
   ## Categories in JDK 5 Era
   - monitor/ (5 exercises)
   - synchronization/ (3 exercises)
   - executors/ (2 exercises)
   - atomics/ (2 exercises)
   - locks/ (3 exercises) ← NEW
   ```

**No changes to pom.xml needed!** The build system automatically discovers new packages.

---

## Scenario 3: Adding a New JDK Era

### Example: Adding JDK 19 Exercises (Future Feature Preview)

**Problem:** JDK 19 introduces new preview features you want to practice

**Steps:**

1. **Create the new era folder:**
   ```
   src/main/java/com/example/concurrency/era8_preview19/
   src/test/java/com/example/concurrency/era8_preview19/
   ```

2. **Organize by category:**
   ```
   src/main/java/com/example/concurrency/era8_preview19/
   ├── preview1/
   │   └── PreviewFeatureExample1.java
   ├── preview2/
   │   └── PreviewFeatureExample2.java
   └── INDEX.md
   ```

3. **Create INDEX.md at era level:**
   ```markdown
   # JDK 19 Preview Features Exercises
   
   ## Overview
   Exercises exploring new preview APIs introduced in JDK 19
   
   ## Categories
   - preview1/
   - preview2/
   ```

4. **Update root documentation:**
   - Add new era to `java_concurrency_exercises.md`
   - Add new era to `exercises_tracking.md`
   - Update REPOSITORY_STRUCTURE.md if desired

**Maven Configuration:** No changes needed! Maven automatically discovers new packages.

---

## Scenario 4: Adding a New Type of Exercise (e.g., Performance Analysis)

### Example: Adding "Performance Analysis" Exercises

**Problem:** You want to add exercises focused purely on performance comparison and optimization

**Steps:**

1. **Create a new top-level category (optional):**
   ```
   src/main/java/com/example/concurrency/performance_analysis/
   ├── MemoryOverheadComparison.java
   ├── ContentionAnalysis.java
   └── ScalabilityBenchmark.java
   ```

2. **Or integrate into existing structure:**
   ```
   src/main/java/com/example/concurrency/util/
   ├── performance/
   │   ├── BenchmarkFramework.java
   │   └── PerformanceComparator.java
   ```

3. **Create corresponding benchmarks:**
   ```
   benchmarks/src/main/java/com/example/concurrency/benchmarks/
   ├── MemoryOverheadBenchmark.java
   ├── ContentionBenchmark.java
   └── ScalabilityBenchmark.java
   ```

---

## Scenario 5: Adding Exercises from External Sources

### Example: Adding Exercises from LeetCode or HackerRank

**Problem:** You find great concurrency interview problems on LeetCode

**Steps:**

1. **Create a dedicated category for these:**
   ```
   src/main/java/com/example/concurrency/interviews/
   ├── leetcode/
   │   ├── PrintInOrder.java
   │   ├── FizzBuzzMultithread.java
   │   └── BarrierIndex.md (links to problems)
   └── hackerrank/
       └── (similar structure)
   ```

2. **Document the source:**
   ```java
   /**
    * Problem: Print in Order (LeetCode #1114)
    * Link: https://leetcode.com/problems/print-in-order/
    * Tags: #Virtual Threads, #Synchronization
    */
   public class PrintInOrder { ... }
   ```

3. **Update tracking document with source reference**

---

## Best Practices for Adding Exercises

### 1. **Naming Consistency**
```
✅ Good:
- PingPongGame.java (describes the problem)
- WebAggregator.java (clear purpose)
- LRUCache.java (well-known algorithm name)

❌ Avoid:
- Exercise1.java (not descriptive)
- Test.java (too vague)
- Foo.java (meaningless)
```

### 2. **Documentation Pattern**
Always include a JavaDoc comment at the top of the class:
```java
/**
 * Exercise: The Ping-Pong Game
 * 
 * Problem:
 * Create two threads, "Ping" and "Pong", that must print their names 
 * alternately using wait() and notify().
 * 
 * Key Concepts:
 * - Monitor locks (synchronized)
 * - wait()/notify() for inter-thread signaling
 * - Spurious wakeup handling
 * 
 * Reference: java_concurrency_exercises.md (Section 1, Monitor Pattern)
 * 
 * Difficulty: ⭐ (Beginner)
 * Estimated Time: 1-2 hours
 */
public class PingPongGame { ... }
```

### 3. **Test Coverage**
Always create corresponding tests:
```
Exercise created → Test created immediately (same commit/PR)
```

### 4. **Benchmarks for Performance Exercises**
If it's a performance-focused exercise, add a JMH benchmark:
```
src/main/java/com/example/concurrency/era2_parallelism/
└── WebAggregator.java

benchmarks/src/main/java/com/example/concurrency/benchmarks/
└── WebAggregatorBenchmark.java
```

---

## Tracking New Exercises

When you add a new exercise:

1. **Update `docs/exercises_tracking.md`:**
   - Add to appropriate section
   - Update progress counter
   - Include difficulty, concepts, and APIs

2. **Optional: Update `docs/java_concurrency_exercises.md`:**
   - Add to the detailed guide if it's substantial
   - Update summary tables

3. **Create INDEX.md in the category folder (when category reaches 3+ exercises):**
   ```markdown
   # Category: Monitor Patterns
   
   ## Overview
   Exercises using wait()/notify() for inter-thread coordination
   
   ## Exercises
   1. PingPongGame - Strict alternation
   2. GuardedSuspension - Complex state waiting
   3. MessageBus - Broadcasting to multiple threads
   4. TaskQueueWithPriority - Priority-based waiting (NEW)
   ```

---

## Scaling Considerations

### Current Structure Supports:
- ✅ Up to 100+ exercises without issues
- ✅ Multiple exercises per category
- ✅ New categories added dynamically
- ✅ New JDK eras added without restructuring
- ✅ Mixed organization (by era, by API, by topic)

### When to Reorganize (Optional):
You might want to reorganize if:
- A single category exceeds 20+ exercises
  - **Solution:** Split into subcategories
  ```
  monitor/
  ├── basic/
  ├── advanced/
  └── expert/
  ```

- Multiple eras share similar patterns
  - **Solution:** Create a cross-cutting category
  ```
  src/main/java/com/example/concurrency/
  ├── patterns/
  │   ├── producer_consumer/
  │   ├── pipeline/
  │   └── barrier/
  ├── era1_classic/
  └── ...
  ```

---

## Maven Automatic Discovery

### Key Point: Maven automatically discovers:
- ✅ New Java source files in src/main/java/**
- ✅ New test files in src/test/java/**
- ✅ New packages (no pom.xml changes needed)
- ✅ New classes (no configuration needed)

**You only need to update pom.xml if:**
- Adding new dependencies
- Changing Java version (unlikely for JDK 25)
- Creating new modules (benchmarks already exists)

---

## Example: Growth Timeline

### Month 1: Initial 52 exercises
```
src/main/java/com/example/concurrency/
├── era1_classic/          (11 exercises)
├── era2_parallelism/      (7 exercises)
├── era3_reactive/         (3 exercises)
├── era4_loom/             (5 exercises)
├── era5_specialized/      (9 exercises)
├── era6_pipeline/         (5 exercises)
├── era7_niche/            (6 exercises)
└── senior/                (4 exercises)
```

### Month 6: Additional 20 exercises discovered
```
src/main/java/com/example/concurrency/
├── era1_classic/          (15 exercises) ← Added 4
├── era2_parallelism/      (9 exercises)  ← Added 2
├── era5_specialized/      (11 exercises) ← Added 2
├── interviews/            (12 exercises) ← NEW category
└── performance/           (8 exercises)  ← NEW category
```

### Year 1: 100+ exercises, multiple categories
```
src/main/java/com/example/concurrency/
├── era[1-4]/              (various)
├── patterns/              (cross-cutting)
├── interviews/            (LeetCode, HackerRank)
├── performance/           (optimization focus)
├── distributed/           (simulation exercises)
└── util/
```

**Structure remains clean and organized!**

---

## Checklist for Adding a New Exercise

```markdown
- [ ] Create exercise class in appropriate era/category folder
- [ ] Add JavaDoc with problem description and difficulty
- [ ] Create corresponding test class with comprehensive tests
- [ ] Add JMH benchmark if it's a performance exercise
- [ ] Update docs/exercises_tracking.md with new exercise
- [ ] Update INDEX.md in the category (if it exists)
- [ ] Optional: Add to docs/java_concurrency_exercises.md if substantial
- [ ] Run tests: `mvn clean test`
- [ ] Run benchmarks (if applicable): `mvn -f benchmarks/pom.xml jmh:benchmark`
```

---

## Summary

**Yes, this structure fully supports unlimited exercise additions because:**

1. **Package-based organization** - Maven auto-discovers all packages
2. **Category-based grouping** - Easy to add new categories within eras
3. **No structural constraints** - Add new eras, new categories, new exercises freely
4. **Scalable documentation** - INDEX.md files guide navigation
5. **Test mirroring** - Automatic parallel test structure
6. **Zero pom.xml maintenance** - New exercises don't require config changes

You can add 10 new exercises, 100 new exercises, or new entire eras without architectural changes. The structure will remain clean and organized.
