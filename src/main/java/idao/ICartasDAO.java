package idao;

import java.util.List;

import model.Baraja;
import model.Carta;

public interface ICartasDAO {
	public List<Carta> loadCards();
	public boolean saveDeck(Baraja baraja);
	public Baraja loadDeck(String deckName);
	public boolean updateDeck(Baraja baraja);
}
