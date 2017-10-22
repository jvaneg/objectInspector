package objectInspector;

import java.util.ArrayList;

public class TestDriver
{
	public static void main(String[] args)
    {
        Inspector inspector = new Inspector();
        TestObject testObj = new TestObject(5, 'j');
        ArrayList<Long> intList = new ArrayList<Long>();
        int intarr[] = new int[3];
        
        
        inspector.inspect(testObj, true);
    }
}
