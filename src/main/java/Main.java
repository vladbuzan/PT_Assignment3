import BusinessLogic.Action;
import DataAccess.ConnectionFactory;
import Presentation.Parser;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(args[0]);
        Parser parser = new Parser(args[0]);
        List<Action> list = parser.getActions();
        parser.close();
        list.forEach(action -> action.doAction());
    }
}
