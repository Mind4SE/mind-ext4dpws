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

package org.ow2.mind.extensions.ext4dpws.wsdl2cpl.core.impl.dpws;

public interface Constants {


	String GSOAP_FILE_EXT = ".gsoap";
	
	String TYPEMAP_FILE_EXT = ".dat";
	
	String NSMAP_FILE_EXT = ".nsmap";
	
	String DC_MSG_MALLOC_TEMPLATE = "$VAR$ = ($TYPE$*)DC_MSG_MALLOC(0, dpws, sizeof($TYPE$));\n";
	
	String MALLOC_TEMPLATE = "$VAR$ = ($TYPE$*)dcpl_malloc(sizeof($TYPE$)*$LENGTH$);\n";
	
	String DCPL_STRDUP_TEMPLATE = "(char *)dcpl_strdup((const char *)$VAR$);\n";
}
