package code;

import org.junit.jupiter.api.Test;
import code.client.Model.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

public class AudioRecorderTest {
    private BaseAudioRecorder audioRecorder;

    @Test
    public void testRecording() {
        Writer writer = new StringWriter();
        audioRecorder = new MockAudioRecorder(writer);
        audioRecorder.startRecording();
        audioRecorder.stopRecording();
        String expected = "Recipe recorded.";
        assertEquals(expected, writer.toString());
    }
}
