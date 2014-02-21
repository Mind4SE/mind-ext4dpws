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

import javax.xml.namespace.QName;

import org.jdom.Namespace;

/**
 * XSD 1.1.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public final class XSD11 {

	/**
	 * Default constructor.
	 */
	private XSD11()
	{

	}

	/**
	 * XSD 1.1 namespace uri.
	 */
	public static final String namespaceURI = "http://www.w3.org/2001/XMLSchema";

	/**
	 * XSD 1.1 prefix.
	 */
	public static final String prefix = "xsd";

	/**
	 * The XSD 1.1 Namespace.
	 */
	public static final Namespace namespace = Namespace.getNamespace(prefix, namespaceURI);


	/**
	 * http://www.w3.org/2001/XMLSchema:byte type.
	 */
	public static final QName xsd_byte = new QName(namespaceURI, XSD11Constants.BYTE_TYPE_LOCALPART, prefix);	
	/**
	 * http://www.w3.org/2001/XMLSchema:boolean type.
	 */	
	public static final QName xsd_boolean = new QName(namespaceURI, XSD11Constants.BOOLEAN_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:short type.
	 */
	public static final QName xsd_short = new QName(namespaceURI, XSD11Constants.SHORT_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:int type.
	 */
	public static final QName xsd_int = new QName(namespaceURI, XSD11Constants.INT_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:long type.
	 */
	public static final QName xsd_long = new QName(namespaceURI, XSD11Constants.LONG_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:float type.
	 */
	public static final QName xsd_float = new QName(namespaceURI, XSD11Constants.FLOAT_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:double type.
	 */
	public static final QName xsd_double = new QName(namespaceURI, XSD11Constants.DOUBLE_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:string type.
	 */
	public static final QName xsd_string = new QName(namespaceURI, XSD11Constants.STRING_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:unsignedByte type.
	 */
	public static final QName xsd_uByte = new QName(namespaceURI, XSD11Constants.UBYTE_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:unsignedShort type.
	 */
	public static final QName xsd_uShort = new QName(namespaceURI, XSD11Constants.USHORT_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:unsignedInt type.
	 */
	public static final QName xsd_uInt = new QName(namespaceURI, XSD11Constants.UINT_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:unsignedLong type.
	 */
	public static final QName xsd_uLong = new QName(namespaceURI, XSD11Constants.ULONG_TYPE_LOCALPART, prefix);
	
	/**
	 * Target type from typemap.dat
	 */
	public static final QName xsd_uint64_t = new QName(namespaceURI, XSD11Constants.UINT64_T_TYPE_LOCALPART, prefix);	

	/**
	 * http://www.w3.org/2001/XMLSchema:unsignedByte type.
	 */
	public static final QName xsd_sByte = new QName(namespaceURI, XSD11Constants.SBYTE_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:unsignedShort type.
	 */
	public static final QName xsd_sShort = new QName(namespaceURI, XSD11Constants.SSHORT_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:unsignedInt type.
	 */
	public static final QName xsd_sInt = new QName(namespaceURI, XSD11Constants.SINT_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:unsignedLong type.
	 */
	public static final QName xsd_sLong = new QName(namespaceURI, XSD11Constants.SLONG_TYPE_LOCALPART, prefix);

	/**
	 * http://www.w3.org/2001/XMLSchema:complexType.
	 */	
	public static final QName xsd_complexeType = new QName(namespaceURI, XSD11Constants.COMPLEX_TYPE_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:complexContent.
	 */
	public static final QName xsd_complexeContent = new QName(namespaceURI, XSD11Constants.COMPLEX_CONTENT_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:restriction.
	 */
	public static final QName xsd_restriction = new QName(namespaceURI, XSD11Constants.RESTRICTION_LOCALPART, prefix);
	/**
	 * http://www.w3.org/2001/XMLSchema:attribute.
	 */
	public static final QName xsd_attribute = new QName(namespaceURI, XSD11Constants.ATTRIBUTE_LOCALPART, prefix);


}


