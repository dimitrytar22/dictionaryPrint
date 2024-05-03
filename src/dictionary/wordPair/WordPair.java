package dictionary.wordPair;

import java.io.Serializable;
import java.util.UUID;

public class WordPair implements Serializable {
	private static final long serialVersionUID = -4830278856255145522L;
	private String id;
	private String word;
	private String translation;
	
	
	public WordPair(String word, String translation) {
		this.id = UUID.randomUUID().toString();
		this.word = word;
		this.translation = translation;
	}
	public WordPair() {
		
	}
	
	
	public void changePair(String word, String translation) {
		this.word = word;
		this.translation = translation;
	}
	public String getUUID() {
		return this.id;
		}
	public String getTranslation() {
		return translation;
	}
	public String getWord() {
		return word;
	}
	
	@Override
	public String toString() {
		return getWord() + " - " + getTranslation();
	}
	
}
