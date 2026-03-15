# Learning Structure: Lessons → Practice → Mastery

This document explains the three-tier learning structure of this repository.

---

## Overview

The repository is organized to support a complete learning journey:

```
📚 LESSONS                      📝 PRACTICE                    ✅ MASTERY
(Learn the concepts)            (Apply what you learned)       (Full understanding)
        ↓                              ↓                              ↓
com.lesson.concurrency.*        com.practice.concurrency.*     Your expert knowledge
docs/lessons/...                docs/practice/...              
Reference implementations       Your implementations            
Tests & examples               Your tests                      
```

---

## Tier 1: Lessons (Learn)

### Location
- **Source Code:** `src/main/java/com/lesson/concurrency/`
- **Tests:** `src/test/java/com/lesson/concurrency/`
- **Documentation:** `docs/lessons/`

### Purpose
**Understand how patterns work** - Reference implementations and explanations.

### Structure
```
com.lesson.concurrency.era1_classic.monitor.PingPongGame
├── Complete implementation
├── Well-documented
├── Test examples
└── Shows best practices
```

### What You Do Here
1. ✅ Read the documentation in `docs/lessons/`
2. ✅ Study the reference implementation
3. ✅ Run the tests to see it work
4. ✅ Understand the "why" behind design choices
5. ✅ Learn from the code examples

### Example: Monitor Pattern Lessons
```
docs/lessons/era1_classic/monitor/
├── INDEX.md                              (Navigation guide)
├── monitor_pattern_coordination.md       (Foundations)
├── monitor_pattern_alternatives.md       (6 better patterns)
└── monitor_pattern_generic.md            (DRY refactoring)
```

Reference code:
```
com.lesson.concurrency.era1_classic.monitor.PingPongGame
com.lesson.concurrency.era1_classic.monitor.PingPongGameTest
```

---

## Tier 2: Practice (Apply)

### Location
- **Source Code:** `src/main/java/com/practice/concurrency/`
- **Tests:** `src/test/java/com/practice/concurrency/`
- **Documentation:** `docs/practice/`

### Purpose
**Apply what you learned** - Implement your own solutions after understanding.

### Structure
```
com.practice.concurrency.era1_classic.monitor.PingPongGame
├── Your implementation
├── Your tests
├── Your design decisions
└── Your learning journey
```

### What You Do Here
1. ✅ Read the problem statement in `docs/practice/`
2. ✅ Review the reference implementation (for guidance, not copying)
3. ✅ Implement your own version from scratch
4. ✅ Write comprehensive tests
5. ✅ Verify your understanding
6. ✅ Refactor and improve

### Example: Monitor Pattern Practice
```
docs/practice/era1_classic/monitor/README.md
├── Problem statement
├── Requirements
├── Step-by-step guide
├── Common mistakes
├── Testing guidance
├── Reflection questions
└── Submission checklist
```

Your code:
```
com.practice.concurrency.era1_classic.monitor.PingPongGame
com.practice.concurrency.era1_classic.monitor.PingPongGameTest
```

---

## Tier 3: Mastery (Master)

### What You Do
1. ✅ Complete all practice exercises
2. ✅ Understand every line of code
3. ✅ Compare your solutions with reference
4. ✅ Try alternative implementations
5. ✅ Teach it to someone else
6. ✅ Apply patterns in new contexts

### The Goal
**You can:**
- Explain the pattern clearly
- Implement it from scratch
- Choose appropriate patterns for scenarios
- Debug concurrency issues
- Refactor for clarity and performance
- Mentor others

---

## The Learning Flow

### For Each Exercise

```
1. STUDY LESSONS (1-2 hours)
   ├── Read documentation
   ├── Understand the "why"
   └── Review reference code
           ↓
2. IMPLEMENT PRACTICE (2-3 hours)
   ├── Write your own code
   ├── Create comprehensive tests
   └── Verify correctness
           ↓
3. REVIEW & REFACTOR (1-2 hours)
   ├── Compare with reference
   ├── Identify improvements
   ├── Try alternative approaches
   └── Document your learning
           ↓
4. MOVE TO NEXT EXERCISE
   └── You're now an expert in this pattern!
```

**Total Time Per Exercise:** 4-7 hours for deep understanding

---

## Package Naming Convention

### Lesson Package
```
com.lesson.concurrency.[era].[category].[subcategory]

Examples:
- com.lesson.concurrency.era1_classic.monitor.PingPongGame
- com.lesson.concurrency.era1_classic.synchronization.BoundedBuffer
- com.lesson.concurrency.era2_parallelism.forkjoin.FileSearch
```

### Practice Package
```
com.practice.concurrency.[era].[category].[subcategory]

Examples:
- com.practice.concurrency.era1_classic.monitor.PingPongGame
- com.practice.concurrency.era1_classic.synchronization.BoundedBuffer
- com.practice.concurrency.era2_parallelism.forkjoin.FileSearch
```

### Shared Utilities
```
com.example.concurrency.util

Examples:
- com.example.concurrency.util.ThreadUtils
- com.example.concurrency.util.BenchmarkRunner
```

---

## Documentation Structure

### Lessons Documentation
```
docs/lessons/
└── era1_classic/
    ├── INDEX.md                          (Era overview)
    └── monitor/
        ├── INDEX.md                      (Category navigation)
        ├── monitor_pattern_coordination.md
        ├── monitor_pattern_alternatives.md
        └── monitor_pattern_generic.md
```

### Practice Documentation
```
docs/practice/
├── README.md                             (Overview & flow)
└── era1_classic/
    └── monitor/
        └── README.md                     (Exercise details)
```

---

## Workflow Example: Monitor Pattern

### Step 1: Learn (Read + Understand)
```
docs/lessons/era1_classic/monitor/
├── Read: monitor_pattern_coordination.md      (2 hours)
├── Read: monitor_pattern_alternatives.md      (1.5 hours)
├── Read: monitor_pattern_generic.md           (1.5 hours)
└── Run & Review: com.lesson.*.PingPongGame    (1 hour)
```

**Time Investment:** 6 hours

**What You Know:** How monitor patterns work, why, and alternatives

### Step 2: Implement (Code + Test)
```
docs/practice/era1_classic/monitor/README.md  (Problem details)
     ↓
com.practice.concurrency.era1_classic.monitor.PingPongGame
     ↓
com.practice.concurrency.era1_classic.monitor.PingPongGameTest
     ↓
Run: mvn test -Dtest=PingPongGameTest
```

**Time Investment:** 3-4 hours

**What You Can Do:** Write correct, thread-safe monitor pattern code

### Step 3: Refactor (Compare + Improve)
```
Your code              Reference code
    ↓                       ↓
    └─→ Compare ←─────────┘
         ↓
    Identify improvements
         ↓
    Try alternatives (Enum, Semaphore, ReentrantLock)
         ↓
    Choose best approach & explain why
```

**Time Investment:** 2-3 hours

**What You Mastered:** Can implement and choose between multiple approaches

---

## Running Tests

### Your Practice Tests Only
```bash
mvn test -Dtest=com.practice.concurrency.**.*Test
```

### Lesson Reference Tests Only
```bash
mvn test -Dtest=com.lesson.concurrency.**.*Test
```

### Both (Full Build)
```bash
mvn clean install
```

### Specific Test
```bash
mvn test -Dtest=PingPongGameTest
```

---

## How to Use This Structure

### If You're New to Concurrency
1. Start with Tier 1 (Lessons)
2. Spend time understanding concepts
3. Review reference code multiple times
4. Move to Tier 2 (Practice) when confident
5. Implement slowly, test thoroughly

### If You Know Some Concurrency
1. Skim Lessons (1-2 hours per category)
2. Focus on "why" not "how"
3. Jump to Practice faster
4. Try alternative implementations early
5. Move through exercises quickly

### If You're Interviewing
1. Master the reference implementations
2. Understand all trade-offs
3. Practice explaining each choice
4. Be ready to implement on whiteboard
5. Know alternatives cold

---

## The Three Types of Understanding

### Level 1: "I Read It"
- You've read the lessons
- You can describe the pattern
- **But:** You haven't coded it yet

### Level 2: "I Implemented It"
- You wrote the code
- Your tests pass
- **But:** You don't fully understand trade-offs

### Level 3: "I Mastered It" ⭐
- You implemented it multiple ways
- You understand trade-offs
- You can teach it to others
- You can apply it to new problems
- **This is the goal!**

---

## Common Questions

### "Should I look at the lesson code?"
**Yes!** That's the whole point. But:
- Understand first, then look
- Don't copy-paste
- Understand why each choice was made

### "My code looks different - is that bad?"
**No!** Different isn't bad. Ask yourself:
- Does it pass all tests?
- Is it clear?
- Can I explain the design?
- If yes to all, you're good!

### "How much time should I spend?"
**Recommended per exercise:**
- Lessons: 4-6 hours
- Practice: 2-4 hours
- Refactoring: 1-2 hours
- **Total: 7-12 hours for deep mastery**

### "Can I skip lessons and go straight to practice?"
**Not recommended**, but you can try:
- You might miss important concepts
- You'll waste time on the practice part
- **Better:** Skim lessons (1-2 hours), then practice

---

## Success Criteria

You've achieved mastery when you can:

✅ **Explain**
- How the pattern works
- Why you'd use it
- Trade-offs vs alternatives

✅ **Implement**
- From scratch, from memory
- With comprehensive tests
- Handling edge cases

✅ **Design**
- Choose right pattern for scenario
- Identify potential issues
- Refactor for clarity

✅ **Debug**
- Find concurrency bugs
- Understand thread states
- Use thread dumps effectively

✅ **Teach**
- Explain to someone else
- Answer questions
- Show different approaches

---

## Next Steps

1. **Start here:** `docs/lessons/era1_classic/monitor/INDEX.md`
2. **Read lessons:** All 3 monitor pattern lessons
3. **Review code:** `com.lesson.concurrency.era1_classic.monitor.PingPongGame`
4. **Implement practice:** Create `com.practice.concurrency.era1_classic.monitor.PingPongGame`
5. **Write tests:** Create comprehensive test suite
6. **Review & refactor:** Compare with reference, try alternatives
7. **Move on:** You're an expert in monitor patterns!

---

## The Goal

After completing all practice exercises in this repository, you will have:

- 🎯 **Deep understanding** of concurrency patterns
- 💪 **Practical skills** to write thread-safe code
- 🧠 **Mental models** for concurrent design
- 🎓 **Interview readiness** for senior roles
- 📚 **Reference implementations** you can use
- 🚀 **Confidence** to tackle any concurrency problem

**Welcome to your concurrency mastery journey!** 🚀
