package AuxTools;

public class Bag {
	int id;
	int passegerId;
	char destination[];
	
	public Bag(int id, int passegerId, char destination[]) {
		this.id = id;
		this.passegerId = passegerId;
		this.destination = destination;
	}
	
	public int getId() {
		return id;
	}
	
	public int getPassegerId() {
		return passegerId;
	}
	
	public char getDestination(int flight) {
		return this.destination[flight];
	}
}
