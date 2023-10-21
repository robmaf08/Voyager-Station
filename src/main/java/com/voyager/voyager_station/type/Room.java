package com.voyager.voyager_station.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roberto Maffucci
 */
public class Room implements Serializable {

    private int id;

    public int getId() {
        return id;
    }

    private String name;

    private String description;

    private String look;

    private String imgNameRoom;

    private boolean visible = true;

    private boolean locked = false;

    private String passwordUnlock;

    private int timeVisited;

    private Room south = null;

    private Room north = null;

    private Room east = null;

    private Room west = null;

    private final List<AdvObject> objects = new ArrayList<>();

    private final List<AdvNPC> NPCs = new ArrayList<>();

    public Room() {
    }

    public Room(int id) {
        this.id = id;
        this.timeVisited = 0;
    }

    public Room(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getImgNameRoom() {
        return imgNameRoom;
    }

    public void setImgNameRoom(String imgNameRoom) {
        this.imgNameRoom = imgNameRoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Room getSouth() {
        return south;
    }

    public void setSouth(Room south) {
        this.south = south;
    }

    public Room getNorth() {
        return north;
    }

    public void setNorth(Room north) {
        this.north = north;
    }

    public Room getEast() {
        return this.east;
    }

    public void setEast(Room east) {
        this.east = east;
    }

    public Room getWest() {
        return this.west;
    }

    public void setWest(Room west) {
        this.west = west;
    }

    public List<AdvObject> getObjects() {
        return objects;
    }

    public void setVisited() {
        timeVisited++;
    }

    public int getTimeVisited() {
        return timeVisited;
    }

    public List<AdvNPC> getNPCs() {
        return NPCs;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Room other = (Room) obj;
        return this.id == other.id;
    }

    public String getPasswordUnlock() {
        return passwordUnlock;
    }

    public void setPasswordUnlock(String passwordUnlock) {
        this.passwordUnlock = passwordUnlock;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

}
