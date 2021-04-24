package game;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class GraphParser
{
    private LinkedHashMap<String, Location> gameMap = new LinkedHashMap<>();

    public GraphParser(String entityFileName)
    {
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
            FileReader reader = new FileReader(entityFileName);
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
                            location.addEntity(entityType, entityName, entityDescription);
                        }
                    }
                    // Add the new Location to the gameMap
                    gameMap.put(nLoc.getId().getId(), location);
                }
                ArrayList<Edge> edges = g.getEdges();
                for (Edge e : edges) {
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

            }

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (ParseException pe) {
            System.out.println(pe);
        }
    }

    public LinkedHashMap<String, Location> getGameMap()
    {
        return gameMap;
    }
}
