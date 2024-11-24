package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Task {
    private String name;
    private String description;
    private String[] legend;
    private LocalDate[] dueDateRange;
    private boolean isCompleted;



    // Constructor for single date
    public Task(String name, String description, LocalDate dueDate, boolean isCompleted, String color, String subject) {
        this(name,description,dueDate,dueDate,isCompleted,color,subject);
    }

    // Constructor for date range (start and end date)
    public Task(String name, String description, LocalDate startDate, LocalDate endDate, boolean isCompleted, String color, String subject) {

        setName(name);
        setDescription(description);
        setDueDate(startDate, endDate);
        setComplete(isCompleted);
        setLegend(color, subject);
    }

    // Constructor for task with only name, due date, and subject color (no description)
    public Task(String name, LocalDate dueDate, boolean isCompleted, String color, String subject) {
        this(name, "", dueDate, isCompleted, color, subject);
    }

    // Constructor for task with only name, date range, and subject color (no description)
    public Task(String name, LocalDate startDate, LocalDate endDate, boolean isCompleted, String color, String subject) {
        this(name, "", startDate, endDate, isCompleted, color, subject);
    }

    // Set a single date (for due date)
    public void setDueDate(LocalDate dueDate) {
        this.dueDateRange = new LocalDate[]{dueDate};  // Single date
    }

    // Set a range of dates (for due date)
    public void setDueDate(LocalDate startDate, LocalDate endDate) {
        this.dueDateRange = new LocalDate[]{startDate, endDate};  // Range of dates
    }

    // Getter for formatted due date (single date or date range)
    public String getFormattedDueDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        if (dueDateRange.length == 1) {
            return dueDateRange[0].format(formatter);  // Single date
        } else {
            return dueDateRange[0].format(formatter) + " - " + dueDateRange[1].format(formatter);  // Date range
        }
    }

    // Getter for subject color and type of class
    public String[] getLegend() {
        return this.legend;
    }

    // Setter for subject color and type of class
    public void setLegend(String color, String subject) {
        this.legend = new String[]{color.toUpperCase(), subject};
    }

    // Getter and Setter methods for other fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate[] getDueDateRange() {
        return dueDateRange;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        setComplete(true);
    }

    public void setComplete(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }


    public String getTaskSummary() {
        return "Task: " + name + "\nDescription: " + description +
                "\nDue: " + getFormattedDueDate() + "\nCompleted: " + isCompleted;
    }
}
