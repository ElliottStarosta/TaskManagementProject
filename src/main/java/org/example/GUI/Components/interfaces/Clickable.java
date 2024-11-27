package org.example.GUI.Components.interfaces;

import org.example.tasks.PriorityTask;

import javax.swing.*;

public interface Clickable {
    void makeDraggable(JPanel stickyNote);

    void doubleClick(JPanel stickyNote, PriorityTask task);

    void handleButtonEvent();
}
