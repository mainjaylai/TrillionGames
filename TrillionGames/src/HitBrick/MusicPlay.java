package HitBrick;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class MusicPlay {
    private AudioClip brick;
    private AudioClip stick;
    private Random rand = new Random();

    MusicPlay() {
        try {
            brick = Applet.newAudioClip(new File("music/brick.mp3").toURL());
            stick = Applet.newAudioClip(new File("music/stick.mp3").toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PlayBrick() {
        brick.play();
    }

    public void PlayStick() {
        stick.play();
    }
}


