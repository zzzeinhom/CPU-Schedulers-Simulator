package org.example;

public class Process implements Comparable<Process> {
    private final String name;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    private int remainingBurstTime;
    private int completionTime;
    private int turnAroundTime;
    private int waitingTime;
    private int queueEntryTime;

    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
        this.priority = priority;

    }

    public int getQueueEntryTime() {
        return queueEntryTime;
    }

    public void setQueueEntryTime(int queueEntryTime) {
        this.queueEntryTime = queueEntryTime;
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }
    public void setRemainingBurstTime(int remainingBurstTime) {
        this.remainingBurstTime = remainingBurstTime;
    }
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    @Override
    public int compareTo(Process other) {
        return Integer.compare(this.burstTime, other.burstTime);
    }


    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                '}';
    }
}
