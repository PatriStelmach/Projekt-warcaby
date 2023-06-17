package ui;

import javax.swing.*;

import game.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class CheckerButton extends JButton {
    private final int position;
    private final Piece piece;
    private GameState gameState;

    // drag drop
    int X;
    int Y;
    int screenX = 0;
    int screenY = 0;


    private final int  checkerWidth = 60;
    private final int checkerHeight = 60;


    public CheckerButton(int position, Piece piece, Gui gui){
        super();
        this.position = position;
        this.piece = piece;
        this.gameState = gameState;
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setContentAreaFilled(false);
        setIcon(piece);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                screenX = mouseEvent.getXOnScreen();
                screenY = mouseEvent.getYOnScreen();
                X = getX();
                Y = getY();
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent){
                int deltaX = mouseEvent.getXOnScreen() - screenX;
                int deltaY = mouseEvent.getYOnScreen() - screenY;
                int dx = (int) Math.round((double)deltaX / (double) Square.squareSize);
                int dy = (int) Math.round((double)deltaY / (double) Square.squareSize);
                gui.onMouseRelease(position, dx, dy);
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                int deltaX = mouseEvent.getXOnScreen() - screenX;
                int deltaY = mouseEvent.getYOnScreen() - screenY;
                setLocation(X + deltaX, Y + deltaY);
            }
        });

    }

    private void setIcon(Piece piece){
        BufferedImage buttonIcon = null;
        try {
            if (!piece.IsWhite()) {
                if (piece.IsKing()) {
                    buttonIcon = ImageIO.read(new File("images/bking.png"));
                } else {
                    buttonIcon = ImageIO.read(new File("images/bpiece.png"));
                }
            }
            else {
                if (piece.IsKing()) {
                    buttonIcon = ImageIO.read(new File("images/wking.png"));
                }
                else {
                    buttonIcon = ImageIO.read(new File("images/wpiece.png"));
                }
            }
        }
        catch(IOException e){
            System.out.println(e.toString());
        }

        if (buttonIcon != null){
            Image resized = buttonIcon.getScaledInstance(checkerWidth,checkerHeight,100);
            ImageIcon icon = new ImageIcon(resized);
            this.setIcon(icon);
        }
    }



}
