/*
 *      Pidgin.java
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.io.Writer;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles logs of Pidgin messenger. 
 */
public class Pidgin extends Messenger{

/* ------------------------------------------------------------------ */
															// Fields
/**
 * Date of the chat session.
 */
	private String date;

/**
 * The user id of the chat buddy.
 */
	private String buddyID;

/**
 * The user id of the user, who owns the logs.
 */
	private String userID;

/**
 * The user name of the user, who owns the logs.
 * This is a free selectable name, e.g. 
 * the first name.
 */
	private String userName;

/* ------------------------------------------------------------------ */
															// Input
	public void readInputFile(List<File> filenames){
		
// read the name of the user, because pidgin doesn't provide it		
		try{
			System.out.print("Your name? (not your id) ");
			BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
			this.userName = br.readLine();
		}
		catch(IOException ioe){
			System.err.println("I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
			ioe.printStackTrace();
			System.exit(1);
		};

// seperate between html and txt logs
		for(int i=0; i<filenames.size();i++){
			if( filenames.get(i).toString().endsWith(".html"))
				this.readInputFileHTML(filenames.get(i));
			else if(filenames.get(i).toString().endsWith(".txt"))
				this.readInputFileTXT(filenames.get(i));
			else{
				System.out.println("Invalid input file!");
				System.exit(1);
			}		
		}		
	}

/** Method for parsing a html log file of Pidgin.
 * @param filename the input file
 */
	private void readInputFileHTML(File filename){
		Writer fw = null;
		File tempLogfile = null;
		try{
			
// we create a temporary file where we convert the html file to the txt logfile			
			tempLogfile = File.createTempFile(filename.getName(), null);
			fw = new FileWriter(tempLogfile);
			BufferedReader buff = new BufferedReader(new FileReader(filename));

// we remove all the html tags and so we get the txt logfile
			String line;
			while ((line = buff.readLine()) != null) {
				String cleanLine = line.replaceAll("<[^>]*>","");
				fw.write(cleanLine);
				fw.append(System.getProperty("line.separator") );
			}
			
// if the jvm exits, we delete the temp file
			tempLogfile.deleteOnExit();
		}
		catch(IOException ioe){
			System.err.println("I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
			ioe.printStackTrace();
			System.exit(1);
		}
		finally { 
			if ( fw != null ) 
				try { fw.close(); }
				catch ( IOException ioe ) {
					System.err.println( "I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
					ioe.printStackTrace();
					System.exit(1);
				} 
		}
		this.readInputFileTXT(tempLogfile);
	}

/** Method for parsing a txt log file of Pidgin.
 * @param filename the input file
 */
	private void readInputFileTXT(File filename){
		String line;
		try{
			BufferedReader buff = new BufferedReader(new FileReader(filename));

			while ((line = buff.readLine()) != null) {
				if (line.startsWith("Conversation")){
					String[] sParts = line.split(" ",13);
					this.buddyID = sParts[2];

					String[] fullUserID = sParts[11].split("/");
					this.userID = fullUserID[0];
					
					String[] dateUgly = filename.getName().split("\\.",3);
					this.date = dateUgly[0].replace("-","");
				}
				else if(line.startsWith("(")){
					String[] messageLine = line.split(" ",3);
					if(messageLine[1].endsWith(":")){
						Message a = new Message();
						a.setMessage(messageLine[2]);
						a.setTime(messageLine[0].replace("(","").replace(")",""));
						a.setDate(new Integer(this.date));
						if(messageLine[1].contains(this.userID)){
							a.setIsUser(true);
							a.setBuddyID(this.userID);
							a.setBuddyName(this.userName);
						}
						else{
							a.setIsUser(false);
							a.setBuddyID(this.buddyID);
							a.setBuddyName(messageLine[1].replace(":",""));
						}
						if(a.getBuddyID() != "")
							Cache.getInstance().addMessage(a);	
					}
				}
			}
		}
		catch(FileNotFoundException e){
			System.err.println( "file " + filename + " not found!");
			System.exit(1);
		}
		catch(IOException ioe){
			System.err.println("I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
			ioe.printStackTrace();
			System.exit(1);
		}
	}
	
/* ------------------------------------------------------------------ */
															// Output
	public void writeOutputFile(){

	if(Cache.getInstance().getNumberAllMsg()!=0){
		Cache.getInstance().calcDifferentDates();
		for(int a=0; a < Cache.getInstance().getDiffDates();a++){

// seperating the user and buddyid
			String userid = null, buddyid = null;

			Cache.getInstance().calcNumberMessagesSameDate();
			for(int i = 0;i < Cache.getInstance().getNumberMsgDiffDates();i++){
				if(Cache.getInstance().getMessage(i).getIsUser()==true)
					userid = Cache.getInstance().getMessage(i).getBuddyID();
				else
					buddyid = Cache.getInstance().getMessage(i).getBuddyID();
			}

			String dateTimeText = null, dateTimeFilename = null;
			try{
	
// parse date/time of sessionstart
				DateFormat inputform = new SimpleDateFormat( "yyyyMMdd HH:mm:ss" ); 
				Date d = inputform.parse(Cache.getInstance().getMessage(0).getDate()+" "+Cache.getInstance().getMessage(0).getTime());
			
// format date/time of sessionstart
				DateFormat outputformText = new SimpleDateFormat( "E d MMM yyyy HH:mm:ss z" ); 
				dateTimeText = outputformText.format(d);
				DateFormat outputformFilename = new SimpleDateFormat( "yyyy-MM-d.HHmmssZz" );
				dateTimeFilename = outputformFilename.format(d);
			}
			catch(ParseException pe){
				System.err.println("Parse Exception. Please contact the author or report this as a bug.\n-------------details-------------");
				pe.printStackTrace();
				System.exit(1);
			};

// starting output
			File filename = new File(dateTimeFilename+".txt");
			FileWriter fw=null;
			try{
				fw = new FileWriter(filename);

// the messaging protocol is missing, but i dont know how to handle this				
				fw.append("Conversation with "+buddyid+" at "+dateTimeText+" on "+userid+"()");
				fw.append(System.getProperty("line.separator") );

				Cache.getInstance().calcNumberMessagesSameDate();

				for(int i = 0;i < Cache.getInstance().getNumberMsgDiffDates();i++){
					fw.append("("+Cache.getInstance().getMessage(0).getTime()+") "+Cache.getInstance().getMessage(0).getBuddyName()+": "+Cache.getInstance().getMessage(0).getMessageText());
					fw.append(System.getProperty("line.separator") );
					Cache.getInstance().removeMessage(0);
				}
				
			}
			catch(IOException ioe){
				System.err.println("I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
				ioe.printStackTrace();
				System.exit(1);
			}
			finally { 
				if ( fw != null ) 
					try { fw.close(); }
					catch ( IOException e ) {
						System.err.println( "I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
						e.printStackTrace();
						System.exit(1);
					} 
			}
			System.out.println("Log succesfully converted to " + filename);
		}
	}
	else
		System.out.println("There are no messages to convert!");
	}
	
	public static void main (String args[]) {		
	}
}
