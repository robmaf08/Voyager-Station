package com.voyager.voyager_station.sound;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Roberto Maffucci
 */
public class SoundGame {

    private File file = null;

    private AudioInputStream audioStream;

    private Clip clip;

    private String lastSoundReproduced;

    private boolean enabled = false;

    private boolean stopped = false;

    private boolean reproducing = false;

    public enum BackgroundSound {
        MENU,
        PROLOGUE,
        STABLE,
        FINAL
    }

    public SoundGame() {
        this.audioStream = null;
        this.stopped = false;
        this.reproducing = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isReproducing() {
        return reproducing;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void menu() {
        play(CostantsSoundPath.MENU);
    }

    public void play(String pathSound) {
        try {
            file = new File(pathSound);
            audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            lastSoundReproduced = pathSound;
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            stopped = false;
            reproducing = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(SoundGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void playOnce(String pathSound, int time) {
        try {
            int seconds = 0;
            file = new File(pathSound);
            audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            while (seconds != time) {
                try {
                    Thread.sleep(1000);
                    seconds++;
                } catch (InterruptedException ex) {
                    Logger.getLogger(SoundGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(SoundGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resume() {
        if (stopped) {
            play(lastSoundReproduced);
            stopped = false;
            reproducing = true;
        }
    }

    public void stop() {
        clip.stop();
        stopped = true;
        reproducing = false;
    }
}
