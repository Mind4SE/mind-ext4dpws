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

package org.ow2.mind.extensions.ext4dpws.idl2wsdl;

public interface IDL2WSDLConstants {

	String SERVICE_NAMESPACE_URI_ATTR = "SERVICE_NAMESPACE_URI";
	
	String TYPE_ATTR = "TYPE";
	
	String NAME_ATTR = "NAME";
	
	String MESSAGES_ATTR = "MESSAGES";
	
	String PORT_TYPES_ATTR = "PORT_TYPES";
	
	String BINDING_ATTR = "BINDING";
		
	String SERVICE_ATTR = "SERVICE";
	
	String REQ_PART_ATTR = "REQ_PART";
	
	String RESP_PART_ATTR = "RESP_PART";
	
	String TYPES_ATTR = "TYPES";

	String OPERATION_ATTR = "OPERATION";

	String PREFIX_ATTR = "PREFIX";

	String INPUT_ATTR = "INPUT";

	String OUTPUT_ATTR = "OUTPUT";
	
	String PORT_ATTR = "PORT";
	
	String BINDING_OPERATION_ATTR = "BINDING_OPERATION";

	String MIND_DISTRIBUTION_URI = "http://org.ow2.mind/distribution/";
	
	String DEFAULT_SERVICE_PREFIX = "tns";

	String SOAP_ACTION = "SOAP_ACTION";

	String RETURN_NAME = "RETURN";

	String OUT_PREFIX = "out";

	String IN_PREFIX = "in";	

	String INOUT_PREFIX = "inout";	

	String WSDL_TYPE_PART_TEMPLATE = "\n<wsdl:part name=\"$NAME$\" type=\"$PREFIX$:$TYPE$\" />";

	String XSD_ELEMENT_TEMPLATE = "\n<xsd:element name=\"$NAME$\" type=\"$TYPE$\"/>";

	String XSD_ENUMERATION_TEMPLATE = "\n<$PREFIX$:enumeration value=\"$VALUE$\"/>";

}
