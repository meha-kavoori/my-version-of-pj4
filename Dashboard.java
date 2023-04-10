package pj4;
import java.util.*;

public class Dashboard {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the messaging platform!");
        boolean invalidinput = true;
        while(invalidinput){
            System.out.printf("Would you like to:\n1. Create an Account\n2. Sign Into an Account\n");
            int input = Integer.parseInt(scanner.nextLine());
            if(input == 1) {
                invalidinput = createAccount(scanner);
            } else if (input == 2) {
                //Sign In To an Account
                invalidinput = false;
                System.out.println("What is your email address?");
                String email = scanner.nextLine();
                System.out.println("What is your password?");
                String password = scanner.nextLine();
                User user = User.signIn(email, password);
                if(user != null) {
                    if (Messenger.existsUnreadMessagesForUser(user))
                        System.out.println("You have NEW messages!!!");

                    boolean cont = true;
                    do {
                        System.out.println("\nWhat action would you like to take:");
                        if (user instanceof Customer) {
                            cont = customerMenu(scanner, (Customer) user);
                        } else if (user instanceof Seller) {
                            cont = sellerMenu(scanner, (Seller) user);
                        }
                    } while(cont);
                } else if (user == null){
                    System.out.println("Incorrect user id or password");
                }

            } else {
                System.out.println("Invalid Input");
                invalidinput = true;
            }
        }

    }

    private static boolean sellerMenu(Scanner scanner, Seller seller) {
        System.out.printf("1. Send a new message\n2. View messages\n3. Edit message\n4. Delete message\n" +
                "5. Block a User\n6. View Store Statistics\n7. Create a Store\n8. Delete Account\n9. Exit\n");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1: {
                sendNewMailSeller(scanner, seller);
                break;
            }
            case 2: {
                viewMessages(scanner, seller);
                break;
            }
            case 3: {
                editMessage(scanner, seller);
                break;
            }
            case 4: {
                deleteMessage(scanner, seller);
                break;
            }
            case 5: {
                blockUser(scanner, seller);
                break;
            }
            case 6: {
                System.out.println("Would you like to sort the Statistics?");
                System.out.println("1. Yes\n2. No");
                int sortStats = scanner.nextInt();
                //scanner.nextLine();

                // ADD STORE STATS IMPLEMENTATION
                //seller = (Seller) user;
                Store store = new Store(seller, seller.getStoreName());

                scanner.nextLine();
                if (sortStats == 1) {
                    store.printSellerDashboardSorted(seller);
                } else if (sortStats == 2) {
                    store.printSellerDashboard(seller);
                }


                // end of added
                break;
            }
            case 7 : {
                addStore(scanner, seller);
                break;
            }
            case 8 : {
                boolean deleted = deleteAccount(scanner, seller);
                return !deleted;
            }
            case 9: {
                return false;
                //break;
            }
        }
        return true;
    }

    public static boolean customerMenu(Scanner scanner, Customer c) {
        System.out.printf("1. Send a new message\n2. View messages\n3. Edit message\n4. Delete message\n" +
                "5. Block a User\n6. View Store Statistics\n7. Delete Account\n8. Exit\n");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1: {
                sendNewMailCustomer(scanner, c);
                break;
            }
            case 2: {
                viewMessages(scanner, c);
                break;
            }
            case 3: {
                editMessage(scanner, c);
                break;
            }
            case 4: {
                deleteMessage(scanner, c);
                break;
            }
            case 5: {
                blockUser(scanner, c);
                break;
            }
            case 6: {
                System.out.println("Would you like to sort the Statistics?");
                System.out.println("1. Yes%n2. No");
                int sortStats = scanner.nextInt();
                scanner.nextLine();

                // newly added
                /*
                Seller seller = new Seller("","","","");

                Store store = new Store(seller, seller.getStoreName());
                if (sortStats == 1) {
                    store.printCustomerDashboardSorted(c);
                } else if (sortStats == 2) {
                    store.printCustomerDashboard(c);
                }
                // added finish

                 */
                break;
            }
            case 7: {
                boolean deleted = deleteAccount(scanner, c);
                return !deleted;
                //break;
            }
            case 8: {
                return false;
                //break;
            }
        }
        return true;
    }

    private static void viewMessages (Scanner scanner, User user) {
        ArrayList<Message> msgs = Messenger.getMessagesForUser(user);

        if (msgs.size() == 0)
            System.out.println("There are no messages to view");
        else {
            printMessages(msgs);

            while (true)
            {
                System.out.println("Which message would you like to view:");
                int i = scanner.nextInt();
                scanner.nextLine();
                if ((i > msgs.size()) || (i <= 0)) {
                    System.out.println("Invalid Input");
                    continue;
                } else {
                    System.out.println(msgs.get(i - 1).getMessage());
                    msgs.get(i - 1).setRead(true);
                    Messenger.writeMessages();
                    break;
                }
            }
        }
    }

    private static void printMessages(ArrayList<Message> msgs) {
        Collections.sort(msgs);
        int count = 1;
        System.out.printf("%3s %15s %15s %3s\n", "Num", "Sender", "Receiver", "New");
        for (Message x : msgs) {
            System.out.printf("%3d %15s %15s %3s\n", count++, x.getSender().getName(),
                    x.getReceiver().getName(), x.isRead() ? "N" : "Y");
        }
    }

    private static void deleteMessage (Scanner scanner, User user) {
        ArrayList<Message> msgs = Messenger.getMessagesForUser(user);

        if (msgs.size() == 0)
            System.out.println("There are no messages to view");
        else {
            printMessages(msgs);

            while (true)
            {
                System.out.println("Which message would you like to delete:");
                int i = scanner.nextInt();
                scanner.nextLine();
                if ((i > msgs.size()) || (i <= 0)) {
                    System.out.println("Invalid Input");
                    continue;
                } else {
                    Messenger.deleteMessage(msgs.get(i - 1));
                    break;
                }
            }
        }
    }

    private static void editMessage (Scanner scanner, User user) {
        ArrayList<Message> msgs = Messenger.getMessagesForUser(user);

        if (msgs.size() == 0)
            System.out.println("There are no messages to view");
        else {
            printMessages(msgs);

            while (true)
            {
                System.out.println("Which message would you like to edit:");
                int i = scanner.nextInt();
                scanner.nextLine();
                if ((i > msgs.size()) || (i <= 0)) {
                    System.out.println("Invalid Input");
                    continue;
                } else {
                    System.out.println(msgs.get(i - 1).getMessage());

                    System.out.println("Please type your message:");
                    String message = scanner.nextLine();
                    message = "[" + user.getName() + "] " + message;

                    Messenger.editMessage(msgs.get(i - 1), message);
                    System.out.println("Your message has been edited!");
                    break;
                }
            }
        }
    }

    private static void sendNewMailCustomer(Scanner scanner, User user) {
        int choice;
        System.out.println("Would you like to:");
        System.out.printf("1. Select a store to message\n2. Search for a seller to message\n");
        choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1: {
                ArrayList<Store> stores = Store.getAllStoresForUser(user);
                if (stores.size() == 0)
                    System.out.println("No stores exist to message");
                else {
                    int count = 1;
                    for (Store st : stores)
                        System.out.println(count++ + ". " + st.getStoreName());

                    while (true) {
                        System.out.printf("Please select a store to message:\n");
                        int num = scanner.nextInt();
                        scanner.nextLine();

                        if ((num > stores.size()) || (num <= 0)) {
                            System.out.println("Invalid Input");
                            continue;
                        } else {
                            System.out.println("What message would you like to send?");
                            String message = scanner.nextLine();
                            message = "[" + user.getName() + "] " + message;

                            Messenger.sendNewMessage(user, stores.get(num - 1).getSeller(), message, false);
                            System.out.println("Your message is sent! Thank you.");
                            break;
                        }
                    }
                }
                break;
            }
            case 2: {
                System.out.println("Please enter seller you are searching for:");
                String sellerSearch = scanner.nextLine();
                ArrayList<User> users = User.searchSellerByUser(sellerSearch, user);
                if (users.size() == 0)
                    System.out.println("No sellers match");
                else {
                    int count = 1;
                    for (User temp : users)
                        System.out.println(count++ + ". " + temp.getEmail());

                    while (true) {
                        System.out.printf("Please select a seller to message:\n");
                        int num = scanner.nextInt();
                        scanner.nextLine();

                        if ((num > users.size()) || (num <= 0)) {
                            System.out.println("Invalid Input");
                            continue;
                        } else {
                            System.out.println("What message would you like to send?");
                            String message = scanner.nextLine();
                            message = "[" + user.getName() + "] " + message;

                            Messenger.sendNewMessage(user, users.get(num - 1), message, false);
                            System.out.println("Your message is sent! Thank you.");
                            break;
                        }
                    }
                }
                break;
            }
            default: {
                System.out.println("Invalid Input");
            }
        }
    }

    private static void sendNewMailSeller (Scanner scanner, User user) {
        int choice;
        System.out.println("Would you like to:");
        System.out.printf("1. Select a customer to message\n2. Search for a customer to message\n");
        choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1: {
                ArrayList<User> users = User.getCustomersByUser(user);
                if (users.size() == 0)
                    System.out.println("No customers exist to message");
                else {
                    int count = 1;
                    for (User u : users)
                        System.out.println(count++ + ". " + u.getEmail());

                    while (true) {
                        System.out.printf("Please select a customer to message:\n");
                        int num = scanner.nextInt();
                        scanner.nextLine();

                        if ((num > users.size()) || (num <= 0)) {
                            System.out.println("Invalid Input");
                            continue;
                        } else {
                            System.out.println("What message would you like to send?");
                            String message = scanner.nextLine();
                            message = "[" + user.getName() + "] " + message;

                            Messenger.sendNewMessage(user, users.get(num - 1), message, false);
                            System.out.println("Your message is sent! Thank you.");
                            break;
                        }
                    }
                }
                break;
            }
            case 2: {
                System.out.println("Please enter customer you are searching for:");
                String sellerSearch = scanner.nextLine();
                ArrayList<User> users = User.searchSellerByUser(sellerSearch, user);
                if (users.size() == 0)
                    System.out.println("No customers match");
                else {
                    int count = 1;
                    for (User temp : users)
                        System.out.println(count++ + ". " + temp.getEmail());

                    while (true) {
                        System.out.printf("Please select a customer to message:\n");
                        int num = scanner.nextInt();
                        scanner.nextLine();

                        if ((num > users.size()) || (num <= 0)) {
                            System.out.println("Invalid Input");
                            continue;
                        } else {
                            System.out.println("What message would you like to send?");
                            String message = scanner.nextLine();
                            message = "[" + user.getName() + "] " + message;

                            Messenger.sendNewMessage(user, users.get(num - 1), message, false);
                            System.out.println("Your message is sent! Thank you.");
                        }
                    }
                }
                break;
            }
            default: {
                System.out.println("Invalid Input");
            }
        }
    }

    private static boolean createAccount(Scanner scanner) {
        int input;
        boolean invalidinput;
        //Create an Account
        invalidinput = false;
        System.out.println("What is your name?");
        String name = scanner.nextLine();
        System.out.println("What is your email address?");
        String email = scanner.nextLine();
        System.out.println("What would you like your password to be?");
        String password = scanner.nextLine();
        System.out.printf("Would you like to be:%n1. A Customer%n2. A Seller%n");
        input = Integer.parseInt(scanner.nextLine());
        if(input == 1) {
            //Customer Account
            User.newUser(name, email, password, "",1);
        } else if (input == 2) {
            //Seller Account
            System.out.println("What would you like your first store to be named?");
            String storeName = scanner.nextLine();
            while(storeName.trim().equals("")){
                System.out.println("Please enter a valid store name.");
            }
            try {
                User.newUser(name, email, password, storeName,2);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            invalidinput = true;
        }
        return invalidinput;
    }

    private static void blockUser (Scanner scanner, User user) {
        ArrayList<User> users;

        System.out.println("Please search user you want to block:");
        String search = scanner.nextLine();
        if (user instanceof Customer)
            users = User.searchSellerByUser(search, user);
        else
            users = User.searchCustomerByUser(search, user);

        if (users.size() == 0)
            System.out.println("No users match");
        else {
            int count = 1;
            for (User temp : users)
                System.out.println(count++ + ". " + temp.getEmail());

            System.out.printf("Please select a user to block:\n");
            int num = scanner.nextInt();
            scanner.nextLine();

            user.block(users.get(num-1));
            System.out.println("The block was set!");
        }
    }

    private static boolean deleteAccount (Scanner scanner, User user) {
        while (true) {
            System.out.println("Are you sure you want to delete your account?");
            System.out.println("1. Yes\n2. No");
            int i = scanner.nextInt();
            scanner.nextLine();

            if (i == 1) {
                User.deleteUser(user);
                return true;
            } else if (i == 2) {
                return false;
            } else {
                System.out.println("Invalid Input");
            }
        }
    }

    private static void addStore(Scanner scanner, Seller seller) {
        while (true) {
            System.out.println("What would you like your store to be named?");
            String storeName = scanner.nextLine();
            while (storeName.trim().equals("")) {
                System.out.println("Please enter a valid store name.");
            }
            try {
                seller.addStore(storeName.trim());
                return;
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
