package org.estg.ed.Room;

import org.estg.ed.Content.Content;

public class Room {

    private final int idRoom;
    private Content content;
    RoomType type;

    public Room(int idRoom, Content content, RoomType type) {
        this.idRoom = idRoom;
        this.content = content;
        this.type = type;
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
