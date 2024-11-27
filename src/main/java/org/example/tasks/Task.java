package org.example.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Task {
    private String name;
    private String description;
    private String[] legend; // Array that stores [color, subject]
    private LocalDate[] dueDateRange; // Array that stores [start, end] or [start, start]
    private boolean isCompleted;

    // Constructor for date range (start and end date)
    public Task(String name, String description, LocalDate startDate, LocalDate endDate, boolean isCompleted, String color, String subject) {
        setName(name);
        setDescription(description);
        setDueDate(startDate, endDate);
        setComplete(isCompleted);
        setLegend(color, subject);
    }

    // Set a range of dates (for due date)
    public void setDueDate(LocalDate startDate, LocalDate endDate) {
        dueDateRange = new LocalDate[]{startDate, endDate};  // Range of dates
    }

    // Getter for formatted due date (single date or date range)
    public String getFormattedDueDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        if (dueDateRange[0].equals(dueDateRange[1])) { // If the dates are the same, treat it like one day
            return dueDateRange[0].format(formatter);
        } else {
            return dueDateRange[0].format(formatter) + " - " + dueDateRange[1].format(formatter);  // Date range
        }
    }

    // Getter for subject color and type of class
    public String[] getLegend() {
        return legend;
    }

    // Setter for subject color and type of class
    public void setLegend(String color, String subject) {
        legend = new String[]{color.toUpperCase(), subject};
    }

    // Getter and Setter methods for other fields
    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    public LocalDate[] getDueDateRange() {
        return dueDateRange;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setComplete(boolean isCom) {
        isCompleted = isCom;
    }

    // Used to check for equality later on
    public String getTaskSummary() {
        return "Task: " + name + "\nDescription: " + description +
                "\nDue: " + getFormattedDueDate() + "\nCompleted: " + isCompleted;
    }
}
