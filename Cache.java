/*
 * 		Cache.java
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

import java.util.LinkedList;

/**
 * Here are all messages stored in a list.
 * You can get access by using the singleton 
 * instance.
 **/
public class Cache {

/* ------------------------------------------------------------------ */
														// attributes
/**
 * List where all messages are stored inside.
 **/
	private LinkedList<Message> messages;

/**
 * The number of messages with the same dates. 
 * More details: @see calcNumberMessagesSameDate()
 **/
	private int numberMessagesSameDate = 0;
	
/**
 * The number of how many different dates we have
 * in our messages.
 * More details: @see calcDifferentDates()
 **/
	private int diffDates = 0;
	
/**
 * The single instance of the cache. Use the getter to
 * get access to the cache.
 **/
	private static Cache instance;
	
/* ------------------------------------------------------------------ */
														// constructor
/**
 * Simple constructor, which initiates the messages list.
 **/	
	private Cache(){
		messages = new LinkedList<Message>();
	}
	
/* ------------------------------------------------------------------ */
														// service-methods
/**
 * This is the method to work with the Singleton Cache.
 * So we can't instantiate a second cache.
 * @return the only instance of Cache
 **/
	public static Cache getInstance() {
          if (instance == null) {
              instance = new Cache();
          }
          return instance;
      }

/**
 * This method calculates the number of messages
 * with the SAME date. This is important because e.g.
 * mcabber saves all data in one single file and so we
 * have to extract the different dates. The relevant number
 * is set to 
 * @see #numberMessagesSameDate
 */
  	public void calcNumberMessagesSameDate(){
		int count = 1;
		if(this.messages.size() == 1)
			this.numberMessagesSameDate = 1;
		else
			for(int i=0;i<this.messages.size()-1;i++){
				if(this.messages.get(i).date.equals(this.messages.get(i+1).date))
					count++;
				else break;
			};
		this.numberMessagesSameDate = count;
	}

/**
 * This method calculates how many different dates we have.
 * This is important, to know how many different files we
 * have to create. The relevant number is set to
 * @see #diffDates
 */
	public void calcDifferentDates(){
		int count = 1;
		if(this.messages.size() == 1) this.diffDates = 1;
		else
			for(int i=0;i<this.messages.size()-1;i++){
				if(!this.messages.get(i).date.equals(this.messages.get(i+1).date))
					count++;
			}
		this.diffDates = count;
	}

/* ------------------------------------------------------------------ */
														// getter + list access
/**
 * Returns how many messages with the same date were in the log file.
 * @return number of messages with the same date
 */
	public int getDiffDates(){
		return this.diffDates;
	}

/**
 * Returns how many different dates were in the log file.
 * @return number of different dates
 */
	public int getNumberMsgDiffDates(){
		return this.numberMessagesSameDate;
	}
	
/**
 * Returns whether there are elements in the list or not.
 * @return true, if the messages list is empty
 */	
	public boolean messageIsEmpty(){
		return this.messages.isEmpty();
	}

/**
 * Returns how many messages we have.
 * @return number of all messages in the list
 * or rather the size of the list
 */
	public int getNumberAllMsg(){
		return this.messages.size();
	}
	
/**
 * Returns a message of the list.
 * @param pos position of the message in the list
 * @return message at the specified position
 * in the list
 */
	public Message getMessage(int pos){
		return this.messages.get(pos);
	}		
	
/**
 * Removes the message at the specified position
 * in the list.
 * @param pos position of the message in the list
 */
	public void removeMessage(int pos){
		this.messages.remove(pos);
	}
	
/**
 * Adds the message at the specified position
 * in the list.
 * @param m the message to add
 */
	public void addMessage(Message m){
		this.messages.add(m);
	}
	
/* ------------------------------------------------------------------ */
														// main-method
	public static void main (String args[]) {		
	}
}
