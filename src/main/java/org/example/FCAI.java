package org.example;

import java.util.*;

public class FCAI extends Scheduler{

    private int AllocateQuantum(QuantumProcess process, boolean completedQuantum, int unusedQuantum) {
        if (completedQuantum) {
            return process.getQuantum() + 2;
        } else {
            return process.getQuantum() + unusedQuantum;
        }
    }

    private double CalculateFCAIFactor(Process process, double V1, double V2) {
        return (10 - process.getPriority()) +
                (process.getArrivalTime() / V1) +
                (process.getRemainingBurstTime() / V2);
    }

    private double CalculateV1(List<Process> processes){
        if (processes.isEmpty()) return 0;
        int maxArrivalTime = Integer.MIN_VALUE;
        for (Process process : processes) {
            maxArrivalTime = Math.max(maxArrivalTime, process.getArrivalTime());
        }
        return (double) maxArrivalTime / 10 ;
    }

    private double CalculateV2(List<Process> processes){
        if (processes.isEmpty()) return 0;
        int maxBurstTime = Integer.MIN_VALUE;
        for (Process process : processes) {
            maxBurstTime = Math.max(maxBurstTime, process.getBurstTime());
        }
        return (double) maxBurstTime / 10 ;
    }

    public List<Process> run(List<Process> process){
        // Initialize variables
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        double V1 = CalculateV1(process);
        double V2 = CalculateV2(process);
        process.sort(Comparator.comparingInt(Process::getArrivalTime));
        int index = 0;


        while (index < process.size() || !queue.isEmpty()) {
            while (index < process.size() && process.get(index).getArrivalTime() <= currentTime) {
                queue.add(process.get(index));
                index++;
            }


        };


    return process;
    }
}
