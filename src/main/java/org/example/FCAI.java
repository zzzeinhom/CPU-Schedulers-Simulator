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

        List<QuantumProcess> queue = new LinkedList<>();
        processes.sort(Comparator.comparingInt(QuantumProcess::getArrivalTime));

        int currentTime = 0;
        boolean WillEnterNonPreemptive = true;
        int logTime = 0;
        while (!processes.isEmpty() || !queue.isEmpty()) {
            // Add processes arriving at the current time
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

            // If queue is empty, advance time
            if (queue.isEmpty()) {
                currentTime++;
                continue;
            }


            // Fetch process from the queue
            QuantumProcess currentProcess = queue.removeFirst();
            int quantum = currentProcess.getQuantum();
            int nonPreemptiveTime = (int) Math.ceil(0.4 * quantum);
            int executionTime = 0;
//            System.out.println("Process that entered CPU: " + currentProcess);
            if (WillEnterNonPreemptive) // if the process was preempted and it's a new process
            {
                // Execute non-preemptively for 40% of quantum
                executionTime = Math.min(nonPreemptiveTime, currentProcess.getRemainingBurstTime());
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - executionTime);
                currentTime += executionTime;
//                System.out.println("Process Executed for: " + executionTime + "And it's quantum was: " + quantum);
                currentProcess.unusedQuantum = quantum - executionTime;
            } else { // if the process will continue execution from where is stopped
                currentTime++;
                executionTime = 1;
                currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - executionTime);
                currentProcess.unusedQuantum--;
            }

            // Check if the process is completed
            if (currentProcess.getRemainingBurstTime() == 0) { // if process is completed
//                System.out.println("Process Completed: " + currentProcess.getName());
                currentProcess.setCompletionTime(currentTime);
                WillEnterNonPreemptive = true;
                debug(logTime,currentTime,currentProcess);

                logTime = currentTime;

            } else { // Process not yet completed
                // Check if a process entered the queue meanwhile
                Iterator<QuantumProcess> iterator2 = processes.iterator();
                while (iterator2.hasNext()) {
                    QuantumProcess process = iterator2.next();
                    process.setFactor(calculateFCAIFactor(process, V1, V2));
                    if (process.getArrivalTime() <= currentTime) {
                        queue.add(process);
                        iterator2.remove();
                    } else {
                        break;
                    }
                }
                // Sort the queue based on FCAI factor (priority for next execution)
                List<QuantumProcess> sortedQueue = new ArrayList<>(queue);

                if (currentProcess.getRemainingBurstTime() > 0 && currentProcess.unusedQuantum == 0) // process not completed but quantum finished
                {
                    sortedQueue.sort(Comparator.comparingDouble(QuantumProcess::getFactor));
                    queue.clear();
                    queue.addAll(sortedQueue);

                    // Update its factor and quantum
                    currentProcess.setFactor(calculateFCAIFactor(currentProcess, V1, V2));
                    currentProcess.setQuantum(currentProcess.getQuantum() + 2);
                    WillEnterNonPreemptive = true;

                    // re-add to queue
                    queue.add(currentProcess);
                    debug(logTime,currentTime,currentProcess);
                    logTime = currentTime;
                } else if (currentProcess.getRemainingBurstTime() > 0 && currentProcess.unusedQuantum > 0) // process not completed and quantum not completed
                {
                    // Default behavior: Assume the process will continue execution
                    WillEnterNonPreemptive = false;

                    // Check if any process in the queue has a lower factor than the current process
                    boolean hasHigherPriorityProcess = false;
                    for (QuantumProcess process : sortedQueue) {
                        if (process.getFactor() < currentProcess.getFactor()) {
                            hasHigherPriorityProcess = true;
                            break;
                        }
                    }

                    if (!hasHigherPriorityProcess) {
                        queue.addFirst(currentProcess); // Continue execution
                    } else { // preempt occurs
                        // Find the next highest priority process (preemption logic)
                        sortedQueue.sort(Comparator.comparingDouble(QuantumProcess::getFactor));
                        queue.clear();
                        queue.addAll(sortedQueue);

                        // Update the current process's factor and quantum
                        currentProcess.setFactor(calculateFCAIFactor(currentProcess, V1, V2));
                        currentProcess.setQuantum(currentProcess.getQuantum() + currentProcess.unusedQuantum);
                        queue.add(currentProcess);

                        // Mark that it will enter non-preemptive mode
                        WillEnterNonPreemptive = true;
                        debug(logTime,currentTime,currentProcess);
                        logTime = currentTime;
                    }
                }
            } // Process that not completed
//            System.out.println("Processes finished this iteration: " + queue);
        } // go to next in queue

    }

    private static void debug(int logTime, int currentTime, QuantumProcess currentProcess) {
        if (currentProcess.getRemainingBurstTime() != 0) {
            System.out.println("Time: " + logTime + "-" + currentTime +
                    " | Process: " + currentProcess.getName() +
                    " | Remaining Burst Time: " + currentProcess.getRemainingBurstTime() +
                    " | Updated Quantum: " + currentProcess.getQuantum() +
                    " | Priority: " + currentProcess.getPriority() +
                    " | Factor: " + currentProcess.getFactor());
        }else{
            System.out.println("Time: " + logTime + "-" + currentTime +
                    " | Process: " + currentProcess.getName() +
                    " | Remaining Burst Time: " + "COMPLETE!" +
                    " | Updated Quantum: " + "COMPLETE!"  +
                    " | Priority: " + currentProcess.getPriority() +
                    " | Factor: " + "COMPLETE!" );
        }
    }
}

