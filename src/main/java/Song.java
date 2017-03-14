import javax.swing.*;
import java.io.File;

/**
 * Created by Michał on 2016-11-14.
 * Class Song stores the song and its label.
 */
public class Song {
    private File file;
    private JLabel label;

    public File getFile() {
        return file;
    }

    public JLabel getLabel() {
        return label;
    }

    public Song(File file, JLabel label) {
        this.file = file;
        this.label = label;
    }
}
