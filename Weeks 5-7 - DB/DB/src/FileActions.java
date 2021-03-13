import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Useful java.io methods:
// canRead()	    Boolean	    Tests whether the file is readable or not
// canWrite()	    Boolean	    Tests whether the file is writable or not
// createNewFile()	Boolean	    Creates an empty file
// delete()	        Boolean	    Deletes a file
// exists()	        Boolean	    Tests whether the file exists
// getName()	    String	    Returns the name of the file
// getAbsolutePath()String	    Returns the absolute pathname of the file
// length()	        Long	    Returns the size of the file in bytes
// list()	        String[]	Returns an array of the files in the directory
// mkdir()	        Boolean	    Creates a directory

class FileActions
{
    public String directory = "C:" + File.separator + "Users" + File.separator + "Harry" + File.separator + "Downloads";
    private String filename = "testfile.txt";
    private String contact = "contact-details.tab";
    private String filepath = directory + File.separator + filename;
    public String contactDetails = directory + File.separator + contact;
    private File fileToOpen;

    private String fileName;
    private String currentDirectory;// = ".";
    private String filePath;
    private File fileLocation;

    public void setCurrentDirectory(String currentDirectory)
    {
        this.currentDirectory = currentDirectory;
    }

    public String getCurrentDirectory()
    {
        return currentDirectory;
    }

    public FileActions()
    {
        fileName = "testFile.tab";
        currentDirectory = ".";
        filePath = currentDirectory + File.separator + fileName;
        fileLocation = new File(filePath); // For prod
        fileToOpen = new File(filepath); // For testing
    }

    public void FileCreator(String fileName, String directory)
    {
        File newFile = new File(directory + File.separator + fileName);

        try {
            if (newFile.exists()) {
                System.out.println("File already exists.");
            }
            else {
                System.out.println("File does not exist. Creating a new one.");
                newFile.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error occurred: "+ e + ".\nFile was not created.");
            e.printStackTrace();
        }
    }

    public void FileWriter(String fileName, String directory, String textToAdd)
    {
        String fileToWrite = directory + File.separator + fileName + ".tab";
        try {
            FileWriter writer = new FileWriter(fileToWrite);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(textToAdd);
            bufferedWriter.flush();
            bufferedWriter.close();
            System.out.println("Successfully wrote to the file.\n");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e + ".\nFailed to write to file.");
            e.printStackTrace();
        }
    }

    public ArrayList<List<String>> FileReader(String fileName, String directory)
    {
//        ArrayList<String[]> readFileArray = new ArrayList<String[]>();
        ArrayList<List<String>> readFileArrayList = new ArrayList<>();
        File fileToRead = new File(directory + File.separator + fileName);

        try {
            FileReader reader = new FileReader(fileToRead);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            String[] lineArray;
            List<String> rowArrayList = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null) {
                lineArray = line.split("\t");
//                readFileArray.add(lineArray);

                rowArrayList = Arrays.asList(lineArray);
                readFileArrayList.add(rowArrayList);
                System.out.println(rowArrayList.get(0));
            }
            System.out.println("RAL: "+rowArrayList.get(0));
            System.out.println("RFAL: "+readFileArrayList.get(1).get(1));

//            System.out.println(readFileArray.get(1)[2]);
            reader.close();
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return readFileArrayList;
    }

    public void FileCreatorTest()
    {
        try {
            if (fileToOpen.exists()) {
                System.out.println("File already exists.");
            }
            else {
                System.out.println("File does not exist. Creating a new one.");
                fileToOpen.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void FileWriterTest()
    {
        try {
            FileWriter writer = new FileWriter(fileToOpen);
            writer.write("Hello world.\n");
            writer.write("This is a new file created by java.\n");
            writer.write("Bing bang bong.\n\n");
            writer.flush();
            writer.close();
            System.out.println("Successfully wrote to the file.\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void FileReaderTest()
    {
        try {
            FileReader reader = new FileReader(fileToOpen);
            BufferedReader buffReader = new BufferedReader(reader);
            String line;
            while ((line = buffReader.readLine()) != null) {
                System.out.println(line);
            }
            buffReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void setFileToOpen(String newFileToOpen) {
        fileToOpen = new File(newFileToOpen);
    }
}
