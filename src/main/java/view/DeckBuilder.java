package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import daoImpl.CartasDAOImpl;
import idao.ICartasDAO;
import model.Baraja;
import model.Carta;

public class DeckBuilder extends JFrame {
	
	private ICartasDAO dao = new CartasDAOImpl();
	
	private CustomListModel clm_cartas = new CustomListModel();
	private CustomListModel clm_mazo = new CustomListModel();
	private List<Carta> cartas;
	
	private JPanel contentPane;
	private JTextField tf_deckname;
	private JLabel lblValue;
	private JList<Carta> lista_cartas;
	private JList<Carta> lista_mazo;
	
	private JButton btnLoadCards;
	private JButton btnLoadDeck;
	private JButton btnRndDeck;
	private JButton btnSaveDeck;
	private JButton toDeck;
	private JButton toList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DeckBuilder frame = new DeckBuilder();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DeckBuilder() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		setTitle("Deck Builder");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lista_cartas = new JList<Carta>();
		lista_cartas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista_cartas.addMouseMotionListener(new MouseMotionListener() {

	        public void mouseDragged(MouseEvent e) {}

	        public void mouseMoved(MouseEvent e) {
	            int index = lista_cartas.locationToIndex(e.getPoint());
	            if (index > -1) {
	            	lista_cartas.setToolTipText(clm_cartas.getCarta(index).toString());
	            }
	        }
	    });
		lista_cartas.setModel(clm_cartas);
		lista_cartas.setBounds(83, 71, 230, 320);
		contentPane.add(lista_cartas);
		
		lista_mazo = new JList<Carta>();
		lista_mazo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lista_mazo.addMouseMotionListener(new MouseMotionListener() {

	        public void mouseDragged(MouseEvent e) {}

	        public void mouseMoved(MouseEvent e) {
	            int index = lista_mazo.locationToIndex(e.getPoint());
	            if (index > -1) {
	            	lista_mazo.setToolTipText(clm_mazo.getCarta(index).toString());
	            }
	        }
	    });
		lista_mazo.setModel(clm_mazo);
		lista_mazo.setBounds(464, 71, 230, 320);
		contentPane.add(lista_mazo);
		
		toDeck = new JButton("\u2192"); // ->
		toDeck.setEnabled(false);
		toDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lista_cartas.getSelectedIndex() != -1) {
					Carta c = clm_cartas.getCarta(lista_cartas.getSelectedIndex());
					if (calcDeckValue()+c.getValue() <= 20) {
						clm_cartas.removeCarta(lista_cartas.getSelectedIndex());
						clm_mazo.addCarta(c);
						lblValue.setText("<html>Deck value: <b>"+calcDeckValue()+"<b></html>");
					} else {
						JOptionPane.showMessageDialog(DeckBuilder.this, "No puedes añadir esta carta, ya que el valor del mazo seria mayor de 20!", "Alerta", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					System.out.println("no selected");
				}
			}
		});
		toDeck.setFont(new Font("Arial", Font.PLAIN, 24));
		toDeck.setBounds(346, 176, 89, 35);
		contentPane.add(toDeck);
		
		toList = new JButton("\u2190"); // <-
		toList.setEnabled(false);
		toList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (lista_mazo.getSelectedIndex() != -1) {
					Carta c = clm_mazo.getCarta(lista_mazo.getSelectedIndex());
					clm_mazo.removeCarta(lista_mazo.getSelectedIndex());
					clm_cartas.addCarta(c);
					lblValue.setText("<html>Deck value: <b>"+calcDeckValue()+"<b></html>");
				} else {
					System.out.println("no selected");
				}
			}
		});
		toList.setFont(new Font("Arial", Font.PLAIN, 24));
		toList.setBounds(346, 234, 89, 35);
		contentPane.add(toList);
		
		btnLoadCards = new JButton("Load cards");
		btnLoadCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cartas = dao.loadCards();
				for (Carta c : cartas) {
					clm_cartas.addCarta(c);
				}
				enableButtons();
			}
		});
		btnLoadCards.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnLoadCards.setBounds(10, 11, 105, 23);
		contentPane.add(btnLoadCards);
		
		btnSaveDeck = new JButton("Save deck");
		btnSaveDeck.setEnabled(false);
		btnSaveDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String deckname = tf_deckname.getText();
				if (!deckname.equals("")) {
					if (dao.loadDeck(deckname) == null) {
						if (dao.saveDeck(new Baraja(deckname, calcDeckValue(), clm_mazo.getCartas()))) {
							clm_cartas.clear();
							clm_mazo.clear();
							for (Carta c : cartas) {
								clm_cartas.addCarta(c);
							}
							tf_deckname.setText("");
							lblValue.setText("<html>Deck value: <b>"+0+"<b></html>");
							JOptionPane.showMessageDialog(DeckBuilder.this, "Mazo "+deckname+" guardado correctamente", "Información", JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(DeckBuilder.this, "El mazo "+deckname+" ya existe", "Alerta", JOptionPane.WARNING_MESSAGE);
						//TODO: si hay que guardar cambios en mazo ya creado, faltará el código
					}
				} else {
					JOptionPane.showMessageDialog(DeckBuilder.this, "No puedes guardar un mazo sin nombre", "Alerta", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnSaveDeck.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnSaveDeck.setBounds(669, 12, 105, 23);
		contentPane.add(btnSaveDeck);
		
		btnRndDeck = new JButton("Rnd deck");
		btnRndDeck.setEnabled(false);
		btnRndDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Limpiamos los dos paneles para crear el mazo aleatorio
				clm_cartas.clear();
				clm_mazo.clear();
				int value = 0;
				for (Carta c : cartas) {
					int rnd = (int) Math.round(Math.random());
					if (rnd == 1) {
						if ((value+c.getValue()) <= 20) {
							value += c.getValue();
							clm_mazo.addCarta(c);
						} else {
							clm_cartas.addCarta(c);
						}
					} else {
						//Las cartas que no se añadan al mazo se verán en la lista de cartas
						clm_cartas.addCarta(c);
					}
				}
				lblValue.setText("<html>Deck value: <b>"+value+"<b></html>");
			}
		});
		btnRndDeck.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnRndDeck.setBounds(334, 127, 105, 23);
		contentPane.add(btnRndDeck);
		
		tf_deckname = new JTextField("");
		tf_deckname.setFont(new Font("Tahoma", Font.PLAIN, 13));
		tf_deckname.setBounds(244, 415, 185, 23);
		contentPane.add(tf_deckname);
		tf_deckname.setColumns(10);
		
		btnLoadDeck = new JButton("Load deck");
		btnLoadDeck.setEnabled(false);
		btnLoadDeck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String deckname = tf_deckname.getText();
				if (!deckname.equals("")) {
					Baraja deck = dao.loadDeck(deckname);
					if (deck != null) {
						//Limpiamos el contenido de las dos listas
						clm_mazo.clear();
						clm_cartas.clear();
						for (Carta c : deck.getDeck()) {
							clm_mazo.addCarta(c);
						}
						lblValue.setText("<html>Deck value: <b>"+calcDeckValue()+"<b></html>");
					} else {
						JOptionPane.showMessageDialog(DeckBuilder.this, "El mazo "+deckname+" no existe", "Alerta", JOptionPane.WARNING_MESSAGE);
						tf_deckname.setText("");
					}
				} else {
					JOptionPane.showMessageDialog(DeckBuilder.this, "Escribe un nombre para buscar un mazo", "Alerta", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnLoadDeck.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnLoadDeck.setBounds(440, 415, 105, 23);
		contentPane.add(btnLoadDeck);
		
		lblValue = new JLabel("<html>Deck value: <b>0<b></html>");
		lblValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblValue.setBounds(464, 45, 113, 23);
		contentPane.add(lblValue);
	}
	
	/**
	 * Calcula el valor del mazo
	 * @return Devuelve el valor del mazo
	 */
	private int calcDeckValue() {
		int value = 0;
		for (int i = 0; i < clm_mazo.getSize(); i++) {
			Carta c = clm_mazo.getCarta(i);
			value += c.getValue();
		}
		return value;
	}
	
	/**
	 * Activa todos los botones y desactiva el botón 'Load cards' una vez esten cargadas
	 */
	private void enableButtons() {
		btnLoadDeck.setEnabled(true);
		toDeck.setEnabled(true);
		toList.setEnabled(true);
		btnRndDeck.setEnabled(true);
		btnSaveDeck.setEnabled(true);
		btnLoadCards.setEnabled(false);
		btnLoadCards.setToolTipText("Ya has cargado las cartas!");
	}

}
