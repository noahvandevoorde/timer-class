package program;

import java.util.ArrayList;

public class Timer {
	//Lists for timers
	private static ArrayList<Timer> timers = new ArrayList<Timer>();
	private static ArrayList<Timer> activeTimers = new ArrayList<Timer>();
	
	/**Enums for states
	 * <ul>
	 * <li>INACTIVE: Available to use and stored in timers list. Currently not in use. </li>
	 * <li>ACTIVE: Available to use and stored in timers list and activeTimers list. Currently in use. </li>
	 * <li>COMPLETED: Not available to use. Removed from lists. </li>
	 * </ul>
	 */
	public static enum TimerState {
		INACTIVE,
		ACTIVE,
		COMPLETED,
	}

	/**number of total timer objects.*/
	private static int count = 0;
	
	/**Constant*/
	private static final int NANO_TO_SECOND_MULTIPLIER = (int) Math.pow(10, 9);
	/**If not a sleep time passed, will use this*/
	private static final int BASE_SLEEP_TIME = 1;
	
	/**This is the saved time for when the program started**/
	private static final long PROGRAM_START_TIME = System.nanoTime();
	/*==============================================*/
	
	/**This is the current state of the timer object.*/
	private TimerState state = TimerState.INACTIVE;
	
	/**This is the start time of the timer in nanoseconds.*/
	private long startTime = 0;
	
	/**This is the end time of the timer in nanoseconds. */
	private long endTime = 0;
	
	/**This is the unique integer identifier for the timer object.*/
	private final int id;
	/*==============================================*/
	
	/**The base constructor for the Timer class. Creates and initiates a timer object, but does not do anything else.*/
	public Timer() {
		timers.add(this);
		count++;
		id = count;
	}
	
	/**A constructor for the Timer class. Creates and initiates a timer object, and acts based on the passed state.*/
	public Timer(TimerState s) {
		this();
		
		if (s == TimerState.ACTIVE) {
			start();
		} else if (s == TimerState.INACTIVE) {
			end();
		} else if (s == TimerState.COMPLETED) {
			complete();
		}
	}
	
	public Timer(Timer other) {
		this(other.getState());
		setStartTime(other.getStartTime());
		setEndTime(other.getEndTime());
	}
	/*==============================================*/
	
	/**
	 *  Returns the passed value, t, to seconds from nanoseconds.
	 * @Param t: a value in nanoseconds
	 * @return the passed value, t, to seconds from nanoseconds.
	 */
	public static double nanoToSeconds(long t) {
		return t / (double) NANO_TO_SECOND_MULTIPLIER;
	}
	
	/** 
	 * Returns the passed value, t, to nanoseconds from seconds.
	 * @Param t: a value in seconds
	 * @return the passed value, t, to nanoseconds from seconds.
	 */
	public static long secondsToNano(double t) {
		return (long) (t * NANO_TO_SECOND_MULTIPLIER);
	}
	
	/**
	 * Returns the current time in nanoseconds.
	 * 
	 * @return the current time in nanoseconds.
	 */
	public static long getTime() {
		return System.nanoTime() - PROGRAM_START_TIME;
	}
	
	/** 
	 * Returns the current time in seconds.
	 * 
	 * @return the current time in seconds.
	 */
	public static double getTimeSeconds() {
		return nanoToSeconds(getTime());
	}
	
	/**
	 * Returns the total count of timer objects.
	 * 
	 * @return the total count of timer objects.
	 */
	public static int getCount() {
		return count;
	}
	

	public static void sleep() {
		sleep(BASE_SLEEP_TIME);
	}
	
	/**
	 * Thread.sleep() wrapper that pauses a thread for argument t (seconds).
	 * 
	 * @param t		time in seconds
	 */
	public static void sleep(double t) {
		if (t < 0) throw new IllegalArgumentException("Sleep time must be non-negative.");
		try {
			Thread.sleep((long) (t * 1000));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.err.println("Sleep interrupted");
		}
	}
	/*==============================================*/
	/**
	 * Starts the timer.
	 * Stops the timer if it's already running, adds it to the active timers list,
	 * sets its state to ACTIVE, and records the start time.
	 */
	public void start() {
		if (state == TimerState.COMPLETED) return;
		end();
		activeTimers.add(this);
		state = TimerState.ACTIVE;
		startTime = getTime();
	}
	
	/**
	 * Resumes a paused timer.
	 * Only works if the timer is currently INACTIVE (paused).
	 * Adjusts the start time to preserve elapsed time, sets state to ACTIVE,
	 * and adds the timer back to the active timers list if necessary.
	 */
	public void resume() {
		if (state == TimerState.COMPLETED || state == TimerState.ACTIVE) return;

		startTime = getTime() - getElapsedTime();
	    state = TimerState.ACTIVE;

	    if (!activeTimers.contains(this)) {
	        activeTimers.add(this);
	    }
	}
	
	/**
	 * Ends the timer.
	 * Removes it from the active timers list, sets its state to INACTIVE,
	 * and records the end time.
	 */
	public void end() {
		if (state == TimerState.COMPLETED || state != TimerState.ACTIVE) return;
		activeTimers.remove(this);
		state = TimerState.INACTIVE;
		setEndTime(getTime());
	}
	
	/**
	 * Completes the timer;
	 * Ends the timer and sets its state to COMPLETED.
	 * Given that the timer is no longer useful, will be removed from the timers list.
	 */
	public void complete() {
		if (state == TimerState.COMPLETED) return;
		end();
		state = TimerState.COMPLETED;
		timers.remove(this);
	}
	/*==============================================*/
	
	public long getStartTime() {
		return startTime;
	}
	
	public double getStartTimeSeconds() {
		return nanoToSeconds(getStartTime());
	}
	
	private void setStartTime(long t) {
		startTime = t;
	}
	/*==============================================*/
	
	public long getEndTime() {
		//If the timer is ongoing, set endTime to the current time
		if (state == TimerState.ACTIVE) {
			setEndTime(getTime());
		}
		return endTime;
	}
	
	public double getEndTimeSeconds() {
		return nanoToSeconds(getEndTime());
	}
	
	private void setEndTime(long t) {
		endTime = t;
	}
	/*==============================================*/
	
	/** 
	 * Returns the difference of endTime and startTime in nanoseconds.
	 * 
	 * @return the difference of endTime and startTime in nanoseconds.
	 */
	public long getElapsedTime() {
		return getEndTime() - getStartTime();
	}
	
	/** 
	 * Returns the difference of endTime and startTime in seconds.
	 * 
	 * @return the difference of endTime and startTime in seconds.
	 */
	public double getElapsedTimeSeconds() {
		return nanoToSeconds(getElapsedTime());
	}
	/*==============================================*/
	
	/** 
	 * Returns the state of the timer object.
	 * 
	 * @return the current state of the timer object.
	 */
	public TimerState getState() {
		return state;
	}

	/** 
	 * Returns the unique integer ID for the timer object.
	 * 
	 * @return the unique integer ID for the timer object.
	 */
	public int getId() {
		return id;
	}
	/*==============================================*/
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj.getClass() != this.getClass()) return false;
		Timer other = (Timer) obj;
		
		return this.getId() == other.getId();
	}
	
	@Override
	public String toString() { 
		String identifier = "Timer: " + id + "\n";
		String timing = "\t[Start(s): " + getStartTimeSeconds() + " | End(s): " + getEndTimeSeconds() + " | Elapsed(s): " + getElapsedTimeSeconds() + "]\n";
		String desc = "\t[State: " + getState() + "]\n";
		
		return identifier + timing + desc;
	}
	
	/** 
	 * Returns a string representation of all the timers.
	 * 
	 * @return a string representation of all the timers.
	 */
	public static String toStringAll() {
		String s = "";
		for (int i = 0; i < timers.size(); i++) {
			s += timers.get(i).toString();
		}
		return s;
	}
	
	/** 
	 * Returns a string representation of all the active timers currently running.
	 * 
	 * @return a string representation of all the active timers currently running.
	 */
	public static String toStringAllActive() {
		String s = "";
		for (int i = 0; i < activeTimers.size(); i++) {
			s += activeTimers.get(i).toString();
		}
		return s;
	}
}


