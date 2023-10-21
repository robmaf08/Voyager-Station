package com.voyager.voyager_station.parser;

import com.voyager.voyager_station.adventure.Utils;
import com.voyager.voyager_station.type.AdvEnemy;
import com.voyager.voyager_station.type.AdvNPC;
import com.voyager.voyager_station.type.AdvObject;
import com.voyager.voyager_station.type.Command;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Roberto Maffucci
 */
public class Parser {

    private final Set<String> stopwords;

    private String extraWords;

    public Parser(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    private int checkForCommand(String token, List<Command> commands) {
        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).getName().equalsIgnoreCase(token) || commands.get(i).getAlias().contains(token)) {
                return i;
            }
        }
        return -1;
    }

    private int checkForObject(String token, List<AdvObject> objects) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getName().equalsIgnoreCase(token)) {
                return i;
            } else {
                if (objects.get(i) != null
                        && objects.get(i).getAlias() != null
                        && !objects.get(i).getAlias().isEmpty()) {
                    if (objects.get(i).getAlias().contains(token)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private int checkForNPCs(String token, List<AdvNPC> NPCs) {
        for (int i = 0; i < NPCs.size(); i++) {
            if (!(NPCs.get(i) instanceof AdvEnemy)
                    && (NPCs.get(i).getName().equals(token) || NPCs.get(i).getAlias().contains(token))) {
                return i;
            } else {
            }
        }
        return -1;
    }

    private int checkForEnemy(String token, List<AdvNPC> NPCs) {
        for (int i = 0; i < NPCs.size(); i++) {
            if (NPCs.get(i) instanceof AdvEnemy && (NPCs.get(i).getName().equalsIgnoreCase(token) || NPCs.get(i).getAlias().contains(token))) {
                return i;
            } else {
            }
        }
        return -1;
    }

    public ParserOutput parse(String command, List<Command> commands, List<AdvObject> objects, List<AdvObject> inventory, List<AdvNPC> NPCs) {
        extraWords = null;
        List<String> tokens = Utils.parseString(command, stopwords);
        if (tokens.size() > 0) {
            int inCommand = checkForCommand(tokens.get(0), commands);
            if (inCommand > -1) {
                if (tokens.size() > 1) {
                    int inCommand2 = -1;
                    int inObj1 = -1;
                    int inObj2 = -1;
                    int inInvObj1 = -1;
                    int inInvObj2 = -1;
                    int inNpc = -1;
                    int inEnemy1 = -1;

                    inCommand2 = checkForCommand(tokens.get(1), commands);
                    for (int i = 1; i < tokens.size(); i++) {
                        if (inObj1 < 0 && inInvObj1 < 0 && inEnemy1 < 0) {
                            inObj1 = checkForObject(tokens.get(i), objects);
                            if (inObj1 < 0) {
                                inInvObj1 = checkForObject(tokens.get(i), inventory);
                                if (inInvObj1 < 0) {
                                    inNpc = checkForNPCs(tokens.get(i), NPCs);
                                    if (inNpc < 0) {
                                        inEnemy1 = checkForEnemy(tokens.get(i), NPCs);
                                        if (inEnemy1 < 0) {
                                            extraWords = tokens.get(i);
                                        }
                                    }
                                }
                            }
                        } else {
                            inObj2 = checkForObject(tokens.get(i), objects);
                            if (inObj2 < 0) {
                                inInvObj2 = checkForObject(tokens.get(i), inventory);
                            }
                        }
                    }

                    if (inCommand >= 0 && inCommand2 >= 0 && inNpc >= 0) {                 //oggetto
                        return new ParserOutput(commands.get(inCommand), commands.get(inCommand2), null, null, NPCs.get(inNpc), null, null, null, extraWords);
                    } else if (inCommand >= 0 && inCommand2 >= 0 && extraWords != null) {
                        return new ParserOutput(commands.get(inCommand), commands.get(inCommand2), null, null, null, null, null, null, extraWords);
                    } else if (inObj1 > -1 && inObj2 < 0 && inInvObj2 < 0) {                 //oggetto
                        return new ParserOutput(commands.get(inCommand), null, objects.get(inObj1), null, null, null, null, null, extraWords);
                    } else if (inObj1 > -1 && inObj2 > -1) {                                                              //oggetto, oggetto
                        return new ParserOutput(commands.get(inCommand), null, objects.get(inObj1), null, null, null, objects.get(inObj2), null, extraWords);
                    } else if (inObj1 > -1 && inInvObj2 > -1) {                                                           //oggetto, inventario
                        return new ParserOutput(commands.get(inCommand), null, objects.get(inObj1), null, null, null, null, inventory.get(inInvObj2), extraWords);
                    } else if (inObj1 > -1) {                                                            //oggetto, nemico
                        return new ParserOutput(commands.get(inCommand), null, objects.get(inObj1), null, null, null, null, null, extraWords);
                    } else if (inEnemy1 > -1 && inObj1 < 0 && inObj2 < 0 && inInvObj2 < 0) {
                        return new ParserOutput(commands.get(inCommand), null, null, null, null, (AdvEnemy) NPCs.get(inEnemy1), null, null, extraWords);
                    } else if (inEnemy1 > -1 && inInvObj2 > -1) {                                                         //nemico, inventario
                        return new ParserOutput(commands.get(inCommand), null, null, null, null, (AdvEnemy) NPCs.get(inEnemy1), null, inventory.get(inInvObj2), extraWords);
                    } else if (inInvObj1 > -1 && inObj2 < 0 && inInvObj2 < 0) {       //inventario            
                        return new ParserOutput(commands.get(inCommand), null, null, inventory.get(inInvObj1), null, null, null, null, extraWords);
                    } else if (inInvObj1 > -1 && inObj2 > -1) {                                                           //inventario, oggetto
                        return new ParserOutput(commands.get(inCommand), null, null, inventory.get(inInvObj1), null, null, objects.get(inObj2), null, extraWords);
                    } else if (inInvObj1 > -1 && inInvObj2 > -1) {                                                        //inventario, inventario
                        return new ParserOutput(commands.get(inCommand), null, null, inventory.get(inInvObj1), null, null, null, inventory.get(inInvObj2), extraWords);
                    } else if (inEnemy1 > -1 && inObj2 > -1) {                                                            //nemico, oggetto
                        return new ParserOutput(commands.get(inCommand), null, null, null, null, (AdvEnemy) NPCs.get(inEnemy1), objects.get(inObj2), null, extraWords);
                    } else if (inNpc > -1 && inEnemy1 < 0 && inObj1 < 0 && inObj2 < 0) {
                        return new ParserOutput(commands.get(inCommand), null, null, null, NPCs.get(inNpc), null, null, null, extraWords);
                    } else {
                        return new ParserOutput(commands.get(inCommand), null, null, null, null, null, null, null, extraWords);
                    }
                } else {
                    if (inCommand > -1) {
                        return new ParserOutput(commands.get(inCommand), null, null, null, null, null, null, null, extraWords);
                    } else {
                        return new ParserOutput(null, null, null, null, null, null, null, null, extraWords);
                    }
                }
            } else {
                extraWords = command;
                return new ParserOutput(null, null, null, null, null, null, null, null, extraWords);
            }
        } else {
            int inCommand = checkForCommand(command, commands);
            if (inCommand > 0) {
                return new ParserOutput(commands.get(inCommand), null, null, null, null, null, null, null, extraWords);
            }
            return null;
        }
    }
}
