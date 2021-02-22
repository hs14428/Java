import OXOExceptions.*;

class OXOController
{
    private final int asciiA = 97;
    private final int ascii1 = 49;
    OXOModel gameModel;
    private String message = "";


    public OXOController(OXOModel model)
    {
        gameModel = model;
        gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(0));
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        if ((gameModel.getWinner() == null) && (!gameModel.isGameDrawn())) {

            int rowNum = (int) command.toLowerCase().charAt(0) - asciiA;
            int colNum = (int) command.charAt(1) - ascii1;
            if (validMove(rowNum, colNum)) {
                gameModel.setCellOwner(rowNum, colNum, gameModel.getCurrentPlayer());
                drawCheck();
                checkWin();
                changePlayer();
            }
            else {
                // Needs work
                message = "Invalid move, please try again";
            }

        }

    }

    public void changePlayer()
    {
        if (gameModel.getCurrentPlayer() == gameModel.getPlayerByNumber(0)) {
            gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(1));
        }
        else {
            gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(0));
        }
    }

//  Can improve to check if draw happens before board is full?
    public boolean drawCheck()
    {
        for (int r = 0; r < gameModel.getNumberOfRows(); r++) {
            for (int c = 0; c < gameModel.getNumberOfColumns(); c++) {
                if (gameModel.getCellOwner(r, c) == null) {
                    return false;
                }
            }
        }
        gameModel.setGameDrawn();
        return true;
    }

//  Check if a row is three of a kind
    public boolean checkRow(int rowNumber)
    {
        for (int c = 1; c < gameModel.getNumberOfColumns(); c++) {
            if (gameModel.getCellOwner(rowNumber, c) == null) {
                return false;
            }
            if (gameModel.getCellOwner(rowNumber, c) != gameModel.getCellOwner(rowNumber, c-1)) {
                return false;
            }
        }
        return true;
    }

//  Check if a col is three of a kind
    public boolean checkCol(int colNumber)
    {
        for (int r = 1; r < gameModel.getNumberOfRows(); r++) {
            if (gameModel.getCellOwner(r, colNumber) == null) {
                return false;
            }
            if (gameModel.getCellOwner(r, colNumber) != gameModel.getCellOwner(r-1, colNumber)) {
                return false;
            }
        }
        return true;
    }

// Check if there is a win via diagonal line. Check top left start and top right start
    public boolean checkDiag()
    {
        int c = 1;
        for (int r = 1; (r < gameModel.getNumberOfRows()) && (c < gameModel.getNumberOfColumns()); r++, c++) {
            if (gameModel.getCellOwner(r, c) == null) {
                return false;
            }
            if (gameModel.getCellOwner(r, c) != gameModel.getCellOwner(r-1, c-1)) {
                // Check right to left diagonal
                c = gameModel.getNumberOfColumns()-1;
                for (r = 0; (r < gameModel.getNumberOfRows()) && (c >= 0); r++, c--) {
                    if (gameModel.getCellOwner(r, c) == null) {
                        return false;
                    }
                    // Erroring here when enter certain cell references - doesn't like right to left diagonal - try separating them out, one for L->R and one R->L
                    if (gameModel.getCellOwner(r, c) != gameModel.getCellOwner(r+1, c-1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkWin()
    {
        for (int r = 0; r < gameModel.getNumberOfRows(); r++) {
            if (checkRow(r)) {
                gameModel.setWinner(gameModel.getCurrentPlayer());
                return true;
            }
        }
        for (int c = 0; c < gameModel.getNumberOfColumns(); c++) {
            if (checkCol(c)) {
                gameModel.setWinner(gameModel.getCurrentPlayer());
                return true;
            }
        }
//        if (checkDiag()) {
//            gameModel.setWinner(gameModel.getCurrentPlayer());
//            return true;
//        }
        return false;
    }

//  Check if position is inbounds
    public boolean inbounds(int rowNumber, int colNumber)
    {
        return rowNumber >= 0 && rowNumber < gameModel.getNumberOfRows()
                && colNumber >= 0 && colNumber < gameModel.getNumberOfColumns();
    }

    public boolean validMove(int rowNumber, int colNumber)
    {
        if (gameModel.getCellOwner(rowNumber, colNumber) == null) {
            return true;
        }
        else {
            return false;
        }
    }
}
