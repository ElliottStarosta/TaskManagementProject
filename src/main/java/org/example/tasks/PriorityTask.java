package org.example.tasks;

import java.time.LocalDate;

public class PriorityTask extends Task {
    private int priority; // 1 = Urgent, 2 =Normal, 3 = Distant, 4 = None

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
        if (priority >= 1 && priority <= 4) {
            this.priority = priority;
        } else {
            this.priority = 4; // Default to none if invalid
        }
    }

    // Override getTaskSummary to include priority
    @Override
    public String getTaskSummary() {
        return super.getTaskSummary() + "\nPriority: " + (priority == 1 ? "High" : priority == 2 ? "Medium" : "Low");
    }
}
