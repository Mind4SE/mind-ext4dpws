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


package org.ow2.mind.extensions.ext4dpws;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.CompilerError;
import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.Loader;
import org.objectweb.fractal.adl.error.Error;
import org.objectweb.fractal.adl.error.GenericErrors;
import org.ow2.mind.ADLCompiler.CompilationStage;
import org.ow2.mind.adl.DefinitionCompiler;
import org.ow2.mind.adl.DefinitionSourceGenerator;
import org.ow2.mind.adl.GraphCompiler;
import org.ow2.mind.adl.annotation.predefined.DeploymentArchitecture;
import org.ow2.mind.adl.annotation.predefined.DeploymentUnit;
import org.ow2.mind.adl.graph.ComponentGraph;
import org.ow2.mind.adl.graph.Instantiator;
import org.ow2.mind.annotation.AnnotationHelper;
import org.ow2.mind.cli.InvalidCommandLineException;
import org.ow2.mind.cli.OutPathOptionHandler;
import org.ow2.mind.cli.StageOptionHandler;
import org.ow2.mind.compilation.CompilationCommand;
import org.ow2.mind.compilation.CompilationCommandExecutor;
import org.ow2.mind.compilation.CompilerCommand;
import org.ow2.mind.compilation.CompilerContextHelper;
import org.ow2.mind.compilation.DirectiveHelper;
import org.ow2.mind.compilation.LinkerCommand;
import org.ow2.mind.extensions.ext4dpws.common.Ext4DPWSASTHelper;
import org.ow2.mind.idl.IDLLoader;
import org.ow2.mind.io.BasicOutputFileLocator;
import org.ow2.mind.target.TargetDescriptorLoader;
import org.ow2.mind.target.TargetDescriptorOptionHandler;
import org.ow2.mind.target.ast.ADLMapping;
import org.ow2.mind.target.ast.Flag;
import org.ow2.mind.target.ast.Target;

/**
 * @author ecoly (edine.coly@sogeti.com)
 * 
 * @contributor sseyvoz (sseyvoz@assystem.com)
 * Contribution: Ported to 2.1 Compiler.
 */
public class Launcher extends org.ow2.mind.Launcher {

	protected Loader adlLoader;
	protected IDLLoader idlLoader;
	protected TargetDescriptorLoader tdLoader;

	protected DefinitionSourceGenerator definitionSourceGenerator;
	protected DefinitionCompiler        definitionCompiler;
	protected Instantiator              graphInstantiator;
	protected GraphCompiler             graphCompiler;

	protected CompilationCommandExecutor executor;

	@Override
	protected void initCompiler() {
		// Standard init for errorManager and adlCompiler
		super.initCompiler();

		// The additions for DPWS manipulations
		adlLoader = injector.getInstance(Loader.class);
		idlLoader = injector.getInstance(IDLLoader.class);
		tdLoader = injector.getInstance(TargetDescriptorLoader.class);

		definitionSourceGenerator = injector.getInstance(DefinitionSourceGenerator.class);
		definitionCompiler = injector.getInstance(DefinitionCompiler.class);
		graphInstantiator = injector.getInstance(Instantiator.class);
		graphCompiler = injector.getInstance(GraphCompiler.class);

		executor = injector.getInstance(CompilationCommandExecutor.class);
	}

	//override for ext4dpws
	@Override
	public List<Object> compile(final List<Error> errors,
			final List<Error> warnings) throws InvalidCommandLineException {

		if (adlToExecName.size() == 0) {
			throw new InvalidCommandLineException("no definition name is specified.",
					1);
		}

		final List<Object> result = new ArrayList<Object>();
		for (final Map.Entry<String, String> e : adlToExecName.entrySet()) {
			try {
				final String adlName = e.getKey();
				final String specificDUinstance = e.getValue();

				// Compiling a first time
				Definition distributedArchDef= adlLoader.load(adlName, compilerContext);

				Map<Object, Object> contextCopy = new HashMap<Object, Object>(compilerContext);
				List<String> contextCopyLDFlags = new ArrayList<String>(CompilerContextHelper.getLDFlags(compilerContext));
				List<String> contextCopyCFlags = new ArrayList<String>(CompilerContextHelper.getCFlags(compilerContext));


				DeploymentArchitecture annotation = AnnotationHelper.getAnnotation(distributedArchDef, DeploymentArchitecture.class);
				Iterator<DeploymentUnit> it = annotation.getDeploymentUnits().iterator(); 
				DeploymentUnit current = null;

				// When an execName is specified, it's to target a SPECIFIC DeploymentUnit in the DeploymentArchitecture
				// That's why we try to find it
				boolean found = false;
				if(specificDUinstance != null)
				{
					while(it.hasNext()) // try to find the according DU instance name (not type !)
					{
						current = it.next();				

						if(current.getTaggedComponent().getName().equals(adlName))
						{
							compilerContext.clear();
							CompilerContextHelper.getLDFlags(contextCopy).clear();
							CompilerContextHelper.getCFlags(contextCopy).clear();
							CompilerContextHelper.getLDFlags(contextCopy).addAll(contextCopyLDFlags);
							CompilerContextHelper.getCFlags(contextCopy).addAll(contextCopyCFlags);
							compilerContext.putAll(contextCopy);	
							compileDU(current,result);
							found = true;					
							break;
						}
						if(!found)
							System.out.println("Could not find deployment unit named '" 
									+ specificDUinstance 
									+ "' in " 
									+ distributedArchDef.getName() 
									+ " deployment architecure.");
					}
				} else // Build all the DUs
				{
					while(it.hasNext()) 
					{
						current = it.next();	
						compilerContext.clear();
						CompilerContextHelper.getLDFlags(contextCopy).clear();
						CompilerContextHelper.getCFlags(contextCopy).clear();
						CompilerContextHelper.getLDFlags(contextCopy).addAll(contextCopyLDFlags);
						CompilerContextHelper.getCFlags(contextCopy).addAll(contextCopyCFlags);
						compilerContext.putAll(contextCopy);	
						compileDU(current,result);

					}
				}

			} catch (final InterruptedException e1) {
				throw new CompilerError(GenericErrors.INTERNAL_ERROR, e,
						"Interrupted while executing compilation tasks");
			} catch (final ADLException e1) {
				if (!errorManager.getErrors().contains(e1.getError())) {
					// the error has not been logged in the error manager, print it.
					try {
						errorManager.logError(e1.getError());
					} catch (final ADLException e2) {
						// ignore
					}
				}
			}
		}
		if (errors != null) errors.addAll(errorManager.getErrors());
		if (warnings != null) warnings.addAll(errorManager.getWarnings());
		return result;
	}

	// ----------------------------------------------------------------------------
	// Old utility methods from mind-compiler 0.2.3a: We should find an alternative
	// by using the new TargetDescriptorLoader etc 
	// ----------------------------------------------------------------------------

	protected String processContext(final Target targetDesc,
			final String inputADL, final Map<Object, Object> context) {
		processCFlags(targetDesc, context);
		processLdFlags(targetDesc, context);
		processCompiler(targetDesc, context);
		processLinker(targetDesc, context);
		processLinkerScript(targetDesc, context);
		return processADLMapping(targetDesc, inputADL, context);
	}

	protected void processCFlags(final Target target,
			final Map<Object, Object> context) {
		if (target != null && target.getCFlags().length > 0) {
			final Flag[] flags = target.getCFlags();

			final List<String> targetFlags = new ArrayList<String>();
			for (final Flag flag : flags) {
				targetFlags.addAll(DirectiveHelper.splitOptionString(flag.getValue()));
			}

			if (logger.isLoggable(Level.FINE))
				logger.log(Level.FINE, "Adding target c-flags: " + targetFlags);

			CompilerContextHelper.getCFlags(context);
			List<String> contextFlags = CompilerContextHelper.getCFlags(context);
			;
			if (contextFlags == null) {
				contextFlags = new ArrayList<String>();
			}
			contextFlags.addAll(targetFlags);
			CompilerContextHelper.setCFlags(context, contextFlags);
		}
	}

	protected void processLdFlags(final Target target,
			final Map<Object, Object> context) {
		if (target != null && target.getLdFlags().length > 0) {
			final Flag[] flags = target.getLdFlags();

			final List<String> targetFlags = new ArrayList<String>();
			for (final Flag flag : flags) {
				targetFlags.addAll(DirectiveHelper.splitOptionString(flag.getValue()));
			}

			if (logger.isLoggable(Level.FINE))
				logger.log(Level.FINE, "Adding target ld-flags: " + targetFlags);

			List<String> contextFlags = CompilerContextHelper.getLDFlags(context);
			if (contextFlags == null) {
				contextFlags = new ArrayList<String>();
			}
			contextFlags.addAll(targetFlags);
			CompilerContextHelper.setLDFlags(context, contextFlags);
		}
	}

	protected void processCompiler(final Target target,
			final Map<Object, Object> context) {
		final String opt = CompilerContextHelper.getCompilerCommand(context);
		if (opt == CompilerContextHelper.DEFAULT_COMPILER_COMMAND) {
			if (target != null && target.getCompiler() != null) {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "Using target compiler : "
							+ target.getCompiler().getPath());
				}
				CompilerContextHelper.setCompilerCommand(context, target.getCompiler()
						.getPath());
			}/* else {
				// Note while porting to 2.1 compiler:
				// Such stuff is now handled by CompilationCommandOptionHandler that is
				// ran while handling compiler init and all options handling.
				// This means the info should ALREADY have been filled, that's why I
				// decided to comment this section

				// TODO: check if the logic is OK
				CompilerContextHelper.setCompilerCommand(context,
						compilerCmdOpt.getDefaultValue());
			}*/
		}
	}

	protected void processLinker(final Target target,
			final Map<Object, Object> context) {
		final String opt = CompilerContextHelper.getLinkerCommand(context);
		if (opt == CompilerContextHelper.DEFAULT_LINKER_COMMAND) {
			if (target != null && target.getLinker() != null) {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "Using target linker : "
							+ target.getLinker().getPath());
				}
				CompilerContextHelper.setLinkerCommand(context, target.getLinker()
						.getPath());
			}/* else {
				// Note while porting to 2.1 compiler:
				// Such stuff is now handled by CompilationCommandOptionHandler that is
				// ran while handling compiler init and all options handling.
				// This means the info should ALREADY have been filled, that's why I
				// decided to comment this section

				// TODO: check if the logic is OK
				CompilerContextHelper.setLinkerCommand(context,
						linkerCmdOpt.getDefaultValue());
			}*/
		}
	}

	protected void processLinkerScript(final Target target,
			final Map<Object, Object> context) {
		if (target != null) {
			final String opt = CompilerContextHelper.getLinkerScript(context);
			if (opt == null && target.getLinkerScript() != null) {
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "Using target linker script : "
							+ target.getLinkerScript().getPath());
				}
				CompilerContextHelper.setLinkerScript(context, target.getLinkerScript()
						.getPath());
			}
		}
	}

	protected String processADLMapping(final Target target,
			final String inputADL, final Map<Object, Object> context) {
		if (target != null) {
			final ADLMapping mapping = target.getAdlMapping();
			if (mapping == null) return inputADL;

			if (mapping.getOutputName() != null) {
				final String outputName = mapping.getOutputName().replace(
						"${inputADL}", inputADL);
				if (logger.isLoggable(Level.FINE)) {
					logger.log(Level.FINE, "Compiling ADL : " + outputName);
				}
				CompilerContextHelper.setExecutableName(context, outputName);
			}

			return mapping.getMapping().replace("${inputADL}", inputADL);
		} else {
			return inputADL;
		}
	}

	// End old utility methods

	// TODO: This was VERY quickly ported and the processContext handling (3 times for different sources)
	// wasn't really understood/changed at all: it will compile but the logic MAY NOT BE OK according to
	// 2.1 compiler (reminder: original code depended on the 0.2.3a compiler)
	private void compileDU(DeploymentUnit du,final List<Object> result) throws ADLException, InterruptedException
	{
		String depUnitName = null;
		Target middlTargetDescriptor = null;
		Target userTargetDescriptor = null;
		Target cliTargetDescriptor = null;

		Definition currentDef = du.getTopLevelDefinition();
		CompilerContextHelper.setExecutableName(compilerContext, du.execName);

		// compute DU-specific out-path from the command-line out-path
		File duBuildDir = new File(OutPathOptionHandler.getOutPath(compilerContext),"build_" + du.execName);
		duBuildDir.mkdirs();
		compilerContext.put(BasicOutputFileLocator.OUTPUT_DIR_CONTEXT_KEY, duBuildDir);


		String compiler = CompilerContextHelper.getCompilerCommand(compilerContext);
		String linker = CompilerContextHelper.getLinkerCommand(compilerContext);

		userTargetDescriptor = buildTargetDescriptor(du.userDescriptor, compilerContext);
		depUnitName = processContext(userTargetDescriptor, currentDef.getName(), compilerContext);
		compiler = CompilerContextHelper.getCompilerCommand(compilerContext);
		linker = CompilerContextHelper.getLinkerCommand(compilerContext);

		System.out.println("Deployment unit compiler = " + compiler);
		System.out.println("Deployment unit  linker = " + linker);

		middlTargetDescriptor = buildTargetDescriptor(du.dpwsDescriptor, compilerContext);
		depUnitName = processContext(middlTargetDescriptor, currentDef.getName(), compilerContext);	

		cliTargetDescriptor = buildTargetDescriptor(TargetDescriptorOptionHandler.getTargetDescriptor(compilerContext), compilerContext);
		depUnitName = processContext(cliTargetDescriptor, currentDef.getName(), compilerContext);

		// SSZ: TODO: Fix: depUnitName was NOT computed the right way during migration
		//currentDef = adlLoader.load(depUnitName, compilerContext);

		//debug
		//		if(logger.getLevel() ==  Level.FINEST)
		//		{
		try {
			Ext4DPWSASTHelper.dumpComponent(currentDef, idlLoader, adlLoader,depUnitName + ".adl", compilerContext);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//		}

		CompilationStage stage = StageOptionHandler.getCompilationStage(compilerContext);

		if (stage == CompilationStage.CHECK_ADL) {
			result.add(currentDef);
			return;
		}

		if (stage == CompilationStage.GENERATE_SRC) {
			definitionSourceGenerator.visit(currentDef, compilerContext);
			return;
		}

		if (stage == CompilationStage.COMPILE_DEF) {
			final Collection<CompilationCommand> commands = definitionCompiler.visit(
					currentDef, compilerContext);
			executor.exec(commands, compilerContext);
			for (final CompilationCommand command : commands) {
				if (command instanceof CompilerCommand) {
					result.addAll(command.getOutputFiles());
				}
			}
			return;
		}

		final ComponentGraph graph = graphInstantiator.instantiate(currentDef,
				compilerContext);
		final Collection<CompilationCommand> commands = graphCompiler.visit(graph,
				compilerContext);
		executor.exec(commands, compilerContext);
		for (final CompilationCommand command : commands) 
		{       
			if (command instanceof LinkerCommand) 
			{
				result.addAll(command.getOutputFiles());
			}
		}
	}

	//added
	protected Target buildTargetDescriptor(String targetDesc, Map<Object,Object>context) 
	{
		Target result = null;
		// load target descriptor (if any)
		try {
			if (targetDesc != null) {
				try {
					result = tdLoader.load(targetDesc, context);
				} catch (final ADLException e) {
					logger.log(Level.FINE, "Error while loading target descriptor", e);
					throw new InvalidCommandLineException(
							"Unable to load target descriptor: " + e.getMessage(), 1);
				}
			}
			if (result != null && result.getLinkerScript() != null) {
				ClassLoader sourceClassLoader = (ClassLoader) context.get("classloader");
				final URL linkerScriptURL = sourceClassLoader
						.getResource(result.getLinkerScript().getPath());
				if (linkerScriptURL == null) {
					throw new InvalidCommandLineException("Invalid linker script: '"
							+ result.getLinkerScript().getPath()
							+ "'. Cannot find file in the source path", 1);
				}
				result.getLinkerScript().setPath(linkerScriptURL.getPath());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void printUsage(final PrintStream ps) {
		final String prgName = System.getProperty(PROGRAM_NAME_PROPERTY_NAME,
				getClass().getName());
		ps.println("Usage: " + prgName + " [OPTIONS] (<deployment-arch-definition>[:<duName>])+");
		ps.println("  where <deployment-arch-definition> is the name of the deployment architecture composite component to"
				+ " be compiled, ");
		ps
		.println("  and <duName> is the optional name of the deployement unit component to compile.");
	}


	public static void main(final String... args) {

		final Launcher l = new Launcher();		

		try {
			l.init(args);
			l.compile(null, null);
		} catch (final InvalidCommandLineException e) {
			l.handleException(e);
		}
	}

}
