package org.example;

import java.time.LocalDate;

public class PriorityTask extends Task {
    private int priority; // 1 = High, 2 = Medium, 3 = Low

    // Constructor for single date
    public PriorityTask(String name, String description, LocalDate dueDate, int priority, boolean isCompleted, String subjectColor, String typeOfClass) {
        super(name, description, dueDate, isCompleted, subjectColor, typeOfClass); // Pass typeOfClass
        this.priority = 3; // Default to Low
        setPriority(priority);
    }

    // Constructor for date range (startDate and endDate)
    public PriorityTask(String name, String description, LocalDate startDate, LocalDate endDate, int priority, boolean isCompleted, String subjectColor, String typeOfClass) {
        super(name, description, startDate, endDate, isCompleted, subjectColor, typeOfClass); // Pass typeOfClass
        this.priority = 3; // Default to Low
        setPriority(priority);
    }

    // Getter for priority
    public int getPriority() {
        return priority;
    }

    // Setter for priority with validation
    public void setPriority(int priority) {
        this.priority = (priority >= 1 && priority <= 3) ? priority : 3; // Default to Low if invalid
    }

    // Override getTaskSummary to include priority
    @Override
    public String getTaskSummary() {
        return super.getTaskSummary() + "\nPriority: " + (priority == 1 ? "High" : priority == 2 ? "Medium" : "Low");
    }
}
