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

package org.ow2.mind.extensions.ext4dpws.idl2wsdl.xsd.xsd11;


/**
 * XSD.1.1 constants. 
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public interface XSD11Constants {
	
	/**
	 * <xsd:schema/> local part.
	 */
	String SCHEMA_LOCALNAME = "schema";
	
	/**
	 * <xsd:element/> local part.
	 */
	String ELEMENT_LOCALNAME = "element";
	/**
	 * <xsd:complexType/> local part.
	 */
	String COMPLEX_TYPE_LOCALPART = "complexType";
	
	/**
	 * <xsd:sequence/> local part.
	 */
	String SEQUENCE_LOCALPART = "sequence";
	
	/**
	 * <xsd:complexContent/> local part.
	 */
	String COMPLEX_CONTENT_LOCALPART = "complexContent";
	/**
	 * <xsd:restriction/> local part.
	 */
	String RESTRICTION_LOCALPART = "restriction";	
	/**
	 * <xsd:attribute/> local part.
	 */
	String ATTRIBUTE_LOCALPART = "attribute";
	/**
	 * <xsd:schema elementFormDefault="yyy"/> 'elementFormDefault' attribute name.
	 */
	String ELEMENT_FORM_DEFAULT_ATT = "elementFormDefault";
	
	/**
	 * 
	 */
	String MIN_OCCURS_ATTNAME = "minOccurs";
	
	/**
	 * 
	 */
	String MAX_OCCURS_ATTNAME = "maxOccurs";
	
	/**
	 * 
	 */
	String ELEMENT_ATT = ELEMENT_LOCALNAME;
	/**
	 * <xsd:schema elementFormDefault="qualified"/> 'elementFormDefault' attribute value.
	 */
	String QUALIFIED_ATT = "qualified";
	/**
	 * <xsd:xxx name="yyy"/> 'name' attribute name.
	 */
	String NAME_ATT = "name";	
	/**
	 * <xsd:xxx type="yyy"/> 'type' attribute name.
	 */
	String TYPE_ATT = "type";
	/**
	 * xsd:byte local part.
	 */
	String BYTE_TYPE_LOCALPART = "byte";	
	/**
	 * xsd:boolean local part.
	 */
	String BOOLEAN_TYPE_LOCALPART = "boolean";	
	/**
	 * xsd:short local part.
	 */
	String SHORT_TYPE_LOCALPART = "short";
	/**
	 * xsd:int local part.
	 */
	String INT_TYPE_LOCALPART = "int";
	/**
	 * xsd:long local part.
	 */
	String LONG_TYPE_LOCALPART = "long";
	/**
	 * xsd:double local part.
	 */
	String DOUBLE_TYPE_LOCALPART = "double";
	/**
	 * xsd:float local part.
	 */
	String FLOAT_TYPE_LOCALPART = "float";
	/**
	 * xsd:string local part.
	 */
	String STRING_TYPE_LOCALPART = "string";
	
	/**
	 * xsd:unsignedByte local part.
	 */
	String UBYTE_TYPE_LOCALPART = "unsignedByte";
	/**
	 * xsd:unsignedShort local part.
	 */
	String USHORT_TYPE_LOCALPART = "unsignedShort";
	/**
	 * xsd:unsignedInt local part.
	 */
	String UINT_TYPE_LOCALPART = "unsignedInt";
	/**
	 * xsd:unsignedLong local part.
	 */
	String ULONG_TYPE_LOCALPART = "unsignedLong";
	
	/**
	 * xsd:unit64_t local part.
	 */
	String UINT64_T_TYPE_LOCALPART = "uintsf";	

	/**
	 * xsd:unsignedByte local part.
	 */
	String SBYTE_TYPE_LOCALPART = "signedByte";
	/**
	 * xsd:unsignedShort local part.
	 */
	String SSHORT_TYPE_LOCALPART = "signedShort";
	/**
	 * xsd:unsignedInt local part.
	 */
	String SINT_TYPE_LOCALPART = "signedInt";
	/**
	 * xsd:unsignedLong local part.
	 */
	String SLONG_TYPE_LOCALPART = "signedLong";
}
