import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import java.util.Map;

import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 * Created by Michał on 2016-11-15.
 * Class Progress is responsible for Progres Bar.
 */
public class Progress extends JPanel implements ActionListener, PropertyChangeListener {

    private static JProgressBar progressBar;
    private static UpdateProgressBar upProgBar;
    private JPanel current = new JPanel();
    private static JButton button;
    private static double progress;
    private static double max;
    private static double tick;

    /**
     * Class UpdateProgressBar is responsible for the bar movement. It divides
     * the bar into 100 ticks and increases it as the song runs.
     */

    public class UpdateProgressBar extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground() {
            try {
                progress = 0.0;
                max = duration(Playlist.getCurrent());
                setProgress(0);
                tick = 100/max;

                while (progress < 100) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {

                    }
                    progress += tick;
                    //System.out.println(progress);
                    setProgress(Math.min( (int) progress, 100));
                }
            } catch (UnsupportedAudioFileException ex) {
            } catch (IOException ex) {
            }
            return null;
        }
    }

    /**
     * Method actionPerformer is resposnible for starting the Progress Bar
     * @param evt 
     */

    public void actionPerformed(ActionEvent evt) {
        upProgBar = new UpdateProgressBar();
        upProgBar.addPropertyChangeListener(this);
        upProgBar.execute();
    }

    /**
     * Method propertyChange sets the value of the progress bar
     * @param evt 
     */

    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

    /**
     * Method Progress is responsible for showing the Progress Bar and its movement
     * @param width 
     */

    public Progress(int width) {
        current.setPreferredSize(new Dimension(width - 20, 50));

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(width - 50, 25));
        current.add(progressBar);

        button = new JButton();
        button.addActionListener(this);

        add(current);
    }

    public static int duration(File file) throws UnsupportedAudioFileException, IOException {

        int mili, sec, min;
        int duration;

        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        if (fileFormat instanceof TAudioFileFormat) {
            Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
            String key = "duration";
            Long microseconds = (Long) properties.get(key);
            mili = (int) (microseconds / 1000);
            sec = (mili / 1000) % 60;
            min = (mili / 1000) / 60;
        } else {
            throw new UnsupportedAudioFileException();
        }

        duration = min * 60 + sec;

        return duration;
    }

    public static JButton getButton() {
        return button;
    }

    public static JProgressBar getProgressBar() {
        return progressBar;
    }

    public static double getProgress() {
        return progress;
    }

    public static void setProgress(double progress) {
        Progress.progress = progress;
    }

    public static double getMax() {
        return max;
    }

    public static void setMax(double max) {
        Progress.max = max;
    }

    public static void setTick(double tick) {
        Progress.tick = tick;
    }
}
