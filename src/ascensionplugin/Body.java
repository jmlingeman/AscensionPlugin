/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensionplugin;

import java.util.HashMap;

/**
 *
 * @author jesse
 */
public class Body {
    public HashMap<String, Marker> markers;
    
    public Body() {
        markers = new HashMap<String, Marker>();
    }
    
    public void addMarker(Marker m) {
        markers.put(m.name, m);
    }
}
