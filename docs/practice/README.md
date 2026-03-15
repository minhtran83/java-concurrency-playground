# Java Concurrency Practice Exercises

After studying the lessons, implement the exercises here to solidify your understanding.

## Structure

```
docs/practice/
└── era1_classic/
    ├── monitor/
    │   └── README.md              (Exercise details & guidance)
    ├── synchronization/           (Coming soon)
    ├── executors/                 (Coming soon)
    ├── atomics/                   (Coming soon)
    └── locks/                     (Coming soon)

src/main/java/com/practice/concurrency/
└── era1_classic/
    ├── monitor/                   (Your implementations go here)
    ├── synchronization/
    ├── executors/
    ├── atomics/
    └── locks/

src/test/java/com/practice/concurrency/
└── era1_classic/
    ├── monitor/                   (Your test cases go here)
    ├── synchronization/
    ├── executors/
    ├── atomics/
    └── locks/
```

## Learning Flow

### 1️⃣ Study the Lessons
```
docs/lessons/era1_classic/monitor/
├── monitor_pattern_coordination.md   (Foundations)
├── monitor_pattern_alternatives.md   (Better patterns)
└── monitor_pattern_generic.md        (Advanced refactoring)
```

### 2️⃣ Review Reference Implementation
```
com.lesson.concurrency.era1_classic.monitor.PingPongGame
com.lesson.concurrency.era1_classic.monitor.PingPongGameTest
```

### 3️⃣ Implement Your Own Version
```
com.practice.concurrency.era1_classic.monitor.PingPongGame
com.practice.concurrency.era1_classic.monitor.PingPongGameTest
```

### 4️⃣ Review & Refactor
- Compare your code with the lesson reference
- Identify improvements
- Implement alternative approaches

---

## Key Principles

### ✅ DO

- **Understand the pattern first** - Read all the lessons before coding
- **Implement from scratch** - Don't copy, understand and create
- **Write comprehensive tests** - Test edge cases, not just happy path
- **Iterate and refactor** - Your first version won't be perfect
- **Ask questions** - When something doesn't make sense
- **Experiment** - Try multiple implementations
- **Document your code** - Explain why, not just what

### ❌ DON'T

- **Copy-paste** - This won't help learning
- **Skip the lessons** - The lessons explain the "why"
- **Write minimal tests** - Tests reveal understanding gaps
- **Give up on first error** - Errors are learning opportunities
- **Implement without understanding** - Memorization ≠ understanding
- **Ignore edge cases** - That's where bugs hide
- **Skip refactoring** - Improvement is part of learning

---

## Exercise Format

Each exercise includes:

1. **Problem Statement** - What to build
2. **Requirements** - Must-have features
3. **Step-by-Step Guide** - How to approach it
4. **Comparison Guide** - How your code compares to reference
5. **Common Mistakes** - What to avoid
6. **Testing Guidance** - How to verify correctness
7. **Reflection Questions** - What you should understand
8. **Submission Checklist** - Verification before completion

---

## Testing Your Practice Code

### Unit Tests Only (default)
```bash
# Run your practice tests
mvn test -Dtest=com.practice.concurrency.**.*Test
```

### Compare with Lesson Tests
```bash
# Run lesson reference tests
mvn test -Dtest=com.lesson.concurrency.**.*Test

# Run both
mvn test
```

### Compile Check
```bash
# Ensure your code compiles
mvn clean compile
```

### Full Build
```bash
# Complete validation
mvn clean install
```

---

## Progress Tracking

Track your practice implementations:

### Era 1: Classic (JDK 5)

#### Monitor Pattern
- [ ] **PingPongGame** - Strict turn-taking with wait/notify
  - Difficulty: ⭐
  - Time: 1-3 hours
  - Status: _________

#### Synchronization (Coming Soon)
- [ ] **CountDownLatch** - One-time barrier
- [ ] **CyclicBarrier** - Multi-stage barrier
- [ ] **Semaphore** - Permit-based access

#### Executors (Coming Soon)
- [ ] **CustomThreadPool** - Build ExecutorService
- [ ] **ThreadPoolBehavior** - Understand pool dynamics

#### Atomics (Coming Soon)
- [ ] **AtomicCounter** - Compare with synchronized
- [ ] **LockFreeStack** - Non-blocking structure

#### Locks (Coming Soon)
- [ ] **ReentrantLockBasics** - Explicit lock usage
- [ ] **ConditionVariables** - Multi-condition coordination

---

## Levels of Practice

### Level 1: Supervised Learning 👶
- Code along with lessons
- Copy structure, understand each part
- All tests provided
- Focus on understanding

### Level 2: Guided Practice 👧
- Reference implementation available
- Tests are yours to write
- Hints provided
- Focus on implementation

### Level 3: Independent Practice 👦
- Only problem statement
- You write tests
- You write implementation
- Focus on design decisions

### Level 4: Expert Review 👨
- Multiple implementation approaches
- Performance comparison
- Architecture decisions
- Focus on trade-offs

---

## Common Questions

### "Can I look at the reference implementation?"
**Yes!** That's the whole point. But:
- Read the lessons FIRST
- Try to implement FIRST
- Use reference to verify, not as a crutch
- Understand WHY, not just HOW

### "What if my code looks different?"
**Great!** Different doesn't mean wrong. Ask:
- Does it pass all tests?
- Is it clear and maintainable?
- Does it handle edge cases?
- Can you explain the design?

### "How do I know if I'm done?"
Complete the **Submission Checklist** at the bottom of each exercise README.

### "What if I'm stuck?"
1. Review the relevant lesson
2. Study the reference implementation
3. Read the test cases
4. Check "Common Mistakes" section
5. Ask a specific question

### "Should I try different implementations?"
**Absolutely!** Try:
- Boolean vs Enum for state
- Synchronized vs ReentrantLock
- Manual wait/notify vs Semaphore
- Extracted methods vs inline
- See which you prefer and why

---

## Review Process

When your practice code is ready:

### Self Review (Before asking for feedback)
- [ ] Does it compile?
- [ ] Do all tests pass?
- [ ] Is the code clear?
- [ ] Did you handle edge cases?
- [ ] Can you explain every line?

### Peer/Mentor Review (Ask for feedback)
- Code structure and organization
- Test coverage completeness
- Edge case handling
- Performance considerations
- Alternative approaches

### Reference Comparison
Compare with `com.lesson` implementation:
- Similarities - What did you get right?
- Differences - Why chose differently?
- Improvements - What could be better?
- Lessons - What did you learn?

---

## Next Steps After Each Exercise

1. **Verify** - All tests pass, no errors
2. **Understand** - Can you explain it?
3. **Document** - Add comments where unclear
4. **Refactor** - Can you improve it?
5. **Alternatives** - Try different approaches
6. **Move On** - Ready for next exercise

---

## Goals of Practice

By completing practice exercises, you will:

✅ **Understand** how concurrency patterns work
✅ **Implement** correct, thread-safe code
✅ **Test** concurrent code properly
✅ **Refactor** to eliminate duplication
✅ **Design** with concurrency in mind
✅ **Debug** concurrency problems
✅ **Choose** appropriate patterns for scenarios

---

## Need Help?

### Resources
- **Lessons:** `docs/lessons/era1_classic/`
- **Reference Code:** `com.lesson.concurrency.era1_classic`
- **Utilities:** `com.example.concurrency.util`
- **Test Examples:** Look at lesson tests

### Common Issues
- Check "Common Mistakes" in each exercise
- Review lesson material again
- Study the reference implementation
- Run tests with verbose output: `mvn test -e`

---

*The goal is mastery through doing, not memorization.* 🚀

Start with **[Monitor Pattern Practice](era1_classic/monitor/README.md)**
