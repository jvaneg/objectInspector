package objectInspector;

import java.lang.reflect.*;
import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Queue;

public class Inspector
{
    private boolean recursive = false;
    private Queue<AbstractMap.SimpleImmutableEntry<Class, Object>> superQueue = new LinkedList<AbstractMap.SimpleImmutableEntry<Class, Object>>();
    private Queue<AbstractMap.SimpleImmutableEntry<Class, Object>> interfaceQueue = new LinkedList<AbstractMap.SimpleImmutableEntry<Class, Object>>();
	
	public Inspector()
	{
		//do NOTHING hahaha
	}
	
	
	public void inspect(Object obj, boolean recursive)
	{
	    this.recursive = recursive;
		Class classObj = obj.getClass();
		String objectID;
		String insertBefore = "| ";
		AbstractMap.SimpleImmutableEntry<Class, Object> superPair;
		AbstractMap.SimpleImmutableEntry<Class, Object> interfacePair;
		
		
		objectID = classObj.getSimpleName() + "@" + Integer.toHexString(obj.hashCode());
		
		System.out.println("============ " + objectID + " ============");
		
		System.out.println(insertBefore + printName(classObj));
		
		System.out.println(insertBefore + printSuperClass(classObj, obj));
		
		System.out.println(insertBefore + printInterfaces(classObj, obj));
		
		System.out.println(printAllConstructors(classObj, insertBefore));
		
		System.out.println(printAllMethods(classObj, insertBefore));
		
		System.out.println(printAllFields(classObj, insertBefore));
		
		System.out.println(printAllFieldValues(classObj, obj, insertBefore));
		
		System.out.println( insertBefore + "========= Superclass of " + objectID + " ==========");
		
		while(!superQueue.isEmpty())
		{
		    superPair = superQueue.remove();
		    classObj = superPair.getKey();
		    obj = superPair.getValue();
		    
		    System.out.println(insertBefore + printName(classObj));
	        
	        System.out.println(insertBefore + printSuperClass(classObj, obj));
	        
	        System.out.println(insertBefore + printInterfaces(classObj, obj));
	        
	        System.out.println(printAllConstructors(classObj, insertBefore));
	        
	        System.out.println(printAllMethods(classObj, insertBefore));
	        
	        System.out.println(printAllFields(classObj, insertBefore));
	        
	        System.out.println(printAllFieldValues(classObj, obj, insertBefore));
		}
		
		System.out.println( insertBefore + "========= Interfaces of " + objectID + " ==========");
		
		while(!interfaceQueue.isEmpty())
		{
		    interfacePair = interfaceQueue.remove();
            classObj = interfacePair.getKey();
            obj = interfacePair.getValue();
            
            System.out.println(insertBefore + printName(classObj));
            
            System.out.println(insertBefore + printSuperClass(classObj, obj));
            
            System.out.println(insertBefore + printInterfaces(classObj, obj));
            
            System.out.println(printAllConstructors(classObj, insertBefore));
            
            System.out.println(printAllMethods(classObj, insertBefore));
            
            System.out.println(printAllFields(classObj, insertBefore));
            
            System.out.println(printAllFieldValues(classObj, obj, insertBefore));
		}
		
		System.out.println("==============================================");
		
	}
	
	
	private String printName(Class classObj)
	{	
		return "Class name:\t" + classObj.getSimpleName();
	}
	
	private String printSuperClass(Class classObj, Object obj)
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
				interfaceQueue.add(new AbstractMap.SimpleImmutableEntry<>(interfaces[i], obj));
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
		//Class classObj = obj.getClass();
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
					    formattedString += " " + objType.getSimpleName() + "@" + Integer.toHexString(fieldObj.get(obj).hashCode());
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
                        formattedString += " " + objType.getSimpleName() + "@" + Integer.toHexString(fieldObj.get(obj).hashCode());
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
        formattedString += arrayType + " " + fieldObj.getName() + " = ";
        
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
                    //TODO put into my queue
                    formattedString += arrayContentClass.getSimpleName() + "@" + Integer.toHexString(arrayObj.hashCode());   
                }
                else
                {
                    //TODO if recursive push into queue
                    formattedString += arrayContentClass.getSimpleName() + "@" + Integer.toHexString(arrayObj.hashCode());
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
