package org.example;

public class QuantumProcess extends Process {
    private int quantum; // Time slice allocated for the process

    public QuantumProcess(String name, int arrivalTime, int burstTime, int priority, int quantum) {
        super(name, arrivalTime, burstTime, priority);
        this.quantum = quantum;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public String toString() {
        return super.toString() + ", quantum=" + quantum;
    }
}
