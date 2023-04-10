package pj4;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Seller extends User
{
    private Customer[] customerList;

    private ArrayList<Store> listOfStores;

    private static final long serialVersionUID = 6387730094462016452L;
    private String storeName;


    public Seller(String name, String email, String password) {
        super(name, email, password);
        listOfStores = new ArrayList<Store>();
    }

    public void addStore(String storeName) throws IllegalArgumentException {
        ArrayList<Store> allStores = Store.getAllStores();

        for (Store temp : allStores) {
            if (temp.getStoreName().equals(storeName))
                throw new IllegalArgumentException("Duplicate Store Name");
        }

        Store st = new Store(this, storeName);
        listOfStores.add(st);
    }


    public void setCustomerList(Customer[] customerList) {
        this.customerList = customerList;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public ArrayList<Store> getListOfStores() {
        return listOfStores;
    }

    public void addCustomer(Customer c) {
        ArrayList<Customer> arr = new ArrayList<>();
        for (int i = 0; i < customerList.length; i++) {
            arr.set(i, this.customerList[i]);
        }

        arr.add(c);

        for (int i = 0; i < arr.size(); i++) {
            this.customerList[i] = arr.get(i);
        }
    }

    public Messenger2 createConversation(Customer c1, Store st){
        Messenger2 c = new Messenger2(c1, this, st);
        return c;
    }

    public Customer getCustomer(int index){
        File f = new File("accounts.ser");
        int count = 1;
/*
        for(User u: this.listUsers(f, this.getNumCreated())){
            if(u instanceof Customer) {
                if(!this.getBlockedUsers().contains(u)) {
                    if(!this.getBlockedBy().contains(u)) {
                        if(!u.getInvisibleTo().contains(this)){
                            if (count == index) {
                                return (Customer) u;
                            }
                            count++;
                        }
                    }
                }
            }
        }
*/
        return null;
    }

    /*public String searchCustomer(int customerID) {
        String customerEmail = "Customer Not Found";
        for (int i = 0; i < customerList.length; i++) {
            if (customerList[i].getCustomerID() == (customerID)) {
                customerEmail = customerList[i].getEmail();
            }
        }
        return customerEmail;
    }

     */
    public void displayCustomers(){
        File f = new File("accounts.ser");
        int count = 1;
        for(User u: getUsers()){
            if(u instanceof Customer){
                if(!this.getBlockedUsers().contains(u)) {
/*
                    if(!this.getBlockedBy().contains(u)) {
                        if(!u.getInvisibleTo().contains(this)) {
                            System.out.println(count++ + ". " + u.getEmail());
                        }
                    }
*/
                }
            }
        }
    }

    public boolean equals(Object o){
        if(o instanceof Seller) {
            Seller seller = (Seller) o;
            if(seller.getEmail().equals(this.getEmail()) && seller.getPassword().equals(this.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public boolean canMessage(Object o) {
        if (o instanceof Seller || o instanceof Customer) {
            return true;
        }
        return false;
    }


    // add equals
//        public Message newMessage() //in progress
//        public Message newMessage() //in progress


}
