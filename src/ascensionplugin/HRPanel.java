package ascensionplugin;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Graphics;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.AbstractAction;


import net.miginfocom.swing.MigLayout;


public class HRPanel extends JPanel {

    private JLabel patient;
    private JLabel bpm;
    private JLabel units;
    
    private JButton zoomInX;
    private JButton zoomOutX;
    private JButton zoomInY;
    private JButton zoomOutY;

    private HRModel model;
    
    private int zoomLevel;
    private double maxValue;
    private double minValue;
    private int maxZoom;
    
    private HRPanel panel;
    
    private int scale;
    
    private List<Double> heartRateWindow;
    private List<Double> heartRateData;

    private PropertyChangeListener positionListener;
    
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	
    	int x1,y1;
    	int x2,y2;
    	for(int i = 0; i < heartRateWindow.size() - 1; i++) {
    		x1 = getXPos(i);
    		y1 = getYPos(i);
    		
    		x2 = getXPos(i+1);
    		y2 = getYPos(i+1);
    		g.fillRect(x1, y1, 5, 5);
    		g.drawLine(x1, y1, x2, y2);
    		
    		if( i == heartRateWindow.size() - 2 ) {
    			g.fillRect(x2, y2, 5, 5);
    		}
    	}
    	
    }
    
    private int getXPos(int i) {
    	int width = panel.getWidth();
    	int height = panel.getHeight();
    	return Math.round(((float)width / heartRateWindow.size()) * i);
    }
    
    private int getYPos(int i) {
    	int width = panel.getWidth();
    	int height = panel.getHeight();
    	
    	// Center the value
    	double yd = heartRateWindow.get(i);
    	int centered_val = (int)Math.round(yd*scale - (( minValue + (maxValue - minValue) / 2) * scale));
    	int y = -centered_val + height / 2;
    	
    	System.out.println(centered_val);
    	System.out.println(y);
      	return y;
    }
    
    private void zoomInX() {
    	zoomLevel = (int) Math.max(5, Math.round(zoomLevel / 1.5));
    }
    
    private void zoomOutX() {
    	zoomLevel = (int) Math.min(maxZoom, Math.round(zoomLevel * 1.5));
    }
    
    private void zoomInY() {
    	scale = Math.max(1, scale + 1);
    }
    
    private void zoomOutY() {
    	scale = Math.min(20, scale - 1);
    }
    
    class ZoomInXButtonListener implements ActionListener {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		zoomInX();
    		positionListener.propertyChange(null);	
    	}
    }
    
    class ZoomOutXButtonListener implements ActionListener {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		zoomOutX();
    		positionListener.propertyChange(null);	
    	}
    }
    
    class ZoomInYButtonListener implements ActionListener {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		zoomInY();
    		positionListener.propertyChange(null);	
    	}
    }
    
    class ZoomOutYButtonListener implements ActionListener {
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		zoomOutY();
    		positionListener.propertyChange(null);	
    	}
    }

    public HRPanel() {
    	panel = this;
    	zoomLevel = 100;
    	maxZoom = 10000;
    	scale = 1;
    	
    	heartRateWindow = new LinkedList<Double>();
        setLayout(new MigLayout());
        setBackground(Color.GRAY.darker());

        patient = new JLabel();
        patient.setHorizontalAlignment(SwingConstants.CENTER);
        patient.setFont(new Font("Helvetica", Font.PLAIN, 36));
        //add(patient, "span 2, growx, pushx, wrap");

        bpm = new JLabel();
        bpm.setHorizontalAlignment(SwingConstants.RIGHT);
        bpm.setFont(new Font("Helvetica", Font.BOLD, 256));
        //add(bpm, "growx, pushx");

        units = new JLabel();
        units.setFont(new Font("Helvetica", Font.BOLD, 36));
        units.setText("bpm");
        //add(units, "");
        
        zoomInX = new JButton();
        zoomInX.setVerticalAlignment(SwingConstants.TOP);
        zoomInX.setFont(new Font("Helvetica", Font.BOLD, 36));
        zoomInX.setText("X+");
        zoomInX.addActionListener(new ZoomInXButtonListener());
        add(zoomInX);
        
        zoomOutX = new JButton();
        zoomOutX.setVerticalAlignment(SwingConstants.TOP);
        zoomOutX.setFont(new Font("Helvetica", Font.BOLD, 36));
        zoomOutX.setText("X-");
        zoomOutX.addActionListener( new ZoomOutXButtonListener());
        add(zoomOutX);
        
        zoomInY = new JButton();
        zoomInY.setVerticalAlignment(SwingConstants.BOTTOM);
        zoomInY.setFont(new Font("Helvetica", Font.BOLD, 36));
        zoomInY.setText("Y+");
        zoomInY.addActionListener(new ZoomInYButtonListener());
        add(zoomInY);
        
        zoomOutY = new JButton();
        zoomOutY.setVerticalAlignment(SwingConstants.BOTTOM);
        zoomOutY.setFont(new Font("Helvetica", Font.BOLD, 36));
        zoomOutY.setText("Y-");
        zoomOutY.addActionListener( new ZoomOutYButtonListener());
        add(zoomOutY);
        
        
        
        
        

        positionListener = new PropertyChangeListener() {
                @Override public void propertyChange(
                    final PropertyChangeEvent evt) {
                    updateFromModel();
                }
            };
    }
    
    

    public void setModel(final HRModel model) {
        removeModel();

        this.model = model;
        model.addPositionListener(positionListener);

        SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    patient.setText(model.getPatientName());
                }
            });

        updateFromModel();
    }

    public void removeModel() {

        if (model != null) {
            model.removePositionListener(positionListener);
            model = null;
        }
    }

    private void updateFromModel() {
        double heartRate = model.getCurrentHeartRate();
//        maxValue = model.getMaxValue();
//        minValue = model.getMinValue();
        heartRateWindow = model.getCurrentHeartRateWindow(zoomLevel);

        Formatter rateFormatter = new Formatter();
        rateFormatter.format("%.1f", heartRate);
        
        this.repaint();

        final String value = rateFormatter.toString();
        final Color newColor = getColorForRate(heartRate);

        SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    bpm.setText(value);

                    bpm.setForeground(newColor);
                    units.setForeground(newColor);
                }
            });
    }

    private Color getColorForRate(final double rate) {

        if (rate < 80D) {
            return Color.GREEN.brighter();
        }

        if (rate < 120D) {
            return Color.YELLOW.darker();
        }

        return Color.RED.darker().darker();
    }



}
