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
    public Point(double time, double x, double y, double z, double pitch, double yaw, double roll, double quality) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
        this.quality = quality;
    }
}
