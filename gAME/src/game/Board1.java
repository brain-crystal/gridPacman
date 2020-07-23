package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
public class Board1 extends JPanel{
    Point p1=new Point(10,10);
    Point p2=new Point(20,20);
    Dimension d=new Dimension(40, 40);
    Board1()
    {
        initUI();
    }
    private void initUI()
    {
        //setSize(getParent().getWidth(),getParent().getHeight());
        //setBackground(Color.black);
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        Rectangle r1=new Rectangle(p1,d);
        g.setColor(new Color(217,146,54));
        Graphics2D g2=(Graphics2D)g;
        //g2.setStroke(new BasicStroke(1));
        GradientPaint gp1= new GradientPaint(5,20,Color.PINK, 5,40, Color.black,true);
        setBackground(Color.BLACK);
         g2.setPaint(gp1);
         g.fillRect(0,0,getParent().getWidth(),getParent().getHeight());
        //g.drawLine(100,50,120,50);
        Toolkit.getDefaultToolkit().sync();
    }
    
}
