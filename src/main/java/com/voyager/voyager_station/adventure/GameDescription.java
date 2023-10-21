package com.voyager.voyager_station.adventure;

import com.voyager.voyager_station.parser.ParserOutput;
import com.voyager.voyager_station.type.Command;
import com.voyager.voyager_station.type.Inventory;
import com.voyager.voyager_station.type.Room;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roberto Maffucci
 */
public abstract class GameDescription implements Serializable {

    private final List<Room> rooms = new ArrayList<>();

    private final List<Command> commands = new ArrayList<>();

    private final Inventory inventory;

    private Room currentRoom;

    public GameDescription() {
        this.inventory = new Inventory();
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public abstract void init() throws Exception;

    public abstract String nextMove(ParserOutput p);

    public abstract boolean save(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException;

    public abstract GameDescription load(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException;

}
