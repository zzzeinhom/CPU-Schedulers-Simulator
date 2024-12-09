package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.io.IOException;


import java.util.ArrayList;
import java.util.List;

public class CPUSchedulerGUI extends Application {

    private static List<ExecutionSegment> executionTimeline;

    public static void setExecutionTimeline(List<ExecutionSegment> timeline) {
        executionTimeline = timeline;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CPU Scheduling Visualization");

        // Canvas for Gantt Chart
        int canvasWidth = 800;
        int canvasHeight = 400;
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawGanttChart(gc, canvasWidth, canvasHeight);

        // Process Information Table
        List<Process> processes = getMockProcesses(); // Replace with actual processes
        TableView<Process> processTable = createProcessTable(processes);

        // Combine Gantt chart and table
        HBox root = new HBox(10, canvas, processTable);
        Scene scene = new Scene(root, 1000, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawGanttChart(GraphicsContext gc, int canvasWidth, int canvasHeight) {
        if (executionTimeline == null || executionTimeline.isEmpty()) {
            gc.setFill(Color.BLACK);
            gc.fillText("No data to display", canvasWidth / 2.0 - 50, canvasHeight / 2.0);
            return;
        }

        // Define dimensions for each process block
        double totalDuration = executionTimeline.stream().mapToDouble(ExecutionSegment::getDuration).sum();
        double unitWidth = canvasWidth / totalDuration;
        double blockHeight = canvasHeight / 5; // Adjust for aesthetics
        double startX = 0;

        // Draw time quanta (markers)
        gc.setFill(Color.BLACK);
        for (int time = 0; time <= totalDuration; time++) {
            double x = time * unitWidth;
            gc.fillText(String.valueOf(time), x, canvasHeight / 2 - blockHeight / 2 - 10); // Time marker above the chart
            gc.strokeLine(x, canvasHeight / 2 - blockHeight / 2 - 5, x, canvasHeight / 2 + blockHeight / 2 + 5); // Vertical tick
        }

        // Draw each segment
        for (ExecutionSegment segment : executionTimeline) {
            double blockWidth = segment.getDuration() * unitWidth;

            // Calculate time quanta for the process
            int startQuantum = (int) (startX / unitWidth);
            int endQuantum = (int) ((startX + blockWidth) / unitWidth);

            // Debugging log with time quanta
            System.out.printf("Process: %s | Start Time: %d | End Time: %d | Duration: %.2f | Color: %s%n",
                    segment.getProcessName(), startQuantum, endQuantum, segment.getDuration(),
                    String.format("#%02X%02X%02X",
                            (int) (segment.getColor().getRed() * 255),
                            (int) (segment.getColor().getGreen() * 255),
                            (int) (segment.getColor().getBlue() * 255)));

            // Draw the block for the process
            gc.setFill(segment.getColor());
            gc.fillRect(startX, canvasHeight / 2 - blockHeight / 2, blockWidth, blockHeight);

            // Add process name as a label
            gc.setFill(Color.BLACK);
            gc.fillText(segment.getProcessName(), startX + blockWidth / 4, canvasHeight / 2);

            // Move the starting position for the next process
            startX += blockWidth;
        }


        // Draw a border around the Gantt chart
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0, canvasHeight / 2 - blockHeight / 2, canvasWidth, blockHeight);
    }

    private TableView<Process> createProcessTable(List<Process> processes) {
        TableView<Process> table = new TableView<>();

        // Handle null or empty process list
        if (processes == null || processes.isEmpty()) {
            System.out.println("No processes to display in the table.");
            return table; // Return an empty table
        }

        // Define columns
        TableColumn<Process, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Process, Integer> arrivalTimeCol = new TableColumn<>("Arrival Time");
        arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));

        TableColumn<Process, Integer> burstTimeCol = new TableColumn<>("Burst Time");
        burstTimeCol.setCellValueFactory(new PropertyValueFactory<>("burstTime"));

        TableColumn<Process, Integer> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));

        nameCol.setPrefWidth(100);
        arrivalTimeCol.setPrefWidth(120);
        burstTimeCol.setPrefWidth(100);
        priorityCol.setPrefWidth(100);

        table.getColumns().addAll(nameCol, arrivalTimeCol, burstTimeCol, priorityCol);

        // Populate Table with Process Data
        table.getItems().addAll(processes);

        table.setStyle("-fx-font-size: 14; -fx-padding: 10;");


        return table;
    }






    // Mock ExecutionSegment class (replace with actual data structure from your project)
    public static class ExecutionSegment {
        private final String processName;
        private final double duration;
        private final Color color;

        public ExecutionSegment(String processName, double duration, Color color) {
            this.processName = processName;
            this.duration = duration;
            this.color = color;
        }

        public String getProcessName() {
            return processName;
        }

        public double getDuration() {
            return duration;
        }

        public Color getColor() {
            return color;
        }
    }
    private List<Process> getMockProcesses() {
        //this is what shows at the sidebar thing
        return List.of(
                new Process("P1", 1, 10, 1),
                new Process("P2", 1, 5, 1),
                new Process("P3", 1, 5, 1),
                new Process("P4", 1, 5, 1)
        );
    }

    public static void main(String[] args) throws IOException {
        // Load processes from input file
        List<QuantumProcess> qProcesses = new ArrayList<>();
        qProcesses.add(new QuantumProcess("P1", 0, 17, 4,4,0));
        qProcesses.add(new QuantumProcess("P2", 3, 6, 9,3,0));
        qProcesses.add(new QuantumProcess("P3", 4, 10, 3,5,0));
        qProcesses.add(new QuantumProcess("P4", 29, 4, 10,2,0));
//        String fileName = "input.txt"; // Ensure this file exists in your project directory
//        List<Process> processes = InputHandler.getProcessesFromFile(fileName);

                // Define processes (replace with dynamic input later)
//        List<Process> processes = List.of(
//                new Process("P1", 1, 10, 1),
//                new Process("P2", 1, 5, 1),
//                new Process("P3", 1, 5, 1),
//                new Process("P4", 1, 5, 1)
//        );
//
//         Example: Run SJF Scheduling
//        SJF sjf = new SJF();
//        sjf.run(processes);
//
//        // Get the Gantt Chart and pass to the GUI
//        List<ExecutionSegment> timeline = sjf.getGanttChart();
//        CPUSchedulerGUI.setExecutionTimeline(timeline);

          FCAI fcai = new FCAI();
          fcai.run(qProcesses);
        // Example: Run SJF Scheduling
//        SRTF srtf = new SRTF(0);
//        SRTF.run(processes);
//
//        // Get the Gantt Chart and pass to the GUI
//        List<ExecutionSegment> timeline = sjf.getGanttChart();
//        CPUSchedulerGUI.setExecutionTimeline(timeline);

        // Launch the GUI
        launch(args);
    }


}
