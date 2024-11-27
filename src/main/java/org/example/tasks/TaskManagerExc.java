package org.example.tasks;

import org.example.WritingUtil;

import java.util.*;

public class TaskManagerExc {
    private static final LinkedHashMap<String, String> legend = new LinkedHashMap<>() {{
        put("All", null);
        put("Math", "RED");
        put("Science", "GREEN");
        put("History", "ORANGE");
        put("English", "BLUE");
        put("Computer Science", "PINK");
        put("Art", "LIME");
        put("Music", "CYAN");
        put("Economics", "GOLD");
    }};

    private static final String[] classes = Arrays.copyOfRange(legend.keySet().toArray(new String[0]), 1, legend.keySet().size());
    private static final ArrayList<String> subjectFilters = new ArrayList<>(List.of(classes));
    private static ArrayList<PriorityTask> taskList = new ArrayList<>();


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

    public static String[] getClasses() {
        return legend.keySet().toArray(new String[0]);
    }

    public static HashMap<String, String> getLegend() {
        return legend;
    }
}