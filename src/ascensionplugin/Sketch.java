/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensionplugin;

import processing.core.*;
import processing.opengl.*;
import com.sun.opengl.impl.macosx.*;

public class Sketch extends PApplet {

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
    
    @Override
    public void setup() {
//        size(screen.height*4/3/2, screen.height/2, OPENGL); //Keep 4/3 aspect ratio, since it matches the kinect's.
        size(w, h, OPENGL);
        hint(ENABLE_OPENGL_4X_SMOOTH);
        noStroke();
    }
    
    @Override
    public void draw() {
        background(0);
        ambientLight(64, 64, 64);
        lightSpecular(255,255,255);
        directionalLight(224,224,224, (float)0.5, 1, -1);
        
        // Get the current point to display

        pushMatrix();
        translate((float)0.5*width, (float)0.5*height, (float)0.5*300);
        sphere(2 * 3);
        popMatrix();
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
}