package objectInspector;

import java.lang.reflect.*;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;



/**
 * @author      Joel van Egmond
 * Email:       joel.vanegmond@ucalgary.ca
 * Student ID:  10102094
 * Course:      CPSC 501
 * Tutorial:    T01
 * Assignment:  2
 * Date:        22 OCT 2017
 * 
 * Purpose: A reflective object inspector that does a complete introspection of an object
 * at runtime.
 * 			
 */
public class Inspector
{	
	private static final int DIVIDER_LENGTH = 80;
	private static final int SUBDIVIDER_LENGTH = 70;
	private static final char DIVIDER_CHAR = '=';
	private static final char SUBDIVIDER_CHAR = '-';
	private static final String INDENT_STRING = "| ";
	private static final int NO_MODIFIERS = 0;
	
    private boolean recursive = false;
    private Queue<AbstractMap.SimpleImmutableEntry<Class, Object>> superQueue = new LinkedList<AbstractMap.SimpleImmutableEntry<Class, Object>>();
    private Queue<AbstractMap.SimpleImmutableEntry<Class, Object>> interfaceQueue = new LinkedList<AbstractMap.SimpleImmutableEntry<Class, Object>>();
    private Queue<Object> objectQueue = new LinkedList<Object>();
    private HashSet<Class> seenInterfaces = new HashSet<Class>();
    private HashSet<Object> seenObjects = new HashSet<Object>();
    private String insertBefore = INDENT_STRING;
	
    
    /**
     * Default constructor for the Inspector class, doesn't do anything special
     */
	public Inspector()
	{
		//do NOTHING hahaha
	}
	
	
	/**
	 * Performs complete inspection of an object at runtime.
	 * This includes:
	 * - Name of declaring class
	 * - Name of the immediate superclass
	 * - Name of the interfaces the class implements
	 * - Methods the class declares
	 * - Contructors the class declares
	 * - Fields the class declares
	 * - Current value of the fields
	 * - The above information for every superclass up the inheritance tree
	 * - The above information for every implemented interface up the inheritance tree
	 * 
	 * Additionally, if set to recursive mode, the inspector will also perform the full inspection
	 * of every object declared as a field.
	 * 
	 * @param obj : the object to inspect
	 * @param recursive : whether or not the run inspect in recursive mode
	 */
	public void inspect(Object obj, boolean recursive)
	{
	    this.recursive = recursive;
		Class classObj;
		String objectID;
		AbstractMap.SimpleImmutableEntry<Class, Object> superPair;
		AbstractMap.SimpleImmutableEntry<Class, Object> interfacePair;
		
		objectQueue.add(obj);
		
		seenObjects.add(obj);
		
		while(!objectQueue.isEmpty())
		{
			obj = objectQueue.remove();
			classObj = obj.getClass();
			
			
			objectID = generateObjectID(obj);
			
			System.out.println(generateDividerLine('=', DIVIDER_LENGTH, objectID));
			
			if(classObj.isArray())
			{
				System.out.println(printArrayInfo(classObj, obj, insertBefore));
			}
			else
			{
			    seenInterfaces.clear();
				printObjectInfo(classObj, obj, insertBefore);
				System.out.println(insertBefore);
				
				if(!superQueue.isEmpty())
				{
					System.out.println(insertBefore + generateDividerLine(DIVIDER_CHAR, DIVIDER_LENGTH - insertBefore.length(),
																		 	("Superclass Hierarchy of " + objectID)));
					
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
					System.out.println(insertBefore + generateDividerLine(DIVIDER_CHAR, DIVIDER_LENGTH - insertBefore.length(),
																			("Interfaces of " + objectID)));
					
					indentIncrease();
					
					while(!interfaceQueue.isEmpty())
					{
					    interfacePair = interfaceQueue.remove();
					    printObjectInfo(interfacePair.getKey(), interfacePair.getValue(), insertBefore);
					    System.out.println(insertBefore);
					}
					
					indentDecrease();
				}
			}
		
			System.out.println(generateDividerLine(DIVIDER_CHAR, DIVIDER_LENGTH, "") + "\n\n");
		}
	}
	
	
	/**
	 * Prints out the full set of information for an object, including:
	 * - Name
	 * - Immediate superclass
	 * - Interfaces implemented
	 * - Methods
	 * - Constructors
	 * - Fields
	 * - Field values
	 * - Divider lines
	 * @param classObj : the class metaobject whose contents are being printed out
	 * @param obj : the instantiated object whose values are being printed out
	 * @param insertBefore : string to add at the start of every new line in the string
	 */
	private void printObjectInfo(Class classObj, Object obj, String insertBefore)
	{
		System.out.println(insertBefore + generateDividerLine(SUBDIVIDER_CHAR, SUBDIVIDER_LENGTH - insertBefore.length(), ""));
		System.out.println(insertBefore + printName(classObj));
        System.out.println(insertBefore + printSuperClass(classObj, obj));
        System.out.println(insertBefore + printInterfaces(classObj, obj));
        System.out.println(printAllConstructors(classObj, insertBefore));
        System.out.println(printAllMethods(classObj, insertBefore));
        System.out.println(printAllFields(classObj, insertBefore)); 
        System.out.println(printAllFieldValues(classObj, obj, insertBefore));
        System.out.println(insertBefore + generateDividerLine(SUBDIVIDER_CHAR, SUBDIVIDER_LENGTH - insertBefore.length(), ""));
	}
	
	
	/**
	 * Formats the array and its contents as a string. Used for standalone arrays that are not
	 * fields of another object. 
	 * @param classObj : The array's type as a class
	 * @param arrayObj : the array itself as an Object
	 * @param insertBefore : string to add at the start of every new line in the string
	 */
	private String printArrayInfo(Class classObj, Object arrayObj, String insertBefore)
	{
		String formattedString = "";
		
		formattedString += insertBefore + "Array contents:\n" + insertBefore;
		formattedString += "\t" + printArray(classObj, null, arrayObj);
		
		//System.out.print(insertBefore + "Array contents:\n" + insertBefore);
		//System.out.println("\t" + printArray(classObj, null, arrayObj));
		
		return formattedString;
	}
	
	
	/**
	 * Generates the ObjectID of an object. This is the name for the object's class name followed
	 * by a unique hex hashcode.
	 * @param obj : the object whose ID is being generated
	 * @return the object's ID as a string
	 */
	private String generateObjectID(Object obj)
	{
		Class classObj = obj.getClass();
		return classObj.getSimpleName() + "@" + Integer.toHexString(obj.hashCode());	
	}
	
	
	/**
	 * Increases the indent of insertBefore by one INDENT_STRING
	 */
	private void indentIncrease()
	{
		insertBefore += INDENT_STRING;
	}
	
	
	/**
	 * Decreases the indent of insertBefore by one INDENT_STRING, to a minimum of
	 * one INDENT_STRING
	 */
	private void indentDecrease()
	{
		if(insertBefore.length() > INDENT_STRING.length())
		{
			insertBefore = insertBefore.substring(0, insertBefore.length() - INDENT_STRING.length());
		}
	}
	
	
	/**
	 * Generates a divider line of a specified length, made of a specified character and containing
	 * a specified string. Useful for aesthetic reasons, ensures the dividers will all be equal length etc.
	 * @param lineChar : the character which makes up the bulk of the divider line
	 * @param lineLength : the length the line should be (may be longer if line content string is longer)
	 * @param lineContent : the string to insert into the divider
	 * @return the line with inserted content as a string
	 */
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
	
	
	/**
	 * Returns the class' name as a string
	 * @param classObj : the class metaobject whose name is returned
	 * @return the class' name as a string
	 */
	private String printName(Class classObj)
	{	
		return "Class name:  " + classObj.getSimpleName();
	}
	
	
	/**
	 * Formats an object's superclass as a string.
	 * Details: unique superclasses encountered are added to queue to be output later
	 * @param classObj : the class metaobject whose superclass is being formatted as a string
	 * @param obj : the instantiated object whose superclass are being formatted
	 * @return : the superclass formatted as a string
	 */
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
	
	
	/**
	 * Formats the list of an object's implemented interfaces as a string.
	 * Details: unique interfaces encountered are added to queue to be output later
	 * @param classObj : the class metaobject whose interfaces are being formatted as a list string
	 * @param obj : the instantiated object whose interfaces are being formatted
	 * @return : the list of interfaces formatted as a string
	 */
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
	
	
	/**
	 * Formats all of an object's methods as a string.
	 * @param classObj : the class metaobject of the object whose methods are being formatted
	 * @param insertBefore : string to add at the start of every new line in the string
	 * @return the object's methods as a formatted string
	 */
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
	
	
	/**
	 * Formats a single method as a string. Includes modifiers, return type, parameter types,
	 * and exceptions. 
	 * @param methodObj : : the method to be formatted
	 * @return the method formatted as a string
	 */
	private String printMethod(Method methodObj)
	{
		String formattedString = "";
		int modifiers = methodObj.getModifiers();
		Class parameters[] = methodObj.getParameterTypes();
		Class exceptions[] = methodObj.getExceptionTypes();
		
		if(modifiers > NO_MODIFIERS)
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
	
	
	/**
	 * Formats all of an object's constructors as a string.
	 * @param classObj : the class metaobject of the object whose constructors are being formatted
	 * @param insertBefore : string to add at the start of every new line in the string
	 * @return the object's constructors as a formatted string
	 */
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

	
	/**
	 * Formats a single constructor as a string. Includes modifiers and parameter types.
	 * @param constructorObj : the constructor to be formatted
	 * @return the constructor formatted as a string
	 */
	private String printConstructor(Constructor constructorObj)
	{
		String formattedString = "";
		String constructorFullName;
		String splitName[];
		int modifiers = constructorObj.getModifiers();
		Class parameters[] = constructorObj.getParameterTypes();
		Class exceptions[] = constructorObj.getExceptionTypes();
		
		if(modifiers > NO_MODIFIERS)
		{
			formattedString += Modifier.toString(modifiers) + " ";
		}
		
		constructorFullName = constructorObj.getName();
		splitName = constructorFullName.split("\\.");	//strip the package name
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
	
	
	/**
	 * Formats all of an object's fields as a string.
	 * @param classObj : the class metaobject of the object whose fields are being formatted
	 * @param insertBefore : string to add at the start of every new line in the string
	 * @return the object's fields as a formatted string
	 */
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
	
	
	/**
	 * Formats a single field as a string. Includes modifiers and type.
	 * @param fieldObj : the field to be formatted
	 * @return the field formatted as a string
	 */
	private String printField(Field fieldObj)
	{
		String formattedString = "";
		
		int modifiers = fieldObj.getModifiers();
		
		if(modifiers > NO_MODIFIERS)
		{
			formattedString += Modifier.toString(modifiers) + " ";
		}
		
		formattedString += fieldObj.getType().getSimpleName() + " " + fieldObj.getName();
		
		return formattedString;
	}
	
	
	/**
	 * Formats all of an object's fields and their contents as a string.
	 * @param classObj : the class metaobject of the object being formatted
	 * @param obj : the object being formatted
	 * @param insertBefore : string to add at the start of every new line in the string
	 * @return the object's fields + contents as a formatted string
	 */
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
	
	
	/**
	 * Formats a field and its contents as a string. If the field contains an object will print
	 * the object's ID, if an array will print the array contents.
	 * @param fieldObj : the field to format
	 * @param obj : the object containing the field
	 * @return the field formatted as a string
	 */
	private String printFieldValue(Field fieldObj, Object obj)
	{
		String formattedString = "";
		int modifiers = fieldObj.getModifiers();
		Class objType = fieldObj.getType();
		
		if(modifiers > NO_MODIFIERS)
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
				System.err.println("Error getting field value!");
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
		    	System.err.println("Error getting field value!");
            }
		}
		else
		{
			formattedString += objType.getSimpleName() + " " + fieldObj.getName() + " =";
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
				System.err.println("Error getting field value!");
			}
		}
		
		return formattedString;
	}
	
	
	/**
	 * Formats an array's name, type, size, and contents as a string. If the array contains objects,
	 * prints out their ID and adds them to the queue, if it contains other arrays, does the same.
	 * @param objType : The array's type as a class
	 * @param fieldObj : the array as a field if it is a field of another object, null if not
	 * @param arrayObj : the array itself as an Object
	 * @return the formatted array output string
	 */
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
                	formattedString += generateObjectID(arrayContent);
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
                	formattedString += generateObjectID(arrayContent);
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
	
	
	/**
	 * Determines whether or not the specified class object is a wrapper class
	 * for a primitive
	 * @param classObj : the class to check
	 * @return whether or not the class is a primitive wrapper
	 */
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
