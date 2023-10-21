package com.voyager.voyager_station.type;

import java.util.Set;

/**
 *
 * @author Roberto Maffucci
 */
public class AdvEnemy extends AdvNPC {

    private String examineDead;

    public AdvEnemy(int id) {
        super(id);
    }

    public AdvEnemy(int id, String name) {
        super(id, name);
    }

    public AdvEnemy(int id, String name, String examine) {
        super(id, name, examine);
    }

    public AdvEnemy(int id, String name, String examine, String look) {
        super(id, name, examine, look);
    }

    public AdvEnemy(int id, String name, String examine, String look, Set<String> alias) {
        super(id, name, examine, look, alias);
    }

    public String getExamineDead() {
        return examineDead;
    }

    public void setExamineDead(String examineDead) {
        this.examineDead = examineDead;
    }
}
