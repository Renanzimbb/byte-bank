package br.com.alura.bytebank.domain.conta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

public class ContaDAO {
    private Connection conn;

    ContaDAO(Connection connection) {
        this.conn = connection;
    }

    public void salvar(DadosAberturaConta dadosDaConta) {
        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), BigDecimal.ZERO, cliente,true);
        String sql = "INSERT INTO conta(numero, saldo, cliente_nome, cliente_cpf, cliente_email, estava_ativa) VALUES(?,?,?,?,?,?)";

        // new ContaDAO(conn).salvar(dadosDaConta);
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, conta.getNumero());
            preparedStatement.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatement.setString(3, dadosDaConta.dadosCliente().nome());
            preparedStatement.setString(4, dadosDaConta.dadosCliente().cpf());
            preparedStatement.setString(5, dadosDaConta.dadosCliente().email());
            preparedStatement.setBoolean(6, true);

            preparedStatement.execute();
            preparedStatement.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void alterar(Integer numero, BigDecimal valor) {
        PreparedStatement ps;
        String sql = "UPDATE conta SET saldo = ? WHERE numero = ?";

        try {
                        conn.setAutoCommit(false);   
                        ps = conn.prepareStatement(sql);

                        ps.setBigDecimal(1, valor);
                        ps.setInt(2, numero);

                        ps.execute();
                        conn.commit();
                        ps.close();
                        conn.close();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }


    public Set<Conta> listar(){
        PreparedStatement ps = null;
        ResultSet resultSet;
        Set<Conta> contas = new HashSet<>();
        String sql = "SELECT * from conta WHERE estava_ativa = true";
        try {
            ps = conn.prepareStatement(sql);
            resultSet = ps.executeQuery();

           while(resultSet.next()){
            Integer numero = resultSet.getInt(1);
            BigDecimal saldo = resultSet.getBigDecimal(2);
            String nome = resultSet.getString(3);
            String cpf = resultSet.getString(4);
            String email = resultSet.getString(5);
            DadosCadastroCliente dadosCadastroCliente =  new DadosCadastroCliente(nome, cpf, email);
            Boolean estaAtiva = resultSet.getBoolean(6);

            Cliente cliente = new Cliente(dadosCadastroCliente);
            contas.add(new Conta(numero, saldo, cliente, estaAtiva));
           }
           resultSet.close();
           ps.close();    
           conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
                return contas;
    }
    
    public Conta listarPorNumero(Integer numero) {
        String sql = "SELECT * FROM conta WHERE numero = ? AND estava_ativa = true";

        PreparedStatement ps;
        ResultSet resultSet;
        Conta conta = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, numero);
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Integer numeroRecuperado = resultSet.getInt(1);
                BigDecimal saldo = resultSet.getBigDecimal(2);
                String nome = resultSet.getString(3);
                String cpf = resultSet.getString(4);
                String email = resultSet.getString(5);
                Boolean estaAtiva = resultSet.getBoolean(6);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);
                Cliente cliente = new Cliente(dadosCadastroCliente);

                conta = new Conta(numeroRecuperado,  saldo, cliente, estaAtiva);
            }
            resultSet.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conta;
    }

    public void deletar(Integer numeroDaConta){
        String sql = "DELETE FROM conta where numero = ?";
        try {
            conn.setAutoCommit(false);   
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, numeroDaConta);
            ps.execute();
            conn.commit();
            ps.close();
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void alterarLogico(Integer numeroDaConta) {
        PreparedStatement ps;
        String sql = "UPDATE conta SET estava_ativa = false WHERE numero = ?";

        try {
                        conn.setAutoCommit(false);   
                        ps = conn.prepareStatement(sql);

                       
                        ps.setInt(1, numeroDaConta);

                        ps.execute();
                        conn.commit();
                        ps.close();
                        conn.close();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
