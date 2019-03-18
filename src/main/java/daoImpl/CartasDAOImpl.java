package daoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.transform.OutputKeys;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import com.ibm.icu.impl.number.PNAffixGenerator.Result;

import idao.ICartasDAO;
import model.Baraja;
import model.Carta;

public class CartasDAOImpl implements ICartasDAO {
	
	private final String db_name = "cartas";
	private final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/"+db_name;
	
	private final String xquery = "xquery version \"3.1\";\r\n" + 
			"for $c in doc(\"cartas/cartas.xml\")//card\r\n" + 
			"return $c";
	
	public static void main(String[] args) {
		// Pruebas
		CartasDAOImpl existdb = new CartasDAOImpl();
		List<Carta> cartas = existdb.loadCards();
		for (int i = 0; i < cartas.size(); i++) {
			System.out.println(cartas.get(i).toString());
		}
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
			XPathQueryService xpqs = (XPathQueryService)col.getService("XPathQueryService", "1.0");
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
			//Cerrar conexión		
			if (col != null) {
				try {
					col.close();
				} catch(XMLDBException xe) {
					xe.printStackTrace();
				}
			}
		}
		return cartas;
	}
	
	public boolean saveDeck(Baraja baraja) {
		// TODO: implementar saveDeck();
		return false;
	}

	public Baraja loadDeck(String deckName) {
		// TODO: implementar loadDeck()
		return null;
	}
	
}
