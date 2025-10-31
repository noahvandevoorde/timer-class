package program;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimerTest {

    private Timer timer;

    @BeforeEach
    void setup() {
        timer = new Timer(); // fresh timer for each test
    }

    // Test static methods

    @Test
    void testNanoToSeconds() {
        assertEquals(1.0, Timer.nanoToSeconds(1_000_000_000L), 1e-9);
        assertEquals(0.5, Timer.nanoToSeconds(500_000_000L), 1e-9);
        assertEquals(0.00001, Timer.nanoToSeconds(10_000L), 1e-9);
    }

    @Test
    void testSecondsToNano() {
        assertEquals(1_000_000_000L, Timer.secondsToNano(1.0));
        assertEquals(2_500_000_000L, Timer.secondsToNano(2.5));
        assertEquals(10_000L, Timer.secondsToNano(0.00001));
    }

    @Test
    void testGetTimeIncreases() {
        long t1 = Timer.getTime();
        Timer.sleep(0.01);
        long t2 = Timer.getTime();
        assertTrue(t2 > t1);
    }

    @Test
    void testGetTimeIncreasesSeconds() {
        double s1 = Timer.getTimeSeconds();
        Timer.sleep(0.01);
        double s2 = Timer.getTimeSeconds();
        assertTrue(s2 > s1);
    }

    @Test
    void testSleepNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> Timer.sleep(-1));
    }

    // Test constructors

    @Test
    void testDefaultConstructor() {
        assertEquals(Timer.TimerState.INACTIVE, timer.getState());
        assertTrue(timer.getId() > 0);
    }

    @Test
    void testStateConstructorActive() {
        Timer t = new Timer(Timer.TimerState.ACTIVE);
        assertEquals(Timer.TimerState.ACTIVE, t.getState());
    }

    @Test
    void testStateConstructorInactive() {
        Timer t = new Timer(Timer.TimerState.INACTIVE);
        assertEquals(Timer.TimerState.INACTIVE, t.getState());
    }

    @Test
    void testStateConstructorCompleted() {
        Timer t = new Timer(Timer.TimerState.COMPLETED);
        assertEquals(Timer.TimerState.COMPLETED, t.getState());
    }

    @Test
    void testCopyConstructor() {
        timer.start();
        Timer.sleep(0.01);
        timer.end();
        Timer copy = new Timer(timer);
        assertEquals(timer.getState(), copy.getState());
        assertEquals(timer.getStartTime(), copy.getStartTime());
        assertEquals(timer.getEndTime(), copy.getEndTime());
    }

    // Start, End, Resume, complete methods

    @Test
    void testStart() {
        timer.start();
        assertEquals(Timer.TimerState.ACTIVE, timer.getState());
        assertTrue(timer.getStartTime() > 0);
    }

    @Test
    void testStartEnd() {
        timer.start();
        Timer.sleep(0.01);
        timer.end();
        assertEquals(Timer.TimerState.INACTIVE, timer.getState());
        assertTrue(timer.getEndTime() >= timer.getStartTime());
    }

    @Test
    void testStartEndResume() {
        timer.start();
        Timer.sleep(0.01);
        timer.end();
        long elapsedBefore = timer.getElapsedTime();
        timer.resume();
        assertEquals(Timer.TimerState.ACTIVE, timer.getState());
        assertTrue(timer.getStartTime() < Timer.getTime());
        assertTrue(timer.getElapsedTime() >= elapsedBefore);
    }

    @Test
    void testStartComplete() {
        timer.start();
        timer.complete();
        assertEquals(Timer.TimerState.COMPLETED, timer.getState());
    }

    @Test
    void testCompleteStart() {
        timer.complete();
        timer.start();
        assertEquals(Timer.TimerState.COMPLETED, timer.getState());
    }

    @Test
    void testEnd() {
        timer.end();
        assertEquals(Timer.TimerState.INACTIVE, timer.getState());
    }

    // Test getter methods

    @Test
    void testGetElapsedTime() {
        timer.start();
        long e1 = timer.getElapsedTime();
        Timer.sleep(0.01);
        long e2 = timer.getElapsedTime();
        assertTrue(e2 >= e1);
    }

    @Test
    void testGetStartAndEndTimeConversions() {
        timer.start();
        Timer.sleep(0.01);
        timer.end();
        assertEquals(Timer.nanoToSeconds(timer.getStartTime()), timer.getStartTimeSeconds(), 1e-9);
        assertEquals(Timer.nanoToSeconds(timer.getEndTime()), timer.getEndTimeSeconds(), 1e-9);
    }

    // Equals, toString

    @Test
    void testEquals() {
        Timer t1 = new Timer();
        Timer t2 = new Timer();
        assertNotEquals(t1, t2); // unique IDs
        assertEquals(t1, t1); // reflexive
    }

    @Test
    void testToString() {
        String str = timer.toString();
        assertTrue(str.contains("State"));
        assertTrue(str.contains("Timer"));
    }

    @Test
    void testToStringAll() {
        Timer t1 = new Timer();
        Timer t2 = new Timer(Timer.TimerState.ACTIVE);
        String all = Timer.toStringAll();
        assertTrue(all.contains("Timer"));
    }
    
    @Test
    void testToStringActive() {
        Timer t1 = new Timer();
        Timer t2 = new Timer(Timer.TimerState.ACTIVE);
        String active = Timer.toStringAllActive();
        assertTrue(active.contains("Timer"));
    }
}