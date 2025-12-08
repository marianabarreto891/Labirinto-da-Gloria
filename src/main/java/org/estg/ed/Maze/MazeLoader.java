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

public class MazeLoader {
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
            System.err.println("Erro ao ler o ficheiro JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao processar o ficheiro JSON: " + e.getMessage());
        }
        return null;
    }

    private static Division findDivisionById(ArrayUnorderedList<Division> divisions, int id) {
        for (Division division : divisions) {
            if (division.getId() == id) {
                return division;
            }
        }
        return null;
    }

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
