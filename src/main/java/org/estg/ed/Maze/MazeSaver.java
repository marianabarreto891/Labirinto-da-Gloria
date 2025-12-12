package org.estg.ed.Maze;

import java.io.FileWriter;
import java.io.IOException;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Hall.Hallway;
import org.json.JSONArray;
import org.json.JSONObject;

import Lists.ArrayUnorderedList;

/**
 * Class responsible for saving a Maze object to a JSON file.
 * It serializes the maze structure including divisions and hallways.
 */
public class MazeSaver {

    /**
     * Saves the Maze object to a JSON file with proper formatting.
     *
     * @param maze The Maze object to be saved
     * @param filename The filename for the saved JSON (e.g., "new_map.json")
     * @return true if saving was successful, false otherwise
     */
    public static boolean saveMazeToJSON(Maze maze, String filename) {
        JSONObject mazeJson = new JSONObject();
        mazeJson.put("id", maze.getId());
        mazeJson.put("name", maze.getName());

        JSONArray divisionsArray = serializeDivisions(maze.getD());
        mazeJson.put("divisions", divisionsArray);

        JSONArray hallsArray = serializeHallways(maze.getH());
        mazeJson.put("halls", hallsArray);

        try (FileWriter file = new FileWriter(filename)) {
            file.write(mazeJson.toString(4));
            System.out.println("Map successfully saved to: " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("ERROR saving JSON file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Converts a list of Divisions into a JSONArray.
     *
     * @param divisions The list of divisions to serialize
     * @return JSONArray representation of divisions
     */
    private static JSONArray serializeDivisions(ArrayUnorderedList<Division> divisions) {
        JSONArray jsonArray = new JSONArray();
        for (Division division : divisions) {
            JSONObject divisionJson = new JSONObject();
            divisionJson.put("id", division.getId());
            divisionJson.put("type", getDivisionTypeInt(division.getDt()));
            jsonArray.put(divisionJson);
        }
        return jsonArray;
    }

    /**
     * Converts a list of Hallways into a JSONArray.
     *
     * @param hallways The list of hallways to serialize
     * @return JSONArray representation of hallways
     */
    private static JSONArray serializeHallways(ArrayUnorderedList<Hallway> hallways) {
        JSONArray jsonArray = new JSONArray();
        for (Hallway hallway : hallways) {
            JSONObject hallJson = new JSONObject();
            hallJson.put("from", hallway.getOrigin().getId());
            hallJson.put("to", hallway.getDestination().getId());
            hallJson.put("weight", hallway.getWeight());
            hallJson.put("eventId", hallway.getEventId());
            jsonArray.put(hallJson);
        }
        return jsonArray;
    }

    /**
     * Maps a DivisionType enum to the corresponding integer used in the JSON.
     *
     * @param type The DivisionType to convert
     * @return The integer representing the division type
     */
    private static int getDivisionTypeInt(DivisionType type) {
        switch (type) {
            case ENTRY:
                return 1;
            case ENIGMA:
                return 2;
            case LEVER:
                return 3;
            case CENTRAL:
                return 4;
            case NULL:
                return 5;
            default:
                return 5;
        }
    }
}
