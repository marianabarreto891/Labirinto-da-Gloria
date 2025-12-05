package org.estg.ed.Map;

import org.estg.ed.Room.Room;

import Lists.ArrayUnorderedList;

public class Map {
    private String name;
    private ArrayUnorderedList<Room> rooms;
    //private ArrayUnorderedList<Hall> halls;

    public Map(String name) {
        this.name = name;
        this.rooms = new ArrayUnorderedList<>();
        //this.halls = new ArrayUnorderedList<>();
    }

    // Métodos para adicionar salas e corredores
    public void addRoom(Room room) {
        rooms.addToRear(room);
    }

    /*
    public void addHall(Hall hall) {
        halls.addToRear(hall);
    }
        */

    // Getters e Setters
    public String getName() {
        return name;
    }

    public ArrayUnorderedList<Room> getRooms() {
        return rooms;
    }

    /*
    public ArrayUnorderedList<Hall> getHalls() {
        return halls;
    }
    */
}
