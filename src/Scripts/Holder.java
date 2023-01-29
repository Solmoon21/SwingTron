/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

/**
 *
 * @author zmoe7
 */
public class Holder extends GameObject{
    
    double velx,vely;
    ArrayList<Trail> line;
    
    
    public Holder(int x, int y) {
        super(x, y);
        line = new ArrayList<>();
        velx = vely = 0;
    }
    
    public void move() {
        x += velx;
        y += vely;
        
        addTrail(x,y);
    }
    
    public void drawTrail(Graphics g,Color c) {
        g.setColor(c);
        for(int i=0; i<line.size(); i++) {
            g.fillRect(line.get(i).getX(),line.get(i).getY(),10,10);
        }
    }

    public double getVelx() {
        return velx;
    }

    public void setVelx(double velx) {
        this.velx = velx;
    }
    
    public double getVely() {
        return vely;
    }

    public void setVely(double vely) {
        this.vely = vely;
    }
    
    public void addTrail(int x,int y){
        if(line.size()>100) line.remove(0);
        line.add(new Trail(x,y));
        
    }
    public ArrayList<Trail> getTrail(){
        return this.line;
    }
    
}
