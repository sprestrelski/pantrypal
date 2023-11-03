package tests;

import client.Controllers.AudioRecorder;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VoiceRetrievalTest {
    private AudioRecorder audioRecorder;
    private Label recordingLabel;

    void setUp() {
        recordingLabel = new Label();
        audioRecorder = new AudioRecorder(recordingLabel);
    }

    @Test
    void testAudioSave() {
        audioRecorder.startRecording();
        audioRecorder.stopRecording();
        File file = new File("recording.wav");
        assertTrue(file.exists());
    }

    @Test
    void testLabelChanges() {
        audioRecorder.startRecording();
        assertTrue(recordingLabel.isVisible());

        audioRecorder.stopRecording();
        assertFalse(recordingLabel.isVisible());
    }

}