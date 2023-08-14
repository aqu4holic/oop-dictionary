package oop_dict.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TranslatorAPI {
	private static String translate(String langFrom, String langTo, String text) throws IOException {
		String urlStr = "https://script.google.com/macros/s/AKfycbxzCfT78zpe2hPNd75uEzo7Joq2m-ach6UxoEuKpnf0JEgUcBc4C7SKL6QFeZ5ghDJM/exec"
				+
				"?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
				"&target=" + langTo +
				"&source=" + langFrom;
		URL url = new URL(urlStr);
		StringBuilder response = new StringBuilder();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	public static String translateEnToVi(String text) {
		try {
			return translate("en", "vi", text);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "500";
	}

	public static String translateViToEn(String text) {
		try {
			return translate("vi", "en", text);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "500";
	}
}