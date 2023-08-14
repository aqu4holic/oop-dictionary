package oop_dict.server;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Trie {
	public static class TrieNode {
		Map<Character, TrieNode> children = new TreeMap<>();

		boolean isEndOfWord;

		TrieNode() {
			isEndOfWord = false;
		}
	}

	private static TrieNode root = new TrieNode();
	private static ArrayList<String> searchedWords = new ArrayList<>();

	public static ArrayList<String> getSearchedWords() {
		return searchedWords;
	}

	public static void insert(String target) {
		TrieNode p = root;

		int n = target.length();
		for (int i = 0; i < n; ++i) {
			char curChar = target.charAt(i);

			if (p.children.get(curChar) == null) {
				p.children.put(curChar, new TrieNode());
			}

			p = p.children.get(curChar);
		}

		p.isEndOfWord = true;
	}

	private static void dfsGetWordsSubtree(TrieNode p, String current) {
		if (p.isEndOfWord) {
			searchedWords.add(current);
		}

		for (char i : p.children.keySet()) {
			if (p.children.get(i) != null) {
				dfsGetWordsSubtree(p.children.get(i), current + i);
			}
		}
	}

	public static ArrayList<String> search(String prefix) {
		if (prefix.isEmpty()) {
			return new ArrayList<>();
		}

		searchedWords.clear();
		TrieNode p = root;

		int n = prefix.length();
		for (int i = 0; i < n; ++i) {
			char curChar = prefix.charAt(i);

			if (p.children.get(curChar) == null) {
				p.children.put(curChar, new TrieNode());
			}

			p = p.children.get(curChar);
		}

		dfsGetWordsSubtree(p, prefix);

		return getSearchedWords();
	}

	public static void delete(String target) {
		TrieNode p = root;

		int n = target.length();
		for (int i = 0; i < n; ++i) {
			char curChar = target.charAt(i);

			if (p.children.get(curChar) == null) {
				System.out.println("No word matched");
				return;
			}

			p = p.children.get(curChar);
		}

		if (!p.isEndOfWord) {
			System.out.println("No word matched");
			return;
		}

		p.isEndOfWord = false;
	}
}