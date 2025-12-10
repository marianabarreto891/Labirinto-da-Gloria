package org.estg.ed.Maze;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Graph.MazeGraphConverter; // Importa o conversor corrigido
import org.estg.ed.Graph.MazeNetwork;
import org.estg.ed.Hall.Hallway;
import org.estg.ed.Hall.HallwayManager;

import Lists.ArrayUnorderedList;

/**
 * Classe responsável por gerir e editar mapas do labirinto (Maze).
 * Cumpre o requisito de criação e gravação de novos mapas no formato JSON.
 */
public class MazeEditor {
    
    private Maze currentMaze;
    private MazeNetwork currentNetwork; // O grafo MazeNetwork (corrigido)

    /**
     * Construtor para iniciar um novo mapa vazio.
     */
    public MazeEditor() {
        // Inicializa o Maze com listas vazias
        ArrayUnorderedList<Hallway> h = new ArrayUnorderedList<>();
        ArrayUnorderedList<Division> d = new ArrayUnorderedList<>();
        this.currentMaze = new Maze(1, "Novo Mapa Nao Salvo", h, d);
        
        // Usa o MazeGraphConverter CORRIGIDO
        this.currentNetwork = MazeGraphConverter.convertMazeToNetwork(currentMaze);
        System.out.println("Novo mapa vazio criado.");
    }
    
    /**
     * Carrega um mapa existente para edição.
     * * @param filename O nome do ficheiro JSON do mapa.
     */
    public void loadMaze(String filename) {
        this.currentMaze = MazeLoader.loadMazeFromJSON(filename);
        
        if (this.currentMaze != null) {
            // Usa o MazeGraphConverter CORRIGIDO
            this.currentNetwork = MazeGraphConverter.convertMazeToNetwork(currentMaze);
            System.out.println("Mapa '" + currentMaze.getName() + "' carregado para edição.");
        } else {
            System.err.println("ERRO: Falha ao carregar o mapa do ficheiro: " + filename);
        }
    }
    
    // --- MÉTODOS DE EDIÇÃO ---

    /**
     * Adiciona uma nova divisão (vértice) ao mapa.
     * * @param id O ID único da nova divisão.
     * @param type O tipo de divisão (ENTRY, ENIGMA, etc.).
     */
    public void addDivision(int id, DivisionType type) {
        if (findDivision(id) != null) {
            System.err.println("ERRO: Divisão com ID " + id + " já existe.");
            return;
        }
        
        Division newDiv = new Division(id, type);
        
        // 1. Adiciona à lista de Divisões do Maze (para guardar em JSON)
        currentMaze.getD().addToRear(newDiv); 
        
        // 2. Adiciona ao Grafo (MazeNetwork) para a lógica do jogo
        currentNetwork.addVertex(newDiv);    
        
        System.out.println("✅ Divisão " + id + " (" + type + ") adicionada.");
    }

    /**
     * Conecta duas divisões com um corredor (aresta).
     * * @param id1 ID da divisão de origem.
     * @param id2 ID da divisão de destino.
     * @param weight O peso/custo do corredor.
     * @param eventId O ID do evento associado (0 para nenhum).
     */
    public void connectDivisions(int id1, int id2, double weight, int eventId) {
        Division div1 = findDivision(id1);
        Division div2 = findDivision(id2);
        
        if (div1 == null || div2 == null) {
             System.err.println("ERRO: Uma ou ambas as divisões não encontradas.");
             return;
        }
        
        // Cria o Hallway usando o HallwayManager (que cria o objeto Event)
        Hallway newHall = HallwayManager.createHallway(div1, div2, eventId, weight);
        
        // 1. Adiciona à lista de Hallways do Maze (para guardar em JSON)
        currentMaze.getH().addToRear(newHall);     
        
        // 2. Adiciona ao Grafo (MazeNetwork)
        currentNetwork.addEdge(div1, div2, weight); 
        
        // Também é útil manter a lista interna de hallways do MazeNetwork atualizada
        currentNetwork.hallways.addToRear(newHall); 

        System.out.println("✅ Corredor (" + id1 + " <-> " + id2 + ", Peso: " + weight + ") criado.");
    }

    /**
     * Define o nome e ID do mapa (metadados).
     */
    public void setMetadata(int id, String name) {
        currentMaze.setId(id);
        currentMaze.setName(name);
        System.out.println("Metadados atualizados para ID: " + id + ", Nome: " + name);
    }
    
    /**
     * Guarda o estado atual do mapa no formato JSON (usa o MazeSaver corrigido).
     * * @param filename O nome do ficheiro (ex: "novo_mapa.json").
     */
    public void saveMaze(String filename) {
        MazeSaver.saveMazeToJSON(currentMaze, filename);
        System.out.println("💾 Mapa '" + currentMaze.getName() + "' guardado com sucesso em " + filename + ".");
    }
    
    // --- HELPER ---
    
    /**
     * Pesquisa uma divisão pelo ID na lista de divisões do Maze.
     * * @param id O ID da divisão.
     * @return A Division encontrada ou null.
     */
    private Division findDivision(int id) {
        for (Division div : currentMaze.getD()) {
            if (div.getId() == id) {
                return div;
            }
        }
        return null;
    }


    /**
     * Getter para o objeto Maze que está atualmente a ser editado.
     * Necessário para inspeção (ex: na consola do editor interativo).
     * @return O objeto Maze atual.
     */
    public Maze getCurrentMaze() {
        return currentMaze;
    }
}

