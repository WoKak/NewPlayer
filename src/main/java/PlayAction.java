import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.Optional;

/**
 * Class PlayAction is responsibe for playing the music and Play/Pause function.
 * Created by MichaĹ‚ on 2016-10-27.
 */
public class PlayAction implements ActionListener {

    private static int play = -1;
    private static FileInputStream fis;
    private static Playing playing = null;
    private static double tmpProgress;

    ImageIcon pauseIcon = new ImageIcon("icons/pause.png");
    static ImageIcon playIcon = new ImageIcon("icons/play.png");

    /**
     * Method Playing is a Singleton. It makes sure there is always only one Playing.
     * @return
     */
    public static Playing getPlaying() {
        if (!Optional.ofNullable(playing).isPresent()) {
            try {
                if (Playlist.getCurrent() == null) {
                    Playlist.setCurrent(Playlist.getSongs().get(0).getFile());
                    fis = new FileInputStream(Playlist.getCurrent());
                    Playlist.getSongs().get(Playlist.find(Playlist.getCurrent())).getLabel().setForeground(new Color(0, 100, 150));
                } else {
                    fis = new FileInputStream(Playlist.getCurrent());
                    Playlist.getSongs().get(Playlist.find(Playlist.getCurrent())).getLabel().setForeground(new Color(0, 100, 150));
                }
                playing = new Playing(fis);

            } catch (Exception e) {
            }
        }
        return playing;
    }

    /**
     * Method destroyPlaying stops playing by setting playing to null.
     */
    public static void destroyPlaying() {
        playing = null;
    }

    /**
     * Method actionPerformed is responsible for play/pause function. Every second
     * click it starts playing the music or stops it.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            play++;

            if (play == 0) {
                getPlaying().play();
                Controls.getPlayButton().setIcon(pauseIcon);
                Progress.getButton().doClick();
            } else if (play % 2 == 0) {
                getPlaying().resume();
                Controls.getPlayButton().setIcon(pauseIcon);

                Playlist.getSongs().get(Playlist.find(Playlist.getCurrent())).getLabel().setForeground(new Color(0, 100, 150));

                Progress.setProgress(tmpProgress);
                Progress.setTick(100/Progress.getMax());
            } else {
                getPlaying().pause();
                Controls.getPlayButton().setIcon(playIcon);

                tmpProgress = Progress.getProgress();
                Progress.getProgressBar().setValue((int)tmpProgress);
                Progress.setTick(0.0);
            }

        } catch (Exception ex) {
        }
    }

    public static void setPlay(int play) {
        PlayAction.play = play;
    }
}


