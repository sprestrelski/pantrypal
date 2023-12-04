package code.client.Model;

public abstract class BaseAudioRecorder {
    protected boolean recording;

    public abstract void startRecording();

    public abstract void stopRecording();

    public boolean toggleRecording() {
        if (recording) {
            stopRecording();
        } else {
            startRecording();
        }

        recording = !recording;
        return recording;
    }
}