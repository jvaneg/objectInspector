package objectInspector;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class TestObject implements Serializable
{
	private int number;
	private char letter;
	public double[] array = {1, 2, 3, 4, 5};
	public int[][] arr2 = new int[3][2];
	public final Socket socket = null;
	public final Socket socket2 = new Socket();
	private Socket[] socketArr = new Socket[2];
	
	
	public TestObject(int number, char letter)
	{
		this.number = number;
		this.letter = letter;
		socketArr[0] = new Socket();
		socketArr[1] = null;
		
	}
	
	private int rich_evans(int coolNUMBER) throws IOException
	{
		return 1+1;
	}
	
	public static String mike_stoklasa(int cool, char wow, char[] wowArray)
	{
		return "haha";
	}
}
