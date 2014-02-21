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

import java.util.ArrayList;
import java.util.Collection;

import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.interfaces.Interface;
import org.objectweb.fractal.adl.interfaces.InterfaceContainer;
import org.objectweb.fractal.adl.types.TypeInterfaceUtil;
import org.ow2.mind.adl.annotation.predefined.RemoteInterface;
import org.ow2.mind.adl.annotation.predefined.RemoteReference;
import org.ow2.mind.annotation.AnnotationHelper;

/**
 * Utility class for distribution. 
 * @author ecoly (edine.coly@sogeti.com)
 *
 */
public final class ProcessorsHelper {

	/**
	 * Default private constructor.
	 */
	private ProcessorsHelper()
	{

	}

		
	/**
	 * Get remote interfaces of a deployment unit definition.
	 * @param definition : The deployment unit definition.
	 * @return : A collection server interfaces tagged with the @RemoteInterface annotation.
	 */
	public static Collection<Interface> getRemoteInterfaces(Definition definition)
	{

		Collection<Interface> result = new ArrayList<Interface>();

		if (definition instanceof InterfaceContainer)
		{

			RemoteInterface	remoteInterface = null;		

			for (Interface currentItf : ((InterfaceContainer)definition).getInterfaces())
			{
				if (TypeInterfaceUtil.isClient(currentItf)) //don't use RemoteReference for the moment
				{
					continue;
				}
				remoteInterface = AnnotationHelper.getAnnotation(currentItf, RemoteInterface.class);
				if (remoteInterface != null)
				{
					result.add(currentItf);
				}				
			} 
		}
		return result;
	}
	
	/**
	 * Get the remote reference of a deployment unit definition.
	 * @param definition : The deployment unit definition.
	 * @return : A collection client interfaces tagged with the @RemoteReference annotation.
	 */
	public static Collection<Interface> getRemoteReferences(Definition definition)
	{

		Collection<Interface> result = new ArrayList<Interface>();

		if (definition instanceof InterfaceContainer)
		{

			RemoteReference	remoteReference = null;		

			for (Interface currentItf : ((InterfaceContainer)definition).getInterfaces())
			{
				if (TypeInterfaceUtil.isServer(currentItf)) //don't use RemoteReference for the moment
				{
					continue;
				}
				remoteReference = AnnotationHelper.getAnnotation(currentItf, RemoteReference.class);
				if (remoteReference != null)
				{
					result.add(currentItf);
				}
				
			} 
		}
		return result;
	}
}
