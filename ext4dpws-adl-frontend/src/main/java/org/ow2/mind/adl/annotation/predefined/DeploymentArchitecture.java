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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.ow2.mind.adl.annotation.ADLAnnotationTarget;
import org.ow2.mind.adl.annotation.ADLLoaderPhase;
import org.ow2.mind.adl.annotation.ADLLoaderProcessor;
import org.ow2.mind.annotation.Annotation;
import org.ow2.mind.annotation.AnnotationTarget;
import org.ow2.mind.ext4dpws.adl.processor.DeploymentArchitecureProcessor;


@ADLLoaderProcessor(processor = DeploymentArchitecureProcessor.class, phases = {ADLLoaderPhase.AFTER_CHECKING})
public class DeploymentArchitecture implements Annotation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8554186898727789051L;

	private static final AnnotationTarget[] ANNOTATION_TARGETS = {ADLAnnotationTarget.DEFINITION};

	public static final String VALUE = "@DeploymentArchitecure";
	
	private Collection<DeploymentUnit> deploymentUnits;
	
	public AnnotationTarget[] getAnnotationTargets() {
		return ANNOTATION_TARGETS;
	}

	public boolean isInherited() {
		return false;
	}
	
	public DeploymentArchitecture()
	{
		this.deploymentUnits = new ArrayList<DeploymentUnit>(); 
	}
	/**
	 * Add a new deployment unit model instance in the deploymentUnits collection.
	 * @param duModel : The new deployment unit model.
	 */
	public void addDeploymentUnit(DeploymentUnit depUnit)
	{
		this.deploymentUnits.add(depUnit);
	}

	/**
	 * Get the deployment units list.
	 * @return : An unmodifiable collection of deployment units model.
	 */
	public Collection<DeploymentUnit> getDeploymentUnits() {
		return Collections.unmodifiableCollection(deploymentUnits);
	}
	
	
}
