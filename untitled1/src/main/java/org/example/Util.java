package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Util {
    public static final String TASKS_PATH = "untitled1/src/main/resources/tasks.json";

    // Method to load tasks from JSON
    public static ArrayList<Task> loadTasksFromJSON() {
        ArrayList<Task> tasks = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(new File(TASKS_PATH));

            for (JsonNode node : rootNode) {
                String name = node.get("name").asText();
                String description = node.get("description").asText();
                LocalDate dueDate = LocalDate.parse(node.get("dueDate").asText());
                int priority = node.get("priority").asInt();
                boolean isCompleted = node.get("isCompleted").asBoolean();

                // Create Task or PriorityTask based on priority
                if (priority == 0) {
                    tasks.add(new Task(name, description, dueDate, isCompleted));
                } else {
                    tasks.add(new PriorityTask(name, description, dueDate, priority, isCompleted));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    // Method to write tasks to a JSON file
    public static void writeTasksToJSON(ArrayList<Task> tasks) {
        ObjectMapper objectMapper = new ObjectMapper();

        // Create a JSON array to hold task objects
        ArrayNode taskArray = objectMapper.createArrayNode();

        // Iterate over tasks and convert each to a JSON object
        for (Task task : tasks) {
            // Create a JSON object for the task
            JsonNode taskNode = objectMapper.createObjectNode()
                    .put("name", task.getName())
                    .put("description", task.getDescription())
                    .put("dueDate", task.getDueDate().toString())
                    .put("priority", task instanceof PriorityTask ? ((PriorityTask) task).getPriority() : 0)
                    .put("isCompleted", task.isCompleted());

            taskArray.add(taskNode);
        }

        try {
            // Write the JSON array to the file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TASKS_PATH), taskArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
