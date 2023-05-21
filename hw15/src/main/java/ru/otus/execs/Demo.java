package ru.otus.execs;

public class Demo {
    private int lastThread = 2;

    public static void main(String[] args) throws InterruptedException {
        var demo = new Demo();

        var thread1 = new Thread(() -> demo.inc(2));
        var thread2 = new Thread(() -> demo.inc(1));

        thread1.start();
        thread2.start();
    }

    public synchronized void inc(int threadNumber) {
        int inc = 1;
        boolean reversed = false;

        while (inc != 0) {
            printNumber(threadNumber, inc);

            if (inc == 10) {
                reversed = true;
            }

            inc = reversed ? inc - 1 : inc + 1;
        }

    }

    public synchronized void printNumber(int threadNumber, int number) {
        try {
            while (lastThread == threadNumber) {
                wait();
            }

            lastThread = threadNumber;

            System.out.println("Thread " + lastThread + " " + number);

            notify();
        } catch (InterruptedException ex) {
            System.out.println("Thread " + threadNumber + " interrupted");
        }
    }
}
