import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SistemaBancoBD {
    public static void main(String[] args){

        String url = "jdbc:mysql://localhost:3306/database_bancoteste";
        String usuario = "root";
        String senha = "215214@";

        System.out.println("Conectando ao MySQL...");

        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conectado com sucesso!");

            String sql = "INSERT INTO contas (titular,saldo) VALUES (?, ?)";
            PreparedStatement comando = conexao.prepareStatement(sql);

            comando.setString(1, "enrico");
            comando.setDouble(2, 1500.50);

            comando .executeUpdate();
            System.out.println("Conta do Enrico salva no Banco de Dados!");

            conexao.close();

        } catch (SQLException erro) {
            System.out.println("Falha ao conectar no Banco de Dados!");
            erro.printStackTrace();
        }

    }

}