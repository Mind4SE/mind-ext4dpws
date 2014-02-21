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

import org.objectweb.fractal.adl.ADLException;
import org.objectweb.fractal.adl.Node;
import org.objectweb.fractal.adl.error.Error;
import org.objectweb.fractal.adl.error.ErrorLocator;
import org.objectweb.fractal.adl.error.ErrorTemplate;
import org.objectweb.fractal.adl.error.NodeErrorLocator;



/**
 * Throw this exception if IDL is not in line with distribution requirements.
 *
 *@author Edine Coly
 *@contact (mind-members@lists.minalogic.net)
 *
 */
public class InvalidIDLException extends ADLException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3852918540813343040L;

	private final Error error;

	/**
	 * Constructs a new {@link ADLException}.
	 * 
	 * @param template the error templates.
	 * @param args the arguments for the formated message (see
	 *            {@link ErrorTemplate#getFormatedMessage(Object...)}).
	 */
	public InvalidIDLException(final ErrorTemplate template, final Object... args) {
		this(template, null, null, args);
	}

	/**
	 * Constructs a new {@link ADLException}.
	 * 
	 * @param template the error templates.
	 * @param node the error location.
	 * @param args the arguments for the formated message (see
	 *            {@link ErrorTemplate#getFormatedMessage(Object...)}).
	 */
	public InvalidIDLException(final ErrorTemplate template, final Node node,
			final Object... args) {
		this(template, new NodeErrorLocator(node), null, args);
	}

	/**
	 * Constructs a new {@link ADLException}.
	 * 
	 * @param template the error templates.
	 * @param locator the error location. May be <code>null</code>.
	 * @param args the arguments for the formated message (see
	 *            {@link ErrorTemplate#getFormatedMessage(Object...)}).
	 */
	public InvalidIDLException(final ErrorTemplate template, final ErrorLocator locator,
			final Object... args) {
		this(template, locator, null, args);
	}

	/**
	 * Constructs a new {@link ADLException}.
	 * 
	 * @param template the error templates.
	 * @param cause the cause of this error. May be <code>null</code>.
	 * @param args the arguments for the formated message (see
	 *            {@link ErrorTemplate#getFormatedMessage(Object...)}).
	 */
	public InvalidIDLException(final ErrorTemplate template, final Throwable cause,
			final Object... args) {
		this(template, null, cause, args);
	}

	/**
	 * Constructs a new {@link ADLException}.
	 * 
	 * @param template the error templates.
	 * @param locator the error location. May be <code>null</code>.
	 * @param cause the cause of this error. May be <code>null</code>.
	 * @param args the arguments for the formated message (see
	 *            {@link ErrorTemplate#getFormatedMessage(Object...)}).
	 */
	public InvalidIDLException(final ErrorTemplate template, final ErrorLocator locator,
			final Throwable cause, final Object... args) {
		this(new Error(template, locator, cause, args));
	}

	/**
	 * Constructs a new {@link ADLException}.
	 * 
	 * @param error the error reported by this exception
	 */
	public InvalidIDLException(final Error error) {
		super(error);
		this.error = error;
	}



	/**
	 * Returns the {@link Error} object reported by this exception.
	 * 
	 * @return the {@link Error} object reported by this exception.
	 */
	public Error getError() {
		return error;
	}

	@Override
	public String getMessage() {
		return error.toString();
	}


}
