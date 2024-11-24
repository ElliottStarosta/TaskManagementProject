package org.example.GUI;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.example.Task;
import org.example.TaskManagerExc;
import org.example.WritingUtil;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DatePicker;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class StickyNoteForm extends JPanel {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox isCompletedCheckBox;
    private JComboBox<String> subjectComboBox;
    private JComboBox<String> priorityComboBox;
    private JFormattedTextField editor;
    private LocalDate[] dateRange;
    private JFrame frame;
    private Task task;

    public StickyNoteForm(JFrame frame, Task task) {
        this.frame = frame;
        this.task = task;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill, insets 20", "[grow, center]", "[][][][][][][grow][]"));
        setPreferredSize(new Dimension(800, 600));

        // Title Section
        titleField = new JTextField(20);

        titleField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "New Task");
        titleField.putClientProperty(FlatClientProperties.STYLE, "font: bold +20");
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
        descriptionArea.putClientProperty(FlatClientProperties.STYLE,"font: bold +3");

        descriptionArea.setMargin(new Insets(10,10,0,10));
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
        datePicker.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(DateEvent dateEvent) {
                dateRange = datePicker.getSelectedDateRange();
            }
        });
        if (task.getDueDateRange().length == 2) {
            datePicker.setSelectedDateRange(task.getDueDateRange()[0],task.getDueDateRange()[1]);
        } else {
            datePicker.setSelectedDateRange(task.getDueDateRange()[0],task.getDueDateRange()[0]);

        }


        // Subject Section
        JLabel subjectLabel = new JLabel("Subject");
        subjectLabel.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");
        subjectComboBox = new JComboBox<>(TaskManagerExc.getSubjectFilters().toArray(new String[0]));
        priorityComboBox = new JComboBox<>(new String[]{"Normal","Urgent","Normal"});

        subjectComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        priorityComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Completed Checkbox
        isCompletedCheckBox = new JCheckBox("Completed");
        isCompletedCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        isCompletedCheckBox.setBackground(Color.WHITE);

        // Buttons Section
        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(100, 30));  // Increased the height of the button
        closeButton.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; background: lighten(@background, 20%)");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> FormsManager.getInstance().showForm(new Dashboard(frame )));

        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(100, 30));  // Increased the height of the button
        saveButton.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; background: lighten(@background, 20%)");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> handleSave());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        buttonPanel.add(saveButton);

        // Add Components to Panel
        add(titleField, "wrap, growx"); // Title field takes up the available space in the first column
        add(descriptionLabel, "wrap, growx, gapy 20");
        add(new JScrollPane(descriptionArea), "wrap, growx, height 100px, gapy 10");
        add(dateLabel, "wrap, growx, gapy 20");
        add(datePicker, "wrap, growx, gapy 20");
        add(subjectLabel, "wrap, growx, gapy 20");
        add(subjectComboBox, "wrap, width 20%, align left");
        add(buttonPanel, "dock south");
    }


    private void handlePrioritySelection(int priority) {
        JOptionPane.showMessageDialog(this, "Priority set to " + priority + " stars!");
    }

    private void handleSave() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        boolean isCompleted = isCompletedCheckBox.isSelected();
        String subject = (String) subjectComboBox.getSelectedItem();

        if (title.isEmpty() || description.isEmpty() || dateRange == null) {
            JOptionPane.showMessageDialog(this, "Please ensure all fields are filled correctly.");
            return;
        }

        task.setName(title);
        task.setDescription(description);
        task.setLegend("Blue", subject);
        task.setDueDate(dateRange[0], dateRange[1]);

        // If the task is marked as completed, ask the user if they are done with it
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you done with the task?",
                "Task Completion",
                JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) {
            // User chose "Yes", delete the task from the JSON file
//            WritingUtil.deleteTaskFromJSON(task);
            task.setComplete(true);
//            JOptionPane.showMessageDialog(this, "Task deleted successfully!");
            JOptionPane.showMessageDialog(this, "Task completed successfully");
        } else {
            // User chose "No", just write the task without deleting
//            JOptionPane.showMessageDialog(this, "Task saved without deletion.");
        }

        // Write all tasks (including the possibly updated one) back to the JSON file
        WritingUtil.writeTasksToJSON();


        // Update the UI
        frame.validate();
        frame.repaint();
        FormsManager.getInstance().showForm(new Dashboard(frame));
    }

}
