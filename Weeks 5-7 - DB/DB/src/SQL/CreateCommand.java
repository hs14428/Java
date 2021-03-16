package SQL;

import Database.Database;

public class CreateCommand extends Command
{

    public CreateCommand(String command) {
        super(command);
    }

    @Override
    public String runCommand(Database database) {
        return null;
    }
}
