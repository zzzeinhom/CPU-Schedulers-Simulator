package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityScheduler extends Scheduler {
    private int contextSwitch;
    public PriorityScheduler(int contextSwitch) {
        this.contextSwitch = contextSwitch;
    }

    @Override
    public List<Process> run(List<Process> processes) {

        int currentTime = 0;
        int order = 1;
        int idx = 0;
        int totWaiting = 0, totTurnAround = 0;
        int numberOfProcesses = processes.size();
        // Processes are sorted on arrival time.
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        // Initialize readyQueue
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));

        readyQueue.add(processes.get(idx));
        while (!readyQueue.isEmpty()) {
            ++idx;
            Process p = readyQueue.poll();
            p.setCompletionTime(currentTime + p.getBurstTime());
            p.setTurnAroundTime(p.getCompletionTime() - p.getArrivalTime());
            p.setWaitingTime(p.getTurnAroundTime() - p.getBurstTime());
            totTurnAround += p.getTurnAroundTime();
            totWaiting += p.getWaitingTime();
            System.out.println(p.getName());
            currentTime += p.getBurstTime() + contextSwitch;
            System.out.printf("Process execution order: %d\nWaiting Time: %d\nTurnaround Time: %d\n\n",
                    order++, p.getWaitingTime(), p.getTurnAroundTime());
            int tmpIdx = idx;
            while (tmpIdx < processes.size()) {
                if (processes.get(tmpIdx).getArrivalTime() <= currentTime) {
                    readyQueue.add(processes.get(tmpIdx));
                    ++tmpIdx;
                }
                else {
                    --tmpIdx;
                    break;
                }
            }
            if (readyQueue.isEmpty() && idx < processes.size()){
                readyQueue.add(processes.get(idx));
                currentTime = processes.get(idx).getArrivalTime();
            }
            else {
                idx = tmpIdx;
            }
        }
        double avgWaitingTime = 1.0*totWaiting / numberOfProcesses;
        double avgTurnaroundTime = 1.0*totTurnAround / numberOfProcesses;
        System.out.printf("Average Waiting Time: %.5f\nAverage Turnaround Time: %.5f\n",
                avgWaitingTime,avgTurnaroundTime);
        return processes;
    }
}
