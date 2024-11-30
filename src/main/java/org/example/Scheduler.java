package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Scheduler {
    protected List<Process> ganttChart = new ArrayList<>();

    public abstract List<Process> run(List<Process> process);
}
