package org.estg.ed.Enigma;

import java.util.Iterator;
import java.util.Random;

import Interfaces.UnorderedListADT;
import Lists.ArrayUnorderedList;

public class EnigmaManager {
    private final UnorderedListADT<Enigma> allEnigmas;
    private UnorderedListADT<Enigma> usedEnigmas;

    public EnigmaManager() {
        this.allEnigmas = new ArrayUnorderedList<>();
        this.usedEnigmas = new ArrayUnorderedList<>();
    }

    public void addEnigma(Enigma enigma) {
        allEnigmas.addToRear(enigma);
    }

// Em EnigmaManager.java

public Enigma getUnusedEnigma() {
    // 1. Tenta reabastecer a lista se ela estiver esgotada (mas não se estiver vazia desde o início)
    if (allEnigmas.isEmpty() && usedEnigmas.size() > 0) {
        // Esta condição só deve ocorrer quando todos os enigmas foram usados.
        resetEnigmas();
    }
    
    // ⬇️ 2. CORREÇÃO ESSENCIAL: Verifica se a lista está vazia APÓS a tentativa de carregamento.
    // Isto acontece se:
    // a) O ficheiro JSON falhar a leitura (problema de caminho).
    // b) O ficheiro JSON for lido, mas não contiver enigmas.
    if (allEnigmas.isEmpty()) {
        System.err.println("WARNING: Unable to load puzzles from the file. Empty list, cannot wait for the split.");
        return null; // Retorna null para evitar a exceção Random.nextInt(0)
    }

    // 3. A lógica de seleção e remoção continua, AGORA COM A GARANTIA de que allEnigmas.size() > 0

    Random random = new Random();
    // O tamanho da lista é garantidamente maior que zero
    int randomIndex = random.nextInt(allEnigmas.size()); 

    Enigma selectedEnigma = getEnigmaByIndex(allEnigmas, randomIndex);
    
    // É importante verificar se selectedEnigma não é null antes de adicionar/remover, 
    // embora no ArrayUnorderedList seja improvável se o índice for válido.
    if (selectedEnigma != null) {
        usedEnigmas.addToRear(selectedEnigma);
        
        try {
            allEnigmas.remove(selectedEnigma);
        } catch (Exception e) {
            // Se o enigma não for encontrado (impossível se usado getEnigmaByIndex), a exceção seria capturada aqui.
             System.err.println("Erro:" + e.getMessage());
        }
    }


    return selectedEnigma;
}



    private Enigma getEnigmaByIndex(UnorderedListADT<Enigma> list, int index) {
        Iterator<Enigma> iterator = list.iterator();
        int currentIndex = 0;
        while (iterator.hasNext()) {
            Enigma enigma = iterator.next();
            if (currentIndex == index) {
                return enigma;
            }
            currentIndex++;
        }
        return null;
    }

    private void resetEnigmas() {
        Iterator<Enigma> iterator = usedEnigmas.iterator();
        while (iterator.hasNext()) {
            allEnigmas.addToRear(iterator.next());
        }
        usedEnigmas = new ArrayUnorderedList<>();
    }
}

