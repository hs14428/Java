package Database;

import DBExceptions.EmptyCommandException;
import Input.Token;
import Input.Tokenizer;

import java.util.ArrayList;

public class DBController
{
    private Database currentDatabase;

    public DBController()
    {
        currentDatabase = null;
    }

    public void processQuery (String command) throws EmptyCommandException
    {
        Tokenizer tokenizer = new Tokenizer(command);
        ArrayList<Token> tokenList = tokenizer.tokenize();
//        Parser parseCommand = new Parser(tokenList, command);

        Database testDB = new Database("JobsDB");
        testDB.listTables();
        testDB.addTable("new-table");
        Table testTable = new Table("contact-details", "JobsDB");
        testTable.printTable();
    }


}
