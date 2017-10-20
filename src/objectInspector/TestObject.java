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
	
	private int oWaddup(int coolNUMBER) throws IOException
	{
		return 1+1;
	}
}
