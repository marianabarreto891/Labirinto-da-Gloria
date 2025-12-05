package org.estg.ed.Players;

import org.estg.ed.Hall.Hallway;
import Lists.DoublyLinkedUnorderedList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner; 
import org.estg.ed.teste1.Divisao;

/**
 * Represents a human-controlled player (Manual mode) in the Labyrinth of Glory game simulator.
 * This class extends the abstract Player base and implements decision-making
 * logic based on user input (I/O interaction). [cite_start]It fulfills the requirement for the Manual game mode[cite: 50].
 */
public class HumanPlayer extends Player {

    /**
     * Constructs a new HumanPlayer instance, setting the initial state inherited from the Player base class.
     * * @param name The unique identifier of the human player.
     * @param initialPosition The entry Division where the player begins the game.
     */
    public HumanPlayer(String name, Divisao initialPosition) {
        super(name, initialPosition);
    }

    /**
     * Determines the human player's next move by prompting the user for input via the console.
     * This method lists the available Hallways by iterating through the custom Doubly Linked List 
     * and uses a temporary array to map the user's selected index to the correct Hallway object.
     * * @param availableHallways The list of valid Hallways the player can choose from the current Division.
     * @return The chosen Hallway for the next movement, or {@code null} if no moves are available or if an unrecoverable error occurs.
     */
    @Override
    public Hallway chooseNextMove(DoublyLinkedUnorderedList<Hallway> availableHallways) {
        
        try (Scanner scanner = new Scanner(System.in)) { 
            int size = availableHallways.size();
            
            if (size == 0) {
                System.out.println(this.getName() + " has no available moves. Skipping turn.");
                return null;
            }

            // Temporary array used to map the user's numeric choice (index) to the Hallway object 
            // since DoublyLinkedUnorderedList does not support O(1) random access (get(index)).
            Hallway[] hallwayOptions = new Hallway[size];
            
            Hallway chosenHallway = null;
            
            while (chosenHallway == null) {
                 
                // 1. List options and populate the temporary array
                Iterator<Hallway> iterator = availableHallways.iterator();
                int i = 0;
                System.out.println("\n" + this.getName() + " está em " + this.getCurrentDivision().getName() + ". Escolha um caminho:");
                
                while (iterator.hasNext()) {
                    Hallway hallway = iterator.next();
                    hallwayOptions[i] = hallway; // Store reference in the temporary array
                    System.out.println("[" + i + "] Ir para " + hallway.getDestination().getName() + " (" + hallway.toString() + ")"); 
                    i++;
                }

                // 2. Request Input
                System.out.print("Digite o número da sua escolha (0 a " + (size - 1) + "): ");
                
                try {
                    int choiceIndex = scanner.nextInt();
                    
                    // 3. Validate input and return chosen Hallway from the array
                    if (choiceIndex >= 0 && choiceIndex < hallwayOptions.length) {
                        chosenHallway = hallwayOptions[choiceIndex];
                        return chosenHallway; // Valid choice found
                    } else {
                        System.out.println("Opção inválida: O número está fora do intervalo permitido.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida: Por favor, digite apenas um número inteiro.");
                    scanner.nextLine(); // Clear scanner buffer
                } catch (Exception e) {
                    System.out.println("Ocorreu um erro inesperado durante a leitura: " + e.getMessage());
                    return null;
                }
            }
        } // Scanner is automatically closed here
        
        return null; // Should ideally be unreachable
    }
}