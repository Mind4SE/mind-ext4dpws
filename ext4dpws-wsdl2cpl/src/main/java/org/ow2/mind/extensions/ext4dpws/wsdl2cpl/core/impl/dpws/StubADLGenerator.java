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

package org.ow2.mind.extensions.ext4dpws.wsdl2cpl.core.impl.dpws;

import java.io.IOException;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.objectweb.fractal.adl.ADLException;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSIDLHelper;
import org.ow2.mind.extensions.ext4dpws.common.InvalidIDLException;
import org.ow2.mind.extensions.ext4dpws.common.StringTemplateHelper;
import org.ow2.mind.extensions.ext4dpws.idl2wsdl.IDL2WSDLConstants;
import org.ow2.mind.idl.IDLLoader;
import org.ow2.mind.idl.ast.EnumReference;
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
 * Load the 'org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/StubADL.st' file, 
 * set some attributes and return it.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public final class StubADLGenerator {


	private static Map<String, Object> currentDpwsFiles = null;

	private static Map <Object, Object> context;

	private static IDLLoader m_idlLoaderItf;

	private static InterfaceDefinition currentDefinition;

	/**
	 * Default constructor.
	 */
	private StubADLGenerator() { }


	public synchronized static StringTemplate generateADL(InterfaceDefinition itfDefinition, Map<String, Object> dpwsFiles, IDLLoader idlLoaderItf, Map<Object, Object> context) throws ADLException, IOException 
	{

		currentDpwsFiles = dpwsFiles;

		currentDefinition = itfDefinition;

		m_idlLoaderItf = idlLoaderItf;

		StringTemplate stubTemplate = StringTemplateHelper.instanceOfStubADL();
		String prefix = (String) currentDpwsFiles.get(STConstants.$SERVICE_NS_PREFIX$);	
		stubTemplate.setAttribute(STConstants.$SERVICE_NS_PREFIX$, prefix);
		stubTemplate.setAttribute(STConstants.$INTERFACE_DEFINITION$, itfDefinition.getName());
		StringBuffer meths = new StringBuffer();

		for (Method meth : itfDefinition.getMethods()) 
		{ 
			meths.append(buildMeth(meth));		
			meths.append("\n");
		}
		stubTemplate.setAttribute("METHS", meths.toString());

		return stubTemplate;
	}

	private static String buildMeth(Method meth) throws InvalidIDLException
	{

		StringBuffer result = new StringBuffer();
		StringBuffer paramList = new StringBuffer();
		StringTemplate methTemplate = StringTemplateHelper.instanceOfMeth();

		String cType = Ext4DPWSIDLHelper.getCType(meth.getType(), null);		

		methTemplate.setAttribute(STConstants.$RETURN_TYPE$, cType);
		methTemplate.setAttribute(STConstants.$METH_NAME$, meth.getName());


		for (Parameter p : meth.getParameters()) 
		{	
			if(Ext4DPWSIDLHelper.isString(p.getType()))
			{
				cType = "const char*";

			} else 
			{
				cType = Ext4DPWSIDLHelper.getCType(p.getType(), null);
			}


			if (Ext4DPWSIDLHelper.isInOutParameter(p)) 
			{
				cType += "*"; //pointer for in out parameter
				paramList.append(cType + " " + IDL2WSDLConstants.INOUT_PREFIX + p.getName() + ",");
			} else if (Ext4DPWSIDLHelper.isInParameter(p)) 
			{
				paramList.append(cType + " " + IDL2WSDLConstants.IN_PREFIX + p.getName() + ",");
			} else 
			{
				cType += "*"; //pointer for out parameter
				paramList.append(cType + " " + IDL2WSDLConstants.OUT_PREFIX + p.getName() + ",");
			}
		}

		methTemplate.setAttribute(STConstants.$METH_PARAMS$, paramList.toString());		
		buildBody(methTemplate, meth);
		String methLine = methTemplate.toString().replace(",)", ")");
		result.append(methLine);		
		return result.toString();
	}

	private static void buildBody(StringTemplate template,Method meth) throws InvalidIDLException
	{
		if (Ext4DPWSIDLHelper.haveToDefineStruct(meth)) 
		{
			buildBodyWithStructResponse(template, meth);
		} else 
		{
			buildBodyWithoutStructResponse(template, meth);
		}
	}

	private static void buildBodyWithStructResponse(StringTemplate template,Method meth) throws InvalidIDLException{


		String nsPrefix = (String) currentDpwsFiles.get(STConstants.$SERVICE_NS_PREFIX$);
		String structResponse =  nsPrefix + "__" + meth.getName() + "Response";

		StringBuffer paramList = new StringBuffer();
		StringBuffer structResponseFill = new StringBuffer();
		StringBuffer outStringCpyDecl = new StringBuffer();
		String inParam, outParam, inOutParam, tmp = "";
		Type type = null;
		StringBuffer structCopy = new StringBuffer();

		String structRespParam = structResponse + "Param";
		template.setAttribute(STConstants.$OUT_DATA_DECL$, "struct " + structResponse + " " + structRespParam + ";\n");
		template.setAttribute(STConstants.$PREFIX$, nsPrefix);

		for (Parameter p : meth.getParameters()) 
		{
			type = p.getType();
			inParam = IDL2WSDLConstants.IN_PREFIX + p.getName();				
			outParam = IDL2WSDLConstants.OUT_PREFIX + p.getName();
			inOutParam = IDL2WSDLConstants.INOUT_PREFIX + p.getName();

			if (Ext4DPWSIDLHelper.isInOutParameter(p)) //inout parameter
			{			
				if(type instanceof PrimitiveType)
				{

					if(Ext4DPWSIDLHelper.isString(type))
					{		
						StringTemplate temp = new StringTemplate(Constants.DCPL_STRDUP_TEMPLATE);
						temp.setAttribute("VAR",structRespParam + "." + outParam);
						tmp = "*" + inOutParam + " = " + temp.toString();
						paramList.append("(char*)*" + inOutParam + ",");

					}else {
						tmp = "*" + inOutParam + " = " + structRespParam + "." +outParam + ";\n";		
						paramList.append("*" + inOutParam + ",");
					}
					structResponseFill.append(tmp);

				}else if(type instanceof EnumReference)
				{
					paramList.append("(enum " + nsPrefix + "__" + ((EnumReference)type).getName() + ")*" + inOutParam + ",");
					tmp = "*" + inOutParam + " = " + structRespParam + "." +outParam + ";\n";			
					structResponseFill.append(tmp);
				}  
				else if(Ext4DPWSIDLHelper.isArrayOfPrimitive(type)) 
				{						
					paramList.append("(struct " + Ext4DPWSIDLHelper.getArrayOf((TypeDefReference) type) + "*)" + inOutParam + ",");
					tmp = arrayCopy(type,nsPrefix, inOutParam, structRespParam + "." + outParam);
					structResponseFill.append(tmp);

				} 

			} else if (Ext4DPWSIDLHelper.isInParameter(p)) //in parameter 
			{

				if(type instanceof StructDefinition)
				{
					inParam= "(struct " + nsPrefix + "__"  + ((StructDefinition) type).getName() + "*)&" + inParam;

				} else if(type instanceof StructReference)
				{
					inParam= "(struct " + nsPrefix + "__"  + ((StructReference) type).getName() + "*)&" + inParam;

				} else if(Ext4DPWSIDLHelper.isArrayOfPrimitive(type))
				{
					inParam= "(struct " + Ext4DPWSIDLHelper.getArrayOf(type) + "*)&" + inParam;

				} else if(Ext4DPWSIDLHelper.isString(type))
				{
					inParam = "(char *)" + inParam;
				} else if(Ext4DPWSIDLHelper.isArrayOfStruct(type))
				{
					tmp = inParam  + "_tmp";
					outStringCpyDecl.append("struct " + Ext4DPWSIDLHelper.getArrayOf(type) + " " + tmp +";\n");
					outStringCpyDecl.append(arrayCopy(type,nsPrefix, tmp, inParam));					
					inParam  =  "&" + tmp;
				}
				paramList.append(inParam + ",");

			} else //out parameter
			{
				if(Ext4DPWSIDLHelper.isString(type))
				{			
					StringTemplate temp = new StringTemplate(Constants.DCPL_STRDUP_TEMPLATE);
					temp.setAttribute("VAR",  structRespParam + "." + outParam);
					tmp = "*" + outParam + " = " + temp.toString();

				}else if(type instanceof PrimitiveType
						|| type instanceof EnumReference)
				{
					tmp = "*" + outParam + " = " + structRespParam + "." + outParam + ";";
				}
				else if(type instanceof TypeDefReference)
				{
					tmp = arrayCopy(type,nsPrefix, outParam, structRespParam + "." + outParam);
				} else if(type instanceof StructReference)
				{
					StructDefinition def = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, (StructReference) type, m_idlLoaderItf, context);
					structCopy(def,outParam + "->",structRespParam + "." + outParam,structResponseFill);

				} else if(type instanceof PointerOf)
				{
					StructReference structRef = ((StructReference) ((PointerOf)((PointerOf) type).getType()).getType());
					StructDefinition def = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, structRef,m_idlLoaderItf , context);
					StringTemplate temp  = new StringTemplate(Constants.MALLOC_TEMPLATE); 
					temp.setAttribute(STConstants.$VAR$, "*" + outParam);
					temp.setAttribute(STConstants.$TYPE$, "struct " + def.getName() + "*");
					temp.setAttribute(STConstants.$LENGTH$,structRespParam + "." + outParam + "->__size");
					tmp = temp.toString();
				}
				if(tmp != null) {
					structResponseFill.append(tmp);
				}
			}
		}

		paramList.append("&" + structRespParam);
		template.setAttribute(STConstants.$OUT_DATA_DECL$, outStringCpyDecl.toString());
		template.setAttribute(STConstants.$DPWS_PARAMS$, paramList.toString());
		template.setAttribute(STConstants.$OUT_DATA_COPY$, structResponseFill.toString());
		template.setAttribute(STConstants.$STRUCT_MEMCPY$, structCopy.toString());
	}



	private static void buildBodyWithoutStructResponse(StringTemplate template,Method meth) throws InvalidIDLException
	{

		StringBuffer paramList = new StringBuffer();
		StringBuffer outDataCopy = new StringBuffer();
		StringBuffer outStringCpyDecl = new StringBuffer();
		String nsPrefix = (String) currentDpwsFiles.get(STConstants.$SERVICE_NS_PREFIX$);
		String inParam, outParam, inOutParam,pfxStruct;
		Type type = null;
		StructDefinition structDef = null;

		template.setAttribute(STConstants.$PREFIX$, nsPrefix);

		for (Parameter p : meth.getParameters()) 
		{
			type = p.getType();		
			inParam = IDL2WSDLConstants.IN_PREFIX + p.getName();				
			outParam = IDL2WSDLConstants.OUT_PREFIX + p.getName();
			inOutParam = IDL2WSDLConstants.INOUT_PREFIX + p.getName();

			if (Ext4DPWSIDLHelper.isInOutParameter(p)) //inout parameter
			{	
				if(type instanceof PrimitiveType)
				{

					if(Ext4DPWSIDLHelper.isString(type))
					{			
						outStringCpyDecl.append("char *" + inOutParam +"_cpy;");				
						StringTemplate temp = new StringTemplate(Constants.DCPL_STRDUP_TEMPLATE);
						temp.setAttribute(STConstants.$VAR$,  inOutParam + "_cpy");	
						outDataCopy.append("*" + inOutParam + "= " + temp.toString());
						paramList.append("(char *)*" + inOutParam + ",");//for in
						paramList.append("&" + inOutParam + "_cpy,"); //for out

					}else 
					{
						paramList.append("*" + inOutParam + ",");//for in
						paramList.append(inOutParam+ ","); //for out
					}
				}


			} else if (Ext4DPWSIDLHelper.isInParameter(p)) //in parameter
			{
				if(type instanceof StructReference)
				{

					structDef = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition,(StructReference) type, m_idlLoaderItf, context);

					pfxStruct =  nsPrefix +"__"  + structDef.getName();	
					inParam= "(struct " + pfxStruct + "*)&" + inParam;

				} else if(Ext4DPWSIDLHelper.isArrayOfPrimitive(type))
				{
					pfxStruct = Ext4DPWSIDLHelper.getArrayOf((TypeDefReference) type);
					inParam= "(struct " + pfxStruct + "*)&" + inParam;
				} else if(Ext4DPWSIDLHelper.isString(type))
				{
					inParam = "(char *)" + inParam;
				}

				paramList.append(inParam + ",");

			} else  //out parameter
			{
				if(Ext4DPWSIDLHelper.isString(type))
				{			
					outStringCpyDecl.append("char *" + outParam +"_cpy;");
					paramList.append("&" + outParam +  "_cpy,");
					StringTemplate temp = new StringTemplate(Constants.DCPL_STRDUP_TEMPLATE);
					temp.setAttribute("VAR",outParam + "_cpy" );
					outDataCopy.append("*" + outParam + "=" + temp.toString());
				}else {
					paramList.append(outParam +  ",");
				}
			}
		}


		template.setAttribute(STConstants.$OUT_DATA_DECL$, outStringCpyDecl.toString());
		template.setAttribute(STConstants.$DPWS_PARAMS$, paramList.toString());
		template.setAttribute(STConstants.$OUT_DATA_COPY$, outDataCopy.toString());
	}


	private static void structCopy(StructDefinition from, String leftStruct, String to, StringBuffer structResponseFill) throws InvalidIDLException
	{
		for (Member m : from.getMembers())
		{
			if(m.getType() instanceof PrimitiveType)
			{
				if (Ext4DPWSIDLHelper.isString(m.getType()))
				{
					StringTemplate temp = new StringTemplate(Constants.DCPL_STRDUP_TEMPLATE);
					temp.setAttribute("VAR",to + "->" + m.getName());
					structResponseFill.append(leftStruct + m.getName() + "= " + temp.toString());		
				} else {
					structResponseFill.append(leftStruct + m.getName() + "= " + to + "->" + m.getName() + ";\n" );		
				}
			} else if(m.getType() instanceof PointerOf)
			{

				StructDefinition structDef = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, (StructReference) ((PointerOf)m.getType()).getType(),m_idlLoaderItf, context);
				StringTemplate temp = new StringTemplate(Constants.MALLOC_TEMPLATE);
				temp.setAttribute(STConstants.$VAR$, leftStruct + m.getName());
				temp.setAttribute(STConstants.$TYPE$, "struct " + structDef.getName());
				temp.setAttribute(STConstants.$LENGTH$, "1");

				structResponseFill.append(temp.toString());
				structCopy(structDef, leftStruct + m.getName() + "->", to + "->" + m.getName() , structResponseFill);
			}else if(m.getType() instanceof EnumReference)
			{
				structResponseFill.append(leftStruct + m.getName() + "= " + to + "->" + m.getName() + ";\n" );		

			}

		}
	}


	private static String arrayCopy(Type arrayType, String prefix, String arrayToName, String arrayFromName) throws InvalidIDLException
	{
		StringTemplate temp = null;
		String name ="";

		if(Ext4DPWSIDLHelper.isArrayOfString(arrayType))
		{
			temp = StringTemplateHelper.instanceOfStringArrayCopy();

		} else if (Ext4DPWSIDLHelper.isArrayOfStruct(arrayType))
		{
			temp = StringTemplateHelper.instanceOfStructArrayCopy();
			name = ((StructReference) ((PointerOf)((PointerOf) arrayType).getType()).getType()).getName();

		}else {
			temp = StringTemplateHelper.instanceOfArrayCopy();
		}

		temp.setAttribute("ARRAY_TO", arrayToName);
		temp.setAttribute("ARRAY_FROM", arrayFromName);
		temp.setAttribute(STConstants.$PREFIX$, prefix);
		temp.setAttribute(STConstants.$NAME$, name);
		temp.setAttribute(STConstants.$TYPE$, Ext4DPWSIDLHelper.getTypeOfArray(arrayType).replace("string","const char *"));			

		return temp.toString();

	}

	private static String deepArrayOfStructCopy(PointerOf arrayType, String prefix, String arrayToName, String arrayFromName) throws InvalidIDLException{

		//String MALLOC_TEMPLATE = "$VAR$ = ($TYPE$*)dcpl_malloc(sizeof($TYPE$)*$LENGTH$);\n";
		StructReference structRef = ((StructReference) ((PointerOf)((PointerOf) arrayType).getType()).getType());
		StructDefinition def = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, structRef,m_idlLoaderItf , context);
		StringBuffer result = new StringBuffer();
		if(def != null)
		{
			StringTemplate temp  = new StringTemplate(Constants.MALLOC_TEMPLATE); 
			temp.setAttribute(STConstants.$VAR$, arrayToName);

			temp.setAttribute(STConstants.$TYPE$, "struct " + def.getName());
			temp.setAttribute(STConstants.$LENGTH$, arrayFromName + ".__size");


			result.append(temp.toString());

			structCopy(def, arrayToName, arrayFromName, result);

		}

		return result.toString();
	}



}

