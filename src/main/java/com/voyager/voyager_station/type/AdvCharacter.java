package com.voyager.voyager_station.type;

import java.util.Set;

/**
 *
 * @author Roberto Maffucci
 */
public class AdvCharacter extends AdvNPC {

    private boolean firstDialogueFlag;

    private String firstDialogue;

    public AdvCharacter(int id) {
        super(id);
        firstDialogueFlag = true;
    }

    public AdvCharacter(int id, String name) {
        super(id, name);
        firstDialogueFlag = true;
    }

    public AdvCharacter(int id, String name, String examine) {
        super(id, name, examine);
        firstDialogueFlag = true;
    }

    public AdvCharacter(int id, String name, String examine, String look) {
        super(id, name, examine, look);
        firstDialogueFlag = true;
    }

    public AdvCharacter(int id, String name, String examine, String look, Set<String> alias) {
        super(id, name, examine, look, alias);
        firstDialogueFlag = true;
    }

    public AdvCharacter(int id, String name, String examine, String look, Set<String> alias, Inventory inventory, boolean firstDialogueFlag, String firstDialogue) {
        super(id, name, examine, look, alias, inventory);
        this.firstDialogueFlag = firstDialogueFlag;
        this.firstDialogue = firstDialogue;
    }

    public boolean isFirstDialogue() {
        return firstDialogueFlag;
    }

    public void setFirstDialogueFlag(boolean firstDialogue) {
        this.firstDialogueFlag = firstDialogue;
    }

    public String getFirstDialogue() {
        return firstDialogue;
    }

    public void setFirstDialogue(String firstDialogue) {
        this.firstDialogue = firstDialogue;
    }

}
