/*
 *      Jalli.java
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
import java.util.LinkedList;
import java.io.File;

/**
 * This is the main class of Jalli.
 * It handles the prompted arguments
 * and calls the read and write methods.
 */
public class Jalli {
	
/* ------------------------------------------------------------------ */
														// Fields
/**
 * The list of the input filenames.
 */	
	private static List<File> filenames;

/* ------------------------------------------------------------------ */
														// constructor
	public Jalli(){
		filenames = new LinkedList<File>();
	}
	
/* ------------------------------------------------------------------ */
														// main-method
/**
 * The main method reads the given arguments
 * and calls the relevant methods.
 * @param args the commandline arguments
 */
	public static void main (String[] args) {

		new Jalli();
		
// we check the correct usage of jalli
		if (args.length < 3) { 
			System.out.println("Usage: java -jar Jalli-<version>.jar <Input> <Output> <InputFile(s)>\nSee readme for more informations.");
			System.exit(0);
		} 

		for(int i=2;i<args.length;i++){
			filenames.add(new File(args[i]));
		}
		
// we read the first argument - the input messenger
// then we call the related class and method
		switch(args[0].hashCode()){
			case 880537848: 	new MCabber().readInputFile(filenames);
								break;
			case -1624392372: 	new Empathy().readInputFile(filenames);
								break;
			case -988451231:	new Pidgin().readInputFile(filenames);
								break;
			default: 			System.out.println("Illegal argument");
								System.exit(1);
		}

// now we read the second argument - the output messenger
// same like above
		switch(args[1].hashCode()){
			case 880537848: 	new MCabber().writeOutputFile();
								break;
			case -1624392372: 	new Empathy().writeOutputFile();
								break;
			case -988451231:	new Pidgin().writeOutputFile();
								break;
			default: 			System.out.println("Illegal argument");
								System.exit(1);
		}
	}
}
