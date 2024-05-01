package ca.bc.gov.open.jag;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;


public class DbConnection {

    private static final Logger logger = Logger.getLogger("DbConnection");
    private static File fileUrl = new File(
            DbConnection.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();

    public static Connection getConnection() {
        String[] jdbc = null;

        try {
            jdbc = getJdbcInfo();
            Class.forName(jdbc[0]);
        } catch (ClassNotFoundException | IOException e) {
            logger.info("NO Oracle JDBC Driver found!");
            e.printStackTrace();
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(jdbc[1], jdbc[2], jdbc[3]);
        } catch (SQLException e) {
            logger.info("Connection Failed! Check output console");
            e.printStackTrace();
        }
        return connection;
    }

    public static String[] getJdbcInfo() throws IOException {
        String jdbcDriver, jdbcUrl, username, password = null;
        Properties jdbcProperties = new Properties();
        java.io.InputStream file;


        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            file = classloader.getResourceAsStream("jdbc.properties");
            jdbcProperties.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jdbcDriver = jdbcProperties.getProperty("jdbc.driver");
        jdbcUrl = CommonUtils.getJdbcUrl();
        username = jdbcProperties.getProperty(CommonUtils.getJdbcUserName());
        password = jdbcProperties.getProperty(CommonUtils.getJdbcPassword());
        String[] jdbc = {jdbcDriver, jdbcUrl, username, password};
        return jdbc;
    }


}
