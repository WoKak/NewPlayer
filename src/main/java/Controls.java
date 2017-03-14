import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javazoom.jl.decoder.JavaLayerException;

/**
 * Class Controls is responcibe for all the buttons used by the program.
 * Created by krystian on 24.10.16.
 */
public class Controls extends JPanel {

    private JPanel buttonPanel;
    private static JButton playButton;
    private static JButton previousButton;
    private static JButton nextButton;
    private static JButton randomButton;
    private static JButton stopButton;
    private static JButton addButton;


    static ImageIcon pauseIcon = new ImageIcon("icons/pause.png");

    /**
     * Method Controls defines all the buttons, sets icons and funcions of the buttons.
     * @param width
     */
    Controls(int width) {
        buttonPanel = new JPanel();

        playButton = new JButton();
        previousButton = new JButton();
        nextButton = new JButton();
        randomButton = new JButton();
        stopButton = new JButton();
        addButton = new JButton();

        makeButton(previousButton, width - 54, "icons/prev.png");
        makeButton(playButton, width - 54, "icons/play.png");
        makeButton(nextButton, width - 54, "icons/next.png");
        makeButton(randomButton, width - 54, "icons/rand.png");
        makeButton(stopButton, width - 54, "icons/stop.png");
        makeButton(addButton, width - 54, "icons/add.png");

        playButton.addActionListener(new PlayAction());
        playButton.setToolTipText("Odtwarzaj/Zatrzymaj muzykę");

        /**
         * ActionListener addButton is responsible for opening a new window, 
         * leting user choose a file with required extention and add it to playlist.
         */
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Tylko pliki .mp3", "mp3");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setCurrentDirectory(new File("*.*"));
                chooser.showOpenDialog(getParent());

                if(!Optional.of(chooser.getSelectedFile()).isPresent()) {
                    return;
                }

                Playlist.add(chooser.getSelectedFile().getPath());
            }
        });
        addButton.setToolTipText("Dodaj piosenkę");

        /**
         * ActionListener previousButton checks if there is a song before current one.
         * If there is one, it starts playing it and sets as Current.
         */
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Controls.getPlayButton().setIcon(pauseIcon);
                File tmp = Playlist.getCurrent();
                int idx = Playlist.find(tmp);
                try {
                    if (idx < 1) {

                        return;

                    } else {

                        //Stops current songs and starts playing previous one
                        Playlist.getSongs().get(Playlist.find(Playlist.getCurrent())).getLabel().setForeground(Color.BLACK);
                        tmp = Playlist.getSongs().get(idx - 1).getFile();
                        Playlist.setCurrent(tmp);
                        PlayAction.getPlaying().stop();
                        PlayAction.setPlay(0);
                        PlayAction.destroyPlaying();
                        PlayAction.getPlaying();

                        //Progress Bar operations
                        Progress.getProgressBar().setValue(0);
                        Progress.setProgress(0.0);
                        Progress.setMax(Progress.duration(Playlist.getCurrent()));
                        Progress.setTick(100 / Progress.getMax());

                        //flag operatons
                        Playing.setFirstFrame(true);

                        PlayAction.getPlaying().play();
                    }
                } catch (JavaLayerException ex) {
                } catch (IndexOutOfBoundsException ex) {
                } catch (IOException ex) {
                } catch (UnsupportedAudioFileException ex) {
                }
            }
        });
        previousButton.setToolTipText("Poprzednia piosenka");

        /**
         * ActionListener nextButton checks if there is a song after current one.
         * If there is one, it starts playing it and sets as Current.
         */
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                Controls.getPlayButton().setIcon(pauseIcon);

                try {

                    File tmp = Playlist.getCurrent();
                    int idx = Playlist.find(tmp);

                    if (idx == Playlist.getSongs().size())
                        return;
                    else {

                        //Stops current songs and starts playing next one
                        Playlist.getSongs().get(Playlist.find(Playlist.getCurrent())).getLabel().setForeground(Color.BLACK);
                        tmp = Playlist.getSongs().get(idx + 1).getFile();
                        Playlist.setCurrent(tmp);
                        PlayAction.getPlaying().stop();
                        PlayAction.setPlay(0);
                        PlayAction.destroyPlaying();
                        PlayAction.getPlaying();

                        //Progress Bar operations
                        Progress.getProgressBar().setValue(0);
                        Progress.setProgress(0.0);
                        Progress.setMax(Progress.duration(Playlist.getCurrent()));
                        Progress.setTick(100 / Progress.getMax());

                        //flag operatons
                        Playing.setFirstFrame(true);

                        PlayAction.getPlaying().play();
                    }
                } catch (JavaLayerException ex) {
                } catch (IndexOutOfBoundsException ex) {
                } catch (IOException ex) {
                } catch (UnsupportedAudioFileException ex) {
                }
            }
        });
        nextButton.setToolTipText("Następna piosenka");

        /**
         * ActionListener stopButton stops current song.
         */
        stopButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    File tmp = Playlist.getCurrent();
                    int idx = Playlist.find(tmp);

                    Controls.nextButton.doClick();
                    Controls.previousButton.doClick();
                    Controls.playButton.doClick();
                    Playlist.getSongs().get(idx).getLabel().setForeground(Color.black);

                } catch (Exception ex) {
                }
            }
        });
        stopButton.setToolTipText("Stop");

        /**
         * ActionListener randomButton chooses randomly a new song from playlist and plays it.
         */
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    File tmp = Playlist.getCurrent();
                    int idx = Playlist.find(tmp);

                    Random r = new Random();
                    int t = r.nextInt(Playlist.getSongs().size() - 1);
                    //System.out.println(t); this lines role is checking if program runs correctly

                    //Stops current song and starts playing another randomly chosen one
                    Playlist.getSongs().get(Playlist.find(Playlist.getCurrent())).getLabel().setForeground(Color.BLACK);
                    tmp = Playlist.getSongs().get(t).getFile();
                    Playlist.setCurrent(tmp);
                    PlayAction.getPlaying().stop();
                    PlayAction.setPlay(0);
                    PlayAction.destroyPlaying();
                    PlayAction.getPlaying();

                    //bar operations
                    Progress.getProgressBar().setValue(0);
                    Progress.setProgress(0.0);
                    Progress.setMax(Progress.duration(Playlist.getCurrent()));
                    Progress.setTick(100 / Progress.getMax());

                    //flag operations
                    Playing.setFirstFrame(true);

                    PlayAction.getPlaying().play();
                } catch (Exception ex) {
                }
            }
        });
        randomButton.setToolTipText("Losuj piosenkę");

        add(buttonPanel);
    }

    /**
     * Method makeButton creates button
     * @param button
     * @param width
     * @param icon
     */
    private void makeButton(JButton button, int width, String icon) {
        int wdt = 50;
        int hgt = 50;
        button.setPreferredSize(new Dimension(wdt, hgt));

        ImageIcon img = new ImageIcon(icon);
        button.setIcon(img);

        buttonPanel.add(button);

    }

    public static JButton getPlayButton() {

        return playButton;
    }

    public static JButton getNextButton() {

        return nextButton;
    }
}