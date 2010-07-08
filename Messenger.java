/*
 *      Messenger.java
 * 
 * 		Copyright 2009 Marcus Nitzschke <kenda@fedoraproject.org>
 *   
 * 	This file is part of Jalli.
 *
 *  Jalli is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  Jalli is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Jalli. If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.List;
import java.io.File;

/**
 * This is the abstract class of all sub-messengers.
 * Normally there are only the two methods for read-in
 * and write-out
 */
public abstract class Messenger {
	
	public Messenger(){
	}
	
/**
 * reads the input file, seperates the important
 * information and adds this to the cache.
 * @param filenames the input files
 */
	protected abstract void readInputFile(List<File> filenames);

/**
 * reads the necessary data from the cache and
 * writes it to the new logfile
 */
	protected abstract void writeOutputFile();

	public static void main (String args[]) {		
		
	}
}
