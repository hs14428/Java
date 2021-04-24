package game;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JSONParsing {

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(args[0]));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray actions = (JSONArray) jsonObject.get("actions");

        for (Object action : actions) {
            JSONObject jsonAction = (JSONObject) action;
            ArrayList<String> triggers = (JSONArray) jsonAction.get("triggers");
            String narration = (String) jsonAction.get("narration");
            System.out.println(triggers);
            System.out.println(narration);
        }

        for (Object action : actions) {
            JSONObject jsonAction = (JSONObject) action;
            ArrayList<String> triggers = (JSONArray) jsonAction.get("triggers");
            String narration = (String) jsonAction.get("narration");
            for (String trigger : triggers) {
                System.out.println(trigger);
            }
        }
    }
}
