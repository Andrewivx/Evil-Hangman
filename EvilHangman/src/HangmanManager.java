//Andrew  Yu
//CSE 143 BS TA:Raymond Webster Berry
//Homework 3 
//AssassinManager manages of game of evil hangman
//which is a game of hangman which attempts to make the 
//user have the hardest time winning a game of hangman
import java.util.*;

public class HangmanManager {
	//field to keep track of the dashes that are displayed
	private String dashes;
	//total guesses to keep track of total guesses left
	private int totalGuesses;
	//a set of possible words to be guessed
	private Set<String> words;
	//a set of all guessed words
	private Set<Character> guessed;
	
	//Pre: if length is smaller than 1 or max is smaller than zero, throw
	// and illegalarugmentexception
	//Post: Constructs a HangmanManger object and creates a series of possible
	//words that a user can guess based on the length the user gets and
	//eliminates duplicates, also 
	//will record the total amount of incorrect guesses the user has left
	//initializes a dash pattern to the length of the word
	//Parameters: Collection<String> dictionary - all potential words
	// int length - length of the words you want selected to put into the possible words pool
	//int max - amount of incorect guesses the user gets 
	public HangmanManager(Collection<String> dictionary, int length, int max) {
		if (length < 1 || max < 0) {
			throw new IllegalArgumentException();
		}
		// store all Collection<String> values into the the Set
		words = new TreeSet<String>();
		for (String word : dictionary) {
			if (word.length() == length && !words.contains(word)) {
				words.add(word);
			}
		}
		// to initialize amount of guesses for other method
		totalGuesses = max;
		guessed = new TreeSet<Character>();
		dashes = "- ";
		for (int i = 1; i < length; i++) {
			dashes += "- ";
		}
	}
	
	//Post: client can call this method 
	// to get the possible words that can be 
	// guessed
	public Set<String> words() {
		return words;
	}
	
	//Post: accesses the set of words that 
	// have already been guessed
	public Set<Character> guesses(){
		return guessed;
	}
	
	//Post: access the total amount of incorrect
	// guesses the client has left
	public int guessesLeft() {
		return totalGuesses;
	}
	
	//Pre: if the amount of potential words is empty
	//throw an IllegalStateException
	//post: gives the pattern in the form of a series of dashes
	// that represent letters that are for sure in the word 
	// and letters that haven't been guessed (dashes = haven't been guessed)
	public String pattern() {
		if (words.isEmpty()) {
			throw new IllegalStateException();
		}
		return dashes;
	}
	
	//Pre: if totalguesses are less than one or the possible words set
	// is empty throw a illegalstateexception
	//if the words isn't empty but the char has already been guessed 
	//throw an illegalargumentexception
	//Post: this method will record the next guess, and then find the set
	//of the largest amount of words for the next steps, updates the new dash pattern
	//updates totalguessesleft as well depending on if the user was right or wrong
	//(subtracts one if the user is wrong) 
	public int record(char guess) {
		if (totalGuesses < 1 || words.isEmpty()) {
			throw new IllegalStateException();
		} else if (!words.isEmpty() && guessed.contains(guess)) {
			throw new IllegalArgumentException();
		}
		Map<String, Set<String>> outcomes = new TreeMap<String, Set<String>>();
		for (String word : words) {
			String thisPattern = translation(word, guess);
			if (!outcomes.containsKey(thisPattern)) {
				outcomes.put(thisPattern, new TreeSet<String>());
			}
			outcomes.get(thisPattern).add(word);
		}
		String newDashes = findMaxKey(outcomes);
		//meaning you guessed wrong
		if(allDashes(newDashes) || newDashes.equals(dashes)) {
			totalGuesses--;
		}
		dashes = newDashes;
		words = outcomes.get(dashes);
		guessed.add(guess);
		return countOccurance(guess);
	}
	
	//Post: A private method that checks to see if the string passed in 
	//is all dashes or not, if it's all dashes there are no for sure letters
	// Parameters: String newDashes - the series of dashes/letters to be checked
	private boolean allDashes(String newDashes) {
		for(int i = 0; i < newDashes.length(); i++) {
			if(newDashes.charAt(i) != '-' && newDashes.charAt(i) != ' ') {
				return false;
			}
		}
		return true;
	}
	
	//Post: finds which key has the most words in the map
	//, and picks that one so that the user will have the most
	//potential words to guess from
	//Parameters: Map<String, Set<String>> outcomes the map 
	//where you are checking to see
	//which key maps to the set that has the most words in it
	private String findMaxKey(Map<String, Set<String>> outcomes) {
		int max = 0;
		String maxKey = "";
		for (String Key : outcomes.keySet()) {
			if (outcomes.get(Key).size() > max) {
				max = outcomes.get(Key).size();
				maxKey = Key;
			}
		}
		return maxKey;
	}
	
	//Post: Sees how many times the occurance of the letter
	// appears in the current dashes
	// Parameters: char guess - the letter being checked
	private int countOccurance(char guess) {
		int occurances = 0;
		for (int i = 0; i < dashes.length(); i++) {
			if (dashes.charAt(i) == guess) {
				occurances++;
			}
		}
		return occurances;
	}
	
	//Post: translates a word into dashes and characters based on which letter
	//is guessed, unguessed letters are represented by dashes
	//Parameters: String word -the word being translated,
	//to see if/where it contains a certain char
	//char guess - the character being checked
	private String translation(String word, char guess) {
		String finalDashes = "";
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess || guessed.contains(word.charAt(i))) {
				finalDashes += word.charAt(i) + " ";
			} else {
				finalDashes += "- ";
			}
		}
		return finalDashes.trim();
	}

}