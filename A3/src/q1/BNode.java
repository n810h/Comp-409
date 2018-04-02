package q1;

public class BNode{
	private int id;
	volatile public long eTime;
	volatile public long dTime;

	public BNode(int _id) {
		id = _id;
	}
	
	public int getId() {
		return id;
	}
}
