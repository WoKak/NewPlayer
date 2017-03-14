import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 * Class Playlist is responsible for the playlist. It stores files added to the
 * program and names of songs shown to the user by the graphic interface.
 * Created by MichaĹ‚ on 2016-10-25.
 */
public class Playlist extends JPanel {


    private static ArrayList<Song> songs;
    private static File current;
    private static JPanel list;

    /**
     * Method Playlist creates both playlists. The one shown to the user by the
     * interface and ArrayList storing songs. It also sets currently played song to null.
     *
     * @param width
     */
    public Playlist(int width) {

        JPanel current = new JPanel();
        current.setPreferredSize(new Dimension(width - 20, 500));
        list = new JPanel();
        list.setLayout(new GridLayout(0, 1));
        current.add(list);
        add(current);

        this.songs = new ArrayList<>();
        this.current = null;
    }

    /**
     * Method add adds a new song chosen by the user to both playlists.
     *
     * @param path
     */
    public static void add(String path) {

        File file = new File(path);

        JLabel toAdd = new JLabel(file.getName());
        Song newSong = new Song(file, toAdd);

        songs.add(newSong);
        list.add(toAdd);
        list.revalidate();
        list.repaint();
    }

    public static ArrayList<Song> getSongs() {
        return songs;
    }

    public static File getCurrent() {
        return current;
    }

    public static void setCurrent(File f) {
        current = f;
    }

    /**
     * Method find finds an index of a song from imput.
     *
     * @param
     * @return
     */
    public static int find(File f) {

        for (Song s : songs) {
            if (s.getFile().equals(f))
                return songs.indexOf(s);
        }

        return -1;
    }

}
