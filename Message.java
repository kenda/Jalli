/*
 *      Message.java
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

/**
 * Class for saving chat messages.
 */
public class Message {

/* ------------------------------------------------------------------ */
														// attributes
/**
 * The time when the message was sent.
 */
	private String time;

/**
 * The date when the message was sent.
 */
	public Integer date;
	
/**
 * The content of the message.
 */
	public String message;
	
/**
 * The name of the person who sent the message.
 */
	private String buddyName;

/**
 * The id of the person who sent the message.
 * E.g. icq number or jabber id.
 */
	private String buddyID;

/**
 * Whether the author of the message is the owner of the logs.
 */
	public Boolean isUser;

/* ------------------------------------------------------------------ */
														// constructor
	public Message(){
	}

/* ------------------------------------------------------------------ */
														// setter

/**
 * Sets the time of the message.
 * @param time the time
 */
	public void setTime(String time){
		this.time=time;
	}

/**
 * Sets the date of the message.
 * @param date the date
 */
	public void setDate(Integer date){
		this.date=date;
	}

/**
 * Sets the content of the message.
 * @param message the content
 */
	public void setMessage(String message){
		this.message=message;
	}

/**
 * Sets the name of the author.
 * @param name the name
 */
	public void setBuddyName(String name){
		this.buddyName=name;
	}

/**
 * Sets the id of the author.
 * @param id the id
 */
	public void setBuddyID(String id){
		this.buddyID=id;
	}

/**
 * Sets whether the author is the owner of the logs.
 * @param isUser true, if the author is the user himself. Otherwise false.
 */
	public void setIsUser(Boolean isUser){
		this.isUser=isUser;
	}

/* ------------------------------------------------------------------ */
														// getter
/**
 * Returns a string in the format hh:mm:ss
 * @return the time when the message was written
 */
	public String getTime(){
		return this.time;
	}

/**
 * Returns a number in the format yyyymmdd
 * @return the date when the message was written
 */
	public Integer getDate(){
		return this.date;
	}

/**
 * Return the content of the message.
 * @return the content
 */
	public String getMessageText(){
		return this.message;
	}

/**
 * Returns the name of the message author.
 * @return the author's name
 */
	public String getBuddyName(){
		return this.buddyName;
	}

/**
 * Returns the id of the message author.
 * @return the author's id
 */
	public String getBuddyID(){
		return this.buddyID;
	}

/**
 * Returns whether the author of the message is the user himself.
 * @return true, if the author is the logs owner. Otherwise false.
 */
	public Boolean getIsUser(){
		return this.isUser;
	}

/* ------------------------------------------------------------------ */
														// service-method
/**
 * Formats a message to a String.
 * @return the message as formatted string
 */
	public String toString(){
		String s = "Datum: "+date+"\nZeit: "+time+"\nMsg: "+message+"\nName: "+buddyName+"\nID: "+buddyID;
		return s;
	}

/* ------------------------------------------------------------------ */
														// main-method
	public static void main (String args[]) {		
	}
}
