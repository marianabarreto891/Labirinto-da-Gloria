package org.estg.ed.Manager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.estg.ed.Players.Player;
import Lists.ArrayUnorderedList;

/**
 * Manages the generation and saving of game reports in JSON format.
 * Creates comprehensive reports of player performance and game history.
 */
public class GameReportManager {
    
    private static final String OUTPUT_FOLDER = "Files/Reports/";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    /**
     * Saves a complete game report including all players' data.
     * 
     * @param players The list of players who participated in the game
     * @param winner The player who won the game (or null if no winner)
     * @param mazeName The name of the maze that was played
     * @param totalTurns The total number of turns played
     */
    public static void saveGameReport(ArrayUnorderedList<Player> players, Player winner, 
                                     String mazeName, int totalTurns) {
        StringBuilder report = new StringBuilder();
        
        // Header do relatório
        report.append("{\n");
        report.append("  \"gameReport\": {\n");
        report.append("    \"timestamp\": \"").append(LocalDateTime.now().format(TIMESTAMP_FORMAT)).append("\",\n");
        report.append("    \"mazeName\": \"").append(mazeName).append("\",\n");
        report.append("    \"totalTurns\": ").append(totalTurns).append(",\n");
        report.append("    \"winner\": \"").append(winner != null ? winner.getName() : "None").append("\",\n");
        
        // Array de jogadores
        report.append("    \"players\": [\n");
        
        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            
            // Indent o JSON de cada jogador
            String playerJSON = player.generateJSONReport();
            String[] lines = playerJSON.split("\n");
            
            for (int i = 0; i < lines.length; i++) {
                report.append("      ").append(lines[i]);
                if (i < lines.length - 1) {
                    report.append("\n");
                }
            }
            
            if (iterator.hasNext()) {
                report.append(",");
            }
            report.append("\n");
        }
        
        report.append("    ]\n");
        report.append("  }\n");
        report.append("}\n");
        
        // Guardar o ficheiro
        String filename = generateFileName(mazeName);
        saveToFile(report.toString(), filename);
    }
    
    /**
     * Generates a unique filename for the game report.
     * 
     * @param mazeName The name of the maze
     * @return A formatted filename with timestamp
     */
    private static String generateFileName(String mazeName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String safeMazeName = mazeName.replaceAll("[^a-zA-Z0-9-_]", "_");
        return OUTPUT_FOLDER + "game_report_" + safeMazeName + "_" + timestamp + ".json";
    }
    
    /**
     * Saves the report content to a file.
     * Creates the output directory if it doesn't exist.
     * 
     * @param content The JSON content to save
     * @param filepath The full path where the file should be saved
     */
    private static void saveToFile(String content, String filepath) {
        try {
            // Criar diretório se não existir
            File directory = new File(OUTPUT_FOLDER);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Escrever ficheiro
            try (FileWriter writer = new FileWriter(filepath)) {
                writer.write(content);
            }
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("Game report saved successfully!");
            System.out.println("Location: " + filepath);
            System.out.println("=".repeat(60));
            
        } catch (IOException e) {
            System.err.println("ERROR: Failed to save game report.");
            System.err.println("Reason: " + e.getMessage());
            System.err.println("\nReport content (console output):");
            System.err.println(content);
        }
    }
    
    /**
     * Saves individual player reports (alternative method).
     * Creates separate files for each player.
     * 
     * @param players The list of players
     * @param mazeName The name of the maze
     */
    public static void saveIndividualPlayerReports(ArrayUnorderedList<Player> players, String mazeName) {
        for (Player player : players) {
            String filename = generatePlayerFileName(player.getName(), mazeName);
            String content = player.generateJSONReport();
            saveToFile(content, filename);
        }
    }
    
    /**
     * Generates a filename for an individual player report.
     * 
     * @param playerName The player's name
     * @param mazeName The maze name
     * @return A formatted filename
     */
    private static String generatePlayerFileName(String playerName, String mazeName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String safePlayerName = playerName.replaceAll("[^a-zA-Z0-9-_]", "_");
        String safeMazeName = mazeName.replaceAll("[^a-zA-Z0-9-_]", "_");
        return OUTPUT_FOLDER + "player_" + safePlayerName + "_" + safeMazeName + "_" + timestamp + ".json";
    }
}