package org.example;

import java.util.List;

public interface TaskManager {
    static void addTask(Task task) {
        addTask(null);
    }
    void deleteTask(Task task);
    List<Task> getTasksByType(boolean filter);
    void generateSummary();
}

