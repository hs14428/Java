import OXOExceptions.*;

import static OXOExceptions.RowOrColumn.COLUMN;
import static OXOExceptions.RowOrColumn.ROW;

class OXOController
{
    public static int turnNum;
    OXOModel gameModel;
    RowOrColumn type;

    // Controller needs to be able to handle if board size changes mid game when checking moves and for wins etc
    // Change OXOGame.java to test game to test if diff board size, test if diff number of players etc

    public OXOController(OXOModel model)
    {
        gameModel = model;
        gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(0));
        turnNum = 0;
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        int asciiA = 97;
        int ascii1 = 49;
        int maxCols = 9;

        if ((gameModel.getWinner() == null) && (!gameModel.isGameDrawn())) {
//          Check that input string is only 2 long
            if (command.length() != 2) {
                throw new InvalidIdentifierLengthException(command.length());
            }

            int rowNum = (int) command.toLowerCase().charAt(0) - asciiA;
            int colNum = (int) command.charAt(1) - ascii1;

//          Check if within the max possible 9x9 first - if outside, it will call: CellDoesNotExistException
            if (rowNum+1 > maxCols /*|| rowNum+1 < 1 */|| colNum+1 > maxCols /*|| colNum+1 < 1*/) {
                throw new CellDoesNotExistException(rowNum, colNum);
            }
//          Now check if selection is within the current row and column limits and call OutsideCellRangeException if not
            else if (rowNum+1 > gameModel.getNumberOfRows() || rowNum+1 == 0) {
                type = ROW;
                throw new OutsideCellRangeException(rowNum, colNum, type);
            }
            else if (colNum+1 > gameModel.getNumberOfColumns() || colNum+1 == 0) {
                type = COLUMN;
                throw new OutsideCellRangeException(rowNum, colNum, type);
            }
//          Check if the row identifier is valid or not
            else if (rowNum+1 < 0) {
                type = ROW;
                throw new InvalidIdentifierCharacterException(rowNum, colNum, type);
            }
//          Finally, check if the cell is taken, as by this point the selection must be valid to reach here
            else if (gameModel.getCellOwner(rowNum, colNum) != null) {
                throw new CellAlreadyTakenException(rowNum, colNum);
            }
            gameModel.setCellOwner(rowNum, colNum, gameModel.getCurrentPlayer());
            drawCheck();
            checkWin();
            changePlayer();
            if (turnNum == 6) {
                OXOModel gameModel = new OXOModel(3,3,3);
//                gameModel.addPlayer(new OXOPlayer('Y'));
                gameModel.setWinThreshold(3);
            }
        }
    }

    public void changePlayer()
    {
        turnNum++;
        int playerNum = turnNum%gameModel.getNumberOfPlayers();
        gameModel.setCurrentPlayer(gameModel.getPlayerByNumber(playerNum));
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

//  Check if a row is a win based of winThreshold
    public boolean checkRow(int rowNumber)
    {
        int cnt = 1;
        for (int c = 1; c < gameModel.getNumberOfColumns(); c++) {
            if (gameModel.getCellOwner(rowNumber, c) == null) {
                cnt = 0;
            }
            if (gameModel.getCellOwner(rowNumber, c) != gameModel.getCellOwner(rowNumber, c-1)) {
                cnt = 0;
            }
            cnt++;
            if (cnt == gameModel.getWinThreshold()) {
                return true;
            }
        }
        return false;
    }

//  Check if a col is a win based of winThreshold
    public boolean checkCol(int colNumber)
    {
        int cnt = 1;
        for (int r = 1; r < gameModel.getNumberOfRows(); r++) {
            if (gameModel.getCellOwner(r, colNumber) == null) {
                cnt = 0;

            }
            if (gameModel.getCellOwner(r, colNumber) != gameModel.getCellOwner(r-1, colNumber)) {
                cnt = 0;
            }
            cnt++;
            if (cnt == gameModel.getWinThreshold()) {
                return true;
            }
        }
        return false;
    }

// Check if there is a win via diagonal line. Check left to right
    public boolean checkDiagLR(int row, int col)
    {
        int cnt = 1;
        int c = col;
        for (int r = row; (r < gameModel.getNumberOfRows()) && (c < gameModel.getNumberOfColumns()); r++, c++) {
            if (gameModel.getCellOwner(r, c) == null) {
                return false;
            }
            if (gameModel.getCellOwner(r, c) != gameModel.getCellOwner(r-1, c-1)) {
                return false;
            }
            cnt++;
            if (cnt == gameModel.getWinThreshold()) {
                return true;
            }
        }
        return false;
    }

// Check if there is a win via diagonal line. Check right to left
    public boolean checkDiagRL(int row, int col)
    {
        int cnt = 1;
        int c = col;
        for (int r = row; (r < gameModel.getNumberOfRows()) && (c >= 0); r++, c--) {
            if (gameModel.getCellOwner(r, c) == null) {
                return false;
            }
            if (gameModel.getCellOwner(r, c) != gameModel.getCellOwner(r-1, c+1)) {
                return false;
            }
            cnt++;
            if (cnt == gameModel.getWinThreshold()) {
                return true;
            }
        }
        return false;
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
        for (int r = 1; r < gameModel.getNumberOfRows(); r++) {
            for (int c = 1; c < gameModel.getNumberOfColumns(); c++) {
                if (checkDiagLR(r, c)) {
                    gameModel.setWinner(gameModel.getCurrentPlayer());
                    return true;
                }
            }
        }
        for (int r = 1; r < gameModel.getNumberOfRows(); r++) {
            for (int c = gameModel.getNumberOfColumns() - 2; c >= 0; c--) {
                if (checkDiagRL(r, c)) {
                    gameModel.setWinner(gameModel.getCurrentPlayer());
                    return true;
                }
            }
        }
        return false;
    }
}
