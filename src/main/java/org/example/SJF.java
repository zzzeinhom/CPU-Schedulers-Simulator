//package org.example;
//
//import java.util.*;
//
//public class SJF extends Scheduler {
//
//    public SJF() {
//
//    }
//
//    @Override
//    public List<Process> run(List<Process> processes) {
//        List<Process> processList = new ArrayList<>(processes);
//        processList.sort(Comparator.comparingInt(Process::getArrivalTime));
//
//
//        int currentTime = 0;
//        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator
//                .comparingInt(Process::getBurstTime)
//                .thenComparingInt(Process::getArrivalTime).thenComparingInt(Process::getPriority));
//
//
//        while (!processList.isEmpty() || !readyQueue.isEmpty()) {
//
//            while (!processList.isEmpty() && processList.getFirst().getArrivalTime() <= currentTime) {
//                readyQueue.add(processList.removeFirst());
//            }
//
//            if (!readyQueue.isEmpty()) {
//
//                Process currentProcess = readyQueue.poll();
//
//                currentProcess.setCompletionTime(currentTime + currentProcess.getBurstTime());
//                currentProcess.setTurnAroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
//                currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getBurstTime());
//
//                ganttChart.add(currentProcess);
//
//                currentTime = currentProcess.getCompletionTime();
//            } else {
//
//                currentTime = processList.getFirst().getArrivalTime();
//            }
//        }
//        return ganttChart;
//    }
//
//    public void getExecutionOrder() {
//        System.out.print("Execution Order: ");
//        for (int i = 0; i < ganttChart.size(); i++) {
//            System.out.print(ganttChart.get(i).getName());
//            if (i < ganttChart.size() - 1) System.out.print(" -> ");
//        }
//        System.out.println();
//    }
//
//    public void printMetrics() {
//        int totalWaitingTime = 0;
//        int totalTurnAroundTime = 0;
//
//        System.out.println("\nProcess Metrics:");
//        System.out.printf("%-10s%-15s%-15s%-15s%-15s%n", "Process", "Waiting Time", "Turnaround Time", "Arrival Time", "Burst Time");
//        for (Process p : ganttChart) {
//            System.out.printf("%-10s%-15s%-15s%-15s%-15s%n",
//                    p.getName(), p.getWaitingTime(), p.getTurnAroundTime(),
//                    p.getArrivalTime(), p.getBurstTime());
//            totalWaitingTime += p.getWaitingTime();
//            totalTurnAroundTime += p.getTurnAroundTime();
//        }
//
//        System.out.println("\nAverage Waiting Time: " + (double) totalWaitingTime / ganttChart.size());
//        System.out.println("Average Turnaround Time: " + (double) totalTurnAroundTime / ganttChart.size());
//    }
//}
