package org.estg.ed.Maze;

import java.io.FileReader;
import java.io.IOException;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Hall.HallwayManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Lists.ArrayUnorderedList;

//@author Joana Rodrigues 8240334
/**
 * Class responsible for loading and parsing the maze structure from a JSON file.
 *
 * This class handles the deserialization of map data, converting raw JSON objects
 * into the game's internal graph representation (Maze, Divisions, and Hallways).
 */
public class MazeLoader {

    /**
     * Parses a JSON file to create a fully initialized Maze object.
     *
     * This method reads the file, extracts maze metadata (id, name), instantiates
     * all Division objects (nodes) with their specific types, and establishes
     * Hallway connections (edges) between them.
     *
     * @param filename The path to the JSON file containing the maze definition.
     * @return A constructed Maze object containing all divisions and hallways, or null if an error occurs.
     */
    public static Maze loadMazeFromJSON(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject root = new JSONObject(tokener);

            int id = root.getInt("id");
            String name = root.getString("name");

            JSONArray divisionsArray = root.getJSONArray("divisions");
            ArrayUnorderedList<Division> divisions = new ArrayUnorderedList<>();

            for (int i = 0; i < divisionsArray.length(); i++) {
                JSONObject divisionObj = divisionsArray.getJSONObject(i);
                int divisionId = divisionObj.getInt("id");
                int divisionType = divisionObj.getInt("type");

                DivisionType dt = getDivisionType(divisionType);
                divisions.addToRear(new Division(divisionId, dt));
            }

            JSONArray hallsArray = root.getJSONArray("halls");
            ArrayUnorderedList<Hallway> halls = new ArrayUnorderedList<>();

            for (int i = 0; i < hallsArray.length(); i++) {
                JSONObject hallObj = hallsArray.getJSONObject(i);
                int from = hallObj.getInt("from");
                int to = hallObj.getInt("to");
                double weight = hallObj.getDouble("weight");
                int eventId = hallObj.getInt("eventId");

                halls.addToRear(
                        HallwayManager.createHallway(
                                findDivisionById(divisions, from),
                                findDivisionById(divisions, to),
                                eventId,
                                weight));
            }

            return new Maze(id, name, halls, divisions);

        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());

        } catch (Exception e) {
            System.err.println("Error processing JSON file: " + e.getMessage());
        }
        return null;
    }


    /**
     * Auxiliary method to find a loaded Division object by its unique identifier.
     *
     * This is essential for linking Hallways to specific Division instances during
     * graph construction, as the JSON only provides integer references.
     *
     * @param divisions The list of already loaded divisions.
     * @param id        The ID of the division to find.
     * @return The matching Division object, or null if not found.
     */
    private static Division findDivisionById(ArrayUnorderedList<Division> divisions, int id) {
        for (Division division : divisions) {
            if (division.getId() == id) {
                return division;
            }
        }
        return null;
    }


    /**
     * Converts the integer representation of a division type from JSON into the internal Enum.
     *
     *
     * @param type The integer code read from the JSON.
     * @return The corresponding DivisionType enum.
     * @throws IllegalArgumentException if the type code is not recognized.
     */
    private static DivisionType getDivisionType(int type) {
        switch (type) {
            case 1:
                return DivisionType.ENTRY;
            case 2:
                return DivisionType.ENIGMA;
            case 3:
                return DivisionType.LEVER;
            case 4:
                return DivisionType.CENTRAL;
            case 5:
                return DivisionType.NULL;
            default:
                throw new IllegalArgumentException("Unknown division type: " + type);
        }
    }
}
