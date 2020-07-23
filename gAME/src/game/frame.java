/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import javax.sound.sampled.AudioFormat;
//import javax.sound.sampled.AudioInputStream;
//import javax.sound.sampled.AudioSystem;
//import javax.sound.sampled.Clip;
//import javax.sound.sampled.DataLine;
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;
public class frame extends JFrame{
    JPanel board;
    frame()
    {
        initComponents();
    }
    void initComponents()
    {
        board=new PacBoard();
        add(board);
        setSize(28*24+17,28*24+38);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //setResizable(false);
        setVisible(true);
        
    }
    public static void main(String[] args) {
        frame f=new frame();
        f.setVisible(true);
        
    }
}