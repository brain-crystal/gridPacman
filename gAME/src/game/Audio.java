/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author Shweta
 */
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio
{
    private boolean loop = false;
    private AudioInputStream ais = null;
    private Clip clip = null;
    //declaration of variables

    public Audio (String fileName, boolean loop)
    //Constructor for the class which fileName and accepts whether the clip needs to loop or not
    {
        this.loop = loop;
        //sets the variable within the class as constructor 

        try {
            clip = AudioSystem.getClip();
            ais = AudioSystem.getAudioInputStream(Audio.class.getResource(fileName));
            clip.open(ais);

        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
        //tries to load file into java's built in audio player, else prints the error to console
    }

    public void musicStart ()
    //starts music
    {
        if (loop)
        {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            //starts music on loop if loop is requested
        }
        else
        {
            clip.start();
            //starts music as not on loop
        }

    }


    public void musicStop ()
    //stops the music
    {
        clip.stop();
    }

}