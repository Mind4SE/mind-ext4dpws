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

import java.util.HashMap;
import java.util.Map;

import org.ow2.mind.adl.annotation.ADLAnnotationTarget;
import org.ow2.mind.adl.annotation.ADLLoaderPhase;
import org.ow2.mind.adl.annotation.ADLLoaderProcessor;
import org.ow2.mind.adl.ast.Component;
import org.ow2.mind.annotation.Annotation;
import org.ow2.mind.annotation.AnnotationTarget;
import org.ow2.mind.ext4dpws.adl.processor.RemoteInterfaceProcessor;
import org.ow2.mind.idl.ast.InterfaceDefinition;

@ADLLoaderProcessor(processor = RemoteInterfaceProcessor.class, phases = {ADLLoaderPhase.AFTER_CHECKING})
public class RemoteInterface implements Annotation {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8794352576488407341L;

	private static final AnnotationTarget[] ANNOTATION_TARGETS = {ADLAnnotationTarget.INTERFACE};

	public static final String VALUE = "@RemoteInterface";
	
	//@AnnotationElement(hasDefaultValue=true)
	private String serviceId = null;  
	
	//one skeleton component per remote interface and per deployment it instance 
	private transient Map<String,Component> skeletons;
	
	//stub component that should be used to communicate with this interface
	//one per deployment unit
	private transient Map<String,Component> stubs;
	
	private static transient Map<String, InterfaceDefinition> clonedDefinition = new HashMap<String, InterfaceDefinition>();
	
	public RemoteInterface() {
		skeletons = new HashMap<String, Component>();
		stubs = new HashMap<String, Component>();
	}
	
	public AnnotationTarget[] getAnnotationTargets() {
		// TODO Auto-generated method stub
		return ANNOTATION_TARGETS;
	}

	public boolean isInherited() {
		// TODO Auto-generated method stub
		return false;
	}


	public String toString()
	{
		return "@RemoteInterface";
	}

	public void addSkeleton(String depUnitId, Component skeleton) {
		skeletons.put(depUnitId, skeleton);
	}
	
	public Component getSkeleton(String depUnitId){
		return this.skeletons.get(depUnitId);		
	}
	
	public void addStub(String depUnitId, Component stub) {
		stubs.put(depUnitId, stub);
	}
	
	public Component getStub(String depUnitId){
		return this.stubs.get(depUnitId);
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public void addClonedDefinition(InterfaceDefinition interfaceDefinition, InterfaceDefinition clone)
	{
		clonedDefinition.put(interfaceDefinition.getName(), clone);
	}
	
	public InterfaceDefinition getClonedInterfaceDefinition(InterfaceDefinition interfaceDefinition)
	{
		return clonedDefinition.get(interfaceDefinition.getName());
	}
}
