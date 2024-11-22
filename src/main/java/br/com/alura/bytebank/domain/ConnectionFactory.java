package br.com.alura.bytebank.domain;

//https://refactoring.guru/pt-br/design-patterns/factory-method
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection recuperarConexao() {
        try {
            return DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/byte_bank?user=root&password=wsl");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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