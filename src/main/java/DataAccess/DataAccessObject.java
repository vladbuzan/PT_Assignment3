package DataAccess;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to access the mysql warehouse database.
 * The queries are executed using reflection techniques on
 * the class of the provided parameter T.
 * @param <T>
 */
public class DataAccessObject<T> {
    private Class<T> type;
    private static int id;

    /**
     * Constructor.
     * Initialises type.
     */
    public DataAccessObject() {
        this.type=(Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Used to obtain a select query.
     * @param field String containing the field the
     *              WHERE condition will be based on
     * @return String containing the query
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append(" WHERE " + field + "= ?");
        return sb.toString();
    }

    /**
     * Used to obtain a specific field from the warehouse database.
     * @param object Object representing the type we want to fetch from
     * @param fieldName the name of the selected field
     * @return String containing the value of the field for the particular
     * object in the warehouse database
     */
    public String getField(T object, String fieldName) {
        String name;
        Field field;
        Connection connection;
        ResultSet resultSet;
        PreparedStatement statement;
        String query = createSelectQuery("name");
        try {
            field = type.getDeclaredField("name");
            field.setAccessible(true);
            name = (String) field.get(object);
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1,name);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString(fieldName);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Used to create an INSERT statement.
     * @return String containing the INSERT statement
     */
    private String createInsertStatement() {
        StringBuilder sb = new StringBuilder();
        Class cls = type;
        Field[] fields = cls.getDeclaredFields();
        sb.append("INSERT INTO ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append("(");
        for(int i = 0; i < fields.length; i++) {
            if(i == fields.length - 1) {
                sb.append(fields[i].getName() + ") VALUES (");
            } else {
                sb.append(fields[i].getName() + ", ");
            }
        }
        for(int i = 0; i < fields.length; i++) {
            if(i == fields.length - 1) {
                sb.append("?)");
            } else {
                sb.append("?" + ", ");
            }
        }
    return sb.toString();
    }

    /**
     * Used to create a DELETE statement where the condition
     * is obtained using a particular number of fields.
     * @param nrFields number of fields used in the WHERE clause
     * @return String containing the DELETE statement
     */
    private String createDeleteStatement(int nrFields) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = type.getDeclaredFields();

        sb.append("DELETE FROM " + type.getSimpleName() + " WHERE ");
        for(int i = 0; i < nrFields; i++) {
            if(i == nrFields - 1) {
                sb.append(fields[i].getName() + "=?");
            } else {
                sb.append(fields[i].getName() + "=? AND ");
            }
        }
        return sb.toString();
    }

    /**
     * Used to obtain a UPDATE statement with particular updated
     * fields and particular fields used in the condition
     * @param updateFields fields the user wants to update
     * @param conditionFields fields the users wants to test
     *                        the condition on
     * @return String containing the UPDATE statement
     */
    private String createUpdateStatement(String[] updateFields, String[] conditionFields){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE " + type.getSimpleName() + " SET ");
        for(int i = 0; i < updateFields.length; i++) {
            if(i == updateFields.length - 1) {
                sb.append(updateFields[i] + "=? WHERE ");
            } else {
                sb.append(updateFields[i] + "=?, ");
            }
        }
        for(int i = 0; i < conditionFields.length; i++) {
            if(i == updateFields.length - 1) {
                sb.append(conditionFields[i] + "=?");
            } else {
                sb.append(conditionFields[i] + "=? AND ");
            }
        }
        return sb.toString();
    }

    /**
     * Used to insert an object of type T in the
     * warehouse database.
     * @param object Object to be inserted
     */
    public void insert(T object) {
        String statement = createInsertStatement();
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement;
        Field[] fields = type.getDeclaredFields();
        Object value;
        try {
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                value = fields[i].get(object);
                preparedStatement.setString(i + 1, value.toString());
            }
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(connection);
            ConnectionFactory.close(generatedKeys);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an object of type T based on the
     * specified fields.
     * @param fields fields the deletion condition is based on
     */
    public void delete(String[] fields) {
        String statement = createDeleteStatement(fields.length);
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(statement);
            for(int i = 0; i < fields.length; i++) {
                preparedStatement.setString(i + 1, fields[i]);
            }
            preparedStatement.executeUpdate();
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update an object of type T in the warehouse database.
     * The update is based on the fields and values passed
     * as arguments.
     * @param object object whose values will be used for the update
     * @param updateFields the fields to be updated
     * @param conditionFields the fields the update condition is built on
     */
    public void update(T object, String[] updateFields, String[] conditionFields) {
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(createUpdateStatement(updateFields,conditionFields));
            for(int i = 0; i < updateFields.length; i++) {
                Field field = type.getDeclaredField(updateFields[i]);
                field.setAccessible(true);
                preparedStatement.setObject(i + 1, field.get(object));
            }
            for(int i = 0; i < conditionFields.length; i++) {
                Field field = type.getDeclaredField(conditionFields[i]);
                field.setAccessible(true);
                preparedStatement.setObject(updateFields.length + i + 1, field.get(object));
            }
            preparedStatement.executeUpdate();
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(connection);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to obtain a list of objects of type T
     * @return list containing objects of type T fetched from
     * the warehouse database
     */
    public List<T> getObjects() {
        var list = new ArrayList<T>();
        Connection connection = ConnectionFactory.getConnection();
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM " + type.getSimpleName());
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                for(Field field : type.getDeclaredFields()) {
                    Object value = resultSet.getObject(field.getName());
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method=propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(connection);
        } catch(Exception e) {
            e.printStackTrace();
        }
    return list;
    }

    /**
     * Used to obtain the id of
     * the last inserted object.
     * @return integer representing the id
     */
    public static int getLastID() {
        return id;
    }
}
