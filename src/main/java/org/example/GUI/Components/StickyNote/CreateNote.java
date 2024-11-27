package org.example.GUI.Components.StickyNote;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import org.example.GUI.Dashboard;
import org.example.GUI.FormsManager;
import org.example.WritingUtil;
import org.example.tasks.PriorityTask;
import org.example.tasks.TaskManagerExc;
import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DatePicker;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class CreateNote extends JPanel {

    private JTextField nameField;
    private JTextArea descriptionArea;
    private JComboBox<String> subjectPackageComboBox;
    private JComboBox<String> priorityComboBox;
    private JButton saveBtn;

    private JFormattedTextField editor;

    private int priorityValue;

    private LocalDate[] dateRange;

    private final JFrame frame;

    public CreateNote(JFrame frame) {
        init();
        this.frame = frame;
    }

    private void init() {
        // Set layout manager
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

        // Initialize components
        nameField = new JTextField(20);
        descriptionArea = new JTextArea(5, 20);
        subjectPackageComboBox = new JComboBox<>(TaskManagerExc.getSubjectFilters().toArray(new String[0]));
        subjectPackageComboBox.putClientProperty(FlatClientProperties.STYLE,
                "font: medium;" +
                        "background: lighten(@background, 10%)");
        priorityComboBox = new JComboBox<>(new String[]{"Normal", "Urgent", "Distant", "None"});
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
                case "Distant":
                    priorityValue = 3;
                    break;
                case "None":
                    priorityValue = 0;
                    break;
                default:
                    priorityValue = 2;
            }
            System.out.println("Selected priority value: " + priorityValue); // For debugging
        });

        subjectPackageComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        priorityComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
        editor.putClientProperty(FlatClientProperties.STYLE, "background:lighten(@background,5%);borderColor: @background");
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
        panel.putClientProperty(FlatClientProperties.STYLE, "background: darken(@background,3%); arc:10");

        // Adding components to the panel
        panel.add(nameField);
        panel.add(new JSeparator(), "gapy 10 10");

        panel.add(new JLabel("Description"), "gapy 10");
        panel.add(new JScrollPane(descriptionArea), "height 100px");


        panel.add(editor, "gapy 10");
        panel.add(new JSeparator(), "gapy 10 10");

        panel.add(new JLabel("Subject Package"), "gapy 10");
        panel.add(subjectPackageComboBox);

        panel.add(new JSeparator(), "gapy 10 10");
        panel.add(new JLabel("Priority"), "gapy 10");
        panel.add(priorityComboBox);
        panel.add(saveBtn, "gapy 20");

        add(panel);
    }

    // Method to handle saving the data
    private void handleSave() {
        String name = nameField.getText();
        String description = descriptionArea.getText();
        String subjectPackage = (String) subjectPackageComboBox.getSelectedItem();

        if (name.isEmpty() || description.isEmpty() || dateRange == null) {
            JOptionPane.showMessageDialog(this, "Please ensure all required fields are filled");
            return;
        }

        new PriorityTask(name, description, dateRange[0], dateRange[1], priorityValue, false, TaskManagerExc.getLegend().get(subjectPackage), subjectPackage);


        WritingUtil.writeTasksToJSON();
        JOptionPane.showMessageDialog(this, "Course details saved successfully");
        frame.validate();
        frame.repaint();
        FormsManager.getInstance().showForm(new Dashboard(frame));
    }
}