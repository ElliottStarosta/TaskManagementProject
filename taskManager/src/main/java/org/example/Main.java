package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Task t1 =new Task("Science Project", "Prepare presentation", LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 2), false, "Green", "Math");
        Task t =new Task("Math Homework", "Complete exercises", LocalDate.of(2024, 1, 10), LocalDate.of(2024, 1, 5), false, "Blue", "Math");

        WritingUtil.writeTasksToJSON();

        ArrayList<Task> l = new ArrayList<>(WritingUtil.loadTasksFromJSON());


    }
}
