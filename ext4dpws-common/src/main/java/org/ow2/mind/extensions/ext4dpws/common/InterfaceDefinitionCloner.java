package org.ow2.mind.extensions.ext4dpws.common;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.adl.NodeFactory;
import org.objectweb.fractal.adl.NodeUtil;
import org.ow2.mind.CommonASTHelper;
import org.ow2.mind.idl.ast.IDLASTHelper;
import org.ow2.mind.idl.ast.InterfaceDefinition;
import org.ow2.mind.idl.ast.Method;
import org.ow2.mind.idl.ast.Parameter;
import org.ow2.mind.idl.ast.PointerOf;
import org.ow2.mind.idl.ast.TypeDefReference;

public class InterfaceDefinitionCloner {

	private static InterfaceDefinitionCloner instance;

	private Map<String, InterfaceDefinition> clonedItfDef;

	private InterfaceDefinitionCloner()
	{
		clonedItfDef = new HashMap<String, InterfaceDefinition>();
	}

	public static InterfaceDefinitionCloner getInstance()
	{
		if (instance == null)
			instance = new InterfaceDefinitionCloner();

		return instance;
	}

	public InterfaceDefinition cloneDefinition(final NodeFactory nodeFactory, final InterfaceDefinition interfaceDefinition)
	{
		InterfaceDefinition clone = clonedItfDef.get(interfaceDefinition.getName());
		if(clone == null)
		{

			clone = NodeUtil.cloneTree(interfaceDefinition);
			Parameter p  = IDLASTHelper.newParameterNode(nodeFactory, "dpws_error");
			TypeDefReference type = CommonASTHelper.newNode(nodeFactory, "type", TypeDefReference.class); 
			type.setName("ext4dpws_error"); 
			PointerOf po = CommonASTHelper.newNode(nodeFactory, "type", PointerOf.class); 
			po.astSetDecoration("kind", "pointerOf");
			type.astSetDecoration("kind", "typedefRef"); 
			po.setType(type);
			p.setType(po);

			//add the dpws_error parameter to each method
			for(Method meth : clone.getMethods())
			{
				meth.addParameter(p); 			
			}
			clone.setName(interfaceDefinition.getName() + ".Stub");
			clonedItfDef.put(interfaceDefinition.getName(), clone);
			
		}

		return clone;
	}
}
