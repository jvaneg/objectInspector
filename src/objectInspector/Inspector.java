package objectInspector;

import java.lang.reflect.*;

public class Inspector
{
	
	public Inspector()
	{
		//do NOTHING hahaha
	}
	
	
	public void inspect(Object obj, boolean recursive)
	{
		Class classObj;
		
		classObj = obj.getClass();
		
		printName(classObj);
		
		printSuperClass(classObj);
		
		printInterfaces(classObj);
		
		printAllMethods(classObj);
		
	}
	
	
	private void printName(Class classObj)
	{	
		System.out.println("Class name:\t" + classObj.getSimpleName());
	}
	
	private void printSuperClass(Class classObj)
	{
		Class superClassObj = classObj.getSuperclass();
		
		if(superClassObj == null)
		{
			System.out.println("Immediate Superclass:\t" + "No Superclass");
		}
		else
		{
			System.out.println("Immediate Superclass:\t" + superClassObj.getSimpleName());
		}	
	}
	
	private void printInterfaces(Class classObj)
	{
		Class interfaces[] = classObj.getInterfaces();
		
		if(interfaces.length == 0)
		{
			System.out.println("Interfaces implemented:\t" + "No Interfaces");
		}
		else if(interfaces.length == 1)
		{
			System.out.println("Interface implemented:\t" + interfaces[0].getSimpleName());
		}
		else
		{
			System.out.print("Interface implemented:\t");
			
			for(int i = 0; i < interfaces.length; i++)
			{
				System.out.print(interfaces[i].getSimpleName());
				if( i < interfaces.length - 1)
				{
					System.out.print(", ");
				}
			}
		}
	}
	
	private void printAllMethods(Class classObj)
	{
		Method methods[] = classObj.getDeclaredMethods();
		
		if(methods.length == 0)
		{
			System.out.println("Declares Methods:\t" + "No methods declared");
		}
		else if(methods.length == 1)
		{
			System.out.println("Declares Methods:");
			System.out.print("\t");
			printMethod(methods[0]);
		}
		else
		{
			System.out.println("Declares Methods");
			
			for(int i = 0; i < methods.length; i++)
			{
				System.out.print("\t");
				printMethod(methods[i]);
			}
		}
	}
	
	private void printMethod(Method methodObj)
	{
		String formattedString = "";
		int modifiers = methodObj.getModifiers();
		Class parameters[] = methodObj.getParameterTypes();
		Class exceptions[] = methodObj.getExceptionTypes();
		
		if(modifiers > 0) //TODO replace constant
		{
			formattedString += Modifier.toString(modifiers) + " ";
		}
		
		formattedString += methodObj.getReturnType().getSimpleName() + " " + methodObj.getName();
		
		formattedString += "(";
		for(int i = 0; i < parameters.length; i++)
		{
			formattedString += parameters[i].getSimpleName();
			if( i < parameters.length - 1)
			{
				formattedString += ", ";
			}
		}
		formattedString += ")";
		
		if(exceptions.length > 0)
		{
			formattedString += " throws ";
			for(int i = 0; i < exceptions.length; i++)
			{
				formattedString += exceptions[i].getSimpleName();
				if( i < exceptions.length - 1)
				{
					formattedString += ", ";
				}
			}
		}
		
		
		System.out.println(formattedString);
	}
}
