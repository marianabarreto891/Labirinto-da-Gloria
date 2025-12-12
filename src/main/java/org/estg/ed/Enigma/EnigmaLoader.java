package org.estg.ed.Enigma;

import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Lists.ArrayUnorderedList;
//@author Mariana Barreto 8240344
/**
 * Utility class responsible for loading and parsing Enigma definitions from a JSON file.
 *
 * This class handles the deserialization of game content, converting raw JSON data
 * into usable Enigma objects that can be distributed throughout the maze.
 */
public class EnigmaLoader {
    
    /**
     * Parses a JSON file to create a list of Enigma objects.
     *
     * This method reads the specified file, extracts the 'enigmas' array,
     * and iterates through it to parse the ID, question text, options, and the correct answer index
     * for each entry. It then constructs Enigma instances and adds them to a list.
     *
     * @param filename The path to the JSON file containing the enigma definitions.
     * @return An unordered list containing the loaded Enigma objects.
     */
    public static ArrayUnorderedList<Enigma> loadEnigmasFromJSON(String filename) {

        ArrayUnorderedList<Enigma> enigmas = new ArrayUnorderedList<>();
        try (FileReader reader = new FileReader(filename)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONObject root = new JSONObject(tokener);
            JSONArray enigmasArray = root.getJSONArray("enigmas");

            for (int i = 0; i < enigmasArray.length(); i++) {
                JSONObject enigmaObj = enigmasArray.getJSONObject(i);
                int id = enigmaObj.getInt("id");
                String question = enigmaObj.getString("question");
                JSONArray optionsArray = enigmaObj.getJSONArray("options");

                String options[] = new String[optionsArray.length()]; 
                for (int j = 0; j < optionsArray.length(); j++) {
                    options[j] = optionsArray.getString(j);
                }

                int correctOption = enigmaObj.getInt("correctOption");

                Enigma enigma = new Enigma(question, options, correctOption);
                enigmas.addToRear(enigma);
            }
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + e.getMessage());
        }

        return enigmas;
    }
}
