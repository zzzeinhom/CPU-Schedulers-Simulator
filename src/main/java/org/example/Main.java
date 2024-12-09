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
//        FCAI scheduler=new FCAI();
//        FCAI.run(processes);
//        SJF sjf = new SJF();
//        sjf.run(processes);
//        sjf.getExecutionOrder();
//        sjf.printMetrics();
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 17, 4));
        processes.add(new Process("P2", 3, 6, 9));
        processes.add(new Process("P3", 4, 10, 3));
        processes.add(new Process("P4", 29, 4, 10));
        SRTF SRTF = new SRTF(1);
        SRTF.run(processes);
    }
}