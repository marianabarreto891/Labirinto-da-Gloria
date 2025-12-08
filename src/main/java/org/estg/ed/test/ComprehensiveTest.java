package org.estg.ed.test;

import java.util.Scanner;

import org.estg.ed.Content.Content;
import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Enigma.Enigma;
import org.estg.ed.Enigma.EnigmaLoader;
import org.estg.ed.Events.ExtraMoveEvent;
import org.estg.ed.Events.IEvent;
import org.estg.ed.Events.MoveBackEvent;
import org.estg.ed.Events.SkipTurnEvent;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Lever.Lever;
import org.estg.ed.Players.BotPlayer;
import org.estg.ed.Players.HumanPlayer;
import org.estg.ed.Players.Player;

import Lists.ArrayUnorderedList;
import Lists.DoublyLinkedUnorderedList;

/**
 * Teste completo e integrado de todas as classes do sistema
 * Labirinto da Glória
 */
public class ComprehensiveTest {

    public static void main(String[] args) {
        
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║                                                        ║");
        System.out.println("║     TESTE COMPLETO - LABIRINTO DA GLÓRIA              ║");
        System.out.println("║     Verificação de Integração de Classes              ║");
        System.out.println("║                                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            exibirMenu();
            int opcao = lerOpcao(scanner);

            switch (opcao) {
                case 1:
                    testarDivisions();
                    break;
                case 2:
                    testarContent();
                    break;
                case 3:
                    testarEnigmas();
                    break;
                case 4:
                    testarLevers();
                    break;
                case 5:
                    testarEventos();
                    break;
                case 6:
                    testarHallways();
                    break;
                case 7:
                    testarPlayers();
                    break;
                case 8:
                    testarIntegracaoCompleta();
                    break;
                case 9:
                    testarCarregamentoJSON();
                    break;
                case 10:
                    testarMovimentoAvancado();
                    break;
                case 11:
                    simulacaoJogoCompleto(scanner);
                    break;
                case 0:
                    continuar = false;
                    System.out.println("\n✓ Testes finalizados!");
                    break;
                default:
                    System.out.println("❌ Opção inválida!");
            }

            if (continuar) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║                    MENU DE TESTES                      ║");
        System.out.println("╠════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Testar Divisions (Divisões)                        ║");
        System.out.println("║  2. Testar Content (Abstrato)                          ║");
        System.out.println("║  3. Testar Enigmas                                     ║");
        System.out.println("║  4. Testar Levers (Alavancas)                          ║");
        System.out.println("║  5. Testar Eventos (Todos os tipos)                    ║");
        System.out.println("║  6. Testar Hallways (Corredores)                       ║");
        System.out.println("║  7. Testar Players (Bot e Human)                       ║");
        System.out.println("║  8. Testar Integração Completa                         ║");
        System.out.println("║  9. Testar Carregamento JSON                           ║");
        System.out.println("║ 10. Testar Movimento Avançado                          ║");
        System.out.println("║ 11. Simulação de Jogo Completo                         ║");
        System.out.println("║  0. Sair                                               ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.print("Escolha: ");
    }

    private static int lerOpcao(Scanner scanner) {
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer
            return opcao;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    /**
     * Teste 1: Divisions
     */
    private static void testarDivisions() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 1: DIVISIONS");
        System.out.println("═══════════════════════════════════════════\n");

        // Criar divisões sem conteúdo
        Division div1 = new Division(1, null, DivisionType.ENIGMA);
        Division div2 = new Division(2, null, DivisionType.LEVER);
        Division div3 = new Division(3);

        System.out.println("✓ Divisões criadas:");
        System.out.println("  - " + div1);
        System.out.println("  - " + div2);
        System.out.println("  - " + div3);

        // Adicionar conteúdo
        Enigma enigma = new Enigma(101, "Teste?", new String[]{"A", "B"}, 0);
        div1.setContent(enigma);

        System.out.println("\n✓ Após adicionar conteúdo:");
        System.out.println("  - Div1 tem conteúdo? " + div1.hasContent());
        System.out.println("  - Div3 tem conteúdo? " + div3.hasContent());

        // Testar equals
        Division div1Copy = new Division(1);
        System.out.println("\n✓ Teste de igualdade:");
        System.out.println("  - div1.equals(div1Copy)? " + div1.equals(div1Copy));
        System.out.println("  - div1.equals(div2)? " + div1.equals(div2));

        System.out.println("\n✅ Teste de Divisions concluído!");
    }

    /**
     * Teste 2: Content (Classe Abstrata)
     */
    private static void testarContent() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 2: CONTENT (Abstrato)");
        System.out.println("═══════════════════════════════════════════\n");

        Content enigma = new Enigma(1, "Pergunta?", new String[]{"A", "B"}, 0);
        Content lever = new Lever(2);

        System.out.println("✓ Conteúdos criados:");
        System.out.println("  - Enigma ID: " + enigma.getIdContent());
        System.out.println("  - Enigma Tipo: " + enigma.getType());
        System.out.println("  - Lever ID: " + lever.getIdContent());
        System.out.println("  - Lever Tipo: " + lever.getType());

        System.out.println("\n✓ Estado inicial:");
        System.out.println("  - Enigma resolvido? " + enigma.isSolved());
        System.out.println("  - Lever resolvido? " + lever.isSolved());

        System.out.println("\n✅ Teste de Content concluído!");
    }

    /**
     * Teste 3: Enigmas
     */
    private static void testarEnigmas() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 3: ENIGMAS");
        System.out.println("═══════════════════════════════════════════\n");

        String[] opcoes = {"Lisboa", "Porto", "Braga", "Faro"};
        Enigma enigma = new Enigma(1, "Capital de Portugal?", opcoes, 0);

        Division startDiv = new Division(100);
        Player bot = new BotPlayer("TestBot", startDiv);

        System.out.println("✓ Enigma criado:");
        System.out.println("  Pergunta: " + enigma.getQuestion());
        System.out.println("  Opções: " + opcoes.length);

        System.out.println("\n✓ Bot tentando resolver...");
        for (int i = 0; i < 3; i++) {
            boolean resolvido = enigma.interactType(bot);
            if (resolvido) {
                System.out.println("  ✓ Resolvido na tentativa " + (i + 1));
                break;
            }
        }

        System.out.println("\n✓ Comparação de enigmas:");
        Enigma enigma2 = new Enigma(2, "Teste", opcoes, 0);
        System.out.println("  enigma1.compareTo(enigma2): " + enigma.compareTo(enigma2));

        System.out.println("\n✅ Teste de Enigmas concluído!");
    }

    /**
     * Teste 4: Levers
     */
    private static void testarLevers() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 4: LEVERS (Alavancas)");
        System.out.println("═══════════════════════════════════════════\n");

        Lever lever = new Lever(1);
        Division startDiv = new Division(100);
        Player bot = new BotPlayer("TestBot", startDiv);

        System.out.println("✓ Lever criada com ID: " + lever.getIdContent());
        System.out.println("✓ Tipo: " + lever.getType());

        System.out.println("\n✓ Bot tentando ativar...");
        int tentativas = 0;
        while (!lever.isSolved() && tentativas < 5) {
            lever.interactType(bot);
            tentativas++;
        }

        System.out.println("  Ativada em " + tentativas + " tentativa(s)");
        System.out.println("  Status: " + (lever.isSolved() ? "ATIVADA" : "NÃO ATIVADA"));

        System.out.println("\n✅ Teste de Levers concluído!");
    }

    /**
     * Teste 5: Eventos
     */
    private static void testarEventos() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 5: EVENTOS");
        System.out.println("═══════════════════════════════════════════\n");

        Division div1 = new Division(1);
        Division div2 = new Division(2);
        Division div3 = new Division(3);
        Player player = new BotPlayer("EventTestBot", div1);

        // 1. ExtraMoveEvent
        System.out.println("1️⃣  ExtraMoveEvent");
        IEvent extraMove = new ExtraMoveEvent(2);
        System.out.println("   Antes: " + player.getExtraMoves() + " extra moves");
        extraMove.applyEffect(player);
        System.out.println("   Depois: " + player.getExtraMoves() + " extra moves");

        // 2. SkipTurnEvent
        System.out.println("\n2️⃣  SkipTurnEvent");
        IEvent skipTurn = new SkipTurnEvent(1);
        System.out.println("   Pode jogar antes: " + player.canPlay());
        skipTurn.applyEffect(player);
        System.out.println("   Pode jogar depois: " + player.canPlay());
        System.out.println("   Próximo turno: " + player.canPlay());

        // 3. MoveBackEvent
        System.out.println("\n3️⃣  MoveBackEvent");
        Hallway hall1 = new Hallway(div1, div2, null, 1.0);
        Hallway hall2 = new Hallway(div2, div3, null, 1.0);
        player.moveTo(div2, hall1);
        player.moveTo(div3, hall2);
        System.out.println("   Posição antes: " + player.getCurrentDivision().getId());
        IEvent moveBack = new MoveBackEvent(1);
        moveBack.applyEffect(player);
        System.out.println("   Posição depois: " + player.getCurrentDivision().getId());

        System.out.println("\n✅ Teste de Eventos concluído!");
    }

    /**
     * Teste 6: Hallways
     */
    private static void testarHallways() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 6: HALLWAYS");
        System.out.println("═══════════════════════════════════════════\n");

        Division div1 = new Division(1);
        Division div2 = new Division(2);
        Division div3 = new Division(3);

        // Hallway sem evento
        Hallway hallSafe = new Hallway(div1, div2, null, 1.0);
        System.out.println("✓ Hallway seguro:");
        System.out.println("  " + hallSafe);
        System.out.println("  Peso: " + hallSafe.getWeight());

        // Hallway com evento
        IEvent event = new ExtraMoveEvent(1);
        Hallway hallEvent = new Hallway(div2, div3, event, 2.5);
        System.out.println("\n✓ Hallway com evento:");
        System.out.println("  " + hallEvent);
        System.out.println("  Tem evento? " + (hallEvent.getEvent() != null));

        // Testar métodos
        System.out.println("\n✓ Métodos de verificação:");
        System.out.println("  Conecta div1? " + hallSafe.connectsDivision(div1));
        System.out.println("  Conecta div3? " + hallSafe.connectsDivision(div3));
        System.out.println("  Oposto de div1: " + hallSafe.getOppositeDivision(div1).getId());

        // Testar equals
        Hallway hallCopy = new Hallway(div2, div1, null, 1.0);
        System.out.println("\n✓ Teste de igualdade (undirected):");
        System.out.println("  hall(1->2).equals(hall(2->1))? " + hallSafe.equals(hallCopy));

        System.out.println("\n✅ Teste de Hallways concluído!");
    }

    /**
     * Teste 7: Players
     */
    private static void testarPlayers() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 7: PLAYERS");
        System.out.println("═══════════════════════════════════════════\n");

        Division start = new Division(1);
        Player bot = new BotPlayer("Bot_Alpha", start);
        Player human = new HumanPlayer("Human_Player", start);

        System.out.println("✓ Jogadores criados:");
        System.out.println("  - " + bot.getName() + " (Bot)");
        System.out.println("  - " + human.getName() + " (Human)");

        // Testar métodos abstratos do Bot
        System.out.println("\n✓ Respostas do Bot:");
        String respEnigma = bot.answerEnigma("Teste?", new String[]{"A", "B", "C"});
        String respLever = bot.answerLever();
        System.out.println("  Enigma: " + respEnigma);
        System.out.println("  Lever: " + respLever);

        // Testar escolha de movimento
        Division div2 = new Division(2);
        Division div3 = new Division(3);
        DoublyLinkedUnorderedList<Hallway> hallways = new DoublyLinkedUnorderedList<>();
        hallways.addToRear(new Hallway(start, div2, null, 1.0));
        hallways.addToRear(new Hallway(start, div3, null, 1.0));

        Hallway escolha = bot.chooseNextMove(hallways);
        System.out.println("\n✓ Bot escolheu ir para: Division " + escolha.getDestination().getId());

        // Testar ranking
        System.out.println("\n✓ Sistema de ranking:");
        bot.incrementEnigmasSolved();
        bot.incrementEnigmasSolved();
        human.incrementEnigmasSolved();
        int comparacao = bot.compareTo(human);
        System.out.println("  Bot enigmas: 2, Human enigmas: 1");
        System.out.println("  Comparação: " + (comparacao < 0 ? "Bot à frente" : "Human à frente"));

        System.out.println("\n✅ Teste de Players concluído!");
    }

    /**
     * Teste 8: Integração Completa
     */
    private static void testarIntegracaoCompleta() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 8: INTEGRAÇÃO COMPLETA");
        System.out.println("═══════════════════════════════════════════\n");

        // Criar mini-labirinto
        Enigma enigma = new Enigma(101, "2+2?", new String[]{"3", "4", "5"}, 1);
        Lever lever = new Lever(102);

        Division entrada = new Division(1, null, DivisionType.ENIGMA);
        Division salaEnigma = new Division(2, enigma, DivisionType.ENIGMA);
        Division salaLever = new Division(3, lever, DivisionType.LEVER);
        Division saida = new Division(4);

        IEvent event1 = new ExtraMoveEvent(1);
        IEvent event2 = new SkipTurnEvent(1);

        Hallway hall1 = new Hallway(entrada, salaEnigma, event1, 1.0);
        Hallway hall2 = new Hallway(salaEnigma, salaLever, null, 1.0);
        Hallway hall3 = new Hallway(salaLever, saida, event2, 1.0);

        Player player = new BotPlayer("IntegrationBot", entrada);

        System.out.println("✓ Mini-labirinto criado:");
        System.out.println("  Entrada -> Enigma -> Lever -> Saída");
        System.out.println("  Eventos: ExtraMove, None, SkipTurn");

        // Simular movimento
        System.out.println("\n✓ Simulando movimento:");
        System.out.println("  Posição: " + player.getCurrentDivision().getId());

        player.moveTo(salaEnigma, hall1);
        hall1.getEvent().applyEffect(player);
        System.out.println("  Moveu para: " + player.getCurrentDivision().getId());
        System.out.println("  Extra moves: " + player.getExtraMoves());

        // Interagir com enigma
        if (salaEnigma.hasContent()) {
            System.out.println("\n  Encontrou enigma!");
            salaEnigma.getContent().interactType(player);
        }

        player.moveTo(salaLever, hall2);
        System.out.println("  Moveu para: " + player.getCurrentDivision().getId());

        // Interagir com lever
        if (salaLever.hasContent()) {
            System.out.println("\n  Encontrou lever!");
            while (!salaLever.getContent().isSolved()) {
                salaLever.getContent().interactType(player);
            }
        }

        player.moveTo(saida, hall3);
        hall3.getEvent().applyEffect(player);
        System.out.println("  Moveu para: " + player.getCurrentDivision().getId());

        System.out.println("\n✓ Relatório JSON:");
        System.out.println(player.generateJSONReport());

        System.out.println("\n✅ Teste de Integração concluído!");
    }

    /**
     * Teste 9: Carregamento JSON
     */
    private static void testarCarregamentoJSON() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 9: CARREGAMENTO JSON");
        System.out.println("═══════════════════════════════════════════\n");

        System.out.println("✓ Tentando carregar 'enigmas.json'...");
        try {
            ArrayUnorderedList<Enigma> enigmas = EnigmaLoader.loadEnigmasFromJSON("enigmas.json");
            System.out.println("  ✓ Enigmas carregados: " + enigmas.size());

            if (enigmas.size() > 0) {
                System.out.println("\n✓ Primeiro enigma:");
                Enigma primeiro = enigmas.first();
                System.out.println("  ID: " + primeiro.getIdContent());
                System.out.println("  Pergunta: " + primeiro.getQuestion());
            }
        } catch (Exception e) {
            System.out.println("  ✗ Erro: " + e.getMessage());
            System.out.println("  (Certifique-se de que enigmas.json existe)");
        }

        System.out.println("\n✅ Teste de Carregamento concluído!");
    }

    /**
     * Teste 10: Movimento Avançado
     */
    private static void testarMovimentoAvancado() {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 10: MOVIMENTO AVANÇADO");
        System.out.println("═══════════════════════════════════════════\n");

        Division d1 = new Division(1);
        Division d2 = new Division(2);
        Division d3 = new Division(3);
        Division d4 = new Division(4);

        Player player = new BotPlayer("AdvancedBot", d1);

        Hallway h1 = new Hallway(d1, d2, null, 1.0);
        Hallway h2 = new Hallway(d2, d3, null, 1.0);
        Hallway h3 = new Hallway(d3, d4, null, 1.0);

        System.out.println("✓ Sequência de movimentos:");
        player.moveTo(d2, h1);
        System.out.println("  1. Posição: " + player.getCurrentDivision().getId() + " | Histórico: " + player.getPathHistorySize());

        player.moveTo(d3, h2);
        System.out.println("  2. Posição: " + player.getCurrentDivision().getId() + " | Histórico: " + player.getPathHistorySize());

        player.moveTo(d4, h3);
        System.out.println("  3. Posição: " + player.getCurrentDivision().getId() + " | Histórico: " + player.getPathHistorySize());

        System.out.println("\n✓ Testando moveBack(2):");
        player.moveBack(2);
        System.out.println("  Posição final: " + player.getCurrentDivision().getId());

        System.out.println("\n✅ Teste de Movimento Avançado concluído!");
    }

    /**
     * Teste 11: Simulação de Jogo Completo
     */
    private static void simulacaoJogoCompleto(Scanner scanner) {
        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  TESTE 11: SIMULAÇÃO DE JOGO COMPLETO");
        System.out.println("═══════════════════════════════════════════\n");

        // Criar labirinto
        Division entrada = new Division(1);
        Enigma enigma1 = new Enigma(101, "5x5?", new String[]{"20", "25", "30"}, 1);
        Division divEnigma = new Division(2, enigma1, DivisionType.ENIGMA);
        Lever lever = new Lever(102);
        Division divLever = new Division(3, lever, DivisionType.LEVER);
        Division saida = new Division(4);

        Hallway h1 = new Hallway(entrada, divEnigma, new ExtraMoveEvent(1), 1.0);
        Hallway h2 = new Hallway(divEnigma, divLever, null, 1.0);
        Hallway h3 = new Hallway(divLever, saida, new MoveBackEvent(1), 1.0);

        Player p1 = new BotPlayer("Bot_1", entrada);
        Player p2 = new BotPlayer("Bot_2", entrada);

        System.out.println("✓ Labirinto: Entrada -> Enigma -> Lever -> Saída");
        System.out.println("✓ Jogadores: " + p1.getName() + ", " + p2.getName());
        System.out.println("\nPressione ENTER para cada turno...");

        boolean jogoAtivo = true;
        int turno = 1;

        while (jogoAtivo && turno <= 15) {
            scanner.nextLine();
            System.out.println("\n--- TURNO " + turno + " ---");

            // Player 1
            if (p1.canPlay()) {
                System.out.println(p1.getName() + " [Div " + p1.getCurrentDivision().getId() + "]");
                if (p1.getCurrentDivision().getId() < 4) {
                    System.out.println("  -> Simulando movimento...");
                } else {
                    System.out.println("🏆 " + p1.getName() + " CHEGOU AO FIM!");
                    jogoAtivo = false;
                }
            }

            turno++;
        }

        System.out.println("\n✓ Relatórios finais:");
        System.out.println("\n" + p1.getName() + ":");
        System.out.println(p1.generateJSONReport());

        System.out.println("\n✅ Simulação concluída!");
    }
}