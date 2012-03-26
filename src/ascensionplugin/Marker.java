/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensionplugin;

import java.util.Vector;

/**
 *
 * @author jesse
 */
public class Marker {
    
    public Vector<Point> points;
    public String name;
    
    public Marker(String name) {
        this.name = name;
        this.points = new Vector<Point>();
    }
}
