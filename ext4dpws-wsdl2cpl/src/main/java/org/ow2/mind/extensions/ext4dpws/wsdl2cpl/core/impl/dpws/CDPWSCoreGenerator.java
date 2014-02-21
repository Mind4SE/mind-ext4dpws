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

package org.ow2.mind.extensions.ext4dpws.wsdl2cpl.core.impl.dpws;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.ow2.mind.extensions.ext4dpws.common.InvalidIDLException;
import org.ow2.mind.extensions.ext4dpws.common.ProcessLauncher;
import org.ow2.mind.extensions.ext4dpws.common.StringTemplateHelper;
import org.ow2.mind.extensions.ext4dpws.idl2wsdl.core.IDL2WSDL11Converter;
import org.ow2.mind.idl.IDLLoader;
import org.ow2.mind.idl.ast.InterfaceDefinition;


/**
 * This class generates all stubs and skeleton C files from a WSDL definition using 
 * wsdl1h and soapcpp2 tools.
 * This include the WSDL file, files generated from 'wsdl2h' and 'soapcpp2', and skeleton/stubs ADL files.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public final class CDPWSCoreGenerator {


	private static int NS = 0;	

	/**
	 * Map of WSDL files per interface interface definition.
	 */
	private static final Map<String, File> wsdlGeneratedFiles = new HashMap<String, File>();

	private static final Map<String, String> servicesNamespaceURI = new HashMap<String, String>();
	


	/**
	 * Default constructor.
	 */
	private CDPWSCoreGenerator() 
	{		

	}


	/**
	 * Generate files from a interface. (wsdl, wsdl2h, soapcpp2)
	 * @param itfDefinition The mind interface definition.
	 * @param outDir The output directory where files should be generated.
	 * @return A Map containing all generated files or templates. Each files can be get using the corresponding file key. (see static field of CDPWSCoreGenerator class) 
	 * @throws IOException 
	 * @throws InvalidIDLException 
	 */
	public static synchronized Map<String, Object> generateFiles(InterfaceDefinition itfDefinition, File outDir, IDLLoader idlLoaderitf, Map<Object, Object> context) throws InvalidIDLException, IOException {

		Map<String, Object> result = new HashMap<String, Object>(11);

		String prefix = "ns" + ++NS;

		if (!outDir.exists() && !outDir.mkdirs())
		{
			throw new IOException("Cannot create " + outDir.getAbsolutePath());
		}
		
		// step 1 generate the WSDL file.
		File wsdlFile = wsdlGeneratedFiles.get(itfDefinition.getName());
		String serviceNamespaceURI = servicesNamespaceURI.get(itfDefinition.getName());

		if(wsdlFile == null)
		{
			wsdlFile = new File(outDir, prefix + ".wsdl");
			Map<String, Object> conversionResult = IDL2WSDL11Converter.idl2wsdl(itfDefinition, wsdlFile, idlLoaderitf, context);
			serviceNamespaceURI = (String) conversionResult.get("serviceNamespaceURI");
			wsdlGeneratedFiles.put(itfDefinition.getName(), wsdlFile);
			servicesNamespaceURI.put(itfDefinition.getName(), serviceNamespaceURI);
		}

		//step 2 create the typemap file
		StringTemplate template = StringTemplateHelper.instanceOfTypesMapping();

		File typemapFile = new File(outDir, prefix + Constants.TYPEMAP_FILE_EXT);
		FileWriter fr = new FileWriter(typemapFile);
		BufferedWriter writer = new BufferedWriter(fr);
		template.setAttribute(STConstants.$SERVICE_NS_PREFIX$, prefix);
		template.setAttribute(STConstants.$NAMESPACE_URI$, serviceNamespaceURI);
		writer.write(template.toString());
		writer.close();			

		result.put(STConstants.$SERVICE_NS_PREFIX$, prefix);

		//step 3 generate the .gsoap file						
		File gsoapFile = new File(outDir.getAbsolutePath() + File.pathSeparator + prefix + Constants.GSOAP_FILE_EXT);	

		String [] wsdl2hCmd = {"wsdl2h"
						,"-t" + prefix + Constants.TYPEMAP_FILE_EXT
						,"-c"
						,"-o" + gsoapFile.getAbsolutePath()
						,wsdlFile.getAbsolutePath()
		};

		ProcessLauncher wsdl2hLauncher = new ProcessLauncher (System.out, System.out, 5000);
		wsdl2hLauncher.exec(wsdl2hCmd, null, outDir);

		//step 4 soapcpp2
		String [] soapcpp2Cmd = {"soapcpp2"
								,"-2ucnxL"
								,"-p" + prefix
								,gsoapFile.getAbsolutePath()
		};

		ProcessLauncher soapcpp2Launcher = new ProcessLauncher(System.out, System.out, 5000);
		soapcpp2Launcher.exec(soapcpp2Cmd, null, outDir);

		File nsmapFile = new File(outDir.getAbsolutePath() + "/" + prefix + Constants.NSMAP_FILE_EXT);

		template = new StringTemplate(STConstants.GSOAP_MARSH_DEMARSH_HEADER);
		template.setAttribute(STConstants.$SERVICE_NS_PREFIX$, prefix);
		File marshHeader = new File(outDir.getAbsolutePath() + "/" + template.toString());

		template = new StringTemplate(STConstants.GSOAP_MARSH_DEMARSH_IMPL);
		template.setAttribute(STConstants.$SERVICE_NS_PREFIX$, prefix);
		File marshImpl = new File(outDir.getAbsolutePath() + "/" + template.toString());

		template = new StringTemplate(STConstants.GSOAP_SKELETON_CODE);
		template.setAttribute(STConstants.$SERVICE_NS_PREFIX$, prefix);
		File gsoapSkeletonCode = new File(outDir.getAbsolutePath() + "/" + template.toString());

		template = new StringTemplate(STConstants.GSOAP_STUB_CODE);
		template.setAttribute(STConstants.$SERVICE_NS_PREFIX$, prefix);
		File gsoapStubCode = new File(outDir.getAbsolutePath() + "/" + template.toString());

		template = new StringTemplate(STConstants.GSOAP_STUB_HEADER);
		template.setAttribute(STConstants.$SERVICE_NS_PREFIX$, prefix);
		File gsoapStubHeaderFile = new File(outDir.getAbsolutePath() + "/" + template.toString());

		//all the generated files are returned in the map
		result.put(Constants.GSOAP_FILE_EXT, gsoapFile);
		result.put(Constants.TYPEMAP_FILE_EXT, typemapFile);
		result.put(Constants.NSMAP_FILE_EXT, nsmapFile);
		result.put(STConstants.GSOAP_MARSH_DEMARSH_HEADER, marshHeader);
		result.put(STConstants.GSOAP_MARSH_DEMARSH_IMPL, marshImpl);
		result.put(STConstants.GSOAP_SKELETON_CODE, gsoapSkeletonCode);
		result.put(STConstants.GSOAP_STUB_CODE, gsoapStubCode);
		result.put(STConstants.GSOAP_STUB_HEADER, gsoapStubHeaderFile);

		return result;
	}	

}
