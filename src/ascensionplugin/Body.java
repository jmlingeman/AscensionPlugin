/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensionplugin;

import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author jesse
 */
public class Body {
    public HashMap<String, Marker> markers;
    public Vector<Long> timepoints;
    
    public Body() {
        markers = new HashMap<String, Marker>();
        timepoints = new Vector<Long>();
    }
    
    public void addMarker(String name) {
        Marker m = new Marker(name);
        markers.put(m.name, m);
    }
    
    public int getTimeStep() {
        return 0;
    }
}
