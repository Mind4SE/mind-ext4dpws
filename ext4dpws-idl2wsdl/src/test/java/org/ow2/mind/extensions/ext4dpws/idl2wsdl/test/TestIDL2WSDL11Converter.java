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

package org.ow2.mind.extensions.ext4dpws.idl2wsdl.test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.ow2.mind.CommonBackendModule;
import org.ow2.mind.CommonFrontendModule;
import org.ow2.mind.extensions.ext4dpws.common.ProcessLauncher;
import org.ow2.mind.extensions.ext4dpws.idl2wsdl.core.IDL2WSDL11Converter;
import org.ow2.mind.idl.IDLBackendModule;
import org.ow2.mind.idl.IDLFrontendModule;
import org.ow2.mind.idl.IDLLoader;
import org.ow2.mind.idl.ast.InterfaceDefinition;
import org.ow2.mind.plugin.PluginLoaderModule;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
/**
 * 
 * Comment: Convert IDL file into WSDL 1.1 Document.  
 * Authors: Edine Coly
 * Contact: mind-members@lists.minalogic.net
 * Contributors: Stephane Seyvoz
 */
public final class TestIDL2WSDL11Converter {


	private static final String TESTA_ITF = "TestA";

	private static final String TESTB_ITF = "org.objectweb.fractal.mind.extensions.ext4dpws.test.idl2wsdl.TestB";

	private static final File genWSDLOutputFolder = new File("src/test/generated-wsdl/");

	private static final File genGSOAPOutputFolder = new File("src/test/generated-gsoap/");

	private static final File cFilesOutputFolder = new File("src/test/generated-c/");

	private static final String WSDL2H_CMD = "wsdl2h -c -o$OUTPUT_FILE$ $WSDL_FILE$";

	private static final String SOAPCPP2_CMD = "soapcpp2 -2ucnxL -p$PREFIX$ $GSOAP_FILE$";

	@Inject
	private IDLLoader idlLoader;

	protected void setUp() throws Exception {
		final Injector injector = Guice.createInjector(new CommonFrontendModule(),
				new IDLFrontendModule(), new PluginLoaderModule(),
				new CommonBackendModule(), new IDLBackendModule());
		idlLoader = injector.getInstance(IDLLoader.class);
	}

	@Test(groups = {"functional"})
	public void testA()
	{

		try {

			if (!genGSOAPOutputFolder.exists())
			{
				genGSOAPOutputFolder.mkdirs();
			}

			if (!genWSDLOutputFolder.exists())
			{
				genWSDLOutputFolder.mkdirs();
			}

			if (!cFilesOutputFolder.exists())
			{
				cFilesOutputFolder.mkdirs();
			}
			ClassLoader srcLoader = null;
			File srcDir = new File("src");

			srcLoader = new URLClassLoader(new URL[] {srcDir.toURI().toURL()});	
			Map<Object, Object> context = new HashMap<Object, Object>(); 
			context.put("classloader", srcLoader);
			StringTemplate wsdl2hTemp = new StringTemplate(WSDL2H_CMD);
			InterfaceDefinition test1Def = (InterfaceDefinition) idlLoader.load(TESTA_ITF, context);

			//step 1 generate the WSDL file
			File wsdlFile = new File(genWSDLOutputFolder, "testAResult.wsdl");			
			IDL2WSDL11Converter.idl2wsdl(test1Def, wsdlFile,idlLoader, context);	

			//step 2 generate the GSOAP file
			File gsoapFile = new File(genGSOAPOutputFolder, "testAResult.gsoap");
			wsdl2hTemp.setAttribute("OUTPUT_FILE", gsoapFile.getAbsolutePath());
			wsdl2hTemp.setAttribute("WSDL_FILE", wsdlFile.getAbsolutePath());
			ProcessLauncher wsdl2hLauncher = new ProcessLauncher(System.out, System.out, 5000);
			wsdl2hLauncher.exec(wsdl2hTemp.toString(), null);

			//step 3 generate stub and skeleton files since GSOAP file
			StringTemplate soapcpp2Temp = new StringTemplate(SOAPCPP2_CMD);
			soapcpp2Temp.setAttribute("PREFIX", "testA");
			soapcpp2Temp.setAttribute("GSOAP_FILE", gsoapFile.getAbsolutePath());
			ProcessLauncher soapcpp2Launcher = new ProcessLauncher(System.out, System.out, 5000);
			soapcpp2Launcher.exec(soapcpp2Temp.toString(), null, cFilesOutputFolder);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());			
		}
	}


	@Test(groups = {"functional"})
	public void testB()
	{

		try {

			ClassLoader srcLoader = null;
			File srcDir = new File("src");

			srcLoader = new URLClassLoader(new URL[] {srcDir.toURI().toURL()});	
			Map<Object, Object> context = new HashMap<Object, Object>(); 
			context.put("classloader", srcLoader);
			StringTemplate wsdl2hTemp = new StringTemplate(WSDL2H_CMD);
			InterfaceDefinition test1Def = (InterfaceDefinition) idlLoader.load(TESTB_ITF, context);

			//step 1 generate the WSDL file
			File wsdlFile = new File(genWSDLOutputFolder, "testBResult.wsdl");			
			IDL2WSDL11Converter.idl2wsdl(test1Def, wsdlFile, idlLoader,context);	

			//step 2 generate the GSOAP file
			File gsoapFile = new File(genGSOAPOutputFolder, "testBResult.gsoap");
			wsdl2hTemp.setAttribute("OUTPUT_FILE", gsoapFile.getAbsolutePath());
			wsdl2hTemp.setAttribute("WSDL_FILE", wsdlFile.getAbsolutePath());
			ProcessLauncher wsdl2hLauncher = new ProcessLauncher(System.out, System.out, 5000);
			wsdl2hLauncher.exec(wsdl2hTemp.toString(), null);

			//step 3 generate stub and skeleton files since GSOAP file
			StringTemplate soapcpp2Temp = new StringTemplate(SOAPCPP2_CMD);
			soapcpp2Temp.setAttribute("PREFIX", "testB");
			soapcpp2Temp.setAttribute("GSOAP_FILE", gsoapFile.getAbsolutePath());
			ProcessLauncher soapcpp2Launcher = new ProcessLauncher(System.out, System.out, 5000);
			soapcpp2Launcher.exec(soapcpp2Temp.toString(), null, cFilesOutputFolder);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, e.getMessage());			
		}
	}

}
