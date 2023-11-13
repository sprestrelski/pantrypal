package code.client.Model;

public abstract class VoiceToText {
    private String apiEndPoint;
    private String apiKey;
    private String model;
    private IHttpConnection connection;

    public VoiceToText(String apiEndPoint, String apiKey, String model) {
        
    }

    abstract String processAudio();
    abstract IHttpConnection sendHttpRequest();
}