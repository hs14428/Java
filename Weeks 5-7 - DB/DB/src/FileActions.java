import java.io.*;

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

    private String currentDirectory = ".";

    public FileActions()
    {
        //fileToOpen = new File(currentDirectory); // For prod
        fileToOpen = new File(filepath); // For testing
    }

    public void FileCreator()
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

    public void FileWriter()
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

    public void FileReader()
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
