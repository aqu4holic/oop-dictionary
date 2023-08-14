package oop_dict.server;

public class Word {
	private String wordTarget, wordExplain;

	public Word(String wordTarget, String wordExplain) {
		this.wordTarget = wordTarget;
		this.wordExplain = wordExplain;
	}

	public void setWordTarget(String x) {
		wordTarget = x;
	}

	public String getWordTarget() {
		return wordTarget;
	}

	public void setWordExplain(String x) {
		wordExplain = x;
	}

	public String getWordExplain() {
		return wordExplain;
	}
}