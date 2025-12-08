package org.estg.ed.Maze;

import org.estg.ed.Divisions.Division;
import org.estg.ed.Divisions.DivisionType;
import org.estg.ed.Hall.Hallway;

import Lists.ArrayUnorderedList;

public class Maze {
    private int id;
    private String name;
    private ArrayUnorderedList<Hallway> h;
    private ArrayUnorderedList<Division> d;

    public Maze(int id, String name, ArrayUnorderedList<Hallway> h, ArrayUnorderedList<Division> d) {
        this.id = id;
        this.name = name;
        this.h = h;
        this.d = d;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayUnorderedList<Hallway> getH() {
        return h;
    }

    public void setH(ArrayUnorderedList<Hallway> h) {
        this.h = h;
    }

    public ArrayUnorderedList<Division> getD() {
        return d;
    }

    public void setD(ArrayUnorderedList<Division> d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "Maze [id=" + id + ", name=" + name + ", h=" + h + ", d=" + d + "]";
    }

    public Division getCentralRoom() {
        for (Division division : d) {
            if (division.getDt() == DivisionType.CENTRAL) {
                return division;
            }
        }
        return null;
    }

}
