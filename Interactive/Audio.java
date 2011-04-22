package Interactive;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class Audio {

	private URL url;
	@SuppressWarnings("unused")
	private String filename;
	private AudioClip ac;
	public static boolean isPlaying;

	public Audio() {}
	
	public Audio(String filename) {
		isPlaying = false;
		this.filename = filename;
		url = getClass().getResource("audio/" + filename);
		ac = Applet.newAudioClip(url);
	}

	public void playAudio() {
		ac.loop();
		isPlaying = true;
	}

	public void stopAudio() {
		try{
			ac.stop();
			isPlaying = false;
		}catch (Exception e){
			
		}
	}
}
