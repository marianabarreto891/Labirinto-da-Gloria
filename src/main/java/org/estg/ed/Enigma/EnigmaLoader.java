package org.estg.ed.Enigma;

import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import Lists.ArrayUnorderedList;

public class EnigmaLoader {
    public static ArrayUnorderedList<Enigma> loadEnigmasFromJSON(String filename) {

        ArrayUnorderedList<Enigma> enigmas = new ArrayUnorderedList<Enigma>();
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

                Enigma enigma = new Enigma(id, question, options, correctOption);
                enigmas.addToRear(enigma);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o ficheiro JSON: " + e.getMessage());
        }

        return enigmas;
    }
}
