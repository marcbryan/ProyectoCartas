package model;

public class Carta {
	private int id;
	private String name;
	private int summonCost;
	private int attack;
	private int defense;
	private int value;
	
	public Carta(int id, String name, int summonCost, int attack, int defense, int value) {
		this.id = id;
		this.name = name;
		this.summonCost = summonCost;
		this.attack = attack;
		this.defense = defense;
		this.value = value;
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getSummonCost() {
		return summonCost;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getDefense() {
		return defense;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSummonCost(int summonCost) {
		this.summonCost = summonCost;
	}
	
	public void setAttack(int attack) {
		this.attack = attack;
	}
	
	public void setDefense(int defense) {
		this.defense = defense;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Carta [id=" + id + ", name=" + name + ", summonCost=" + summonCost + ", attack=" + attack + ", defense="
				+ defense + ", value=" + value + "]";
	}
	
	
}
