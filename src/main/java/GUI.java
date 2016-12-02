import javax.swing.*;
import java.awt.*;

/**
 * Class GUI is responsible for runing the program.
 * Created by MichaĹ‚ on 2016-10-24.
 */
public class GUI {
    /**
     * Method main is a main method of this program, creates AudioFrame and sets its featurs.
     * @param args 
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AudioFrame frame = new AudioFrame();
                frame.setTitle("AudioPlayer");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setVisible(true);
            }
        });
    }
}

