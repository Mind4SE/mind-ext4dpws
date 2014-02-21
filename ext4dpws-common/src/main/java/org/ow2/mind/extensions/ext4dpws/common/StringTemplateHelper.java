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

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * Make the loading of String template more easy.
 * 
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public final class StringTemplateHelper {

	private static final StringTemplateGroup TEMPLATE_GRP = new StringTemplateGroup("ext4dpws");
	
	private StringTemplateHelper ()
	{		
	}
	
	public static StringTemplate instanceOfDeploymentUnitADL()
	{			
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/DeploymentUnitADL");
	}
	
	public static StringTemplate instanceOfServerBootstrapADL()
	{				
		return 	TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/ServerBootstrapADL");
	}
	
	public static StringTemplate instanceOfClientBootstrapADL()
	{				
		return 	TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/ClientBootstrapADL");
	}
	
	public static StringTemplate instanceOfClientServerBootstrapADL()
	{				
		return 	TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/ClientServerBootstrapADL");
	}
	
	public static StringTemplate instanceOfSkeletonADL()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/SkeletonADL");
	}
	
	public static StringTemplate instanceOfCallWithStructResp()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/CallWithStructResp");
	}
	
	public static StringTemplate instanceOfArrayCopy()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/ArrayCopy");
	}
	
	public static StringTemplate instanceOfStringArrayCopy()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/StringArrayCopy");
	}
	
	public static StringTemplate instanceOfStructArrayCopy()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/StructArrayCopy");
	}
	
	public static StringTemplate instanceOfSkeletonStructArrayCopy()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/SkeletonStructArrayCopy");
	}
	
	
	
	public static StringTemplate instanceOfCallWithNoStructResp()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/CallWithNoStructResp");
	}
	
	public static StringTemplate instanceOfStubADL()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/StubADL");
	}
	
	public static StringTemplate instanceOfStubWithHandlerADL()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/StubWithHandlerADL");
	}
	
	public static StringTemplate instanceOfMeth()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/Meth");
	}
	
	public static StringTemplate instanceOfWSDLDefinition()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11Definition");
	}
	
	public static StringTemplate instanceOfWSDL11Types()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11Types");
	}
	
	public static StringTemplate instanceOfWSDL11Message()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11Message");
	}
	
	public static StringTemplate instanceOfWSDL11PortType()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11PortType");
	}
		
	public static StringTemplate instanceOfWSDL11Operation()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11Operation");
	}
	
	public static StringTemplate instanceOfWSDL11OperationIn()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11OperationIn");
	}
	
	public static StringTemplate instanceOfWSDL11OperationOut()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11OperationOut");
	}
	
	public static StringTemplate instanceOfWSDL11RPCBinding()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11RPCBinding");
	}
	
	public static StringTemplate instanceOfWSDL11BindingOp()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11BindingOp");
	}
	
	public static StringTemplate instanceOfWSDL11BindingRpcLitOp()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11BindingDocLitOp");
	}
	
	public static StringTemplate instanceOfWSDL11Service()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11Service");		
	}
	
	public static StringTemplate instanceOfWSDL11Port()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11Port");
	}
	
	public static StringTemplate instanceOfWSDL11BindingEncOpIn()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11BindingEncOpIn");
	}
	
	public static StringTemplate instanceOfWSDL11BindingLitOpIn()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11BindingLitOpIn");
	}
	
	public static StringTemplate instanceOfWSDL11BindingEncOpOut()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11BindingEncOpOut");
	}
	
	public static StringTemplate instanceOfWSDL11BindingLitOpOut()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/WSDL11BindingLitOpOut");
	}
	
	public static StringTemplate instanceOfTypesMapping()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/wsdl2cpl/templates/TypesMapping");		
	}
	
	public static StringTemplate instanceOfStructComplexType()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/XSDComplexType");		
	}
	
	public static StringTemplate instanceOfXsdEnumDef()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/XSDEnum");		
	}
	
	public static StringTemplate instanceOfWSDL11ArrayOf()
	{				
		return TEMPLATE_GRP.getInstanceOf("org/ow2/mind/extensions/ext4dpws/idl2wsdl/templates/ArrayOf");
	}	
	
	
}
