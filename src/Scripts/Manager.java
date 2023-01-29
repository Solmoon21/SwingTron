/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scripts;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author zmoe7
 */
public class Manager extends JPanel {
    private DB_Manager db;
    private final int FPS = 250;
    
    int gameState = 0, option = 1;
    String[] colors = {"red","yellow","pink","green","magenta","orange","blue"};
    JFrame parent;
    JLabel info;
    Holder h1,h2;
    Player p1,p2,win;
    Timer FrameTimer,time;
    int sec;
    
    String firstName,secondName;
    Color firstColor,secondColor;
    
    public Manager(JFrame p) {
        super();
        parent = p;
        gameState = 0;
        
        info = new JLabel("Passed : 00s",SwingConstants.CENTER);
        
        try {   
            db = new DB_Manager();
        } catch (SQLException e) {
            System.out.println(e);
        }
        
        configMenu();
        
        FrameTimer = new Timer(1000 / FPS, new FrameListener());
        FrameTimer.start();
        
    }
    
    void ShowBoard(){
        JFrame tableFrame = new JFrame();
            ArrayList<PlayerData> temp;
            int size = 10;
            try {
                temp = new ArrayList<PlayerData>(db.fetchData());
                size = 10;
                
                tableFrame.setTitle("LeaderBoard");
                String[] th = {"Name", "Score"};
                String[][] tr = new String[size][2];
                
                for (int i = 0; i < size; i++) {
                    tr[i][0] = temp.get(i).getName();
                    tr[i][1] = Integer.toString(temp.get(i).getScore());
                }
                
                JTable table = new JTable(tr, th);
                JScrollPane sp = new JScrollPane(table);
                tableFrame.add(sp);
                tableFrame.setSize(625, 300);
                tableFrame.setVisible(true);
            
            } catch (SQLException ex) {
                System.out.println(ex);
            }
    }
    
    void RegisterKey(Player p,String key,int x,int y){
        if(this.getInputMap().get(KeyStroke.getKeyStroke(key)) == null)
            this.getInputMap().put(KeyStroke.getKeyStroke(key), key);
        this.getActionMap().put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ChangeDir(p.getHolder(),x,y);
            }
        });
    }
    
    void ChangeDir(Holder h,int x,int y){
        h.setVelx(x);
        h.setVely(y);
    }
    
    public void Quit(){
        configMenu();
        time.stop();
        gameState = 0;
    }
    
    public void Pause(){
        if(gameState == 0 || gameState == 3)
            return;
        
        gameState = 3;
        FrameTimer.stop();
        time.stop();
        info.setText("Paused");
    }
        
    public void Resume() {
        if(gameState == 0 || gameState != 3)
            return;
        gameState = 1;
        FrameTimer.start();
        time.start();
    }
        
    public void Start() {
        
        h1 = new Holder(50, 300);
        p1 = new Player(firstName,firstColor,h1);
        h2 = new Holder(750, 300);
        p2 = new Player(secondName,secondColor,h2);
        h1.setVelx(1);
        h2.setVelx(-1);
        this.getInputMap().clear();
        
        RegisterKey(p1,"W",0,-1);
        RegisterKey(p1,"S",0,1);
        RegisterKey(p1,"A",-1,0);
        RegisterKey(p1,"D",1,0);
        RegisterKey(p2,"UP",0,-1);
        RegisterKey(p2,"DOWN",0,1);
        RegisterKey(p2,"LEFT",-1,0);
        RegisterKey(p2,"RIGHT",1,0);
        sec = 0;
        gameState = 1;
        
        if(time != null)
            time.stop();
        time = new Timer(1000,new ActionListener(){
            public void actionPerformed(ActionEvent e){
                sec++;
                info.setText(String.format("Passed : %02ds", sec));
            }
        });
        
        time.start();  
    }
        
    public void configPlayers(){
        firstName = JOptionPane.showInputDialog("Player 1 ");
        secondName = JOptionPane.showInputDialog("Player 2 ");
        
        firstName = firstName == null || firstName.equals("") ? "p1" : firstName;
        secondName = secondName == null || secondName.equals("") ? "p2" : secondName;
        
        //System.out.println(firstName);
        //System.out.println(secondName);
        
        while(firstName.equals(secondName)){
            secondName = JOptionPane.showInputDialog("Player 2 ");
        }
        
        String color = (String)JOptionPane.showInputDialog(null, firstName+"'s Trail Color:", 
            "Choose color", JOptionPane.QUESTION_MESSAGE, null, colors, colors[0]);
        
        try {
            Field field = Class.forName("java.awt.Color").getField(color);
            firstColor = (Color)field.get(null);
        } catch (Exception e) {
            firstColor = Color.GREEN;
        }
        color = (String)JOptionPane.showInputDialog(null, secondName+"'s Trail Color:", 
            "Choose color", JOptionPane.QUESTION_MESSAGE, null, colors, colors[1]);
        try {
            Field field = Class.forName("java.awt.Color").getField(color);
            secondColor = (Color)field.get(null);
        } catch (Exception e) {
            secondColor = Color.orange;
        }
    }
    
    public void drawGame(Graphics g){
        g.setColor(new Color(19,96,147));
        g.fillRect(0, 0, 800, 600);
        p1.getHolder().draw(g);
        p2.getHolder().draw(g);
        p1.getHolder().drawTrail(g,p1.getColor());
        p2.getHolder().drawTrail(g,p2.getColor());
    }
        
    public void configMenu(){
        this.getInputMap().clear();
        
        this.getInputMap().put(KeyStroke.getKeyStroke("W"), "W");
        this.getActionMap().put("W", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    changeOpt(-1);
                }
            });
        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "S");
        this.getActionMap().put("S", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    changeOpt(1);
                }
            });
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "Enter");
        this.getActionMap().put("Enter", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    switch(option){
                        case 0:
                            configPlayers();
                            Start();
                            break;
                        case 1:
                            ShowBoard();
                            break;
                        case 2:
                            System.exit(0);
                            break;
                    }
                }
            });
    }
        
    public void drawMenu(Graphics g) {
        Date d = new Date();
        
        SimpleDateFormat sf = new SimpleDateFormat("hh:mm");
        info.setText(sf.format(d));
        
        int br = 50;
        
        g.setColor(new Color(19,96,147));
        g.fillRect(0, 0, 800, 600);
        g.setFont(g.getFont().deriveFont(Font.BOLD,96F));
        String text = "TRONNY";
        int x = fontcenter(g,text);
        int y = br*3;
        g.setColor(Color.gray);
        g.drawString(text, x+5, y+5);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
        
        
        g.setFont(g.getFont().deriveFont(Font.BOLD,35F));
        text = "NEW GAME";
        x = fontcenter(g,text);
        y += br*4;
        g.drawString(text, x, y);
        if(option == 0){
            g.drawString(">", x-br, y);
        }
        
        text = "HIGH SCORES";
        x = fontcenter(g,text);
        y += br;
        g.drawString(text, x, y);
        if(option == 1){
            g.drawString(">", x-br, y);
        }
        
        text = "EXIT";
        x = fontcenter(g,text);
        y += br;
        g.drawString(text, x, y);
        if(option == 2){
            g.drawString(">", x-br, y);
        }
    }
        
    int fontcenter(Graphics g,String s){
        int n = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
        return 400-n/2;
    }
        
    public void changeOpt(int n){
        option = (option+n+3)%3;
        //System.out.println(option);
    }
        
    public void Game(){
        // 0 Menu, 1 Playing
        if(gameState == 0)
            return;
        
        p1.getHolder().move();
        p2.getHolder().move();
        ArrayList<PlayerData> scores = new ArrayList<>();
        try {
            scores = db.fetchData();
        } catch (SQLException e) {
            System.out.println(e);
        }
        if(p1.kill(p2) || p2.out()){
            win = p1;
           
        }
        else if(p2.kill(p1) || p1.out()){
            win = p2;
            
        }
        if(win != null){
            info.setText(String.format("Passed : %02ds", sec));
            time.stop();
            if(gameState != 2){
                JOptionPane.showMessageDialog(null,win.getName() + " Won");
            }
                
            boolean exists = scores.stream()
                        .filter(e -> win.getName().equals(e.getName()))
                        .findAny()
                        .orElse(null) != null;
            try{
                if(!exists){
                    db.add(win.getName());
                }
                else{
                    db.increase(win.getName());
                }
                int result = -1;
                if(gameState != 2){
                    result = JOptionPane.showConfirmDialog(parent,"Rematch?", win.name+" victory",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
                    gameState = 2;
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            Start();
                            break;
                        case JOptionPane.NO_OPTION:
                            configMenu();
                            gameState = 0;
                            break;
                        default:
                            configMenu();
                            gameState = 0;
                            break;
                    }
                }
                win = null;
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }
        
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        switch(gameState){
            case 0:
                drawMenu(g);
                break;
            case 1:
                drawGame(g);
                break;
            default:
                break;
        }
    }
    
    public JLabel getLabel() {
        return info;
    }
        
    class FrameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            switch(gameState){
                case 1:
                    Game();
                    break;
                default:
                    break;
            }
            repaint();
        }
    }   

}
    
