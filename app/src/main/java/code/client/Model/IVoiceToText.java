package code.client.Model;

interface IVoiceToText {
    String processAudio();

    IHttpConnection sendHttpRequest();
}