import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Michał on 2016-10-31.
 * Class Playing is responsible for playing songs.
 */
public class Playing {
    private final static int NOTSTARTED = 0;
    private final static int PLAYING = 1;
    private final static int PAUSED = 2;
    private final static int FINISHED = 3;

    private final Player player;

    private final Object playerLock = new Object();

    private int playerStatus = NOTSTARTED;

    private static boolean isFirstFrame = true;

    public Playing(final InputStream inputStream) throws JavaLayerException {
        this.player = new Player(inputStream);
    }

    /**
     * Method play starts playing the song.
     *
     * @throws JavaLayerException
     */
    public void play() throws JavaLayerException {
        synchronized (playerLock) {
            switch (playerStatus) {
                case NOTSTARTED:
                    final Runnable r = new Runnable() {
                        public void run() {
                            playInternal();
                        }
                    };
                    final Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setPriority(Thread.MAX_PRIORITY);
                    playerStatus = PLAYING;
                    t.start();
                    break;
                case PAUSED:
                    resume();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Method pause pauses song.
     *
     * @return
     */
    public boolean pause() {
        synchronized (playerLock) {
            if (playerStatus == PLAYING) {
                playerStatus = PAUSED;

            }
            return playerStatus == PAUSED;
        }
    }

    /**
     * Method resume resumes playing song.
     *
     * @return
     */
    public boolean resume() {
        synchronized (playerLock) {
            if (playerStatus == PAUSED) {
                playerStatus = PLAYING;
                playerLock.notifyAll();
            }
            return playerStatus == PLAYING;
        }
    }

    /**
     * Method stop stops playing.
     */
    public void stop() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
            playerLock.notifyAll();

        }
    }

    /**
     * Method playInternal plays the song frame by frame.
     */
    private void playInternal() {
        try {
            while (playerStatus != FINISHED) {
                if (!isFirstFrame) {
                    while (playerStatus != FINISHED) {

                        try {
                            if (!player.play(1)) {
                                Controls.getNextButton().doClick();
                                Progress.getButton().doClick();
                            }
                        } catch (final JavaLayerException e) {
                            break;
                        }
                        synchronized (playerLock) {
                            while (playerStatus == PAUSED) {
                                try {
                                    playerLock.wait();
                                } catch (final InterruptedException e) {
                                    break;
                                }
                            }
                        }
                    }
                    close();
                } else {
                    player.play(0);
                    Thread.sleep(1000);
                    isFirstFrame = false;
                }
            }
        } catch (final JavaLayerException e) {
        } catch (InterruptedException e) {
        }
    }

    /**
     * Method close starts playing next song after finishing previous one.
     */
    public void close() {
        synchronized (playerLock) {
            playerStatus = FINISHED;
        }
        try {
            player.close();
        } catch (final Exception e) {
        }
    }

    public static boolean isFirstFrame() {
        return isFirstFrame;
    }

    public static void setFirstFrame(boolean firstFrame) {
        isFirstFrame = firstFrame;
    }
}
