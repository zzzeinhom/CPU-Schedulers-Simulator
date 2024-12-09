package org.example;

import javafx.scene.paint.Color;
import java.io.*;
import java.util.*;

public class InputHandler {

    public static List<Process> getProcessesFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        // Read general parameters
        int numProcesses = Integer.parseInt(reader.readLine().trim());
        int quantum = Integer.parseInt(reader.readLine().trim());
        int contextSwitch = Integer.parseInt(reader.readLine().trim());

        System.out.println("Number of processes: " + numProcesses);
        System.out.println("Round Robin Time Quantum: " + quantum);
        System.out.println("Context Switching Time: " + contextSwitch);

        List<Process> processes = new ArrayList<>();

        // Read process details
        for (int i = 0; i < numProcesses; i++) {
            String[] parts = reader.readLine().trim().split(" ");
            String name = parts[0];
            Color color = Color.web(parts[1]);
            int arrivalTime = Integer.parseInt(parts[2]);
            int burstTime = Integer.parseInt(parts[3]);
            int priority = Integer.parseInt(parts[4]);

            processes.add(new Process(name, arrivalTime, burstTime, priority/*, color*/));
        }

        reader.close();
        System.out.println("Processes loaded from file!");
        return processes;
    }
}
