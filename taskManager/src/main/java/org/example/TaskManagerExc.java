package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManagerExc implements TaskManager {
    private static ArrayList<Task> taskList = new ArrayList<>();
    private static ArrayList<String> subjectFilters = new ArrayList<>(List.of(new String[]{"Math", "Physics"}));

    public TaskManagerExc() {
        taskList = WritingUtil.loadTasksFromJSON();
    }


    public static void addTask(Task task) {
        taskList.add(task);
    }

    public static ArrayList<Task> getTaskList() {
        return taskList;
    }

    public static ArrayList<String> getSubjectFilters() {
        return subjectFilters;
    }

    @Override
    public void deleteTask(Task task) {
        taskList.remove(task);
    }

    @Override
    public List<Task> getTasksByType(boolean filter) {
        if (filter) {
            return taskList.stream().filter(Task::isCompleted).collect(Collectors.toList());
        } else if (!(filter)) {
            return taskList.stream().filter(task -> !task.isCompleted()).collect(Collectors.toList());
        }
        return taskList; // No filtering
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
