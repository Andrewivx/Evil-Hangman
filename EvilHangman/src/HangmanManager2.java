import java.util.*;

public class HangmanManager2 extends HangmanManager {
	
	private boolean wordInstanceChanged;
	private boolean guessInstanceChanged;
	public HangmanManager2(Collection<String> dictionary, int length, int max) {
		super(dictionary, length, max);
		// first time always return new unmodifiable object
		wordInstanceChanged = true; //boolean to track the instance change of word
		guessInstanceChanged = true; //boolean to track the instance change of guesses
	}

	// overload the record method
	// Behavior Change: now has a extra function when there is only one guess left
	// rather than allowing the user to guess and get something correct and keep playing
	// it will just end the game by telling 
	//the user the first occurance of a word that hasn't
	// had a letter guessed
	public int record(char guess) {
		
		if (super.guessesLeft() == 1) {
			Set<String> currentWords = super.words();
			for (String currentWord : currentWords) {
				if (currentWord.indexOf(guess) == -1) {
					currentWords.clear();
					currentWords.add(currentWord);
					wordInstanceChanged = true;
					return super.record(guess);
				}
			}
		}
		Set<String> originalSet = super.words();
		Set<Character> originalGuesses = super.guesses();
		int number = super.record(guess);
		Set<String> newSet = super.words();
		Set<Character> newGuess = super.guesses();
 		instanceChangeCheck(originalSet, originalGuesses, newSet, newGuess);
		return number;
	}

	//helper method to be used in record to check if the instance has been changed 
	//or not in conjuction with 
	//the words/guesses method
	//Parameters: Set<String> originalSet - the original set
	//Set<String> newSet - the new set being compared to the old one
	//Set<Character> originalGuesses - the original Set of guesses
	//Set<Character> newGuess - the new set being compared to the old one 
	private void instanceChangeCheck(Set<String> originalSet, Set<Character> originalGuesses,
			Set<String> newSet, Set<Character> newGuess) {
		if(originalSet != newSet) {
			wordInstanceChanged = true;
		}
		else {
			wordInstanceChanged = false;
		}
		if(originalGuesses != newGuess) {
			guessInstanceChanged = true;
		}
		else {
			guessInstanceChanged = false;
		}
	}
	
	//overloads the words method
	//Behavior Change - now checks if a new instance has been created or not and
		//if so will create a unmodifiableSet so it's protected from user change otherwise
		//will just run normally and not create a new instance
	public Set<String> words() {
		if(!wordInstanceChanged) {
			return super.words(); //no need to create new instance
		}
		Set<String> words2 = Collections.unmodifiableSet(super.words());
		wordInstanceChanged = false;
		return words2;
	}
	
	//overloads the guesses method
	//Behavior Change - now checks if a new instance has been created or not and
		//if so will create a unmodifiableSet so it's protected from user change otherwise
		//will just run normally and not create a new instance
	public Set<Character> guesses() {
		if(!guessInstanceChanged) {
			return super.guesses(); //no need to create new instance
		}
		Set<Character> guesses2 = Collections.unmodifiableSet(super.guesses());
		guessInstanceChanged = false;
		return guesses2;
	}
}
