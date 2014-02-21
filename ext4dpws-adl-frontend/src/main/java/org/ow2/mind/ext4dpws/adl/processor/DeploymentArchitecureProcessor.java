/**
 * ================================================================================
 *
 *   					 Copyright (C) 2010 Sogeti High Tech
 *   					 Copyright (C) 2014 Schneider-Electric
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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.antlr.stringtemplate.StringTemplate;
import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.Node;
import org.objectweb.fractal.adl.error.NodeErrorLocator;
import org.objectweb.fractal.adl.interfaces.Interface;
import org.objectweb.fractal.adl.interfaces.InterfaceContainer;
import org.objectweb.fractal.adl.types.TypeInterface;
import org.objectweb.fractal.adl.util.FractalADLLogManager;
import org.ow2.mind.adl.annotation.ADLLoaderAnnotationProcessor;
import org.ow2.mind.adl.annotation.ADLLoaderPhase;
import org.ow2.mind.adl.annotation.AbstractADLLoaderAnnotationProcessor;
import org.ow2.mind.adl.annotation.predefined.DeploymentArchitecture;
import org.ow2.mind.adl.annotation.predefined.DeploymentUnit;
import org.ow2.mind.adl.annotation.predefined.RemoteInterface;
import org.ow2.mind.adl.annotation.predefined.RemoteReference;
import org.ow2.mind.adl.ast.ASTHelper;
import org.ow2.mind.adl.ast.Component;
import org.ow2.mind.adl.ast.ComponentContainer;
import org.ow2.mind.adl.ast.DefinitionReference;
import org.ow2.mind.adl.idl.InterfaceDefinitionDecorationHelper;
import org.ow2.mind.adl.parameter.ast.Argument;
import org.ow2.mind.adl.parameter.ast.ArgumentContainer;
import org.ow2.mind.adl.parameter.ast.ParameterASTHelper;
import org.ow2.mind.annotation.Annotation;
import org.ow2.mind.annotation.AnnotationHelper;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSADLErrors;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSASTHelper;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSCompilerContextHelper;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSIOError;
import org.ow2.mind.extensions.ext4dpws.common.InterfaceDefinitionCloner;
import org.ow2.mind.extensions.ext4dpws.common.StringTemplateHelper;
import org.ow2.mind.extensions.ext4dpws.wsdl2cpl.core.impl.dpws.CDPWSCoreGenerator;
import org.ow2.mind.extensions.ext4dpws.wsdl2cpl.core.impl.dpws.STConstants;
import org.ow2.mind.extensions.ext4dpws.wsdl2cpl.core.impl.dpws.SkeletonADLGenerator;
import org.ow2.mind.extensions.ext4dpws.wsdl2cpl.core.impl.dpws.StubADLGenerator;
import org.ow2.mind.idl.ast.InterfaceDefinition;
import org.ow2.mind.value.ast.StringLiteral;
import org.ow2.mind.value.ast.ValueASTHelper;

/**
 * Process the @DeploymentArchitecure annotation.
 * @author ecoly (edine.coly@sogeti.com)
 * 
 * @contributor sseyvoz (sseyvoz@assystem.com)
 * Contribution: Ported to 2.1 Compiler.
 */
public class DeploymentArchitecureProcessor extends AbstractADLLoaderAnnotationProcessor
implements
ADLLoaderAnnotationProcessor {


	public static final String DPWS_GNU_LINUX_TARGET = "dpws.target.gnu.linux.Descriptor";

	public static final String DPWS_GNU_MINGW_TARGET = "dpws.target.gnu.mingw.Descriptor";

	/**
	 * Logger.
	 */
	private static Logger logger = FractalADLLogManager.getLogger(DeploymentArchitecture.VALUE);

	private int _NB = 0;

	private Map<Object, Object> context; 

	private Component currentDepUnitCpt;

	private Definition currentDepUnitDef;

	private DeploymentUnit currentDeploymentUnit;

	public Definition processAnnotation(final Annotation annotation,
			final Node node, final Definition definition, final ADLLoaderPhase phase,
			final Map<Object, Object> context) throws ADLException {

		this.context = context;

		DeploymentArchitecture deploymentArchitecure = AnnotationHelper.getAnnotation(node, DeploymentArchitecture.class);
		Component [] deployUnits = ((ComponentContainer)definition).getComponents();

		for (Component depUnitCpt : deployUnits)
		{				

			this.currentDepUnitCpt = depUnitCpt;
			this.currentDeploymentUnit = AnnotationHelper.getAnnotation(currentDepUnitCpt, DeploymentUnit.class);
			this.currentDepUnitDef = ASTHelper.getResolvedComponentDefinition(depUnitCpt, loaderItf, context);
			
			isValidDeploymentUnitCpt();
			processCurrentDeploymentUnit();
			deploymentArchitecure.addDeploymentUnit(this.currentDeploymentUnit);
			
			_NB++;
		}

		return definition;
	}

	// SSZ
	/**
	 *  Moved the RemoteReference annotation processor (@see RemoteReferenceProcessor) code here
	 *  during migration from 0.2.3a to 2.1, since it modified the client interfaces type before
	 *  binding check, and the server was not modified accordingly, which seems normal since the
	 *  server won't be in the same  DeploymentUnit... but the binding check failed since the 
	 *  interface signature was suffixed by ".Stub" and not the server one.
	 * @throws ADLException 
	 */
	private void processRemoteReference() throws ADLException {
		Interface currentItf = null;
		
		Collection<Interface> remoteReferenceItfs = this.currentDeploymentUnit.getRemoteReferences();
		
		// we only want to handle concerned deployment units here
		if (remoteReferenceItfs == null)
			return;
		
		Iterator<Interface> it = remoteReferenceItfs.iterator();

		while (it.hasNext())
		{
			currentItf = it.next();

			//InterfaceDefinition currentItfDef = InterfaceDefinitionDecorationHelper.getResolvedInterfaceDefinition((TypeInterface) currentItf, idlLoaderItf, context);
			InterfaceDefinition currentItfDef = itfSignatureResolverItf.resolve((TypeInterface) currentItf, this.currentDepUnitDef, context);

			InterfaceDefinition clone = InterfaceDefinitionCloner.getInstance().cloneDefinition(nodeFactoryItf, currentItfDef);

			loadIDLFromAST(clone, context);				

			Interface toStubItf = ASTHelper.newClientInterfaceNode(nodeFactoryItf, currentItf.getName(),clone.getName());

			InterfaceDefinitionDecorationHelper.setResolvedInterfaceDefinition((TypeInterface) toStubItf, clone);

			((InterfaceContainer) this.currentDepUnitDef).removeInterface(currentItf);

			((InterfaceContainer) this.currentDepUnitDef).addInterface(toStubItf);		

		}
	}

	/**
	 * Get the deployment unit annotation associated to the
	 * deploymentUnitComponent and fill it values.
	 * @throws ADLException
	 * @throws GeneratorException
	 * @throws IOException
	 */
	private void processCurrentDeploymentUnit() throws ADLException
	{

		StringTemplate template = StringTemplateHelper.instanceOfDeploymentUnitADL();

		String name = (this.currentDepUnitDef.getName().replace('<', '_').replace('>', '_'))
				+ ".DeploymentUnit.Impl" + _NB;


		template.setAttribute(STConstants.$NAME$, name);			

		//load the new top level deployment unit definition		
		Definition duTopLevelDef = loadFromSource(name, template.toString(), context);		
		((ComponentContainer) duTopLevelDef).addComponent(currentDepUnitCpt);	
		this.currentDeploymentUnit.setTopLevelDefinition(duTopLevelDef);
		this.currentDeploymentUnit.setTaggedComponent(currentDepUnitCpt);
		this.currentDeploymentUnit.setRemoteInterfaces(ProcessorsHelper.getRemoteInterfaces(this.currentDepUnitDef));
		this.currentDeploymentUnit.setRemoteReferences(ProcessorsHelper.getRemoteReferences(this.currentDepUnitDef));

		// Added when ported to 2.1 compiler
		processRemoteReference();
		
		processRemoteInterface();

		logger.info("Create new deployment unit of " 
				+ duTopLevelDef.getName() 
				+ " with deviceUUID = " 
				+ this.currentDeploymentUnit.id);

	}	

	/**
	 * Create a skeleton component per remote interfaces and assign it to each of them.
	 * Also create the stub template that should be used during remote binding
	 * to create stub component to communicate with this interface.
	 * @throws ADLException : Thrown if error occurs during AST manipulation.
	 */
	@SuppressWarnings("unchecked")
	private void processRemoteInterface() throws ADLException
	{

		Map<String, Object> generatedFiles = null;
		Iterator<Interface> it = this.currentDeploymentUnit.getRemoteInterfaces().iterator();
		Interface currentItf = null;
		InterfaceDefinition currentItfDef = null;
		String prefix, source = null;
		String outDirName= null;
		File outputFolder = null;
		RemoteInterface remoteInterface = null; 
		StringTemplate skeletonTemplate, stubTemplate = null;		
		Definition skeletonDef,stubDef;
		DefinitionReference definitionReference  = null;
		Argument arg = null;
		Component skeletonCpt,stubCpt;
		StringLiteral strLit;


		while (it.hasNext())
		{
			currentItf = it.next();

			remoteInterface = AnnotationHelper.getAnnotation(currentItf, RemoteInterface.class);

			currentItfDef = InterfaceDefinitionDecorationHelper.getResolvedInterfaceDefinition((TypeInterface) currentItf, idlLoaderItf, context);

			remoteInterface.setServiceId(currentItf.getName()); // the DPWS hosted service id is the interface name.

			outDirName = this.currentDepUnitDef.getName().replace('.', '_')
					+ "_" + this.currentDepUnitCpt.getName() 
					+ "_" + currentItfDef.getName().replace('.', '_')
					+ "_" + currentItf.getName();

			outDirName = outDirName.replace('>', '_').replace('<', '_');

			outputFolder = new File((File)context.get("outputdir"), "dpws-gen/" + outDirName);
			Ext4DPWSCompilerContextHelper.updateIncludeCFlags(outputFolder, context);

			try {

				generatedFiles = CDPWSCoreGenerator.generateFiles(currentItfDef, outputFolder, idlLoaderItf, context);

				//generate the skeleton ADL definition for this interface
				prefix = currentItfDef.getName() + ".Skeleton" + _NB;
				skeletonTemplate = SkeletonADLGenerator.generateADL(currentItfDef, generatedFiles, idlLoaderItf, context);
				skeletonTemplate.setAttribute(STConstants.$NAME$, prefix);
				source = skeletonTemplate.toString();
				skeletonDef = loadFromSource(prefix, source, context);			

				//dump skeleton ADL if FINEST level
				if(logger.getLevel()==Level.FINEST)
				{
					Ext4DPWSASTHelper.dumpComponent(source, prefix + ".adl", context);
				}

				//clone the interface definition by adding the ext4dpws_error parameter to each method
				InterfaceDefinition clone = InterfaceDefinitionCloner.getInstance().cloneDefinition(nodeFactoryItf, currentItfDef);
				loadIDLFromAST(clone, context);	
				//load the stub template that use the cloned interface
				stubTemplate = StubADLGenerator.generateADL(currentItfDef, generatedFiles, idlLoaderItf, context);	
				prefix = currentItfDef.getName() + ".Stub" + _NB;
				Map attributes = stubTemplate.getAttributes();
				attributes.put(STConstants.$NAME$, prefix);
				source = stubTemplate.toString();					
				stubDef = loadFromSource(prefix, source, context);
				//dump stub ADL if FINEST level
				if(logger.getLevel()==Level.FINEST)
				{
					Ext4DPWSASTHelper.dumpComponent(source, prefix + ".adl", context);
				}


			} catch (Exception e) {

				throw new ADLException(Ext4DPWSIOError.GEN_IO_ERR, new NodeErrorLocator(currentItfDef),currentItf.getName(),e.getMessage());

			}

			//create the skeleton instance
			definitionReference = ASTHelper.newDefinitionReference(nodeFactoryItf, skeletonDef.getName());
			definitionReference = (DefinitionReference) ParameterASTHelper.turnsToArgumentContainer(definitionReference, nodeFactoryItf, nodeMergerItf);
			arg = ParameterASTHelper.newArgument(nodeFactoryItf, "serviceId");
			((ArgumentContainer) definitionReference).addArgument(arg);
			strLit = ValueASTHelper.newStringLiteral(nodeFactoryItf,  remoteInterface.getServiceId());
			arg.setValue(strLit);			
			skeletonCpt = ASTHelper.newComponent(nodeFactoryItf, "sk_" + _NB, definitionReference);
			ASTHelper.setResolvedComponentDefinition(skeletonCpt, skeletonDef); 			
			//assign it to the remote interface
			remoteInterface.addSkeleton(this.currentDeploymentUnit.id,skeletonCpt);
			logger.info("Create new DPWS skeleton : " + skeletonDef.getName()
					+ " '" + skeletonCpt.getName());

			//create the stub instance			
			definitionReference = ASTHelper.newDefinitionReference(nodeFactoryItf, stubDef.getName());
			definitionReference = (DefinitionReference) ParameterASTHelper.turnsToArgumentContainer(definitionReference, nodeFactoryItf, nodeMergerItf);
			arg = ParameterASTHelper.newArgument(nodeFactoryItf, "deviceId");
			((ArgumentContainer) definitionReference).addArgument(arg);
			strLit = ValueASTHelper.newStringLiteral(nodeFactoryItf,  this.currentDeploymentUnit.id);
			arg.setValue(strLit);
			arg = ParameterASTHelper.newArgument(nodeFactoryItf, "serviceId");
			((ArgumentContainer) definitionReference).addArgument(arg);
			strLit = ValueASTHelper.newStringLiteral(nodeFactoryItf,  remoteInterface.getServiceId());
			arg.setValue(strLit);
			stubCpt = ASTHelper.newComponent(nodeFactoryItf, "stub_" + _NB, definitionReference);
			ASTHelper.setResolvedComponentDefinition(stubCpt, stubDef); 	
			//assign it to the remote interface
			remoteInterface.addStub(this.currentDeploymentUnit.id,stubCpt);
			_NB++;

		}
	}

	/**
	 * Check if the deployment unit definition is valid.
	 * @throws ADLException : Thrown if the definition is invalid.
	 */
	private void isValidDeploymentUnitDef() throws ADLException
	{

		Interface main = Ext4DPWSASTHelper.findProvidedMain(this.currentDepUnitDef,idlLoaderItf, context);

		if (main == null)
		{
			throw new ADLException(Ext4DPWSADLErrors.INVALID_DEPL_UNIT_NO_MAIN, this.currentDepUnitDef, this.currentDepUnitDef.getName());
		}

		RemoteInterface remoteInterface = AnnotationHelper.getAnnotation(main, RemoteInterface.class);

		RemoteReference remoteReference = AnnotationHelper.getAnnotation(main, RemoteReference.class);

		if (remoteInterface != null
				|| remoteReference != null)
		{
			throw new ADLException(Ext4DPWSADLErrors.INVALID_DEPL_UNIT_TAGGED_MAIN, this.currentDepUnitDef, main.getName());
		}

		this.currentDeploymentUnit.setMain(main);

	}


	/**
	 * Check if the astNode is a valid deployment unit.
	 * @throws ADLException : Thrown if invalid deployment unit.
	 */
	private void isValidDeploymentUnitCpt() throws ADLException
	{

		DeploymentUnit depUnitAnnot = AnnotationHelper.getAnnotation(this.currentDepUnitCpt, DeploymentUnit.class);

		if (depUnitAnnot == null)
		{
			throw new ADLException(Ext4DPWSADLErrors.INVALID_DEPL_UNIT_NOT_TAGGED, this.currentDepUnitCpt, this.currentDepUnitCpt.getName());
		}

		if (depUnitAnnot.dpwsDescriptor != null)
		{
			if (!(depUnitAnnot.dpwsDescriptor.equalsIgnoreCase(DPWS_GNU_LINUX_TARGET)
					|| depUnitAnnot.dpwsDescriptor.equalsIgnoreCase(DPWS_GNU_MINGW_TARGET)))
			{
				throw new ADLException(Ext4DPWSADLErrors.INVALID_TARGET_DESCRIPTOR, this.currentDepUnitCpt, depUnitAnnot.dpwsDescriptor, DPWS_GNU_LINUX_TARGET, DPWS_GNU_MINGW_TARGET);

			}
		}		

		isValidDeploymentUnitDef();

	}


}
