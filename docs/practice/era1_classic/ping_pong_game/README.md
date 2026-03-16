# Practice Exercises: Monitor Pattern (Era 1 - Classic)

This folder contains your practice implementations of monitor pattern exercises.

## Learning → Practice Workflow

1. **Study the lessons** in `docs/lessons/era1_classic/monitor/`
   - Read: `monitor_pattern_coordination.md`
   - Learn: `monitor_pattern_alternatives.md`
   - Understand: `monitor_pattern_generic.md`

2. **Review the reference implementation** in `com.lesson.concurrency.era1_classic.monitor.PingPongGame`
   - Understand how the pattern works
   - Study the synchronization logic
   - Review the test cases

3. **Implement your own version** in `com.practice.concurrency.era1_classic.monitor`
   - Create your own `PingPongGame.java`
   - Write your own `PingPongGameTest.java`
   - Use the lessons as a guide, not a copy

4. **Review and refactor**
   - Can you improve the code?
   - Can you use Enum instead of boolean?
   - Can you extract common patterns?
   - Can you eliminate duplication?

---

## Exercise: The Strict Turn-Taker (Ping-Pong)

### Problem
Create two threads, "Ping" and "Pong", that must print their names alternately using `wait()` and `notify()`.

**Expected output for 5 iterations:**
```
Ping, Pong, Ping, Pong, Ping, Pong, Ping, Pong, Ping, Pong
```

### Requirements

✅ **Functionality:**
- Two threads that print "Ping" and "Pong" alternately
- Strict turn-based coordination
- No race conditions
- Configurable iteration count

✅ **Code Quality:**
- Clear, self-documenting code
- Proper synchronization
- Handles spurious wakeups correctly
- Includes a `stop()` method for cleanup

✅ **Testing:**
- Unit tests verify alternation
- Tests verify no deadlock
- Tests verify thread termination
- All tests pass

✅ **Documentation:**
- JavaDoc on all public methods
- Explain the synchronization approach
- Document the state management

---

## Step-by-Step Guide

### Step 1: Create the Class Structure
```java
package com.practice.concurrency.era1_classic.monitor;

public class PingPongGame {
    // TODO: Add fields
    
    // TODO: Implement ping(int count)
    
    // TODO: Implement pong(int count)
    
    // TODO: Implement stop()
    
    // TODO: Implement main() for testing
}
```

### Step 2: Design Your State Management

**Option A: Boolean Flag** (Simplest, but less clear)
```java
private boolean pingTurn = true;
```

**Option B: Enum** (Recommended - clearer)
```java
private enum Turn { PING, PONG }
private Turn turn = Turn.PING;
```

**Choose one and explain why in a comment.**

### Step 3: Implement the Synchronization

Think about:
- What is the shared lock object?
- What is the condition to wait for?
- How do you switch turns?
- Why use `while` instead of `if`?

### Step 4: Write Comprehensive Tests

Your `PingPongGameTest.java` should test:
- [ ] Basic alternation pattern
- [ ] Single iteration
- [ ] Multiple iterations
- [ ] No deadlock (with timeout)
- [ ] Thread termination
- [ ] Interrupt handling
- [ ] Turn switching logic

### Step 5: Refactor and Improve

After getting it working, consider:
- [ ] Can you use the generic pattern from lesson 3?
- [ ] Can you extract `waitForMyTurn()` and `passTurn()` methods?
- [ ] Can you use Semaphore instead?
- [ ] Can you use ReentrantLock + Condition?

---

## Comparison: Your Code vs Lesson Reference

After you've implemented your version, compare with `com.lesson.concurrency.era1_classic.monitor.PingPongGame`:

**Similarities to look for:**
- Same synchronization pattern
- Same state management
- Same test coverage

**Differences to discuss:**
- Different code organization?
- Different naming choices?
- Different error handling?
- Which approach is clearer?

---

## Common Mistakes to Avoid

❌ **Using `if` instead of `while`**
```java
// WRONG
if (!pingTurn) lock.wait();

// CORRECT
while (!pingTurn) lock.wait();
```

❌ **Not handling InterruptedException**
```java
// WRONG
lock.wait();  // Ignores exception

// CORRECT
try {
    lock.wait();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

❌ **Forgetting to notify**
```java
// WRONG
synchronized(lock) {
    // Do work
    // Forgot notifyAll!
}

// CORRECT
synchronized(lock) {
    // Do work
    lock.notifyAll();
}
```

❌ **Not using volatile for shared flags**
```java
// WRONG
private boolean running = true;  // Not volatile!

// CORRECT
private volatile boolean running = true;
```

---

## Testing Your Implementation

### Run your tests:
```bash
mvn test -Dtest=PingPongGameTest
```

### Run with output capture:
```bash
mvn test -Dtest=PingPongGameTest -e
```

### Run the main method directly:
```bash
java -cp target/classes com.practice.concurrency.era1_classic.monitor.PingPongGame
```

---

## Levels of Completion

### Level 1: Basic Implementation ⭐
- [ ] Code compiles
- [ ] Basic alternation works
- [ ] Simple tests pass
- Estimated time: 1-2 hours

### Level 2: Production Quality ⭐⭐
- [ ] All edge cases handled
- [ ] Comprehensive tests
- [ ] JavaDoc documented
- [ ] Proper error handling
- Estimated time: 2-3 hours

### Level 3: Refactored & Optimized ⭐⭐⭐
- [ ] Multiple implementations (boolean, Enum, Semaphore, ReentrantLock)
- [ ] Generic extracted pattern
- [ ] Performance comparison
- [ ] All tests passing for all variants
- Estimated time: 3-5 hours

---

## Reflection Questions

After implementing, answer these questions:

1. **Understanding:**
   - What is the purpose of `synchronized(lock)`?
   - Why can't you use `if` instead of `while`?
   - What happens if you forget to `notifyAll()`?

2. **Design:**
   - Is boolean or Enum better for state management? Why?
   - Could you use a different synchronization primitive?
   - How would you extend this to 3+ threads?

3. **Testing:**
   - What edge cases did you test?
   - What could go wrong in production?
   - How would you test for deadlocks?

4. **Refactoring:**
   - Can you eliminate code duplication?
   - Can you extract common patterns?
   - How would you parameterize the work?

---

## Next Exercises

After mastering PingPongGame:
1. **Bounded Buffer** - Producer-Consumer with shared queue
2. **Dining Philosophers** - Deadlock prevention
3. **Custom Thread Pool** - Executor implementation
4. **Semaphore Example** - Alternative synchronization

---

## Resources

- **Lessons:** `docs/lessons/era1_classic/monitor/`
- **Reference:** `com.lesson.concurrency.era1_classic.monitor.PingPongGame`
- **Test Examples:** `com.lesson.concurrency.era1_classic.monitor.PingPongGameTest`
- **Utilities:** `com.example.concurrency.util.ThreadUtils`

---

## Submission Checklist

Before marking as complete:

- [ ] Code compiles: `mvn clean install`
- [ ] All tests pass: `mvn test -Dtest=PingPongGameTest`
- [ ] No warnings or errors
- [ ] JavaDoc on all public classes/methods
- [ ] At least 5 different test cases
- [ ] Proper exception handling
- [ ] Clear, readable code
- [ ] Package name: `com.practice.concurrency.era1_classic.monitor`

---

## Getting Help

If stuck:
1. **Review the lessons** - All answers are in the documentation
2. **Study the reference implementation** - See how it's done
3. **Read the test cases** - They show expected behavior
4. **Check common mistakes** - Above in this file
5. **Ask specific questions** - About synchronization or testing

---

*Remember: The goal is to understand, not to copy. Write your own code, make your own mistakes, learn from them!* 🚀
