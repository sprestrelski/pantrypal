package code.client.Model;

import java.io.IOException;
import java.io.Writer;

public class MockAudioRecorder extends BaseAudioRecorder {
    private Writer writer;

    public MockAudioRecorder(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void startRecording() {
    }

    @Override
    public void stopRecording() {
        try {
            writer.write("Recipe recorded.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
