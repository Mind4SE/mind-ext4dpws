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
 * Code generation error template.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public enum Ext4DPWSIOError implements ErrorTemplate {

	/**
	 * 
	 */
	GEN_IO_ERR("Error occurs during files generation process for %s : %s",
	"<adl name>"),
	;

	/** The groupId of ErrorTemplates defined in this enumeration. */
	public static final String GROUP_ID = "MADL";

	private int                id;
	private String             format;

	private Ext4DPWSIOError(final String format, final Object... args) {
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
