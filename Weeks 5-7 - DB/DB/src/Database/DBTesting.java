package Database;

import DBExceptions.DatabaseException;
import DBExceptions.InvalidQueryException;
import Input.Token;
import Input.Tokenizer;
import SQL.RegEx;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBTesting {

    public static void main(String[] args) throws InvalidQueryException, DatabaseException, IOException {
	// write your code here
        FileActions createFile = new FileActions();

//      Playing around with Java.io API
        File directory = new File(createFile.directory);
        System.out.println(createFile.directory);
        File[] listOfFiles = directory.listFiles();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
 //               System.out.println("File " + listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
 //               System.out.println("Directory " + listOfFile.getName());
            }
        }

        File directory2 = new File(".");
        System.out.println(directory2);
        File[] listOfFiles2 = directory2.listFiles();
        for (int i = 0; i < listOfFiles2.length; i++) {
            if (listOfFiles2[i].isFile()) {
                System.out.println("File " + listOfFiles2[i].getName());
            }
            else if (listOfFiles2[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles2[i].getName());
                System.out.println("Directory " + listOfFiles2[i].getPath());
            }
        }

//      Test the readTable method and check it stores each line of table in ArrayList<List<String>>
        System.out.println("\nreadTable tests:");
        Table table = new Table("JobsDB");
        ArrayList<ArrayList<String>> tableArrayList = table.readTable("contactdetails");
        for (List<String> strings : tableArrayList) {
            for (String string : strings) {
                System.out.print(string + "\t");
            }
            System.out.println();
        }
        System.out.println(tableArrayList.get(2).get(2));

//      Test getRecords method and check it does add the individual records to LinkedHashMap
        System.out.println("\naddRecords test:");
        table.addRecords();
        ArrayList<Record> records = table.getRecords();
        System.out.print("id\tName\tAge\tEmail");
        for (Record record : records) {
            System.out.print("\n"+record.getColumnData("id"));
            System.out.print("\t"+record.getColumnData("Name"));
            System.out.print("\t"+record.getColumnData("Age"));
            System.out.print("\t"+record.getColumnData("Email"));
        }

//      Test table creating and writing methods
        System.out.println("\n\ncreateTable and writeToTable tests:");
//        table.createTable("writeToTableTest");
//        table.writeToTable("writeToTableTest");
//      Test addColumns method in Table class
        ArrayList<Token> columnNames = new ArrayList<>();
        Token token1 = new Token("name");
        Token token2 = new Token("mark");
        Token token3 = new Token("pass");
        columnNames.add(token1);
        columnNames.add(token2);
        columnNames.add(token3);
//        table.addColumns("writeToTableTest", columnNames);
//      Test addRow method in Table class
        ArrayList<Token> rowValues = new ArrayList<>();
        Token token4 = new Token("'Steve'");
        Token token5 = new Token("55");
        Token token6 = new Token("true");
        rowValues.add(token4);
        rowValues.add(token5);
        rowValues.add(token6);
//        table.addRow("writeToTableTest", rowValues);

//      test regex matching
        String trueString = "'tony'";
        if (trueString.matches(RegEx.VALUE.getRegex()))
        {
            System.out.println("Big time success.");
        }

//      test printTable
        table.readTable("contactdetails");
        System.out.println(table.printTable("contactdetails"));
        ArrayList<String> columnNamesString = new ArrayList<>();
        columnNamesString.add("Name");
        columnNamesString.add("Age");
        System.out.println(table.printSome("contactdetails", columnNamesString));
        System.out.println(table.printTable("contactdetails"));

//      Test tokensizer class with the Tokenize fn
        String command = "CREATE TABLE Table1 (id, name, email, address);";
        Tokenizer tokenizer = new Tokenizer(command);
        tokenizer.tokenize();
        System.out.println("\n"+tokenizer.getToken(3).getTokenString());
//      Test tokensizer class with the TokenizeBrackets fn
        tokenizer.tokenizeBrackets(tokenizer.getToken(3).getTokenString());
    }
}
