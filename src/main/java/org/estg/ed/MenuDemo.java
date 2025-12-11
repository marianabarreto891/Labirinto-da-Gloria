package org.estg.ed;

import java.util.Iterator;
import java.util.Scanner;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Graph.ExtendedNetwork;
import org.estg.ed.Graph.MazeGraphConverter;
import org.estg.ed.Manager.GameManager;
import org.estg.ed.Maze.Maze;
import org.estg.ed.Maze.MazeManager;
import org.estg.ed.Players.BotPlayer;
import org.estg.ed.Players.HumanPlayer;
import org.estg.ed.Players.Player;

import Lists.ArrayUnorderedList;

/**
 * @author maria
 */
public class MenuDemo {
    public static Scanner INPUT = new Scanner(System.in);

    public static void main(String[] args) {
        String mapa = "C:\\Users\\maria\\TP_ED_8240344_8240334\\src\\main\\java\\org\\estg\\ed\\files_labirintos\\mapa.json";
        String enigmas = "C:\\Users\\maria\\TP_ED_8240344_8240334\\src\\main\\java\\org\\estg\\ed\\files_enigmas\\enigmas.json";

        MazeManager mazeManager = new MazeManager();
        Maze maze = mazeManager.loadMazeWithContents(mapa, enigmas);

        ExtendedNetwork<Division> mazeNetwork = MazeGraphConverter.convertMazeToNetwork(maze);

        // Obter todas as divisões de entrada
        ArrayUnorderedList<Division> entryDivisions = getEntryDivisions(maze);

        // Permitir que o jogador escolha uma entrada
        Division playerEntry = chooseEntry(entryDivisions, "Player1");

        // O bot escolhe uma entrada diferente
        Division botEntry = chooseBotEntry(entryDivisions, playerEntry);

        ArrayUnorderedList<Player> players = new ArrayUnorderedList<>();
        players.addToRear(new HumanPlayer("Player1", playerEntry, INPUT));
        players.addToRear(new BotPlayer("Bot1", botEntry, INPUT));

        Division centralRoom = maze.getCentralRoom();

        GameManager gameManager = new GameManager(mazeNetwork, players, centralRoom);
        gameManager.startGame();
    }

    private static ArrayUnorderedList<Division> getEntryDivisions(Maze maze) {
        ArrayUnorderedList<Division> entryDivisions = new ArrayUnorderedList<>();
        Iterator<Division> divisionIterator = maze.getD().iterator();
        while (divisionIterator.hasNext()) {
            Division division = divisionIterator.next();
            if (division.getDt() == DivisionType.ENTRY) {
                entryDivisions.addToRear(division);
            }
        }
        return entryDivisions;
    }

    private static Division chooseEntry(ArrayUnorderedList<Division> entryDivisions, String playerName) {
        System.out.println(playerName + ", choose your entry:");
        Iterator<Division> iterator = entryDivisions.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Division division = iterator.next();
            System.out.println((index) + ". Division ID: " + division.getId());
            index++;
        }

        System.out.print("Enter the number of your choice: ");
        int choice = INPUT.nextInt() - 1;
        INPUT.nextLine(); // Consume newline

        // Obter a divisão escolhida
        iterator = entryDivisions.iterator();
        for (int i = 0; i <= choice; i++) {
            if (iterator.hasNext()) {
                iterator.next();
            }
        }
        return iterator.next();
    }

    private static Division chooseBotEntry(ArrayUnorderedList<Division> entryDivisions, Division playerEntry) {
        Iterator<Division> iterator = entryDivisions.iterator();
        while (iterator.hasNext()) {
            Division division = iterator.next();
            if (!division.equals(playerEntry)) {
                return division;
            }
        }
        // Se todas as entradas forem iguais (improvável), retorna a primeira
        return entryDivisions.iterator().next();
    }

}
