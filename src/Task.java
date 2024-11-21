import java.time.LocalDate;

public class Task {
    private String name;
    private String description;
    private LocalDate dueDate;
    private boolean isCompleted;

    public Task(String name, String description, LocalDate dueDate) {
        setName(name);
        setDescription(description);
        setDueDate(dueDate);
        setComplete(true);
    }

    public Task(String name, LocalDate dueDate) {
        this(name,"",dueDate);
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    private void setComplete(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getTaskSummary() {
        return "Task: " + name + "\nDescription: " + description +
                "\nDue: " + dueDate + "\nCompleted: " + isCompleted;
    }
}
