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

import java.util.Collection;
import java.util.Collections;

import org.objectweb.fractal.adl.Definition;
import org.objectweb.fractal.adl.interfaces.Interface;
import org.ow2.mind.adl.annotation.ADLAnnotationTarget;
import org.ow2.mind.adl.annotation.ADLLoaderPhase;
import org.ow2.mind.adl.annotation.ADLLoaderProcessor;
import org.ow2.mind.adl.ast.Component;
import org.ow2.mind.annotation.Annotation;
import org.ow2.mind.annotation.AnnotationElement;
import org.ow2.mind.annotation.AnnotationTarget;
import org.ow2.mind.ext4dpws.adl.processor.DeploymentUnitProcessor;
import org.ow2.mind.extensions.ext4dpws.common.RandomGUID;


@ADLLoaderProcessor(processor = DeploymentUnitProcessor.class, phases = {ADLLoaderPhase.AFTER_CHECKING })
public class DeploymentUnit implements Annotation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3394478316548600528L;

	private static final AnnotationTarget[] ANNOTATION_TARGETS = {ADLAnnotationTarget.COMPONENT};

	public static final String VALUE = "@DeploymentUnit";

	@AnnotationElement(hasDefaultValue=true)
	public String dpwsDescriptor = null;  

	@AnnotationElement(hasDefaultValue=true)
	public String userDescriptor = null;
	
	@AnnotationElement(hasDefaultValue=true)
	public String serverPort = "9867";

	@AnnotationElement(hasDefaultValue=true)
	public final String id = new RandomGUID().toString();

	@AnnotationElement
	public String execName;	
	
	private transient Definition topLevelDefinition;
	
	private transient Component taggedComponent;
	
	private transient Interface main;
	
	private transient Collection<Interface>remoteInterfaces;
	
	private transient Collection<Interface>remoteReferences;

	public DeploymentUnit ()
	{
	}
	
	public AnnotationTarget[] getAnnotationTargets() {
		return ANNOTATION_TARGETS;
	}

	public boolean isInherited() {
		return false;
	}

	public Definition getTopLevelDefinition() {
		return topLevelDefinition;
	}

	public void setTopLevelDefinition(Definition topLevelDefinition) {
		this.topLevelDefinition = topLevelDefinition;
	}

	public Component getTaggedComponent() {
		return taggedComponent;
	}

	public void setTaggedComponent(Component taggedComponent) {
		this.taggedComponent = taggedComponent;
	}

	public Interface getMain() {
		return main;
	}

	public void setMain(Interface main) {
		this.main = main;
	}

	public Collection<Interface> getRemoteInterfaces() {
		return Collections.unmodifiableCollection(remoteInterfaces);
	}

	public void setRemoteInterfaces(Collection<Interface> remoteInterfaces) {
		this.remoteInterfaces = remoteInterfaces;
	}

	public Collection<Interface> getRemoteReferences() {
		return Collections.unmodifiableCollection(remoteReferences);
	}

	public void setRemoteReferences(Collection<Interface> remoteReferences) {
		this.remoteReferences = remoteReferences;
	}
	

}
