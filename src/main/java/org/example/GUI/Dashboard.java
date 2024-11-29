package org.example.GUI;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.GUI.Components.CustomColors;
import org.example.GUI.Components.StickyNote.CreateNote;
import org.example.GUI.Components.StickyNote.EditableNote;
import org.example.GUI.Components.interfaces.Clickable;
import org.example.WritingUtil;
import org.example.tasks.PriorityTask;
import org.example.tasks.TaskManagerExc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Dashboard extends JPanel implements Clickable {

    private final JFrame frame;
    private JPanel leftPanel, rightPanel;
    private Point startPt;

    JList<String> classFilter;
    JComboBox<String> statusFilter;

    public Dashboard(JFrame frame) {
        init();
        this.frame = frame;
    }

    private void init() {
        SwingUtilities.invokeLater(() -> {


            int frameWidth = frame.getWidth();
            int frameHeight = frame.getHeight();
            double mainWidth = 0.89; // percentage of left panel

            setLayout(new BoxLayout(Dashboard.this, BoxLayout.X_AXIS));

            // Left panel to hold the sticky notes
            leftPanel = new JPanel();
            leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Side by side with 10px gap
            leftPanel.setPreferredSize(new Dimension((int) (frameWidth * mainWidth), frameHeight));

            // Loop through the ArrayList of tasks and create sticky notes for each
            for (PriorityTask task : TaskManagerExc.getTaskList()) {
                JPanel notePanel = (JPanel) stickyNote(task);
                notePanel.setPreferredSize(new Dimension(notePanel.getPreferredSize().width, notePanel.getPreferredSize().height));

                // Add the sticky note to the left panel
                leftPanel.add(notePanel);

                // Make the note draggable
                makeDraggable(notePanel);
            }

            // Right panel
            rightPanel = new JPanel();
            rightPanel.putClientProperty(FlatClientProperties.STYLE, "background: darken(@background, 5%)");
            rightPanel.setPreferredSize(new Dimension((int) (frameWidth * (1 - mainWidth)), frameHeight));
            rightPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            // Center the components horizontally and vertically
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.weightx = 1.0;  // Allow panel to expand horizontally
            gbc.weighty = 1.0;  // Allow panel to expand vertically
            gbc.fill = GridBagConstraints.NONE;  // Don't fill extra space

            // Add the menu panel to the right panel
            rightPanel.add(menu(frameWidth, frameHeight), gbc);

            // Add the panels to the main frame
            add(leftPanel);
            add(rightPanel);
            filterTasksAndClasses(new ArrayList<>(List.of("Not Complete")), new ArrayList<>(List.of("All")));

            revalidate();
            repaint();
        });
    }

    private Component stickyNote(PriorityTask task) {
        // Main panel for the sticky note
        JPanel stickyNotePanel = new JPanel();

        stickyNotePanel.putClientProperty("task",task);

        stickyNotePanel.setLayout(null); // Absolute positioning
        stickyNotePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Measure the due date width dynamically
        String dueDateText = task.getFormattedDueDate();
        Font dueDateFont = new Font("Arial", Font.PLAIN, 12);
        FontMetrics metrics = stickyNotePanel.getFontMetrics(dueDateFont);
        int dueDateWidth = metrics.stringWidth(dueDateText);


        // Check if task has two dates (start and end date)
        LocalDate[] dueDateRange = task.getDueDateRange(); // Get the date range
        boolean hasTwoDates = dueDateRange != null && dueDateRange.length == 2 && dueDateRange[0] != null && dueDateRange[1] != null;

        int stickyNoteWidth = Math.max(260, (hasTwoDates ? 235 : 0) + dueDateWidth);

        stickyNotePanel.setPreferredSize(new Dimension(stickyNoteWidth, 170));
        String backgroundColor;

        switch (task.getPriority()) {
            case 1: // Urgent
                backgroundColor = "@urgent";
                break;
            case 2: // Default
                backgroundColor = "@normal";
                break;
            case 3: // Distant
                backgroundColor = "@distant";
                break;
            case 4: // None
            default:
                backgroundColor = "@none";
        }

        stickyNotePanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" + "[dark]background:" + backgroundColor);

        // Name Label (Top Left)
        JLabel nameLabel = new JLabel(task.getName());
        nameLabel.setBounds(13, 0, 200, 50);
        nameLabel.putClientProperty(FlatClientProperties.STYLE,
                "font:bold +6;" + "[dark]foreground:@white");
        stickyNotePanel.add(nameLabel);

        // Description Label (Below Name)
        JLabel descriptionLabel = new JLabel("<html>" + task.getDescription().replace("\n", "<br>") + "</html>");
        descriptionLabel.setBounds(13, 45, 280, 80);
        descriptionLabel.putClientProperty(FlatClientProperties.STYLE,
                "font: $small.font;" + "[dark]foreground:lighten(@white,25%)");
        stickyNotePanel.add(descriptionLabel);


        // Due Date Label (Top Right)
        JLabel dueDateLabel = new JLabel(dueDateText);
        dueDateLabel.setFont(dueDateFont);

        dueDateLabel.putClientProperty(FlatClientProperties.STYLE,
                "font: $medium.font;" + "[dark]foreground:darken(@white,5%)");

        dueDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dueDateLabel.setBounds(220, 0, dueDateWidth, 50);
        stickyNotePanel.add(dueDateLabel);


        // Legend Panel (Bottom Left)
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS)); // Horizontal alignment
        legendPanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" + "[dark]background:" + backgroundColor);
        legendPanel.setBounds(10, 130, 200, 40);

        // Legend Dot
        JLabel legendDot = new JLabel("\u25CF"); // Unicode for a filled circle
        legendDot.setFont(new Font("Arial", Font.PLAIN, 30));
        legendDot.setAlignmentY(JLabel.CENTER_ALIGNMENT); // Center-align within the BoxLayout
        legendPanel.add(legendDot);

        // Spacing between the dot and text
        legendPanel.add(Box.createHorizontalStrut(10));
        // Legend Text
        JLabel legendText = new JLabel(task.getLegend()[1]);
        legendText.putClientProperty(FlatClientProperties.STYLE,
                "font: bold;" + "[dark]foreground:darken(@white,15%)");
        legendText.setAlignmentY(JLabel.CENTER_ALIGNMENT); // Center-align within the BoxLayout
        legendPanel.add(legendText);

        try {
            // Attempt to convert the subject color string to a Color object
            Color color = CustomColors.getColor(task.getLegend()[0]);
            legendDot.setForeground(color); // Apply subject color to the legend dot
        } catch (Exception e) {
            // If the subjectColor is invalid or not found, set to a default color (e.g., gray)
            legendDot.setForeground(Color.GRAY);
        }

        legendPanel.add(legendText);

        if (task.isCompleted()) {
            stickyNotePanel.putClientProperty(FlatClientProperties.STYLE,
                    "arc:20;" + "[dark]background:darken(@stickyNote,15%)");
            legendPanel.putClientProperty(FlatClientProperties.STYLE,
                    "arc:20;" + "[dark]background:darken(@stickyNote,15%)");
        }

        stickyNotePanel.add(legendPanel);

        doubleClick(stickyNotePanel, task);

        return stickyNotePanel;
    }

    @Override
    public void makeDraggable(JPanel stickyNote) {
        stickyNote.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPt = SwingUtilities.convertPoint(stickyNote, e.getPoint(), stickyNote.getParent());
            }
        });

        stickyNote.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPt = SwingUtilities.convertPoint(stickyNote, e.getPoint(), stickyNote.getParent());
                int deltaX = currentPt.x - startPt.x;
                int deltaY = currentPt.y - startPt.y;

                // Calc new Pt
                Point newLocation = stickyNote.getLocation();
                newLocation.translate(deltaX, deltaY);

                // Get bounds of leftPanel and stickyNote
                Rectangle panelBounds = leftPanel.getBounds();
                Dimension noteSize = stickyNote.getSize();

                // Ensure the sticky note doesn't go outside the panel
                int maxX = panelBounds.width - noteSize.width;
                int maxY = panelBounds.height - noteSize.height;

                // Clamp the new location within bounds
                newLocation.x = Math.max(0, Math.min(newLocation.x, maxX));
                newLocation.y = Math.max(0, Math.min(newLocation.y, maxY));

                stickyNote.setLocation(newLocation);
                startPt = currentPt;

                // Check for collisions
                for (Component comp : leftPanel.getComponents()) {
                    if (comp != stickyNote && comp instanceof JPanel) {
                        JPanel otherNote = (JPanel) comp;
                        if (stickyNote.getBounds().intersects(otherNote.getBounds())) {
                            combineTasks(stickyNote, otherNote);
                            break;
                        }
                    }
                }
            }
        });
    }


    private PriorityTask getTaskFromStickyNote(JPanel stickyNote) {
        for (PriorityTask task : TaskManagerExc.getTaskList()) {
            if (task.getTaskSummary().equals(getTaskFromNotePanel(stickyNote).getTaskSummary())) {
                return task;
            }
        }
        throw new IllegalArgumentException("Task not found for the sticky note");
    }


    private LocalDate[] mergeDueDates(LocalDate[] dates1, LocalDate[] dates2) {
        LocalDate start = dates1[0].isBefore(dates2[0]) ? dates1[0] : dates2[0];
        LocalDate end = dates1[1].isAfter(dates2[1]) ? dates1[1] : dates2[1];
        return new LocalDate[]{start, end};
    }


    private void combineTasks(JPanel stickyNote1, JPanel stickyNote2) {
        PriorityTask task1 = getTaskFromStickyNote(stickyNote1);
        PriorityTask task2 = getTaskFromStickyNote(stickyNote2);

        // Create combined task name with (S) prefix
        String combinedName = "(S)" + task1.getName();

        // Create a combined description
        String combinedDescription = "Combined task of: " + task1.getName() + " and " + task2.getName() + "\n\n" +
                "Description of combined:\n" + task1.getDescription() + "\n\n" +
                task2.getDescription();

        // Merge the due date ranges
        LocalDate[] combinedDueDates = mergeDueDates(task1.getDueDateRange(), task2.getDueDateRange());

        String task1Subject = task1.getLegend()[1];
        String task2Subject = task2.getLegend()[1];
        String mergedSubject = (task1Subject.equals(task2Subject)) ? task1Subject : task2Subject + "/" + task1Subject;

        // Check if the mergedSubject is not already in the legend
        if (!TaskManagerExc.getLegend().keySet().contains(mergedSubject)) {

            // Combine the colors
            Color combinedColor = CustomColors.combineColors(mergedSubject,task1.getLegend()[0], task2.getLegend()[0]);
            String combinedColorS = CustomColors.convertColorString(String.valueOf(combinedColor));

            // Add the combined color to the legend
            TaskManagerExc.addLegendElement(mergedSubject, String.valueOf(combinedColorS));
            task1.setLegend(String.valueOf(combinedColorS),mergedSubject);
        }

        // Determine the highest priority (urgency) between the two tasks
        int priorityValue1 = task1.getPriority();
        int priorityValue2 = task2.getPriority();
        int highestPriorityValue = Math.min(priorityValue1, priorityValue2);
        task1.setPriority(highestPriorityValue);


        // Update task1 with combined properties
        task1.setName(combinedName);
        task1.setDescription(combinedDescription);
        task1.setDueDateRange(combinedDueDates);


        // Remove stickyNote2 from the panel and task list
        leftPanel.remove(stickyNote2);

        TaskManagerExc.removeTask(task2);
        WritingUtil.writeTasksToJSON();
        filterNotes();
        // Refresh the UI
        leftPanel.revalidate();
        leftPanel.repaint();

        rightPanel.revalidate();
        rightPanel.repaint();
    }



    private PriorityTask getTaskFromNotePanel(JPanel notePanel) {
        return (PriorityTask) notePanel.getClientProperty("task");
    }




    @Override
    public void doubleClick(JPanel stickyNote, PriorityTask task) {
        stickyNote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    FormsManager.getInstance().showForm(new EditableNote(frame, task));
                }
            }
        });
    }

    private Component menu(int frameWidth, int frameHeight) {
        // Create a panel to hold the menu components
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        menuPanel.putClientProperty(FlatClientProperties.STYLE, "background: lighten(@background,5%); arc:10");

        // Add padding to the menuPanel (top, left, bottom, right)
        menuPanel.setBorder(new EmptyBorder(20, 5, 20, 5));

        // Create the components (Button and ComboBoxes)
        JButton addButton = new JButton("Add Task");
        addButton.setPreferredSize(new Dimension(10, 70));  // Increased the height of the button
        addButton.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; background: lighten(@background, 20%)");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addButton.addActionListener(e -> handleButtonEvent());

        JLabel classLabel = new JLabel("Subjects");
        classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        classLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +1");

        // Multi-select list for classes
        classFilter = new JList<>(TaskManagerExc.getClasses());
        classFilter.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        classFilter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        classFilter.setFixedCellWidth(125);
        classFilter.setFixedCellHeight(50);
        classFilter.putClientProperty(FlatClientProperties.STYLE,
                "font: medium;" +
                        "background: lighten(@background, 10%);" +
                        "selectionBackground: lighten(@stickyNote, 5%); " +
                        "selectionInactiveBackground: lighten(@stickyNote, 5%)");
        classFilter.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the list horizontally
        classFilter.setToolTipText("Class Filter");
        classFilter.setSelectedIndex(0);

        JLabel completionLabel = new JLabel("Completion");
        completionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        completionLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +1");


        // Multi-select list for task status
        statusFilter = new JComboBox<>(new String[]{"All", "Complete", "Not Complete"});
        statusFilter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        statusFilter.setSelectedItem("Not Complete");

        statusFilter.putClientProperty(FlatClientProperties.STYLE,
                "font: medium;" +
                        "background: lighten(@background, 10%)");
        statusFilter.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adding a listener for the statusFilter JComboBox (for statuses like "Complete", "Not Complete", etc.)
        statusFilter.addActionListener(e -> {
            filterNotes();
        });

        // Adding a listener for the classFilter JList
        classFilter.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                filterNotes();
            }
        });


        // Add the components to the menu panel
        menuPanel.add(addButton);
        menuPanel.add(Box.createVerticalStrut(35));
        menuPanel.add(classLabel);
        menuPanel.add(Box.createVerticalStrut(10));  // Increased space between components
        menuPanel.add(classFilter);  // Add the class filter
        menuPanel.add(Box.createVerticalStrut(35));  // Increased space between components
        menuPanel.add(completionLabel);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(statusFilter);  // Add the status filter

        // Center the components inside the menuPanel
        menuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center horizontally

        // Create the rightPanel and center the menuPanel inside it
        JPanel rightPanel = new JPanel();

        rightPanel.setLayout(new BorderLayout());  // Use BorderLayout for centering

        // Add the menuPanel to the center of rightPanel
        rightPanel.add(menuPanel, BorderLayout.CENTER);

        // Set the size of the rightPanel as needed
        rightPanel.setPreferredSize(new Dimension((int) (frameWidth * 0.1), frameHeight));

        return rightPanel;
    }

    private void filterNotes() {
        // Get selected statuses
        String selectedStatus = (String) statusFilter.getSelectedItem();
        // Get selected classes from classFilter JList
        List<String> selectedClasses = classFilter.getSelectedValuesList();
        // Filter based on both selected statuses and selected classes
        filterTasksAndClasses(new ArrayList<>(List.of(selectedStatus)), new ArrayList<>(selectedClasses));
    }

    private void filterTasksAndClasses(ArrayList<String> selectedStatuses, ArrayList<String> selectedClasses) {
        leftPanel.removeAll(); // Clear existing sticky notes

        for (PriorityTask task : TaskManagerExc.getTaskList()) {
            boolean shouldDisplay = false;

            // Filter by status
            if (selectedStatuses.contains("All")) {
                shouldDisplay = true;
            } else if (selectedStatuses.contains("Complete") && task.isCompleted()) {
                shouldDisplay = true;
            } else if (selectedStatuses.contains("Not Complete") && !task.isCompleted()) {
                shouldDisplay = true;
            }

            // Filter by class (only if the status filter allowed the task to be displayed)
            if (shouldDisplay) {
                if (selectedClasses.contains("All")) {
                    shouldDisplay = true;
                } else {
                    String[] taskLegend = task.getLegend();
                    if (taskLegend != null && taskLegend.length > 0) {
                        for (String className : selectedClasses) {
                            if (taskLegend[1].contains(className)) { // Compare the selected class with the task's legend
                                shouldDisplay = true;
                                break; // Stop checking once a match is found
                            } else {
                                shouldDisplay = false;
                            }
                        }
                    } else {
                        shouldDisplay = false; // If no class matches or taskLegend is null
                    }
                }

            }

            // If both status and class filters allow the task to be displayed, add the sticky note
            if (shouldDisplay) {
                JPanel notePanel = (JPanel) stickyNote(task);
                notePanel.setPreferredSize(new Dimension(notePanel.getPreferredSize().width, notePanel.getPreferredSize().height));
                leftPanel.add(notePanel);
                makeDraggable(notePanel);
            }
        }

        // Update UI
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    @Override
    public void handleButtonEvent() {
        FormsManager.getInstance().showForm(new CreateNote(frame));
    }

}