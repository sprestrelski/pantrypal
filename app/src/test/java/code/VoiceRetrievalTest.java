package code;

import javafx.scene.control.Label;

import java.io.*;
import org.junit.jupiter.api.Test;

import code.client.Controllers.AudioRecorder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import javafx.application.Platform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Work In Progress Voice Retrieval Unit Test
 */
public class VoiceRetrievalTest {
    private AudioRecorder audioRecorder;
    private Label recordingLabel;

    @BeforeAll
    static void initJfxRuntime() {
        Platform.startup(() -> {
        });
    }

    @BeforeEach
    void setUp() {
        recordingLabel = new Label("Recording");
        audioRecorder = new AudioRecorder(recordingLabel);
    }

    void testAudioSave() throws InterruptedException {
        audioRecorder.startRecording();
        Thread.sleep(1000);
        audioRecorder.stopRecording();
        File file = new File("recording.wav");
        assertTrue(file.exists());
    }

    void testLabelChanges() throws InterruptedException {
        audioRecorder.startRecording();
        assertTrue(recordingLabel.isVisible());
        Thread.sleep(1000);

        audioRecorder.stopRecording();
        assertFalse(recordingLabel.isVisible());
    }

}
