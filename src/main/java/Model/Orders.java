package Model;

/**
 * Class containing information about orders(client name, product name, orderItem id).
 */
public class Orders {
    private String client_name;
    private String product_name;
    private int orderItem_id;

    /**
     * Constructor
     * @param client_name
     * @param product_name- S
     * @param orderItem_id
     */
    public Orders(String client_name, String product_name, int orderItem_id) {
        this.client_name = client_name;
        this.product_name = product_name;
        this.orderItem_id = orderItem_id;
    }

    /**
     * No parameter constructor. Initialises all values
     * to null, respectively to 0.
     */
    public Orders() {
        client_name = null;
        product_name = null;
        orderItem_id = 0;
    }

    /**
     * Setter method for the client name field.
     * @param client_name String containing the name of the client.
     */
    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    /**
     * Setter method for the product name field.
     * @param product_name String containing the name of the product
     */
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    /**
     * Setter method for the orderItem id field.
     * @param orderItem_id integer representing the orderItem id.
     */
    public void setOrderItem_id(int orderItem_id) {
        this.orderItem_id = orderItem_id;
    }

    /**
     * Getter method for the client name field.
     * @return String containing the name of the client.
     */
    public String getClient_name() {
        return client_name;
    }

    /**
     * Setter method for the product name field.
     * @return String containing the name of the product
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * Getter method for the orderItem id field.
     * @return integer representing the orderItem id.
     */
    public int getOrderItem_id() {
        return orderItem_id;
    }
}
