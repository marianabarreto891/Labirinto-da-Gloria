package org.estg.ed;

import java.util.Iterator;
import java.util.Scanner;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Graph.MazeGraphConverter;
import org.estg.ed.Manager.GameManager;
import org.estg.ed.Maze.Maze;
import org.estg.ed.Maze.MazeManager;
import org.estg.ed.Players.BotPlayer;
import org.estg.ed.Players.HumanPlayer;
import org.estg.ed.Players.Player;

import Graphs.Network;
import Lists.ArrayUnorderedList;

/**
 *
 * @author maria
 */
public class MenuDemo {
    public static Scanner INPUT = new Scanner(System.in);

    public static void main(String[] args) {

        String mapa = "C:\\Users\\maria\\TP_ED_8240344_8240334\\src\\main\\java\\org\\estg\\ed\\files_labirintos\\mapa.json";
        String enigmas = "C:\\Users\\maria\\TP_ED_8240344_8240334\\src\\main\\java\\org\\estg\\ed\\files_enigmas\\enigmas.json";

        MazeManager mazeManager = new MazeManager();
        Maze maze = mazeManager.loadMazeWithContents(mapa, enigmas);

        // Obter as divisões de entrada
        Iterator<Division> divisionIterator = maze.getD().iterator();
        Division firstDivision = divisionIterator.next(); // Primeira divisão
        Division secondDivision = divisionIterator.next(); // Segunda divisão

        // Converter o labirinto para um grafo Network
        Network<Division> mazeNetwork = MazeGraphConverter.convertMazeToNetwork(maze);

        // Criar jogadores
        ArrayUnorderedList<Player> players = new ArrayUnorderedList<>();
        players.addToRear(new HumanPlayer("Player1", firstDivision, INPUT));
        players.addToRear(new BotPlayer("Bot1", secondDivision,INPUT));

        // Obter a sala central
        Division centralRoom = maze.getCentralRoom();

        // Iniciar o jogo
        GameManager gameManager = new GameManager(mazeNetwork, players, centralRoom);
        gameManager.startGame();
    }
}