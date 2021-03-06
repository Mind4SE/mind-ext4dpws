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

package org.ow2.mind.adl.annotation.predefined;

import org.ow2.mind.adl.annotation.ADLAnnotationTarget;
import org.ow2.mind.adl.annotation.ADLLoaderPhase;
import org.ow2.mind.adl.annotation.ADLLoaderProcessor;
import org.ow2.mind.annotation.Annotation;
import org.ow2.mind.annotation.AnnotationTarget;
import org.ow2.mind.ext4dpws.adl.processor.RemoteReferenceProcessor;
import org.ow2.mind.idl.ast.InterfaceDefinition;

@ADLLoaderProcessor(processor = RemoteReferenceProcessor.class, phases = {ADLLoaderPhase.AFTER_EXTENDS})
public class RemoteReference implements Annotation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9081740561837586541L;

	private static final AnnotationTarget[] ANNOTATION_TARGETS = {ADLAnnotationTarget.INTERFACE};

	public static final String VALUE = "@RemoteReference";
	
	public InterfaceDefinition stubInterfaceDefinition;
	
	public AnnotationTarget[] getAnnotationTargets() {
		return ANNOTATION_TARGETS;
	}

	public boolean isInherited() {
		return false;
	}

	public String toString()
	{
		return "@RemoteReference";
	}

}
