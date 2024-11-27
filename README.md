# TaskManager

Welcome to the **TaskManager Project**! This repository contains a Java-based application designed to manage tasks efficiently, providing users with features such as adding, editing, and tracking tasks.

---

## Features
- **Task Management:** Add, edit, delete, and view tasks.
- **Priority Levels:** Assign priority levels to tasks for better organization.
- **Due Dates:** Track deadlines with due date functionality.
- **Categorization:** Organize tasks into categories for clarity.
- **User-Friendly Interface:** Simple and intuitive GUI for seamless task management.

---

## Technologies Used
- **Programming Language:** Java
- **GUI Framework:** Swing
- **Build Tool:** Maven
- **Testing:** JUnit
- **Version Control:** Git

---

## Installation

### Prerequisites:
- Ensure you have **Java 11** or higher installed.
- Install **Maven** for dependency management.
- A Git client for cloning the repository.

### Steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/ElliottStarosta/TaskManagementProject.git
   ```
2. Navigate to the project directory:
   ```bash
   cd TaskManager
   ```
3. Install dependencies:
   ```bash
   mvn install
   ```
4. Run the application:
   ```bash
   mvn exec:java
   ```

---

## Usage
1. Launch the application using the steps above.
2. Use the GUI to:
    - Add new tasks by providing details like title, description, priority, and due date.
    - Edit or delete existing tasks as needed.
    - View all tasks categorized by priority or due date.

---

## Folder Structure
```
TaskManager/
|-- .idea/                     # IntelliJ IDEA project settings
|-- src/
|   |-- main/
|       |-- java/
|           |-- org/example/
|               |-- GUI/
|                   |-- Components/
|                       |-- interfaces/
|                           |-- Clickable.java    # Interface for clickable components
|                       |-- StickyNote/
|                           |-- CreateNote.java   # Handles creation of sticky notes
|                           |-- EditableNote.java # Manages editable sticky notes
|                   |-- CustomColors.java         # Custom color utilities
|                   |-- Dashboard.java            # Main dashboard interface
|                   |-- FormsManager.java         # Handles form management
|                   |-- Main.java                 # Entry point of the application
|               |-- tasks/
|                   |-- PriorityTask.java         # Task model with priority
|                   |-- Task.java                 # Basic task model
|                   |-- TaskManagerExc.java       # Task manager exception handling
|                   |-- WritingUtil.java          # Utility class for writing tasks
|       |-- resources/
|           |-- themes/
|               |-- tasks.json                   # Predefined task data
|-- target/                     # Compiled classes and build artifacts
|-- pom.xml                     # Maven configuration file
|-- README.md                   # Project documentation
```

---

## License
This project is licensed under the [MIT License](LICENSE.txt).

---
