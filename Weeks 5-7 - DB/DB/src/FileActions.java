import java.io.*;

class FileActions
{
    String directory = "C:" + File.separator + "Users" + File.separator + "Harry" + File.separator + "Downloads";
    String filename = "testfile.txt";
    String filepath = directory + File.separator + filename;
    File fileToOpen;

    public FileActions()
    {
        fileToOpen = new File(filepath);
    }

    public void FileCreator()
    {
        try {
            if (fileToOpen.exists()) {
                System.out.println("File already exists.\n");
            }
            else {
                System.out.println("File does not exist. Creating a new one.\n");
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
            writer.write("Bing bang bong.\n");
            writer.flush();
            writer.close();
            System.out.println("Successfully wrote to the file.");
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
}
