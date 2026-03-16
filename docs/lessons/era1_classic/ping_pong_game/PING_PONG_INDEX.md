# Monitor Pattern Lessons - JDK 5 Classic Era

Comprehensive lessons on using `wait()` and `notify()` for inter-thread coordination.

## Learning Path

### 1. [Monitor Pattern: Coordination (monitor_pattern_coordination.md)](monitor_pattern_coordination.md)
**Foundation - Start here!**

Learn the fundamentals of monitor-based synchronization:
- Core components: lock object, volatile state, shared data
- The basic coordination pattern with while loops
- Why `while` is essential, not just `if`
- Spurious wakeup handling
- Real PingPongGame example with execution timeline
- Common mistakes and how to fix them
- Why this pattern is important (foundation for modern APIs)

**Key Takeaway:** Master the basics of wait/notify before moving to optimizations.

---

### 2. [Monitor Pattern: Alternatives (monitor_pattern_alternatives.md)](monitor_pattern_alternatives.md)
**Better Patterns - Learn cleaner approaches**

Six superior patterns for 2-thread coordination:
1. **Named State Enum** - Self-documenting instead of boolean
2. **Extract Wait/Act Logic** - Separate methods for clarity
3. **ReentrantLock + Condition** - Modern JDK 5+ approach
4. **Multiple Conditions** - For complex scenarios (Producer-Consumer)
5. **Semaphore** - Simple, elegant, hard to misuse
6. **Exchanger** - For buffer swapping patterns

Comparison table, recommendations by scenario, and when to use each.

**Key Takeaway:** Use Enum or Semaphore, never a simple boolean flag.

---

### 3. [Monitor Pattern: Generic/DRY (monitor_pattern_generic.md)](monitor_pattern_generic.md)
**Advanced - Eliminate code duplication**

Four approaches to remove duplication:
1. **Generic Method** - Extract common pattern (RECOMMENDED for 2 threads)
2. **Worker Interface** - FunctionalInterface for tasks
3. **Builder Pattern** - Fluent API for configuration
4. **Strategy + Registry** - For N threads (3+)

Learn how to use lambdas and generics to write DRY code.

**Key Takeaway:** Extract the common pattern; pass turns and actions as parameters.

---

## Quick Reference Table

| Lesson | Focus | Best For | Difficulty |
| :--- | :--- | :--- | :--- |
| Coordination | Fundamentals | Learning | ⭐ |
| Alternatives | Pattern comparison | Production code | ⭐⭐ |
| Generic/DRY | Code reuse | Advanced design | ⭐⭐⭐ |

---

## Recommended Reading Order

### If You're Learning Concurrency:
1. Start with **Coordination** lesson (understand the basics)
2. Read **Alternatives** lesson (learn better patterns)
3. Study **Generic/DRY** lesson (see how to refactor)

### If You're Writing Production Code:
1. Skim **Coordination** (understand the foundation)
2. Focus on **Alternatives** (pick the best pattern)
3. Apply **Generic/DRY** (eliminate duplication)

### If You're Interviewing:
1. Master **Coordination** (explain the basics clearly)
2. Mention **Alternatives** (show you know options)
3. Show **Generic/DRY** refactoring (impress with design)

---

## Exercises Using These Lessons

- **PingPongGame** - Demonstrates the monitor pattern
  - Located: `src/main/java/com/example/concurrency/era1_classic/monitor/PingPongGame.java`
  - Test: `src/test/java/com/example/concurrency/era1_classic/monitor/PingPongGameTest.java`

---

## Key Concepts Covered

### Synchronization Primitives
- `synchronized` keyword and intrinsic locks
- `Object.wait()` - Release lock and wait
- `Object.notify()` / `Object.notifyAll()` - Wake waiting threads
- `volatile` keyword - Memory visibility

### Design Patterns
- Monitor pattern (classic)
- Enum-based state (self-documenting)
- Strategy pattern (behavior parameterization)
- Builder pattern (fluent configuration)

### Best Practices
- Always use `while`, not `if` (spurious wakeup handling)
- Use `notifyAll()`, not `notify()`
- Extract common patterns to avoid duplication
- Use lambdas for simple actions
- Prefer `ReentrantLock` + `Condition` in production

---

## Common Pitfalls

1. ❌ Using `if` instead of `while`
2. ❌ Not synchronizing state checks
3. ❌ Calling `wait()` outside `synchronized` block
4. ❌ Using `notify()` instead of `notifyAll()`
5. ❌ Duplicating the same synchronization pattern

---

## Next Steps

After mastering these lessons:
1. Implement more exercises from Era 1 (Bounded Buffer, Dining Philosophers)
2. Study synchronization alternatives in other categories
3. Move to `ReentrantLock` lessons for modern approaches
4. Explore `Phaser` for more complex coordination

---

## Related Topics

- **era1_classic/locks/** - ReentrantLock and Condition (modern version)
- **era1_classic/synchronization/** - Other synchronization primitives
- **era5_specialized/** - Semaphore, Exchanger, other utilities
- **era6_pipeline/** - Phaser for multi-stage coordination

---

*These lessons form the foundation for understanding all Java concurrency patterns.*
