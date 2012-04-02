/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensionplugin;

/**
 *
 * @author jesse
 */
public class Point {
    public double x, y, z, pitch, yaw, roll, quality;
    public long time;
    public int button;
    
    public Point() {
        
    }
    public Point(long time, double x, double y, double z, double pitch, double yaw, double roll, double quality, int button) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.quality = quality;
        this.button = button;
    }
}
