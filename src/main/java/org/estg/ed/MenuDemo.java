
package org.estg.ed;

import java.util.Scanner;

import org.estg.ed.Hall.Hallway;
import org.estg.ed.Maze.Maze;
import org.estg.ed.Maze.MazeLoader;

import Interfaces.UnorderedListADT;
import Lists.ArrayUnorderedList;

/**
 *
 * @author maria
 */
public class MenuDemo {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        String caminhoFicheiro = "C:\\Users\\maria\\TP_ED_8240344_8240334\\src\\main\\java\\org\\estg\\ed\\files_labirintos\\mapa.json";
        ArrayUnorderedList<Maze> m = MazeLoader.loadMazeFromJSON(caminhoFicheiro);

        for (Maze maze : m) {
            System.out.println("ID: " + maze.getId());
            System.out.println("Name: " + maze.getName());

            // Imprimir as entradas
            System.out.println("Entries: ");
            for (int entry : maze.getE()) {
                System.out.println("- " + entry);
            }

            /* mprimir as divisões
            
              System.out.println("Divisions: ");
              UnorderedListADT<Division> divisions = maze.getD();
              for(int i = 0; i < divisions.size(); i++) {
                Division division = divisions.;
              System.out.println("- Division ID: " + division.getId());
              }
            */
        
            // Imprimir a sala central
            System.out.println("Central Room ID: " + maze.getC());

            // Imprimir os corredores (halls)«
            System.out.println("Halls: ");
            UnorderedListADT<Hallway> halls = maze.getH();
            for (int i = 0; i < halls.size(); i++) {
                Hallway hall = halls.iterator().next();
                System.out.println("- From: " + hall.getOrigin().getId() +
                        ", To: " + hall.getDestination().getId() +
                        // ", Event: " + hall.getEvent() +
                        ", Weight: " + hall.getWeight());
            }
        }
        /*
         * 
         * 
         * System.err.println("Bem vindo ao Labirinto da Glória!");
         * System.err.println("--------------------------------");
         * System.err.println("**** MENU **** ");
         * System.err.println("1 - Jogar");
         * System.err.println("2 - Criar Mapas");
         * System.err.println("3 - Sair");
         * 
         * int opcao = scan.nextInt();
         * 
         * switch (opcao) {
         * case 1 ->
         * System.err.println("Funcionalidade de Jogo ainda não implementada.");
         * case 2 -> System.err.
         * println("Funcionalidade de criação de mapas ainda não implementada.");
         * case 3 -> {
         * System.err.println("Obrigado por jogar! Até à próxima.");
         * return;
         * }
         * default ->
         * System.err.println("Opção inválida. Por favor, escolha uma opção válida.");
         * }
         * 
         */

    }
}
