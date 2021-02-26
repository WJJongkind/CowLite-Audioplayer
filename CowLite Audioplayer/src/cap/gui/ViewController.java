/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import javax.swing.JComponent;

/**
 * Base interface for a viewcontrolle. Each viewcontroller should have a method
 * that exposes the view that is being controlled.
 * @author Wessel Jongkind
 */
public interface ViewController {
    
    /**
     * The view that this viewcontroller is controlling.
     * @return This viewcontroller's view.
     */
    public JComponent getView();
    
}
