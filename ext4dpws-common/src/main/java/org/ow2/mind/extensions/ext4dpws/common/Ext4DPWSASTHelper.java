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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.Loader;
import org.objectweb.fractal.adl.NodeFactory;
import org.objectweb.fractal.adl.interfaces.Interface;
import org.objectweb.fractal.adl.interfaces.InterfaceContainer;
import org.objectweb.fractal.adl.types.TypeInterface;
import org.objectweb.fractal.adl.types.TypeInterfaceUtil;
import org.ow2.mind.adl.ast.ASTHelper;
import org.ow2.mind.adl.ast.Attribute;
import org.ow2.mind.adl.ast.AttributeContainer;
import org.ow2.mind.adl.ast.Binding;
import org.ow2.mind.adl.ast.BindingContainer;
import org.ow2.mind.adl.ast.Component;
import org.ow2.mind.adl.ast.ComponentContainer;
import org.ow2.mind.adl.ast.ImplementationContainer;
import org.ow2.mind.adl.ast.Source;
import org.ow2.mind.adl.idl.InterfaceDefinitionDecorationHelper;
import org.ow2.mind.idl.IDLLoader;
import org.ow2.mind.idl.ast.InterfaceDefinition;
import org.ow2.mind.io.BasicOutputFileLocator;


/**
 * Utility class used to manipulate component ADL ast.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public final class Ext4DPWSASTHelper extends ASTHelper {



	/**
	 * Default constructor.
	 */
	private Ext4DPWSASTHelper()
	{

	}

	/**
	 * Dump a component definition into a file.
	 * @param componentDef The component definition to dump.
	 * @param idlLoader The IDL loader to used to retrieve definition of interface contained in the component definition.
	 * @param adlLoader The ADL loader to used to retrieve definition of component contained in the component definition;
	 * @param outFile The out file.
	 * @param context The compiler context.
	 * @throws IOException Exception during writing.
	 * @throws ADLException Exception during definition load.
	 */
	public static void dumpComponent(Definition componentDef, IDLLoader idlLoader, Loader adlLoader, String outFile, Map<Object, Object> context) throws IOException, ADLException
	{
		FileWriter fr = new FileWriter(new File((File)context.get(BasicOutputFileLocator.OUTPUT_DIR_CONTEXT_KEY), outFile));
		BufferedWriter br = new BufferedWriter(fr);
		Definition tmp = null;

		InterfaceDefinition itfDef = null;

		String type = ASTHelper.isComposite(componentDef) ? "composite " : "primitive ";
		br.write(type + componentDef.getName() + "{");
		br.newLine();


		for (final Interface itf : ((InterfaceContainer) componentDef).getInterfaces()) 
		{

			itfDef = InterfaceDefinitionDecorationHelper.getResolvedInterfaceDefinition((TypeInterface) itf, idlLoader, context);
			type = TypeInterfaceUtil.isServer(itf) ? "provides " : "requires ";
			br.newLine();
			br.write(type + itfDef.getName() + " as " + itf.getName() + ";");
			br.newLine();
		}
		if (componentDef instanceof ComponentContainer)
		{
			Component[] subCpts = ((ComponentContainer)componentDef).getComponents();
			for (Component instance : subCpts)
			{
				tmp = ASTHelper.getResolvedComponentDefinition(instance, adlLoader, context);
				br.newLine();
				br.write("\tcontains " + tmp.getName() + " as " + instance.getName() + ";");
				br.newLine();
			}
		}

		if (componentDef instanceof AttributeContainer)
		{
			for(Attribute att : ((AttributeContainer)componentDef).getAttributes())
			{
				br.newLine();
				br.write("attributes " + att.getType() + " " + att.getName() + " = " + att.getValue().toString() + ";");
			}
		}

		if (componentDef instanceof BindingContainer)
		{
			Binding [] bindings = ((BindingContainer)componentDef).getBindings();
			for (Binding instance : bindings)
			{
				br.newLine();
				br.write("\tbinds " + instance.getFromComponent() + "." + instance.getFromInterface() + " to " + instance.getToComponent() + "." + instance.getToInterface() + ";");
				br.newLine();
			}
		}
		if (componentDef instanceof ImplementationContainer)
		{
			Source [] sources = ((ImplementationContainer)componentDef).getSources();
			for (Source instance : sources)
			{			
				br.newLine();
				br.write("\t source {{\n\t" + instance.getCCode() + "\n\t}};");
				br.newLine();
			}
		}

		br.write("}");
		br.close();

	}

	/**
	 * Dump a component definition into a file.
	 * @param componentDef The component definition contained into a String.
	 * @param outFile The output file name.
	 * @param context The current mind compiler context.
	 * @throws IOException Exception during output file access.
	 */
	public static void dumpComponent(String componentDef, String outFile, Map<Object, Object> context) throws IOException
	{

		FileWriter fr = new FileWriter(new File((File)context.get(BasicOutputFileLocator.OUTPUT_DIR_CONTEXT_KEY), outFile));
		BufferedWriter br = new BufferedWriter(fr);
		br.write(componentDef.toString());
		br.close();

	}

	/**
	 * Check if a component definition contains a "boot.Main" interface as server interface.
	 * @param definition : The component definition to check.
	 * @param context : The current mind compiler context.
	 * @return : The "boot.Main" interface (null if not found)
	 * @throws ADLException : Exception during component definition parsing. 
	 */
	public static Interface findProvidedMain(Definition definition,IDLLoader idlLoader, Map<Object, Object> context) throws ADLException
	{

		Interface main = null;

		Collection<Interface> interfaces = Ext4DPWSIDLHelper.getServerInterfaces(definition);
		Iterator<Interface> it = interfaces.iterator();
		Interface itf = null;
		InterfaceDefinition itfDef = null;
		while (it.hasNext())
		{
			itf = it.next();
			itfDef = InterfaceDefinitionDecorationHelper.getResolvedInterfaceDefinition((TypeInterface) itf, idlLoader, context);
			if (itfDef.getName().equalsIgnoreCase("boot.Main"))
			{
				main = itf;
				break;
			}
			main  = null;
		}

		return main;

	}

	/**
	 * Create a new binding node.
	 * @param nodeFactoryItf The node factory to use for node creation.
	 * @param from The from component.
	 * @param to The to component.
	 * @param fromItf The from interface name.
	 * @param toItf The to interface name.
	 * @return The new created binding.
	 */
	public static Binding newBinding(NodeFactory nodeFactoryItf, Component from, Component to, String fromItf, String toItf)
	{
		Binding binding = ASTHelper.newBinding(nodeFactoryItf);
		binding.setFromComponent(from.getName());
		binding.setToComponent(to.getName());
		binding.setFromInterface(fromItf);
		binding.setToInterface(toItf);

		return binding;
	}

}
