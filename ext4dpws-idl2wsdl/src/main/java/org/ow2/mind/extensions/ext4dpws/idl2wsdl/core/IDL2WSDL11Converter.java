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

package org.ow2.mind.extensions.ext4dpws.idl2wsdl.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.antlr.stringtemplate.StringTemplate;
import org.jdom.Attribute;
import org.jdom.Element;
import org.objectweb.fractal.adl.util.FractalADLLogManager;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSIDLErrors;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSIDLHelper;
import org.ow2.mind.extensions.ext4dpws.common.InvalidIDLException;
import org.ow2.mind.extensions.ext4dpws.common.StringTemplateHelper;
import org.ow2.mind.extensions.ext4dpws.idl2wsdl.IDL2WSDLConstants;
import org.ow2.mind.extensions.ext4dpws.idl2wsdl.xsd.xsd11.XSD11;
import org.ow2.mind.extensions.ext4dpws.idl2wsdl.xsd.xsd11.XSD11Constants;
import org.ow2.mind.idl.IDLLoader;
import org.ow2.mind.idl.ast.EnumDefinition;
import org.ow2.mind.idl.ast.EnumMember;
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
 * Comment: Convert IDL file into WSDL 1.1 Document.  
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public final class IDL2WSDL11Converter {


	private static InterfaceDefinition currentDefinition = null;

	private static final Map<String, Element> struct2xsdMapping = new HashMap<String, Element>();

	private static final Map<String, String> arrays2xsdMapping = new HashMap<String, String>();

	private static final Map<String, Element> enum2xsdMapping = new HashMap<String, Element>();

	private static Map<Object, Object> context;
	
	private static IDLLoader m_idlLoaderItf;

	/**
	 * Logger.
	 */
	private static Logger logger = FractalADLLogManager.getLogger("idl2wsdl");


	/**
	 * Constructor.
	 */
	private IDL2WSDL11Converter() 
	{

	}

	/**
	 * 
	 * @param idl : The interface definition to convert.
	 * @param outfile : The WSDL output file.
	 * @return The service Namespace URI associated to the generated WSDL.
	 * @throws InvalidIDLException : If provided interface definition is not valid.
	 * @throws IOException : Generation IO errors.
	 */
	public static synchronized Map<String, Object> idl2wsdl(InterfaceDefinition idl, File outfile, IDLLoader idlLoaderItf, Map<Object,Object> context) throws InvalidIDLException, IOException 
	{

		logger.info("Convert " + idl.getName() + " IDL to WSDL.");
		enum2xsdMapping.clear();
		struct2xsdMapping.clear();
		m_idlLoaderItf = idlLoaderItf;

		Method [] methods = idl.getMethods();
		IDL2WSDL11Converter.context = context;

		//checkpoint on method number
		if (methods.length == 0)
		{
			throw new InvalidIDLException(Ext4DPWSIDLErrors.INVALID_IDL_NO_OPERATION, idl);
		}

		Ext4DPWSIDLHelper.checkErrorsIDTInclude(idl);

		currentDefinition = idl;

		File parentDir = outfile.getParentFile();
		if (!parentDir.exists() && !parentDir.mkdirs())
		{
			throw new IOException("Cannot create " + parentDir.getAbsolutePath());
		}
		String serviceNamespaceURI = IDL2WSDLConstants.MIND_DISTRIBUTION_URI + idl.getName().replace('.', '/');

		StringTemplate wsdlDefinition = StringTemplateHelper.instanceOfWSDLDefinition();
		wsdlDefinition.setAttribute(IDL2WSDLConstants.PREFIX_ATTR, IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX);
		wsdlDefinition.setAttribute(IDL2WSDLConstants.SERVICE_NAMESPACE_URI_ATTR, serviceNamespaceURI);

		//build and add types
		String wsdlTypes = buildWSDLTypesElt(idl);

		wsdlDefinition.setAttribute(IDL2WSDLConstants.TYPES_ATTR, wsdlTypes);

		//build message
		for (Method m : methods) 
		{
			wsdlDefinition.setAttribute(IDL2WSDLConstants.MESSAGES_ATTR, buildMessage(m));
		}

		//build port type element
		wsdlDefinition.setAttribute(IDL2WSDLConstants.PORT_TYPES_ATTR, buildPortType(idl));
		//build soap binding element
		wsdlDefinition.setAttribute(IDL2WSDLConstants.BINDING_ATTR, buildBinding(idl));
		//build service element
		wsdlDefinition.setAttribute(IDL2WSDLConstants.SERVICE_ATTR, buildService(idl));
		FileWriter fr = new FileWriter(outfile);
		BufferedWriter bw = new BufferedWriter(fr);
		bw.write(wsdlDefinition.toString());
		bw.close();

		Map<String, Object> conversionResult = new HashMap<String, Object>();
		conversionResult.put("serviceNamespaceURI", serviceNamespaceURI);

		return conversionResult;
	}

	/**
	 * Build the WSDL types element.
	 * @param itfDefinition : The IDL definition.
	 * @return : The element representing the <wsdl:types> element.
	 * @throws InvalidIDLException 
	 */
	private static String buildWSDLTypesElt(InterfaceDefinition itfDefinition) throws InvalidIDLException 
	{
		//TODO build complex types
		String serviceNamespaceURI = IDL2WSDLConstants.MIND_DISTRIBUTION_URI + itfDefinition.getName().replace('.', '/');
		StringTemplate wsdlTypesTemplate = StringTemplateHelper.instanceOfWSDL11Types();

		wsdlTypesTemplate.setAttribute(IDL2WSDLConstants.SERVICE_NAMESPACE_URI_ATTR, serviceNamespaceURI);

		for (Method m : itfDefinition.getMethods()) 
		{
			for (Parameter p : m.getParameters()) 
			{
				addType(wsdlTypesTemplate, p.getType());
			}			

		}
		return wsdlTypesTemplate.toString();
	}


	private static void addType(StringTemplate wsdlTypesTemplate, Type t) throws InvalidIDLException 
	{

		if (t instanceof StructReference ) 
		{

			StructDefinition structDef  = null;
			if(t instanceof StructReference)
			{
				structDef = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, (StructReference) t, m_idlLoaderItf, context);
			} else if(t instanceof PointerOf) 
			{
				structDef = Ext4DPWSIDLHelper.getArrayOfStructDefinition(currentDefinition, t, m_idlLoaderItf, context);
			}
			ConvertIDLStructDefinition(structDef, wsdlTypesTemplate);

		} else if (t instanceof EnumDefinition 
				|| t instanceof EnumReference)
		{
			EnumDefinition enumDef  = null;
			if(t instanceof EnumDefinition)
			{
				enumDef = (EnumDefinition) t;
			} else 
			{
				enumDef = Ext4DPWSIDLHelper.findEnumDefinition(currentDefinition, (EnumReference) t, m_idlLoaderItf, context);
			}
			convertIDLEnumDefinition(enumDef, wsdlTypesTemplate);			
		} else if(t instanceof TypeDefReference) //at this step we are sure that it is a, ArrayOf (due to Ext4DPWSIDLHelper.controlType operation)
		{
			convertIDLBasicArray((TypeDefReference)t, wsdlTypesTemplate);
		} else if(t instanceof PointerOf) //array of struct
		{
			convertIDLArrayOfStruct((PointerOf) t, wsdlTypesTemplate);
			
		}
	}


	/**
	 * Build a WSDL message element based on the IDL method m.
	 * @param m : The IDL method.
	 * @return : The element representing the <wsdl:message> 
	 * @throws InvalidIDLException 
	 */
	private static String buildMessage(Method m) throws InvalidIDLException 
	{

		Ext4DPWSIDLHelper.controlReturnParameter(m);
		StringTemplate wsdlMessage = StringTemplateHelper.instanceOfWSDL11Message();
		String part = null;

		wsdlMessage.setAttribute(IDL2WSDLConstants.NAME_ATTR, m.getName());

		for (Parameter p : m.getParameters()) 
		{	
			/*if(!Ext4DPWSIDLHelper.isErrorParam(p)){*/

				Ext4DPWSIDLHelper.controlParameter(currentDefinition, p, m_idlLoaderItf, context);

				if (Ext4DPWSIDLHelper.isInOutParameter(p)) 
				{	
					part = buildPart(IDL2WSDLConstants.IN_PREFIX + p.getName(), p.getType());
					wsdlMessage.setAttribute(IDL2WSDLConstants.REQ_PART_ATTR, part);
					part = buildPart(IDL2WSDLConstants.OUT_PREFIX + p.getName(), p.getType());
					wsdlMessage.setAttribute(IDL2WSDLConstants.RESP_PART_ATTR, part);					
				} else if (Ext4DPWSIDLHelper.isInParameter(p)) 
				{
					part = buildPart(IDL2WSDLConstants.IN_PREFIX + p.getName(), p.getType());
					wsdlMessage.setAttribute(IDL2WSDLConstants.REQ_PART_ATTR, part);				
				} else if (Ext4DPWSIDLHelper.isOutParameter(p)) 
				{
					part = buildPart(IDL2WSDLConstants.OUT_PREFIX + p.getName(), p.getType());
					wsdlMessage.setAttribute(IDL2WSDLConstants.RESP_PART_ATTR, part);			
				}
			/*}*/
		}
		return "\n" + wsdlMessage.toString();

	}	


	/**
	 * Build a WSDL part element based on a method parameter name and his type.
	 * @param name : The parameter name.
	 * @param t : The parameter type.
	 * @return :The element representing the <wsdl:part> element.
	 * @throws InvalidIDLException 
	 */
	private static String buildPart(String name , Type t) throws InvalidIDLException 
	{
		StringTemplate wsdlPart = new StringTemplate(IDL2WSDLConstants.WSDL_TYPE_PART_TEMPLATE);
		QName xsdType = null;
		String prefix = "";
		String type = "";
		Element complexElt = null;

		if (t instanceof PrimitiveType)
		{
			xsdType = convertIDLPrimitive((PrimitiveType) t);			
			prefix = xsdType.getPrefix();
			type = xsdType.getLocalPart();	

		} else if (t instanceof StructReference)
		{
			complexElt = struct2xsdMapping.get(((StructReference)t).getName());	
		}
		else if(t instanceof StructDefinition)
		{	
			complexElt = struct2xsdMapping.get(((StructDefinition)t).getName());

		} else if(t instanceof EnumReference)
		{
			complexElt = enum2xsdMapping.get(((EnumReference)t).getName());

		} else if (t instanceof EnumDefinition)
		{
			complexElt = enum2xsdMapping.get(((EnumDefinition)t).getName());		

		} else if (t instanceof TypeDefReference)
		{
			type = Ext4DPWSIDLHelper.getArrayOf(t);
			prefix = IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX;	
		} else if(t instanceof PointerOf) //array of struct
		{
			type = Ext4DPWSIDLHelper.getArrayOf(t);//"ArrayOfStruct" + def.getName();
			prefix = IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX;	
			
		}

		if(complexElt != null)
		{
			type = complexElt.getAttributeValue("name");
			prefix = IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX;	
		}	

		wsdlPart.setAttribute(IDL2WSDLConstants.NAME_ATTR, name);
		wsdlPart.setAttribute(IDL2WSDLConstants.TYPE_ATTR, type);
		wsdlPart.setAttribute(IDL2WSDLConstants.PREFIX_ATTR, prefix);

		return wsdlPart.toString();
	}

	/**
	 * Build a WSDL PortType element.
	 * @param itfDefinition : The IDL interface definition.
	 * @return : The element representing the IDL interface in a <wsdl:portType> element.
	 */
	private static String buildPortType(InterfaceDefinition itfDefinition)
	{
		StringTemplate wsdlPortType = StringTemplateHelper.instanceOfWSDL11PortType();
		String itfname = itfDefinition.getName();
		String [] tmp = itfname.split("\\.");
		String portTypeName = (tmp.length == 0 ? itfname : tmp[tmp.length - 1]);

		wsdlPortType.setAttribute(IDL2WSDLConstants.NAME_ATTR, portTypeName);
		for (Method m : itfDefinition.getMethods()) 
		{
			wsdlPortType.setAttribute(IDL2WSDLConstants.OPERATION_ATTR, buildOperation(m));
		}

		return wsdlPortType.toString();
	}

	/**
	 * Build a WSDL operation element.
	 * @param m : The IDL operation.
	 * @return : The element representing the IDL method in a <wsdl:operation> element.
	 */
	private static String buildOperation(Method m)
	{
		StringTemplate wsdlPortTypeOp = StringTemplateHelper.instanceOfWSDL11Operation();
		wsdlPortTypeOp.setAttribute(IDL2WSDLConstants.NAME_ATTR, m.getName());
		wsdlPortTypeOp.setAttribute(IDL2WSDLConstants.INPUT_ATTR, buildOperationIn(m));
		wsdlPortTypeOp.setAttribute(IDL2WSDLConstants.OUTPUT_ATTR, buildOperationOut(m));
		return "\n" + wsdlPortTypeOp.toString();
	}



	/**
	 * Build the WSDL input part of the <wsdl:operation> based on the metho m. 
	 * @param m : The IDL operation.
	 * @return : The element representing the <wsdl:input>.
	 */
	private static String buildOperationIn(Method m)
	{
		StringTemplate wsdlPortTypeOpIn = StringTemplateHelper.instanceOfWSDL11OperationIn();
		wsdlPortTypeOpIn.setAttribute(IDL2WSDLConstants.PREFIX_ATTR, IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX);
		wsdlPortTypeOpIn.setAttribute(IDL2WSDLConstants.NAME_ATTR, m.getName());		
		return wsdlPortTypeOpIn.toString();
	}

	/**
	 * Build the WSDL output part of the <wsdl:operation> based on the method m. 
	 * @param m : The IDL operation.
	 * @return : The element representing the <wsdl:output>.
	 */
	private static String buildOperationOut(Method m)
	{
		StringTemplate wsdlPortTypeOpOut = StringTemplateHelper.instanceOfWSDL11OperationOut();
		wsdlPortTypeOpOut.setAttribute(IDL2WSDLConstants.PREFIX_ATTR, IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX);
		wsdlPortTypeOpOut.setAttribute(IDL2WSDLConstants.NAME_ATTR, m.getName());		
		return wsdlPortTypeOpOut.toString();
	}

	/**
	 * Build a WSDL binding element based on an interface definition.
	 * @param itfDefinition : The interface definition.
	 * @return : The element representing the <wsdl:binding> element.
	 */
	private static String buildBinding(InterfaceDefinition itfDefinition)
	{
		StringTemplate wsdlBinding  = null;
		String itfname = itfDefinition.getName();
		String [] tmp = itfname.split("\\.");
		String bindingName = (tmp.length == 0 ? itfname : tmp[tmp.length - 1]);

		wsdlBinding = StringTemplateHelper.instanceOfWSDL11RPCBinding();

		String serviceNamespaceURI = IDL2WSDLConstants.MIND_DISTRIBUTION_URI + itfDefinition.getName().replace('.', '/');
		wsdlBinding.setAttribute(IDL2WSDLConstants.NAME_ATTR, bindingName);
		wsdlBinding.setAttribute(IDL2WSDLConstants.PREFIX_ATTR, IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX);

		for (Method m : itfDefinition.getMethods()) 
		{
			wsdlBinding.setAttribute(IDL2WSDLConstants.BINDING_OPERATION_ATTR, buildBindingOp(serviceNamespaceURI, m));
		}

		return wsdlBinding.toString();
	}

	private static String buildBindingOp(String namespaceURI, Method m)
	{
		StringTemplate wsdlBindingOp = null;

		wsdlBindingOp = StringTemplateHelper.instanceOfWSDL11BindingOp();
		wsdlBindingOp.setAttribute(IDL2WSDLConstants.NAME_ATTR, m.getName());
		String soapAction = namespaceURI + "/" + m.getName();
		wsdlBindingOp.setAttribute(IDL2WSDLConstants.SOAP_ACTION, soapAction);
		wsdlBindingOp.setAttribute(IDL2WSDLConstants.INPUT_ATTR, buildBindingOpIn(namespaceURI, m));
		wsdlBindingOp.setAttribute(IDL2WSDLConstants.OUTPUT_ATTR, buildBindingOpOut(namespaceURI, m));


		return wsdlBindingOp.toString();
	}

	private static String buildService(InterfaceDefinition itfDefinition)
	{
		StringTemplate wsdlService = StringTemplateHelper.instanceOfWSDL11Service();
		String itfname = itfDefinition.getName();
		String [] tmp = itfname.split("\\.");
		String serviceName = (tmp.length == 0 ? itfname : tmp[tmp.length - 1]);
		wsdlService.setAttribute(IDL2WSDLConstants.NAME_ATTR, serviceName);
		wsdlService.setAttribute(IDL2WSDLConstants.PORT_ATTR, buildPort(itfDefinition));
		return wsdlService.toString();
	}

	private static String buildPort(InterfaceDefinition itfDefinition)
	{
		StringTemplate wsdlPort = StringTemplateHelper.instanceOfWSDL11Port();
		String itfname = itfDefinition.getName();
		String [] tmp = itfname.split("\\.");
		String portName = (tmp.length == 0 ? itfname : tmp[tmp.length - 1]);
		wsdlPort.setAttribute(IDL2WSDLConstants.NAME_ATTR, portName);
		wsdlPort.setAttribute(IDL2WSDLConstants.PREFIX_ATTR, IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX);		
		return wsdlPort.toString();
	}

	private static String buildBindingOpIn(String namespaceURI, Method m)
	{
		StringTemplate wsdlBindingOpIn = null;

		wsdlBindingOpIn = StringTemplateHelper.instanceOfWSDL11BindingEncOpIn();

		wsdlBindingOpIn.setAttribute(IDL2WSDLConstants.SERVICE_NAMESPACE_URI_ATTR, namespaceURI);
		return wsdlBindingOpIn.toString();
	}

	private static String buildBindingOpOut(String namespaceURI, Method m)
	{
		StringTemplate wsdlBindingOpOut = null;

		wsdlBindingOpOut = StringTemplateHelper.instanceOfWSDL11BindingEncOpOut();

		wsdlBindingOpOut.setAttribute(IDL2WSDLConstants.SERVICE_NAMESPACE_URI_ATTR, namespaceURI);
		return wsdlBindingOpOut.toString();
	}

	/**
	 * Convert a struct definition into a jdom Element.
	 * @param structDef : The struct definition to convert.
	 * @return : The created jdom element.
	 * @throws InvalidIDLException 
	 */
	private static Element ConvertIDLStructDefinition(StructDefinition structDef, StringTemplate wsdlTypesTemplate) throws InvalidIDLException {


		Element resultElt = struct2xsdMapping.get(structDef.getName());
		Element eltElt,tmp = null;
		Type type = null;
		QName primitiveQname = null;
		StructDefinition structDefTmp = null;
		String name = null;

		if (resultElt == null) //if not yet generated
		{
			resultElt = new Element("complexType", XSD11.namespace);
			resultElt.setAttribute("name", structDef.getName());
			Element sequenceElt = new Element("sequence", XSD11.namespace);
			Member [] members = structDef.getMembers();


			for (Member m : members)
			{
				type = m.getType();

				if(type instanceof PrimitiveType)
				{

					primitiveQname = convertIDLPrimitive((PrimitiveType) type); //at this step we are sure that type is a primitive type		
					eltElt = new Element("element", XSD11.namespace);
					eltElt.setAttribute(new Attribute("name", m.getName()));
					eltElt.setAttribute(new Attribute("type", primitiveQname.getPrefix() + ":" + primitiveQname.getLocalPart()));
					sequenceElt.addContent(eltElt);

				} else if(type instanceof PointerOf)
				{
					type = ((PointerOf) type).getType(); 

					if(type instanceof StructReference)
					{					
						structDefTmp = Ext4DPWSIDLHelper.findStructDefinition(currentDefinition, (StructReference)type, m_idlLoaderItf, context);
						if(structDefTmp != null)
						{
							tmp = ConvertIDLStructDefinition(structDefTmp, wsdlTypesTemplate);
							eltElt = new Element("element", XSD11.namespace);
							eltElt.setAttribute(new Attribute("name", m.getName()));
							eltElt.setAttribute(new Attribute("type", IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX + ":" + tmp.getAttributeValue("name")));
							sequenceElt.addContent(eltElt);
						}
					} else if (type instanceof TypeDefReference) //it is a array of
					{
						name = convertIDLBasicArray((TypeDefReference)type, wsdlTypesTemplate);
						eltElt = new Element("element", XSD11.namespace);
						eltElt.setAttribute(new Attribute("name", m.getName()));
						eltElt.setAttribute(new Attribute("type", IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX + ":" + name));
						sequenceElt.addContent(eltElt);
					}

				} else if(type instanceof EnumReference)
				{
					EnumDefinition enumDef = Ext4DPWSIDLHelper.findEnumDefinition(currentDefinition, (EnumReference) type, m_idlLoaderItf, context);
					convertIDLEnumDefinition (enumDef, wsdlTypesTemplate);
					eltElt = new Element("element", XSD11.namespace);
					eltElt.setAttribute(new Attribute("name", m.getName()));
					eltElt.setAttribute(new Attribute("type", IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX + ":" + enumDef.getName()));
					sequenceElt.addContent(eltElt);
				}


			}		
			resultElt.addContent(sequenceElt);
			struct2xsdMapping.put(structDef.getName(), resultElt);
			wsdlTypesTemplate.setAttribute(IDL2WSDLConstants.TYPES_ATTR, "\n" + xsdStructDefToString(resultElt));

		}

		return resultElt;

	}



	private static String convertIDLBasicArray(TypeDefReference t, StringTemplate wsdlTypesTemplate) {

		String name = Ext4DPWSIDLHelper.getArrayOf(t);	
		String resultElt = arrays2xsdMapping.get(name);

		if(resultElt == null)
		{
			StringTemplate temp = StringTemplateHelper.instanceOfWSDL11ArrayOf();
			temp.setAttribute(IDL2WSDLConstants.TYPE_ATTR,Ext4DPWSIDLHelper.getTypeOfArray(t));
			temp.setAttribute(IDL2WSDLConstants.NAME_ATTR,name);
			temp.setAttribute(IDL2WSDLConstants.PREFIX_ATTR,XSD11.prefix);

			arrays2xsdMapping.put(name, temp.toString());
			wsdlTypesTemplate.setAttribute(IDL2WSDLConstants.TYPES_ATTR, "\n" + temp.toString());
		}

		return name;

	}
	
	private static String convertIDLArrayOfStruct(PointerOf t, StringTemplate wsdlTypesTemplate) throws InvalidIDLException {

		String name = Ext4DPWSIDLHelper.getArrayOf(t);
		String resultElt = arrays2xsdMapping.get(name);

		if(resultElt == null)
		{
			StringTemplate temp = StringTemplateHelper.instanceOfWSDL11ArrayOf();
			StructDefinition def = Ext4DPWSIDLHelper.getArrayOfStructDefinition(currentDefinition, t, m_idlLoaderItf, context);
			temp.setAttribute(IDL2WSDLConstants.TYPE_ATTR,def.getName());
			temp.setAttribute(IDL2WSDLConstants.NAME_ATTR,name);
			temp.setAttribute(IDL2WSDLConstants.PREFIX_ATTR,IDL2WSDLConstants.DEFAULT_SERVICE_PREFIX);
			arrays2xsdMapping.put(name, temp.toString());
			wsdlTypesTemplate.setAttribute(IDL2WSDLConstants.TYPES_ATTR, "\n" + temp.toString());
		}

		return name;


	}

	private static Element convertIDLEnumDefinition(EnumDefinition enumDef,
			StringTemplate wsdlTypesTemplate) {

		Element resultElt = enum2xsdMapping.get(enumDef.getName());
		Element enumerationElt = null;	
		if (resultElt == null) //if not yet generated
		{
			resultElt = new Element("simpleType", XSD11.namespace);
			resultElt.setAttribute("name", enumDef.getName());
			Element restrictionElt = new Element("restriction", XSD11.namespace);
			restrictionElt.setAttribute("base",XSD11.namespace.getPrefix() + ":token");
			EnumMember [] members = enumDef.getEnumMembers();
			for (EnumMember m : members)
			{
				enumerationElt = new Element("enumeration",XSD11.namespace);
				enumerationElt.setAttribute("value",m.getName());
				restrictionElt.addContent(enumerationElt);
			}
			resultElt.addContent(restrictionElt);
			enum2xsdMapping.put(enumDef.getName(), resultElt);
			wsdlTypesTemplate.setAttribute(IDL2WSDLConstants.TYPES_ATTR, "\n" + xsdEnumDefToString(resultElt));
		}

		return resultElt;

	}

	public static QName convertIDLPrimitive(PrimitiveType primitiveType) {

		QName result = null;

		String name = primitiveType.getName();

		switch (PrimitiveType.PrimitiveTypeEnum.fromIDLTypeName(name)) {
		case CHAR:
			result = XSD11.xsd_byte;
			break;
		case BOOLEAN:
			result = XSD11.xsd_boolean;
			break;
		case INT:
			result = XSD11.xsd_int;
			break;			
		case SHORT:
			result = XSD11.xsd_short;
			break;
		case LONG :		
			result = XSD11.xsd_long;
			break;
		case FLOAT :
			result = XSD11.xsd_float;
			break;
		case DOUBLE:
			result = XSD11.xsd_double;
			break;
		case STRING:
			result = XSD11.xsd_string;
			break;
		case INT8_T:
			result = XSD11.xsd_sByte;
			break;
		case UINT8_T:
			result = XSD11.xsd_uByte;
			break;
		case INT16_T:
			result = XSD11.xsd_sShort;
			break;
		case UINT16_T:
			result = XSD11.xsd_uShort;
			break;
		case INT32_T:
			result = XSD11.xsd_sInt;
			break;
		case UINT32_T:
			result = XSD11.xsd_uInt;
			break;
		case INT64_T:
			result = XSD11.xsd_sLong;
			break;
		case UINT64_T:
			result = XSD11.xsd_uint64_t;
			break;			
		default:
			break;
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private static String xsdStructDefToString(Element element)
	{
		StringTemplate template = StringTemplateHelper.instanceOfStructComplexType();
		StringTemplate xsdEltTemplate = null;
		template.setAttribute("NAME", element.getAttributeValue("name"));
		Element current = null;
		List children = element.getChild("sequence", XSD11.namespace).getChildren();
		Iterator<Element> it = children.iterator();
		while (it.hasNext())
		{
			current = it.next();
			xsdEltTemplate = new StringTemplate(IDL2WSDLConstants.XSD_ELEMENT_TEMPLATE);
			xsdEltTemplate.setAttribute("NAME", current.getAttributeValue("name"));			
			xsdEltTemplate.setAttribute("TYPE", current.getAttributeValue("type"));
			template.setAttribute("ELEMENT", xsdEltTemplate.toString());
		}

		return template.toString();
	}

	@SuppressWarnings("unchecked")
	private static String xsdEnumDefToString(Element element)
	{
		StringTemplate template = StringTemplateHelper.instanceOfXsdEnumDef();
		StringTemplate xsdEltTemplate = null;
		template.setAttribute("NAME", element.getAttributeValue("name"));
		Element current = null;
		List children = element.getChild("restriction", XSD11.namespace).getChildren();
		Iterator<Element> it = children.iterator();
		while (it.hasNext())
		{
			current = it.next();
			xsdEltTemplate = new StringTemplate(IDL2WSDLConstants.XSD_ENUMERATION_TEMPLATE);
			xsdEltTemplate.setAttribute("VALUE", current.getAttributeValue("value"));		
			xsdEltTemplate.setAttribute("PREFIX", XSD11.namespace.getPrefix());	
			template.setAttribute("XSD_ENUMERATION", xsdEltTemplate.toString());
		}

		return template.toString();
	}

}
