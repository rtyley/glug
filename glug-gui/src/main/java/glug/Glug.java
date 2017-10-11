package glug;

import glug.gui.MainFrame;

import javax.swing.*;

import static java.awt.EventQueue.invokeLater;

public class Glug {

    public static void main(String args[]) {
        UIManager.put("Slider.paintValue", Boolean.FALSE);
        invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
