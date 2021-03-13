import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DBController {

    public static void main(String[] args) {
	// write your code here
        FileActions createFile = new FileActions();
        createFile.FileCreatorTest();
        createFile.FileWriterTest();
        createFile.FileReaderTest();
        createFile.setFileToOpen(createFile.contactDetails);
        createFile.FileReaderTest();


        //Playing around with Java.io API
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

//        File directory2 = new File("C:" + File.separator + "Users" + File.separator + "Harry" + File.separator + "OneDrive" + File.separator + "Documents");
        File directory2 = new File(".");
        System.out.println("\n\n"+directory2);
        File[] listOfFiles2 = directory2.listFiles();

        for (int i = 0; i < listOfFiles2.length; i++) {
            if (listOfFiles2[i].isFile()) {
                System.out.println("File " + listOfFiles2[i].getName());
            }
            else if (listOfFiles2[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles2[i].getName());
//                System.out.println("Directory " + listOfFiles2[i].getParent());
                System.out.println("Directory " + listOfFiles2[i].getPath());
            }
        }

        FileActions testFile = new FileActions();
        String testPath = "C:"+File.separator+"Users"+File.separator+"Harry"+File.separator+"OneDrive"+File.separator+"Documents";
        System.out.println(testPath+"\\contact-details.tab");
        testFile.FileReader("contact-details.tab", testPath);

        testFile.setCurrentDirectory("C:"+File.separator+"Users"+File.separator+"Harry"+File.separator+"OneDrive"+File.separator+"Documents");
        System.out.println(testFile.getCurrentDirectory());

        System.out.println("\n\nNew testing from here\n\n");

        Table table = new Table("contact-details", "JobsDB");
        ArrayList<List<String>> tableArrayList = table.ReadTable();

        System.out.println(tableArrayList.size());
        System.out.println(tableArrayList.get(3)+"\n");
        for (List<String> strings : tableArrayList) {
            for (String string : strings) {
                System.out.print(string + "\t");
            }
            System.out.println();
        }
    }
}
