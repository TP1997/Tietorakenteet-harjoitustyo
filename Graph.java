import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/*
 * Graafia kuvaava luokka
 */
public class Graph<T>{
	//Graafin solmut talletetaan avain-alkio pareina
	private HashMap<Integer, Node<T>> nodes;
	
	private static final boolean NODE_FOUND=true;
	private static final boolean NODE_NOT_FOUND=false;
	
	public Graph(int size) {
		nodes=new HashMap<>(size);
	}
	public int nodeCount() {
		return nodes.size();
	}
	public Node<T> getNode(int key) {
		return nodes.get(key);
	}
	public boolean contains(int key) {
		return nodes.containsKey(key);
	}
	public Collection<Node<T>> getAllNodes(){
		return nodes.values();
	}
	public void addNode(Node<T> node, int key) {
		if(!nodes.containsKey(key)) {
			nodes.put(key, node);
		}
	}
	/*
	 * Metodi tarkistaa onko kahden solmun välillä kaarta
	 */
	public boolean hasEdge(int from, int to) {
		Node<T> sNode=getNode(from);
		Node<T> eNode=getNode(to);
		if(sNode!=null && eNode!=null) {
			return sNode.hasNeighbor(eNode);
		}
		return false;
	}
	/*
	 * Metodi lisää kaaren kahden solmun välille
	 */
	public void addEdge(int startKey, int endKey, double weight) {
		Node<T> startNode=nodes.get(startKey);
		Node<T> endNode=nodes.get(endKey);
		if(startNode!=null && endNode!=null) {
			startNode.addNeighbor(endNode, weight);
		}
	}
	private void setNodeMarkers(boolean value) {
		for(Map.Entry<Integer, Node<T>> entry : nodes.entrySet()) {
			Node<T> currentNode=entry.getValue();
			currentNode.setStatus(value);
		}
	}
	/*
	 * Metodi poistaa solmun graafista
	 */
	public Node<T> deleteNode(int key){
		Node<T> removable=getNode(key);
		//Jos poistettava solmu löytyi
		if(removable!=null) {
			//Poistetaan kaikki kyseiseen solmuun liittyvät viitteet
			Iterator<Node<T>> itr=removable.outDegree.iterator();
			while(itr.hasNext()) {
				Node<T> currentNode=itr.next();
				currentNode.removeInDegreeNode(removable);
			}
			itr=removable.inDegree.iterator();
			while(itr.hasNext()) {
				Node<T> currentNode=itr.next();
				currentNode.removeOutDegreeNode(removable);
			}
			//Poistetaan solmu graafista
			nodes.remove(key);
			return removable;
		}
		return null;
	}
	
	/*
	 * Leveyshaku alkaen annettua avainta vastaavasta solmusta.
	 * Jos write=true, niin haku kirjoitetaan tiedostoon
	 */
	public void BFS(int startNodeKey, boolean write) {
		Node<T> startNode=getNode(startNodeKey);
		if(startNode!=null) {
			setNodeMarkers(NODE_NOT_FOUND);
			Queue <Node<T>>queue=new LinkedList<Node<T>>();
			startNode.setStatus(NODE_FOUND);
			queue.add(startNode);
			
			while(!queue.isEmpty()) {
				Node<T> currentNode=queue.poll();
				if(write)
					FileWriteHandler.writeToFile(currentNode.toString());
				Iterator<Node<T>> itr=currentNode.outDegree.iterator();
				while(itr.hasNext()) {
					Node<T> neighbor=itr.next();
					if(neighbor.status()==NODE_NOT_FOUND) {
						queue.add(neighbor);
						neighbor.setStatus(NODE_FOUND);
					}
				}
			}
		}
	}
	/*
	 * Syvyyshaku alkaen annettua avainta vastaavasta solmusta.
	 * Jos write=true, niin haku kirjoitetaan tiedostoon.
	 */
	public void DFS(int startNodeKey, boolean write) {
		Node<T> startNode=getNode(startNodeKey);
		if(startNode!=null) {
			setNodeMarkers(NODE_NOT_FOUND);
			//Suoritetaan syvyyshaku rekursiivisesti
			DFS(startNode, write);
		}
	}
	/*
	 * Syvyyshaku, rekursiivinen osa
	 */
	public void DFS(Node<T> startNode, boolean write) {
		if(write)
			FileWriteHandler.writeToFile(startNode.toString());
		startNode.setStatus(NODE_FOUND);
		Iterator<Node<T>> itr=startNode.outDegree.iterator();
		while(itr.hasNext()) {
			Node<T> neighbor=itr.next();
			if(neighbor.status()==NODE_NOT_FOUND) {
				DFS(neighbor, write);
			}
		}
	}
	/*
	 * "Käänteinen" syvyyshaku. Haussa edetään sisääntulevien viitteiden mukaan
	 */
	public void DFSTranspose(int startNodeKey) {
		Node<T> startNode=getNode(startNodeKey);
		if(startNode!=null) {
			setNodeMarkers(NODE_NOT_FOUND);
			DFSrecTranspose(startNode);
		}
	}
	/*
	 * Käänteinen syvyyshaku, rekursiivinen osa.
	 */
	public void DFSrecTranspose(Node<T> startNode) {
		startNode.setStatus(NODE_FOUND);
		Iterator<Node<T>> itr=startNode.inDegree.iterator();
		while(itr.hasNext()) {
			Node<T> node=itr.next();
			if(node.status()==NODE_NOT_FOUND) {
				DFSrecTranspose(node);
			}
		}
	}
	/*
	 * Metodi tarkistaa onko graafi vahvasti yhdistetty ja se käyttää hyväkseen
	 * syvyyshakua ja kääneistä syvyyshakua.
	 */
	public boolean isStronglyConnected() {
		
		if(!nodes.isEmpty()) {
			//Aloitetaan tarkastelu sattumanvaraisesta solmusta.
			Set<Integer> keys=nodes.keySet();
			Iterator<Integer> itr=keys.iterator();
			int startNodeKey=itr.next();
			//Suoritetaan syvyyshaku
			DFS(startNodeKey, false);
			//Tarkistetaan onko kaikki solmut käyty läpi
			for(Map.Entry<Integer, Node<T>> entry : nodes.entrySet()) {
				Node<T> currentNode=entry.getValue();
				if(currentNode.status()==NODE_NOT_FOUND) {
					//Jos löytyi käymätön solmu, niin graafi ei ole vahvasti yhdistetty
					return false;
				}
			}
			//Suoritetaan käänteinen syvyyshaku
			DFSTranspose(startNodeKey);
			for(Map.Entry<Integer, Node<T>> entry : nodes.entrySet()) {
				Node<T> currentNode=entry.getValue();
				if(currentNode.status()==NODE_NOT_FOUND) {
					//Jos löytyi käymätön solmu, niin graafi ei ole vahvasti yhdistetty
					return false;
				}
			}
			//Jos tarkistus meni läpi, niin graafi on vahvasti yhdistetty.
			return true;
		}
		return false;
		
	}
}