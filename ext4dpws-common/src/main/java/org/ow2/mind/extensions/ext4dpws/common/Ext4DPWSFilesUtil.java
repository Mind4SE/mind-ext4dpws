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

import java.io.File;

/**
 * Some utility functions to manipulate files.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public final class Ext4DPWSFilesUtil {

	/**
	 * Default constructor.
	 */
	private Ext4DPWSFilesUtil()
	{
		
	}
	/**
	 * Get a file name without his extension.
	 * @param file The input file.
	 * @return The input file name without extension.
	 */
	public static String getFileNameWithoutExtension(File file) {


		String result = "";

		file.getName();

		int whereDot = file.getName().lastIndexOf('.');

		if (0 < whereDot && whereDot <= file.getName().length() - 2) 
		{
			result =  file.getName().substring(0, whereDot);
		}

		return result;

	}
}
