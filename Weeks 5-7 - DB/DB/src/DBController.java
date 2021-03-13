import java.util.Locale;

public class DBController {

    public void processQuery (String command)
    {
        command.toUpperCase();

        Database testDB = new Database("JobsDB");
        testDB.listTables();
        testDB.addTable("new-table");
        Table testTable = new Table("contact-details", "JobsDB");
        testTable.printTable();
    }


}
