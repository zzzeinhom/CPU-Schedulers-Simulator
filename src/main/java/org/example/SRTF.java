package org.example;

import javafx.scene.paint.Color;

import java.util.*;

import org.example.CPUSchedulerGUI.ExecutionSegment; // Import ExecutionSegment from your GUI class
import javafx.scene.paint.Color; // Import Color for process visualization

public class SRTF extends Scheduler {
    private int contextSwitch;
    private static final int MX_WAITING = 10;
    private static final int INF = 1000000000;

    public SRTF(int contextSwitch) {
        this.contextSwitch = contextSwitch;
    }

    List<Process> ganttChartSRTF = new ArrayList<>();


    @Override
    public List<Process> run(List<Process> processes) {
        int order = 1;
        int currentTime = 0;
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
        while (!readyQueue.isEmpty()) {
            while (idx < numberOfProcesses && processes.get(idx).getArrivalTime() <= currentTime) {
                processes.get(idx).setQueueEntryTime(currentTime);
                readyQueue.add(processes.get(idx++));
            }
            // Check whether there is a starvation
            Process starvingProcess = null;
            boolean starving = false;
            Iterator<Process> iterator = readyQueue.iterator();
            while (iterator.hasNext()) {
                Process process = iterator.next();
                int waitingInQueue = currentTime - process.getQueueEntryTime();
                if (waitingInQueue > MX_WAITING) {
                    starvingProcess = process;
                    iterator.remove();
                    process.setQueueEntryTime(currentTime); // Reset aging
                    starving = true;
                    break;
                }
            }
            Process p;
            if (starving) p = starvingProcess;
            else p = readyQueue.poll();


            int remainingTime = p.getRemainingBurstTime();
            if (remainingTime < prevRemainingTime) { // There is a context switch
                currentTime += contextSwitch;
            }
            currentTime++;
            p.setRemainingBurstTime(remainingTime - 1);
            // process output
            if (p.getRemainingBurstTime() == 0) {
                p.setCompletionTime(currentTime);
                p.setTurnAroundTime(p.getCompletionTime() - p.getArrivalTime());
                p.setWaitingTime(p.getTurnAroundTime() - p.getBurstTime());
                totTurnAround += p.getTurnAroundTime();
                totWaiting += p.getWaitingTime();
                ganttChartSRTF.add(p);
                System.out.println(p.getName());
                System.out.printf("Process execution order: %d\nWaiting Time: %d\nTurnaround Time: %d\n\n",
                        order++, p.getWaitingTime(), p.getTurnAroundTime());
                prevRemainingTime = INF;
            }
            // back on queue
            else {
                prevRemainingTime = p.getRemainingBurstTime();
                p.setQueueEntryTime(currentTime);
                readyQueue.add(p);
            }

        }
        double avgWaitingTime = 1.0 * totWaiting / numberOfProcesses;
        double avgTurnaroundTime = 1.0 * totTurnAround / numberOfProcesses;
        System.out.printf("Average Waiting Time: %.2f\nAverage Turnaround Time: %.2f\n",
                avgWaitingTime, avgTurnaroundTime);
        return processes;
    }

    public List<CPUSchedulerGUI.ExecutionSegment> getGanttChartSRTF() {
        List<CPUSchedulerGUI.ExecutionSegment> timeline = new ArrayList<>();
        for (Process p : ganttChartSRTF) {
            timeline.add(new CPUSchedulerGUI.ExecutionSegment(
                    p.getName(),
                    p.getBurstTime(),
                    getColorForProcess(p.getName())
            ));
        }
        return timeline;
    }

    private Color getColorForProcess(String processName) {
        switch (processName) {
            case "P1":
                return Color.RED;
            case "P2":
                return Color.BLUE;
            case "P3":
                return Color.GREEN;
            case "P4":
                return Color.YELLOW;
            default:
                return Color.GRAY; // Default color for unrecognized processes
        }
    }
}
