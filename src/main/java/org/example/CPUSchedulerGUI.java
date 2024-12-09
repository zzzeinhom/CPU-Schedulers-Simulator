package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class CPUSchedulerGUI extends Application {

    private static List<ExecutionSegment> executionTimeline;

    public static void setExecutionTimeline(List<ExecutionSegment> timeline) {
        executionTimeline = timeline;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CPU Scheduling Visualization");

        // Canvas dimensions
        int canvasWidth = 800;
        int canvasHeight = 400;
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawGanttChart(gc, canvasWidth, canvasHeight);

        VBox root = new VBox(canvas);
        Scene scene = new Scene(root);

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

    public static void main(String[] args) {
        // Example data
        setExecutionTimeline(List.of(
                new ExecutionSegment("P1", 5, Color.RED),
                new ExecutionSegment("P2", 3, Color.BLUE),
                new ExecutionSegment("P3", 7, Color.GREEN),
                new ExecutionSegment("P4", 2, Color.YELLOW)
        ));

        launch(args);
    }
}
