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
 * IDL error template.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public enum Ext4DPWSIDLErrors implements ErrorTemplate {

	/**
	 * 
	 */
	INVALID_IDL_NO_OPERATION("Invalid IDL : To be remotable IDL must provides at least one operation.",
	"<idl name>"),
	
	INVALID_IDL_UNSUPPORTED_TYPE_OR_DEC("Invalid IDL : Unsupported type for parameter '%s'. Please read the user guide for supported types and declarations.",
	"<idl name>"),
	
	INVALID_IDL_TYPE_DEF_NOT_FOUND("Invalid IDL : '%s' definition not found.",
	"<idl name>"),

	INVALID_IDL_RETURN_TYPE ("Invalid IDL : Return type must be void.",
	"<idl name>"), 
	
	INVALID_IDL_MUST_INCLUDE_ERRORS_IDT ("Invalid IDL : Must include %s.",
	"<idl name>"), 
	;
	
	/** The groupId of ErrorTemplates defined in this enumeration. */
	public static final String GROUP_ID = "MADL";

	private int                id;
	private String             format;

	private Ext4DPWSIDLErrors(final String format, final Object... args) {
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
