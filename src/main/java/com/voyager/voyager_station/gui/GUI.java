/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.voyager.voyager_station.gui;

import com.voyager.voyager_station.gui.GUI.ObjectPressedAction;
import com.voyager.voyager_station.adventure.Utils;
import com.voyager.voyager_station.game.Parameters.InterfaceCommandNames;
import com.voyager.voyager_station.game.Parameters.InterfaceObjectParameters;
import com.voyager.voyager_station.game.Parameters.ItalianCommandNames;
import com.voyager.voyager_station.game.Parameters.ItalianNPCParameters;
import com.voyager.voyager_station.game.Parameters.ItalianObjectParameters;
import com.voyager.voyager_station.game.VoyagerStationGame;
import com.voyager.voyager_station.parser.Parser;
import com.voyager.voyager_station.parser.ParserOutput;
import com.voyager.voyager_station.socket.ClientGUI;
import com.voyager.voyager_station.socket.Server;
import com.voyager.voyager_station.sound.CostantsSoundPath;
import com.voyager.voyager_station.sound.SoundGame;
import com.voyager.voyager_station.sound.SoundGame.BackgroundSound;
import com.voyager.voyager_station.type.AdvEnemy;
import com.voyager.voyager_station.type.AdvNPC;
import com.voyager.voyager_station.type.AdvObject;
import com.voyager.voyager_station.type.AdvObjectContainer;
import com.voyager.voyager_station.type.CommandType;
import com.voyager.voyager_station.type.Room;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Roberto Maffucci
 */
public class GUI extends javax.swing.JFrame {

    private static ImageIcon currentImage;
    private final static String IMG_PATH = "./resources/img/";
    private final static String OBJ_PATH = "./resources/img/objects/";
    private static boolean stopAnimationImage = false;
    private static VoyagerStationGame game = new VoyagerStationGame(new ItalianCommandNames(), new ItalianObjectParameters());
    private static Room currentRoom;
    private Parser parser;
    private InventoryGUI inventory;
    private ComputerGUI computerGui = null;
    ContainerObjectGUI container = new ContainerObjectGUI(this);
    private KeypadGUI keypadGUI;
    private static Map<javax.swing.JButton, Room> objectRoom;
    private SoundGame soundGame = new SoundGame();
    private int counter;
    private Timer tm;
    private ClientGUI serverSocket;
    private Server server;
    private QuitGameThread quitGameThread;
    private InterfaceObjectParameters objNames = game.getObjNames();

    /**
     * Creates new form GUI
     */
    public GUI() {
        this.keypadGUI = new KeypadGUI();
        try {
            game.init();
        } catch (Exception ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Set<String> stopwords = Utils.loadFileListInSet(new File("./resources/stopwords"));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        initComponents();
        init();
    }

    private void init() {
        inventory = new InventoryGUI(this);
        inventory.setVisible(false);
        inventory.pack();
        backpack.setIcon(new ImageIcon(IMG_PATH + "backpack.png"));
        objectRoom = new HashMap<>();
        try {
            currentImage = new ImageIcon(IMG_PATH + "voyager_station.jpg");
            lblBackground.setIcon(currentImage);
            lblCommand.setVisible(false);
            btnCommand.setVisible(false);
            txtCommand.setVisible(false);
            btnArrowUp.setIcon(new ImageIcon(IMG_PATH + "icons/" + "arrow_up2.png"));
            btnArrowDown.setIcon(new ImageIcon(IMG_PATH + "icons/" + "arrow_down2.png"));
            btnArrowLeft.setIcon(new ImageIcon(IMG_PATH + "icons/" + "arrow_left2.png"));
            btnArrowRight.setIcon(new ImageIcon(IMG_PATH + "icons/" + "arrow_right2.png"));
        } catch (Exception e) {
            System.out.println("Errore: " + e);
        }
        if (boxMusic.isSelected()) {
            soundGame.play(CostantsSoundPath.MENU);
        }
        game.setBackgroundSound(BackgroundSound.MENU);

        /* Setting visible objects */
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        objectsPositions.setVisible(false);
        giornale.setVisible(false);
        torcia.setVisible(false);
        emergencykit.setVisible(false);
        weaponsBag.setVisible(false);
        alien1.setVisible(false);
        dossier.setVisible(false);
        cmdManual.setVisible(false);
        jmiSave.setEnabled(false);

        /* Settings Actions */
        ObjectPressedAction objAction = new ObjectPressedAction();
        torcia.addActionListener(objAction);
        giornale.addActionListener(objAction);
        emergencykit.addActionListener(objAction);
        weaponsBag.addActionListener(objAction);
        spacesuit.addActionListener(objAction);
        dossier.addActionListener(objAction);
        cmdManual.addActionListener(objAction);
        alien1.addActionListener(objAction);
        alien2.addActionListener(objAction);
        CommandAction cmdAction = new CommandAction();
        txtCommand.addActionListener(cmdAction);
        btnCommand.addActionListener(cmdAction);
        ArrowAction arwAction = new ArrowAction();
        btnArrowUp.addActionListener(arwAction);
        btnArrowDown.addActionListener(arwAction);
        btnArrowLeft.addActionListener(arwAction);
        btnArrowRight.addActionListener(arwAction);
        ExitAction extAction = new ExitAction();
        btnExit.addActionListener(extAction);
        jmiExit.addActionListener(extAction);

        setObjNames();
        initObjectRoom();
    }

    private void setObjNames() {
        ItalianNPCParameters npc = new ItalianNPCParameters();
        torcia.setName(objNames.torchName());
        giornale.setName(objNames.journalName());
        emergencykit.setName(objNames.emergencyKitName());
        weaponsBag.setName(objNames.weaponsBagName());
        spacesuit.setName(objNames.suiteName());
        cmdManual.setName(objNames.commandManualName());
        dossier.setName(objNames.dossierName());
        alien1.setName(npc.alien1Name());
        alien2.setName(npc.alien2Name());
    }

    public VoyagerStationGame getGame() {
        return game;
    }

    public InventoryGUI getInventory() {
        return inventory;
    }

    private void setObjImg() {
        for (Map.Entry<JButton, Room> entry : objectRoom.entrySet()) {
            for (AdvObject o : entry.getValue().getObjects()) {
                if (entry.getKey().getName().equals(o.getName())) {
                    entry.getKey().setIcon(new ImageIcon(OBJ_PATH + o.getImgObjRoom()));
                }
            }
            if (!entry.getValue().getNPCs().isEmpty()) {
                for (AdvNPC e : entry.getValue().getNPCs()) {
                    if (e instanceof AdvEnemy && entry.getKey().getName().equals(e.getName())) {
                        entry.getKey().setIcon(new ImageIcon(OBJ_PATH + e.getImgObjRoom()));
                    }
                }
            }
        }
    }

    private void changeGIFtoImage(final int duration, ImageIcon image, boolean end) {

        while (stopAnimationImage == false) {
            //do nothing
            System.out.print("");
        }

        if (stopAnimationImage) {
            long timeStart = System.currentTimeMillis();
            int i = 0;
            while ((System.currentTimeMillis() - timeStart) / 1000 <= duration) {
            }
            currentImage = image;
            lblBackground.setIcon(currentImage);
            stopAnimationImage = false;
        }

        if (!end) {
            setCurrentRoom();
            objectsPositions.setVisible(true);
            enableCommands(true, true, true);
            jmiSave.setEnabled(true);
        } else {
            txtArea.setText("Grazie per aver giocato!");
            lblBackground.setIcon(currentImage);
        }
    }

    private static void setCurrentRoom() {
        if (game.getCurrentRoom().isVisible()) {
            currentImage = new ImageIcon(IMG_PATH + game.getCurrentRoom().getImgNameRoom());
        } else {
            currentImage = new ImageIcon(IMG_PATH + "unvisibleRoom.jpg");
        }
        lblBackground.setIcon(currentImage);
        txtArea.setText(game.getCurrentRoom().getDescription());
        currentRoom = game.getCurrentRoom();
        printObjectRoom(true, currentRoom);
    }

    private static void printObjectRoom(boolean print, Room room) {
        for (Map.Entry<JButton, Room> entry : objectRoom.entrySet()) {
            for (AdvObject o : entry.getValue().getObjects()) {
                if (entry.getKey().getName().equals(o.getName()) && room.equals(entry.getValue())) {
                    if (entry.getValue().isVisible()) {
                        entry.getKey().setVisible(print);
                    }
                }
            }
            if (!entry.getValue().getNPCs().isEmpty()) {
                for (AdvNPC e : entry.getValue().getNPCs()) {
                    if (e instanceof AdvEnemy && entry.getKey().getName().equals(e.getName())
                            && room.equals(entry.getValue())) {
                        entry.getKey().setVisible(print);
                    }
                }
            }
        }
    }

    private void removeObjectRoom(AdvObject obj) {
        for (Map.Entry<JButton, Room> entry : objectRoom.entrySet()) {
            if (entry.getKey().getName().equals(obj.getName())) {
                entry.getValue().getObjects().remove(obj);
                Room modified = entry.getValue();
                entry.getKey().setVisible(false);
                objectRoom.replace(entry.getKey(), modified);
                break;
            }
        }
    }

    private void initObjectRoom() {
        for (Room r : game.getRooms()) {
            for (AdvObject o : r.getObjects()) {
                if (torcia.getName().equals(o.getName())) {
                    objectRoom.put(torcia, r);
                } else if (giornale.getName().equals(o.getName())) {
                    objectRoom.put(giornale, r);
                } else if (emergencykit.getName().equals(o.getName())) {
                    objectRoom.put(emergencykit, r);
                } else if (weaponsBag.getName().equals(o.getName())) {
                    objectRoom.put(weaponsBag, r);
                } else if (dossier.getName().equals(o.getName())) {
                    objectRoom.put(dossier, r);
                } else if (cmdManual.getName().equals(o.getName())) {
                    objectRoom.put(cmdManual, r);
                } else if (spacesuit.getName().equals(o.getName())) {
                    objectRoom.put(spacesuit, r);
                }
            }
            if (!r.getNPCs().isEmpty()) {
                for (AdvNPC e : r.getNPCs()) {
                    if (alien1.getName().equals(e.getName())) {
                        objectRoom.put(alien1, r);
                    } else if (alien2.getName().equals(e.getName())) {
                        objectRoom.put(alien2, r);
                    }
                }
            }
        }
        setObjImg();
        objectRoom.entrySet().stream()
                .forEach(entry -> entry.getKey().setVisible(false));
    }

    private void enableCommands(boolean command, boolean movement, boolean visible) {
        btnCommand.setEnabled(command);
        txtCommand.setEnabled(command);
        backpack.setEnabled(command);
        btnArrowUp.setEnabled(movement);
        btnArrowDown.setEnabled(movement);
        btnArrowLeft.setEnabled(movement);
        btnArrowRight.setEnabled(movement);
        btnCommand.setVisible(visible);
        txtCommand.setVisible(visible);
        lblCommand.setVisible(visible);
        backpack.setVisible(visible);
        btnArrowUp.setVisible(visible);
        btnArrowDown.setVisible(visible);
        btnArrowLeft.setVisible(visible);
        btnArrowRight.setVisible(visible);
    }

    private void printText(String text, boolean inGame) {
        int i;
        char[] toCharArray = text.toCharArray();
        txtArea.setText("");
        counter = -1;
        SoundGame soundText = new SoundGame();
        if (boxSound.isSelected()) {
            soundText.play(CostantsSoundPath.TEXT);
        }
        ActionListener taskPerformer = (ActionEvent evt) -> {
            counter++;
            if (counter >= text.length()) {
                counter = 0;
                tm.stop();
                if (soundText.isReproducing()) {
                    soundText.stop();
                }
                if (inGame) {
                    enableCommands(true, true, true);
                }
            } else {
                StringBuilder supp = new StringBuilder();
                supp.append(text.charAt(counter));
                txtArea.append(supp.toString());
                supp = new StringBuilder();
            }
        };
        int delay = 30;
        tm = new Timer(delay, taskPerformer);
        tm.start();
    }

    public void executeCommand(String command) {
        boolean wasVisible = game.getCurrentRoom().isVisible();
        try {
            if (command != null) {
                String textReturned = null;
                ParserOutput p = parser.parse(command, game.getCommands(), game.getCurrentRoom().getObjects(), game.getInventory().getList(), game.getCurrentRoom().getNPCs());
                if (p.getCommand() != null && p.getCommand().getType() == CommandType.SLEEP) {
                    if (!game.isHasSleep() && game.getCurrentRoom().getId() == 1) {
                        if (soundGame.isStopped() == false) {
                            soundGame.stop();
                            soundGame.play(CostantsSoundPath.BACKGROUND_MUSIC);
                        } else {
                            soundGame.play(CostantsSoundPath.BACKGROUND_MUSIC);
                            soundGame.stop();
                        }
                        game.setBackgroundSound(BackgroundSound.STABLE);
                    }
                }

                if (p.getCommand() != null && p.getCommand().getType() == CommandType.END) {
                    txtArea.setText("Grazie per aver giocato!");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.exit(0);
                } else if (p.getCommand() != null && p.getCommand().getType() == CommandType.INVENTORY) {
                    inventory.setVisible(true);
                } else if (p.getCommand() != null) {
                    textReturned = game.nextMove(p);
                    if (p.getCommand() != null && p.getCommand().getType() == CommandType.PICK_UP && p.getObject1() != null && p.getObject1().isPickupable()) {
                        if (!game.getCurrentRoom().getObjects().contains(p.getObject1())) {
                            inventory.setItemsInvetory(p.getObject1());
                            removeObjectRoom(p.getObject1());
                            txtArea.setText(textReturned);
                        }
                    }

                    /* This control if an item is removed from an object container */
                    if (p.getCommand() != null && p.getCommand().getType() == CommandType.PICK_UP) {
                        if (p.getObject1() == null && p.getInvObject1() == null) {
                            for (AdvObject o : game.getInventory().getList()) {
                                if (o.getName().equals(p.getExtraWords())) {
                                    inventory.setItemsInvetory(o);
                                    removeObjectRoom(o);
                                    txtArea.setText(textReturned);
                                    break;
                                }
                            }
                        }
                    }

                    if (p.getCommand() != null && p.getCommand().getType() == CommandType.PLUNDER) {
                        if (p.getObject1() == null && p.getInvObject1() == null) {
                            for (AdvNPC n : game.getCurrentRoom().getNPCs()) {
                                for (AdvObject o : n.getInventory().getList()) {
                                    if (o.getName().equals(p.getExtraWords())) {
                                        inventory.setItemsInvetory(o);
                                        removeObjectRoom(o);
                                        txtArea.setText(textReturned);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (p.getCommand() != null
                            && (p.getCommand().getType() == CommandType.OPEN || p.getCommand().getType() == CommandType.PLUNDER)) {
                        if (p.getObject1() != null || p.getCommand2() != null || p.getNpc1() != null) {
                            if ((p.getCommand2() != null
                                    && p.getCommand2().getType() == CommandType.INVENTORY
                                    && p.getNpc1() != null) || p.getNpc1() != null) {
                                if (!p.getNpc1().isAlive()) {
                                    container.setVisible(true);
                                    container.init(p.getNpc1().getInventory(), p.getNpc1(), game.getInventory());
                                }
                            } else if (!p.getObject1().getName().equals(objNames.dossierName()) && !p.getObject1().getName().equals(objNames.commandManualName())) {
                                container.setVisible(true);
                                AdvObjectContainer objContainer = (AdvObjectContainer) p.getObject1();
                                container.init(objContainer, game.getInventory());
                            }
                        }
                    }

                    if (p.getCommand() != null && p.getCommand().getType() == CommandType.WEST
                            || p.getCommand().getType() == CommandType.EAST
                            || p.getCommand().getType() == CommandType.SOUTH
                            || p.getCommand().getType() == CommandType.NORD) {

                        if (game.getCurrentRoom() != currentRoom) {
                            Room toHide = currentRoom;
                            printObjectRoom(false, toHide);
                            setCurrentRoom();
                            printObjectRoom(true, game.getCurrentRoom());
                            if (game.getCurrentRoom().getId() == 20) {
                                game.setBackgroundSound(BackgroundSound.FINAL);
                                if (soundGame.isReproducing()) {
                                    soundGame.stop();
                                }
                                soundGame.play(CostantsSoundPath.FINAL_SCENE);
                                if (!boxMusic.isSelected()) {
                                    soundGame.stop();
                                }
                                server = new Server();
                                server.start();
                                serverSocket = new ClientGUI();
                                enableCommands(true, false, true);
                            }
                        } else {
                            InterfaceCommandNames cmdNames = game.getCommandNames();
                            StringBuilder cmd = new StringBuilder();
                            cmd.append(cmdNames.unlock()).append(" ");
                            if (game.getCurrentRoom().getWest() != null && p.getCommand().getType() == CommandType.WEST) {
                                if (game.getCurrentRoom().getWest().isLocked()) {
                                    keypadGUI = new KeypadGUI(this, game.getCurrentRoom().getWest(), cmd.append(cmdNames.west()).append(" ").toString());
                                    keypadGUI.setVisible(true);
                                }
                            }
                            if (game.getCurrentRoom().getEast() != null && p.getCommand().getType() == CommandType.EAST) {
                                if (game.getCurrentRoom().getEast().isLocked()) {
                                    keypadGUI = new KeypadGUI(this, game.getCurrentRoom().getEast(), cmd.append(cmdNames.east()).append(" ").toString());
                                    keypadGUI.setVisible(true);
                                }
                            }
                            if (game.getCurrentRoom().getNorth() != null && p.getCommand().getType() == CommandType.NORD) {
                                if (game.getCurrentRoom().getNorth().isLocked()) {
                                    if (!game.getCurrentRoom().getNorth().getName().equals("warehouse")) {
                                        keypadGUI = new KeypadGUI(this, game.getCurrentRoom().getNorth(), cmd.append(cmdNames.north()).append(" ").toString());
                                        keypadGUI.setVisible(true);
                                    }
                                }
                            }
                            if (game.getCurrentRoom().getSouth() != null && p.getCommand().getType() == CommandType.SOUTH) {
                                if (game.getCurrentRoom().getSouth().isLocked()) {
                                    keypadGUI = new KeypadGUI(this, game.getCurrentRoom().getSouth(), cmd.append(cmdNames.south()).append(" ").toString());
                                    keypadGUI.setVisible(true);
                                }
                            }
                        }

                    }

                    if (wasVisible != game.getCurrentRoom().isVisible()) {
                        setCurrentRoom();
                    }

                    if (p.getCommand().getType() == CommandType.USE) {
                        if (p.getObject1() != null) {
                            if (p.getObject1().getName().equals(objNames.computerName())) {
                                computerGui = new ComputerGUI(this);
                                computerGui.setVisible(true);
                                txtArea.setText("Questo computer ti sembra surreale, vorresti averne uno a casa tua...");
                            } else if (p.getObject1().getName().equals(objNames.serverName())) {
                                serverSocket.setVisible(true);
                                quitGameThread = new QuitGameThread();
                                quitGameThread.start();
                                enableCommands(true, false, true);
                                textReturned = "Hai stabilito la comunicazione con il server!";
                            }
                        }
                    }
                    if (p.getCommand().getType() != CommandType.TALK_TO) {
                        txtArea.setText(textReturned);
                    } else {
                        enableCommands(false, false, true);
                        printText(textReturned, true);
                    }
                } else {
                    txtArea.setText("Non sono sicuro di aver capito cosa fare.");
                }
                setObjImg();
                printObjectRoom(true, game.getCurrentRoom());
                refresh();
            }
        } catch (NoSuchFieldException ex) {
            System.out.println("EX: " + ex.getMessage());
        }
        txtCommand.setText("");
    }

    private void refresh() {
        for (Map.Entry<JButton, Room> entry : objectRoom.entrySet()) {
            for (Room r : game.getRooms()) {
                if (entry.getValue().equals(r)) {
                    objectRoom.replace(entry.getKey(), r);
                }
            }
        }
    }

    protected class CommandAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            executeCommand(txtCommand.getText());
        }
    }

    protected class ObjectPressedAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String typeCommand = null;
            String cmd = null;
            for (Map.Entry<JButton, Room> entry : objectRoom.entrySet()) {
                if (entry.getKey().getName().equals(button.getName())) {
                    for (AdvObject o : entry.getValue().getObjects()) {
                        if (button.getName().equals(o.getName()) && o instanceof AdvObjectContainer) {
                            typeCommand = game.getCommandNames().open() + " ";
                            break;
                        }
                    }
                    if (!entry.getValue().getNPCs().isEmpty()) {
                        for (AdvNPC enemy : entry.getValue().getNPCs()) {
                            if (button.getName().equals(enemy.getName()) && enemy instanceof AdvEnemy) {
                                typeCommand = game.getCommandNames().attack() + " ";
                                break;
                            }
                        }
                    }
                    if (typeCommand != null) {
                        cmd = typeCommand + entry.getKey().getName();
                    } else {
                        cmd = game.getCommandNames().pickUp() + " " + entry.getKey().getName();
                    }
                    if (button.getName().equals(objNames.dossierName())
                            || button.getName().equals(objNames.commandManualName())) {
                        cmd = game.getCommandNames().open() + " " + button.getName();
                    }
                    executeCommand(cmd);
                    break;
                }
            }
        }
    }

    protected class ArrowAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String command = null;
            switch (button.getName()) {
                case "arrowUp":
                    command = game.getCommandNames().north();
                    break;
                case "arrowDown":
                    command = game.getCommandNames().south();
                    break;
                case "arrowLeft":
                    command = game.getCommandNames().west();
                    break;
                default:
                    command = game.getCommandNames().east();
                    break;
            }
            executeCommand(command);
        }
    }

    protected class ExitAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            int choice = JOptionPane.showConfirmDialog(null, "Vuoi davvero uscire?");
            if (choice == 0) {
                System.exit(0);
            }
        }
    }

    protected class QuitGameThread extends Thread {

        boolean run = true;

        private QuitGameThread() {

        }

        @Override
        public void run() {
            while (run) {
                if (serverSocket.isDone()) {
                    run = false;
                    serverSocket.dispose();
                } else {
                    //do nothing
                    System.out.print("");
                }
            }
            enableCommands(false, false, false);
            printText("Sei riuscito a tornare sulla terra! Uno dei rientri più terrificanti che"
                    + "tu abbia mai fatto, ma ci sei riuscito! Sei stato isolato in un centro della NASA,"
                    + "che ha provveduto a bombardare la stazione Voyager!", false);
            printObjectRoom(false, game.getCurrentRoom());
            currentImage = new ImageIcon(IMG_PATH + "finalScene.gif");
            lblBackground.setIcon(currentImage);
            currentImage = new ImageIcon(IMG_PATH + "end.jpg");
            stopAnimationImage = true;
            jmiSave.setVisible(false);
            PlayGIF playGif = new PlayGIF();
            playGif.setDuration(22);
            playGif.setEndGame(true);
            playGif.start();
            while (!playGif.isInterrupted()) {
                if (playGif.isInterrupted()) {
                    this.interrupt();
                }
            }
        }
    }

    protected class PlayGIF extends Thread {

        private boolean endGame = false;

        private int duration = 0;

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void setEndGame(boolean endGame) {
            this.endGame = endGame;
        }

        @Override
        public void run() {
            if (stopAnimationImage) {
                changeGIFtoImage(duration, currentImage, endGame);
                stopAnimationImage = false;
            }
            this.interrupt();
        }
    }

    private void activateCommands() {
        objectsPositions.setVisible(true);
        btnStart.setVisible(false);
        btnExit.setVisible(false);
        btnArrowUp.setVisible(true);
        btnArrowUp.setEnabled(true);
        btnArrowDown.setVisible(true);
        btnArrowDown.setEnabled(true);
        btnArrowLeft.setVisible(true);
        btnArrowLeft.setEnabled(true);
        btnArrowRight.setVisible(true);
        btnArrowRight.setEnabled(true);
        btnCommand.setVisible(true);
        txtCommand.setVisible(true);
        lblCommand.setVisible(true);
        backpack.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCommand = new javax.swing.JButton();
        txtCommand = new javax.swing.JTextField();
        lblCommand = new javax.swing.JLabel();
        objectsPositions = new javax.swing.JLayeredPane();
        torcia = new javax.swing.JButton();
        giornale = new javax.swing.JButton();
        weaponsBag = new javax.swing.JButton();
        emergencykit = new javax.swing.JButton();
        btnArrowLeft = new javax.swing.JButton();
        btnArrowDown = new javax.swing.JButton();
        btnArrowRight = new javax.swing.JButton();
        btnArrowUp = new javax.swing.JButton();
        backpack = new javax.swing.JButton();
        alien1 = new javax.swing.JButton();
        dossier = new javax.swing.JButton();
        cmdManual = new javax.swing.JButton();
        spacesuit = new javax.swing.JButton();
        alien2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();
        btnStart = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jmFile = new javax.swing.JMenu();
        jmiOpen = new javax.swing.JMenuItem();
        jmiSave = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jmiExit = new javax.swing.JMenuItem();
        jmOption = new javax.swing.JMenu();
        boxMusic = new javax.swing.JCheckBoxMenuItem();
        boxSound = new javax.swing.JCheckBoxMenuItem();
        jmAiuto = new javax.swing.JMenu();
        jMenuCommands = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Voyager Station");
        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/img/icons/logo.png"));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCommand.setBackground(new java.awt.Color(51, 160, 243));
        btnCommand.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        btnCommand.setForeground(new java.awt.Color(255, 255, 255));
        btnCommand.setText("Esegui");
        btnCommand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCommand.setOpaque(false);
        getContentPane().add(btnCommand, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 490, 200, 40));

        txtCommand.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtCommand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCommandKeyPressed(evt);
            }
        });
        getContentPane().add(txtCommand, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 450, 200, 40));

        lblCommand.setFont(new java.awt.Font("Eras Light ITC", 2, 24)); // NOI18N
        lblCommand.setForeground(new java.awt.Color(255, 255, 255));
        lblCommand.setText("Comando:");
        getContentPane().add(lblCommand, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 418, 120, 40));

        objectsPositions.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        objectsPositions.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        torcia.setBorder(null);
        torcia.setBorderPainted(false);
        torcia.setContentAreaFilled(false);
        torcia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        torcia.setFocusable(false);
        torcia.setName(""); // NOI18N
        objectsPositions.add(torcia, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 320, 110, 70));

        giornale.setBorder(null);
        giornale.setBorderPainted(false);
        giornale.setContentAreaFilled(false);
        giornale.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        giornale.setFocusable(false);
        giornale.setName(""); // NOI18N
        objectsPositions.add(giornale, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 270, 110, 80));

        weaponsBag.setBorder(null);
        weaponsBag.setBorderPainted(false);
        weaponsBag.setContentAreaFilled(false);
        weaponsBag.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        weaponsBag.setFocusable(false);
        weaponsBag.setName(""); // NOI18N
        objectsPositions.add(weaponsBag, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 200, 120, 90));

        emergencykit.setBorder(null);
        emergencykit.setBorderPainted(false);
        emergencykit.setContentAreaFilled(false);
        emergencykit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        emergencykit.setFocusable(false);
        emergencykit.setName(""); // NOI18N
        objectsPositions.add(emergencykit, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 410, 90, 70));

        btnArrowLeft.setText("jButton1");
        btnArrowLeft.setBorder(null);
        btnArrowLeft.setBorderPainted(false);
        btnArrowLeft.setContentAreaFilled(false);
        btnArrowLeft.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnArrowLeft.setName("arrowLeft"); // NOI18N
        objectsPositions.add(btnArrowLeft, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 490, 50, 40));

        btnArrowDown.setBorder(null);
        btnArrowDown.setContentAreaFilled(false);
        btnArrowDown.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnArrowDown.setName("arrowDown"); // NOI18N
        objectsPositions.add(btnArrowDown, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 500, 40, 40));

        btnArrowRight.setBorder(null);
        btnArrowRight.setBorderPainted(false);
        btnArrowRight.setContentAreaFilled(false);
        btnArrowRight.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnArrowRight.setName("arrowRight"); // NOI18N
        objectsPositions.add(btnArrowRight, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 490, 50, 40));

        btnArrowUp.setBorder(null);
        btnArrowUp.setBorderPainted(false);
        btnArrowUp.setContentAreaFilled(false);
        btnArrowUp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnArrowUp.setName("arrowUp"); // NOI18N
        objectsPositions.add(btnArrowUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 450, 40, 40));

        backpack.setBackground(new java.awt.Color(0, 153, 255));
        backpack.setBorder(null);
        backpack.setBorderPainted(false);
        backpack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backpack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backpackActionPerformed(evt);
            }
        });
        objectsPositions.add(backpack, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 460, 70, 70));

        alien1.setBorderPainted(false);
        alien1.setContentAreaFilled(false);
        alien1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        alien1.setName(""); // NOI18N
        objectsPositions.add(alien1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 30, -1, -1));

        dossier.setBorderPainted(false);
        dossier.setContentAreaFilled(false);
        dossier.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        dossier.setName(""); // NOI18N
        objectsPositions.add(dossier, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 200, 90, 90));

        cmdManual.setBorderPainted(false);
        cmdManual.setContentAreaFilled(false);
        cmdManual.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        objectsPositions.add(cmdManual, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 330, 100, 70));

        spacesuit.setContentAreaFilled(false);
        spacesuit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        spacesuit.setName(""); // NOI18N
        objectsPositions.add(spacesuit, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 100, 230, 430));

        alien2.setContentAreaFilled(false);
        alien2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        alien2.setName(""); // NOI18N
        objectsPositions.add(alien2, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, 260, 380));

        getContentPane().add(objectsPositions, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, -2, 740, 570));

        jScrollPane2.setBorder(null);
        jScrollPane2.setFocusable(false);
        jScrollPane2.setRequestFocusEnabled(false);

        txtArea.setEditable(false);
        txtArea.setColumns(20);
        txtArea.setFont(new java.awt.Font("LuzSans-Book", 2, 24)); // NOI18N
        txtArea.setRows(5);
        txtArea.setText("Benvenuto in Voyager Station! Quella che doveva essere la vacanza più bella di sempre, si trasforma in un incubo senza precedenti.");
        txtArea.setToolTipText("");
        txtArea.setWrapStyleWord(true);
        txtArea.setAutoscrolls(false);
        txtArea.setBorder(null);
        txtArea.setDoubleBuffered(true);
        txtArea.setDragEnabled(false);
        txtArea.setRequestFocusEnabled(false);
        jScrollPane2.setViewportView(txtArea);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 0, 280, 570));

        btnStart.setBackground(new java.awt.Color(0, 0, 0));
        btnStart.setFont(new java.awt.Font("Impact", 0, 36)); // NOI18N
        btnStart.setForeground(new java.awt.Color(255, 255, 255));
        btnStart.setText("Avvia partita");
        btnStart.setBorder(null);
        btnStart.setBorderPainted(false);
        btnStart.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStart.setOpaque(false);
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        getContentPane().add(btnStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 400, 235, 51));

        btnExit.setBackground(new java.awt.Color(0, 0, 0));
        btnExit.setFont(new java.awt.Font("Impact", 0, 36)); // NOI18N
        btnExit.setForeground(new java.awt.Color(255, 255, 255));
        btnExit.setText("Esci");
        btnExit.setBorder(null);
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 470, 235, 51));
        getContentPane().add(lblBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, -1, 770, 570));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 570));
        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 140, 20));
        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 130, 20));

        jMenuBar1.setBackground(new java.awt.Color(102, 255, 102));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jmFile.setBackground(new java.awt.Color(255, 255, 255));
        jmFile.setText("File");
        jmFile.setToolTipText("");
        jmFile.setContentAreaFilled(false);

        jmiOpen.setText("Apri");
        jmiOpen.setToolTipText("");
        jmiOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiOpenActionPerformed(evt);
            }
        });
        jmFile.add(jmiOpen);

        jmiSave.setText("Salva");
        jmiSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSaveActionPerformed(evt);
            }
        });
        jmFile.add(jmiSave);
        jmFile.add(jSeparator1);

        jmiExit.setText("Esci");
        jmFile.add(jmiExit);

        jMenuBar1.add(jmFile);

        jmOption.setText("Opzioni");
        jmOption.setContentAreaFilled(false);

        boxMusic.setSelected(true);
        boxMusic.setText("Musica");
        boxMusic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxMusicActionPerformed(evt);
            }
        });
        jmOption.add(boxMusic);

        boxSound.setSelected(true);
        boxSound.setText("Suoni");
        boxSound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boxSoundActionPerformed(evt);
            }
        });
        jmOption.add(boxSound);

        jMenuBar1.add(jmOption);

        jmAiuto.setText("Aiuto");
        jmAiuto.setContentAreaFilled(false);

        jMenuCommands.setText("Comandi");
        jMenuCommands.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuCommandsActionPerformed(evt);
            }
        });
        jmAiuto.add(jMenuCommands);

        jMenuBar1.add(jmAiuto);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        try {
            game.init();
            currentImage = new ImageIcon(IMG_PATH + "loading.gif");
            lblBackground.setIcon(currentImage);
            btnStart.setVisible(false);
            btnExit.setVisible(false);
            stopAnimationImage = true;
            game.setBackgroundSound(BackgroundSound.PROLOGUE);
            if (!boxMusic.isSelected() || soundGame.isReproducing()) {
                soundGame.stop();
            }
            soundGame.play(CostantsSoundPath.PROLOGUE);
            if (!boxMusic.isSelected()) {
                soundGame.stop();
            }
            printText("Ti stai dirigendo verso il primo hotel spaziale mai creato!", false);
            PlayGIF playGif = new PlayGIF();
            playGif.setDuration(12);
            playGif.setEndGame(false);
            playGif.start();
        } catch (Exception ex) {
            Logger.getLogger(GUI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnStartActionPerformed

    private void boxMusicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxMusicActionPerformed
        if (!boxMusic.isSelected()) {
            soundGame.stop();
        } else {
            if (btnStart.isVisible()) {
                soundGame.play(CostantsSoundPath.MENU);
                game.setBackgroundSound(BackgroundSound.MENU);
            } else {
                soundGame.resume();
            }
        }
    }//GEN-LAST:event_boxMusicActionPerformed

    private void jmiOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiOpenActionPerformed
        JFileChooser chooser = new JFileChooser();
        File dir = new File("./resources/saves");
        chooser.setCurrentDirectory(dir);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                game = new VoyagerStationGame(new ItalianCommandNames(), new ItalianObjectParameters());
                game = (VoyagerStationGame) game.load(file.getName());
                /* This remove all the objects in the inventory from the relative room*/
                for (AdvObject o : game.getInventory().getList()) {
                    removeObjectRoom(o);
                }
                /* Update the modified rooms */
                for (Map.Entry<JButton, Room> entry : objectRoom.entrySet()) {
                    for (Room r : game.getRooms()) {
                        if (entry.getValue().equals(r)) {
                            objectRoom.replace(entry.getKey(), r);
                            entry.getKey().setVisible(false);
                        }
                    }
                }
                activateCommands();
                setCurrentRoom();
                game.checkForEnemySound();
                if (boxMusic.isSelected()) {
                    soundGame.stop();
                }

                /* This control to start server */
                if (game.getCurrentRoom().getName().equals("spaceship")) {
                    server = new Server();
                    server.start();
                    serverSocket = new ClientGUI();
                    enableCommands(true, false, true);
                }

                if (null != game.getBackgroundSound()) {
                    switch (game.getBackgroundSound()) {
                        case MENU:
                            soundGame.play(CostantsSoundPath.MENU);
                            break;
                        case PROLOGUE:
                            soundGame.play(CostantsSoundPath.PROLOGUE);
                            break;
                        case STABLE:
                            soundGame.play(CostantsSoundPath.BACKGROUND_MUSIC);
                            break;
                        case FINAL:
                            soundGame.play(CostantsSoundPath.FINAL_SCENE);
                        default:
                            break;
                    }
                    if (!boxMusic.isSelected()) {
                        soundGame.stop();
                    }
                }
                inventory = new InventoryGUI(this);
                inventory.loadInventory(game.getInventory());
                jmiSave.setEnabled(true);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Exception: " + e.getMessage());
            }
        }

    }//GEN-LAST:event_jmiOpenActionPerformed

    private void jMenuCommandsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuCommandsActionPerformed
        CommandList commandList = new CommandList();
        commandList.init(game);
        commandList.setVisible(true);
    }//GEN-LAST:event_jMenuCommandsActionPerformed

    private void backpackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backpackActionPerformed
        inventory.setVisible(true);
    }//GEN-LAST:event_backpackActionPerformed

    private void jmiSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiSaveActionPerformed
        try {
            DialogFileSaver fileName = new DialogFileSaver(this, true);
            fileName.setVisible(true);
            int choice = 0;
            while (!game.save(fileName.getFileName()) && fileName.getFileName() != null) {
                choice = JOptionPane.showConfirmDialog(null, "Vuoi sovrascrivere il file già esistente?");
                if (choice == 0) {
                    File remove = new File("./resources/saves/" + fileName.getFileName() + ".dat");
                    remove.delete();
                } else if (choice > 0) {
                    fileName = new DialogFileSaver(this, true);
                    fileName.setVisible(true);
                } else {
                    fileName.dispose();
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(GUI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jmiSaveActionPerformed

    private void txtCommandKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCommandKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            txtCommand.setText("");
        }
    }//GEN-LAST:event_txtCommandKeyPressed

    private void boxSoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boxSoundActionPerformed
        if (boxSound.isSelected()) {
            game.setSoundEnabled(true);
        } else {
            game.setSoundEnabled(false);
        }
    }//GEN-LAST:event_boxSoundActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton alien1;
    private javax.swing.JButton alien2;
    private javax.swing.JButton backpack;
    private javax.swing.JCheckBoxMenuItem boxMusic;
    private javax.swing.JCheckBoxMenuItem boxSound;
    private javax.swing.JButton btnArrowDown;
    private javax.swing.JButton btnArrowLeft;
    private javax.swing.JButton btnArrowRight;
    private javax.swing.JButton btnArrowUp;
    private static javax.swing.JButton btnCommand;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton cmdManual;
    private javax.swing.JButton dossier;
    private static javax.swing.JButton emergencykit;
    private javax.swing.JButton giornale;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuCommands;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JMenu jmAiuto;
    private javax.swing.JMenu jmFile;
    private javax.swing.JMenu jmOption;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiOpen;
    private javax.swing.JMenuItem jmiSave;
    private static javax.swing.JLabel lblBackground;
    private static javax.swing.JLabel lblCommand;
    private static javax.swing.JLayeredPane objectsPositions;
    private javax.swing.JButton spacesuit;
    private javax.swing.JButton torcia;
    private static javax.swing.JTextArea txtArea;
    private static javax.swing.JTextField txtCommand;
    private javax.swing.JButton weaponsBag;
    // End of variables declaration//GEN-END:variables
}
