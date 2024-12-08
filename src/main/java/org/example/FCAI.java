package org.example;

import java.util.*;

public class FCAI {

    // Helper method to calculate V1
    private static double calculateV1(List<QuantumProcess> processes) {
        return processes.stream().mapToInt(QuantumProcess::getArrivalTime).max().orElse(0) / 10.0;
    }

    // Helper method to calculate V2
    private static double calculateV2(List<QuantumProcess> processes) {
        return processes.stream().mapToInt(QuantumProcess::getBurstTime).max().orElse(0) / 10.0;
    }

    // Helper method to calculate FCAI factor
    private static double calculateFCAIFactor(QuantumProcess process, double V1, double V2) {
        return (10 - process.getPriority()) +
                (process.getArrivalTime() / V1) +
                (process.getRemainingBurstTime() / V2);
    }

    public static void run(List<QuantumProcess> processes) {
        double V1 = calculateV1(processes);
        double V2 = calculateV2(processes);
        List<QuantumProcess> completedProcesses = new ArrayList<>();
        // Queue will manage the ordering for processes
        List<QuantumProcess> queue = new LinkedList<>();
        processes.sort(Comparator.comparingInt(QuantumProcess::getArrivalTime));

        // setup variables
        int currentTime = 0;
        boolean WillEnterNonPreemptive = true;
        int logTime = 0;

        // find first processes to enter the queue
        while(queue.isEmpty()){
            handleNewArrivals(processes, queue, currentTime, V1, V2);
            if(queue.isEmpty())
            {
                currentTime++;
            }
        }

        while (!processes.isEmpty() || !queue.isEmpty()) {

//            // Add processes arriving at the current time
//            handleNewArrivals(processes, queue, currentTime, V1, V2); // also removes process from the input list
//
//            // If queue is empty, advance time
//            if (queue.isEmpty()) {
//                currentTime++;
//                continue;
//            }

            // Fetch process from the queue in order
            QuantumProcess currentProcess = queue.removeFirst();

            int quantum = currentProcess.getQuantum();
            int nonPreemptiveTime = (int) Math.ceil(0.4 * quantum);
            int executionTime;

            // if a process was preempted, or it's a new process
            if (WillEnterNonPreemptive) {
                // Execute non-preemptively for 40% of quantum
                executionTime = Math.min(nonPreemptiveTime, currentProcess.getRemainingBurstTime());
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - executionTime);
                currentTime += executionTime;
                currentProcess.unusedQuantum = quantum - executionTime;

            } else {
                // if the process will continue execution from where is stopped
                currentTime++;
                executionTime = 1;
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - executionTime);
                currentProcess.unusedQuantum--;
            }

            // =============== Preemptive phase ===================

            // Case 1: Check if the process is completed
            if (currentProcess.getRemainingBurstTime() == 0) { // if process is completed
                // Calculate time metrics for the process
                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnAroundTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnAroundTime() - currentProcess.getBurstTime());
                completedProcesses.add(currentProcess);
                // This flag is set to let the next in queue to execute non-preemptively
                WillEnterNonPreemptive = true;
                debug(logTime, currentTime, currentProcess);
                logTime = currentTime;

            }

            // check if new processes arrived meanwhile
            handleNewArrivals(processes, queue, currentTime, V1, V2);

            // Case 2: process not completed but quantum finished
            if (currentProcess.getRemainingBurstTime() > 0 && currentProcess.unusedQuantum == 0) {
//                queue.sort(Comparator.comparingDouble(QuantumProcess::getFactor));

                // Update its factor and quantum
                currentProcess.setFactor(calculateFCAIFactor(currentProcess, V1, V2));
                currentProcess.setQuantum(currentProcess.getQuantum() + 2);
                WillEnterNonPreemptive = true;

                // re-add to queue
                queue.add(currentProcess);
                debug(logTime, currentTime, currentProcess);
                logTime = currentTime;
            }
            // Case 3: process not completed and quantum not completed " Can be preempted "
            else if (currentProcess.getRemainingBurstTime() > 0 && currentProcess.unusedQuantum > 0) {
                // Default behavior - Assume the process will continue execution
                WillEnterNonPreemptive = false;

                // Check if any process in the queue has a lower factor than the current process
                QuantumProcess lowerFactorProcess = null;

                // Iterate through the queue to find the first process with a lower factor
                for (int i = 0; i < queue.size(); i++) {
                    QuantumProcess process = queue.get(i);
                    if (process.getFactor() < currentProcess.getFactor()) {
                        lowerFactorProcess = process;
                        queue.remove(i);
                        break;
                    }
                }

                if (lowerFactorProcess == null) {
                    // Case 3.1: No preemption occurs, will continue execution
                    queue.addFirst(currentProcess); // Continue execution
                } else {
                    // Case 3.2: preempt occurs, find a lower process

                    // Add the found process with a lower factor to the front of the queue
                    queue.addFirst(lowerFactorProcess);

                    // Update the current process's factor and quantum
                    currentProcess.setFactor(calculateFCAIFactor(currentProcess, V1, V2));
                    currentProcess.setQuantum(currentProcess.getQuantum() + currentProcess.unusedQuantum);
                    queue.add(currentProcess);

                    // Mark that it will enter non-preemptive mode
                    WillEnterNonPreemptive = true;
                    debug(logTime, currentTime, currentProcess);
                    logTime = currentTime;
                }
            }
        }  // go to next in queue
        PrintSummary(completedProcesses);
    }


    // Handle processes arriving at the current time
    private static void handleNewArrivals(List<QuantumProcess> processes, List<QuantumProcess> queue,
                                          int currentTime, double V1, double V2) {
        Iterator<QuantumProcess> iterator = processes.iterator();
        while (iterator.hasNext()) {
            QuantumProcess process = iterator.next();
            if (process.getArrivalTime() <= currentTime) {
                process.setFactor(calculateFCAIFactor(process, V1, V2));
                queue.add(process);
                iterator.remove();
            } else {
                break;
            }
        }
    }

    private static void debug(int logTime, int currentTime, QuantumProcess currentProcess) {
        if (currentProcess.getRemainingBurstTime() != 0) {
            System.out.println("Time: " + logTime + "-" + currentTime +
                    " | Process: " + currentProcess.getName() +
                    " | Remaining Burst Time: " + currentProcess.getRemainingBurstTime() +
                    " | Updated Quantum: " + currentProcess.getQuantum() +
                    " | Priority: " + currentProcess.getPriority() +
                    " | Factor: " + currentProcess.getFactor());
        } else {
            System.out.println("Time: " + logTime + "-" + currentTime +
                    " | Process: " + currentProcess.getName() +
                    " | Remaining Burst Time: " + "COMPLETE!" +
                    " | Updated Quantum: " + "COMPLETE!" +
                    " | Priority: " + currentProcess.getPriority() +
                    " | Factor: " + "COMPLETE!");
        }
    }

    private static void PrintSummary(List<QuantumProcess> processes) {
        double SumTAT = 0;
        double SumWT = 0;
        for (QuantumProcess process : processes) {
            SumTAT += process.getTurnAroundTime();
            SumWT += process.getWaitingTime();
            System.out.println(process.getName() + ": ");
            System.out.println("    Turnaround Time: " + process.getTurnAroundTime());
            System.out.println("    Waiting Time: " + process.getWaitingTime());
        }
        System.out.println();
        System.out.println("Average Turnaround Time: " + SumTAT / processes.size());
        System.out.println("Average Waiting Time: " + SumWT / processes.size());
    }
}

