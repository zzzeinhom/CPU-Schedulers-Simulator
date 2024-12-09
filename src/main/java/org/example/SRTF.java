package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SRTF extends Scheduler{
    private int contextSwitch;
    private static final int MX_WAITING = 50;
    public SRTF(int contextSwitch){
        this.contextSwitch = contextSwitch;
    }

    @Override
    public List<Process> run(List<Process> processes){
        int currentTime = 0;
        int order = 1;
        int idx = 0;
        int totWaiting = 0, totTurnAround = 0;
        int numberOfProcesses = processes.size();
        int prevRemainingTime = 0;
        // Processes are sorted on arrival time.
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        // Initialize readyQueue
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingBurstTime)
                        .thenComparingInt(Process::getArrivalTime));
        readyQueue.add(processes.get(idx++));
        while (!readyQueue.isEmpty()){
            while(idx < numberOfProcesses && processes.get(idx).getArrivalTime() <= currentTime){
                readyQueue.add(processes.get(idx++));
            }
            Process p = readyQueue.poll();
            int remainingTime = p.getRemainingBurstTime();
            if(remainingTime < prevRemainingTime) { // There is a context switch
                currentTime += contextSwitch;
            }
            currentTime++;
            p.setRemainingBurstTime(remainingTime - 1);
            if(p.getRemainingBurstTime() == 0){
                p.setCompletionTime(currentTime);
                p.setTurnAroundTime(p.getCompletionTime() - p.getArrivalTime());
                p.setWaitingTime(p.getTurnAroundTime() - p.getBurstTime());
                totTurnAround += p.getTurnAroundTime();
                totWaiting += p.getWaitingTime();
                System.out.println(p.getName());
                System.out.printf("Process execution order: %d\nWaiting Time: %d\nTurnaround Time: %d\n\n",
                        order++, p.getWaitingTime(), p.getTurnAroundTime());
                currentTime += contextSwitch;
                prevRemainingTime = 0;
            }
            else{
                prevRemainingTime = p.getRemainingBurstTime();
                readyQueue.add(p);
            }
        }
        double avgWaitingTime = 1.0*totWaiting / numberOfProcesses;
        double avgTurnaroundTime = 1.0*totTurnAround / numberOfProcesses;
        System.out.printf("Average Waiting Time: %.2f\nAverage Turnaround Time: %.2f\n",
                avgWaitingTime,avgTurnaroundTime);
        return processes;
    }
}
