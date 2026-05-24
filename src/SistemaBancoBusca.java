import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SistemaBancoBusca {
    public static void main(String[] args) {

            String url = "jdbc:mysql://localhost:3306/database_bancoteste";
            String usuario = "root";
            String senha = "215214@";

            System.out.println("Abrindo o extrato para confirmação de valores!\n");

            try {
                Connection conexao = DriverManager.getConnection(url, usuario, senha);

                String sql = "SELECT * FROM contas";
                PreparedStatement comando = conexao.prepareStatement(sql);

                ResultSet resultado = comando.executeQuery();

                System.out.println("=== EXTRATO GERAL DA AGÊNCIA ===");

                while (resultado.next()){

                    int id = resultado.getInt("id");
                    String titular = resultado.getString("titular");
                    double saldo = resultado.getDouble("saldo");

                    System.out.println("ID: " + id + " | Titular: " + titular + " | Saldo: R$" + saldo);

                }

                System.out.println("=================\n");
                conexao.close();

            } catch(SQLException erro) {
                System.out.println("Falha ao buscar os dados no banco");
                erro.printStackTrace();
            }

        }
    }