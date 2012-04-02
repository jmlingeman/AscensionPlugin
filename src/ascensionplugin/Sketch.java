/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensionplugin;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import processing.core.*;
import peasy.*;

public class Sketch extends PApplet {
    
    private HRModel model;
    private PropertyChangeListener positionListener;
    private int pos = 0;

//    @Override
//    public void setup() {
//        size(640, 480);
//        stroke(155, 0, 0);
//    }
//
//    @Override
//    public void draw() {
//        line(mouseX, mouseY, width / 2, height / 2);
//    }
    int w = 640;
    int h = 480;
    PeasyCam cam;
    
    @Override
    public void setup() {
        System.out.println(System.getProperties());
//        size(screen.height*4/3/2, screen.height/2, OPENGL); //Keep 4/3 aspect ratio, since it matches the kinect's.
        size(w, h, P3D);
//        hint(ENABLE_OPENGL_4X_SMOOTH);
        noStroke();
        
        cam = new PeasyCam(this, 100);
        cam.setMinimumDistance(50);
        cam.setMaximumDistance(500);
        
        positionListener = new PropertyChangeListener() {
                @Override public void propertyChange(
                    final PropertyChangeEvent evt) {
                    updateFromModel();
                }
            };
    }
    
    @Override
    public synchronized void draw() {
        background(0);
        ambientLight(64, 64, 64);
        lightSpecular(255,0,0);
        directionalLight(224,224,224, (float)0.5, 1, -1);
        
        // Get the current point to display

//        pushMatrix();
//        translate((float)0.5*width + i, (float)0.5*height + i, (float)0.5*300 + i);
//        sphere(2 * 3);
//        popMatrix();
        
        Marker curMarker;
        Point curPoint;
        
        if(pos >= 0 && model != null && model.body != null && model.body.markers != null) {
            for(String k : model.body.markers.keySet()) {
                curMarker = model.body.markers.get(k);
                curPoint = curMarker.points.get(pos);

                pushMatrix();
                translate((float)(curPoint.x / 100.0) * width, (float)(curPoint.y / 100.0) * height, -(float)(curPoint.z / 100.0));
                sphere(6);
                popMatrix();
            }
        }
        
//        for (Skeleton s: skels.values()) {
//            fill(s.colors[0], s.colors[1], s.colors[2]);
//            for (float[] j: s.allCoords) {
//                pushMatrix();
//                translate(j[0]*width, j[1]*height, -j[2]*300);
//                sphere(2 * ballSize/j[2]);
//                popMatrix();
//            }
//        }
    }
    
    public void setModel(HRModel model) {
        removeModel();
        this.model = model;
        this.model.addPositionListener(positionListener);
    }
    
    public void removeModel() {

//        if (model != null) {
//            model.removePositionListener(positionListener);
//            model = null;
//        }
    }
    
    public void updateFromModel() {
        pos = model.getCurrentPosition();
        System.out.println(pos);
        draw();
    }
}