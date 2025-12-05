package org.estg.ed.teste1;

import org.estg.ed.Hall.Hallway;
import org.estg.ed.Events.SkipTurnEvent;
import org.estg.ed.Events.SwapPositionEvent;
import Lists.DoublyLinkedUnorderedList;
import org.estg.ed.Players.*;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;



// --- MOCK MÍNIMO PARA COMPILAÇÃO (Assumindo que estão noutros arquivos) ---
// Nota: Certifique-se de que a sua classe Divisao tem um construtor Divisao(String name).
// -------------------------------------------------------------------------


public class PlayerChoiceSimulator {

    // Método auxiliar para criar e preencher a lista de Hallways
    public static DoublyLinkedUnorderedList<Hallway> createMockHallways(Divisao currentDiv) {
        DoublyLinkedUnorderedList<Hallway> available = new DoublyLinkedUnorderedList<>();
        
        // Criar divisões alvo
        Divisao targetA = new Divisao("Sala do Tesouro (Falsa)");
        Divisao targetB = new Divisao("Sala Enigma X");
        Divisao targetC = new Divisao("Corredor Longo");

        // Usando seus eventos concretos
        SwapPositionEvent swapEvt = new SwapPositionEvent();
        SkipTurnEvent skipEvt = new SkipTurnEvent(2); // Bloqueia 2 turnos

        // Corredor com evento de Troca de Posição
        available.addToRear(new Hallway(currentDiv, targetA, swapEvt)); 
        
        // Corredor sem evento
        available.addToRear(new Hallway(currentDiv, targetB, null)); 
        
        // Corredor com evento de Bloqueio de Turno
        available.addToRear(new Hallway(currentDiv, targetC, skipEvt));
        
        return available;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // 1. Configuração
        Divisao startDiv = new Divisao("Entrada Central");
        
        HumanPlayer human = new HumanPlayer("Carlos (Humano)", startDiv);
        BotPlayer bot = new BotPlayer("Bender (Bot)", startDiv);

        DoublyLinkedUnorderedList<Hallway> availablePaths = createMockHallways(startDiv);

        System.out.println("==================================================");
        System.out.println("🧪 Teste de Escolha de Movimento (Polimorfismo) 🧪");
        System.out.println("==================================================");

        
        // --- TESTE 1: BOT PLAYER (Modo Automático) ---
        // O BotPlayer usa a lógica de iteração/indexação para obter uma escolha aleatória.
        System.out.println("\n--- Teste 1: BOT (Escolha Automática) ---");
        System.out.println(bot.getName() + " está a jogar...");
        Hallway botChoice = bot.chooseNextMove(availablePaths);
        System.out.println("bot1 " + bot.getName() + " escolheu: " + botChoice.toString());

        
        // --- TESTE 2: HUMAN PLAYER (Modo Manual) ---
        System.out.println("\n--- Teste 2: HUMANO (Escolha por Input) ---");
        System.out.println(human.getName() + " está a jogar. Escolha uma opção:");
        
        Hallway humanChoice = null;
        
        // --- CRIAÇÃO DO ARRAY TEMPORÁRIO E LOOP DE ESCOLHA ---
        
        int size = availablePaths.size(); 
        // Array temporário para mapear o índice de escolha (0, 1, 2) aos objetos Hallway
        Hallway[] hallwayOptions = new Hallway[size];
        
        // Preenche o Array temporário usando o Iterator
        Iterator<Hallway> populateIterator = availablePaths.iterator();
        for (int i = 0; populateIterator.hasNext(); i++) {
            hallwayOptions[i] = populateIterator.next();
        }

        // Simulação do loop de escolha
        while (humanChoice == null) {
            
            // 1. Listar opções a partir do Array temporário
            for (int i = 0; i < size; i++) {
                Hallway h = hallwayOptions[i];
                String eventDesc = (h.getEvent() != null) ? h.getEvent().getDescription() : "Nenhum Evento";
                System.out.println("[" + i + "] Ir para " + h.getDestination().getName() + " (" + eventDesc + ")");
            }
            
            // 2. Pedir Input
            System.out.print("Digite o número da sua escolha (0 a " + (size - 1) + "): ");
            
            try {
                int choiceIndex = scanner.nextInt();
                
                // 3. Validar e obter a escolha do Array
                if (choiceIndex >= 0 && choiceIndex < size) {
                    humanChoice = hallwayOptions[choiceIndex]; 
                } else {
                    System.out.println("Opção fora do intervalo. Tente novamente.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
                scanner.nextLine(); // Limpar buffer do Scanner
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            } finally {
                // Limpa o buffer caso a escolha tenha sido inválida
                if (humanChoice == null && scanner.hasNextLine()) {
                    scanner.nextLine(); 
                }
            }
        }
        
        System.out.println("humano:" + human.getName() + " escolheu: " + humanChoice.toString());

        scanner.close();
    }
}