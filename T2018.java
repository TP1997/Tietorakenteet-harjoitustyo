import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
/*
 * Tietorakenteet 2018-harjoitustyˆ.
 * Suoritetut toiminnallisuudet: 1-7 ja 9-10
 * 
 * Toiminnallisuus 9. pohdintaosuus:
 * Kahden solmun aligraafissa molemmat solmut ovat toistensa l‰himpi‰ naapureita.
 * Vastaavasti kolmen solmun aligraafissa kaksi sen solmua ovat toistensa l‰himpi‰ naapureita
 * ja kolmannella l‰hin naapuri on jompi kumpi n‰ist‰ kahdesta. Voi myˆs olla, ett‰ n‰m‰
 * kolme solmua muodostavat suunnatun syklin, jossa jokaisen aligraafin solmun sis‰aste ja
 * ulkoaste on yksi.
 */
public class T2018{
	
	private static	String line;
	private static float x[];
	private static float y[];
	
	private static void readInput() {
		x=new float[400];
		y=new float[400];
		try {
			BufferedReader br = new BufferedReader( new FileReader("Tdata.txt"));
	            
			for(int i=0; i<400; i++){
				line=br.readLine();
				String[] values=line.split(",");	
				x[i]=Float.parseFloat(values[0]);	
				y[i]=Float.parseFloat(values[1]);				  
			}
		 
		} 
		catch(IOException e){
			System.out.println("File not found.");
		}
	}
	/***************** Teht‰v‰n 10 toiminnallisuus **************************************/
	/*
	 * Metodi hakee pisteelle mainPoint K-1 kpl l‰himpi‰ pisteit‰
	 */
	private static Coordinate[] getClosestPoints(Coordinate[] points, Coordinate mainPoint, int K) {
		Coordinate[] closestPoints=new Coordinate[K-1];
		boolean swapped;
		int m=0;
		do {
			swapped=false;
			for(int n=0; n<points.length-m-1; n++) {
				if(distance(mainPoint,points[n]) < distance(mainPoint, points[n+1])) {
					Coordinate temp=points[n];
					points[n]=points[n+1];
					points[n+1]=temp;
					swapped=true;
				}
			}
		}
		while(swapped && m<K);
		System.arraycopy(points, points.length-K, closestPoints, 0, K-1);
		return closestPoints;
	}
	/*
	 * Metodi laskee et‰isyyden kahden eri pisteen v‰lill‰
	 */
	private static double distance(Coordinate c1, Coordinate c2) {
		double distance=Math.sqrt(Math.pow((c1.x-c2.x), 2) + Math.pow((c1.y-c2.y), 2));
		return distance;
	}
	/*
	 * Metodi laskee pisteen p l‰himpien pisteiden et‰isyyksien keskiarvon
	 */
	private static double avarageDist(Coordinate p, Coordinate[] q, int K) {
		double sum=0;
		for(int j=0; j<K-1; j++) {
			sum+=distance(p, q[j]);
		}
		return sum/(K-1);
	}
	/*
	 * Metodi laskee keskim‰‰r‰isten et‰isyyksien keskiarvon
	 */
	private static double overallMean(double[] avarage) {
		double sum=0;
		for(int j=0; j<avarage.length; j++) {
			sum+=avarage[j];
		}
		return sum/avarage.length;
	}
	/*
	 * Metodi lasketaan keskim‰‰r‰isten et‰isyyksien hajonnan
	 */
	private static double overallStDev(double[] avarage, double E) {
		double sum=0;
		for(int j=0; j<avarage.length; j++) {
			sum+=Math.pow(avarage[j]-E, 2);
		}
		return Math.sqrt(sum/avarage.length);
	}
	/*
	 * Metodi palauttaa "outlier" pisteet annetusta taulukosta
	 */
	private static ArrayList<Coordinate> getOutliers(Coordinate[] points) {
		//Tutkitaan K-1 kpl l‰himpi‰ naapureita
		int K=11;
		//Keskim‰‰r‰iset et‰isyydet naapureihin
		double[] avarage=new double[points.length];
		Coordinate[] pointsCopy=new Coordinate[points.length];
		System.arraycopy(points, 0, pointsCopy, 0, points.length);
		
		//Lasketaan jokaiselle pisteelle kesim‰‰r‰inen et‰isyys K-1 kpl l‰himmist‰ pisteist‰
		for(int n=0; n<points.length; n++) {
			Coordinate currentPoint=points[n];
			//Haetaan K-1 kpl l‰himpi‰ pisteit‰
			Coordinate[] closestPoints=getClosestPoints(pointsCopy, currentPoint, K);
			//Lasketaan keskim‰‰r‰inen et‰isyys
			avarage[n]=avarageDist(currentPoint, closestPoints, K);
		}
		//Lasketaan kaikkien keskim‰‰r‰isten et‰isyyksien keskim‰‰r‰inen et‰isyys
		double avaragesMeanValue=overallMean(avarage);
		//Lasketaan keskihajonta et‰isyyksille
		double distanceSD=overallStDev(avarage, avaragesMeanValue);
		
		//Luodaan kynnysarvo, jos pisteen l‰himpien pisteiden et‰isyyksien keskiarvo on
		//yli hajonnan p‰‰ss‰ kaikkien et‰isyyksien keskiarvosta, niin piste luokitellaan
		//poikkeavaksi.
		double tresholdValue=avaragesMeanValue+distanceSD;
		ArrayList<Coordinate> outliers=new ArrayList<Coordinate>();
		for(int n=0; n<points.length; n++) {
			if(avarage[n]>tresholdValue) {
				outliers.add(points[n]);
			}
		}
		return outliers;
	}
	
	/**********************************************************************************/
	//Funktio graafin luomiseen
	private static Graph<Coordinate> createGraph(int size) {
		readInput();
		Graph<Coordinate> graph=new Graph<Coordinate>(size);
		//Luodaan graafin solmut ja lis‰t‰‰n ne graafiin.
		//Solmun avaimeksi m‰‰ritell‰‰n luku 0-399
		for(int n=0; n<400; n++) {
			Node<Coordinate> node=new Node<Coordinate>(new Coordinate(x[n], y[n]), n);
			graph.addNode(node, n);
		}
		return graph;
	}
	//Lis‰t‰‰n l‰hin (ei tunnettu) naapuri jokaiselle graafin solmulle
	public static void addClosestNeighborToAll(Graph<Coordinate> graph) {
		//Haetaan graafin kaikki solmut
		Collection<Node<Coordinate>> nodes=graph.getAllNodes();
		//Selataan kaikki solmut l‰pi
		Iterator<Node<Coordinate>> itr=nodes.iterator();
		while(itr.hasNext()) {
			Node<Coordinate> current=itr.next();
			//Lis‰t‰‰n solmulle l‰hin naapuri
			addClosestNeighbor(graph, current.id);
		}
	}
	//Lis‰t‰‰n l‰hin naapuri yhdelle solmulle
	public static void addClosestNeighbor(Graph<Coordinate> graph, int nodeKey) {
		if(graph.contains(nodeKey)) {
			int closestIdx=0;
			double minDist=Double.MAX_VALUE;
			//Selataan graafin solmmut l‰pi
			Collection<Node<Coordinate>> nodes=graph.getAllNodes();
			Iterator<Node<Coordinate>> itr=nodes.iterator();
			while(itr.hasNext()) {
				Node<Coordinate> current=itr.next();
				//Lasketaan et‰isyys kahden solmun v‰lill‰
				double dist=euclideanDistance(graph.getNode(nodeKey), current);
				//Jos et‰isyys on pienin tunnettu...
				if(nodeKey!=current.id && !graph.hasEdge(nodeKey, current.id) && dist<minDist) {
					//p‰ivitet‰‰n muuttujien arvot
					closestIdx=current.id;
					minDist=dist;
				}	
			}
			//Lis‰t‰‰n kaari graafiin
			graph.addEdge(nodeKey, closestIdx, minDist);	
		}	
	}
	//Funktio kahden solmun v‰lisen et‰isyyden laskemiseen
	private static double euclideanDistance(Node<Coordinate> n, Node<Coordinate> m) {
		Coordinate nCoord=n.getElement();
		Coordinate mCoord=m.getElement();
		double distance=Math.sqrt(Math.pow((nCoord.x-mCoord.x), 2) + Math.pow((nCoord.y-mCoord.y), 2));
		return distance;
	}
	//Funktio solmujen sis‰ -ja ulkoasteiden tulostamiseen
	private static void printDegreesToFile(Graph<Coordinate> graph) {
		FileWriteHandler.init("Degrees.txt");
		Collection<Node<Coordinate>> nodes=graph.getAllNodes();
		Iterator<Node<Coordinate>> itr=nodes.iterator();
		while(itr.hasNext()) {
			Node<Coordinate> current=itr.next();
			FileWriteHandler.writeToFile(current.toString());
			FileWriteHandler.writeToFile("In-Degree: "+current.inDegree());
			FileWriteHandler.writeToFile("Out-Degree: "+current.outDegree()+"\n");
		}
		FileWriteHandler.closeFile();
	}
	//Funktio luo annetusta graafista vahvasti yhdistetyn.
	private static void makeGraphConnected(Graph<Coordinate> graph){
		//Niin kauan kun graafi ei ole vahvasti yhdistetty...
		while(!graph.isStronglyConnected()) {
			//Lis‰t‰‰n l‰himpi‰ naapureita solmuille
			addClosestNeighborToAll(graph);
		}
	}
	
	

	public static void main(String[] args) {
		//Teht‰v‰ 1
		//Luodaan graafi 400 solmulle
		Graph<Coordinate> graph=createGraph(400);
		//Lis‰t‰‰n jokaiselle solmulle l‰hin naapuri
		addClosestNeighborToAll(graph);
		
		//Teht‰v‰ 2
		//Lis‰t‰‰n jokaiselle solmulle toiseksi l‰hin naapuri
		addClosestNeighborToAll(graph);
		
		//Teht‰v‰ 3
		//M‰‰ritell‰‰n leveyshaun aloitussolmun tunnus [0, 399]
		int bfsStartNodeId=0;
		//Avataan tiedosto kirjoitusta varten
		FileWriteHandler.init("BFS.txt");
		//Suoritetaan leveyshaku
		graph.BFS(bfsStartNodeId, true);
		FileWriteHandler.closeFile();
		
		//Teht‰v‰ 4
		//M‰‰ritell‰‰n syvyysyshaun aloitussolmun tunnus [0, 399]
		int dfsStartNodeId=0;
		FileWriteHandler.init("DFS.txt");
		//Suoritetaan syvyyshaku
		graph.DFS(dfsStartNodeId, true);
		FileWriteHandler.closeFile();

		//Teht‰v‰ 5
		printDegreesToFile(graph);
		
		//Teht‰v‰ 6
		//M‰‰ritell‰‰n poistettavan solmun tunnus [0, 399]
		int removableId=0;
		//M‰‰ritell‰‰n syvyysyshaun aloitussolmun tunnus
		dfsStartNodeId=1;
		graph.deleteNode(removableId);
		FileWriteHandler.init("DIM.txt");
		graph.DFS(dfsStartNodeId, true);
		FileWriteHandler.closeFile();
		
		//Teht‰v‰ 7
		//Tehd‰‰n graafista vahvasti yhdistetty
		makeGraphConnected(graph);
		//M‰‰ritell‰‰n syvyysyshaun aloitussolmun tunnus
		dfsStartNodeId=1;
		FileWriteHandler.init("COMP.txt");
		graph.DFS(dfsStartNodeId, true);
		FileWriteHandler.closeFile();
		
		//Teht‰v‰ 10
		//Luodaan uusi graafi, joka sis‰lt‰‰ pistejoukon poikkeavat arvot (outliers)
		//Luodaan uudet koordinaattiarvot
		Coordinate[] points=new Coordinate[400];
		for(int n=0; n<x.length; n++) {
			points[n]=new Coordinate(x[n], y[n]);
		}
		//Haetaan poikkeavat arvot pistejoukosta
		ArrayList<Coordinate> outliers=getOutliers(points);
		//Luodaan uusi graafi ja lis‰t‰‰n siihen outlier-pisteit‰ vastaavat solmut
		Graph<Coordinate> graph2=new Graph<Coordinate>(outliers.size());
		for(int n=0; n<outliers.size(); n++) {
			Node<Coordinate> newNode=new Node<Coordinate>(outliers.get(n), n);
			graph2.addNode(newNode,  n);
		}
		//Tehd‰‰n graafista yhdistetty leveyshakua varten
		makeGraphConnected(graph2);
		//M‰‰ritell‰‰n leveyshaun aloitussolmun tunnus [0, n].
		bfsStartNodeId=0;
		FileWriteHandler.init("OUTLIERS.txt");
		graph2.BFS(bfsStartNodeId, true);
		FileWriteHandler.closeFile();
	}
}