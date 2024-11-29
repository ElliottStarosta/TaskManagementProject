package org.example.GUI.Components.StickyNote;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.example.GUI.Dashboard;
import org.example.GUI.FormsManager;
import org.example.WritingUtil;
import org.example.tasks.PriorityTask;
import org.example.tasks.TaskManagerExc;
import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EditableNote extends JPanel {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> subjectComboBox;
    private JComboBox<String> priorityComboBox;
    private JFormattedTextField editor;
    private LocalDate[] dateRange;
    private final JFrame frame;
    private final PriorityTask task;
    private int priorityValue;

    public EditableNote(JFrame frame, PriorityTask task) {
        this.frame = frame;
        this.task = task;
        this.priorityValue = task.getPriority();

        init();
    }

    private void init() {
        setLayout(new MigLayout("fill, insets 20", "[grow, center]", "[center]"));
        setPreferredSize(new Dimension(800, 600));

        // Title Section
        titleField = new JTextField(10);
        titleField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "New Task");
        titleField.putClientProperty(FlatClientProperties.STYLE, "font: bold +25; foreground: darken(@white,20%)");
        titleField.setBackground(new Color(0, 0, 0, 0));
        titleField.setPreferredSize(new Dimension(titleField.getPreferredSize().width, 40));
        titleField.setBorder(BorderFactory.createEmptyBorder());
        titleField.setText(task.getName());

        // Description Section
        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");

        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.putClientProperty(FlatClientProperties.STYLE, "font: bold +3");
        descriptionArea.setMargin(new Insets(10, 10, 0, 10));
        descriptionArea.setText(task.getDescription());

        // Date Picker Section
        JLabel dateLabel = new JLabel("Date");
        dateLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");

        DatePicker datePicker = new DatePicker();
        datePicker.setDateSelectionMode(DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setSeparator(" to ");
        datePicker.setUsePanelOption(true);
        datePicker.setEditor(editor);
        datePicker.setDateSelectionAble(localDate -> !localDate.isBefore(LocalDate.now()));
        datePicker.addDateSelectionListener(dateEvent -> dateRange = datePicker.getSelectedDateRange());

        if (task.getDueDateRange().length == 2) {
            datePicker.setSelectedDateRange(task.getDueDateRange()[0], task.getDueDateRange()[1]);
        } else {
            datePicker.setSelectedDateRange(task.getDueDateRange()[0], task.getDueDateRange()[0]);
        }

        // Subject Section
        JLabel subjectLabel = new JLabel("Subject");
        subjectLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");

        subjectComboBox = new JComboBox<>(TaskManagerExc.getSubjectFilters().toArray(new String[0]));
        subjectComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        subjectComboBox.setSelectedItem(task.getLegend()[1]);
        subjectComboBox.putClientProperty(FlatClientProperties.STYLE,
                "font: medium;" +
                        "background: lighten(@background, 10%)");

        // Priority Section
        JLabel priorityLabel = new JLabel("Priority");
        priorityLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");

        priorityComboBox = new JComboBox<>(new String[]{"Normal", "Urgent", "Distant", "None"});
        priorityComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        priorityComboBox.setSelectedItem(getPriorityValue());
        priorityComboBox.putClientProperty(FlatClientProperties.STYLE,
                "font: medium;" +
                        "background: lighten(@background, 10%)");


        // Action listener to get priority value
        priorityComboBox.addActionListener(e -> {
            String selected = (String) priorityComboBox.getSelectedItem();

            switch (selected) {
                case "Urgent":
                    priorityValue = 1;
                    break;
                case "Normal":
                    priorityValue = 2;
                    break;
                case "Distant":
                    priorityValue = 3;
                    break;
                default:
                    priorityValue = 4;
            }
            System.out.println("Selected priority value: " + priorityValue); // For debugging
        });


        // Buttons Section
        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(100, 30));  // Increased height
        closeButton.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; background: lighten(@background, 20%)");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> FormsManager.getInstance().showForm(new Dashboard(frame)));

        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(100, 30));  // Increased height
        saveButton.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; background: lighten(@background, 20%)");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> handleSave());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        buttonPanel.add(saveButton);

        // Add Components to Panel
        add(titleField, "wrap, growx");
        add(new JSeparator(), "wrap, growx");
        add(descriptionLabel, "wrap, growx, gapy 0");
        add(new JScrollPane(descriptionArea), "wrap, growx, height 150px, gapy 10");
        add(new JSeparator(), "wrap, growx");
        add(dateLabel, "wrap, growx, gapy 0");
        add(datePicker, "wrap, growx, gapy 0");
        add(new JSeparator(), "wrap, growx");
        add(subjectLabel, "wrap, growx, gapy 0");
        add(subjectComboBox, "wrap, width 20%, align left");
        add(priorityLabel, "wrap, growx, gapy 10");
        add(priorityComboBox, "wrap, width 20%, align left");
        add(buttonPanel, "dock south");
    }


    private String getPriorityValue() {
        switch (priorityValue) {
            case 1:
                return "Urgent";
            case 2:
                return "Normal";
            case 3:
                return "Distant";
            case 4:
                return "None";
        }
        return "";
    }


    private void handleSave() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String subject = (String) subjectComboBox.getSelectedItem();


        if (title.length() > 20) {
            JOptionPane.showMessageDialog(this,"Please make the title shorter");
            return;
        }

        if (title.isEmpty() || description.isEmpty() || dateRange == null) {
            JOptionPane.showMessageDialog(this, "Please ensure all fields are filled correctly.");
            return;
        }

        task.setName(title);
        task.setDescription(description);
        task.setLegend(TaskManagerExc.getLegend().get(subject), subject);
        task.setDueDate(dateRange[0], dateRange[1]);
        task.setPriority(priorityValue);


        // If the task is marked as completed, ask the user if they are done with it
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you done with the task?",
                "Task Completion",
                JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) {

            task.setComplete(true);
            JOptionPane.showMessageDialog(this, "Task completed successfully");
        }

        // Write all tasks (including the possibly updated one) back to the JSON file
        WritingUtil.writeTasksToJSON();


        // Update the UI
        frame.validate();
        frame.repaint();
        FormsManager.getInstance().showForm(new Dashboard(frame));
    }
}
