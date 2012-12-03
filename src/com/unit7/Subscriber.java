package com.unit7;

/**
 * Created with IntelliJ IDEA.
 * File: Subscriber
 * User: unit7
 * Date: 02.12.12
 * Time: 18:44
 */
public class Subscriber {
    public Subscriber(int[] requests, double[] times, int id) {
        for(int i = 0; i < requests.length; ++i)
            requestTime += requests[i];

        for(int i = 0; i < times.length; ++i)
            workTime += times[i];

        requestTime /= requests.length;
        workTime /= times.length;

        this.id = id;
    }

    public void nextTime(double currentTime) {
        timeIn = -Math.log(Math.random()) / requestTime + currentTime;
        timeOut = workTime + timeIn;
    }

    public double getTimeIn() {
        return timeIn;
    }

    public double getTimeOut() {
        return timeOut;
    }

    public double getRequestTime() {
        return requestTime;
    }

    public double getWorkTime() {
        return workTime;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Subscriber))
            return false;

        return ((Subscriber) obj).id == id;
    }

    private double timeIn;
    private double timeOut;
    private double requestTime;
    private double workTime;

    private int id;
}
