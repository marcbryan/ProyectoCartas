package model;

import java.util.ArrayList;

public class Baraja {
	private String deckName;
	private int deckValue;
	private ArrayList<Carta> deck;
	
	public Baraja(String deckName, int deckValue, ArrayList<Carta> deck) {
		this.deckName = deckName;
		this.deckValue = deckValue;
		this.deck = deck;
	}

	public String getDeckName() {
		return deckName;
	}

	public int getDeckValue() {
		return deckValue;
	}

	public ArrayList<Carta> getDeck() {
		return deck;
	}

	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}

	public void setDeckValue(int deckValue) {
		this.deckValue = deckValue;
	}

	public void setDeck(ArrayList<Carta> deck) {
		this.deck = deck;
	}
	
}
