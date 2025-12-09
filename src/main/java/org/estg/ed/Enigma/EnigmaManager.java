package org.estg.ed.Enigma;

import Lists.ArrayUnorderedList;
import Interfaces.UnorderedListADT;

import java.util.Iterator;
import java.util.Random;

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

    public Enigma getUnusedEnigma() {
        if (allEnigmas.isEmpty()) {
            resetEnigmas();
        }

        Random random = new Random();
        int randomIndex = random.nextInt(allEnigmas.size());

        Enigma selectedEnigma = getEnigmaByIndex(allEnigmas, randomIndex);
        usedEnigmas.addToRear(selectedEnigma);
        
        try {
            allEnigmas.remove(selectedEnigma);
        } catch (Exception e) {
            e.printStackTrace();
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

