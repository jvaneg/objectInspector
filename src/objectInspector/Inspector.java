package objectInspector;

import java.lang.reflect.*;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Inspector
{	
	private static final int DIVIDER_LENGTH = 80;
	private static final int SUBDIVIDER_LENGTH = 70;
	
    private boolean recursive = false;
    private Queue<AbstractMap.SimpleImmutableEntry<Class, Object>> superQueue = new LinkedList<AbstractMap.SimpleImmutableEntry<Class, Object>>();
    private Queue<AbstractMap.SimpleImmutableEntry<Class, Object>> interfaceQueue = new LinkedList<AbstractMap.SimpleImmutableEntry<Class, Object>>();
    private Queue<AbstractMap.SimpleImmutableEntry<Object, Object>> newobjectQueue = new LinkedList<AbstractMap.SimpleImmutableEntry<Object, Object>>();
    private Queue<Object> objectQueue = new LinkedList<Object>();
    private HashSet<Class> seenInterfaces = new HashSet<Class>();
    private HashSet<Object> seenObjects = new HashSet<Object>();
    private String insertBefore = "| "; //TODO make this a constant
	
	public Inspector()
	{
		//do NOTHING hahaha
	}
	
	
	public void inspect(Object obj, boolean recursive)
	{
	    this.recursive = recursive;
		Class classObj;
		String objectID;
		//String insertBefore;
		AbstractMap.SimpleImmutableEntry<Class, Object> superPair;
		AbstractMap.SimpleImmutableEntry<Class, Object> interfacePair;
		AbstractMap.SimpleImmutableEntry<Object, Object> objectFoundInPair;
		
		objectQueue.add(obj);
		newobjectQueue.add(new AbstractMap.SimpleImmutableEntry<>(obj, null));
		
		seenObjects.add(obj);
		
		while(!objectQueue.isEmpty())
		{
			obj = objectQueue.remove();
			classObj = obj.getClass();
			
			
			objectID = generateObjectID(obj);
			
			System.out.println(generateDividerLine('=', DIVIDER_LENGTH, objectID));
			
			if(classObj.isArray())
			{
				printArrayInfo(classObj, obj, insertBefore);
			}
			else
			{
				printObjectInfo(classObj, obj, insertBefore);
				System.out.println(insertBefore);
				
				if(!superQueue.isEmpty())
				{
					System.out.println(generateDividerLine('=', DIVIDER_LENGTH, ("Superclass Hierarchy of " + objectID)));
					
					indentIncrease();
					
					while(!superQueue.isEmpty())
					{
					    superPair = superQueue.remove();
					    printObjectInfo(superPair.getKey(), superPair.getValue(), insertBefore);
					    System.out.println(insertBefore);
					}
					
					indentDecrease();
				}
				
				if(!interfaceQueue.isEmpty())
				{
					System.out.println(generateDividerLine('=', DIVIDER_LENGTH, ("Interfaces of " + objectID)));
					
					indentIncrease();
					seenInterfaces.clear();
				
					while(!interfaceQueue.isEmpty())
					{
					    interfacePair = interfaceQueue.remove();
					    printObjectInfo(interfacePair.getKey(), interfacePair.getValue(), insertBefore);
					    System.out.println(insertBefore);
					}
					
					indentDecrease();
				}
			}
		
			System.out.println(generateDividerLine('=', DIVIDER_LENGTH, "") + "\n");
		}
	}
	
	private void printObjectInfo(Class classObj, Object obj, String insertBefore)
	{
		//System.out.println(insertBefore + "------------------------------------------------");
		System.out.println(insertBefore + generateDividerLine('-', SUBDIVIDER_LENGTH, ""));
		System.out.println(insertBefore + printName(classObj));
        System.out.println(insertBefore + printSuperClass(classObj, obj));
        System.out.println(insertBefore + printInterfaces(classObj, obj));
        System.out.println(printAllConstructors(classObj, insertBefore));
        System.out.println(printAllMethods(classObj, insertBefore));
        System.out.println(printAllFields(classObj, insertBefore)); 
        System.out.println(printAllFieldValues(classObj, obj, insertBefore));
        //System.out.println(insertBefore + "------------------------------------------------");
        System.out.println(insertBefore + generateDividerLine('-', SUBDIVIDER_LENGTH, ""));
	}
	
	private void printArrayInfo(Class classObj, Object arrayObj, String insertBefore)
	{
		System.out.print(insertBefore + "Array contents:\n" + insertBefore);
		System.out.println("\t" + printArray(classObj, null, arrayObj));
	}
	
	private String generateObjectID(Object obj)
	{
		Class classObj = obj.getClass();
		return classObj.getSimpleName() + "@" + Integer.toHexString(obj.hashCode());	
	}
	
	private void indentIncrease()
	{
		insertBefore += "| ";
	}
	
	private void indentDecrease()
	{
		if(this.insertBefore.length() > 2)
		{
			insertBefore = insertBefore.substring(0, insertBefore.length() - 2);
		}
	}
	
	private String generateDividerLine(char lineChar, int lineLength, String lineContent)
	{
		int contentLength = lineContent.length();
		String dividerLine = "";
		int firstHalfLength;
		int secondHalfLength;
		
		if(contentLength == 0)
		{
			for(int i = 0; i < lineLength; i++)
			{
				dividerLine += lineChar;
			}
		}
		else
		{
			if(contentLength >= lineLength - 1)
			{
				dividerLine += lineChar + " " + lineContent + " " + lineChar;
			}
			else
			{
				firstHalfLength = (lineLength - contentLength - 2)/2;
				secondHalfLength = (lineLength - contentLength - 2) - firstHalfLength;
				
				for(int i = 0; i < firstHalfLength; i++)
				{
					dividerLine += lineChar;
				}
				dividerLine += " " + lineContent + " ";
				for(int i = 0; i < secondHalfLength; i++)
				{
					dividerLine += lineChar;
				}
			}
		}
		
		return dividerLine;
	}
	
	
	private String printName(Class classObj)
	{	
		return "Class name:  " + classObj.getSimpleName();
	}
	
	private String printSuperClass(Class classObj, Object obj)
	{
		String formattedString = "";
		Class superClassObj = classObj.getSuperclass();
		
		formattedString += "Immediate Superclass:  ";
		
		if(superClassObj == null)
		{
			formattedString += "No Superclass";
		}
		else
		{
			formattedString += superClassObj.getSimpleName();
			superQueue.add(new AbstractMap.SimpleImmutableEntry<>(superClassObj, obj));
		}
		
		return formattedString;
	}
	
	private String printInterfaces(Class classObj, Object obj)
	{
		String formattedString = "";
		Class interfaces[] = classObj.getInterfaces();
		
		if(interfaces.length == 1)
		{
			formattedString += "Interface implemented:  ";
		}
		else
		{
			formattedString += "Interfaces implemented:  ";
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
				
				if(!seenInterfaces.contains(interfaces[i]))
				{
					interfaceQueue.add(new AbstractMap.SimpleImmutableEntry<>(interfaces[i], obj));
					seenInterfaces.add(interfaces[i]);
				}
				
				if( i < interfaces.length - 1)
				{
					formattedString += ", ";
				}
			}
		}
		
		return formattedString;
	}
	
	private String printAllMethods(Class classObj, String insertBefore)
	{
		String formattedString = insertBefore;
		Method methods[] = classObj.getDeclaredMethods();
		
		if(methods.length == 1)
		{
			formattedString += "Method:\n" + insertBefore;
		}
		else
		{
			formattedString += "Methods:\n" + insertBefore;
		}
		
		if(methods.length == 0)
		{
			formattedString += "\tNo methods declared";
		}
		else
		{
			for(int i = 0; i < methods.length; i++)
			{
				formattedString += "\t" + printMethod(methods[i]);
				if( i < methods.length - 1)
				{
					formattedString += "\n" + insertBefore;
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
	
	private String printAllConstructors(Class classObj, String insertBefore)
	{
		String formattedString = insertBefore;
		Constructor constructors[] = classObj.getConstructors();
		
		if(constructors.length == 1)
		{
			formattedString += "Constructor:\n" + insertBefore;
		}
		else
		{
			formattedString += "Constructors:\n" + insertBefore;
		}
		
		if(constructors.length == 0)
		{
			formattedString += "\tNo constructors declared";
		}
		else
		{
			for(int i = 0; i < constructors.length; i++)
			{
				formattedString += "\t" + printConstructor(constructors[i]);
				if( i < constructors.length - 1)
				{
					formattedString += "\n" + insertBefore;
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
	
	private String printAllFields(Class classObj, String insertBefore)
	{
		String formattedString = insertBefore;
		Field fields[] = classObj.getDeclaredFields();
		
		if(fields.length == 1)
		{
			formattedString += "Field:\n" + insertBefore;
		}
		else
		{
			formattedString += "Fields:\n" + insertBefore;
		}
		
		if(fields.length == 0)
		{
			formattedString += "\tNo fields declared";
		}
		else
		{
			for(int i = 0; i < fields.length; i++)
			{
				formattedString += "\t" + printField(fields[i]);
				if( i < fields.length - 1)
				{
					formattedString += "\n" + insertBefore;
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
	
	private String printAllFieldValues(Class classObj, Object obj, String insertBefore)
	{
		String formattedString = insertBefore;
		Field fields[] = classObj.getDeclaredFields();
		
		if(fields.length == 1)
		{
			formattedString += "Current Field Value:\n" + insertBefore;
		}
		else
		{
			formattedString += "Current Field Values:\n" + insertBefore;
		}
		
		if(fields.length == 0)
		{
			formattedString += "\tNo fields declared";
		}
		else
		{
			for(int i = 0; i < fields.length; i++)
			{
				formattedString += "\t" + printFieldValue(fields[i], obj);
				if( i < fields.length - 1)
				{
					formattedString += "\n" + insertBefore;
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
		    try
		    {
    		    if(!fieldObj.isAccessible())
                {
                    fieldObj.setAccessible(true);
                    formattedString += printArray(objType, fieldObj, fieldObj.get(obj));
                    fieldObj.setAccessible(false);
                }
                else
                {
                    formattedString += printArray(objType, fieldObj, fieldObj.get(obj));
                }
		    }
		    catch(Exception e)
            {
                //TODO deal with me in a better way
                e.printStackTrace();
            }
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
					if(fieldObj.get(obj) != null)
					{
						if(this.recursive)
						{
							if(!seenObjects.contains(fieldObj.get(obj)))
							{
								objectQueue.add(fieldObj.get(obj));
								seenObjects.add(fieldObj.get(obj));
							}
						}
						formattedString += " " + generateObjectID(fieldObj.get(obj));
					}
					else
					{
					    formattedString += " " + null;
					}
					fieldObj.setAccessible(false);
				}
				else
				{
				    if(fieldObj.get(obj) != null)
                    {
				    	if(this.recursive)
				    	{
					    	if(!seenObjects.contains(fieldObj.get(obj)))
							{
								objectQueue.add(fieldObj.get(obj));
								seenObjects.add(fieldObj.get(obj));
							}
				    	}
				    	formattedString += " " + generateObjectID(fieldObj.get(obj));
                    }
                    else
                    {
                        formattedString += " " + null;
                    }
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
	
	
	private String printArray(Class objType, Field fieldObj, Object arrayObj)
	{
	    String formattedString = "";
	    String arrayType = objType.getSimpleName();
	    String arrayTypeFront;
	    String arrayTypeEnd;
        int arrayLength = Array.getLength(arrayObj);
        Object arrayContent;
        Class arrayContentClass;
        
        arrayTypeFront = arrayType.substring(0, arrayType.indexOf('['));
        arrayTypeEnd = arrayType.substring(arrayType.indexOf('[') + 2, arrayType.length());
        arrayType = arrayTypeFront + "[" + arrayLength + "]" + arrayTypeEnd;
        formattedString += arrayType;
        if(fieldObj != null)
        {
        	formattedString += " " + fieldObj.getName();
        }
        formattedString += " = ";
        
        formattedString += "[";
        for(int i = 0; i < arrayLength; i++)
        {
            arrayContent = Array.get(arrayObj, i);
            
            if(arrayContent != null)
            {
                arrayContentClass = arrayContent.getClass();
                
                if(isPrimitiveWrapper(arrayContentClass))
                {
                    formattedString += arrayContent;
                }
                else if(arrayContentClass.isArray())
                {
                	if(!seenObjects.contains(arrayContent))
					{
						objectQueue.add(arrayContent);
						seenObjects.add(arrayContent);
					}
                	formattedString += generateObjectID(arrayObj);
                }
                else
                {
                	if(this.recursive)
					{
						if(!seenObjects.contains(arrayContent))
						{
							objectQueue.add(arrayContent);
							seenObjects.add(arrayContent);
						}
					}
                	formattedString += generateObjectID(arrayObj);
                }
            }
            else
            {
                formattedString += null;
            }
            
            if( i < arrayLength - 1)
            {
                formattedString += ", ";
            }
        }
        formattedString += "]";
	    
	    
	    return formattedString;
	}
	
	
	private boolean isPrimitiveWrapper(Class classObj)
	{
	    return( classObj == Boolean.class ||
	            classObj == Character.class ||
    	        classObj == Byte.class ||
    	        classObj == Short.class || 
    	        classObj == Integer.class || 
    	        classObj == Long.class || 
    	        classObj == Float.class || 
    	        classObj == Double.class || 
    	        classObj == Void.class );
	}
	
	
	
	
	
	
	
	
	
	
}
