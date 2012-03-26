package ascensionplugin;

import java.awt.Frame;

import java.io.FileFilter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.io.filefilter.SuffixFileFilter;

import org.openshapa.plugins.DataViewer;
import org.openshapa.plugins.Filter;
import org.openshapa.plugins.Plugin;


public class HeartRatePlugin implements Plugin {

    private static final Filter HR_FILTER = new Filter() {
            List<String> exts = new ArrayList<String>();
            FileFilter ff;

            {
                exts.add(".csv");
                ff = new SuffixFileFilter(exts);
            }

            @Override public String getName() {
                return "CSV files";
            }

            @Override public FileFilter getFileFilter() {
                return ff;
            }

            @Override public Iterable<String> getExtensions() {
                return exts;
            }
        };

    private static final Filter[] FILTERS = new Filter[] { HR_FILTER };

    @Override public String getClassifier() {
        return "com.dteoh.heartrate";
    }

    @Override public Filter[] getFilters() {
        return FILTERS;
    }

    @Override public DataViewer getNewDataViewer(final Frame parent,
        final boolean modal) {
        return new HRDataViewer(parent, modal);
    }

    @Override public String getPluginName() {
        return "Heart Rate Plugin";
    }

    @Override public ImageIcon getTypeIcon() {
        return null;
    }

    @Override public Class<? extends DataViewer> getViewerClass() {
        return HRDataViewer.class;
    }

}
