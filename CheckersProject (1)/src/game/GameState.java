package game;

import static java.lang.Math.abs;

public class GameState {
    public static final int SIDE_LENGTH = 8;
    public static final int NO_SQUARES = SIDE_LENGTH * SIDE_LENGTH; // 8 x 8

    // state of the board
    private Piece[] state;
    private Piece[] newState;


    boolean blackTurn = true;

    private int kingLastMoveDirection = 0;

    private int blackScore = 12;
    private int whiteScore = 12;


    public GameState() {
        state = InitialState();

    }

    public Piece[] InitialState() {
        Piece[] st = new Piece[GameState.NO_SQUARES];
        for (int i = 0; i < st.length; i++) {
            int y = i / SIDE_LENGTH;
            int x = i % SIDE_LENGTH;
            // place on black squares only
            if ((x + y) % 2 == 1) {
                // First player pieces in first 3 rows
                if (y < 3) {
                    st[i] = new Piece(false,true,true);
                }
                // Second player pieces in last 3 rows
                else if (y > 4) {
                    st[i] = new Piece(false,false,true);
                }
                else{
                    st[i] = new Piece(false,false,false);
                }
            }
            else{
                st[i] = new Piece(false,false,false);
            }
        }
    return st;
    }

    public Piece[] getState() {
        return state;
    }

    public Piece getPiece(int i){
        return state[i];
    }



    public  MoveFeedback playerMove(int fromPos, int dx, int dy) {
        int toPos = fromPos + dx + GameState.SIDE_LENGTH * dy;
        //not on board
        if (toPos > state.length) {
            return MoveFeedback.NOT_ON_BOARD;
        }

        //check for move onto piece
        if (getPiece(toPos).IsPiece()) {
            return MoveFeedback.NO_FREE_SPACE;
        }


        // black player - starts bottom, moves upwards
        if (!getPiece(fromPos).IsWhite()) {

            if (!blackTurn) {

                return MoveFeedback.NOT_YOUR_TURN;
            }

            // move up OR down black king
            if (getPiece(fromPos).IsKing()) {
                //more than 1 square

                if(validMoveDown(fromPos, toPos)){
                    if(abs(fromPos - toPos) > 20) {
                        if (!kingVictim(fromPos, toPos, false)) { //
                            return MoveFeedback.UNKNOWN_INVALID;
                        }
                    }
                    if(validJumpDown(fromPos,toPos)){
                        makeJump(fromPos, toPos);
                        this.blackTurn = false;
                        return MoveFeedback.SUCCESS;
                    }


                }
                else if(validMoveUp(fromPos, toPos)){
                    if(abs(fromPos - toPos) > 18) {
                        if (!kingVictim(fromPos, toPos, true)) { //
                            return MoveFeedback.UNKNOWN_INVALID;
                        }
                    }
                    if(validJumpUp(fromPos, toPos)){
                        makeJump(fromPos, toPos);
                        this.blackTurn = false;
                        return MoveFeedback.SUCCESS;
                    }

                }
                makeMove(fromPos, toPos);
                this.blackTurn = false;
                return MoveFeedback.SUCCESS;



            }
            // move for black
            if (fromPos - toPos < 10) {

                if (validMoveUp(fromPos, toPos)) {
                    makeMove(fromPos, toPos);
                    this.blackTurn = false;
                    return MoveFeedback.SUCCESS;
                } else return MoveFeedback.UNKNOWN_INVALID;

            }
            //jump for black
            else {

                if (validJumpUp(fromPos, toPos)){
                    this.blackTurn = false;
                    makeJump(fromPos, toPos);
                    return MoveFeedback.SUCCESS;
                } else return MoveFeedback.UNKNOWN_INVALID;

            }

        }
        // white player - starts top, moves downwards
        if (getPiece(fromPos).IsWhite()) {
            if (blackTurn) {

                return MoveFeedback.NOT_YOUR_TURN;
            }

            // move up OR down white king
            if (getPiece(fromPos).IsKing()) {
                //more than 1 square
                if(abs(fromPos - toPos) > 10) {
                    if (validMoveDown(fromPos, toPos)) {
                        if (!kingVictim(fromPos, toPos, true)) { //
                            return MoveFeedback.UNKNOWN_INVALID;
                        }

                        makeMove(fromPos, toPos);
                        this.blackTurn = true;
                        return MoveFeedback.SUCCESS;
                    } else if (validMoveUp(fromPos, toPos)) {
                        if (!kingVictim(fromPos, toPos, true)) {
                            return MoveFeedback.UNKNOWN_INVALID;
                        }
                        makeMove(fromPos, toPos);
                        this.blackTurn = true;
                        return MoveFeedback.SUCCESS;
                    }
                }
                else{
                    return MoveFeedback.UNKNOWN_INVALID;
                }


            }
            //move for white
            if (toPos - fromPos < 10) {

                if (validMoveDown(fromPos, toPos)) {
                    makeMove(fromPos, toPos);
                    this.blackTurn = true;
                    return MoveFeedback.SUCCESS;
                } else return MoveFeedback.UNKNOWN_INVALID;

            }
            //jump for white
            else {

                if (validJumpDown(fromPos, toPos)) {
                    this.blackTurn = true;
                    makeJump(fromPos, toPos);
                    return MoveFeedback.SUCCESS;
                } else {
                    return MoveFeedback.UNKNOWN_INVALID;
                }

            }
        }
        return MoveFeedback.FORCED_JUMP;
    }

    //true if captured by king

    private boolean kingVictim(int fromP,int toP,boolean isWhite){

        Piece[] nState = this.state;
        int blackVictims = 0;
        int whiteVictims =0;


        if(validMoveDown(fromP, toP)){
            for (int i = fromP+this.kingLastMoveDirection ; i < toP  ; i+=this.kingLastMoveDirection){
                if(nState[i].IsPiece())
                {
                    if(nState[i].IsWhite() && !nState[fromP].IsWhite()){
                        whiteVictims++;
                    }
                    else if(!nState[i].IsWhite() && nState[fromP].IsWhite()){
                        blackVictims++;
                    }
                }

            }
        } else if (validMoveUp(fromP,toP)){
            for (int i = toP+this.kingLastMoveDirection ; i < fromP  ; i+=this.kingLastMoveDirection){
                if(nState[i].IsPiece())
                {

                }
            }
        }

        if(whiteVictims+blackVictims == 1)
        {
            // moved down
            if(validMoveDown(fromP, toP)){
                for (int i = fromP+this.kingLastMoveDirection ; i < toP  ; i+=this.kingLastMoveDirection){
                    if(nState[i].IsPiece())
                    {
                        nState[i].setPiece(false);
                    }

                }
            } else if (validMoveUp(fromP,toP)){
                for (int i = toP+this.kingLastMoveDirection ; i < fromP  ; i+=this.kingLastMoveDirection){
                    if(nState[i].IsPiece())
                    {
                        nState[i].setPiece(false);
                    }
                }
            }
        }

        if (whiteVictims+blackVictims == 1)  {
            if(blackVictims==1){
                updateState(nState);
                this.blackScore--;
                return true;
            }
            else if (whiteVictims == 1){
                updateState(nState);
                this.whiteScore--;
                return true;
            }

        }
        nState = this.state;
        updateState(nState);
        return false;

    }


    // check if move down is valid
    private boolean validMoveDown(int fromP, int toP) {
        int diff = toP - fromP;

        if(getPiece(fromP).IsKing() && diff > 20)
        {
            //left border
            if (fromP % 8 == 0) {
                this.kingLastMoveDirection = 9;
                return (diff % 9) == 0;
            }

            //right-border
            if (fromP % 8 == 7) {
                this.kingLastMoveDirection = 7;
                return (diff % 7) == 0;
            }
            if ((diff %7) == 0){
                this.kingLastMoveDirection = 7;
                return true;
            }
            if ((diff % 9) == 0){
                this.kingLastMoveDirection = 9;
                return true;
            }
        }



        // the left-border
        if (fromP % 8 == 0) {
            return diff == 9;
        }

        //right-border
        if (fromP % 8 == 7) {
            return diff == 7;
        }



        return diff == 7 || diff == 9;
    }
    // check if move up is valid
    private boolean validMoveUp(int fromP, int toP) {
        int diff = fromP - toP;


        if(getPiece(fromP).IsKing() && diff > 20)
        {
            //left border
            if (fromP % 8 == 0) {
                this.kingLastMoveDirection = 7;
                return (diff % 7) == 0;
            }

            //right-border
            if (fromP % 8 == 7) {
                this.kingLastMoveDirection = 9;
                return (diff % 9) == 0;
            }
            if ((diff %7) == 0){
                this.kingLastMoveDirection = 7;
                return true;
            }
            if ((diff % 9) == 0){
                this.kingLastMoveDirection = 9;
                return true;
            }

        }




        //left border
        if (fromP % 8 == 0) {
            return diff == 7;
        }

        //right border
        if (fromP % 8 == 7) {
            return diff == 9;
        }

        return diff == 7 || diff == 9;
    }

    // checks if jump up is valid
    private boolean validJumpUp(int fromP, int toP) {
        int diff = fromP - toP;
        int midPiece = (fromP + toP) / 2;

        if (getPiece(midPiece).IsWhite() == getPiece(fromP).IsWhite() || getPiece(midPiece).IsPiece() != getPiece(fromP).IsPiece()) {
            return false;
        }

        // left border
        if (fromP % 8 == 0 || fromP % 8 == 1) {
            return diff == 14;
        }

        // right border
        if (fromP % 8 == 7 || fromP % 8 == 6) {
            return diff == 18;
        }

        return diff == 14 || diff == 18;

    }

    // checks if jump down is valid
        private boolean validJumpDown(int fromP, int toP) {
            int diff = toP - fromP;
            int midPiece = (fromP + toP) / 2;

            if (getPiece(midPiece).IsWhite() == getPiece(fromP).IsWhite() || getPiece(midPiece).IsPiece() != getPiece(fromP).IsPiece()) {
                return false;
            }

            // left border
            if (fromP % 8 == 0  || fromP % 8 == 1) {
                return diff == 18;
            }

            // right border
            if (fromP % 8 == 7  || fromP % 8 == 6) {
                return diff == 14;
            }

            return diff == 14 ||  diff == 18;
        }

    private void updateState(Piece[] State){
        this.state= State;
    }
    //move piece(no jumps)
    private void makeMove(int fromP, int toP)
    {
        this.newState =  this.getState();

        //black
        if(!getPiece(fromP).IsWhite()){

            this.newState[fromP].setPiece(false);
            this.newState[toP].setPiece(true);
            this.newState[toP].setWhite(getPiece(fromP).IsWhite());
            this.newState[toP].setKing(getPiece(fromP).IsKing());
            // check if and make king
            if (toP < 8){
                this.newState[toP].setKing(true);
            }

            updateState(newState);


        }
        //white
        else if (getPiece(fromP).IsWhite()){

            this.newState[fromP].setPiece(false);
            this.newState[toP].setPiece(true);
            this.newState[toP].setWhite(getPiece(fromP).IsWhite());
            this.newState[toP].setKing(getPiece(fromP).IsKing());
            // check if and make king
            if (toP > 55){
                this.newState[toP].setKing(true);
            }

            updateState(newState);

        }
    }
    private void makeJump(int fromP,int toP)
    {
        int midP = (fromP + toP)/2;
        this.newState =  this.getState();

        //black
        if(!getPiece(fromP).IsWhite()){

            this.newState[fromP].setPiece(false);
            this.newState[toP].setPiece(true);

            this.newState[toP].setWhite(getPiece(fromP).IsWhite());
            if(this.newState[midP].IsWhite())
            {
                this.blackScore--;
            }
            else{
                this.whiteScore--;
            }

            this.newState[midP].setPiece(false);
            this.newState[midP].setKing(false);
            this.newState[toP].setKing(getPiece(fromP).IsKing());
            // check if and make king
            if (toP < 8){
                this.newState[toP].setKing(true);
            }

            updateState(newState);



        }
        //white
        else {

            this.newState[fromP].setPiece(false);
            this.newState[toP].setPiece(true);

            this.newState[toP].setWhite(getPiece(fromP).IsWhite());
            if(this.newState[midP].IsWhite())
            {
                this.blackScore--;
            }
            else{
                this.whiteScore--;
            }

            this.newState[midP].setPiece(false);
            this.newState[midP].setKing(false);
            this.newState[toP].setKing(getPiece(fromP).IsKing());
            // check if and make king
            if (toP > 55){
                this.newState[toP].setKing(true);
            }

            updateState(newState);

        }
    }
    public String checkForWin(){

        if (this.blackScore==0){
            return "Biale Wygraly";
        }
        if(this.whiteScore==0) {
            return "Czarne Wygraly";
        }
        return "";

    }



}
