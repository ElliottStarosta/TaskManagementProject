package org.example;

import java.time.LocalDate;

public class PriorityTask extends Task {
    private int priority; // 1 = High, 2 = Medium, 3 = Low

    public PriorityTask(String name, String description, LocalDate dueDate, int priority, boolean isCompleted) {
        super(name, description, dueDate,isCompleted);
        this.priority = 3;
        setPriority(priority);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = (priority >= 0 && priority <= 3) ? priority : getPriority();
    }

    @Override
    public String getTaskSummary() {
        return super.getTaskSummary() + "\nPriority: " + (priority == 1 ? "High" : priority == 2 ? "Medium" : "Low");
    }
}
