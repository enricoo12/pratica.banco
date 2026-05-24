import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SistemaBancoUpdate {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/database_bancoteste";
        String usuario = "root";
        String senha = "215214@";

        System.out.println("Iniciando operação de depósito...");

        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);

            String sql = "UPDATE contas SET saldo = ? WHERE titular = ?";

            PreparedStatement comando = conexao.prepareStatement(sql);

            comando.setDouble(1, 5000.00);
            comando.setString(2, "Enrico");

            int linhasAfetadas = comando.executeUpdate();

            System.out.println("Depósito realizado com sucesso!");
            System.out.println("Linhas alteradas no banco: " + linhasAfetadas);

            conexao.close();

        } catch (SQLException erro) {
            System.out.println("Falha ao atualizar o banco!");
            erro.printStackTrace();

        }
    }
}