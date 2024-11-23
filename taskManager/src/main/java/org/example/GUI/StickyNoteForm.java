package org.example.GUI;

import com.formdev.flatlaf.FlatClientProperties;
import org.example.GUI.Components.Clickable;
import org.example.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;

public class StickyNoteForm extends JPanel {

    private final JFrame frame;

    private void init() {
        JPanel notePanel = (JPanel) stickyNote();
        add(notePanel);
    }

    public StickyNoteForm(JFrame frame) {
        init();
        this.frame = frame;
    }

    public Component stickyNote() {
        // Main panel for the sticky note
        JPanel stickyNotePanel = new JPanel();
        stickyNotePanel.setLayout(null); // Absolute positioning

        // Create the label and set its text
        JLabel label = new JLabel("StickyNote more Info");

        // Set the bounds for the label (x, y, width, height)
        label.setBounds(10, 10, 200, 20);

        // Add the label to the sticky note panel
        stickyNotePanel.add(label);

        // Set a preferred size for the sticky note panel
        stickyNotePanel.setPreferredSize(new Dimension(260, 150));

        return stickyNotePanel;
    }
}

