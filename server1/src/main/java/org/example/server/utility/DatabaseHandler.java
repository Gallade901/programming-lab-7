package org.example.server.utility;

import org.example.server.App;
import org.example.utility.Console;

import java.sql.*;

/**
 * A class for handle database.
 */
public class DatabaseHandler {
    private static final String intt = " int, ";
    private static final String text = " text, ";
    private static final String serial = " serial, ";
    public static final String ifExists = " IF NOT EXISTS ";
    // Table names
    public static final String MARINE_TABLE = "space_marine";
    public static final String USER_TABLE = "my_user";
    public static final String COORDINATES_TABLE = "coordinates";
    public static final String CHAPTER_TABLE = "chapter";
    // MARINE_TABLE column names
    public static final String MARINE_TABLE_ID_COLUMN = "id";
    public static final String MARINE_TABLE_NAME_COLUMN = "name";
    public static final String MARINE_TABLE_CREATION_DATE_COLUMN = "creation_date";
    public static final String MARINE_TABLE_HEALTH_COLUMN = "health";
    public static final String MARINE_TABLE_HEART_COLUMN = "heart";
    public static final String MARINE_TABLE_CATEGORY_COLUMN = "category";
    public static final String MARINE_TABLE_MELEE_WEAPON_COLUMN = "melee_weapon";
    public static final String MARINE_TABLE_CHAPTER_ID_COLUMN = "chapter_id";
    public static final String MARINE_TABLE_USER_ID_COLUMN = "user_id";
    public static final String CREATE_MARINE_TABLE = "CREATE TABLE" + ifExists + MARINE_TABLE + "(" + MARINE_TABLE_ID_COLUMN + serial +
            MARINE_TABLE_NAME_COLUMN + text +
            MARINE_TABLE_CREATION_DATE_COLUMN + " date, " +
            MARINE_TABLE_HEALTH_COLUMN + intt +
            MARINE_TABLE_HEART_COLUMN + intt +
            MARINE_TABLE_CATEGORY_COLUMN + text +
            MARINE_TABLE_MELEE_WEAPON_COLUMN + text +
            MARINE_TABLE_CHAPTER_ID_COLUMN + intt +
            MARINE_TABLE_USER_ID_COLUMN + " int)";
    // USER_TABLE column names
    public static final String USER_TABLE_ID_COLUMN = "id";
    public static final String USER_TABLE_USERNAME_COLUMN = "username";
    public static final String USER_TABLE_PASSWORD_COLUMN = "password";
    public static final String CREATE_USER_TABLE = "CREATE TABLE" + ifExists + USER_TABLE + "(" +
            USER_TABLE_ID_COLUMN + serial +
            USER_TABLE_USERNAME_COLUMN + text +
            USER_TABLE_PASSWORD_COLUMN + " text)";
    // COORDINATES_TABLE column names
    public static final String COORDINATES_TABLE_ID_COLUMN = "space_marine_id";
    public static final String COORDINATES_TABLE_SPACE_MARINE_ID_COLUMN = "space_marine_id";
    public static final String COORDINATES_TABLE_X_COLUMN = "x";
    public static final String COORDINATES_TABLE_Y_COLUMN = "y";
    public static final String CREATE_COORINATES_TABLE = "CREATE TABLE" + ifExists + COORDINATES_TABLE + "(" +
            COORDINATES_TABLE_ID_COLUMN + intt +
            COORDINATES_TABLE_X_COLUMN + intt +
            COORDINATES_TABLE_Y_COLUMN + " int)";
    // CHAPTER_TABLE column names
    public static final String CHAPTER_TABLE_ID_COLUMN = "id";
    public static final String CHAPTER_TABLE_NAME_COLUMN = "name";
    public static final String CHAPTER_TABLE_PARENT_COLUMN = "parent_legion";
    public static final String CREATE_CHAPTER_TABLE = "CREATE TABLE" + ifExists + CHAPTER_TABLE + "(" +
            CHAPTER_TABLE_ID_COLUMN + serial +
            CHAPTER_TABLE_NAME_COLUMN + text +
            CHAPTER_TABLE_PARENT_COLUMN + " text)";

    private final String JDBC_DRIVER = "org.postgresql.Driver";

    private String url;
    private String user;
    private String password;
    private Connection connection;

    public DatabaseHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        connectToDataBase();
        dataBaseIfNotExist();
    }

    /**
     * A class for connect to database.
     */
    private void connectToDataBase() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(url, user, password);
            Console.println("Соединение с базой данных установлено.");
            App.logger.info("Соединение с базой данных установлено.");
        } catch (SQLException exception) {
            Console.printerror("Произошла ошибка при подключении к базе данных!");
            App.logger.error("Произошла ошибка при подключении к базе данных!");
        } catch (ClassNotFoundException exception) {
            Console.printerror("Драйвер управления базой дынных не найден!");
            App.logger.error("Драйвер управления базой дынных не найден!");
        }
    }

    private void dataBaseIfNotExist() {
        try {
            PreparedStatement createUserTable = connection.prepareStatement(CREATE_USER_TABLE);
            createUserTable.executeUpdate();
            PreparedStatement createMarianeTable = connection.prepareStatement(CREATE_MARINE_TABLE);
            createMarianeTable.executeUpdate();
            PreparedStatement createChapterTable = connection.prepareStatement(CREATE_CHAPTER_TABLE);
            createChapterTable.executeUpdate();
            PreparedStatement createCoordinatesTable = connection.prepareStatement(CREATE_COORINATES_TABLE);
            createCoordinatesTable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Console.printerror("ошибка создание таблицы");
        }
    }

    /**
     * @param sqlStatement SQL statement to be prepared.
     * @param generateKeys Is keys needed to be generated.
     * @return Pprepared statement.
     * @throws SQLException When there's exception inside.
     */
    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        try {
            if (connection == null) throw new SQLException();
            if (generateKeys) {
                preparedStatement = connection.prepareStatement(sqlStatement, new String[]{"id"});
            } else {
                preparedStatement = connection.prepareStatement(sqlStatement, Statement.NO_GENERATED_KEYS);
            }
            //App.logger.info("Подготовлен SQL запрос '" + sqlStatement + "'.");
            return preparedStatement;
        } catch (SQLException exception) {
            //App.logger.error("Произошла ошибка при подготовке SQL запроса '" + sqlStatement + "'.");
            if (connection == null) App.logger.error("Соединение с базой данных не установлено!");
            throw new SQLException(exception);
        }
    }

    /**
     * Close prepared statement.
     *
     * @param sqlStatement SQL statement to be closed.
     */
    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if (sqlStatement == null) return;
        try {
            sqlStatement.close();
            //App.logger.info("Закрыт SQL запрос '" + sqlStatement + "'.");
        } catch (SQLException exception) {
            //App.logger.error("Произошла ошибка при закрытии SQL запроса '" + sqlStatement + "'.");
        }
    }

    /**
     * Close connection to database.
     */
    public void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
            Console.println("Соединение с базой данных разорвано.");
            App.logger.info("Соединение с базой данных разорвано.");
        } catch (SQLException exception) {
            Console.printerror("Произошла ошибка при разрыве соединения с базой данных!");
            App.logger.error("Произошла ошибка при разрыве соединения с базой данных!");
        }
    }

    /**
     * Set commit mode of database.
     */
    public void setCommitMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            App.logger.error("Произошла ошибка при установлении режима транзакции базы данных!");
        }
    }

    /**
     * Set normal mode of database.
     */
    public void setNormalMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            App.logger.error("Произошла ошибка при установлении нормального режима базы данных!");
        }
    }

    /**
     * Commit database status.
     */
    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException exception) {
            App.logger.error("Произошла ошибка при подтверждении нового состояния базы данных!");
        }
    }

    /**
     * Roll back database status.
     */
    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException exception) {
            App.logger.error("Произошла ошибка при возврате исходного состояния базы данных!");
        }
    }

    /**
     * Set save point of database.
     */
    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException exception) {
            App.logger.error("Произошла ошибка при сохранении состояния базы данных!");
        }
    }
}
