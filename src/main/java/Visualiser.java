import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class Visualiser is responsible for displaying KNI logo at the top of graphical interface.
 * Created by MichaĹ‚ on 2016-10-24.
 */
public class Visualiser extends JPanel {
    /**
     * Method Visualiser creats a panel containing KNI logo which size is determined by the imput
     * @param width
     * @param height 
     */
    public Visualiser(int width, int height) {
        JPanel panel = new JPanel();
        panel.setSize(new Dimension(width, height - 400));

        try{
            BufferedImage img = ImageIO.read(new File("icons/kni.gif"));
            JLabel picLabel = new JLabel(new ImageIcon(img));
            panel.add(picLabel);
        } catch (IOException e) {
            System.out.println("Kicha!");
        }

        add(panel);
    }
}
