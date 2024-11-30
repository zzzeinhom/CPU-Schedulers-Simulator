package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 6, 0));
        processes.add(new Process("P2", 1, 8, 0));
        processes.add(new Process("P3", 2, 7, 0));
        processes.add(new Process("P4", 3, 3, 0));

        SJF sjf = new SJF();
        sjf.run(processes);
        sjf.getExecutionOrder();
        sjf.printMetrics();
    }
}