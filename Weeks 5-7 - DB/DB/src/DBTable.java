import java.util.ArrayList;

public class DBTable
{
    private ArrayList<TableRow> tableRows;
    private ArrayList<String> tableColumns;
    private int primaryKey;

    public DBTable() {
        tableRows = new ArrayList<>();
        tableColumns = new ArrayList<>();
        primaryKey = 0;
    }


}
