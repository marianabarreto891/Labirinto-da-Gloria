package org.estg.ed.Room;

import org.estg.ed.Content.Content;
import org.estg.ed.Hall.Hall;

import Interfaces.ListADT;
import Lists.LinkedUnorderedList;

public class Room {

    private final int idRoom;
    private Content content;
    RoomType type;
    ListADT<Hall> halls;

    public Room(int idRoom, Content content, RoomType type) {
        this.idRoom = idRoom;
        this.content = content;
        this.type = type;
        this.halls = new LinkedUnorderedList<>();
    }

    
    public void addHalls(Hall hl) {
        ((LinkedUnorderedList<Hall>) this.halls).addToRear(hl);
    }

    public ListADT<Hall> getHalls() {
        return halls;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public Content getContent() {
        return content;
    }

     
    public boolean hasContent() {
        return content != null;
    }


    public RoomType getRoomType() {
        return type;
    }

}
