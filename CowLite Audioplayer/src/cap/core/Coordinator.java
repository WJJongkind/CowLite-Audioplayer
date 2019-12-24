/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.core;

import cap.gui.Window;

/**
 * Interface to which all coordinators should adhere. A coordinator's main responsibility
 * is to manage navigation between screens. Only very rarely should it contain business logic. 
 * Preferably this is prevented at all times (though I do violate this rule myself for the
 * sake of simplicity).
 * @author Wessel Jongkind
 */
public interface Coordinator {
    
    /**
     * Requests the coordinator to present/push it's initial UI to the given window.
     * @param window The window to which the coordinator's initial UI should be presented.
     */
    public void start(Window window);
    
}
