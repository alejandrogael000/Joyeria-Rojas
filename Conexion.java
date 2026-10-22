package rojvsjoyeria.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String SERVER   = "localhost";
    private static final String DATABASE = "JoyeriaRojvs";
    private static final String USER     = "sa";
    private static final String PASSWORD = "tu_password";
    private static final String URL =
        "jdbc:sqlserver://" + SERVER + ":1433;databaseName=" + DATABASE +
        ";encrypt=true;trustServerCertificate=true";

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQL Server no encontrado: " + e.getMessage());
        }
    }
}
