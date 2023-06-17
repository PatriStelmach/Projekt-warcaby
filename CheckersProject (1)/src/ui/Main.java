package ui;

import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        //Set the look and feel to the OS look and feel
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a window to display the checkers game
        Gui g = new Gui();
        g.setDefaultCloseOperation(g.EXIT_ON_CLOSE);
        g.setVisible(true);
    }

}