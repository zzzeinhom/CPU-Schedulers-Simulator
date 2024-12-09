package org.example;
import javafx.scene.paint.Color;
import java.util.*;

public class InputHandler {

    public static List<Process> getProcessesFromUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int numProcesses = scanner.nextInt();

        System.out.print("Enter Round Robin time quantum: ");
        int quantum = scanner.nextInt();

        System.out.print("Enter context switching time: ");
        int contextSwitch = scanner.nextInt();

        List<Process> processes = new ArrayList<>();

        // Per-Process Parameters
        for (int i = 0; i < numProcesses; i++) {
            System.out.println("Enter details for Process " + (i + 1) + " in the format:");
            System.out.println("Name Color ArrivalTime BurstTime Priority (e.g., noor #FFFFFF 1 5 1):");

            // Consume the leftover newline character from previous input
            if (i == 0) scanner.nextLine();

            // Read the entire line of input
            String inputLine = scanner.nextLine(); // Use nextLine() instead of next()
            String[] parts = inputLine.split(" ");

            // Validate input length
            if (parts.length != 5) {
                System.out.println("Invalid input format. Please enter exactly 5 values.");
                i--; // Retry this process
                continue;
            }

            try {
                // Parse input
                String name = parts[0];
                Color color = Color.web(parts[1]); // Parse the color from HEX
                int arrivalTime = Integer.parseInt(parts[2]);
                int burstTime = Integer.parseInt(parts[3]);
                int priority = Integer.parseInt(parts[4]);

                // Add the process to the list
                processes.add(new Process(name, arrivalTime, burstTime, priority, color));
            } catch (IllegalArgumentException e) {
                // Handles invalid numbers or colors
                System.out.println("Invalid input. Please ensure numbers are integers and color is in HEX format.");
                i--; // Retry this process
            }
        }



        System.out.println("All inputs received!");
        return processes;
    }
}
