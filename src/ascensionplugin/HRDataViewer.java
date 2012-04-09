package ascensionplugin;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import org.openshapa.models.db.Datastore;
import org.openshapa.models.id.Identifier;

import org.openshapa.plugins.CustomActions;
import org.openshapa.plugins.CustomActionsAdapter;
import org.openshapa.plugins.DataViewer;
import org.openshapa.plugins.ViewerStateListener;

import org.openshapa.util.DataViewerUtils;

import org.openshapa.views.DataController;
import org.openshapa.views.component.DefaultTrackPainter;
import org.openshapa.views.component.TrackPainter;


public class HRDataViewer implements DataViewer {

    /** Data viewer ID. */
    private Identifier id;

    /** Dialog for showing our visualizations. */
    private JDialog hrDialog;

    /** Panel for showing our visualizations. */
//    private HRPanel hrPanel;

    /** Data viewer offset. */
    private long offset;

    /** Data to visualize. */
    private File data;

    /** Data model. */
    private HRModel model;
    
    public Sketch sketch;

    /** Data viewer clock. */
    private Clock clock;

    /** Data viewer current playback rate. */
    private double playbackRate;

    /** Data viewer state listeners. */
    private List<ViewerStateListener> stateListeners;

    /** Action button for demo purposes. */
    private JButton sampleButton;

    /** Supported custom actions. */
    private CustomActions actions = new CustomActionsAdapter() {
            @Override public AbstractButton getActionButton1() {
                return sampleButton;
            }
        };

    public HRDataViewer(final Frame parent, final boolean modal) {
//        Runnable edtTask = new Runnable() {
//                @Override public void run() {
                    hrDialog = new JDialog(parent, modal);
                    hrDialog.setName("HRDataViewer");
                    hrDialog.setResizable(true);
                    hrDialog.setSize(640, 480);

                    Container c = hrDialog.getContentPane();
                    c.setLayout(new BorderLayout());
                    
                    sketch = new Sketch();
                    System.out.println("INITING");
                    c.add(sketch, BorderLayout.CENTER);
                    sketch.init();
                    

//                    hrPanel = new HRPanel();
//                    c.add(hrPanel, BorderLayout.CENTER);

                    hrDialog.setVisible(true);

//                    sampleButton = new JButton();
////                    sampleButton.setIcon(new ImageIcon(
////                            HRDataViewer.class.getResource("ascension.png")));
//                    sampleButton.setBorderPainted(false);
//                    sampleButton.setContentAreaFilled(false);
                    
                    
//                }
//            };

//        if (SwingUtilities.isEventDispatchThread()) {
//            edtTask.run();
//        } else {
//            SwingUtilities.invokeLater(edtTask);
//        }

        stateListeners = new ArrayList<ViewerStateListener>();
        clock = new Clock();
        playbackRate = 1D;
    }

    @Override public JDialog getParentJDialog() {
        return hrDialog;
    }

    @Override public float getFrameRate() {
        return  ((float)model.body.timepoints.size()) / ((float)model.getDuration()/1000.0F);
    }

    @Override public void setIdentifier(final Identifier id) {
        this.id = id;
    }

    @Override public Identifier getIdentifier() {
        return id;
    }

    @Override public void setOffset(final long offset) {
        this.offset = offset;
    }

    @Override public long getOffset() {
        return offset;
    }

    @Override public TrackPainter getTrackPainter() {
        return new DefaultTrackPainter();
    }

    @Override public void setDataViewerVisible(final boolean isVisible) {
        hrDialog.setVisible(isVisible);
    }

    @Override public void setDataFeed(final File dataFeed) {
        data = dataFeed;
        model = new HRModel(data);

        sketch.setModel(model);
//        hrPanel.setModel(model);

        SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    getParentJDialog().setTitle(dataFeed.getName());
                }
            });
    }

    @Override public File getDataFeed() {
        return data;
    }

    @Override public long getDuration() {

        if (model == null) {
            return 0;
        }

        return model.getDuration();
    }

    @Override public long getCurrentTime() throws Exception {

        if (model == null) {
            return 0;
        }

        return model.getCurrentTimestamp();
    }

    @Override public void seekTo(final long position) {

        if (model != null) {
            model.seek(position);
        }
    }

    @Override public boolean isPlaying() {
        return clock.isTicking();
    }

    @Override public void stop() {
        clock.stop();
    }

    @Override public void setPlaybackSpeed(final float rate) {
        playbackRate = rate;
    }

    @Override public void play() {
        Runnable task;

        if (playbackRate < 0) {
            task = new Runnable() {
                    @Override public void run() {
                        model.prev();
                    }
                };
        } else if (playbackRate > 0) {
            task = new Runnable() {

                    @Override public void run() {
                        model.next();
                    }
                };
        } else {
            stop();

            return;
        }

        clock.start(task, getFrameRate(), playbackRate);
    }

    @Override public void storeSettings(final OutputStream os) {

        try {
            DataViewerUtils.storeDefaults(this, os);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override public void loadSettings(final InputStream is) {

        try {
            DataViewerUtils.loadDefaults(this, is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override public void addViewerStateListener(
        final ViewerStateListener vsl) {

        if (vsl != null) {
            stateListeners.add(vsl);
        }
    }

    @Override public void removeViewerStateListener(
        final ViewerStateListener vsl) {

        if (vsl != null) {
            stateListeners.remove(vsl);
        }
    }

    @Override public CustomActions getCustomActions() {
        return actions;
    }

    @Override public void clearDataFeed() {
        stop();
//        hrPanel.removeModel();
        sketch.removeModel();
        model.clearData();
        model = null;
    }

    @Override public void setDatastore(final Datastore sDB) {
        // TODO Auto-generated method stub
    }

    @Override public void setParentController(
        final DataController dataController) {
        // TODO Auto-generated method stub
    }

}
