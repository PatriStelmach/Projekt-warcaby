package game;

public enum MoveFeedback {
    FORCED_JUMP ("You're forced to take."),
    NO_FREE_SPACE ("You can't move onto another piece."),
    NOT_ON_BOARD("NOT ON BOARD"),
    UNKNOWN_INVALID("Not a valid move."),
    SUCCESS ("Success"),
    NOT_YOUR_TURN("NOT YOUR TURN");

    private final String name;

    MoveFeedback(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }

}