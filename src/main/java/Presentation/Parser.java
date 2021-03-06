package Presentation;

import BusinessLogic.Action;
import BusinessLogic.Operation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to parse the commands given
 * as lines of a text file.
 */
public class Parser {
    private BufferedReader reader;

    /**
     * Constructor used to instantiate BufferedReader.
     * @param inputFile Path to the txt file containing the
     *                  commands to be parsed
     */
    public Parser(String inputFile) {
        try {
            reader = new BufferedReader(new FileReader(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to fetch a list of action object.
     * Parse each line of the text file and create the
     * corresponding Action object
     * @return List containing parsed commands as Action objects
     */
    public List<Action> getActions() {
        List<Action> list = new ArrayList<>();
        String currentLine;
        try {
            while((currentLine = reader.readLine()) != null) {
                list.add(parseAction(currentLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Parse a String containing the command into
     * a Action object
     * @param line String containing the command
     * @return parsed Action object
     * @throws IOException Invalid command
     */
    private Action parseAction(String line) throws IOException {
        String[] args = line.split(" ");
        String[] actionArgs;
        Operation operation;
        switch (args[0].toLowerCase()) {
            case "insert":
                switch(args[1]) {
                    case "client:":
                        operation = Operation.InsertClient;
                        actionArgs = line.substring(14).split(",");
                        break;
                    case "product:":
                        operation = Operation.InsertProduct;
                        actionArgs = line.substring(15).split(",");
                        break;
                    default:
                        throw new IOException("Invalid insert statement");
                }
                break;
            case "delete":
                switch(args[1]) {
                    case "client:":
                        operation = Operation.DeleteClient;
                        actionArgs = line.substring(15).split(",");
                        actionArgs[1] = actionArgs[1].substring(1);
                        break;
                    case "product:":
                        operation = Operation.DeleteProduct;
                        actionArgs = line.substring(16).split(",");
                        break;
                    default:
                        throw new IOException("Invalid delete statement");
                }
                break;
            case "order:":
                operation = Operation.Order;
                actionArgs = line.substring(6).split(",");
                break;
            case "report":
                operation = Operation.Report;
                actionArgs = new String[1];
                actionArgs[0] = args[1];
                break;
            default:
                throw new IOException();
        }
        return new Action(operation, actionArgs);
    }

    /**
     * Close the opened BufferedReader.
     */
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
