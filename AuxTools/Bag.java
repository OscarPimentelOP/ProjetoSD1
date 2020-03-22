package AuxTools;

public class Bag {
	int id;
	int passegerId;
	char destination;
	
	public Bag(int id, int passegerId, char destination) {
		this.id = id;
		this.passegerId = passegerId;
	}
	
	public int getId() {
		return id;
	}
	
	public int getPassegerId() {
		return passegerId;
	}
	
	public void setDestination(char destination) {
		this.destination = destination;
	}
	
	public char getDestination() {
		return this.destination;
	}
}
