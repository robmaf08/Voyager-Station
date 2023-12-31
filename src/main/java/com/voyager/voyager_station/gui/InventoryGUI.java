package com.voyager.voyager_station.gui;

import com.voyager.voyager_station.sound.CostantsSoundPath;
import com.voyager.voyager_station.sound.SoundGame;
import com.voyager.voyager_station.type.AdvObject;
import com.voyager.voyager_station.type.Inventory;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Roberto Maffucci
 */
public class InventoryGUI extends javax.swing.JFrame {

    private final static String IMG_PATH = "./resources/img/objects/icons/";
    private static GUI gui;
    private final Map<JButton, AdvObject> objectMap;

    /**
     * Creates new form InventoryGUI
     *
     * @param gui
     */
    public InventoryGUI(GUI gui) {
        this.objectMap = new HashMap<>();
        initComponents();
        InventoryGUI.gui = gui;
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);
    }

    public void loadInventory(Inventory inv) {
        for (AdvObject o : inv.getList()) {
            try {
                setItemsInvetory(o);
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(InventoryGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setItemsInvetory(AdvObject o) throws NoSuchFieldException {
        JButton temp = new JButton();
        if (jItem1.getIcon() == null) {
            temp = jItem1;
        } else if (jItem2.getIcon() == null) {
            temp = jItem2;
        } else if (jItem3.getIcon() == null) {
            temp = jItem3;
        } else if (jItem4.getIcon() == null) {
            temp = jItem4;
        } else if (jItem5.getIcon() == null) {
            temp = jItem5;
        } else if (jItem6.getIcon() == null) {
            temp = jItem6;
        } else if (jItem7.getIcon() == null) {
            temp = jItem7;
        } else if (jItem8.getIcon() == null) {
            temp = jItem8;
        }

        temp.setIcon(new ImageIcon(IMG_PATH + o.getImgObjRoom()));
        temp.setName(o.getName());
        temp.addActionListener(new PrintDescriptionAction());
        objectMap.put(temp, o);
    }

    private static <X, Y> void processElement(Set<X> source, Predicate<X> tester,
            Function<X, Y> mapper, Consumer<Y> block) {
        for (X p : source) {
            if (tester.test(p)) {
                Y data = mapper.apply(p);
                block.accept(data);
                break;
            }
        }
    }

    public void removeItem(AdvObject o) {
        processElement(objectMap.keySet(),
                p -> p.getName().equals(o.getName()),
                p -> p,
                key -> {
                    key.setIcon(null);
                    gui.getGame().getInventory().remove(o);
                    objectMap.remove(key);
                });
    }

    protected class PrintDescriptionAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            processElement(objectMap.keySet(),
                    p -> p.equals(e.getSource()),
                    p -> objectMap.get(p),
                    key -> jTextArea1.setText(key.getDescription()));
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topI = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        backgroundItems = new javax.swing.JPanel();
        jItem5 = new javax.swing.JButton();
        jItem6 = new javax.swing.JButton();
        jItem7 = new javax.swing.JButton();
        jItem8 = new javax.swing.JButton();
        row2 = new javax.swing.JPanel();
        row1 = new javax.swing.JPanel();
        jItem4 = new javax.swing.JButton();
        jItem1 = new javax.swing.JButton();
        jItem2 = new javax.swing.JButton();
        jItem3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Inventario");
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        topI.setBackground(new java.awt.Color(0, 0, 0));
        topI.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Inventario");
        topI.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 36));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Descrizione oggetto: ");
        topI.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, -1, 36));

        getContentPane().add(topI, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 807, 55));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 2, 24)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 50, 370, 190));

        backgroundItems.setBackground(new java.awt.Color(51, 137, 29));
        backgroundItems.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jItem5.setContentAreaFilled(false);
        jItem5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backgroundItems.add(jItem5, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, 80, 50));

        jItem6.setContentAreaFilled(false);
        jItem6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backgroundItems.add(jItem6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 90, 80, 50));

        jItem7.setContentAreaFilled(false);
        jItem7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backgroundItems.add(jItem7, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 80, 50));

        jItem8.setContentAreaFilled(false);
        jItem8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backgroundItems.add(jItem8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 80, 50));
        backgroundItems.add(row2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 410, 10));
        backgroundItems.add(row1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 410, 10));

        jItem4.setContentAreaFilled(false);
        jItem4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backgroundItems.add(jItem4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 80, 50));

        jItem1.setContentAreaFilled(false);
        jItem1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backgroundItems.add(jItem1, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 80, 50));

        jItem2.setContentAreaFilled(false);
        jItem2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backgroundItems.add(jItem2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 90, 50));

        jItem3.setContentAreaFilled(false);
        jItem3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backgroundItems.add(jItem3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 80, 50));

        getContentPane().add(backgroundItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 440, 190));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        jTextArea1.setText("");
        if (gui.getGame().isSoundEnabled()) {
            SoundGame sound = new SoundGame();
            sound.playOnce(CostantsSoundPath.BACKPACK_CLOSING, 1);
        }
    }//GEN-LAST:event_formWindowClosed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        if (gui.getGame().isSoundEnabled()) {
            SoundGame sound = new SoundGame();
            sound.playOnce(CostantsSoundPath.BACKPACK_OPENING, 1);
        }
    }//GEN-LAST:event_formComponentShown

    /**
     * @param args the command line arguments
     */
    public void main(String args[]) {
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
            java.util.logging.Logger.getLogger(InventoryGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InventoryGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InventoryGUI(gui).setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel backgroundItems;
    private javax.swing.JButton jItem1;
    private javax.swing.JButton jItem2;
    private javax.swing.JButton jItem3;
    private javax.swing.JButton jItem4;
    private javax.swing.JButton jItem5;
    private javax.swing.JButton jItem6;
    private javax.swing.JButton jItem7;
    private javax.swing.JButton jItem8;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel row1;
    private javax.swing.JPanel row2;
    private javax.swing.JPanel topI;
    // End of variables declaration//GEN-END:variables
}
