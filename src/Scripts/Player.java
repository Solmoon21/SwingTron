/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scripts;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author zmoe7
 */
public class Player {
    String name;
    Color color;
    Holder hold;
    
    Player(String name,Color color,Holder hold)
    {
        this.name = name;
        this.color = color;
        this.hold = hold;
    }
    
    public String getName(){
        if(!this.name.equals("")){
            return this.name;
        }
        return "<PLAYER>";
    }
    
    public Holder getHolder(){
        return this.hold;
    }
    
    public Color getColor(){
        return this.color;
    }
    
    public boolean kill(Player other){
        
        ArrayList<Trail> tr1 = hold.getTrail();
        
        for(int i=0;i<tr1.size();i++)
        {
            if (tr1.get(i).collides(other.getHolder().getX(),other.getHolder().getY())){
                return true;
            }
        }
        
        return false; 
    }
    
    public boolean out(){
        return (hold.getX()+20>800 || hold.getY()+10>600 || hold.getX()-5<0 || hold.getY()<0);
    }
}
