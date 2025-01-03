package org.example;
import javafx.scene.paint.Color; //for color

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
    private double effectiveBurstTime;

    private Color color;

    public Process(String name, int arrivalTime, int burstTime, int priority, Color color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
        this.priority = priority;
        this.effectiveBurstTime = burstTime;
        this.color = color;
    }

        //for backward compatibility
    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this(name, arrivalTime, burstTime, priority, Color.GRAY); // Default color is gray
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
    public void setEffectiveBurstTime() {
        effectiveBurstTime = Math.max(0, burstTime - waitingTime);
    }

    public double getEffectiveBurstTime() {
        return effectiveBurstTime;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor() {
        return color;
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
