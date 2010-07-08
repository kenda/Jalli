/*
 *      Empathy.java
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
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.xml.stream.*;

/**
 * Class to handle logs of empathy messenger.
 */
public class Empathy extends Messenger {
/* ------------------------------------------------------------------ */
															// Input																
	public void readInputFile(List<File> filenames){

	for(int j=0; j<filenames.size();j++){
		try{
			XMLInputFactory factory = XMLInputFactory.newInstance(); 
			XMLStreamReader parser = factory.createXMLStreamReader( new FileInputStream( filenames.get(j) ) ); 
 
			while ( parser.hasNext() ){ 

// parse XML file and check the events
			Message a=null;
				if(parser.getEventType()==XMLStreamConstants.START_ELEMENT){
					if (parser.getLocalName() == "message"){
						a = new Message();
						
// read the attributes of the message tag						
						for ( int i = 0; i < parser.getAttributeCount(); i++ ){	
							switch(parser.getAttributeLocalName(i).hashCode()){
									case 3560141:	String dateTime = parser.getAttributeValue(i);
													String[] sParam = dateTime.split("T");
													a.setTime(sParam[1]);
													a.setDate(new Integer(sParam[0]));
													break;
									case 3355: 		a.setBuddyID(parser.getAttributeValue(i));
													break;
									case 3373707:	a.setBuddyName(parser.getAttributeValue(i));
													break;
									case -1179102219:a.setIsUser(new Boolean(parser.getAttributeValue(i)));
													break;
									default: 		break;
							}
						}
						
// read the text of the message tag						
						a.setMessage(parser.getElementText());
					}
				}
				else if(parser.getEventType()==XMLStreamConstants.END_DOCUMENT)
					parser.close(); 
				
				if(a!=null) Cache.getInstance().addMessage(a);
				parser.next(); 
			}
		}
		catch(FileNotFoundException e)
		{
			System.err.println( "File " + filenames.get(j) + " not found!");
			System.exit(1);
		}
		catch(XMLStreamException e){
			System.err.println("XML parsing error. Please check your Inputfile for correct xml syntax or "+
								"contact the author when you think thats a bug.\n-------------details-------------\n"+e.getLocation());
			e.printStackTrace();
			System.exit(1);
		}
	}
	}		
	
/* ------------------------------------------------------------------ */
															// Output													
	public void writeOutputFile(){

	if(Cache.getInstance().getNumberAllMsg()!=0){
		Cache.getInstance().calcDifferentDates();
		for(int a=0; a < Cache.getInstance().getDiffDates();a++){
			String filename = Cache.getInstance().getMessage(0).getDate()+".log";
			try{
				XMLOutputFactory factory = XMLOutputFactory.newInstance(); 
				XMLStreamWriter writer = factory.createXMLStreamWriter( 
						new FileWriter( filename ) ); 
 
				writer.writeStartDocument("utf-8","1.0"); 
					writer.writeProcessingInstruction("xml-stylesheet type=\"text/xsl\" href=\"empathy-log.xsl\"");
						writer.writeStartElement( "log" );
							Cache.getInstance().calcNumberMessagesSameDate();
							for(int i = 0;i < Cache.getInstance().getNumberMsgDiffDates();i++){
							
								writer.writeStartElement("message");
									writer.writeAttribute( "time", Cache.getInstance().getMessage(0).getDate() +"T" + Cache.getInstance().getMessage(0).getTime());

									writer.writeAttribute( "id", Cache.getInstance().getMessage(0).getBuddyID() );

									writer.writeAttribute( "name", Cache.getInstance().getMessage(0).getBuddyName() );

									writer.writeAttribute( "isuser", Cache.getInstance().getMessage(0).getIsUser().toString() );

									writer.writeAttribute( "type", "normal");

									writer.writeCharacters(Cache.getInstance().getMessage(0).getMessageText());
								writer.writeEndElement();
							Cache.getInstance().removeMessage(0);
							}
							
						writer.writeEndElement();
				writer.writeEndDocument(); 
				writer.close();
			}
			catch(IOException e){
				System.err.println("I/O Exception. Please contact the author or report this as a bug.\n-------------details-------------");
				e.printStackTrace();
				System.exit(1);
			}
			catch(XMLStreamException e){
				System.err.println("XML parsing error. Please contact the author or report this as a bug.\n-------------details-------------\n"
									+e.getLocation());
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println("Log succesfully converted to " + filename);
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
