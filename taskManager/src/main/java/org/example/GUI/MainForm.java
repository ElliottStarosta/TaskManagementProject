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

public class MainForm extends JPanel implements Clickable {

    private final JFrame frame;
    Point startPt;

    private void init() {
        Task t = new Task("Science Project", "Prepare presentation", LocalDate.of(2024, 1, 7), LocalDate.of(2024,1,2), false, "Green","Math");
        JPanel notePanel = (JPanel) stickyNote(t);
        makeDraggable(notePanel);
        add(notePanel);



    }

    public MainForm(JFrame frame) {
        init();
        this.frame = frame;
    }

    public Component stickyNote(Task task) {
        // Main panel for the sticky note
        JPanel stickyNotePanel = new JPanel();

        stickyNotePanel.setLayout(null); // Absolute positioning

        // Measure the due date width dynamically
        String dueDateText = task.getFormattedDueDate();
        Font dueDateFont = new Font("Arial", Font.PLAIN, 12);
        FontMetrics metrics = stickyNotePanel.getFontMetrics(dueDateFont);
        int dueDateWidth = metrics.stringWidth(dueDateText);

        // Set the preferred width dynamically, ensuring it's large enough for the due date

        // Check if task has two dates (start and end date)
        LocalDate[] dueDateRange = task.getDueDateRange(); // Get the date range
        boolean hasTwoDates = dueDateRange != null && dueDateRange.length == 2 && dueDateRange[0] != null && dueDateRange[1] != null;

        int width = Math.max(260, (hasTwoDates ? 195 : 0) + dueDateWidth);

        stickyNotePanel.setPreferredSize(new Dimension(width, 150));

        stickyNotePanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" + "[dark]background:darken(@stickyNote,5%)");

        // Name Label (Top Left)
        JLabel nameLabel = new JLabel(task.getName());
        nameLabel.setBounds(13, 0, 200, 50);
        nameLabel.putClientProperty(FlatClientProperties.STYLE,
                "font:bold +7;" + "[dark]foreground:darken(@background,5%)");
        stickyNotePanel.add(nameLabel);

        // Description Label (Below Name)
        JLabel descriptionLabel = new JLabel(task.getDescription());
        descriptionLabel.setBounds(13, 40, 280, 40); // Allow room for multi-line text
        descriptionLabel.putClientProperty(FlatClientProperties.STYLE,
                "font: $small.font;" + "[dark]foreground:lighten(@background,25%)");
        stickyNotePanel.add(descriptionLabel);

        // Due Date Label (Top Right)
        JLabel dueDateLabel = new JLabel(dueDateText);
        dueDateLabel.setFont(dueDateFont);

        dueDateLabel.putClientProperty(FlatClientProperties.STYLE,
                "font: $medium.font;" + "[dark]foreground:darken(@background,5%)");

        dueDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dueDateLabel.setBounds(180, 0, dueDateWidth, 50); // Adjust width based on due date length
        stickyNotePanel.add(dueDateLabel);


        // Legend Panel (Bottom Left)
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.X_AXIS)); // Horizontal alignment
        legendPanel.putClientProperty(FlatClientProperties.STYLE,
                "arc:20;" + "[dark]background:darken(@stickyNote,5%)");
        legendPanel.setBounds(10, 110, 200, 40); // Adjusted width and height for spacing

        // Legend Dot
        JLabel legendDot = new JLabel("\u25CF"); // Unicode for a filled circle
        legendDot.setFont(new Font("Arial", Font.PLAIN, 30));
        legendDot.setAlignmentY(JLabel.CENTER_ALIGNMENT); // Center-align within the BoxLayout
        legendPanel.add(legendDot);

        // Spacing between the dot and text
        legendPanel.add(Box.createHorizontalStrut(10));
// Spacing between the dot and the text
        // Legend Text
        JLabel legendText = new JLabel(task.getLegend()[1]);
        legendText.putClientProperty(FlatClientProperties.STYLE,
                "font: bold;" + "[dark]foreground:darken(@background,15%)");
        legendText.setAlignmentY(JLabel.CENTER_ALIGNMENT); // Center-align within the BoxLayout
        legendPanel.add(legendText);

        try {
            // Attempt to convert the subject color string to a Color object
            Color color = (Color) Color.class.getField(task.getLegend()[0]).get(null);
            legendDot.setForeground(color); // Apply subject color to the legend dot
        } catch (Exception e) {
            // If the subjectColor is invalid or not found, set to a default color (e.g., gray)
            legendDot.setForeground(Color.GRAY);
        }


        legendPanel.add(legendText);

        stickyNotePanel.add(legendPanel);

        return stickyNotePanel;
    }

    @Override
    public void makeDraggable(JPanel stickyNote) {
        doubleClick(stickyNote);

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
                Point location = SwingUtilities.convertPoint(stickyNote,e.getPoint(),stickyNote.getParent());

                if(stickyNote.getParent().getBounds().contains(location)) {
                    Point newLocation = stickyNote.getLocation();
                    newLocation.translate(location.x - startPt.x,location.y - startPt.y);
                    newLocation.x = Math.max(newLocation.x,0);
                    newLocation.y = Math.max(newLocation.y,0);
                    newLocation.x = Math.min(newLocation.x,stickyNote.getParent().getWidth() - stickyNote.getWidth());
                    newLocation.y = Math.min(newLocation.y,stickyNote.getParent().getHeight() - stickyNote.getHeight());
                    stickyNote.setLocation(newLocation);
                    startPt = location;
                }

            }
        });
    }

    @Override
    public void doubleClick(JPanel stickyNote) {
        stickyNote.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    FormsManager.getInstance().showForm(new StickyNoteForm(frame));
                }
            }
        });
    }

}
