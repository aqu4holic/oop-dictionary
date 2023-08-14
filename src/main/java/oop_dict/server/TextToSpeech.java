package oop_dict.server;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javazoom.jl.player.Player;

public class TextToSpeech {
	public static void playSoundGoogleTranslate(String text, String tl) {
		try {
			String urlStr = "https://translate.google.com/translate_tts?ie=UTF-8&tl="
					+ tl
					+ "&client=tw-ob&q="
					+ URLEncoder.encode(text, StandardCharsets.UTF_8);

			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			InputStream audio = con.getInputStream();
			new Player(audio).play();
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}