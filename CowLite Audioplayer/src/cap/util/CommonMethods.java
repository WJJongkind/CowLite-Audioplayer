/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.util;

/**
 *
 * @author Wessel
 */
public class CommonMethods 
{
    public static String pathToFilename(String path)
    {
        if(path.contains("/"));
            path = path.substring(path.lastIndexOf("/") + 1);
        if(path.contains("\\"))
            path = path.substring(path.lastIndexOf("\\" + 1));
        path = path.replace("%20", " ");
        return path;
    }
}
