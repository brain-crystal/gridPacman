/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
import java.awt.EventQueue;
import javax.swing.JFrame;
/**
 *
 * @author Shweta
 */

public class SwingTimerExample extends JFrame {
    public SwingTimerExample() {
        initUI();
    }
    private void initUI() {
        add(new Board());        
        setResizable(false);
        pack();
        setTitle("Star");
        setLocationRelativeTo(null);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        
       // EventQueue.invokeLater(new Runnable() {
         //   @Override
           // public void run() {                
                JFrame ex = new SwingTimerExample();
                ex.setVisible(true);                
            //}
       // });
    }
}