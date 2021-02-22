import java.util.*;

class OXOModel
{
    private ArrayList<ArrayList<OXOPlayer>> cells;
//    private OXOPlayer cells[][];
    private OXOPlayer players[];
    private OXOPlayer currentPlayer;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh)
    {
        winThreshold = winThresh;
        cells = new ArrayList<ArrayList<OXOPlayer>>(numberOfRows);
        for (int i = 0; i < numberOfRows; i++) {
            cells.add(new ArrayList<OXOPlayer>(numberOfColumns));
            for (int j = 0; j < numberOfColumns; j++) {
                cells.get(i).add(null);
            }
        }
        //        cells = new OXOPlayer[numberOfRows][numberOfColumns];
        players = new OXOPlayer[2];
    }

    public int getNumberOfPlayers()
    {
        return players.length;
    }

    public void addPlayer(OXOPlayer player)
    {
        for(int i=0; i<players.length ;i++) {
            if(players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }

    public OXOPlayer getPlayerByNumber(int number)
    {
        return players[number];
    }

    public OXOPlayer getWinner()
    {
        return winner;
    }

    public void setWinner(OXOPlayer player)
    {
        winner = player;
    }

    public OXOPlayer getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer(OXOPlayer player)
    {
        currentPlayer = player;
    }

    public int getNumberOfRows()
    {
        return cells.size();
//        return cells.length;
    }

    public int getNumberOfColumns()
    {
        return cells.get(0).size();
//        return cells[0].length;
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber)
    {
        return cells.get(rowNumber).get(colNumber);
//        return cells[rowNumber][colNumber];
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player)
    {
        cells.get(rowNumber).set(colNumber, player);
//        cells[rowNumber][colNumber] = player;
    }

    public void setWinThreshold(int winThresh)
    {
        winThreshold = winThresh;
    }

    public int getWinThreshold()
    {
        return winThreshold;
    }

    public void setGameDrawn()
    {
        gameDrawn = true;
    }

    public boolean isGameDrawn()
    {
        return gameDrawn;
    }
}
