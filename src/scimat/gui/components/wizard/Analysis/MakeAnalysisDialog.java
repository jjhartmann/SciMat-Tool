/*
 * MakeAnalysisDialog.java
 *
 * Created on 02-abr-2011, 17:17:02
 */
package scimat.gui.components.wizard.Analysis;

import scimat.analysis.KindOfMatrixEnum;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import scimat.analysis.ClusteringAlgorithmEnum;
import scimat.analysis.AnalysisConfiguration;
import scimat.analysis.UnitOfAnalysisEnum;
import scimat.gui.components.IncorrectDataObserver;
import scimat.model.knowledgebase.entity.Period;

/**
 *
 * @author mjcobo
 */
public class MakeAnalysisDialog extends javax.swing.JDialog implements IncorrectDataObserver {

  /** Creates new form MakeAnalysisDialog */
  public MakeAnalysisDialog(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();

    this.selectPeriodsPanel.addIncorrectDataObserver(this);
    this.selectUnitOfAnalysisPanel.addIncorrectDataObserver(this);
    this.selectMatrixBuilderPanel.addIncorrectDataObserver(this);
    this.selectSimilarityMeasurePanel.addIncorrectDataObserver(this);
    this.selectClusteringAlgorithmPanel.addIncorrectDataObserver(this);
    this.selectDocumentMappersPanel.addIncorrectDataObserver(this);
    this.selectCouplingDocumentMappersPanel.addIncorrectDataObserver(this);
    this.selectQuialityMeasuresPanel.addIncorrectDataObserver(this);
    this.selectLongitudinalMeasuresPanel.addIncorrectDataObserver(this);
  }

  /**
   * 
   */
  public void refresh() {

    step = __STEP_1;

    step1Label.setFont(boldFont);
    step1DescriptionLabel.setFont(boldFont);
    step2Label.setFont(null);
    step2DescriptionLabel.setFont(null);
    step3Label.setFont(null);
    step3DescriptionLabel.setFont(null);
    step4Label.setFont(null);
    step4DescriptionLabel.setFont(null);
    step5Label.setFont(null);
    step5DescriptionLabel.setFont(null);
    step6Label.setFont(null);
    step6DescriptionLabel.setFont(null);
    step7Label.setFont(null);
    step7DescriptionLabel.setFont(null);
    step8Label.setFont(null);
    step8DescriptionLabel.setFont(null);
    step9Label.setFont(null);
    step9DescriptionLabel.setFont(null);
    step10Label.setFont(null);
    step10escriptionLabel.setFont(null);
    step11Label.setFont(null);
    step11DescriptionLabel.setFont(null);

    backButton.setEnabled(false);
    nextButton.setEnabled(false);
    finishButton.setEnabled(false);
    cancelButton.setEnabled(true);

    messageErrorLabel.setVisible(false);

    this.selectPeriodsPanel.refresh();
    //this.selectUnitOfAnalysisPanel = new SelectUnitOfAnalysisPanel();
    this.selectUnitOfAnalysisPanel.refresh();
    this.selectMatrixBuilderPanel.refresh();
    this.selectDataReductionPerPeriod.refresh(null);
    //this.selectSimilarityMeasurePanel = new SelectSimilarityMeasurePanel();
    this.selectSimilarityMeasurePanel.refresh();
    this.selectNetworkReductionPerPeriod.refresh(null);
    this.selectClusteringAlgorithmPanel.refresh();
    this.selectDocumentMappersPanel.refresh();
    this.selectCouplingDocumentMappersPanel.refresh();
    this.selectQuialityMeasuresPanel.refresh();
    //this.selectLongitudinalMeasuresPanel = new SelectLongitudinalMeasuresPanel();
    this.selectLongitudinalMeasuresPanel.refresh();
    this.doExperimentPanel.refreh();
    
    
    
    
    currentStepDescriptionLabel.setText("Selecting the periods");    
    currentStepScrollPane.setViewportView(this.selectPeriodsPanel);
    currentStepScrollPane.validate();
   
    selectPeriodsPanel.fireIncorrectDataObservers();
  }

  /**
   * Se encarga de avanzar o retroceder un paso. Carga el panel que en cada
   * paso corresponda.
   *
   * @param forward true si tenemos que avanzar un paso y false en caso de que
   *                tengamos que retroceder.
   */
  private void changeStep(boolean forward) {

    if (forward) {
      step ++;
    } else {
      step --;
    }

    step1Label.setFont(null);
    step1DescriptionLabel.setFont(null);
    step2Label.setFont(null);
    step2DescriptionLabel.setFont(null);
    step3Label.setFont(null);
    step3DescriptionLabel.setFont(null);
    step4Label.setFont(null);
    step4DescriptionLabel.setFont(null);
    step5Label.setFont(null);
    step5DescriptionLabel.setFont(null);
    step6Label.setFont(null);
    step6DescriptionLabel.setFont(null);
    step7Label.setFont(null);
    step7DescriptionLabel.setFont(null);
    step8Label.setFont(null);
    step8DescriptionLabel.setFont(null);
    step9Label.setFont(null);
    step9DescriptionLabel.setFont(null);
    step10Label.setFont(null);
    step10escriptionLabel.setFont(null);
    step11Label.setFont(null);
    step11DescriptionLabel.setFont(null);

    //this.currentStepScrollPane.removeAll();
    
    //this.currentStepScrollPane.repaint();
    //this.currentStepScrollPane.revalidate();
    //this.currentStepPanel.repaint();
    //this.currentStepPanel.validate();
    
    if (step == __STEP_1) {

      backButton.setEnabled(false);

    } else {

      backButton.setEnabled(true);
    }

    switch (step) {

      case __STEP_1:
        currentStepDescriptionLabel.setText("Selecting the periods");
        currentStepScrollPane.setViewportView(this.selectPeriodsPanel);
        selectPeriodsPanel.fireIncorrectDataObservers();
        step1Label.setFont(boldFont);
        step1DescriptionLabel.setFont(boldFont);
        break;

      case __STEP_2:
        currentStepDescriptionLabel.setText("Selecting the units of analysis");
        //currentStepScrollPane.add(this.selectUnitOfAnalysisPanel);
        currentStepScrollPane.setViewportView(this.selectUnitOfAnalysisPanel);
        selectUnitOfAnalysisPanel.fireIncorrectDataObservers();
        step2Label.setFont(boldFont);
        step2DescriptionLabel.setFont(boldFont);
        break;

      case __STEP_3:
        currentStepDescriptionLabel.setText("Selecting the data redecution methods");
        //this.selectDataReductionPerPeriod.refresh(this.selectPeriodsPanel.getSelectedPeriods());
        currentStepScrollPane.setViewportView(this.selectDataReductionPerPeriod);
        this.incorrectData(true, "");
        step3Label.setFont(boldFont);
        step3DescriptionLabel.setFont(boldFont);

        break;

      case __STEP_4:
        currentStepDescriptionLabel.setText("Selecting the kind of matrix");
        currentStepScrollPane.setViewportView(this.selectMatrixBuilderPanel);
        selectMatrixBuilderPanel.fireIncorrectDataObservers();
        step4Label.setFont(boldFont);
        step4DescriptionLabel.setFont(boldFont);

        break;

      case __STEP_5:
        currentStepDescriptionLabel.setText("Selecting the network reduction methods");
        //this.selectNetworkReductionPerPeriod.refresh(this.selectPeriodsPanel.getSelectedPeriods());
        currentStepScrollPane.setViewportView(this.selectNetworkReductionPerPeriod);
        this.incorrectData(true, "");
        step5Label.setFont(boldFont);
        step5DescriptionLabel.setFont(boldFont);

        break;

      case __STEP_6:
        currentStepDescriptionLabel.setText("Selecting the normalization measure");
        currentStepScrollPane.setViewportView(this.selectSimilarityMeasurePanel);
        selectSimilarityMeasurePanel.fireIncorrectDataObservers();
        step6Label.setFont(boldFont);
        step6DescriptionLabel.setFont(boldFont);

        break;

      case __STEP_7:
        currentStepDescriptionLabel.setText("Selecting a clustering algorithm");
        currentStepScrollPane.setViewportView(this.selectClusteringAlgorithmPanel);
        selectClusteringAlgorithmPanel.fireIncorrectDataObservers();
        step7Label.setFont(boldFont);
        step7DescriptionLabel.setFont(boldFont);

        break;

      case __STEP_8:
        currentStepDescriptionLabel.setText("Selecting the document mappers");

        if (this.selectMatrixBuilderPanel.getSelectedKindOfMatrix().equals(KindOfMatrixEnum.BasicCoupling)) {

          currentStepScrollPane.setViewportView(this.selectCouplingDocumentMappersPanel);
          selectCouplingDocumentMappersPanel.fireIncorrectDataObservers();

        } else {

          currentStepScrollPane.setViewportView(this.selectDocumentMappersPanel);
          selectDocumentMappersPanel.fireIncorrectDataObservers();
        }
        
        step8Label.setFont(boldFont);
        step8DescriptionLabel.setFont(boldFont);

        break;

      case __STEP_9:
        currentStepDescriptionLabel.setText("Selecting the quality measures");
        currentStepScrollPane.setViewportView(this.selectQuialityMeasuresPanel);
        selectQuialityMeasuresPanel.fireIncorrectDataObservers();
        step9Label.setFont(boldFont);
        step9DescriptionLabel.setFont(boldFont);

        break;

      case __STEP_10:
        currentStepDescriptionLabel.setText("Selecting the measures for the longitudinal map");
        currentStepScrollPane.setViewportView(this.selectLongitudinalMeasuresPanel);
        selectLongitudinalMeasuresPanel.fireIncorrectDataObservers();
        step10Label.setFont(boldFont);
        step10escriptionLabel.setFont(boldFont);

        //backButton.setEnabled(true);
        nextButton.setEnabled(false);
        //finishButton.setEnabled(true);

        break;

      case __STEP_11:
        currentStepDescriptionLabel.setText("Executing the analysis");
        currentStepScrollPane.setViewportView(this.doExperimentPanel);
        step11Label.setFont(boldFont);
        step11DescriptionLabel.setFont(boldFont);

        backButton.setEnabled(false);
        nextButton.setEnabled(false);
        finishButton.setEnabled(false);

        break;
    }

    this.currentStepScrollPane.repaint();
    this.currentStepScrollPane.validate();
    this.currentStepPanel.repaint();
    this.currentStepPanel.validate();
  }

  /**
   * Cuando los datos introducidos por el usuario sean incorrectos el objeto
   * sera notificado a traves de este metodo.
   *
   * @param correct true si los datos son correctos.
   * @param message mensaje con informacion acerca de porque los datos son
   *                incorrectos.
   */
  public void incorrectData(boolean correct, String message) {

    // Si correct es false significa que los datos de entrada en el panel derecho
    // son incorrectos.
    if (! correct) {

      //_finishButton.setEnabled(false);
      //_nextButton.setEnabled(false);
      //messageErrorLabel.setForeground(Color.RED);
      messageErrorLabel.setText(message);
      messageErrorLabel.setForeground(Color.RED);
      finishButton.setEnabled(false);
      nextButton.setEnabled(false);
      messageErrorLabel.setVisible(true);

    } else {

      // Si estamos en el ultimo paso, activamos el boton Finish y desactivamos
      // el boton Next.
      if (step == __MAX_STEP) {

        finishButton.setEnabled(false);
        nextButton.setEnabled(false);

      } else if (step == (__MAX_STEP - 1)) {

        finishButton.setEnabled(true);
        nextButton.setEnabled(false);

      } else {

        // Si  no estamos en el ultimo paso, desactivamos el boton Finish y
        // activamos el boton Next.

        finishButton.setEnabled(false);
        nextButton.setEnabled(true);
        
        if (step == __STEP_1) {
        
          this.selectDataReductionPerPeriod.refresh(this.selectPeriodsPanel.getSelectedPeriods());
          this.selectNetworkReductionPerPeriod.refresh(this.selectPeriodsPanel.getSelectedPeriods());
      
        }
      }

      messageErrorLabel.setText("Correct arguments!");
      messageErrorLabel.setForeground(new Color(0, 99, 0));
    }
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stepsPanel = new javax.swing.JPanel();
        stepsTitleLabel = new javax.swing.JLabel();
        separator2 = new javax.swing.JSeparator();
        step1Label = new javax.swing.JLabel();
        step1DescriptionLabel = new javax.swing.JLabel();
        step2Label = new javax.swing.JLabel();
        step2DescriptionLabel = new javax.swing.JLabel();
        step3Label = new javax.swing.JLabel();
        step3DescriptionLabel = new javax.swing.JLabel();
        step4Label = new javax.swing.JLabel();
        step4DescriptionLabel = new javax.swing.JLabel();
        step5Label = new javax.swing.JLabel();
        step5DescriptionLabel = new javax.swing.JLabel();
        step6Label = new javax.swing.JLabel();
        step6DescriptionLabel = new javax.swing.JLabel();
        step7Label = new javax.swing.JLabel();
        step7DescriptionLabel = new javax.swing.JLabel();
        step8Label = new javax.swing.JLabel();
        step8DescriptionLabel = new javax.swing.JLabel();
        step9Label = new javax.swing.JLabel();
        step9DescriptionLabel = new javax.swing.JLabel();
        step10Label = new javax.swing.JLabel();
        step10escriptionLabel = new javax.swing.JLabel();
        step11Label = new javax.swing.JLabel();
        step11DescriptionLabel = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        currentStepPanel = new javax.swing.JPanel();
        currentStepDescriptionLabel = new javax.swing.JLabel();
        separator3 = new javax.swing.JSeparator();
        currentStepScrollPane = new javax.swing.JScrollPane();
        errorPanel = new javax.swing.JPanel();
        messageErrorLabel = new javax.swing.JLabel();
        separator1 = new javax.swing.JSeparator();
        backButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        finishButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Make analysis");

        stepsPanel.setBackground(new java.awt.Color(255, 254, 246));

        stepsTitleLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        stepsTitleLabel.setText("Steps");

        step1Label.setFont(new java.awt.Font("Tahoma", 1, 11));
        step1Label.setText("1.");

        step1DescriptionLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        step1DescriptionLabel.setText("Select periods");

        step2Label.setText("2.");

        step2DescriptionLabel.setText("Select unit of analysis");

        step3Label.setText("3.");

        step3DescriptionLabel.setText("Data reduction");

        step4Label.setText("4.");

        step4DescriptionLabel.setText("Select kind of matrix");

        step5Label.setText("5.");

        step5DescriptionLabel.setText("Network reduction");

        step6Label.setText("6.");

        step6DescriptionLabel.setText("Normalization");

        step7Label.setText("7.");

        step7DescriptionLabel.setText("Clustering algorithm");

        step8Label.setText("8.");

        step8DescriptionLabel.setText("Document mapper");

        step9Label.setText("9.");

        step9DescriptionLabel.setText("Quality measures");

        step10Label.setText("10.");

        step10escriptionLabel.setText("Longitudinal");

        step11Label.setText("11.");

        step11DescriptionLabel.setText("Make analysis");

        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backgroundNewProyect.png"))); // NOI18N

        javax.swing.GroupLayout stepsPanelLayout = new javax.swing.GroupLayout(stepsPanel);
        stepsPanel.setLayout(stepsPanelLayout);
        stepsPanelLayout.setHorizontalGroup(
            stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stepsPanelLayout.createSequentialGroup()
                .addGroup(stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(stepsPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(stepsTitleLabel))
                    .addComponent(imageLabel)
                    .addGroup(stepsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(separator2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(stepsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(step1Label)
                            .addComponent(step2Label))
                        .addGap(12, 12, 12)
                        .addGroup(stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(step1DescriptionLabel)
                            .addComponent(step2DescriptionLabel)))
                    .addGroup(stepsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(step3Label)
                        .addGap(12, 12, 12)
                        .addComponent(step3DescriptionLabel))
                    .addGroup(stepsPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(step8Label)
                            .addComponent(step4Label)
                            .addComponent(step5Label)
                            .addComponent(step6Label)
                            .addComponent(step7Label)
                            .addComponent(step9Label)
                            .addComponent(step10Label)
                            .addComponent(step11Label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(step11DescriptionLabel)
                            .addComponent(step10escriptionLabel)
                            .addComponent(step9DescriptionLabel)
                            .addComponent(step8DescriptionLabel)
                            .addComponent(step7DescriptionLabel)
                            .addComponent(step6DescriptionLabel)
                            .addComponent(step5DescriptionLabel)
                            .addComponent(step4DescriptionLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        stepsPanelLayout.setVerticalGroup(
            stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stepsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stepsTitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, stepsPanelLayout.createSequentialGroup()
                        .addComponent(step1Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step2Label))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, stepsPanelLayout.createSequentialGroup()
                        .addComponent(step1DescriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step2DescriptionLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(step3Label)
                    .addComponent(step3DescriptionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(stepsPanelLayout.createSequentialGroup()
                        .addComponent(step4Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step5Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step6Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step7Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step8Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step9Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step10Label)
                        .addGap(20, 20, 20))
                    .addGroup(stepsPanelLayout.createSequentialGroup()
                        .addComponent(step4DescriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step5DescriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step6DescriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step7DescriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step8DescriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step9DescriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(step10escriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(stepsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(step11DescriptionLabel)
                            .addComponent(step11Label))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imageLabel))
        );

        currentStepDescriptionLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        currentStepScrollPane.setBorder(null);

        javax.swing.GroupLayout currentStepPanelLayout = new javax.swing.GroupLayout(currentStepPanel);
        currentStepPanel.setLayout(currentStepPanelLayout);
        currentStepPanelLayout.setHorizontalGroup(
            currentStepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(separator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
            .addGroup(currentStepPanelLayout.createSequentialGroup()
                .addComponent(currentStepDescriptionLabel)
                .addContainerGap())
            .addComponent(currentStepScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE)
        );
        currentStepPanelLayout.setVerticalGroup(
            currentStepPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(currentStepPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(currentStepDescriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(separator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentStepScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout errorPanelLayout = new javax.swing.GroupLayout(errorPanel);
        errorPanel.setLayout(errorPanelLayout);
        errorPanelLayout.setHorizontalGroup(
            errorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(errorPanelLayout.createSequentialGroup()
                .addComponent(messageErrorLabel)
                .addContainerGap(537, Short.MAX_VALUE))
        );
        errorPanelLayout.setVerticalGroup(
            errorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(messageErrorLabel)
        );

        backButton.setText("< Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        nextButton.setText("Next >");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        finishButton.setText("Finish");
        finishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(stepsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(currentStepPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(errorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(finishButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(separator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 537, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(currentStepPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(errorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(separator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backButton)
                    .addComponent(nextButton)
                    .addComponent(finishButton)
                    .addComponent(cancelButton))
                .addContainerGap())
            .addComponent(stepsPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  /**
   * 
   * @param evt
   */
  private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
    changeStep(false);
  }//GEN-LAST:event_backButtonActionPerformed

  /**
   * 
   * @param evt
   */
  private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
    changeStep(true);
  }//GEN-LAST:event_nextButtonActionPerformed

  /**
   * 
   * @param evt
   */
  private void finishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finishButtonActionPerformed

    int i;
    AnalysisConfiguration analysisConfiguration;
    ArrayList<Period> periods;

    changeStep(true);

    periods = this.selectPeriodsPanel.getSelectedPeriods();

    analysisConfiguration = new AnalysisConfiguration(periods,
            this.selectUnitOfAnalysisPanel.getSelectedUnitOfAnalysis(),
            this.selectMatrixBuilderPanel.getSelectedKindOfMatrix(),
            this.selectSimilarityMeasurePanel.getSelectedNormalizationMeasures(),
            this.selectClusteringAlgorithmPanel.getSelectedClusteringAlgorithm(), 
            this.selectLongitudinalMeasuresPanel.getSelectedEvolutionMapMeasures(),
            this.selectLongitudinalMeasuresPanel.getSelectedOverlappingMapMeasures());

    if (analysisConfiguration.getUnitOfAnalysis().equals(UnitOfAnalysisEnum.Words)) {

      analysisConfiguration.setAuthorWords(this.selectUnitOfAnalysisPanel.isAuthorWordsSelected());
      analysisConfiguration.setSourceWords(this.selectUnitOfAnalysisPanel.isSourceWordsSelected());
      analysisConfiguration.setExtractedWords(this.selectUnitOfAnalysisPanel.isAddedWordsSelected());
    }

    if (analysisConfiguration.getClusteringAlgorithm().equals(ClusteringAlgorithmEnum.CentersSimples)) {

      analysisConfiguration.setMinNetworkSize(this.selectClusteringAlgorithmPanel.getMinNetworkSize());
      analysisConfiguration.setMaxNetworkSize(this.selectClusteringAlgorithmPanel.getMaxNetworkSize());
      
    } else {

      analysisConfiguration.setMinNetworkSize(this.selectClusteringAlgorithmPanel.getMinNetworkSize());
      analysisConfiguration.setMaxNetworkSize(this.selectClusteringAlgorithmPanel.getMaxNetworkSize());
      analysisConfiguration.setCutOff(this.selectClusteringAlgorithmPanel.getCutOff());
    }

    for (i = 0; i < periods.size(); i++) {

      if (this.selectDataReductionPerPeriod.isFrequencyDataReductionSelected(i)) {

        analysisConfiguration.setFrequencyDataReduction(i, true);
        analysisConfiguration.setMinFrequency(i, this.selectDataReductionPerPeriod.getMinFrequencyDataReductionSelected(i));
      }

      if (this.selectNetworkReductionPerPeriod.isCoOccurrenceNetworkReductionSelected(i)) {

        analysisConfiguration.setCoOccurrenceDataReduction(i, true);
        analysisConfiguration.setMinCoOccurrence(i, this.selectNetworkReductionPerPeriod.getMinCoOccurrenceNetworkReductionSelected(i));
      }
    }
    
    if ((this.selectMatrixBuilderPanel.getSelectedKindOfMatrix().equals(KindOfMatrixEnum.CoOccurrence))
         || (this.selectMatrixBuilderPanel.getSelectedKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnAuthor))
         || (this.selectMatrixBuilderPanel.getSelectedKindOfMatrix().equals(KindOfMatrixEnum.AggregatedCouplingBasedOnJournal))) {

      if (this.selectDocumentMappersPanel.isCoreMapperSelected()) {

        analysisConfiguration.setCoreMapper(true);
      }

      if (this.selectDocumentMappersPanel.isIntersectionMapperSelected()) {

        analysisConfiguration.setIntersectionMapper(true);
      }

      if (this.selectDocumentMappersPanel.isKCoreMapperSelected()) {

        analysisConfiguration.setKCoreMapper(true);
        analysisConfiguration.setKCore(this.selectDocumentMappersPanel.getKCore());
      }

      if (this.selectDocumentMappersPanel.isSecondaryMapperSelected()) {

        analysisConfiguration.setSecondaryMapper(true);
      }

      if (this.selectDocumentMappersPanel.isUnionMapperSelected()) {

        analysisConfiguration.setUnionMapper(true);
      }
      
    } else {
    
      analysisConfiguration.setBasicCouplingMapper(true);
    }
    
    if (this.selectQuialityMeasuresPanel.isHIndexSelected()) {

      analysisConfiguration.setHIndex(true);
    }

    if (this.selectQuialityMeasuresPanel.isGIndexSelected()) {

      analysisConfiguration.setGIndex(true);
    }

    if (this.selectQuialityMeasuresPanel.isHgIndexSelected()) {

      analysisConfiguration.setHgIndex(true);
    }

    if (this.selectQuialityMeasuresPanel.isQ2IndexSelected()) {

      analysisConfiguration.setQ2Index(true);
    }

    if (this.selectQuialityMeasuresPanel.isAverageCitationsSelected()) {

      analysisConfiguration.setAverageCitations(true);
    }

    if (this.selectQuialityMeasuresPanel.isSumCitationsSelected()) {

      analysisConfiguration.setSumCitations(true);
    }

    if (this.selectQuialityMeasuresPanel.isMaxCitationsSelected()) {

      analysisConfiguration.setMaxCitations(true);
    }

    if (this.selectQuialityMeasuresPanel.isMinCitationsSelected()) {

      analysisConfiguration.setMinCitations(true);
    }

    

    this.doExperimentPanel.execute(analysisConfiguration);
  }//GEN-LAST:event_finishButtonActionPerformed

  /**
   * 
   * @param evt
   */
  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed

    this.doExperimentPanel.cancelTask();
    dispose();
  }//GEN-LAST:event_cancelButtonActionPerformed
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel currentStepDescriptionLabel;
    private javax.swing.JPanel currentStepPanel;
    private javax.swing.JScrollPane currentStepScrollPane;
    private javax.swing.JPanel errorPanel;
    private javax.swing.JButton finishButton;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JLabel messageErrorLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JSeparator separator1;
    private javax.swing.JSeparator separator2;
    private javax.swing.JSeparator separator3;
    private javax.swing.JLabel step10Label;
    private javax.swing.JLabel step10escriptionLabel;
    private javax.swing.JLabel step11DescriptionLabel;
    private javax.swing.JLabel step11Label;
    private javax.swing.JLabel step1DescriptionLabel;
    private javax.swing.JLabel step1Label;
    private javax.swing.JLabel step2DescriptionLabel;
    private javax.swing.JLabel step2Label;
    private javax.swing.JLabel step3DescriptionLabel;
    private javax.swing.JLabel step3Label;
    private javax.swing.JLabel step4DescriptionLabel;
    private javax.swing.JLabel step4Label;
    private javax.swing.JLabel step5DescriptionLabel;
    private javax.swing.JLabel step5Label;
    private javax.swing.JLabel step6DescriptionLabel;
    private javax.swing.JLabel step6Label;
    private javax.swing.JLabel step7DescriptionLabel;
    private javax.swing.JLabel step7Label;
    private javax.swing.JLabel step8DescriptionLabel;
    private javax.swing.JLabel step8Label;
    private javax.swing.JLabel step9DescriptionLabel;
    private javax.swing.JLabel step9Label;
    private javax.swing.JPanel stepsPanel;
    private javax.swing.JLabel stepsTitleLabel;
    // End of variables declaration//GEN-END:variables
  private Font boldFont = new java.awt.Font("Tahoma", 1, 11);
  private int step = __STEP_1;
  private static final int __STEP_1 = 0;
  private static final int __STEP_2 = 1;
  private static final int __STEP_3 = 2;
  private static final int __STEP_4 = 3;
  private static final int __STEP_5 = 4;
  private static final int __STEP_6 = 5;
  private static final int __STEP_7 = 6;
  private static final int __STEP_8 = 7;
  private static final int __STEP_9 = 8;
  private static final int __STEP_10 = 9;
  private static final int __STEP_11 = 10;
  private static final int __MAX_STEP = __STEP_11;
  private SelectPeriodsPanel selectPeriodsPanel = new SelectPeriodsPanel();
  private SelectUnitOfAnalysisPanel selectUnitOfAnalysisPanel = new SelectUnitOfAnalysisPanel();
  private SelectMatrixBuilderPanel selectMatrixBuilderPanel = new SelectMatrixBuilderPanel();
  private SelectDataReductionPerPeriod selectDataReductionPerPeriod = new SelectDataReductionPerPeriod();
  private SelectSimilarityMeasurePanel selectSimilarityMeasurePanel = new SelectSimilarityMeasurePanel();
  private SelectNetworkReductionPerPeriod selectNetworkReductionPerPeriod = new SelectNetworkReductionPerPeriod();
  private SelectClusteringAlgorithmPanel selectClusteringAlgorithmPanel = new SelectClusteringAlgorithmPanel();
  private SelectDocumentMappersPanel selectDocumentMappersPanel = new SelectDocumentMappersPanel();
  private SelectCouplingDocumentMappersPanel selectCouplingDocumentMappersPanel = new SelectCouplingDocumentMappersPanel();
  private SelectQuialityMeasuresPanel selectQuialityMeasuresPanel = new SelectQuialityMeasuresPanel();
  private SelectLongitudinalMeasuresPanel selectLongitudinalMeasuresPanel = new SelectLongitudinalMeasuresPanel();
  private DoAnalysisPanel doExperimentPanel = new DoAnalysisPanel(this);
}
