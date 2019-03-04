import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Apuluokka tiedostoon kirjoitusta varten
 */
public final class FileWriteHandler{
	private static BufferedWriter bw;
	
	public static void init(String filename) {
		try {
			bw=new BufferedWriter(new FileWriter(filename));
		}
		catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	public static void closeFile() {
		try {
			bw.close();
		}
		catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	public static void writeToFile(String txt) {
		try {
			bw.write(txt);
			bw.newLine();
		}
		catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
}