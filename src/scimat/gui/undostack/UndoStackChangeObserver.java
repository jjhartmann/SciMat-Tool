/* ========================================================================
 * UndoStackChangeObserver.java
 * ========================================================================
 */

package scimat.gui.undostack;

/**
 * Esta interfaz debe ser implementada por todos aquellos objetos 
 * interesados en ser informados cada vez que se produzca un cambio en la
 * pila de �rdenes para deshacer. Por ejemplo, los botones deshacer y
 * rehacer para poder activarse o desactivarse.
 * PATRON: Observador.
 * PAPEL EN EL PATRON: Observador.
 * @author Ildefonso Adan Ramirez
 *         Manuel Jesus Cobo Martin
 *         Salvador Garcia Garcia
 *         Antonio Jose Puentedura Rodriguez         
 */
public interface UndoStackChangeObserver {
    
    /*
     * = M�todos ==========================================================
     */

    /**
     * M�todo mediante el cual la pila de �rdenes para deshacer (a quien 
     * este componente observa) avisa a este componente de que se ha 
     * producido un cambio en la misma.
     * @param canUndo Indica si hay �rdenes para deshacer.
     * @param canRedo Indica si hay �rdenes para rehacer.
     */
    public void undoStackChanged(boolean canUndo, boolean canRedo);
}

/*
 * ========================================================================
 */
