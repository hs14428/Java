package Database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Input.Token;
import Input.Tokenizer;

public class DBTesting {

    public static void main(String[] args) {
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
        Table table = new Table("contact-details", "JobsDB");
        ArrayList<List<String>> tableArrayList = table.readTable();
        for (List<String> strings : tableArrayList) {
            for (String string : strings) {
                System.out.print(string + "\t");
            }
            System.out.println();
        }

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
        table.createTable("writeToTableTest");
        table.writeToTable("writeToTableTest");

//      Test tokensizer class with the Tokenize fn
        Tokenizer tokenizer = new Tokenizer();
        tokenizer.tokenize("CREATE TABLE Table1 (id, name, email, address)");
        System.out.println("\n"+tokenizer.getToken(3).getTokenString());
//      Test tokensizer class with the TokenizeBrackets fn
        tokenizer.tokenizeBrackets(tokenizer.getToken(3).getTokenString());


        Token tokenType = new Token("use");
    }
}
