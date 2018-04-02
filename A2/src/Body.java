
public class Body {
	public boolean hasTail;
	private Leg hindleg;
	private Leg foreleg;

	public Body() {
		hasTail = false;
	}
	public boolean hasLegs() {
		return (foreleg != null && hindleg != null);
	}
	
	public void addForeleg(Leg leg) {
		foreleg = leg;
	}
	
	public void addHindleg(Leg leg) {
		hindleg = leg;
	}
}
