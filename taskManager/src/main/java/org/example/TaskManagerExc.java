package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManagerExc implements TaskManager {
    private static ArrayList<PriorityTask> taskList = new ArrayList<>();
    private static LinkedHashMap<String, String> legend = new LinkedHashMap<>() {{
        put("All", null);
        put("Math", "RED");
        put("Science","GREEN");
        put("History", "ORANGE");
        put("English", "BLUE");
        put("Computer Science", "PINK");
        put("Art", "LIME");
        put("Music", "CYAN");
        put("Economics", "GOLD");
    }};

    private static String[] classes = legend.keySet().toArray(new String[0]);
    private static ArrayList<String> subjectFilters = new ArrayList<>(List.of(classes));


    public TaskManagerExc() {
        taskList = WritingUtil.loadTasksFromJSON();
    }


    public static void addTask(PriorityTask task) {
        taskList.add(task);
    }

    public static ArrayList<PriorityTask> getTaskList() {
        return taskList;
    }

    public static ArrayList<String> getSubjectFilters() {
        return subjectFilters;
    }

    public static void deleteTask(Task task) {
        taskList.remove(task);
    }

    @Override
    public List<PriorityTask> getTasksByType(boolean filter) {
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

    public static String[] getClasses() {
        return classes;
    }

    public static HashMap<String,String> getLegend() {
        return legend;
    }
}
