import java.util.LinkedList;
/*
 * Graafin yksitt‰ist‰ solmua kuvaava luokka
 */
public class Node<T>{
	public int id;
	private T element;
	public LinkedList<Node<T>> outDegree;
	private LinkedList<Double> outDegreeWeights;
	public LinkedList<Node<T>> inDegree;
	private LinkedList<Double> inDegreeWeights;
	
	private boolean visited;
	
	public Node(T elem, int id_) {
		element=elem;
		id=id_;
		outDegree=new LinkedList<Node<T>>();
		outDegreeWeights=new LinkedList<Double>();
		inDegree=new LinkedList<Node<T>>();
		inDegreeWeights=new LinkedList<Double>();		
		visited=false;
	}
	public int outDegree() {
		return outDegree.size();
	}
	public int inDegree() {
		return inDegree.size();
	}
	public T getElement() {
		return element;
	}
	/*
	 * Metodi lis‰‰ solmulle uuden naapurin
	 */
	public void addNeighbor(Node<T> neighbor, double weight) {
		//Lis‰t‰‰n kutsuvan solmun naapuriksi parametriarvot
		outDegree.add(neighbor);
		outDegreeWeights.add(weight);
		//Lis‰t‰‰n parametrisolmun sis‰asteeseen kutsuva solmu
		neighbor.addIDNode(this, weight);
	}
	//Metodi lis‰‰ solmulle uuden sis‰astesolmun
	public void addIDNode(Node<T> IdNode, double weight) {
		inDegree.add(IdNode);
		inDegreeWeights.add(weight);
	}
	/*
	 * Metodi tarkistaa kuuluuko parametrin mukainen solmu kutsuvan solmun naapureihin
	 */
	public boolean hasNeighbor(Node<T> n) {
		return outDegree.contains(n);
	}
	public void setStatus(boolean value) {
		visited=value;
	}
	public boolean status() {
		return visited;
	}
	/*
	 * Metodi poistaa solmulta naapurin
	 */
	public Node<T> removeOutDegreeNode(Node<T> n) {
		int weightIdx=outDegree.indexOf(n);
		if(outDegree.remove(n)) {
			outDegreeWeights.remove(weightIdx);
			return n;
		}
		return null;
	}
	/*
	 * Metodi poistaa solmulta sis‰asteen solmun
	 */
	public Node<T> removeInDegreeNode(Node<T> n) {
		int weightIdx=inDegree.indexOf(n);
		if(inDegree.remove(n)) {
			inDegreeWeights.remove(weightIdx);
			return n;
		}
		return null;
	}
	public String toString() {
		return element.toString();
	}
	
}