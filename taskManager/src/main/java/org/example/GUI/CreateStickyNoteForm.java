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

public class CreateStickyNoteForm extends JPanel {

    private JTextField nameField;
    private JTextArea descriptionArea;
    private JCheckBox isCompletedCheckBox;
    private JComboBox<String> subjectPackageComboBox;
    private JComboBox<String> priorityComboBox;
    private JButton saveBtn;

    private JFormattedTextField editor;

    private LocalDate[] dateRange;

    private JFrame frame;

    public CreateStickyNoteForm(JFrame frame) {
        init();
        this.frame = frame;
    }

    private void init() {
        // Set layout manager
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

        // Initialize components
        nameField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        isCompletedCheckBox = new JCheckBox("Is Completed");
        subjectPackageComboBox = new JComboBox<>(TaskManagerExc.getSubjectFilters().toArray(new String[0]));
        priorityComboBox = new JComboBox<>(new String[]{"Normal","Urgent","Normal"});

        saveBtn = new JButton("Save");

        // Style for text area and text field
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Set placeholders for text fields
        nameField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "New Task");
        nameField.putClientProperty(FlatClientProperties.STYLE, "font: bold +10");
        nameField.setBackground(new Color(0, 0, 0, 0));
        nameField.setPreferredSize(new Dimension(nameField.getPreferredSize().width, 40));
        nameField.setBorder(BorderFactory.createEmptyBorder());
        descriptionArea.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Course Description");


        DatePicker datePicker = new DatePicker();
        datePicker.setDateSelectionMode(DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setSeparator(" to date ");
        datePicker.setUsePanelOption(true);

        editor = new JFormattedTextField();
        editor.putClientProperty(FlatClientProperties.STYLE,"background:lighten(@background,5%);borderColor: @background");
        datePicker.setEditor(editor);
        datePicker.setDateSelectionAble(localDate -> !localDate.isBefore(LocalDate.now()));


        datePicker.addDateSelectionListener(new DateSelectionListener() {
            @Override
            public void dateSelected(DateEvent dateEvent) {
                dateRange = datePicker.getSelectedDateRange();
            }
        });

        saveBtn.addActionListener(e -> handleSave());

        // Create panel with MigLayout
        JPanel panel = new JPanel(new MigLayout("wrap, fillx, insets 35 45 30 45", "[fill, 360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background: darken(@background,3%); arc:10"); // Sets a blue background

        // Adding components to the panel
        panel.add(nameField);
        panel.add(new JSeparator(), "gapy 10 10");

        panel.add(new JLabel("Description"), "gapy 10");
        panel.add(new JScrollPane(descriptionArea), "height 100px");


        panel.add(editor,"gapy 10");
        panel.add(new JSeparator(), "gapy 10 10");

        panel.add(isCompletedCheckBox);
        panel.add(new JLabel("Subject Package"), "gapy 10");
        panel.add(subjectPackageComboBox);
        panel.add(new JSeparator(), "gapy 10 10");
        panel.add(priorityComboBox);
        panel.add(saveBtn, "gapy 20");

        add(panel);
    }

    // Method to handle the Start Date selection
    private void pickStartDate() {
        // Code to open a date picker dialog
        JOptionPane.showMessageDialog(this, "Start Date Picker opened");
    }

    // Method to handle the End Date selection
    private void pickEndDate() {
        // Code to open a date picker dialog
        JOptionPane.showMessageDialog(this, "End Date Picker opened");
    }

    // Method to handle saving the data
    private void handleSave() {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        boolean isCompleted = isCompletedCheckBox.isSelected();
        String subjectPackage = (String) subjectPackageComboBox.getSelectedItem();

        if (name.isEmpty() || description.isEmpty() || dateRange == null) {
            JOptionPane.showMessageDialog(this, "Please ensure all required fields are filled");
            return;
        }

        if (dateRange.length == 2) {
            Task task = new Task(name,description,dateRange[0],dateRange[1],isCompleted,"blue",subjectPackage);
        } else {
            Task task = new Task(name,description,dateRange[0],isCompleted,"blue",subjectPackage);
        }

        WritingUtil.writeTasksToJSON();
        JOptionPane.showMessageDialog(this, "Course details saved successfully");
        frame.validate();
        frame.repaint();
        FormsManager.getInstance().showForm(new Dashboard(frame));
    }
}

