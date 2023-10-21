package com.voyager.voyager_station.type;

/**
 *
 * @author Roberto Maffucci
 */
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AdvNPC implements Serializable {

    private final int id;

    private String name;

    private String examine;

    private String imgObjRoom;

    private String look;

    private boolean alive = true;

    private Set<String> alias = new HashSet<>();

    private Inventory inventory = new Inventory();

    public AdvNPC(int id) {
        this.id = id;
    }

    public AdvNPC(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public AdvNPC(int id, String name, String examine) {
        this.id = id;
        this.name = name;
        this.examine = examine;
    }

    public AdvNPC(int id, String name, String examine, String look) {
        this.id = id;
        this.name = name;
        this.examine = examine;
        this.look = look;
    }

    public AdvNPC(int id, String name, String examine, String look, Set<String> alias) {
        this.id = id;
        this.name = name;
        this.examine = examine;
        this.look = look;
        this.alias = alias;
    }

    public AdvNPC(int id, String name, String examine, String look, Set<String> alias, Inventory inventory) {
        this.id = id;
        this.name = name;
        this.examine = examine;
        this.look = look;
        this.alias = alias;
        this.inventory = inventory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    public int getId() {
        return id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void addInventory(AdvObject o) {
        inventory.add(o);
    }

    public void removeFromInventory(int id) {
        inventory.remove(id);
    }

    public AdvObject getFromInventory(int id) {
        return inventory.get(id);
    }

    public String getImgObjRoom() {
        return imgObjRoom;
    }

    public void setImgObjRoom(String imgObjRoom) {
        this.imgObjRoom = imgObjRoom;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
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
        final AdvNPC other = (AdvNPC) obj;
        return this.id == other.id;
    }
}
