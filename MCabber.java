/*
 * 		MCabber.java
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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * Handles logs of mcabber messenger. 
 */
public class MCabber extends Messenger{

/* ------------------------------------------------------------------ */
															// Fields
/**
 * The jabber id of the user
 */
	private String jid;
	
/**
 * A free selectable nickname for the user
 * , e.g. the first name.
 */ 
	private String nickname;
	
/**
 * A free selectable nickname for the chat partner
 * , e.g. the first name.
 */ 
	private String buddyName;
	
/* ------------------------------------------------------------------ */
															// Input			
/**
 * Reads additional informations about the chat.
 * MCabber doesn't	provide any details about the 
 * chatting people. So we have to ask the user for
 * the additional, essential infos (jid and the
 * nicknames).
 */
	public void readAdditionalInfos(){
		try{
			do{
				System.out.print("Your JID? [nick@jabber-server.tld] ");
				BufferedReader br1 = new BufferedReader( new InputStreamReader(System.in));
				this.jid = br1.readLine();
			}
			while(!jid.contains("@") || !jid.contains("."));

			System.out.print("Your name? (not your jid) ");
			BufferedReader br2 = new BufferedReader( new InputStreamReader(System.in));
			this.nickname = br2.readLine();

			System.out.print("Name of the chatpartner? (not the jid) ");
			BufferedReader br3 = new BufferedReader( new InputStreamReader(System.in));
			this.buddyName = br3.readLine();
		}
		catch(IOException e){
			System.err.println("I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
			e.printStackTrace();
			System.exit(1);
		}		
	}

/* ------------------------------------------------------------------ */
															// Input
	public void readInputFile(List<File> filenames){
		this.readAdditionalInfos();	

		for(int i=0; i<filenames.size();i++){
		try{
			BufferedReader buff = new BufferedReader(new FileReader(filenames.get(i)));
			String line;
			while (( line = buff.readLine()) != null) {

// we only read send or received messages, no status messages	
				if ((line.startsWith("MS") || line.startsWith("MR"))){

					String[] sParam = line.split(" ",4);
					String[] dateTime = sParam[1].split("T");
					
					Message a = new Message();
					a.setTime(dateTime[1].replaceAll("Z",""));
					a.setDate(new Integer(dateTime[0]));
					a.setMessage(sParam[3]);
					
// sent messages					
					if (line.startsWith("MS")){
						a.setIsUser(true);
						a.setBuddyID(jid);
						a.setBuddyName(this.nickname);
					}

// received messages				
					else{
						a.setIsUser(false);
						a.setBuddyName(this.buddyName);

// this isn't quite beautiful, but we hope that the user didn't changed the standard filenames of mcabber...
						a.setBuddyID(filenames.get(i).getName());
					}
					Cache.getInstance().addMessage(a);
				}
			}
		}
		catch(FileNotFoundException e){
			System.err.println( "File " + filenames.get(i).getName() + " not found!");
			System.exit(1);
		}
		catch(IOException e){
			System.err.println("I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
			e.printStackTrace();
			System.exit(1);
		}
		}
	}
	
/* ------------------------------------------------------------------ */
															// Output												
	public void writeOutputFile(){

		Writer fw = null;
		File filename = null;
		if(Cache.getInstance().getNumberAllMsg()!=0){
		
// we look for the name of the chat buddy and use it for the filename		
		for(int i=0;i<Cache.getInstance().getNumberAllMsg();i++){
			if(Cache.getInstance().getMessage(i).getIsUser()==false){
				filename = new File(Cache.getInstance().getMessage(i).getBuddyID());
				break;
			}
		};
		try{

// if we didn't found a buddy nick, e.g. because there's only one message of the user
// we have to ask for the name.	
			if(filename == null){
				do{
					System.out.print("The JID of your chat partner? [nick@jabber-server.tld] ");
					BufferedReader br1 = new BufferedReader( new InputStreamReader(System.in));
					filename = new File(br1.readLine());
				}
				while(!filename.getName().contains("@") || !filename.getName().contains("."));
			};
		
// if the filename already exists, we have to append the messages to the existing file
			if(filename.exists())
				fw = new FileWriter(filename,true);
			
// otherwise we create the new one			
			else
				fw = new FileWriter(filename);

			for(int i=0;i<Cache.getInstance().getNumberAllMsg();i++){
				if(Cache.getInstance().getMessage(i).getIsUser()==true)
					fw.write("MS ");
				else
					fw.write("MR ");
				fw.append(Cache.getInstance().getMessage(i).getDate()+"T"+Cache.getInstance().getMessage(i).getTime()+"Z 000 "+Cache.getInstance().getMessage(i).getMessageText());
				fw.append(System.getProperty("line.separator") );
			}
			System.out.println("Log succesfully converted to " + filename);
		}
		catch(IOException e){
			System.err.println( "I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
								e.printStackTrace();
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
	}
	else
		System.out.println("There are no messages to convert!");
	}

/* ------------------------------------------------------------------ */
														// main-method
	public static void main (String args[]) {		
	}
}
