/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

/**
 *
 * @author Wessel
 */
public interface Window {
    public void presentViewController(ViewController viewController);
    public void setVisible(boolean visible);
}
