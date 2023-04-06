import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
public class SimpleAudioPlayer {

    // to store current position
    Long currentFrame;
    Clip clip;

    AudioInputStream audioInputStream;
    static String filePath;

    public SimpleAudioPlayer(String filePath)
            throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        // create AudioInputStream object
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

        // create clip reference
        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);
    }

    public void play() {
        //clip.setFramePosition(0);
        clip.start();
        //start the clip
    }

    public void pause(){
        clip.stop();
        // reset clip
        clip.setFramePosition(0);
    }

}
