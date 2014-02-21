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

package org.ow2.mind.extensions.ext4dpws.common;

import static org.objectweb.fractal.adl.error.ErrorTemplateValidator.validErrorTemplate;

import org.objectweb.fractal.adl.error.ErrorTemplate;


/**
 *  ADL error template.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public enum Ext4DPWSADLErrors implements ErrorTemplate {

	/**
	 * 
	 */
	INVALID_DEPL_ARCH_NO_COMPOSITE("Invalid deployment architecure: \"%s\" must be a composite definition.",
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_DEPL_ARCH_NO_DEFINITION("Invalid deployment architecure: \"@DeploymentUnit\" is not set on a composite definition.",
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_DEPL_UNIT_NOT_TAGGED("Invalid deployment unit: \"%s\" is not tagged with @DeploymentUnit.",
	"<adl name>"),
	/***
	 * 
	 */
	INVALID_DEPL_UNIT_TAGGED_MAIN("Invalid deployment unit definition: \"%s\" Main interface must not be tagged \"@RemoteInterface\" nor \"@RemoteReference\".",
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_DEPL_UNIT_NO_COMPONENT("Invalid deployment unit: \"%s\" is not a component instance." 
			+ "@DeploymentUnit annotation should be used above 'contains' keyword.",
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_DEPL_UNIT_NO_MAIN("Invalid deployment unit: \"%s\" should provides \"boot.Main\"." ,
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_BINDING_NOT_TAGGED_INTERFACE("Invalid binding : \"%s\" declaration must be tagged with \"%s\".",
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_BINDING_SELECTED_NOT_FOUND("Invalid binding. \"%s\" type is not found for \"%s\" server interface.",
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_SOAP_BINDING("Invalid binding. \"%s\" soap binding is not valid or not yet implemented for \"%s\" interface. Available mode are RPC_ENCODED.",
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_BINDING_NO_DEP_UNIT("Invalid binding. Component implies in remote binding must be tagged with \"@DeploymentUnit\" annotation-->%s",
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_DEFINITION_NOT_TAGGED("Invalid definition : %s must be tagged with \"%s\"",
	"<adl name>"),
	/**
	 * 
	 */
	INVALID_TARGET_DESCRIPTOR("Invalid target descriptor \"%s\". Possible values are : \"%s\", \"%s\" .",
	"<adl name>"),
		/**
	 * 
	 */
	INVALID_REMOTE_REF_IS_OPTIONAL("Invalid remote reference : remote reference interfaces cannot be declared optional.",
	"<adl name>"), 
	;


	/** The groupId of ErrorTemplates defined in this enumeration. */
	public static final String GROUP_ID = "MADL";

	private int                id;
	private String             format;

	private Ext4DPWSADLErrors(final String format, final Object... args) {
		this.id = ordinal();
		this.format = format;

		assert validErrorTemplate(this, args);
	}

	public int getErrorId() {
		return id;
	}

	public String getGroupId() {
		return GROUP_ID;
	}

	public String getFormatedMessage(final Object... args) {
		return String.format(format, args);
	}

	public String getFormat() {
		return format;
	}

}
