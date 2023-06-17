package ui;

import javax.swing.*;
import java.awt.*;

public class Square extends JPanel {
    public static int squareSize = 80;

    private Color color;

    public Square(int i, int j) {
        this.setPreferredSize(new Dimension(squareSize, squareSize));
        if (((i % 2) + (j % 2)) % 2 == 0) {
            color = Color.WHITE;
        } else {
            color = Color.BLACK;
        }
    }

    public void setHighlighted() {
        color = Color.GRAY;
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

}