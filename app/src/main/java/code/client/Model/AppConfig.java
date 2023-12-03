package code.client.Model;

import javax.sound.sampled.AudioFileFormat;

public class AppConfig {
    public static final String APP_NAME = "Pantry Pal";
    public static final String CSV_FILE = "recipes.csv";
    public static final String AUDIO_FILE = "recording.wav";
    public static final AudioFileFormat.Type AUDIO_TYPE = AudioFileFormat.Type.WAVE;
    public static final String API_KEY = "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";
    public static final String MICROPHONE_IMG_FILE = "app/src/main/java/code/client/View/microphone.png";
    public static final String OFFLINE_IMG_FILE = "app/src/main/java/code/client/View/cat.png";
    public static final String RECIPE_IMG_FILE = "app/src/main/java/code/client/View/defaultRecipe.png";
    public static final String MONGODB_CONN = "mongodb://trungluu:xGoGkkbozvWyiXyZ@ac-ajwebab-shard-00-00.lta1oi1.mongodb.net:27017,ac-ajwebab-shard-00-01.lta1oi1.mongodb.net:27017,ac-ajwebab-shard-00-02.lta1oi1.mongodb.net:27017/?ssl=true&replicaSet=atlas-3daxhg-shard-0&authSource=admin&retryWrites=true&w=majority";
    public static final String MONGO_DB = "pantry_pal";
    public static final String MONGO_RECIPE_COLLECTION = "recipes";
    public static final String MONGO_USER_COLLECTION = "users";
    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8100;
    public static final String SERVER_URL = "http://" + SERVER_HOST + ":" + SERVER_PORT;
}
