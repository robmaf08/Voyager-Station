package com.voyager.voyager_station.parser;

import com.voyager.voyager_station.type.AdvEnemy;
import com.voyager.voyager_station.type.AdvNPC;
import com.voyager.voyager_station.type.AdvObject;
import com.voyager.voyager_station.type.Command;

/**
 *
 * @author Roberto Maffucci
 */
public class ParserOutput {

    private Command command;

    private Command command2;

    private AdvObject object1 = null;

    private AdvObject invObject1 = null;

    private AdvNPC npc1 = null;

    private AdvEnemy enemy1 = null;

    private String extraWords = null;

    private AdvObject object2 = null;

    private AdvObject invObject2 = null;

    public ParserOutput(Command command, AdvObject object) {
        this.command = command;
        this.object1 = object;
    }

    public ParserOutput(Command command, Command command2, AdvObject object1, AdvObject invObject1, AdvNPC npc1, AdvEnemy enemy1, AdvObject object2, AdvObject invObject2, String extraWords) {
        this.command = command;
        this.command2 = command2;
        this.object1 = object1;
        this.invObject1 = invObject1;
        this.npc1 = npc1;
        this.enemy1 = enemy1;
        this.object2 = object2;
        this.invObject2 = invObject2;
        this.extraWords = extraWords;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public AdvObject getObject1() {
        return object1;
    }

    public AdvObject getObject2() {
        return object2;
    }

    public void setObject1(AdvObject object) {
        this.object1 = object;
    }

    public void setObject2(AdvObject object) {
        this.object2 = object;
    }

    public AdvObject getInvObject1() {
        return invObject1;
    }

    public AdvObject getInvObject2() {
        return invObject2;
    }

    public void setInvObject1(AdvObject invObject) {
        this.invObject1 = invObject;
    }

    public void setInvObject2(AdvObject invObject) {
        this.invObject2 = invObject;
    }

    public AdvEnemy getEnemy1() {
        return enemy1;
    }

    public void setEnemy1(AdvEnemy enemy1) {
        this.enemy1 = enemy1;
    }

    public String getExtraWords() {
        return extraWords;
    }

    public void setExtraWords(String extraWords) {
        this.extraWords = extraWords;
    }

    public Command getCommand2() {
        return command2;
    }

    public void setCommand2(Command command2) {
        this.command2 = command2;
    }

    public AdvNPC getNpc1() {
        return npc1;
    }

    public void setNpc1(AdvNPC npc1) {
        this.npc1 = npc1;
    }

}
