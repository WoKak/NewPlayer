import javax.swing.*;
import java.io.File;

/**
 * Created by Michał on 2016-11-14.
 * Class Song stores the song and its label.
 */
public class Song {
    private File file;
    private JLabel label;
    /**
     * File getter
     * @return 
     */
    public File getFile() {
        return file;
    }
    /**
     * File setter
     * @param file 
     */
    public void setFile(File file) {
        this.file = file;
    }
    /**
     * Label getter
     * @return 
     */
    public JLabel getLabel() {
        return label;
    }
    /**
     * Label setter
     * @param label 
     */
    public void setLabel(JLabel label) {
        this.label = label;
    }
    /**
     * method Song sets songs file and label
     * @param file
     * @param label 
     */
    public Song(File file, JLabel label) {
        this.file = file;
        this.label = label;
    }
}
