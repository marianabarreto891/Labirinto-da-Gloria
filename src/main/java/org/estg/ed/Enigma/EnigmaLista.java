package org.estg.ed.Enigma;

import Interfaces.ListADT;

public class EnigmaLista {
    private ListADT<Enigma> enigmas;

    public ListADT<Enigma> getEnigmas() {
        return enigmas;
    }

    public void setEnigmas(ListADT<Enigma> enigmas) {
        this.enigmas = enigmas;
    }

}
