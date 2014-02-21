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
import java.util.List;
import java.util.Map;

import org.ow2.mind.compilation.CompilerContextHelper;

/**
 * Utility class used to manipulate current mind compiler context.
 * (for example add new include path during compilation process. Useful to add
 * stubs/skeletons headers during compilation process)
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public final class Ext4DPWSCompilerContextHelper {

	private Ext4DPWSCompilerContextHelper()
	{
				
	}
	
	/**
	 * Update the CFLags by adding the newIncDir path to the include path (-I).
	 * @param newIncDir : The new include directory.
	 * @param context : The current mind compiler context.
	 */
	public static void updateIncludeCFlags(File newIncDir, Map<Object, Object> context)
	{
		
	    List<String> contextFlags = CompilerContextHelper.getCFlags(context);
	    contextFlags.add("-I");
	    contextFlags.add(newIncDir.getAbsolutePath());
	    CompilerContextHelper.setCFlags(context, contextFlags);
	}
}
