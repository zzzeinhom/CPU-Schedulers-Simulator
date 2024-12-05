package org.example;

public class QuantumProcess extends Process {
    private int quantum; // Time slice allocated for the process
    private double factor;
    public int unusedQuantum = quantum;
    public QuantumProcess(String name, int arrivalTime, int burstTime, int priority, int quantum,double factor) {
        super(name, arrivalTime, burstTime, priority);
        this.quantum = quantum;
        this.factor = factor;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public double getFactor() { return factor; }
    public void setFactor(double factor) { this.factor = factor; }

    @Override
    public String toString() {
        return super.toString() + ", quantum=" + quantum;
    }
}
