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
		Class classObj = obj.getClass();
		
		System.out.println(printName(classObj));
		
		System.out.println(printSuperClass(classObj));
		
		System.out.println(printInterfaces(classObj));
		
		System.out.println(printAllConstructors(classObj));
		
		System.out.println(printAllMethods(classObj));
		
		System.out.println(printAllFields(classObj));
		
		System.out.println(printAllFieldValues(obj));
		
		
		
	}
	
	
	private String printName(Class classObj)
	{	
		return "Class name:\t" + classObj.getSimpleName();
	}
	
	private String printSuperClass(Class classObj)
	{
		String formattedString = "";
		Class superClassObj = classObj.getSuperclass();
		
		formattedString += "Immediate Superclass:\t";
		
		if(superClassObj == null)
		{
			formattedString += "No Superclass";
		}
		else
		{
			formattedString += superClassObj.getSimpleName();
		}
		
		return formattedString;
	}
	
	private String printInterfaces(Class classObj)
	{
		String formattedString = "";
		Class interfaces[] = classObj.getInterfaces();
		
		if(interfaces.length == 1)
		{
			formattedString += "Interface implemented:\t";
		}
		else
		{
			formattedString += "Interfaces implemented:\t";
		}
		
		if(interfaces.length == 0)
		{
			formattedString += "No Interfaces";
		}
		else
		{
			for(int i = 0; i < interfaces.length; i++)
			{
				formattedString += interfaces[i].getSimpleName();
				if( i < interfaces.length - 1)
				{
					formattedString += ", ";
				}
			}
		}
		
		return formattedString;
	}
	
	private String printAllMethods(Class classObj)
	{
		String formattedString = "";
		Method methods[] = classObj.getDeclaredMethods();
		
		if(methods.length == 1)
		{
			formattedString += "Method:\n";
		}
		else
		{
			formattedString += "Methods:\n";
		}
		
		if(methods.length == 0)
		{
			formattedString += "No methods declared";
		}
		else
		{
			for(int i = 0; i < methods.length; i++)
			{
				formattedString += "\t" + printMethod(methods[i]);
				if( i < methods.length - 1)
				{
					formattedString += "\n";
				}
			}
		}
		
		return formattedString;
	}
	
	private String printMethod(Method methodObj)
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
		
		return formattedString;
	}
	
	private String printAllConstructors(Class classObj)
	{
		String formattedString = "";
		Constructor constructors[] = classObj.getConstructors();
		
		if(constructors.length == 1)
		{
			formattedString += "Constructor:\n";
		}
		else
		{
			formattedString += "Constructors:\n";
		}
		
		if(constructors.length == 0)
		{
			formattedString += "No constructors declared";
		}
		else
		{
			for(int i = 0; i < constructors.length; i++)
			{
				formattedString += "\t" + printConstructor(constructors[i]);
				if( i < constructors.length - 1)
				{
					formattedString += "\n";
				}
			}
		}
		
		return formattedString;
	}

	private String printConstructor(Constructor constructorObj)
	{
		String formattedString = "";
		String constructorFullName;
		String splitName[];
		int modifiers = constructorObj.getModifiers();
		Class parameters[] = constructorObj.getParameterTypes();
		Class exceptions[] = constructorObj.getExceptionTypes();
		
		if(modifiers > 0) //TODO replace constant
		{
			formattedString += Modifier.toString(modifiers) + " ";
		}
		
		constructorFullName = constructorObj.getName();
		splitName = constructorFullName.split("\\.");	//TODO ask ta if i should be stripping package name
		formattedString += splitName[splitName.length - 1];
		
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
		
		return formattedString;
	}
	
	private String printAllFields(Class classObj)
	{
		String formattedString = "";
		Field fields[] = classObj.getDeclaredFields();
		
		if(fields.length == 1)
		{
			formattedString += "Field:\n";
		}
		else
		{
			formattedString += "Fields:\n";
		}
		
		if(fields.length == 0)
		{
			formattedString += "No fields declared";
		}
		else
		{
			for(int i = 0; i < fields.length; i++)
			{
				formattedString += "\t" + printField(fields[i]);
				if( i < fields.length - 1)
				{
					formattedString += "\n";
				}
			}
		}
		
		return formattedString;
	}
	
	private String printField(Field fieldObj)
	{
		String formattedString = "";
		
		int modifiers = fieldObj.getModifiers();
		
		if(modifiers > 0) //TODO replace constant
		{
			formattedString += Modifier.toString(modifiers) + " ";
		}
		
		formattedString += fieldObj.getType().getSimpleName() + " " + fieldObj.getName();
		
		return formattedString;
	}
	
	private String printAllFieldValues(Object obj)
	{
		String formattedString = "";
		Class classObj = obj.getClass();
		Field fields[] = classObj.getDeclaredFields();
		
		if(fields.length == 1)
		{
			formattedString += "Current Field Value:\n";
		}
		else
		{
			formattedString += "Current Field Values:\n";
		}
		
		if(fields.length == 0)
		{
			formattedString += "No fields declared";
		}
		else
		{
			for(int i = 0; i < fields.length; i++)
			{
				formattedString += "\t" + printFieldValue(fields[i], obj);
				if( i < fields.length - 1)
				{
					formattedString += "\n";
				}
			}
		}
		
		
		return formattedString;
	}
	
	private String printFieldValue(Field fieldObj, Object obj)
	{
		String formattedString = "";
		int modifiers = fieldObj.getModifiers();
		Class objType = fieldObj.getType();
		
		if(modifiers > 0) //TODO replace constant
		{
			formattedString += Modifier.toString(modifiers) + " ";
		}
		
		
		//formattedString += "\t\t";
		
		if(objType.isPrimitive())
		{
			formattedString += objType.getSimpleName() + " " + fieldObj.getName() + " =";
			try
			{
				if(!fieldObj.isAccessible())
				{
					fieldObj.setAccessible(true);
					formattedString += " " + fieldObj.get(obj);
					fieldObj.setAccessible(false);
				}
				else
				{
					formattedString += " " + fieldObj.get(obj);
				}
			}
			catch(Exception e)
			{
				//TODO deal with me in a better way
				e.printStackTrace();
			}
		}
		else if(objType.isArray())
		{
			formattedString += objType.getSimpleName() + " " + fieldObj.getName() + " =";
			System.out.println("WE ARRAY");
		}
		else
		{
			formattedString += objType.getSimpleName() + " " + fieldObj.getName() + " =";
			//FANCIER STUFF FOR THE RECURSION PART
			try
			{
				if(!fieldObj.isAccessible())
				{
					fieldObj.setAccessible(true);
					formattedString += " " + objType.getSimpleName() + "@" + Integer.toHexString(fieldObj.get(obj).hashCode());
					fieldObj.setAccessible(false);
				}
				else
				{
					formattedString += " " + objType.getSimpleName() + "@" + Integer.toHexString(fieldObj.get(obj).hashCode());
				}
			}
			catch(Exception e)
			{
				//TODO deal with me in a better way
				e.printStackTrace();
			}
		}
		
		return formattedString;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
