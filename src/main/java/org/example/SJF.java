package org.example;

import java.util.*;

public class SJF extends Scheduler {

    public SJF() {

    }

    List<Process> ganttChartSJF = new ArrayList<>();

    @Override
    public List<Process> run(List<Process> processes) {
        List<Process> processList = new ArrayList<>(processes);
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));


        int currentTime = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator
                .comparingDouble(Process::getEffectiveBurstTime)
                .thenComparingInt(Process::getArrivalTime).thenComparingInt(Process::getPriority));


        while (!processList.isEmpty() || !readyQueue.isEmpty()) {

            while (!processList.isEmpty() && processList.getFirst().getArrivalTime() <= currentTime) {
                readyQueue.add(processList.removeFirst());
            }

            if (!readyQueue.isEmpty()) {

                Process currentProcess = readyQueue.poll();

                currentProcess.setCompletionTime(currentTime + currentProcess.getBurstTime());
                currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getBurstTime());
                currentTime = currentProcess.getCompletionTime();

                for (Process p : readyQueue) {
                    p.setWaitingTime(currentTime - p.getArrivalTime());
                    p.setEffectiveBurstTime();
                }
                ganttChartSJF.add(currentProcess);

            } else {

                currentTime = processList.getFirst().getArrivalTime();
            }
        }
        return ganttChartSJF;
    }

    public void getExecutionOrder() {
        System.out.print("Execution Order: ");
        for (int i = 0; i < ganttChartSJF.size(); i++) {
            System.out.print(ganttChartSJF.get(i).getName());
            if (i < ganttChartSJF.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
    }

    public void printMetrics() {
        int totalWaitingTime = 0;
        int totalTurnAroundTime = 0;

        System.out.println("\nProcess Metrics:");
        System.out.printf("%-10s%-15s%-15s%-15s%-15s%n", "Process", "Waiting Time", "Turnaround Time", "Arrival Time", "Burst Time");
        for (Process p : ganttChartSJF) {
            System.out.printf("%-10s%-15s%-15s%-15s%-15s%n",
                    p.getName(), p.getWaitingTime(), p.getTurnAroundTime(),
                    p.getArrivalTime(), p.getBurstTime());
            totalWaitingTime += p.getWaitingTime();
            totalTurnAroundTime += p.getTurnAroundTime();
        }

        System.out.println("\nAverage Waiting Time: " + (double) totalWaitingTime / ganttChartSJF.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnAroundTime / ganttChartSJF.size());
    }
}
