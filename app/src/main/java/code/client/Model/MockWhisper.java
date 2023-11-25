package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public class MockWhisper implements IVoiceToText {
    public String processAudio() throws IOException, URISyntaxException {
        return "Invoked mock audio processing";
    }
}