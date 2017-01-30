/*
 * SelectPeriodPreprocessingFunctionPanel.java
 *
 * Created on 03-abr-2011, 19:08:32
 */
package scimat.gui.components.wizard.Analysis;

/**
 *
 * @author mjcobo
 */
public class SelectPeriodDataReductionMethodPanel extends javax.swing.JPanel {

  /** Creates new form SelectPeriodPreprocessingFunctionPanel */
  public SelectPeriodDataReductionMethodPanel() {
    initComponents();
  }

  /**
   * 
   */
  public void refresh() {
    
    this.frequencyCheckBox.setSelected(false);
    this.minFrequencySpinner.setEnabled(false);
    this.minFrequencySpinner.setValue(1);
  }

  /**
   *
   * @return
   */
  public boolean isFrequencyDataReductionSelected() {

    return this.frequencyCheckBox.isSelected();
  }

  /**
   *
   * @return
   */
  public int getMinFrequencyDataReductionSelected() {

    return (Integer)this.minFrequencySpinner.getModel().getValue();
  }

  /**
   * 
   * @param title
   */
  public void setTitleToMainPanel(String title) {

    this.setBorder(javax.swing.BorderFactory.createTitledBorder(title));
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    dataReductionPanel = new javax.swing.JPanel();
    frequencyCheckBox = new javax.swing.JCheckBox();
    minFrequencyDescriptionLabel = new javax.swing.JLabel();
    minFrequencySpinner = new javax.swing.JSpinner();

    jLabel1.setText("jLabel1");

    dataReductionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Data reduction"));

    frequencyCheckBox.setText("Frequency reduction");
    frequencyCheckBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        frequencyCheckBoxActionPerformed(evt);
      }
    });

    minFrequencyDescriptionLabel.setText("Minimun frequency:");

    minFrequencySpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
    minFrequencySpinner.setEnabled(false);

    javax.swing.GroupLayout dataReductionPanelLayout = new javax.swing.GroupLayout(dataReductionPanel);
    dataReductionPanel.setLayout(dataReductionPanelLayout);
    dataReductionPanelLayout.setHorizontalGroup(
      dataReductionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(dataReductionPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addGroup(dataReductionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(dataReductionPanelLayout.createSequentialGroup()
            .addGap(21, 21, 21)
            .addComponent(minFrequencyDescriptionLabel)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(minFrequencySpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE))
          .addComponent(frequencyCheckBox))
        .addContainerGap())
    );
    dataReductionPanelLayout.setVerticalGroup(
      dataReductionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(dataReductionPanelLayout.createSequentialGroup()
        .addComponent(frequencyCheckBox)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(dataReductionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(minFrequencyDescriptionLabel)
          .addComponent(minFrequencySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(dataReductionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(dataReductionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
    );
  }// </editor-fold>//GEN-END:initComponents

  /**
   * 
   * @param evt
   */
  private void frequencyCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frequencyCheckBoxActionPerformed

    if (this.frequencyCheckBox.isSelected()) {

      this.minFrequencySpinner.setEnabled(true);

    } else {

      this.minFrequencySpinner.setEnabled(false);
    }
  }//GEN-LAST:event_frequencyCheckBoxActionPerformed

  /**
   * 
   * @param evt
   */
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel dataReductionPanel;
  private javax.swing.JCheckBox frequencyCheckBox;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel minFrequencyDescriptionLabel;
  private javax.swing.JSpinner minFrequencySpinner;
  // End of variables declaration//GEN-END:variables
}
