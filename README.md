# Timer Class in Java

###### A lightweight Java utility for precision timing, conversions between nanoseconds and seconds, and managing multiple timers. Ideal for benchmarking, profiling, sleeping, or time-sensitive applications.

## Features:
* Retrieve the current time
* Convert between nanoseconds(long) and seconds(double)
* Sleep for precise durations
* Manage multiple timers with start/stop/resume/complete functionality
* Unit-tested for accuracy

## Examples

### Measuring elapsed time:
```
long start = Timer.getTime();
Timer.sleep(0.01);
long end = Timer.getTime();
double elapsedSeconds = Timer.nanoToSeconds(end - start);
System.out.println("Elapsed time: " + elapsedSeconds + " seconds");
```
#### Converting between units:
```
long nanos = 10_000L;
double seconds = Timer.nanoToSeconds(nanos) // returns 0.00001 seconds;
long backToNanos = Timer.secondsToNano(seconds) // returns 10_000;
```
#### Using timer object:
```
Timer t1 = new Timer();
t1.start();
// do some work
t1.stop();
System.out.println(t1);
```

#### Cloning timer:
```
Timer t1 = new Timer();
t1.start();
Timer.sleep(1);
t1.stop();
Timer t2 = new Timer(t1);
System.out.println("Timer 1 duration: " + t1.getElapsedTimeSeconds() + " seconds");
System.out.println("Timer 2 duration: " + t2.getElapsedTimeSeconds() + " seconds");
```
