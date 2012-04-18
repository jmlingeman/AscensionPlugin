/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ascensionplugin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jesse
 */
public class DataReader {
    
    public static Body ReadAscensionControl(String filename) {
        Body body = new Body();
        
        return body;
    }
    
    public static Body ReadAscensionControlLog(File filename) {
        Body body = new Body();
        
        try{
            // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = new FileInputStream(filename);
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            String curHead;
            String[] header = br.readLine().split(",", -1);
            String[] sptLine;
            
            
            String[] temp;
            int curSensor;
            
            double x, y, z, pitch, yaw, roll, quality, temp_time;
            long time;
            int button;
            
            ConcurrentHashMap<String, Integer> headerHash = new ConcurrentHashMap<String, Integer>();
            
            for(int i = 0; i < header.length; i++) {
                headerHash.put(header[i], i);
            }
            
            for(int i = 0; i < 16; i++) {
                body.addMarker(String.valueOf(i));
            }
            
            // Get the first time point so we can subtract it from the rest of them.
            double first_time = -1;
            
            System.out.println(header.length);
            System.out.println(headerHash.keySet().size());
            
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                sptLine = strLine.split(",", -1);
                
                
                
                // Now get each Sensor's data by building the string to match
                // the hash
                for(int i = 0; i < 16; i++) {
                    if(!sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".X")].equals("")) {
                        x = Double.valueOf(sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".X")]);
                        y = Double.valueOf(sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".Y")]);
                        z = Double.valueOf(sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".Z")]);
                        pitch = Double.valueOf(sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".Pitch")]);
                        yaw = Double.valueOf(sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".yaw")]);
                        roll = Double.valueOf(sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".Roll")]);
                        temp_time = Double.valueOf(sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".Time")]);
                        quality = Double.valueOf(sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".quality")]);
                        button = Integer.valueOf(sptLine[headerHash.get("Sensor" + String.valueOf(i) + ".Switch")]);
                        
                        if(first_time == -1) {
                            first_time = temp_time;
                        }
                        
                        time = Math.round((temp_time - first_time) * 1000.0);
                    } else {
                        x = -1;
                        y = -1;
                        z = -1;
                        pitch = -1;
                        yaw = -1;
                        roll = -1;
                        temp_time = -1;
                        quality = -1;
                        button = -1;
                        time = -1;
                    }
                    
                    body.markers.get(String.valueOf(i)).points.add(new Point(time, x, y, z, pitch, yaw, roll, quality, button));
                }
            }
            //Close the input stream
            in.close();
            
            // Now collect the time points so we can read them
            for(Point p : body.markers.get("0").points) {
                body.timepoints.add(p.time);
            }
            
            
            
        } catch (Exception e) {//Catch exception if any
            e.printStackTrace();
        }
        
        
        body = fixHemisphereFlips(body);
        
        return body;
    }
    
    public static Body fixHemisphereFlips(Body body) {
        double thresh = 1.0;
        for(String key : body.markers.keySet()) {
            Marker marker = body.markers.get(key);
            for(int i = 1; i < marker.points.size(); i++) {
                Point cp = marker.points.get(i);
                Point pp = marker.points.get(i-1);
                
                // Check for a X flip
                if(sign(cp.x) != sign(pp.x) && Math.abs(cp.x - pp.x) > thresh ) {
                    cp.x = -cp.x;
                }
                
                // Check for a Y flip
                if(sign(cp.y) != sign(pp.y) && Math.abs(cp.y - pp.y) > thresh ) {
                    cp.y = -cp.y;
                }
                
                // Check for a Z flip
                if(sign(cp.z) != sign(pp.z) && Math.abs(cp.z - pp.z) > thresh ) {
                    cp.z = -cp.z;
                }
                
                
            }
        }
        
        return body;
    }
    
    int sign(int i) {
        if (i == 0) return 0;
        if (i >> 31 != 0) return -1;
        return +1;
    }
    int sign(long i) {
        if (i == 0) return 0;
        if (i >> 63 != 0) return -1;
        return +1;
    }
    public static int sign(double f) {
        if (f != f) throw new IllegalArgumentException("NaN");
        if (f == 0) return 0;
        f *= Double.POSITIVE_INFINITY;
        if (f == Double.POSITIVE_INFINITY) return +1;
        if (f == Double.NEGATIVE_INFINITY) return -1;

        //this should never be reached, but I've been wrong before...
        throw new IllegalArgumentException("Unfathomed double");
    }

}
