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

package org.ow2.mind.ext4dpws.adl.processor;


import java.util.Map;
import java.util.logging.Logger;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.Node;
import org.objectweb.fractal.adl.interfaces.Interface;
import org.objectweb.fractal.adl.util.FractalADLLogManager;
import org.ow2.mind.adl.annotation.ADLLoaderAnnotationProcessor;
import org.ow2.mind.adl.annotation.ADLLoaderPhase;
import org.ow2.mind.adl.annotation.AbstractADLLoaderAnnotationProcessor;
import org.ow2.mind.adl.annotation.predefined.DeploymentArchitecture;
import org.ow2.mind.adl.annotation.predefined.DeploymentUnit;
import org.ow2.mind.adl.annotation.predefined.RemoteBinding;
import org.ow2.mind.adl.annotation.predefined.RemoteInterface;
import org.ow2.mind.adl.ast.ASTHelper;
import org.ow2.mind.adl.ast.Binding;
import org.ow2.mind.adl.ast.BindingContainer;
import org.ow2.mind.adl.ast.Component;
import org.ow2.mind.adl.ast.ComponentContainer;
import org.ow2.mind.annotation.Annotation;
import org.ow2.mind.annotation.AnnotationHelper;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSADLErrors;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSASTHelper;

/**
 * Process a @RemoteBinding annotation.
 * @author ecoly (edine.coly@sogeti.com)
 *
 */
public class RemoteBindingProcessor extends AbstractADLLoaderAnnotationProcessor
implements
ADLLoaderAnnotationProcessor
{

	/**
	 * The current binding client component.
	 */
	private Component currentClientCpt = null;

	/**
	 * The current binding server component.
	 */
	private Component currentServerCpt = null;

	/**
	 * The current binding client component definition. 
	 */
	private Definition currentClientDef = null;

	/**
	 * The current binding server component definition.
	 */
	private Definition currentServerDef = null;
	
	/**
	 * The current server interface involved in the binding.
	 */
	private Interface currentServerItf;
	
	/**
	 * The current client interface involved in the binding.
	 */
	private Interface currentClientItf;
	
	/**
	 * The definition where the binding is currently processed.
	 */
	private Definition currentDefinition;
	
	private Binding currentBinding;

	/**
	 * Logger.
	 */
	private static Logger logger = FractalADLLogManager.getLogger(RemoteBinding.VALUE);
		
	public Definition processAnnotation(final Annotation annotation,
			final Node node, final Definition definition, final ADLLoaderPhase phase,
			final Map<Object, Object> context) throws ADLException {


		if (node instanceof Binding)
		{
			this.currentBinding = (Binding) node;
			
			//get the server component instance
			this.currentServerCpt = ASTHelper.getComponent(definition, this.currentBinding.getToComponent());
			//get the client component instance
			this.currentClientCpt = ASTHelper.getComponent(definition, this.currentBinding.getFromComponent());
			//get the server component definition
			this.currentServerDef = ASTHelper.getResolvedComponentDefinition(currentServerCpt, null, context);
			//get the client component definition
			this.currentClientDef = ASTHelper.getResolvedComponentDefinition(currentClientCpt, null, context);
			//get the server interface involved in the binding
			this.currentServerItf = ASTHelper.getInterface(this.currentServerDef, this.currentBinding.getToInterface());
			//get the client interface involved in the binding
			this.currentClientItf = ASTHelper.getInterface(this.currentClientDef, this.currentBinding.getFromInterface());
			//set the definition where the binding is currently processed
			this.currentDefinition = definition;
			checkBinding();

			processBinding();

		}

		return definition;

	}


	private void processBinding() throws ADLException
	{

		DeploymentUnit clientDepUnit = AnnotationHelper.getAnnotation(this.currentClientCpt, DeploymentUnit.class);
		DeploymentUnit serverDepUnit = AnnotationHelper.getAnnotation(this.currentServerCpt, DeploymentUnit.class);

		
		Definition topLevelDef = null;
	
		//check the skeleton and stub data for from the remote interface
		RemoteInterface remoteInterface = AnnotationHelper.getAnnotation(this.currentServerItf, RemoteInterface.class);
		Component skeletonCpt = remoteInterface.getSkeleton(serverDepUnit.id);
		Component stubCpt = remoteInterface.getStub(serverDepUnit.id);
		//add the stub to the client deployment unit top level component definition if not yet done
		topLevelDef = clientDepUnit.getTopLevelDefinition();
		Component [] components = ((ComponentContainer)topLevelDef).getComponents();
		boolean contains = false;
		for(Component cpt : components)
		{
			if(cpt.equals(stubCpt))
			{
				contains = true;
				break;
			}
		}
		if(!contains){
			((ComponentContainer)topLevelDef).addComponent(stubCpt);
		}
	
		//bind the stub to the deployment unit component
		
		Binding binding = Ext4DPWSASTHelper.newBinding(nodeFactoryItf, this.currentClientCpt, stubCpt, this.currentClientItf.getName(), "depl_unit_itf");
		((BindingContainer)topLevelDef).addBinding(binding);
		logger.info("Connect " 
				+ this.currentClientCpt.getName()
				+ " '" 
				+ this.currentClientItf.getName()
				+ "' through stub " 
				+ stubCpt.getDefinitionReference().getName()
				+ " '" 
				+ stubCpt.getName() 
				+ "' with skeleton " 
				+ skeletonCpt.getDefinitionReference().getName()
				+ " '" 
				+ skeletonCpt.getName() 
				+ "'(DPWS service ID = '"
				+ remoteInterface.getServiceId()	
				+ "')"
				);	
	}
	


	/**
	 * Check if the binding is done between a "@RemoteReference" tagged client interface and 
	 * a "@RemoteInterface" tagged server interface. And check if selected binding type is coherent.
	 * @throws ADLException : If checking fails.
	 */
	private void checkBinding() throws ADLException
	{

		//check if the definition is tagged with @DeploymentArchitecure
		if (AnnotationHelper.getAnnotation(this.currentDefinition, DeploymentArchitecture.class) == null)
		{
			throw new ADLException(Ext4DPWSADLErrors.INVALID_DEFINITION_NOT_TAGGED, this.currentDefinition, this.currentDefinition.getName(), DeploymentArchitecture.VALUE);
		}
		
		//check that server component are tagged deploy unit
		if (AnnotationHelper.getAnnotation(this.currentServerCpt, DeploymentUnit.class) == null)
		{
			throw new ADLException(Ext4DPWSADLErrors.INVALID_BINDING_NO_DEP_UNIT, this.currentDefinition, this.currentServerCpt.getName());
		}

		//check that client component are tagged deploy unit
		if (AnnotationHelper.getAnnotation(this.currentClientCpt, DeploymentUnit.class) == null)
		{
			throw new ADLException(Ext4DPWSADLErrors.INVALID_BINDING_NO_DEP_UNIT, this.currentDefinition, this.currentClientCpt.getName());
		}

		//check that server interface is tagged with @RemoteInterface
		if (AnnotationHelper.getAnnotation(this.currentServerItf, RemoteInterface.class) == null)
		{
			throw new ADLException(Ext4DPWSADLErrors.INVALID_BINDING_NOT_TAGGED_INTERFACE, this.currentServerDef, this.currentServerItf.getName(), RemoteInterface.VALUE);
		}

		//check that client interface is tagged with @RemoteReference
		/*if (AnnotationHelper.getAnnotation(this.currentClientItf, RemoteReference.class) == null)
		{
			throw new ADLException(Ext4DPWSADLErrors.INVALID_BINDING_NOT_TAGGED_INTERFACE, this.currentClientDef, this.currentClientItf.getName(), RemoteReference.VALUE);
		}*/

	}	
}
