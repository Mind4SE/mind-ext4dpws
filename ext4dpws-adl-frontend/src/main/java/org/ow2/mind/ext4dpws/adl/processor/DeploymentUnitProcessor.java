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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.antlr.stringtemplate.StringTemplate;
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
import org.ow2.mind.extensions.ext4dpws.common.StringTemplateHelper;
import org.ow2.mind.extensions.ext4dpws.wsdl2cpl.core.impl.dpws.STConstants;

/**
 * Process @DeploymentUnit annotation for MINDEXT4DPWS.
 * @author ecoly (edine.coly@sogeti.com)
 *
 */
public class DeploymentUnitProcessor extends AbstractADLLoaderAnnotationProcessor
implements
ADLLoaderAnnotationProcessor {


	/**
	 * Info logger.
	 */
	private static Logger logger = FractalADLLogManager.getLogger(DeploymentUnit.VALUE);

	/**
	 * The current mind compiler context.
	 */
	private Map<Object, Object> context;

	/**
	 * The current processed deployment unit.
	 */
	private DeploymentUnit currentDeploymentUnit;

	private Definition currentDepUnitDef = null;

	private Component currentDepUnitCpt = null;


	public Definition processAnnotation(final Annotation annotation,
			final Node node, final Definition definition, final ADLLoaderPhase phase,
			final Map<Object, Object> context) throws ADLException {

		if(node instanceof Component)
		{
			this.currentDepUnitCpt = (Component) node;
			logger.info("Process a DeploymentUnit component : "
					+ this.currentDepUnitCpt.getDefinitionReference().getName() + " '"+ this.currentDepUnitCpt.getName() + "'");
			checkAnnotation(definition);

			this.currentDeploymentUnit = (DeploymentUnit) annotation;		
			this.context = context;
			this.currentDepUnitDef =  ASTHelper.getResolvedComponentDefinition(currentDeploymentUnit.getTaggedComponent(), loaderItf, context);

			processDeploymentUnit();


		}

		return definition;

	}

	/**
	 * Process a DPWS deployment unit model (add DPWS bootstrap, bind skeletons, etc...).
	 * @throws ADLException : Thrown if error during AST manipulation.
	 */
	private void processDeploymentUnit() throws ADLException
	{

		Definition topLevelDef = this.currentDeploymentUnit.getTopLevelDefinition();

		//create a DPWS bootstrap component and add it to the deploy unit top level
		Component dpwsBootstrapCpt = createDPWSBootSrap(topLevelDef.getName()); 

		((ComponentContainer) topLevelDef).addComponent(dpwsBootstrapCpt);

		//add and bind skeleton		
		addAndBindSkeletons(topLevelDef, dpwsBootstrapCpt);

		//create a mind bootstrap and add it to the top level definition
		Component mindBootstrapCpt = createMindBoostrap(); 
		((ComponentContainer) topLevelDef).addComponent(mindBootstrapCpt); 		

		//bind the MIND bootstrap and the DPWS bootstrap
		Binding binding = Ext4DPWSASTHelper.newBinding(nodeFactoryItf, mindBootstrapCpt, dpwsBootstrapCpt, "entryPoint", "main");
		((BindingContainer) topLevelDef).addBinding(binding);

		Interface main = this.currentDeploymentUnit.getMain(); //should not be null at this step because controlled by the DeploymentArchitecureProcessor

		binding = Ext4DPWSASTHelper.newBinding(nodeFactoryItf, dpwsBootstrapCpt, this.currentDepUnitCpt, "entryPoint", main.getName());

		((BindingContainer) topLevelDef).addBinding(binding);

	}

	/**
	 * Create a DPWS bootstrap component.
	 * @param name The top level definition name.
	 * @return The created DPWS bootstrap component.
	 * @throws ADLException : Thrown if any error occurs during AST manipulation.
	 */
	private Component createDPWSBootSrap(String name) throws ADLException
	{

		Definition dpwsBootstrapDef = null;
		Component dpwsBootstrapCpt = null;
		int ris = this.currentDeploymentUnit.getRemoteInterfaces().size();
		int rrs = this.currentDeploymentUnit.getRemoteReferences().size();
		StringTemplate boostrapAdlTemplate = null;


		if(ris == 0)//client only deployment unit			
		{
			boostrapAdlTemplate = StringTemplateHelper.instanceOfClientBootstrapADL();
			logger.info("Load CLIENT_ONLY_BOOTSTRAP for " + this.currentDepUnitDef.getName());
		} else if(rrs == 0) //server only deployment unit
		{			
			boostrapAdlTemplate = StringTemplateHelper.instanceOfServerBootstrapADL();	
			logger.info("Load SERVER_ONLY_BOOTSTRAP for " + this.currentDepUnitDef.getName());
		} else //client-server deployment unit
		{ 
			boostrapAdlTemplate = StringTemplateHelper.instanceOfClientServerBootstrapADL();
			logger.info("Load CLIENT_SERVER_BOOTSTRAP for " + this.currentDepUnitDef.getName());
		}

		String deviceUUID = this.currentDeploymentUnit.id;
		boostrapAdlTemplate.setAttribute(STConstants.$NAME$, name);
		boostrapAdlTemplate.setAttribute(STConstants.$UNIT_NAME$, this.currentDeploymentUnit.execName);
		boostrapAdlTemplate.setAttribute(STConstants.$DEVICE_UUID$, deviceUUID);
		boostrapAdlTemplate.setAttribute(STConstants.$PORT$, this.currentDeploymentUnit.serverPort);
		boostrapAdlTemplate.setAttribute(STConstants.$SKELETONS_SIZE$, ris);

		//load the DPWS bootstrap component definition
		dpwsBootstrapDef = loadFromSource(name + ".Bootstrap", boostrapAdlTemplate.toString(), context);

		//create the DPWS bootstrap component
		dpwsBootstrapCpt = ASTHelper.newComponent(nodeFactoryItf, "dpws_bootstrap", dpwsBootstrapDef.getName());		
		ASTHelper.setResolvedComponentDefinition(dpwsBootstrapCpt, dpwsBootstrapDef);

		//debug
		if(logger.getLevel() ==  Level.FINEST)
		{
			try {
				Ext4DPWSASTHelper.dumpComponent(boostrapAdlTemplate.toString(), dpwsBootstrapDef.getName() + ".adl", context);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dpwsBootstrapCpt;

	}

	/**
	 * Load and create a new standard MIND boot.Bootstrap.
	 * This component should be bind with the dpws.Bootstrap component.
	 * @return : The created boot.Bootstrap component.
	 * @throws ADLException : Thrown if any error occurs during AST manipulation.
	 */
	private Component createMindBoostrap() throws ADLException
	{

		//load the MIND bootstrap and add it to the deploy unit top level
		Definition mindBootstrapDef = loaderItf.load("boot.Bootstrap", context);
		Component mindBootstrapCpt = ASTHelper.newComponent(nodeFactoryItf, "mind_bootsrap", mindBootstrapDef.getName());
		ASTHelper.setResolvedComponentDefinition(mindBootstrapCpt, mindBootstrapDef); 	
		return mindBootstrapCpt;
	}

	/**
	 * Add skeleton to deployment unit top level definition and bind them to DPWS bootstrap component and to deployment unit component.
	 * @param topLevelDef : The deployment unit top level definition.
	 * @param dpwsBootstrapCpt : The "DPWS" bootstrap component.
	 * @param dpwsRemoteItfs : The set of "DPWS" remote interfaces of the deployment unit.
	 * @throws ADLException : Thrown if any error occurs during AST manipulation.
	 */
	private void addAndBindSkeletons(Definition topLevelDef, Component dpwsBootstrapCpt) throws ADLException
	{
		//add and bind generated skeletons
		int i = 0;
		Binding binding = null;
		Component skeletonCpt = null;
		Iterator<Interface> it = this.currentDeploymentUnit.getRemoteInterfaces().iterator();
		Interface current = null;
		Component depUnitCpt = currentDeploymentUnit.getTaggedComponent();
		RemoteInterface remoteInterface = null;
		while (it.hasNext())
		{

			current = it.next();
			remoteInterface = AnnotationHelper.getAnnotation(current, RemoteInterface.class);
			skeletonCpt = remoteInterface.getSkeleton(this.currentDeploymentUnit.id);

			((ComponentContainer)topLevelDef).addComponent(skeletonCpt);
			binding = Ext4DPWSASTHelper.newBinding(nodeFactoryItf, dpwsBootstrapCpt, skeletonCpt, "skItf", "skItf");
			((BindingContainer) topLevelDef).addBinding(binding);
			binding.setFromInterfaceNumber(String.valueOf(i));

			binding = Ext4DPWSASTHelper.newBinding(nodeFactoryItf, skeletonCpt, depUnitCpt, "depl_unit_itf", current.getName());
			((BindingContainer) topLevelDef).addBinding(binding);
			i++;
		}
	}

	private void checkAnnotation(Definition definition) throws ADLException
	{
		//check if the definition is tagged with @DeploymentArchitecure
		DeploymentArchitecture depArchAnnot = AnnotationHelper.getAnnotation(definition, DeploymentArchitecture.class);

		if (depArchAnnot == null)
		{
			throw new ADLException(Ext4DPWSADLErrors.INVALID_DEFINITION_NOT_TAGGED, definition, definition.getName(), DeploymentArchitecture.VALUE);

		}
	}
}


