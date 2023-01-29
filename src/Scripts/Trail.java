/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scripts;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author zmoe7
 */
public class Trail {
    private int x, y;
    
    public Trail(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }
    
    public boolean collides(int x1,int y1) {
        Rectangle rect = new Rectangle(x, y, 20, 20);
        Rectangle otherRect = new Rectangle(x1, y1, 20, 20);        
        return rect.intersects(otherRect);
    }
    

}
