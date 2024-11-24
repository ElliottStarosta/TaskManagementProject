package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class WritingUtil {
    public static final String TASKS_PATH = "src/main/resources/tasks.json";

    public static ArrayList<PriorityTask> loadTasksFromJSON() {
        ArrayList<PriorityTask> tasks = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(new File(TASKS_PATH));

            for (JsonNode node : rootNode) {
                String name = node.get("name").asText();
                String description = node.get("description").asText();
                JsonNode legendNode = node.get("legend");
                String subjectColor = legendNode.get("color").asText();
                String typeOfClass = legendNode.get("subject").asText();
                boolean isCompleted = node.get("isCompleted").asBoolean();
                int priority = node.has("priority") ? node.get("priority").asInt() : 0;

                LocalDate startDate = null;
                LocalDate endDate = null;

                // Handle dueDate as a range or single date
                if (node.has("dueDate")) {
                    JsonNode dueDateNode = node.get("dueDate");
                    if (dueDateNode.has("startDate") && dueDateNode.has("endDate")) {
                        startDate = LocalDate.parse(dueDateNode.get("startDate").asText());
                        endDate = LocalDate.parse(dueDateNode.get("endDate").asText());
                    } else {
                        startDate = LocalDate.parse(dueDateNode.get("startDate").asText());
                    }
                }

                // Create the appropriate task type

                if (endDate != null) {
                    tasks.add(new PriorityTask(name, description, startDate, endDate, priority, isCompleted, subjectColor, typeOfClass));
                } else {
                    tasks.add(new PriorityTask(name, description, startDate, priority, isCompleted, subjectColor, typeOfClass));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tasks;
    }


    public static void writeTasksToJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode taskArray = objectMapper.createArrayNode();
        ArrayList<PriorityTask> tasks = TaskManagerExc.getTaskList();

        for (Task task : tasks) {
            ObjectNode taskNode = objectMapper.createObjectNode()
                    .put("name", task.getName())
                    .put("description", task.getDescription());

            // Add legend
            ObjectNode legendNode = taskNode.putObject("legend")
                    .put("color", task.getLegend()[0])
                    .put("subject", task.getLegend()[1]);



            // Add due date or date range
            LocalDate[] dueDateRange = task.getDueDateRange();
            ObjectNode dueDateNode = taskNode.putObject("dueDate");
            if (dueDateRange.length == 2) {
                dueDateNode.put("startDate", dueDateRange[0].toString());
                dueDateNode.put("endDate", dueDateRange[1].toString());
            } else if (dueDateRange.length == 1) {
                dueDateNode.put("startDate", dueDateRange[0].toString());
            }

            // Add completion status and priority
            taskNode.put("isCompleted", task.isCompleted());
            taskNode.put("priority", task instanceof PriorityTask ? ((PriorityTask) task).getPriority() : 0);

            taskArray.add(taskNode);
        }

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TASKS_PATH), taskArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTaskFromJSON(Task taskToDelete) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<PriorityTask> tasks = loadTasksFromJSON();  // Load existing tasks from the JSON file

        // Remove the task that matches the taskToDelete
        tasks.removeIf(task -> task.getTaskSummary().equals(taskToDelete.getTaskSummary()));

        // Write the updated list of tasks back to the JSON file
        writeUpdatedTasksToJSON(tasks, objectMapper);

    }

    private static void writeUpdatedTasksToJSON(ArrayList<PriorityTask> tasks, ObjectMapper objectMapper) {
        ArrayNode taskArray = objectMapper.createArrayNode();

        // Convert each task into a JSON node and add it to the array
        for (Task task : tasks) {
            ObjectNode taskNode = objectMapper.createObjectNode()
                    .put("name", task.getName())
                    .put("description", task.getDescription());

            // Add legend
            ObjectNode legendNode = taskNode.putObject("legend")
                    .put("color", task.getLegend()[0])
                    .put("subject", task.getLegend()[1]);

            // Add due date or date range
            LocalDate[] dueDateRange = task.getDueDateRange();
            ObjectNode dueDateNode = taskNode.putObject("dueDate");
            if (dueDateRange.length == 2) {
                dueDateNode.put("startDate", dueDateRange[0].toString());
                dueDateNode.put("endDate", dueDateRange[1].toString());
            } else if (dueDateRange.length == 1) {
                dueDateNode.put("startDate", dueDateRange[0].toString());
            }

            // Add completion status and priority
            taskNode.put("isCompleted", task.isCompleted());
            taskNode.put("priority", task instanceof PriorityTask ? ((PriorityTask) task).getPriority() : 0);

            taskArray.add(taskNode);
        }

        // Write the updated array to the JSON file
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(TASKS_PATH), taskArray);
            new TaskManagerExc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
