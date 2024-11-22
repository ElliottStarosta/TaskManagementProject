package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        PriorityTask task = new PriorityTask("Do UML","Umling",LocalDate.of(1,1,1),LocalDate.of(1,2,3),2,false,"Red","Science");
        Task task2 = new Task("Do this","Umling",LocalDate.of(1,2,3),false,"Red","Science");

        WritingUtil.writeTasksToJSON();

        ArrayList<Task> l = new ArrayList<>(WritingUtil.loadTasksFromJSON());


    }
}
