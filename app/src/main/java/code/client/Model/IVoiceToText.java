package code.client.Model;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IVoiceToText {
    String processAudio() throws IOException, URISyntaxException;
}
