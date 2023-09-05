import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


//add difficulty delay (easy,normal,hard)
//add on/off for grid
//add restart game

public class GamePanel extends JPanel implements ActionListener{

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;//how big do we want the objects in the game(in this case 25x25 pixels)
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;//calculate how many objects we can fit on the screen
    private int DELAY = 75;//this will dictate how fast the game is running
    private final int x[] = new int[GAME_UNITS];//this holds all of the x coordinates of the body parts of the snake(size of the array is set to game_units)
    private final int y[] = new int[GAME_UNITS];;//this holds all of the y coordinates of the body parts of the snake(size of the array is set to game_units)
    private int bodyParts = 6;//initial amount of body parts
    private int applesEaten;
    private int appleX;//x coordinate of the apple
    private int appleY;//y coordinate of the apple
    private char direction = 'R';//initial direction of the snake
    private boolean running = false;
    private Timer timer;
    private Random random;
    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);//we also have to put in this as sa second parameter because we are implementing Action Listener interface
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {

        if (running) {
            //this is used to enable the grid
            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
                }
            */

            //draw apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);//shape,x and y coordinate, and the size of the apple

            //draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {//if i=0 then this is the head of the snake
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);//fill a cube as a snakes head
                }else{//if i is not 0 then this is the body of the snake
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);//fill a cube as a snakes body
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Monospaced",Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            //we will put the score in the middle of the screen by x and on the top of the screen by y
            g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }
    public void newApple(){
        //generation of the x and y coordinates of the apple
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts;i>0;i--){//this for loop iterates through all body parts of the snake
            x[i] = x[i-1];//we are shifting all coordinates in the x array by one spot
            y[i] = y[i-1];//we are shifting all coordinates in the y array by one spot
        }

        switch (direction){
            case 'U'://u is for up
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D'://d is for down
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L'://l is for left
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R'://r is for right
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        //if head of the snake is same x y coordinate as the apple then add +1 bodypart and add +1 to the score
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        //checks if there is a collision between head and the body. this will trigger a game over
        for(int i = bodyParts;i>0;i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        //check if head touches left border
        if(x[0] < 0){
            running = false;
        }
        //check if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0){
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){//if the game is no longer running, stop the timer
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Monospaced",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        //we will put the game over text in the middle of the screen by x and in the center by y
        g.drawString("Game Over",(SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);


        g.setColor(Color.red);
        g.setFont(new Font("Monospaced",Font.BOLD,40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        //we will put the score in the middle of the screen by x and on the top of the screen by y
        g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Monospaced",Font.BOLD,20));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press R to restart",(SCREEN_WIDTH - metrics3.stringWidth("Press R to restart"))/2,(SCREEN_HEIGHT/10)*2);
        g.drawString("Press ESC to exit",(SCREEN_WIDTH - metrics3.stringWidth("Press ESC to exit"))/2,(SCREEN_HEIGHT/10)*3);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){//if the game is running
            move();//call the move method
            checkApple();//check if the snake touches the apple
            checkCollisions();//and check if the snake touched the walls or itself
        }
        repaint();//if the game is no longer running then repaint
        //When call to repaint method is made, it performs a request to erase and perform redraw of the component after a small delay in time
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                //since we will only allow 90 degree turns I added an if statement to check the direction
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_R://when game is no longer running and the user presses the R button, restart the game and close the old window
                    if(!running){
                        Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
                        window.dispose();
                        new GameFrame();
                    }
                    break;
                case KeyEvent.VK_ESCAPE://when the user presses the ESC button, close the application
                    System.exit(0);
                    break;
            }
        }
    }
}
