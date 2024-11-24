package org.example;

import java.util.List;

public interface TaskManager {
    static void addTask(PriorityTask task) {
        addTask(null);
    }
    static void deleteTask(PriorityTask task) {
    }
    List<PriorityTask> getTasksByType(boolean filter);
    void generateSummary();
}

