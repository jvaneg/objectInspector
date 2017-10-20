package objectInspector;


public class TestDriver
{
	public static void main(String[] args)
    {
        Inspector inspector = new Inspector();
        TestObject testObj = new TestObject(5, 'j');
        
        
        inspector.inspect(testObj, false);
    }
}
