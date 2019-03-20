package model;

import java.util.ArrayList;
import java.util.List;

public class Baraja {
	private String DeckName;
	private int DeckValue;
	private List<Carta> Deck = new ArrayList<Carta>();
	
	public Baraja() {}
	
	public Baraja(String deckName, int deckValue, List<Carta> deck) {
		this.DeckName = deckName;
		this.DeckValue = deckValue;
		this.Deck = deck;
	}

	public String getDeckName() {
		return DeckName;
	}

	public int getDeckValue() {
		return DeckValue;
	}

	public List<Carta> getDeck() {
		return Deck;
	}

	public void setDeckName(String deckName) {
		this.DeckName = deckName;
	}

	public void setDeckValue(int deckValue) {
		this.DeckValue = deckValue;
	}

	public void setDeck(List<Carta> deck) {
		this.Deck = deck;
	}

	@Override
	public String toString() {
		return "Baraja [deckName=" + DeckName + ", deckValue=" + DeckValue + ", deck=" + Deck + "]";
	}
	
}
