package com.hayt.devicejudge;

/**
 * Author: Haytham Teng
 * Date: 13-8-29
 * Time: 上午10:59
 * hevlhayt@foxmail.com
 */

public class Stopwatch {

    private final long start;

    /**
     * Create a stopwatch object.
     */
    public Stopwatch() {
        start = System.currentTimeMillis();
    }


    /**
     * Return elapsed time (in seconds) since this object was created.
     */
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start) / 1000.0;
    }

    public static void main(String[] args) {
        Stopwatch s = new Stopwatch();
        System.out.println(s.elapsedTime());
    }

}
