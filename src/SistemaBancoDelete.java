import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SistemaBancoDelete {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/database_bancoteste";
        String usuario = "root";
        String senha = "215214@";

        System.out.println("Iniciado processo de encerramento de conta...");

        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);

            String sql = "DELETE FROM contas WHERE titular = ?";

            PreparedStatement comando = conexao.prepareStatement(sql);

            comando.setString(1, "Enrico");

            int linhasAfetadas = comando.executeUpdate();

            if (linhasAfetadas > 0 ){
                System.out.println("Conta encerrada e apagada com sucesso do banco!");
            } else {
                System.out.println("Nenhuma conta encontrada com esse nome");
            }

            conexao.close();

        } catch(SQLException erro) {
            System.out.println("Falha ao apagar dados!");
            erro.printStackTrace();

        }
    }
}