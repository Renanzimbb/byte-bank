package br.com.alura.bytebank.domain;

//https://refactoring.guru/pt-br/design-patterns/factory-method
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionFactory {

    public Connection recuperarConexao() {
        try {
            return createDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/byte_bank");
        config.setUsername("root");
        config.setPassword("");
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }
}

// public static void main(String[] args) {
// try {
// Connection connection = DriverManager
// .getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=wsl");
// System.out.println("Recuperei a conexão");

// connection.close();
// } catch (SQLException e) {
// System.out.println(e);
// }
// }