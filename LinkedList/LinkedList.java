import java.lang.*;
import java.io.*;
import java.util.*;

class ListElement{
	String lastname;
	String firstname;
	ListElement next;
	
	public ListElement(String lastname, String firstname){
		this.lastname = lastname;
		this.firstname = firstname;
	}
}

class List{
	ListElement next;
	
	//inserts in alphabetical order
	void insert(ListElement element){
		if(next == null){
			next = element;
		}
		else{
			
			ListElement temp = next;
			ListElement last = null;
			while(temp != null && element.lastname.compareTo(temp.lastname)>0){

				last = temp;
				temp = temp.next;
			}
			while(temp != null && element.lastname.compareTo(temp.lastname) == 0 && element.firstname.compareTo(temp.firstname) > 0){
				last = temp;
				temp = temp.next;
			}
		
			element.next = temp;
			if(last!=null)last.next = element;
		}
	}
	
	//this insert puts in it in non-alphabetical order
/* 	boolean insert(ListElement element, int index){
		if(next != null)
		{
			//temp is used to hold the current listelement while it is being checked
			ListElement temp = next;
			//last is used to hold the last listelement so that the pointers can be connected
			ListElement last = null;
			for(int i = 0; i < index; i++)
				last = temp;
				if(temp.next == null) return false;
				temp = temp.next;
			}
			last.next = element;
			element.next = temp.next;
			return true;
		}
		else return false;
	} */
	
	//this method deletes a listelement according to the last name, returns successfulness
	boolean delete(String name){
		if(next != null)
		{
			//temp is used to hold the current listelement while it is being checked
			ListElement temp = next;
			//last is used to hold the last listelement so that the pointers can be connected
			ListElement last = null;
			while(temp.lastname != name){ //loop continues until match is found
				last = temp;
				if(temp.next == null) return false;
				temp = temp.next;
			}
			last.next = temp.next;
			return true;
		}
		else return false;
	}
	
	//this method finds and returns a listElement with a specific last name
	ListElement find(String name){
		if(next != null)
		{
			//temp is used to hold the current listelement while it is being checked
			ListElement temp = next;
			//loop continues until match is found
			while(temp.lastname != name){
				if(temp.next==null) return null;
				temp = temp.next;
			} 
			return temp;
		}
		else return null;		
	}	
	
	
	//this method prints out the list, returns successfulness (presence of first element)
	boolean printList(){
		//temp is used to hold the current listelement while it is being checked
		if(next == null) return false;
		ListElement temp = next;
		while(temp != null){ //loop continues until there is none left
			System.out.println(temp.lastname+", "+temp.firstname);
			temp = temp.next;
		}
		return true;		
	}
	
	void reverse(){
		iterate(next);
	} 
	
	ListElement iterate(ListElement temp){
		if(temp.next != null){
			ListElement last = iterate(temp.next);
			if(last==null){
				return null;
			}
			else{
				last.next = temp;
				if(temp.next == last) temp.next = null;
				return temp;
			} 
		}
		next=temp;
		return temp;
	}
	
	//this adds the classlist to the linked list
	void addClass(){
		byte buff[] = new byte[20500];
		String str = new String();
		int length;
				
		try(DataInputStream din = new DataInputStream(new FileInputStream("classlist.txt")))
		{
			length = din.read(buff);
			if(length != -1)
			{
				str = new String(buff,0,length);
			}
			
			boolean done = false;
			while(str != "" & !done)
			{
				String lastname;
				String firstname;
				
				str = str.substring(9,str.length());
				str = str.trim();

				lastname = new String(str.substring(0,str.indexOf(" ")));
				lastname = lastname.trim();
				
				str = str.substring(str.indexOf(" "),str.length());
				str = str.trim();
				firstname = new String(str.substring(0,str.indexOf(" ")));
				firstname = firstname.trim();
				
				ListElement newElement = new ListElement(lastname,firstname);
				insert(newElement);

				if(str.length()>10){
					str = str.substring(str.indexOf("\n"),str.length());
					str = str.trim();
				}	
				else done =true;
				
			}
		}
		catch(IOException exc)
		{
			System.out.println("error opening file");
		}
	}
	
}

class linkedlist{
	
	public static void main(String args[]){
 
		List list1 = new List();
		
		list1.addClass();
		System.out.println("Printing Original:");
		list1.printList();
		
		list1.reverse();
		System.out.println("\nPrinting Reversed:");
		list1.printList();
	}
}