import java.util.Scanner;
import java.sql.*;

public class SistemaBancoMain {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        String url = "jdbc:mysql://localhost:3306/database_bancoteste";
        String usuario = "root";
        String senha = "215214@";

        int opcao = 0;

        while(opcao != 5){
            System.out.println("----BANK OF WORLD----");
            System.out.println("1 - Criar Nova Conta");
            System.out.println("2 - Ver Extrato Geral");
            System.out.println("3 - Depositar");
            System.out.println("4 - Encerrar Conta");
            System.out.println("5 - Sair do Sistema");
            System.out.println("Escolha uma das opções!\n");

            opcao = teclado.nextInt();
            teclado.nextLine();

            try {
                Connection conexao = DriverManager.getConnection(url, usuario, senha);

                switch (opcao) {
                    case 1 -> {
                        System.out.println("Digite o nome do titular!");
                        String titular = teclado.nextLine();
                        System.out.println("Saldo inicial: ");
                        double saldo = teclado.nextDouble();

                        String sqlInsert = "INSERT INTO contas (titular, saldo) VALUES (?, ?)";
                        PreparedStatement cmdInsert = conexao.prepareStatement(sqlInsert);
                        cmdInsert.setString(1, titular);
                        cmdInsert.setDouble(2, saldo);
                        cmdInsert.executeUpdate();
                        System.out.println("Conta Criada com Sucesso!");
                    }

                    case 2 -> {
                        String sqlSelect = " SELECT * FROM contas";
                        ResultSet rs = conexao.prepareStatement(sqlSelect).executeQuery();

                        while(rs.next()){
                            System.out.println("ID: " + rs.getInt("id") + " | " + rs.getString("titular") + " | " + rs.getDouble("saldo"));
                        }
                    }

                    case 3 -> {
                        System.out.println("Nome do titular para depósito!");
                        String nomeDep = teclado.nextLine();
                        System.out.println("Qual o valor do depósito!");
                        Double valorDep = teclado.nextDouble();

                        String sqlUpdate = "UPDATE contas SET saldo = saldo + ? WHERE titular =?";
                        PreparedStatement cmdUpdate = conexao.prepareStatement(sqlUpdate);
                        cmdUpdate.setDouble(1, valorDep);
                        cmdUpdate.setString(2, nomeDep);

                        int linhasUp = cmdUpdate.executeUpdate();
                        if (linhasUp > 0){
                            System.out.println("Depósito realizado com sucesso!");
                        } else {
                            System.out.println("Cliente não encontrado!");
                        }
                    }


                    case 4 -> {
                        System.out.println("Digite o nome do titular para deletar!");
                        String nomeDel = teclado.nextLine();
                        String sqlDel = "DELETE FROM contas WHERE titular = ?";
                        PreparedStatement cmdDel = conexao.prepareStatement(sqlDel);
                        cmdDel.setString(1, nomeDel);
                        int linhas = cmdDel.executeUpdate();

                        if(linhas > 0){
                            System.out.println("Conta Removida!");
                        } else {
                            System.out.println("Conta não encontrada!");
                        }

                    }

                }

                conexao.close();

            } catch (SQLException erro) {
                erro.printStackTrace();
            }
        }

        System.out.println("Sistema encerrado!");





        teclado.close();

    }
}