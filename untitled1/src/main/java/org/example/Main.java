package org.example;

import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Task t1 = new Task("Task1", "No description", LocalDate.of(2024, 11, 21), false);
        Task t2 = new Task("Task2", "No description", LocalDate.of(2023, 11, 21), true);

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(t1);
        tasks.add(t2);
        Util.writeTasksToJSON(tasks);

    }
}
