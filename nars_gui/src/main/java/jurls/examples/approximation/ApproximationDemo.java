/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jurls.examples.approximation;

import jurls.core.Expression;
import jurls.core.approximation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author thorsten
 */
public class ApproximationDemo extends javax.swing.JFrame {

    private final ApproxParameters approxParameters = new ApproxParameters(0.01, 0.1);
    private final RenderParameterizedFunction renderParameterizedFunction = new RenderParameterizedFunction(Color.yellow);

    private int numIterations = 0;
    private int numIterationsPerLoop = 1;

    /**
     * Creates new form ApproximationDemo
     */
    public ApproximationDemo() {
        initComponents();

        jComboBox1ActionPerformed(null);

        double[] ys = new double[10];
        for (int i = 0; i < ys.length; ++i) {
            ys[i] = Math.random() * (getHeight() - 10) + 5;
        }
        final RenderFunction f = new RenderArrayFunction(getWidth(), Color.blue, ys);

        functionRenderer1.renderFunctions.add(f);
        functionRenderer1.renderFunctions.add(renderParameterizedFunction);

        new Timer(5, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < numIterationsPerLoop; ++i) {
                    double x = Math.random() * getWidth();
                    renderParameterizedFunction.oneStepTowards(x, f.compute(x));
                    numIterations++;
                }
                functionRenderer1.repaint();
                jLabel1.setText("Iterations : " + numIterations);

                /*
                 StringBuilder sb = new StringBuilder();
                 for (Scalar p : fm.getParameters()) {
                 sb.append(p.getName() + " : " + p.getValue() + "\n");
                 }
                 jTextArea1.setText(sb.toString());
                 */
                jTextArea1.setText(
                        "No. parameters : "
                        + renderParameterizedFunction.getParameterizedFunction().numberOfParameters()
                );
            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        functionRenderer1 = new jurls.examples.approximation.FunctionRenderer();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        featuresComboBox = new javax.swing.JComboBox();
        alphaComboBox = new javax.swing.JComboBox();
        momentumComboBox = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        jPanel1.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout functionRenderer1Layout = new javax.swing.GroupLayout(functionRenderer1);
        functionRenderer1.setLayout(functionRenderer1Layout);
        functionRenderer1Layout.setHorizontalGroup(
            functionRenderer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 583, Short.MAX_VALUE)
        );
        functionRenderer1Layout.setVerticalGroup(
            functionRenderer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 253, Short.MAX_VALUE)
        );

        jPanel1.add(functionRenderer1, java.awt.BorderLayout.CENTER);

        jComboBox1.setMaximumRowCount(20);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Fourier Basis", "Radial Basis Functions Net", "Tanh Feed Forward Neural Net", "ATan Feed Forward Neural Net", "Logistic Sigmoid Forward Neural Net", "Parameterized Boolean Function", "Linear Function", "Genetic Function", "Weighted Interpolation Function" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Demo", jPanel1);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Debug 1", jPanel2);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("5000");
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton4);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("500");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("50");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton2);

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("1");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton3);

        jLabel1.setText("jLabel1");
        jPanel3.add(jLabel1);

        jPanel4.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(0, 1));

        featuresComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3", "5", "7", "24" }));
        featuresComboBox.setSelectedIndex(1);
        featuresComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                featuresComboBoxActionPerformed(evt);
            }
        });
        jPanel6.add(featuresComboBox);

        alphaComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.01", "0.001", "0.0001" }));
        alphaComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alphaComboBoxActionPerformed(evt);
            }
        });
        jPanel6.add(alphaComboBox);

        momentumComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "0.95" }));
        momentumComboBox.setSelectedIndex(1);
        momentumComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                momentumComboBoxActionPerformed(evt);
            }
        });
        jPanel6.add(momentumComboBox);

        jPanel5.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel7.setLayout(new java.awt.GridLayout(0, 1));

        jLabel2.setText("Features");
        jPanel7.add(jLabel2);

        jLabel3.setText("Learning Rate (Alpha)");
        jPanel7.add(jLabel3);

        jLabel4.setText("Momentum");
        jPanel7.add(jLabel4);

        jPanel5.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel4.add(jPanel5, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        approxParameters.setAlpha(Double.parseDouble(alphaComboBox.getSelectedItem().toString()));
        approxParameters.setMomentum(Double.parseDouble(momentumComboBox.getSelectedItem().toString()));

        ParameterizedFunction f = null;
        if (jComboBox1.getSelectedIndex() <= 6) {
            final int numFeatures = Integer.parseInt(featuresComboBox.getSelectedItem().toString());
            DiffableFunctionGenerator dfg = null;
            switch (jComboBox1.getSelectedIndex()) {
                case 0:
                    dfg = Generator.generateFourierBasis();
                    break;

                case 1:
                    dfg = Generator.generateRBFNet();
                    break;

                case 2:
                    dfg = Generator.generateTanhFFNN();
                    break;

                case 3:
                    dfg = Generator.generateATanFFNN();
                    break;

                case 4:
                    dfg = Generator.generateLogisticFFNN();
                    break;
                case 5:
                    dfg = Generator.generateFourierBasis();
                    f = new OutputNormalizer(
                            new InputNormalizer(
                                    new GeneticFitter(1, dfg, numFeatures)
                            )
                    );
                    break;
                case 6:
                    dfg = Generator.generateRBFNet();
                    f = new OutputNormalizer(
                            new InputNormalizer(
                                    new GeneticFitter(1, dfg, numFeatures)
                            )
                    );
                    break;                    
            }
            if (f==null) {
                f = new OutputNormalizer(
                        new InputNormalizer(
                                new GradientFitter(
                                        approxParameters,
                                        new DiffableFunctionMarshaller(dfg, 1, numFeatures)
                                )
                        )
                );

                /*if (f instanceof Functional) {
                    f = (ParameterizedFunction) Expression.optimize((Functional) f);
                }*/
            }

            Expression.print(f);

        } else {
            switch (jComboBox1.getSelectedIndex()) {
                case 7:
                    f = new OutputNormalizer(
                            new InputNormalizer(
                                    new CNFBooleanFunction(10, 10, 1)
                            )
                    );
                    break;
                case 8:
                    f = new OutputNormalizer(
                            new InputNormalizer(
                                    new LinearInterpolationFunction(1, 128)
                            )
                    );
                    break;
                case 9:
//                    f = new OutputNormalizer(
//                            new InputNormalizer(
//                                    new GeneticParameterizedFunction(1, 128, 50, 0.95f, 6, true, true, true, true)
//                            )
//                    );
                    break;
                case 10:
                    f = new OutputNormalizer(
                            new InputNormalizer(
                                    new WeightedInterpolationFunction(1, 20, 100))
                    );
                    break;
            }
        }
        renderParameterizedFunction.setParameterizedFunction(f);
        numIterations = 0;
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        numIterationsPerLoop = 1;
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        numIterationsPerLoop = 50;
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        numIterationsPerLoop = 500;
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        numIterationsPerLoop = 5000;
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void featuresComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_featuresComboBoxActionPerformed
        jComboBox1ActionPerformed(evt);
    }//GEN-LAST:event_featuresComboBoxActionPerformed

    private void alphaComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alphaComboBoxActionPerformed
        approxParameters.setAlpha(Double.parseDouble(alphaComboBox.getSelectedItem().toString()));
    }//GEN-LAST:event_alphaComboBoxActionPerformed

    private void momentumComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_momentumComboBoxActionPerformed
        approxParameters.setMomentum(Double.parseDouble(momentumComboBox.getSelectedItem().toString()));
    }//GEN-LAST:event_momentumComboBoxActionPerformed

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
            java.util.logging.Logger.getLogger(ApproximationDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ApproximationDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ApproximationDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ApproximationDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ApproximationDemo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox alphaComboBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox featuresComboBox;
    private jurls.examples.approximation.FunctionRenderer functionRenderer1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JComboBox momentumComboBox;
    // End of variables declaration//GEN-END:variables
}