package org.sakila;

import java.sql.*;
import java.sql.Date;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the last name of an actor you like");
        String lastName = scanner.nextLine();
        getLastName(lastName);

        System.out.println("Please enter the first name of an actor you like");
        String firstName = scanner.nextLine();

        System.out.println("Please enter last name of an actor you like: ");
        String lastName1 = scanner.nextLine();

        getFilms(firstName, lastName1);

    }

    private static void getFilms (String firstName, String lastName) {
        try (
                Connection connection = getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        """
                            SELECT title, first_name, last_name
                            FROM film AS f
                            JOIN film_actor AS fa ON f.film_id = fa.film_id
                            JOIN actor AS a ON fa.actor_id = a.actor_id
                            WHERE first_name = ? AND last_name = ?;
                            """
                );
        ) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            // execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // process the results
                while (resultSet.next()) {
                    System.out.printf(
                            "title = %s, first_name = %s, last_name = %s; \n",
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3)
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void getLastName (String lastName) {
        try (
            Connection connection = getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                        SELECT first_name, last_name FROM actor 
                        WHERE last_name LIKE ? ORDER BY first_name
                        """
            );
        ) {
            // set any required parameters
            preparedStatement.setString(1, lastName);

            // execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // process the results
                while (resultSet.next()) {
                    System.out.printf(
                            "first_name = %s, last_name = %s; \n",
                            resultSet.getString(1),
                            resultSet.getString(2)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // throw new RuntimeException(e);
        }
    }
    private static BasicDataSource getDataSource() {
        // create the datasource
        BasicDataSource dataSource;
        dataSource = new BasicDataSource();

        // configure the datasource
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername("poweruser");
        dataSource.setPassword("poweruser");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }
}