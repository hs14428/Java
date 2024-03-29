package Game;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class JSONParsing
{
    private final ArrayList<Action> actionArrayList = new ArrayList<>();

    public JSONParsing(String actionFilename) throws IOException, ParseException
    {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(actionFilename));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray actions = (JSONArray) jsonObject.get("actions");
        Action newAction;

        for (Object action : actions) {
            JSONObject jsonAction = (JSONObject) action;
            ArrayList<String> triggers = (ArrayList<String>) jsonAction.get("triggers");
            ArrayList<String> subjects = (ArrayList<String>) jsonAction.get("subjects");
            ArrayList<String> consumed = (ArrayList<String>) jsonAction.get("consumed");
            ArrayList<String> produced = (ArrayList<String>) jsonAction.get("produced");
            String narration = (String) jsonAction.get("narration");

            for (String s : triggers)
            {
                newAction = new Action();
                newAction.setSubjects(subjects);
                newAction.setConsumed(consumed);
                newAction.setProduced(produced);
                newAction.setNarration(narration);
                newAction.setActionName(s);
                actionArrayList.add(newAction);
            }
        }
    }

    public ArrayList<Action> getActionsAL()
    {
        return actionArrayList;
    }
}
