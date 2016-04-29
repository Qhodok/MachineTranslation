/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.co.qhodok.nlp.MachineTranslationAPP;

import id.co.qhodok.nlp.MachineTranslation.MachineTranslation;
import java.io.File;

/**
 *
 * @author Andika Permana
 */
public class FormTesting extends javax.swing.JFrame {

    /**
     * Creates new form main
     */
    protected FormTraining trainingForm;
    protected MachineTranslation machineTranslation;

    public FormTesting() {
        initComponents();
        this.trainingForm = new FormTraining(this);
        if (new File(System.getProperty("user.home") + File.separator + ".machine_translation" + File.separator + "tm.ser").exists()
                &&new File(System.getProperty("user.home") + File.separator + ".machine_translation" + File.separator + "lm.ser").exists()
                && new File(System.getProperty("user.home") + File.separator + ".machine_translation" + File.separator + "ngram.dict").exists()) {
            this.machineTranslation = new MachineTranslation(System.getProperty("user.home") + File.separator + ".machine_translation");
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

        englishText = new javax.swing.JTextField();
        translate = new javax.swing.JButton();
        indoText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        trainingMenu = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        translate.setText("translate");
        translate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                translateActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel1.setText("ENGLISH - INDONESIA SBMT USING LG");

        jMenu1.setText("File");

        trainingMenu.setText("Buat Data SBMT");
        trainingMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trainingMenuActionPerformed(evt);
            }
        });
        jMenu1.add(trainingMenu);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(englishText, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(translate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(indoText, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(125, 125, 125))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(englishText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(translate)
                    .addComponent(indoText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void trainingMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trainingMenuActionPerformed
        trainingForm.show();
        this.setVisible(false);
    }//GEN-LAST:event_trainingMenuActionPerformed

    private void translateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_translateActionPerformed
        if (this.machineTranslation == null) {
            if (new File(System.getProperty("user.home") + File.separator + ".machine_translation" + File.separator + "tm.ser").exists()
                    && new File(System.getProperty("user.home") + File.separator + ".machine_translation" + File.separator + "lm.ser").exists()
                    && new File(System.getProperty("user.home") + File.separator + ".machine_translation" + File.separator + "ngram.dict").exists()) {
                this.machineTranslation = new MachineTranslation(System.getProperty("user.home") + File.separator + ".machine_translation");
                this.indoText.setText(this.machineTranslation.translation(this.englishText.getText(), true));
            }else{
                System.out.println("data tidak ada, silahkan training data terlebih dahulu");
            }
        } else {
            this.indoText.setText(this.machineTranslation.translation(this.englishText.getText(), true));
        }
    }//GEN-LAST:event_translateActionPerformed

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
            java.util.logging.Logger.getLogger(FormTesting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormTesting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormTesting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormTesting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormTesting().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField englishText;
    private javax.swing.JTextField indoText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem trainingMenu;
    private javax.swing.JButton translate;
    // End of variables declaration//GEN-END:variables
}
