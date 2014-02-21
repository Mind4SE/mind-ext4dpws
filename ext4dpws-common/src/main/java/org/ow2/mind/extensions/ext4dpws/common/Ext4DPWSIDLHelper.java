/**
 * ================================================================================
 *
 *   					 Copyright (C) 2010 Sogeti High Tech
 *
 *    This file is part of "mindext4dpws-0.1-alpha-6".
 *    It is a free software: you can redistribute it and/or modify it under the terms
 *    of the GNU Lesser General Public License as published by the Free Software
 *    Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful, but WITHOUT
 *    ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *    FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 *    details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * ================================================================================
 */

package org.ow2.mind.extensions.ext4dpws.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.Node;
import org.objectweb.fractal.adl.interfaces.Interface;
import org.objectweb.fractal.adl.interfaces.InterfaceContainer;
import org.objectweb.fractal.adl.types.TypeInterfaceUtil;
import org.ow2.mind.idl.IDLLoader;
import org.ow2.mind.idl.ast.EnumDefinition;
import org.ow2.mind.idl.ast.EnumMember;
import org.ow2.mind.idl.ast.EnumReference;
import org.ow2.mind.idl.ast.IDL;
import org.ow2.mind.idl.ast.IDLASTHelper;
import org.ow2.mind.idl.ast.Include;
import org.ow2.mind.idl.ast.InterfaceDefinition;
import org.ow2.mind.idl.ast.Member;
import org.ow2.mind.idl.ast.Method;
import org.ow2.mind.idl.ast.Parameter;
import org.ow2.mind.idl.ast.PointerOf;
import org.ow2.mind.idl.ast.PrimitiveType;
import org.ow2.mind.idl.ast.StructDefinition;
import org.ow2.mind.idl.ast.StructReference;
import org.ow2.mind.idl.ast.Type;
import org.ow2.mind.idl.ast.TypeDefReference;

/**
 * Utility class used to manipulate IDL.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public class Ext4DPWSIDLHelper {


	//public static IDLFrontend idlFrontEnd = IDLLoaderChainFactory.newLoader();

	public static final String ERRORS_IDT_FILE_PATH = "/org/ow2/mind/extensions/ext4dpws/Errors.idt";


	/**
	 * Check if a parameter is in/out.
	 * @param p The parameter to check.
	 * @return True if is in/out, false else.
	 */
	public static boolean isInOutParameter(Parameter p) {

		return (p.getIsIn() != null 
				&& p.getIsIn().equals(Parameter.TRUE)
				&& p.getIsOut() != null
				&& p.getIsOut().equals(Parameter.TRUE));
	}

	/**
	 * Check if a parameter is out.
	 * @param p The parameter to check.
	 * @return True if is out, false else.
	 */
	public static boolean isOutParameter(Parameter p) {

		return (p.getIsOut() != null
				&& p.getIsOut().equals(Parameter.TRUE));
	}

	/**
	 * Check if a parameter is in.
	 * @param p The parameter to check.
	 * @return True if is in or not out neither explicitly in, false else.
	 */
	public static boolean isInParameter(Parameter p) {

		return ((p.getIsIn() != null 
				&& p.getIsIn().equals(Parameter.TRUE))
				|| (p.getIsIn() == null && p.getIsOut() == null));
	}

	/**
	 * Check if the return parameter type of the given method is int.
	 * (in line with the distribution specifications) 
	 * @param m The method to check.
	 * @throws InvalidIDLException : Exception if the return type is not from int type.
	 */
	public static void controlReturnParameter(Method m) throws InvalidIDLException {
		Type t = m.getType();
		if (t instanceof PrimitiveType)
		{
			String name = ((PrimitiveType)t).getName();
			if(PrimitiveType.PrimitiveTypeEnum.fromIDLTypeName(name) == PrimitiveType.PrimitiveTypeEnum.VOID)
			{
				return;
			}
		}

		throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_RETURN_TYPE,t);

	}


	/**
	 * Get the server interfaces of a component definition.
	 * @param definition : The component definition.
	 * @return An unmodifiable list of server interfaces.
	 * @throws ADLException : Exception during the ADL parse.
	 */
	public static Collection<Interface> getServerInterfaces(Definition definition) throws ADLException {

		List<Interface> result = new ArrayList<Interface>();
		for (Interface itf : ((InterfaceContainer) definition).getInterfaces()) 
		{
			if (TypeInterfaceUtil.isServer(itf)) 
			{			
				result.add(itf);
			}
		}
		return Collections.unmodifiableCollection(result);
	}

	/**
	 * Utility : sometimes soapcpp generate a response struct...sometimes no. It depends on the number of out parameters.
	 * @param op : The WSDL operation.
	 * @return : True if we have to deal with a response struct, false else.
	 */
	public static boolean haveToDefineStruct(Method meth) {

		Parameter [] params = meth.getParameters();

		int numberOfOut = 0;

		for (Parameter p : params) 
		{
			if (isInOutParameter(p)) 
			{
				numberOfOut++;
				if(p.getType() instanceof TypeDefReference) 
				{
					numberOfOut++;
				}
			} else if (isOutParameter(p)) 
			{
				numberOfOut++;		
				if(p.getType() instanceof TypeDefReference
						|| p.getType() instanceof StructReference || p.getType() instanceof PointerOf) 
				{
					numberOfOut++;
				} 
			} else if(isInParameter(p)
					&& isArrayOfStruct(p.getType()))
			{
				numberOfOut +=2;
			}
		}

		return numberOfOut >= 2
		|| numberOfOut == 0;
	}

	/**
	 * Check if an IDL include the required IDT for remote errors system.
	 * @param idl The input IDL.
	 * @throws InvalidIDLException
	 */
	public static void checkErrorsIDTInclude(InterfaceDefinition idl) throws InvalidIDLException
	{
		Node [] includeNodes = idl.astGetNodes("include");
		boolean found  = false;
		for(Node n : includeNodes)
		{
			if(((Include) n).getPath().equals("\"" + ERRORS_IDT_FILE_PATH + "\""))				
			{
				found = true;
				break;
			}		
		}

		if(!found)
		{
			throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_MUST_INCLUDE_ERRORS_IDT,idl,ERRORS_IDT_FILE_PATH);
		}
	}

	/**
	 * Control a type found in an interface definition. 
	 * @param itfDefinition The interface definition where the type has been encountered.
	 * @param type The type to check.
	 * @param context The mind compiler current context.
	 * @throws InvalidIDLException Exception is the type is invalid (for example PointerOf are forbidden in distribution)
	 */
	private static void controlType(InterfaceDefinition itfDefinition, Parameter param, Type type, IDLLoader idlLoaderItf, Map <Object, Object> context) throws InvalidIDLException
	{
		String name = null;
		Type tmp;

		if(type instanceof PrimitiveType)
		{
			name = ((PrimitiveType)type).getName();
			switch (PrimitiveType.PrimitiveTypeEnum.fromIDLTypeName(name)) {
			case CHAR:
				break;
			case BOOLEAN:
				break;
			case INT:
				break;			
			case SHORT:
				break;
			case LONG :		
				break;
			case FLOAT :
				break;
			case DOUBLE:
				break;
			case STRING:
				break;
			case INT8_T:
				break;
			case UINT8_T:
				break;
			case INT16_T:
				break;
			case UINT16_T:
				break;
			case INT32_T:
				break;
			case UINT32_T:
				break;
			case INT64_T:
				break;
			case UINT64_T:
				break;			
			case VOID:
				break;
			default:
				throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC,type,param.getName());
			}
		} else if (type instanceof StructReference)
		{
			StructDefinition structDef = findStructDefinition(itfDefinition, (StructReference)type, idlLoaderItf,context);

			if(structDef == null)
			{
				throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_TYPE_DEF_NOT_FOUND,type,((StructReference)type).getName());
			}

			Type memberType;

			Member [] members = structDef.getMembers();
			for (Member m : members)
			{
				memberType = m.getType();

				if (memberType instanceof PrimitiveType)
				{
					controlType(itfDefinition, param, memberType, idlLoaderItf, context);

				} else if(memberType instanceof PointerOf)
				{			
					tmp = ((PointerOf) memberType).getType();

					if(tmp instanceof TypeDefReference)
					{
						if(!isArrayOfPrimitive((TypeDefReference)tmp))
						{
							throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC, type,param.getName());
						} 
					} else if(!(tmp instanceof StructReference) )
					{
						throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC, type,param.getName());
					}
				} else if(!(memberType instanceof EnumReference || memberType instanceof EnumDefinition)){
					throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC, type,param.getName());		
				}
			}

		}else if( type instanceof EnumReference)
		{
			EnumDefinition enumDef = findEnumDefinition(itfDefinition,(EnumReference)type, idlLoaderItf, context);

			if(enumDef == null)
			{
				throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC,type,param.getName());
			}

			for(EnumMember m : enumDef.getEnumMembers())
			{
				if(m.getConstantExpression() != null)
				{
					throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC,type,param.getName());
				}
			}			

		} else if(type instanceof TypeDefReference)
		{
			if(!isArrayOfPrimitive((TypeDefReference)type))
			{
				throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC, type,param.getName());
			} 
		} else if(!isArrayOfStruct(type))
		{
			throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC, type,param.getName());

		}
	}

	public static boolean  isArrayOfStruct(Type type)
	{
		return type instanceof PointerOf
		&& ((PointerOf) type).getType() instanceof PointerOf
		&& ((PointerOf)((PointerOf) type).getType()).getType() instanceof StructReference;
	}

	/**
	 * Get the struct definition of the struct reference array. 
	 * @param arrayOfStruct
	 * @return
	 * @throws InvalidIDLException 
	 */
	public static StructDefinition getArrayOfStructDefinition(IDL idl, Type arrayOfStruct, IDLLoader idlLoaderItf, Map<Object,Object> context) throws InvalidIDLException
	{
		StructReference ref = (StructReference) ((PointerOf)((PointerOf) arrayOfStruct).getType()).getType();
		return findStructDefinition(idl, ref, idlLoaderItf, context);

	}
	

	/**
	 * Process a deep search (look also into included idt) of a struct reference definition.
	 * @param idl The first IDL where start the search.
	 * @param structRef The input struct reference.
	 * @param context The current mind compiler context.
	 * @return The struct definition (null if not found)
	 * @throws InvalidIDLException : Exception during IDl parsing.
	 */
	public static StructDefinition findStructDefinition(IDL idl,StructReference structRef, IDLLoader idlLoaderItf, Map<Object, Object> context) throws InvalidIDLException
	{

		Node [] typesNode = idl.astGetNodes("type");	
		StructDefinition structDef = null;
		for (Node n : typesNode)
		{
			if(n instanceof StructDefinition)
			{
				structDef = (StructDefinition) n;
				if(structDef.getName().equals(structRef.getName()))
				{
					break;
				}
				structDef = null;
			} 
		}

		if( structDef == null && idl instanceof InterfaceDefinition) //not found, so we search in include IDL
		{
			Node [] includeNodes = idl.astGetNodes("include");
			Include inc;
			IDL includeIdl;
			for(Node n : includeNodes)
			{
				inc = (Include) n;
				try {
					includeIdl = IDLASTHelper.getIncludedIDL(inc, idlLoaderItf, context);
					structDef = findStructDefinition(includeIdl, structRef,idlLoaderItf,context);
					if(structDef != null)
					{
						break;
					}				
				} catch (ADLException e) {
					throw new InvalidIDLException(e.getError());
				}
			}
		} 

		return structDef;

	}

	/**
	 * Check if a TypeDefReference type is an ArrayOf. 
	 * @param value : The TypeDefReference type to check.
	 * @return : True if it is an ArrayOf typedef, false else.
	 */
	public static boolean isArrayOfPrimitive(Type type)
	{

		boolean result = false;
		if (type instanceof TypeDefReference)
		{

			TypeDefReference value = (TypeDefReference) type;

			result = value.getName().equals ("MindArrayOfInt")
			|| value.getName().equals ("MindArrayOfShort")
			|| value.getName().equals ("MindArrayOfFloat")
			|| value.getName().equals ("MindArrayOfChar")
			|| value.getName().equals ("MindArrayOfLong")
			|| value.getName().equals ("MindArrayOfString");//TODO complete with other type
		}
		return result;

	}

	/**
	 * Get the gsoap equivalent typedef name of an mind ArrayOf.
	 * @param value : The mind array typedef.
	 * @return : The equivalent typedef name. (for example in IDL you have to use MindArrayOfInt and the 
	 * equivalent type def in gsoap generated files is 'ArrayOfInt' )
	 */
	public static String getArrayOf(Type type)
	{
		String result = "";

		if( type instanceof TypeDefReference)
		{
			if (((TypeDefReference)type).getName().equals ("MindArrayOfInt"))
			{
				result = "ArrayOfInt";
			}else if (((TypeDefReference)type).getName().equals ("MindArrayOfShort"))
			{
				result = "ArrayOfShort";

			}else if (((TypeDefReference)type).getName().equals ("MindArrayOfFloat"))
			{
				result = "ArrayOfFloat";
			}else if (((TypeDefReference)type).getName().equals ("MindArrayOfChar"))
			{
				result = "ArrayOfChar";
			}else if (((TypeDefReference)type).getName().equals ("MindArrayOfLong"))
			{
				result = "ArrayOfLong";
			}else if (((TypeDefReference)type).getName().equals ("MindArrayOfString"))
			{
				result = "ArrayOfString";
			}
		} else if (type instanceof PointerOf)
		{
			result = "ArrayOfStruct" + ((StructReference) ((PointerOf)((PointerOf) type).getType()).getType()).getName();
		}


		return result;

	}

	/**
	 * Get the type contained by an array.
	 * @param value : The mind array.
	 * @return : The type value (for example MindArrayOfInt typedef should return 'int' ) 
	 */
	public static String getTypeOfArray(Type value)
	{
		String result = "";
		if( value instanceof TypeDefReference)
		{
			if (((TypeDefReference)value).getName().equals ("MindArrayOfInt"))
			{
				result = "int";
			}else if (((TypeDefReference)value).getName().equals ("MindArrayOfShort"))
			{
				result = "short";			
			}else if (((TypeDefReference)value).getName().equals ("MindArrayOfFloat"))
			{
				result = "float";
			}else if (((TypeDefReference)value).getName().equals ("MindArrayOfChar"))
			{
				result = "byte";
			}else if (((TypeDefReference)value).getName().equals ("MindArrayOfLong"))
			{
				result = "long";
			}else if (((TypeDefReference)value).getName().equals ("MindArrayOfString"))
			{
				result = "string";
			}
		}
		return result;

	}

	/**
	 * Process a deep search (look also into included idt) of a enum reference definition.
	 * @param idl The first IDL where start the search.
	 * @param enumRef The input enum reference.
	 * @param context The current mind compiler context.
	 * @return The enum definition (null if not found)
	 * @throws InvalidIDLException : Exception during IDl parsing.
	 */
	public static EnumDefinition findEnumDefinition(IDL idl,EnumReference enumRef, IDLLoader idlLoaderItf, Map<Object, Object> context) throws InvalidIDLException
	{

		Node [] typesNode = idl.astGetNodes("type");	
		EnumDefinition enumDef = null;
		for (Node n : typesNode)
		{
			if(n instanceof EnumDefinition)
			{
				enumDef = (EnumDefinition) n;
				if(enumDef.getName().equals(enumRef.getName()))
				{
					break;
				}
				enumDef = null;
			}
		}

		if( enumDef == null && idl instanceof InterfaceDefinition) //not found, so we search in include IDL
		{
			Node [] includeNodes = idl.astGetNodes("include");
			Include inc;
			IDL includeIdl;
			for(Node n : includeNodes)
			{
				inc = (Include) n;
				try {
					includeIdl = IDLASTHelper.getIncludedIDL(inc, idlLoaderItf, context);
					enumDef = findEnumDefinition(includeIdl, enumRef, idlLoaderItf, context);
					if(enumDef != null)
					{
						break;
					}				
				} catch (ADLException e) {
					throw new InvalidIDLException(e.getError());
				}
			}
		} 

		return enumDef;

	}


	/**
	 * Check if a given type is a string.
	 * @param t : The type to check.
	 * @return True if it is string type, false else.
	 */
	public static boolean isString(Type t)
	{
		return (t instanceof PrimitiveType 
				&& PrimitiveType.PrimitiveTypeEnum.fromIDLTypeName(((PrimitiveType)t).getName()) == PrimitiveType.PrimitiveTypeEnum.STRING);
	}

	/**
	 * Get the c type corresponding to the IDL type.
	 * Since wsdl2h prefix C type, it is possible to pass this prefix to match with wsdl2h prefixed types.
	 * @param type The IDL type.
	 * @param prefix The wsdl2h prefix. (null if no prefix)
	 * @return The corresponding C type.
	 */
	public static String getCType(Type type, String prefix)
	{
		String cType = "";
		String pfx = "";
		if(prefix != null)
		{
			pfx = prefix;
		}

		if(type instanceof PrimitiveType)
		{
			cType = PrimitiveType.PrimitiveTypeEnum.fromIDLTypeName(((PrimitiveType) type).getName()).getCType();

		} else if(type instanceof StructReference)
		{
			cType = "struct " + pfx + ((StructReference) type).getName(); 

		}else if (type instanceof StructDefinition)
		{
			cType = "struct " + pfx + ((StructDefinition) type).getName(); 

		} else if(type instanceof EnumReference)
		{
			cType = "enum " + pfx + ((EnumReference)type).getName();

		} else if(type instanceof EnumDefinition)
		{
			cType = "enum " + pfx + ((EnumDefinition)type).getName();

		} else if(type instanceof TypeDefReference)
		{
			cType = ((TypeDefReference)type).getName(); 

		} else if(type instanceof PointerOf)
		{
			cType = getCType(((PointerOf)type).getType(), prefix) + "*";
		}

		return cType;
	}

	public static void controlParameter(InterfaceDefinition itfDefinition,
			Parameter p, IDLLoader idlLoaderItf, Map<Object, Object> context) throws InvalidIDLException {

		if (Ext4DPWSIDLHelper.isInOutParameter(p)
				&& (p.getType() instanceof StructDefinition || p.getType() instanceof StructReference))
		{
			throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC, p,p.getName());
		}
		controlType(itfDefinition, p, p.getType(), idlLoaderItf, context);
	}

	public static boolean isArrayOfString(Type arrayType) {
		return arrayType instanceof TypeDefReference
		&& ((TypeDefReference)arrayType).getName().equals ("MindArrayOfString");
	}

}
