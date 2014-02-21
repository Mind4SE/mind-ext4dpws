/**
 * ================================================================================
 *
 *   					 Copyright (C) 2010 Sogeti High Tech
 *						 Copyright (C) 2014 Schneider-Electric
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

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.Node;
import org.ow2.mind.adl.annotation.ADLLoaderAnnotationProcessor;
import org.ow2.mind.adl.annotation.ADLLoaderPhase;
import org.ow2.mind.adl.annotation.AbstractADLLoaderAnnotationProcessor;
import org.ow2.mind.annotation.Annotation;

/**
 * Process the @DeploymentArchitecure annotation.
 * @author ecoly (edine.coly@sogeti.com)
 * 
 * @contributor sseyvoz (sseyvoz@assystem.com)
 * Contribution: Ported to 2.1 Compiler.
 */
public class RemoteReferenceProcessor extends AbstractADLLoaderAnnotationProcessor
implements
ADLLoaderAnnotationProcessor {

	/**
	 * Info logger.
	 */
	//private static Logger logger = FractalADLLogManager.getLogger(DeploymentUnit.VALUE);

	public Definition processAnnotation(Annotation annotation, Node node,
			Definition definition, ADLLoaderPhase phase,
			Map<Object, Object> context) throws ADLException {

		/**
		 * Moved to DeploymentArchitecureProcessor#processRemoteReference() since as it changed
		 * the interface signature (in the clone) of clients, and servers weren't edited
		 * accordingly, the binding checker raised an error (here we were too early in the process,
		 * compared to legacy 0.2.3a compiler).
		 */

		return definition;
	}	
	

}
