package daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

import idao.ICartasDAO;
import model.Baraja;
import model.Carta;

public class CartasDAOImpl implements ICartasDAO {

	private final String db_name = "cartas";
	private final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc" + db_name;

	private final String xquery = "xquery version \"3.1\";\r\n" + "for $c in doc(\"cartas/cartas.xml\")//card\r\n"
			+ "return $c";

	public static void main(String[] args) {
		// Pruebas
		CartasDAOImpl dao = new CartasDAOImpl();
		List<Carta> cartas = dao.loadCards();
		for (int i = 0; i < cartas.size(); i++) {
			System.out.println(cartas.get(i).toString());
		}
		// System.out.println("Insertado en mongodb? "+dao.saveDeck(new Baraja("Mazo de
		// prueba", 6, cartas)));
		// System.out.println(dao.loadDeck("Mazo de prueba").toString());
		// dao.loadDeck("Mazo de prueba").toString();
	}

	/**
	 * Este método carga todas las cartas que hayan en la colección de exist-db
	 * @return Devuelve una lista con todas las cartas
	 */
	public List<Carta> loadCards() {
		ArrayList<Carta> cartas = new ArrayList<Carta>();
		final String driver = "org.exist.xmldb.DatabaseImpl";
		Class cl;

		Collection col = null;
		XMLResource xml = null;

		try {
			cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(database);
			// Conectar a eXist-db
			col = DatabaseManager.getCollection(URI);
			XPathQueryService xpqs = (XPathQueryService) col.getService("XPathQueryService", "1.0");
			xpqs.setProperty("indent", "yes");
			// Ejecutar la consulta
			ResourceSet result = xpqs.query(xquery);
			// Parsear los resultados
			xml = (XMLResource) result.getMembersAsResource();
			Node node = xml.getContentAsDOM();
			Document document = null;
			if (node.getOwnerDocument() == null) {
				document = (Document) node;
			} else {
				document = node.getOwnerDocument();
			}

			// Guardamos todos los elementos con el nombre carta en un NodeList
			NodeList nodeList = document.getElementsByTagName("card");
			for (int index = 0; index < nodeList.getLength(); index++) {
				// Coger un elemento de la lista (una carta)
				Element element = (Element) nodeList.item(index);

				// Sacamos los datos del elemento y los guardamos en variables para luego guardarlo como Carta
				int id = Integer.parseInt(element.getAttribute("id"));
				String name = element.getElementsByTagName("name").item(0).getTextContent();
				int summonCost = Integer.parseInt(element.getElementsByTagName("summonCost").item(0).getTextContent());
				int attack = Integer.parseInt(element.getElementsByTagName("attack").item(0).getTextContent());
				int defense = Integer.parseInt(element.getElementsByTagName("defense").item(0).getTextContent());
				int value = Integer.parseInt(element.getElementsByTagName("value").item(0).getTextContent());

				Carta carta = new Carta(id, name, summonCost, attack, defense, value);
				cartas.add(carta);
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (XMLDBException e) {
			e.printStackTrace();
		} finally {
			// Cerrar conexión
			if (col != null) {
				try {
					col.close();
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}
		}
		return cartas;
	}
	
	/**
	 * Guarda un mazo en MongoDB
	 * @param baraja - La baraja que queremos guardar
	 */
	public boolean saveDeck(Baraja baraja) {
		try {
			MongoClient mongoClient = new MongoClient();
			MongoDatabase database = mongoClient.getDatabase("mongo");
			MongoCollection<org.bson.Document> collection = database.getCollection("decks");
			String json = new Gson().toJson(baraja);
			org.bson.Document doc = org.bson.Document.parse(json);
			collection.insertOne(doc);
			mongoClient.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Este método cargará un mazo si el nombre del mazo que queremos existe
	 * @param deckName - El mazo que queremos buscar
	 * @return Devolverá el mazo correspondiente si el nombre del mazo es correcto
	 */
	public Baraja loadDeck(String deckName) {
		MongoClient mongoClient = new MongoClient();
		MongoDatabase database = mongoClient.getDatabase("mongo");
		MongoCollection<org.bson.Document> collection = database.getCollection("decks");
		// Buscar un mazo por su nombre
		org.bson.Document doc = collection.find(Filters.eq("DeckName", deckName)).projection(Projections.excludeId()).first();
		Gson gson = new Gson();
		String json = doc.toJson();
		// System.out.println(json);
		Baraja baraja = gson.fromJson(json, Baraja.class);
		//System.out.println(baraja.toString());
		mongoClient.close();
		return baraja;
	}

}
