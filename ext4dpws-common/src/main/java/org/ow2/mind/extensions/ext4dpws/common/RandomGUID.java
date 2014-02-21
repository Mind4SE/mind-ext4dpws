/*******************************************************************************
 * 
 *  This file belongs to the SODA distribution.
 *  Copyright (C) 2004-2007 Schneider Electric.
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307.
 *  You can also get it at http://www.gnu.org/licenses/lgpl.html
 *  
 *  For more information on this software, see http://www.soda-itea.org.
 *******************************************************************************/
package org.ow2.mind.extensions.ext4dpws.common; //change package declaration for mindext4dpws project (ecoly)

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class RandomGUID extends Object {

	private String valueAfterMD5;

	private static Random myRand;

	private static SecureRandom mySecureRand;

	private static String s_id;

	static {
		mySecureRand = new SecureRandom();
		long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);
		try {
			s_id = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	public RandomGUID() {
		MessageDigest md5 = null;

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getClass().getName() + ": " + e.getMessage());
		}

		try {
			long time = System.currentTimeMillis();
			long rand = 0;

			rand = myRand.nextLong();

			StringBuffer sb = new StringBuffer();
			sb.append(s_id);
			sb.append(":");
			sb.append(Long.toString(time));
			sb.append(":");
			sb.append(Long.toString(rand));

			md5.update(sb.toString().getBytes());

			byte[] array = md5.digest();
			sb.setLength(0);
			for (int j = 0; j < array.length; ++j) {
				int b = array[j] & 0xFF;
				if (b < 0x10)
					sb.append('0');
				sb.append(Integer.toHexString(b));
			}

			valueAfterMD5 = sb.toString();

		} catch (Exception e) {
			throw new RuntimeException(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/*
	 * Convert to the standard format for GUID (Useful for SQL Server UniqueIdentifiers, etc.) Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
	 */
	public String toString() {
		String raw = valueAfterMD5.toLowerCase();
		StringBuffer sb = new StringBuffer();
		sb.append(raw.substring(0, 8));
		sb.append("-");
		sb.append(raw.substring(8, 12));
		sb.append("-");
		sb.append(raw.substring(12, 16));
		sb.append("-");
		sb.append(raw.substring(16, 20));
		sb.append("-");
		sb.append(raw.substring(20));
		return sb.toString();
	}

}
