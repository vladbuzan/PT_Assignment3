package BusinessLogic;

import DataAccess.*;
import Model.Client;
import Model.Orders;
import Model.OrderItem;
import Model.Product;
import Presentation.PDFGenerator;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class representing a parsed action.
 * Used to determine the next step in the execution.
 */
public class Action {
    private Operation operation;
    private String[] args;

    /**
     * Basic constructor for the Action class
     * @param operation - the next operation in the execution
     * @param args - the arguments for the given operation
     */
    public Action(Operation operation, String[] args) {
        this.operation = operation;
        this.args = args;
    }

    /**
     * Method used to determine the next operation to be executed.
     */
    public void doAction() {
        switch(operation) {
            case InsertClient:
                insertClient();
                break;
            case InsertProduct:
                insertProduct();
                break;
            case DeleteClient:
                deleteClient();
                break;
            case DeleteProduct:
                deleteProduct();
                break;
            case Report:
                report();
                break;
            case Order:
                order();
            default:
                break;
        }
    }

    /**
     * Method used to insert a client into the
     * warehouse database, works by creating a new instance
     * of the Client class, then using a data access object in
     * order to do the actual insert.
     */
    private void insertClient() {
        Client client = new Client(args[0].substring(1), args[1].substring(1));
        ClientDAO clientDAO = new ClientDAO();
        if(clientDAO.getField(client, "name") == null ) {
            clientDAO.insert(client);
        } else {
            System.out.println("Client is already in the database");
        }
    }

    /**
     * Method used to insert a product into the warehouse
     * database. Uses args[] for constructing a new instance of the
     * Product class where args[0] is product name, args[1] is quantity
     * and args[2] is the price. The actual insert is done using a
     * data access object.
     */
    private void insertProduct() {
        Product product = new Product(args[0].trim(), Integer.parseInt(args[1].trim()), Float.parseFloat(args[2].trim()));
        ProductDAO productDAO = new ProductDAO();
        if(productDAO.getField(product, "name") == null) {
            productDAO.insert(product);
        } else {
            int quantity = Integer.parseInt(productDAO.getField(product, "quantity"));
            product.setQuantity(product.getQuantity() + quantity);
            productDAO.update(product, new String[]{"quantity"}, new String[]{"name"});
        }
    }

    /**
     * Method used to generate a PDF report of the
     * argument given as args[0]. When "order" is passed as
     * an argument, the method fetches two lists from the database
     * in order to generate the report.
     */
    private void report() {
        switch(args[0]) {
            case "client":
                var clients = (new ClientDAO()).getObjects();
                PDFGenerator.generatePDF(clients);
                break;
            case "product":
                var products = (new ProductDAO()).getObjects();
                PDFGenerator.generatePDF(products);
                break;
            case "order":
                var orders = (new OrderDAO()).getObjects();
                var orderItems = (new OrderItemDAO()).getObjects();
                PDFGenerator.generatePDF(orders, orderItems,"ordersReport");
                break;
            default:
                System.out.println("Invalid report argument");
        }
    }

    /**
     * Method used to delete a client from the database.
     * The actual deletion is done by the data access object.
     */
    private void deleteClient() {
        ClientDAO clientDAO = new ClientDAO();
        clientDAO.delete(args);
    }

    /**
     * Method used to delete a product from the database.
     * The actual deletion is done by the data access object.
     */
    private void deleteProduct() {
        ProductDAO productDAO = new ProductDAO();
        productDAO.delete(args);
    }

    /**
     * Method used to create a order.
     * An order implies actually two objects.
     */
    private void order() {
        Product product = new Product(args[1].trim());
        int orderItemID;
        ProductDAO productDAO = new ProductDAO();
        OrderItemDAO orderItemDAO = new OrderItemDAO();
        OrderDAO orderDAO = new OrderDAO();
        OrderItem orderItem;
        int inStock;
        float paying;
        int buyQuantity = Integer.parseInt(args[2].trim());
        if(productDAO.getField(product, "name") == null) {
            System.out.println("No such product in the database");
            return;
        }
        inStock = Integer.parseInt(productDAO.getField(product, "quantity"));
        if(buyQuantity > inStock) {
            System.out.println("Trying to buy more products than in stock");
            return;
        }
        paying = Float.parseFloat(productDAO.getField(product, "price")) * buyQuantity;
        orderItem = new OrderItem(buyQuantity, paying);
        orderItemDAO.insert(orderItem);
        orderItemID = DataAccessObject.getLastID();
        Orders orders = new Orders(args[0], args[1], orderItemID);
        orderDAO.insert(orders);
        product.setQuantity(inStock - buyQuantity);
        productDAO.update(product, new String[]{"quantity"}, new String[]{"name"});
        PDFGenerator.generatePDF(new ArrayList(Arrays.asList(orders)), new ArrayList(Arrays.asList(orderItem)), "bill");
    }
}
