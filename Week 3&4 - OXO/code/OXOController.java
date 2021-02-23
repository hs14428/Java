import OXOExceptions.*;

import static OXOExceptions.RowOrColumn.COLUMN;
import static OXOExceptions.RowOrColumn.ROW;

class OXOController
{
    private final int asciiA = 97;
    private final int ascii1 = 49;
    private final int maxCols = 9;
    private final int maxRows = 9;
    OXOModel gameModel;
    private String message = "";
    RowOrColumn type;

    // Controller needs to be able to handle if board size changes mid game when checking moves and for wins etc
    // Change OXOGame.class to test game to test if diff board size, test if diff number of players etc
    // Check if game can handle new players mid way through (modify model/ call functions on model, so only need to change one class)
    // Write OXOGameTester.class
    // Extension of multiplayer is about 5%

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

            System.out.println(colNum);
            System.out.println(gameModel.getNumberOfColumns());
            System.out.println(rowNum);
            System.out.println(gameModel.getNumberOfRows() + "\n");

//          Check if within the max possible 9x9 first - if outside, it will call: CellDoesNotExistException
            if (rowNum+1 > maxCols || rowNum+1 < 1 || colNum+1 > maxCols || colNum+1 < 1) {
                throw new CellDoesNotExistException(rowNum, colNum);
            }
//          Now check if selection is within the current row and column limits and call OutsideCellRangeException if not
            else if (rowNum+1 > gameModel.getNumberOfRows()) {
                type = ROW;
                throw new OutsideCellRangeException(rowNum, colNum, type);
            }
            else if (colNum+1 > gameModel.getNumberOfColumns()) {
                type = COLUMN;
                throw new OutsideCellRangeException(rowNum, colNum, type);
            }
//          Finally, check if the cell is taken, as by this point the selection must be valid to reach here
            else if (gameModel.getCellOwner(rowNum, colNum) != null) {
                throw new CellAlreadyTakenException(rowNum, colNum);
            }
            gameModel.setCellOwner(rowNum, colNum, gameModel.getCurrentPlayer());
            drawCheck();
            checkWin();
            changePlayer();

//            if (validMove(rowNum, colNum-1)) {
//                gameModel.setCellOwner(rowNum, colNum-1, gameModel.getCurrentPlayer());
//                drawCheck();
//                checkWin();
//                changePlayer();
//            }
//            else {
//                // Needs work
//                message = "Invalid move, please try again";
//            }

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
