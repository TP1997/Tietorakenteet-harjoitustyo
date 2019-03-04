
/*
 * Apuluokka koordinaattien esitykseen
 */
public class Coordinate{
	public float x;
	public float y;
	
	public Coordinate(float x, float y) {
		this.x=x;
		this.y=y;
	}
	public String toString() {
		return x+","+y;
	}
}