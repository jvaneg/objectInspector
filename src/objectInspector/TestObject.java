package objectInspector;

import java.io.IOException;
import java.io.Serializable;

public class TestObject implements Serializable
{
	private int number;
	private char letter;
	
	
	public TestObject(int number, char letter)
	{
		this.number = number;
		this.letter = letter;
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
