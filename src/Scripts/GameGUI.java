/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scripts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author zmoe7
 */
public class GameGUI {
    private JFrame frame;
    private Manager manager;    

    public GameGUI() {
        frame = new JFrame("Tron");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Game");
        JMenuItem menuPause = new JMenuItem(new AbstractAction("Pause"){
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.Pause();
            }
        });
        
        JMenuItem menuResume = new JMenuItem(new AbstractAction("Resume"){
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.Resume();
            }
        });
        
        JMenuItem menuQuit = new JMenuItem(new AbstractAction("Quit"){
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.Quit();
            }
        });
        
        JMenuItem menuRestart = new JMenuItem(new AbstractAction("Restart") {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.Start();
            }
        });
        
        menuGame.add(menuPause);
        menuGame.add(menuResume);
        menuGame.add(menuRestart);
        menuGame.add(menuQuit);
        menuBar.add(menuGame);
        frame.setJMenuBar(menuBar);
        
        manager = new Manager(frame);
        frame.getContentPane().add(manager);
        frame.getContentPane().add(manager.getLabel(), BorderLayout.NORTH);
        frame.setPreferredSize(new Dimension(800, 720));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
    
}
