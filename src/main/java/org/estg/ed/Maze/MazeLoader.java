package org.estg.ed.Maze;

import java.io.FileReader;
import java.io.IOException;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Hall.Hallway;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Lists.ArrayUnorderedList;

public class MazeLoader {
    public static ArrayUnorderedList<Maze> loadMazeFromJSON(String filename) {

        ArrayUnorderedList<Maze> mazes = new ArrayUnorderedList<>();

        try (FileReader reader = new FileReader(filename)) {

            JSONTokener tokener = new JSONTokener(reader);
            JSONObject root = new JSONObject(tokener);

            int id = root.getInt("id");
            String name = root.getString("name");
            JSONArray entriesArray = root.getJSONArray("entries");
            int[] entries = new int[entriesArray.length()];
            for (int i = 0; i < entriesArray.length(); i++) {
                entries[i] = entriesArray.getInt(i);
            }

            // Extrair as divisões
            JSONArray divisionsArray = root.getJSONArray("divisions");
            ArrayUnorderedList<Division> divisions = new ArrayUnorderedList<>();
            for (int i = 0; i < divisionsArray.length(); i++) {
                JSONObject divisionObj = divisionsArray.getJSONObject(i);
                int divisionId = divisionObj.getInt("id");
                divisions.addToRear(new Division(divisionId));
            }

            // Extrair a sala central
            JSONObject centralRoomObj = root.getJSONObject("centralRoom");
            int centralRoomId = centralRoomObj.getInt("id");

            // Extrair os corredores (halls)
            JSONArray hallsArray = root.getJSONArray("halls");
            ArrayUnorderedList<Hallway> halls = new ArrayUnorderedList<>();
            
            for (int i = 0; i < hallsArray.length(); i++) {
                JSONObject hallObj = hallsArray.getJSONObject(i);

                int from = hallObj.getInt("from");
                int to = hallObj.getInt("to");
                double weight = hallObj.getDouble("weight");
                
                //IEvent event = (IEvent)hallObj.getJSONObject("event"); 
                
                halls.addToRear(new Hallway(new Division(from), new Division(to), null, weight));

            }

            Maze maze = new Maze(id, name, halls, divisions, entries, centralRoomId);
            mazes.addToRear(maze);

        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro JSON: " + e.getMessage());
        }

        return mazes;
    }
}
