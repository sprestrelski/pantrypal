package code.client.Model;

import java.io.*;
import javax.sound.sampled.*;

import code.AppConfig;

public class AppAudioRecorder extends BaseAudioRecorder {
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;

    public AppAudioRecorder() {
        this.audioFormat = getAudioFormat();
    }

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;
        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;
        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 2;
        // whether the data is signed or unsigned.
        boolean signed = true;
        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    private void recordOnThread() {
        try {
            // the format of the TargetDataLine
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            // the TargetDataLine used to capture audio data from the microphone
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            // the AudioInputStream that will be used to write the audio data to a file
            AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
            // the file that will contain the audio data
            File audioFile = new File(AppConfig.AUDIO_FILE);
            AudioSystem.write(audioInputStream, AppConfig.AUDIO_TYPE, audioFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void startRecording() {
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        recordOnThread();
                    }
                });
        thread.start();
    }

    @Override
    public void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
    }
}
