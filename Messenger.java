package pj4;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Messenger {
	private static final String FILENAME = "messages.ser";
	private static ArrayList<Message> messages = null;

	public static ArrayList<Message> getMessages() {
		if (Messenger.messages != null)
			return Messenger.messages;

		ArrayList<Message> temp = new ArrayList<Message>();

		try {
			File f = new File(FILENAME);
			FileInputStream fis;
			ObjectInputStream ois;

			try {
				fis = new FileInputStream(f);
				ois = new ObjectInputStream(fis);

				while (true) {
					Message m = (Message) ois.readObject();
					temp.add(m);
				}
			} catch (EOFException ex) {
			} catch (FileNotFoundException ex2) {
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Messenger.messages = temp;
		return Messenger.messages;
	}

	public static void sendNewMessage(User sender, User receiver, String message, Boolean disappearing) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String dateString = dtf.format(LocalDateTime.now());

		Message m1 = new Message(sender, sender, receiver, message, disappearing, dateString);
		m1.setRead(true);
		Message m2 = new Message(receiver, sender, receiver, message, disappearing, dateString);

		ArrayList<Message> temp = Messenger.getMessages();
		temp.add(m1);
		temp.add(m2);
		Messenger.writeMessages();
	}

	public static void writeMessages() {
		try {
			File f = new File(Messenger.FILENAME);
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			for (Message m : Messenger.messages) {
				oos.writeObject(m);
			}
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Message> getMessagesForUser(User u) {
		ArrayList<Message> temp = Messenger.getMessages();
		ArrayList<Message> results = new ArrayList<Message>();
		for (Message m : temp) {
			if (m.getMessageOwner().equals(u)) {
				User other;
				if (!m.getSender().equals(u))
					other = m.getSender();
				else
					other = m.getReceiver();

				if (!(other.getBlockedUsers().contains(u) || u.getBlockedUsers().contains(other)))
					results.add(m);
			}
		}
		return results;
	}

	public static void editMessage(Message m, String newMessage) {
		StringBuilder sb = new StringBuilder(m.getMessage());
		sb.append("\n");
		sb.append(newMessage);

		Message rel = Messenger.findRelatedMessage(m);
		m.setMessage(sb.toString());
		m.setRead(true);
		if (rel != null) {
			rel.setMessage(sb.toString());
			rel.setRead(false);
		}
		Messenger.writeMessages();
	}

	private static Message findRelatedMessage(Message m) {
		ArrayList<Message> temp = Messenger.getMessages();
		for (Message rel : temp) {
			if (rel.related(m))
				return rel;
		}
		return null;
	}

	public static void deleteMessage(Message m) {
		ArrayList<Message> temp = Messenger.getMessages();
		temp.remove(m);
		writeMessages();
	}

	public static void deleteMessagesForUser(User user) {
		ArrayList<Message> temp = Messenger.getMessages();
		ArrayList<Message> removeList = new ArrayList<Message>();

		for (Message m : temp) {
			if (m.getSender().equals(user) || m.getReceiver().equals(user))
				removeList.add(m);
		}

		for (Message m : removeList) {
			temp.remove(m);
		}
		writeMessages();
	}

	public static boolean existsUnreadMessagesForUser(User user) {
		ArrayList<Message> temp = Messenger.getMessages();
		for (Message m : temp) {
			if (!m.isRead())
				return true;
		}
		return false;
	}

}

//	public Messenger(Customer cust, Seller vendor) throws UserNotFoundException {
//
//		//verify exists
//
//		//check blocked
//
//		this.cust = cust;
//		this.vendor = vendor;
//		cust2vend = new ArrayList<String>();
//		vend2cust = new ArrayList<String>();
//
//	}
//
//	public void send(String message, User name) throws AccessDeniedException {
//
//		if (name.equals(cust)) {
//
//		} else if (name.equals(vendor)) {
//
//		} else
//			throw new AccessDeniedException("The user does not have access to this conversation.");
//
//	}
//
//	public void edit(String message, User name) throws AccessDeniedException {
//
//		if (name.equals(cust)) {
//
//		} else if (name.equals(vendor)) {
//
//		} else
//			throw new AccessDeniedException("The user does not have access to this conversation.");
//
//	}
//
//	public void delete(String message, User name) throws AccessDeniedException {
//
//		if (name.equals(cust)) {
//
//		} else if (name.equals(vendor)) {
//
//		} else
//			throw new AccessDeniedException("The user does not have access to this conversation.");
//
//	}
//
//	public Customer getCustomer(){
//		return cust;
//	}
//
//	public Seller getSeller(){
//		return vendor;
//	}
//
//	public String toString(User name) /*throws AccessDeniedException {
//
//		if (name.equals(cust)) {
//
//		} else if (name.equals(vendor)) {
//
//		} /*else
//			throw new AccessDeniedException("The user does not have access to this conversation log.");
//		return "to be implemented";
//	}
//
//	public ArrayList<String> getMessageList(User name) /*throws AccessDeniedException {
//
//		if (name.equals(cust)) {
//
//		} else if (name.equals(vendor)) {
//
//		} /*else
//			throw new AccessDeniedException("The user does not have access to this conversation log.");
//		return new ArrayList<String>();
//	}
//}
//
