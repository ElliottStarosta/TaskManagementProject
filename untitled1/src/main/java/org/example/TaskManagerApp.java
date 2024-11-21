package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManagerApp implements TaskManager {
    private ArrayList<Task> taskList;

    public TaskManagerApp() {
        taskList = Util.loadTasksFromJSON();
    }

    @Override
    public void addTask(Task task) {
        taskList.add(task);
    }

    @Override
    public void deleteTask(Task task) {
        taskList.remove(task);
    }

    @Override
    public List<Task> getTasksByType(String filter) {
        switch (filter.toLowerCase()) {
            case "completed":
                return taskList.stream().filter(Task::isCompleted).collect(Collectors.toList());
            case "incomplete":
                return taskList.stream().filter(task -> !task.isCompleted()).collect(Collectors.toList());
            default:
                return taskList; // No filtering
        }
    }

    @Override
    public void generateSummary() {
        System.out.println("Task Summary:");
        for (Task task : taskList) {
            System.out.println(task.getTaskSummary());
            System.out.println("--------------------");
        }
    }
}
