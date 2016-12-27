import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Class AudioFrame creates Visual Interface responcible for comunication with the user
 * Created by MichaĹ‚ on 2016-11-13.
 */
public class AudioFrame extends JFrame{
    public AudioFrame() {
     /**
     * Method AudioFrame sets the size of a window and logo it contains.
     * Also this method initializes all the features and sets their location in the window.
     */
        int screenHeight = 1080;
        int screenWidth = 1920;

        setSize(screenWidth / 2 - 600, screenHeight / 2 );
        setLocationByPlatform(true);

        Image img = new ImageIcon("icons/kni.gif").getImage();
        setIconImage(img);

        Visualiser visualiser = new Visualiser(screenWidth, screenHeight);
        Controls controls = new Controls(screenWidth / 2 - 600);
        Progress current = new Progress(screenWidth / 2 - 600);
        Playlist playlist = new Playlist(screenWidth / 2 - 600);

        JPanel main = new JPanel();

        JPanel mainTop = new JPanel();
        JPanel mainBot = new JPanel();
        JPanel mainCen = new JPanel();

        mainTop.add(visualiser);

        mainBot.setLayout(new GridLayout(2, 1, 0, 0));
        mainBot.add(controls);
        mainBot.add(current);

        mainCen.add(playlist);

        main.setLayout(new BorderLayout());

        main.add(mainTop, BorderLayout.NORTH);
        main.add(mainCen, BorderLayout.CENTER);
        main.add(mainBot, BorderLayout.SOUTH);

        add(main);

        JMenuBar menuBar = new JMenuBar();

        JMenu playlistMenu = new JMenu("Playlista");
        JMenuItem save = new JMenuItem("Zapisz", new ImageIcon("icons/save.png"));
        JMenuItem load = new JMenuItem("Wczytaj", new ImageIcon("icons/load.png"));

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File("savedplaylists/savedplaylist.txt");
                    FileWriter writer = new FileWriter(file);

                    for (int i = 0; i < Playlist.getSongs().size(); i++) {
                        writer.write(Playlist.getSongs().get(i).getFile().getPath());
                        writer.write("\n");
                    }

                    writer.close();
                } catch (IOException ex) {}
            }
        });
        save.setToolTipText("Zapisuje playlistę");

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File("savedplaylists/savedplaylist.txt");
                    FileReader reader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(reader);

                    String line = bufferedReader.readLine();

                    while (line != null) {
                        Playlist.add(line);
                        line = bufferedReader.readLine();
                    }
                } catch (IOException ex) {}
            }
        });
        load.setToolTipText("Pozwala wczytać ostatnio zapisaną playlistę");

        playlistMenu.add(save);
        playlistMenu.add(load);

        menuBar.add(playlistMenu);

        this.setJMenuBar(menuBar);

    }
}
