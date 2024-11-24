package org.example.GUI.Components;

import org.example.PriorityTask;
import org.example.Task;

import javax.swing.*;

public interface Clickable {
    void makeDraggable(JPanel stickyNote);
    void doubleClick(JPanel stickyNote, PriorityTask task);
    void handleButtonEvent();
}
