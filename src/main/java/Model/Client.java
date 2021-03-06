package Model;

/**
 * Holds information about a client(name and address).
 */
public class Client {
    private String name;
    private String address;

    /**
     * Constructor
     * @param name
     * @param address
     */
    public Client(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /**
     * No parameter constructor. Initializes fields as nulls.
     */
    public Client() {
        name = null;
        address = null;
    }

    /**
     * Setter method for the name field
     * @param name String containing the name of the client
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter method for the address field.
     * @param address String containing the address of the client
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter method for the name field.
     * @return String containing the name of the client
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for the address field.
     * @return String containing the address of the client
     */
    public String getAddress() {
        return address;
    }
}
