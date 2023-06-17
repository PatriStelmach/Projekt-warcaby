package game;

public class Piece {
    private boolean isWhite;
    private boolean isKing;

    private boolean isPiece;


    public Piece(boolean king, boolean white, boolean Piece){
        this.isKing = king;
        this.isWhite = white;
        this.isPiece = Piece;
    }


    public boolean IsWhite(){
        return this.isWhite;
    }
    public boolean IsKing(){
        return this.isKing;
    }
    public boolean IsPiece(){
        return this.isPiece;
    }

    public void setKing(boolean king) {
        this.isKing = king;
    }

    public void setPiece(boolean piece) {
        this.isPiece = piece;
    }

    public void setWhite(boolean white) {
        this.isWhite = white;
    }
}
