import java.io.*;

public class DBController {

    public static void main(String[] args) {
	// write your code here
        FileActions createFile = new FileActions();
        createFile.FileCreatorTest();
        createFile.FileWriter();
        createFile.FileReader();
        createFile.setFileToOpen(createFile.contactDetails);
        createFile.FileReader();


        //Playing around with Java.io API
        File directory = new File(createFile.directory);
        System.out.println(createFile.directory);
        File[] listOfFiles = directory.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            }
            else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
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
                System.out.println("Directory " + listOfFiles2[i].getParent());
                System.out.println("Directory " + listOfFiles2[i].getPath());
            }
        }

    }
}
