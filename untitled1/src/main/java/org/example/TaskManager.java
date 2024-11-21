package org.example;

import java.util.List;

public interface TaskManager {
    void addTask(Task task);
    void deleteTask(Task task);
    List<Task> getTasksByType(String type);
    void generateSummary();
}

