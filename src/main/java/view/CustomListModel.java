package view;

import java.util.ArrayList;

import javax.swing.AbstractListModel;

import model.Carta;

public class CustomListModel extends AbstractListModel {

	private ArrayList<Carta> cartas = new ArrayList<Carta>();
	
	public Object getElementAt(int index) {
		Carta c = cartas.get(index);
		// flecha -> = \u2192
		return "<html>"+c.getName()+" \u2192 <b style=\"color:blue\">value:"+c.getValue()+"</b></html>";
	}

	public int getSize() {
		return cartas.size();
	}
	
	public void addCarta(Carta c) {
		cartas.add(c);
        this.fireIntervalAdded(this, getSize(), getSize()+1);
	}
	
	public void removeCarta(int index) {
		cartas.remove(index);
        this.fireIntervalRemoved(index, getSize(), getSize()+1);
	}
	
	public Carta getCarta(int index) {
		return cartas.get(index);
	}
	
	public void clear() {
		cartas.clear();
		this.fireIntervalRemoved(this, getSize(), getSize()+1);
	}
	
	public ArrayList<Carta> getCartas(){
		return cartas;
	}

}
