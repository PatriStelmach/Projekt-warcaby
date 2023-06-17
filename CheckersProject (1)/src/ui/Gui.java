package ui;

import game.GameState;
import game.*;

import javax.swing.*;
import java.awt.*;



public class Gui extends JFrame {

    private GameState gameState;

    private Square[] squares;
    private JPanel checkerboardPanel;
    private JPanel contentPane;
    private JTextArea textBox;



    public Gui() {
        GameStart();
    }

    private void GameStart(){
        gameState = new GameState();
        setup();
    }


    // Sets up initial GUI configuration.

    public void setup()
    {

        contentPane = new JPanel();
        checkerboardPanel = new JPanel(new GridBagLayout());
        JPanel textPanel = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        this.setContentPane(contentPane);
        checkerboardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        contentPane.add(checkerboardPanel);
        contentPane.add(textPanel);
        textBox = new JTextArea();
        textBox.setEditable(false);
        textBox.setLineWrap(false);
        textBox.setWrapStyleWord(true);
        textBox.setAutoscrolls(true);
        textPanel.add(textBox);

        updateCheckerBoard();
        this.pack();
        this.setVisible(true);

    }

    private void updateCheckerBoard(){
        checkerboardPanel.removeAll();
        addPieces();
        addSquares();
        checkerboardPanel.setVisible(true);
        checkerboardPanel.repaint();
        this.pack();
        this.setVisible(true);
    }

    private void addSquares(){
        squares = new Square[gameState.NO_SQUARES];
        int fromPos = -1;
        int toPos = -1;

        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < gameState.NO_SQUARES; i++){
            c.gridx = i % gameState.SIDE_LENGTH;
            c.gridy = i / gameState.SIDE_LENGTH;
            squares[i] = new Square(c.gridx, c.gridy);
            if (i == fromPos){
                squares[i].setHighlighted();
            }
            if(i == toPos){
                squares[i].setHighlighted();
            }
            checkerboardPanel.add(squares[i], c);
        }
    }



    //Add checker pieces to the GUI corresponding to the game state

    private void addPieces(){
        GridBagConstraints c = new GridBagConstraints();
        for (int i = 0; i < gameState.NO_SQUARES; i++){
            c.gridx = i % gameState.SIDE_LENGTH;
            c.gridy = i / gameState.SIDE_LENGTH;
            if(gameState.getPiece(i).IsPiece()){
                Piece piece = gameState.getPiece(i);
                CheckerButton button = new CheckerButton(i, piece, this);
                checkerboardPanel.add(button, c);
            }
        }
    }
    public void onMouseRelease(int position, int dx, int dy){

        MoveFeedback feedback = gameState.playerMove(position, dx, dy);
        if (feedback == MoveFeedback.SUCCESS){
            updateCheckerBoard();

        }
        else{
            updateCheckerBoard();
            System.out.println(feedback.toString());
        }
        textBox.append(gameState.checkForWin());



    }


}
