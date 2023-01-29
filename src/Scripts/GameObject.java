/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author zmoe7
 */
public class GameObject {
    int x,y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x-5, y-5, 20, 20);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}
