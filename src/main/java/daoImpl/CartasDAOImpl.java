package daoImpl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;

import org.exist.xmldb.EXistResource;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import idao.ICartasDAO;
import model.Baraja;
import model.Carta;

public class CartasDAOImpl implements ICartasDAO {
	
	private final String db_name = "";
	private final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/"+db_name;
	private final String resource = "";
	
	public List<Carta> loadCards() {
		//TODO: loadCards() en desarrollo
		ArrayList<Carta> cartas = new ArrayList<Carta>();
		final String driver = "org.exist.xmldb.DatabaseImpl";
		Class cl;
		
		Collection col = null;
		XMLResource res = null;
		
		try {
			cl = Class.forName(driver);
			Database database = (Database) cl.newInstance();
			database.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(database);

			col = DatabaseManager.getCollection(URI);
			col.setProperty(OutputKeys.INDENT, "no");
			res = (XMLResource) col.getResource(resource);
			
			//Prueba
			if (res == null) {
				System.out.println("doc not found!");
			} else {
				System.out.println(res.getContent());
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
			if (res != null) {
				try {
					((EXistResource)res).freeResources();
				} catch (XMLDBException xe) {
					xe.printStackTrace();
				}
			}
			
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
