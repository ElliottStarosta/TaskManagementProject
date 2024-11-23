package org.example.GUI.Components;

import org.example.Task;

import javax.swing.*;

public interface Clickable {
    void makeDraggable(JPanel stickyNote);
    void doubleClick(JPanel stickyNote, Task task);
    void handleButtonEvent();
}
