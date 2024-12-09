package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<QuantumProcess> qProcesses = new ArrayList<>();
        qProcesses.add(new QuantumProcess("P1", 0, 17, 4,4,0));
        qProcesses.add(new QuantumProcess("P2", 3, 6, 9,3,0));
        qProcesses.add(new QuantumProcess("P3", 4, 10, 3,5,0));
        qProcesses.add(new QuantumProcess("P4", 29, 4, 10,2,0));
        FCAI scheduler=new FCAI();
        FCAI.run(qProcesses);
//        List<Process> processes = new ArrayList<>();
//        processes.add(new Process("P1", 1, 20, 0)); // Long burst time, likely to starve
//        processes.add(new Process("P2", 1, 5, 0));  // Short burst time
//        processes.add(new Process("P3", 2, 4, 0));  // Short burst time
//        processes.add(new Process("P4", 3, 3, 0));  // Short burst time
//        processes.add(new Process("P5", 4, 3, 0));  // Short burst time
//        processes.add(new Process("P6", 5, 2, 0));  // Short burst time
//        processes.add(new Process("P7", 6, 4, 0));  // Short burst time
//        processes.add(new Process("P8", 7, 3, 0));  // Short burst time
//        processes.add(new Process("P9", 8, 2, 0));  // Short burst time
//        processes.add(new Process("P10", 9, 6, 0)); // Short burst time
//        processes.add(new Process("P11", 10, 2, 0)); // Short burst time
//        processes.add(new Process("P12", 11, 3, 0)); // Short burst time
//        processes.add(new Process("P13", 12, 5, 0)); // Short burst time
//        processes.add(new Process("P14", 13, 4, 0)); // Short burst time
//        processes.add(new Process("P15", 14, 2, 0)); // Short burst time

//        SJF sjf = new SJF();
//        sjf.run(processes);
//        sjf.getExecutionOrder();
//        sjf.printMetrics();
//        List<Process> processes = new ArrayList<>();
//        processes.add(new Process("P1", 0, 17, 4));
//        processes.add(new Process("P2", 3, 6, 9));
//        processes.add(new Process("P3", 4, 10, 3));
//        processes.add(new Process("P4", 29, 4, 10));
//        SRTF SRTF = new SRTF(1);
//        SRTF.run(processes);
    }
}