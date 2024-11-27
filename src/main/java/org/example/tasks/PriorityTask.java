package org.example.tasks;

import java.time.LocalDate;

public class PriorityTask extends Task {
    private int priority; // 1 = High, 2 = Medium, 3 = Low, 0 = Normal

    // Constructor for date range (startDate and endDate)
    public PriorityTask(String name, String description, LocalDate startDate, LocalDate endDate, int priority, boolean isCompleted, String subjectColor, String typeOfClass) {
        super(name, description, startDate, endDate, isCompleted, subjectColor, typeOfClass); // Pass typeOfClass
        this.priority = 3; // Default to Low
        setPriority(priority);
        TaskManagerExc.addTask(this);
    }


    // Getter for priority
    public int getPriority() {
        return priority;
    }

    // Setter for priority with validation
    public void setPriority(int priority) {
        if (priority >= 0 && priority <= 3) {
            this.priority = priority;
        } else {
            this.priority = 3; // Default to Low if invalid
        }
    }

    // Override getTaskSummary to include priority
    @Override
    public String getTaskSummary() {
        return super.getTaskSummary() + "\nPriority: " + (priority == 1 ? "High" : priority == 2 ? "Medium" : "Low");
    }
}
