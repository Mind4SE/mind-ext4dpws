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
 * Load the 'org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/SkeletonADL.st' file, 
 * set some attributes and return it.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public final class SkeletonADLGenerator {


	private static Map<String, Object> currentDpwsFiles = null;

	private static final byte WITH_NO_STRUCT = 0;

	private static final byte WITH_STRUCT = 1;

	private static InterfaceDefinition currentDefinition;

	private static Map <Object, Object> context;	
	
	private static IDLLoader m_idlLoaderItf;


	/**
	 * Default constructor.
	 */
	private SkeletonADLGenerator() {

	}


	/**
	 * 
	 * @param itfDefinition : The server interface definition that the skeleton should map.
	 * @return : The skeleton template.
	 * @throws IOException  : Error of file generation.
	 * @throws InvalidIDLException 
	 */
	public  synchronized static StringTemplate generateADL(InterfaceDefinition itfDefinition, Map<String, Object> dpwsFiles, IDLLoader idlLoaderItf, Map <Object, Object> context) throws IOException, InvalidIDLException
	{

		currentDpwsFiles = dpwsFiles;

		currentDefinition = itfDefinition;
		
		m_idlLoaderItf = idlLoaderItf;

		SkeletonADLGenerator.context = context;

		StringTemplate skeletonTemplate = StringTemplateHelper.instanceOfSkeletonADL();

		StringBuffer calls = new StringBuffer();
		String prefix = (String) currentDpwsFiles.get(STConstants.$SERVICE_NS_PREFIX$);	
		skeletonTemplate.setAttribute(STConstants.$SERVICE_NS_PREFIX$, prefix);


		for (Method meth : itfDefinition.getMethods()) 
		{ 
			calls.append(buildProto(meth));		
			calls.append("\n");
		}	

		prefix = itfDefinition.getName().replace('.', '_');
		skeletonTemplate.setAttribute(STConstants.$CALLS$, calls.toString());
		skeletonTemplate.setAttribute(STConstants.$SERVICE_NS$, IDL2WSDLConstants.MIND_DISTRIBUTION_URI + prefix);
		skeletonTemplate.setAttribute(STConstants.$INTERFACE_DEFINITION$, itfDefinition.getName());


		return skeletonTemplate;
	}

	private static String buildProto(Method meth) throws InvalidIDLException{

		StringTemplate template  = null;
		Type type = null;
		String cType = null;
		StringBuffer dpwsParamsList = new StringBuffer();
		Parameter [] params = meth.getParameters();
		byte bodyType = Ext4DPWSIDLHelper.haveToDefineStruct(meth) ? WITH_STRUCT : WITH_NO_STRUCT;
		String ns = (String) currentDpwsFiles.get(STConstants.$SERVICE_NS_PREFIX$);

		switch (bodyType) 
		{
		case WITH_STRUCT:
			template = StringTemplateHelper.instanceOfCallWithStructResp();
			for (Parameter p : params) 
			{
				type = p.getType(); 
				cType = Ext4DPWSIDLHelper.getCType(type, ns + "__");
				if(type instanceof StructReference)
				{
					cType += "*"; 
				} else if(type instanceof TypeDefReference)
				{
					if(Ext4DPWSIDLHelper.isArrayOfPrimitive((TypeDefReference) type))
					{
						cType = "struct " + Ext4DPWSIDLHelper.getArrayOf((TypeDefReference) type) + "*";
					}
				} else if (Ext4DPWSIDLHelper.isArrayOfStruct(type))
				{
					cType = "struct " + Ext4DPWSIDLHelper.getArrayOf(type) + "*";					
				}
				if (Ext4DPWSIDLHelper.isInParameter(p)) 
				{
					dpwsParamsList.append("," + cType + " " + IDL2WSDLConstants.IN_PREFIX  + p.getName());
				} 
			}
			buildBodyWithStructResponse(template, meth);
			break;
		case WITH_NO_STRUCT :

			template = StringTemplateHelper.instanceOfCallWithNoStructResp();

			for (Parameter p : params) 
			{
				type = p.getType(); 

				cType = Ext4DPWSIDLHelper.getCType(type, ns + "__");

				if(type instanceof StructReference )
				{
					cType += "*"; 
				} else if(type instanceof TypeDefReference)
				{
					if(Ext4DPWSIDLHelper.isArrayOfPrimitive((TypeDefReference) type))
					{
						cType = "struct " + Ext4DPWSIDLHelper.getArrayOf((TypeDefReference) type) + "*";
					}
				}

				if (Ext4DPWSIDLHelper.isInOutParameter(p)) 
				{
					dpwsParamsList.append("," + cType + " " + IDL2WSDLConstants.IN_PREFIX + p.getName());
					dpwsParamsList.append("," + cType + " *" + IDL2WSDLConstants.OUT_PREFIX + p.getName());
				} else if (Ext4DPWSIDLHelper.isInParameter(p)) 
				{
					dpwsParamsList.append("," + cType + " " + IDL2WSDLConstants.IN_PREFIX  + p.getName());					
				} else if (Ext4DPWSIDLHelper.isOutParameter(p)) 
				{
					dpwsParamsList.append("," + cType + " *" + IDL2WSDLConstants.OUT_PREFIX + p.getName());			
				}
			}
			buildBodyWithoutStructResponse(template, meth);
			break;
		default:
			break;
		}

		template.setAttribute(STConstants.$PREFIX$, ns);		
		template.setAttribute(STConstants.$METH_NAME$, meth.getName());
		template.setAttribute(STConstants.$DPWS_PARAMS$, dpwsParamsList.toString());

		return template.toString();

	}


	private static void buildBodyWithoutStructResponse(StringTemplate template, Method meth) throws InvalidIDLException{

		String inParam,outParam = null;
		Parameter [] params = meth.getParameters();
		StringBuffer callParams = new StringBuffer();
		template.setAttribute(STConstants.$ITF_NAME$, STConstants.DEPL_UNIT_ITF_NAME);
		StringBuffer structCopy = new StringBuffer();
		StructDefinition structDef = null;
		Type type;
		String structVar,memcpy;
		for (Parameter p : params) 
		{
			inParam = IDL2WSDLConstants.IN_PREFIX + p.getName();
			outParam = IDL2WSDLConstants.OUT_PREFIX + p.getName();
			type = p.getType();

			if (Ext4DPWSIDLHelper.isInOutParameter(p)) //inout parameter
			{
				if(Ext4DPWSIDLHelper.isString(type))
				{
					callParams.append("(const char**)&" + inParam + ",");					
				} else 
				{
					callParams.append("&" + inParam + ",");
				}
				template.setAttribute(STConstants.$SET_OUT_PARAM$, "*" + outParam + "= " + inParam + ";");

			} else if (Ext4DPWSIDLHelper.isInParameter(p)) //in parameter
			{
				if(type instanceof StructReference)
				{

					structDef = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, (StructReference) type, m_idlLoaderItf, context);

					structVar = "struct "+ structDef.getName() + " " + inParam + "_s;\n";
					memcpy = "memcpy(&"+inParam+"_s,"+inParam+",sizeof(*"+inParam+"));\n";
					structCopy.append(structVar);
					structCopy.append(memcpy);
					inParam += "_s";
				} else if(Ext4DPWSIDLHelper.isString(type))
				{
					inParam = "(char*)" + inParam;
				}
				callParams.append(inParam + ",");

			} else if (Ext4DPWSIDLHelper.isOutParameter(p)) //out parameter
			{
				if(Ext4DPWSIDLHelper.isString(type))
				{
					outParam = "(const char**)" + outParam;
				} 
				callParams.append(outParam + ",");

			}		
		}
		int l = callParams.length();
		String paramsStr = (l > 0 ? callParams.substring(0, l - 1) : "");
		template.setAttribute(STConstants.$CALL_PARAMS$, paramsStr);
		template.setAttribute(STConstants.$STRUCT_MEMCPY$, structCopy.toString());
	}

	private static void buildBodyWithStructResponse(StringTemplate template, Method meth) throws InvalidIDLException{

		String inParam,outParam = null;
		Parameter [] params = meth.getParameters();
		StringBuffer callParams = new StringBuffer();
		template.setAttribute(STConstants.$ITF_NAME$, STConstants.DEPL_UNIT_ITF_NAME);
		StringBuffer structCopy = new StringBuffer();
		Type type;
		String ns = (String) currentDpwsFiles.get(STConstants.$SERVICE_NS_PREFIX$) + "__";
		String responseParam = ns + meth.getName() + "ResponseParam";
		StringBuffer structFill = new StringBuffer();
		String tmp;
		for (Parameter p : params) 
		{
			inParam = IDL2WSDLConstants.IN_PREFIX + p.getName();
			outParam = IDL2WSDLConstants.OUT_PREFIX + p.getName();
			type = p.getType();

			if (Ext4DPWSIDLHelper.isInOutParameter(p)) //inout parameter
			{	
				if(type instanceof TypeDefReference) 
				{					
					callParams.append("(" + ((TypeDefReference)type).getName()  + "*)" + inParam + ",");

				} else if(type instanceof EnumReference)
				{
					callParams.append("(enum " + ((EnumReference)type).getName()  + "*)&" + inParam + ",");
				}else if(Ext4DPWSIDLHelper.isString(type))
				{
					callParams.append("(const char**)&" + inParam + ",");
				}else 
				{
					callParams.append("&" + inParam + ",");
				}
				structFill .append(responseParam + "->" + outParam + "=" + inParam + ";\n");


			} else if (Ext4DPWSIDLHelper.isInParameter(p)) //in param
			{	
				if(type instanceof StructReference)
				{	inParam = "*(struct " + ((StructReference)type).getName() + "*)" + inParam;

				}else if(type instanceof TypeDefReference)					
				{	inParam ="*(" + ((TypeDefReference)type).getName() + "*)" + inParam;			

				}else if(Ext4DPWSIDLHelper.isArrayOfStruct(type))
				{

					tmp = inParam + "__tmp";
					structCopy.append(Ext4DPWSIDLHelper.getCType(type, null) + " " +tmp + ";\n");
					structCopy.append(arrayCopy(type, tmp, inParam) + "\n");
					inParam = tmp;			
				}
				callParams.append(inParam + ",");

			} else if (Ext4DPWSIDLHelper.isOutParameter(p)) //out param
			{
				if(Ext4DPWSIDLHelper.isString(type))
				{
					callParams.append("(const char**)&" + responseParam + "->" + outParam + ",");
				} else if(type instanceof PrimitiveType)
				{
					callParams.append("&" + responseParam + "->" + outParam + ",");
				}else if(type instanceof TypeDefReference) 
				{
					StringTemplate temp = new StringTemplate(Constants.DC_MSG_MALLOC_TEMPLATE);
					temp.setAttribute("VAR", responseParam + "->" + outParam);
					temp.setAttribute("TYPE", "struct " + Ext4DPWSIDLHelper.getArrayOf(type));
					structCopy.append(temp.toString());
					callParams.append("("+ ((TypeDefReference)type).getName() + "*)" + responseParam + "->" + outParam + ",");

				} else if(type instanceof EnumReference)
				{
					callParams.append("(enum "+ ((EnumReference)type).getName() + "*)&" + responseParam + "->" + outParam + ",");

				}else if(type instanceof StructReference)					
				{
					StructDefinition def = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, (StructReference) type, m_idlLoaderItf, context);
					tmp = "struct " + def.getName() + " " + outParam + ";";
					structCopy.append(tmp);
					callParams.append("&" + outParam + ",");

					StringTemplate temp = new StringTemplate(Constants.DC_MSG_MALLOC_TEMPLATE);
					temp.setAttribute(STConstants.$VAR$, responseParam + "->" + outParam);
					temp.setAttribute(STConstants.$TYPE$, "struct " + ns + def.getName());
					structFill.append(temp.toString());

					structCopy(def,ns, responseParam + "->" + outParam + "->",outParam + "." ,structFill);
				} else if(type instanceof PointerOf){
					
					Type t = ((PointerOf)((PointerOf)type).getType()).getType();
					//StructDefinition def = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, (StructReference) t, m_idlLoaderItf, context);

					/*StringTemplate temp = new StringTemplate(Constants.DC_MSG_MALLOC_TEMPLATE);
					temp.setAttribute("VAR", responseParam + "->" + outParam);
					temp.setAttribute("TYPE", "struct " + Ext4DPWSIDLHelper.getArrayOf(type));
					structCopy.append(temp.toString());*/
					callParams.append("(struct "+ ((StructReference)t).getName() + "***)&" + responseParam + "->" + outParam + "->__ptr,");

				}
			}		
		}

		template.setAttribute(STConstants.$OUT_DATA_COPY$, structFill.toString());
		int l = callParams.length();
		String paramsStr = (l > 0 ? callParams.substring(0, l - 1) : "");
		template.setAttribute(STConstants.$CALL_PARAMS$, paramsStr);
		template.setAttribute(STConstants.$STRUCT_MEMCPY$, structCopy.toString());
	}

	private static String arrayCopy(Type arrayType, String arrayToName, String arrayFromName) throws InvalidIDLException
	{
		StringTemplate temp = null;
		String name ="";

		temp = StringTemplateHelper.instanceOfSkeletonStructArrayCopy();
		name = ((StructReference) ((PointerOf)((PointerOf) arrayType).getType()).getType()).getName();			

		temp.setAttribute("ARRAY_TO", arrayToName);
		temp.setAttribute("ARRAY_FROM", arrayFromName);
		temp.setAttribute(STConstants.$NAME$, name);
		//temp.setAttribute(STConstants.$TYPE$, Ext4DPWSIDLHelper.getCType(arrayType, null));			

		return temp.toString();

	}


	private static void structCopy(StructDefinition from, String ns, String leftMember, String rightMember, StringBuffer structResponseFill) throws InvalidIDLException
	{
		String rigth_m;
		StringTemplate temp;

		for (Member m : from.getMembers())
		{
			if(m.getType() instanceof PrimitiveType)
			{
				rigth_m = rightMember + m.getName();
				if (Ext4DPWSIDLHelper.isString(m.getType()))
				{
					temp = new StringTemplate(Constants.DCPL_STRDUP_TEMPLATE);
					temp.setAttribute(STConstants.$VAR$,rigth_m);
					structResponseFill.append(leftMember + m.getName() + "= " + temp.toString());		
				} else {
					structResponseFill.append(leftMember + m.getName() + "= " + rigth_m  + ";\n" );		
				}
			} else if(m.getType() instanceof PointerOf)
			{

				rigth_m = rightMember + m.getName() + "->";
				StructDefinition structDef = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, (StructReference) ((PointerOf)m.getType()).getType(), m_idlLoaderItf, context);

				temp = new StringTemplate(Constants.DC_MSG_MALLOC_TEMPLATE);
				temp.setAttribute(STConstants.$VAR$, leftMember + m.getName());
				temp.setAttribute(STConstants.$TYPE$, "struct " + ns + structDef.getName());
				structResponseFill.append(temp.toString());

				structCopy(structDef, ns, leftMember + m.getName() + "->", rigth_m , structResponseFill);
			}  else if(m.getType() instanceof EnumReference)
			{
				rigth_m = rightMember + m.getName();
				structResponseFill.append(leftMember + m.getName() + "= " + rigth_m  + ";\n" );		
			}
		}
	}


}

