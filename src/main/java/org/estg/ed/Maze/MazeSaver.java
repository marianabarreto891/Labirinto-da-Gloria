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
 * Class responsible for serializing a Maze object into a JSON file format.
 * This class is the counterpart to MazeLoader.
 */
public class MazeSaver {

    /**
     * Converts a Maze object into a structured JSON file.
     *
     * @param maze O objeto Maze a ser guardado.
     * @param filename O nome do ficheiro (ex: 'novo_mapa.json').
     * @return true se o mapa foi guardado com sucesso, false em caso de erro.
     */
    public static boolean saveMazeToJSON(Maze maze, String filename) {
        // 1. Criar o objeto JSON principal
        JSONObject mazeJson = new JSONObject();
        mazeJson.put("id", maze.getId());
        mazeJson.put("name", maze.getName());

        // 2. Serializar Divisões
        JSONArray divisionsArray = serializeDivisions(maze.getD());
        mazeJson.put("divisions", divisionsArray);

        // 3. Serializar Corredores (Hallways)
        JSONArray hallsArray = serializeHallways(maze.getH());
        mazeJson.put("halls", hallsArray);

        // 4. Escrever o JSON para o ficheiro
        try (FileWriter file = new FileWriter(filename)) {
            // Usamos o toString(4) para formatar o JSON (indentação de 4 espaços), tornando-o legível.
            file.write(mazeJson.toString(4)); 
            System.out.println("Mapa guardado com sucesso em: " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("ERRO ao gravar o ficheiro JSON: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Helper: Converte a lista de Divisions para um JSONArray.
     */
    private static JSONArray serializeDivisions(ArrayUnorderedList<Division> divisions) {
        JSONArray jsonArray = new JSONArray();
        for (Division division : divisions) {
            JSONObject divisionJson = new JSONObject();
            divisionJson.put("id", division.getId());
            // Conversão do Enum DivisionType para o inteiro usado no JSON.
            divisionJson.put("type", getDivisionTypeInt(division.getDt()));
            // Poderia adicionar aqui lógica para serializar o 'Content' (Enigma/Lever) se necessário.
            jsonArray.put(divisionJson);
        }
        return jsonArray;
    }

// No ficheiro MazeSaver.java, dentro do método serializeHallways
private static JSONArray serializeHallways(ArrayUnorderedList<Hallway> hallways) {
    JSONArray jsonArray = new JSONArray();
    for (Hallway hallway : hallways) {
        JSONObject hallJson = new JSONObject();
        hallJson.put("from", hallway.getOrigin().getId());
        hallJson.put("to", hallway.getDestination().getId());
        hallJson.put("weight", hallway.getWeight());
        
        // CORREÇÃO: Deve usar getEventId() em vez de getEvent()
        hallJson.put("eventId", hallway.getEventId()); 
        
        jsonArray.put(hallJson);
    }
    return jsonArray;
}

    /**
     * Helper: Mapeia o Enum de volta para o inteiro usado no JSON (simetria com MazeLoader).
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