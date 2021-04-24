package game;

import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class GraphParserExample
{

    public static void main(String[] args)
    {
        LinkedHashMap<String, Location> gameMap = new LinkedHashMap<>();
        Location location;
        String locationName;
        String locationDescription;
        String entityType;
        String entityName;
        String entityDescription;
        String pathStart;
        String pathEnd;

        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(args[0]);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();
            for(Graph g : subGraphs){
                System.out.printf("id = %s\n",g.getId().getId());
                ArrayList<Graph> subGraphs1 = g.getSubgraphs();
                for (Graph g1 : subGraphs1){
                    ArrayList<Node> nodesLoc = g1.getNodes(false);
                    Node nLoc = nodesLoc.get(0);
                    locationName = nLoc.getId().getId();
                    locationDescription = nLoc.getAttribute("description");
                    location = new Location(locationName, locationDescription);
                    System.out.printf("\tid = %s, name = %s, description = %s\n",g1.getId().getId(), nLoc.getId().getId(), nLoc.getAttribute("description"));
                    ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
                    for (Graph g2 : subGraphs2) {
                        System.out.printf("\t\tid = %s\n", g2.getId().getId());
                        ArrayList<Node> nodesEnt = g2.getNodes(false);
                        for (Node nEnt : nodesEnt) {
                            System.out.printf("\t\t\tid = %s, description = %s\n", nEnt.getId().getId(), nEnt.getAttribute("description"));
                            entityType = g2.getId().getId();
                            entityName = nEnt.getId().getId();
                            entityDescription = nEnt.getAttribute("description");
                            location.addNewEntity(entityType, entityName, entityDescription);
                        }
                    }
                    // Add the new Location to the gameMap
                    gameMap.put(nLoc.getId().getId(), location);
                }
                ArrayList<Edge> edges = g.getEdges();
                for (Edge e : edges) {
//                    Location newLocation;
                    // Set the location equal to the start start of the path route
                    pathStart = e.getSource().getNode().getId().getId();
                    location = gameMap.get(pathStart);
                    // Add the end of the path to the locations list of possible path locations
                    pathEnd = e.getTarget().getNode().getId().getId();
                    location.addPath(pathEnd);
                    // Save the updated location class back to the gameMap, now with possible path locations
                    gameMap.put(e.getSource().getNode().getId().getId(), location);
                    System.out.printf("Path from %s to %s\n", e.getSource().getNode().getId().getId(), e.getTarget().getNode().getId().getId());
                }

//                location = gameMap.get("start");
//                System.out.println("Location: "+ location.getName());
//                System.out.println(location.getEntity("artefacts", "potion").getDescription());
//                System.out.println(location.getEntity("furniture", "door").getDescription());
//                location.getPaths();
//
//                location = gameMap.get("cellar");
//                System.out.println("Location: "+ location.getName());
//                System.out.println(location.getEntity("characters", "elf").getDescription());
//
//                location.getPaths();

                Set<String> locationNames = gameMap.keySet();
                Collection<Location> locationDescriptions = gameMap.values();
                // Key value pair
                Set<Map.Entry<String, Location>> pairs = gameMap.entrySet();
                System.out.println(pairs);
                System.out.println(locationNames);
                System.out.println(locationDescriptions);

                for (String s : locationNames)
                {
                    System.out.println(s);
                }

                Set<String> entityNames;
                for (Map.Entry<String, Location> e : pairs)
                {

                    System.out.println();
                    System.out.println("Location: "+e.getKey() + ", Description: "+e.getValue());
                    Location l = e.getValue();
//                    entityNames = l.getAllEntityNames("artefacts");
//                    System.out.println("Artefacts: "+entityNames);
//                    entityNames = l.getAllEntityNames("furniture");
//                    System.out.println("furniture: "+entityNames);
//                    entityNames = l.getAllEntityNames("characters");
//                    System.out.println("characters: "+entityNames);
                    System.out.println("Possible locations to move to: ");
                    l.printPaths();
                }

            }

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (com.alexmerz.graphviz.ParseException pe) {
            System.out.println(pe);
        }
    }
}
