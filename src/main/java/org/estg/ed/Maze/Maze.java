package org.estg.ed.Maze;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Hall.Hallway;

import Interfaces.UnorderedListADT;
import Lists.ArrayUnorderedList;

public class Maze {
    private int id;
    private String name;
    private UnorderedListADT<Hallway> h;
    private UnorderedListADT<Division> d;
    private int e[];
    private int c;

    public Maze(int id, String name, ArrayUnorderedList<Hallway> h, ArrayUnorderedList<Division> d, int e[], int c) {
        this.id = id;
        this.name = name;
        this.h = h;
        this.d = d;
        this.e = e;
        this.c = c;
    }
    //enquue de tudo e depois dequeue
    // Métodos para adicionar salas e corredores
    /*
     * public void addRoom(Room room) {
     * rooms.addToRear(room);
     * }
     */

    /*
     * public void addHall(Hall hall) {
     * halls.addToRear(hall);
     * }
     */

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UnorderedListADT<Hallway> getH() {
        return h;
    }

    public void setH(UnorderedListADT<Hallway> h) {
        this.h = h;
    }

    public UnorderedListADT<Division> getD() {
        return d;
    }

    public void setD(UnorderedListADT<Division> d) {
        this.d = d;
    }

    public int[] getE() {
        return e;
    }

    public void setE(int[] e) {
        this.e = e;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /*
     * public ArrayUnorderedList<Hall> getHalls() {
     * return halls;
     * }
     */
}
