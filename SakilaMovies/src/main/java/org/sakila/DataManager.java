package org.sakila;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private BasicDataSource dataSource;

    public DataManager() {
        this.dataSource = getDataSource();
    }

    private BasicDataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername("poweruser");
        dataSource.setPassword("poweruser");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    public List<Actor> searchActorsByName(String lastName) {
        List<Actor> actors = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT actor_id, first_name, last_name FROM actor WHERE last_name LIKE ? ORDER BY first_name"
             )) {
            preparedStatement.setString(1, lastName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    actors.add(new Actor(
                            resultSet.getInt("actor_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }

    public List<Film> getMoviesByActorId(int actorId) {
        List<Film> films = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                           """
                             SELECT f.film_id, f.title 
                             FROM film AS f
                             JOIN film_actor AS fa ON f.film_id = fa.film_id
                             WHERE fa.actor_id = ?
                         """
             )) {
            preparedStatement.setInt(1, actorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    films.add(new Film(
                            resultSet.getInt("film_id"),
                            resultSet.getString("title")

                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }
}
