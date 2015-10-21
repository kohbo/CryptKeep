
package cen_gui;

import javax.swing.JFrame;

public class CEN_GUI {

    public static void main(String[] args) {
        BUILDER builder = new BUILDER();
        builder.pack();
        builder.setLocationRelativeTo(null);
        builder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        builder.setVisible(true);
        //steve smells
    }
}
