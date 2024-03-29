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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String trueString = "42.9";
        if (trueString.matches(RegEx.VALUE.getRegex()))
        {
            System.out.println("Big time success value.");
        }

//      test printTable
        table.readTable("contactdetails");
        System.out.println(table.printTable());
        ArrayList<String> columnNamesString = new ArrayList<>();
        columnNamesString.add("Name");

        columnNamesString.add("Age");
//        System.out.println(table.selectTable("contactdetails", columnNamesString));
        System.out.println(table.printTable());

//      test regex matching
        trueString = "LIKE";
        if (trueString.matches(RegEx.OPERATOR.getRegex()))
        {
            System.out.println("Big time success operator.");
        }

        trueString = "name=='clive';";
        if (trueString.matches(RegEx.WHERESPLIT.getRegex()))
        {
            System.out.println("Big time success WHERESPLIT.");
        }
        String[] trueStringArray = trueString.split(RegEx.WHERESPLIT.getRegex());
        System.out.println("trueString1: "+trueStringArray[0]);
        System.out.println("trueString2: "+trueStringArray[1]);
        System.out.println("trueString3: "+trueStringArray[2]);
        System.out.println();

        Pattern p = Pattern.compile(RegEx.WHERESPLIT.getRegex());
        Matcher m = p.matcher("name<='clive';");
        if (m.find())
        {
            System.out.println("pattern success");
        }

        String set = "UPDATE actors SET age = 45 WHERE name=='Hugh Grant';";
//        String[] splitSet = set.split("((?<==)|(?==))");
        String[] splitSet = set.split("([^\\s\"'()]+|\"([^\"]*)\"|'([^']*)'|\\(([^(]*)\\))|\\s");
        for (int i = 0; i < splitSet.length; i++)
        {
            System.out.println(splitSet[i].trim());
        }

        System.out.println("array print test");
        ArrayList<String> test = new ArrayList<>();
        test.add("test1");
        test.add("test2");
        test.add("test3");
        System.out.println(test);
        test.remove(0);
        System.out.println(test);
        test.remove(0);
        System.out.println(test);

        System.out.println();
        System.out.println("Start here");
//        Tokenizer tok = new Tokenizer("SELECT ( name, age ) FROM marks WHERE name>='Hugh Grant';");
        Tokenizer tok = new Tokenizer("SELECT * FROM actors WHERE name LIKE 'an';");
//        Tokenizer tok = new Tokenizer("SELECT * FROM actors WHERE (awards > 5) AND ((nationality == 'British') OR (nationality == 'Australian'));");
        tok.tokenize();
        tok.scanForANDOR();
        System.out.println("end here");

        String s5 = "40";
        String[] splitNameValuePair = s5.split(RegEx.SPLITSET.getRegex());
        for (int i = 0; i < splitNameValuePair.length; i++) {
            System.out.println(splitNameValuePair.length);
            System.out.println(splitNameValuePair[i]);
        }

        String s1 = "awards > 5";
        String s2 = "AND";
        String s3 = "nationality=='British'";
        String[] s1Arr = s1.split(RegEx.WHERESPLIT.getRegex().trim());

        for (int i = 0; i < s1Arr.length; i++)
        {
            System.out.println(s1Arr[i]);
        }
        String[] s2Arr = s2.split(RegEx.WHERESPLIT.getRegex().trim());
        for (int i = 0; i < s2Arr.length; i++)
        {
            System.out.println(s2Arr[i]);
        }
        String[] s3Arr = s3.split(RegEx.WHERESPLIT.getRegex().trim());
        for (int i = 0; i < s3Arr.length; i++)
        {
            System.out.println(s3Arr[i]);
        }



        System.out.println();

        String query = "SELECT (name, age) FROM marks WHERE name == 'mark smith';";
//        var pattern = Pattern.compile("\\(.*\\)|'.*';|"+RegEx.WHERESPLIT2.getRegex()+"|\\w+");
//        var matcher = pattern.matcher(query);
//        while (matcher.find())
        {
//            System.out.println(matcher.group());
        }

        Pattern pat = Pattern.compile("\\(.*?\\)|'.*?'|[^('\\s]+");
//        String s = "UPDATE marks SET mark = 38 WHERE name == 'Clive';";
//        String s = "SELECT * FROM marks WHERE pass == true;";
        String s = "SELECT ( name, age ) FROM marks WHERE name == 'Hugh Grant';";

//        if ((s == null) || ( s.length() ==0))
//        {
//            System.out.println("Error Success");
//        }
//        System.out.println();
//        System.out.println(s.substring(s.length()-1));
//        System.out.println();
//        if (s.substring(s.length() - 1).equals(";"))
//        {
//            System.out.println(s.substring(s.length()-2, s.length()-1));
//            if (!s.substring(s.length() - 2, s.length()-1).equals(";"))
//            {
//                System.out.println("; success");
//                s = s.substring(0, s.length()-1);
//                System.out.println(s);
//            }
//            else {
//                System.out.println("too many semi colons");
//            }
//        }
//
//        String[] r = pat.matcher(s).results().map(m3 -> m3.group()).toArray(String[]::new);
//        for (int i = 0; i < r.length; i++)
//        {
//            System.out.println(r[i]);
//        }

//        for (String e : r)
//            System.out.println(e);

        System.out.println();

//      test readColumnNames
        table = new Table("JobsDB");
        System.out.println(table.readColumnNames("contactdetails"));

//      Test tokensizer class with the Tokenize fn
//        String command = "CREATE TABLE Table1 (id, name, email, address);";
//        Tokenizer tokenizer = new Tokenizer(command);
//        tokenizer.tokenize();
//        System.out.println("\n"+tokenizer.getToken(3).getTokenString());
////      Test tokensizer class with the TokenizeBrackets fn
//        tokenizer.tokenizeBrackets(tokenizer.getToken(3).getTokenString());
    }
}
