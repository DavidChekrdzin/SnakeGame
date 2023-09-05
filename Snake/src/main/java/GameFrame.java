import javax.swing.*;
import java.awt.event.*;

public class GameFrame extends JFrame{
    public GamePanel panel;

    GameFrame(){
        panel = new GamePanel();

        this.add(panel);
        this.setTitle("Zmijica");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();//this will pack all the components we add snuggly into our frame
        this.setVisible(true);
        this.setLocationRelativeTo(null);//this will set the window to the middle of our screen
    }
}
