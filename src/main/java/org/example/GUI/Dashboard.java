package org.example.GUI;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.GUI.Components.CustomColors;
import org.example.GUI.Components.StickyNote.CreateNote;
import org.example.GUI.Components.StickyNote.EditableNote;
import org.example.GUI.Components.interfaces.Clickable;
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

    public Dashboard(JFrame frame) {
        init();
        this.frame = frame;
    }

    private void init() {
        SwingUtilities.invokeLater(() -> {


            int frameWidth = frame.getWidth();
            int frameHeight = frame.getHeight();
            double mainWidth = 0.89;

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

        int stickyNoteWidth = Math.max(260, (hasTwoDates ? 195 : 0) + dueDateWidth);

        stickyNotePanel.setPreferredSize(new Dimension(stickyNoteWidth, 150));
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
            case 0: // None
            default:
                backgroundColor = "@none";
        }

        stickyNotePanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" + "[dark]background:" + backgroundColor);

        // Name Label (Top Left)
        JLabel nameLabel = new JLabel(task.getName());
        nameLabel.setBounds(13, 0, 200, 50);
        nameLabel.putClientProperty(FlatClientProperties.STYLE,
                "font:bold +7;" + "[dark]foreground:@white");
        stickyNotePanel.add(nameLabel);

        // Description Label (Below Name)
        JLabel descriptionLabel = new JLabel(task.getDescription());
        descriptionLabel.setBounds(13, 40, 280, 40); // Allow room for multi-line text
        descriptionLabel.putClientProperty(FlatClientProperties.STYLE,
                "font: $small.font;" + "[dark]foreground:lighten(@white,25%)");
        stickyNotePanel.add(descriptionLabel);

        // Due Date Label (Top Right)
        JLabel dueDateLabel = new JLabel(dueDateText);
        dueDateLabel.setFont(dueDateFont);

        dueDateLabel.putClientProperty(FlatClientProperties.STYLE,
                "font: $medium.font;" + "[dark]foreground:darken(@white,5%)");

        dueDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dueDateLabel.setBounds(180, 0, dueDateWidth, 50); // Adjust width based on due date length
        stickyNotePanel.add(dueDateLabel);


        // Legend Panel (Bottom Left)
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS)); // Horizontal alignment
        legendPanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" + "[dark]background:" + backgroundColor);
        legendPanel.setBounds(10, 110, 200, 40); // Adjusted width and height for spacing

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
            Color color = (Color) CustomColors.class.getField(task.getLegend()[0]).get(null);
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
                // Capture the initial mouse press location relative to the sticky note's position
                startPt = SwingUtilities.convertPoint(stickyNote, e.getPoint(), stickyNote.getParent());
            }
        });

        stickyNote.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point location = SwingUtilities.convertPoint(stickyNote, e.getPoint(), stickyNote.getParent());

                if (stickyNote.getParent().getBounds().contains(location)) {
                    Point newLocation = stickyNote.getLocation();
                    newLocation.translate(location.x - startPt.x, location.y - startPt.y);

                    // Get the bounds of the sticky note
                    Rectangle newBounds = new Rectangle(newLocation.x, newLocation.y, stickyNote.getWidth(), stickyNote.getHeight());

                    // Loop through all other sticky notes to check for collisions
                    for (Component comp : stickyNote.getParent().getComponents()) {
                        if (comp != stickyNote && comp instanceof JPanel otherNote) {

                            // Get the bounds of the other sticky note
                            Rectangle otherBounds = otherNote.getBounds();

                            // Check if the new position would cause overlap with another sticky note
                            if (newBounds.intersects(otherBounds)) {
                                // Check which side the sticky note is closest to the other sticky note
                                if (Math.abs(newBounds.x + newBounds.width - otherBounds.x) <= 100) { // Right side close
                                    newLocation.x = otherBounds.x - newBounds.width; // Snap to the left of the other note
                                } else if (Math.abs(newBounds.x - (otherBounds.x + otherBounds.width)) <= 100) { // Left side close
                                    newLocation.x = otherBounds.x + otherBounds.width; // Snap to the right of the other note
                                } else if (Math.abs(newBounds.y + newBounds.height - otherBounds.y) <= 100) { // Bottom side close
                                    newLocation.y = otherBounds.y - newBounds.height; // Snap above the other note
                                } else if (Math.abs(newBounds.y - (otherBounds.y + otherBounds.height)) <= 100) { // Top side close
                                    newLocation.y = otherBounds.y + otherBounds.height; // Snap below the other note
                                }
                            }
                        }
                    }

                    // Keep the sticky note within the bounds of the parent panel
                    newLocation.x = Math.max(newLocation.x, 0);
                    newLocation.y = Math.max(newLocation.y, 0);
                    newLocation.x = Math.min(newLocation.x, stickyNote.getParent().getWidth() - stickyNote.getWidth());
                    newLocation.y = Math.min(newLocation.y, stickyNote.getParent().getHeight() - stickyNote.getHeight());

                    // Set the adjusted position
                    stickyNote.setLocation(newLocation);
                    startPt = location;
                }
            }
        });
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
        JList<String> classFilter = new JList<>(TaskManagerExc.getClasses());
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
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "Complete", "Not Complete"});
        statusFilter.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        statusFilter.setSelectedItem("Not Complete");

        statusFilter.putClientProperty(FlatClientProperties.STYLE,
                "font: medium;" +
                        "background: lighten(@background, 10%)");
        statusFilter.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adding a listener for the statusFilter JComboBox (for statuses like "Complete", "Not Complete", etc.)
        statusFilter.addActionListener(e -> {
            // Get selected statuses
            String selectedStatus = (String) statusFilter.getSelectedItem();

            // Get selected classes from classFilter JList
            List<String> selectedClasses = classFilter.getSelectedValuesList();
            // Filter based on both selected statuses and selected classes
            filterTasksAndClasses(new ArrayList<>(List.of(selectedStatus)), new ArrayList<>(selectedClasses));
        });

        // Adding a listener for the classFilter JList
        classFilter.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Get selected classes from classFilter JList
                List<String> selectedClasses = classFilter.getSelectedValuesList();  // Works for JList
                // Get selected status from statusFilter JComboBox
                String selectedStatus = (String) statusFilter.getSelectedItem();  // Use getSelectedItem for single-selection
                // Filter based on both selected statuses and selected classes
                filterTasksAndClasses(new ArrayList<>(List.of(selectedStatus)), new ArrayList<>(selectedClasses));
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
                            if (className.equals(taskLegend[1])) { // Compare the selected class with the task's legend
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
