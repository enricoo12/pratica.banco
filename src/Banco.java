import java.util.Scanner;
import java.util.ArrayList;
import java.sql.*;

public class Banco {

    static Scanner teclado = new Scanner(System.in);
    static ArrayList<Conta> listaDeContas = new ArrayList<>();
    static Conta contaLogada = null;

    public static void main(String[] args) {

        String url =  "jdbc:mysql://localhost:3306/database_bancoteste";
        String usuario = "root";
        String senha = "215214@";

        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Banco de Dados conectado com sucesso!\n");

            int opcaoInicial = 0;
            while (opcaoInicial != 3) {

                System.out.println("----BEM-VINDO AO BANCO----\n");
                System.out.println("1 - Criar Nova Conta");
                System.out.println("2 - Fazer Login");
                System.out.println("3 - Sair do Sistema");

                opcaoInicial = teclado.nextInt();
                teclado.nextLine();

                switch (opcaoInicial) {
                    case 1 -> criarConta(conexao);
                    case 2 -> {
                        fazerLogin(conexao);
                        if (contaLogada != null) {
                            menuDoBanco(conexao);
                        }
                    }
                    case 3 -> {
                        System.out.println("Fechando o Sistema");
                        conexao.close();
                    }
                    default -> System.out.println("Opção Inválida!");
                }
            }

        } catch (SQLException erro) {
            System.out.println("O sistema não conseguiu se conectar ao Banco de Dados!\n");
        }
        teclado.close();
    }

    public static void criarConta(Connection conexao) {
        try {
            System.out.println("Digite sua idade!");
            int idade = teclado.nextInt();
            teclado.nextLine();

            if (idade >= 18) {
                Conta novaConta = new Conta();

                System.out.println("Digite seu nome!");
                novaConta.titular = teclado.nextLine();

                System.out.println("Seja bem vindo, cadastre seu login");
                novaConta.usuario = teclado.nextLine();

                System.out.println("Crie uma senha para o usuário");
                novaConta.senha = teclado.nextLine();

                String sql = "INSERT INTO contas (titular, idade, usuario, senha, saldo) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement comando = conexao.prepareStatement(sql);

                comando.setString(1, novaConta.titular);
                comando.setInt(2, idade);
                comando.setString(3, novaConta.usuario);
                comando.setString(4, novaConta.senha);
                comando.setDouble(5, novaConta.getSaldo());
                comando.executeUpdate();

                listaDeContas.add(novaConta);
                System.out.println("Conta criada com sucesso! Já pode fazer login!");
                System.out.println("----------------------\n");

            } else {
                System.out.println("Acesso negado, você é menor de idade!");
                System.out.println("----------------------\n");
            }

        } catch (SQLException erroBanco) {
            System.out.println("O MYSQL recusou os dados!");
            erroBanco.printStackTrace();

        } catch (Exception erro) {
            System.out.println("Erro, digite um número válido para a idade!");
            teclado.nextLine();
        }
    }

    public static void fazerLogin(Connection conexao) {
        int tentativas = 0;
        boolean logado = false;

        while (tentativas < 3 && !logado) {

            System.out.println("Digite seu login!");
            String usuarioCorreto = teclado.nextLine();

            System.out.println("Digite sua senha!");
            String senhaCorreta = teclado.nextLine();

            try {
                String sql = "SELECT * FROM contas WHERE usuario = ? AND senha = ?";
                PreparedStatement comando = conexao.prepareStatement(sql);
                comando.setString(1, usuarioCorreto);
                comando.setString(2, senhaCorreta);
                ResultSet resultado = comando.executeQuery();

                if (resultado.next()) {
                    contaLogada = new Conta();
                    contaLogada.titular = resultado.getString("titular");
                    contaLogada.usuario = resultado.getString("usuario");
                    contaLogada.senha = resultado.getString("senha");

                    double saldo = resultado.getDouble("saldo");
                    contaLogada.depositar(saldo);
                    logado = true;
                    System.out.println("Acesso liberado com sucesso! Bem vindo " + contaLogada.titular +"!");
                    menuDoBanco(conexao);

                } else {
                    tentativas++;
                    System.out.println("Login ou senha incorretos! Tentativas restantes: " + ( 3 - tentativas) + "!\n");
                }

        } catch (SQLException erroLogin) {
                System.out.println("Erro na comunicação com banco de dados!");
                erroLogin.printStackTrace();
                break;
            }
        }
            if (!logado) {
                System.out.println("Login não realizado, conta bloqueada, voltando ao início!");
            }
    }

    public static void menuDoBanco(Connection conexao) {
        int opcao = 0;

        while (contaLogada != null) {
            System.out.println("----MENU DA CONTA----\n");
            System.out.println("1 - Consultar Saldo");
            System.out.println("2 - Fazer Saque");
            System.out.println("3 - Fazer Depósito");
            System.out.println("4 - Investir Dinheiro");
            System.out.println("5 - Ver Extrato");
            System.out.println("6 - Fazer PIX (transferir)");
            System.out.println("7 - Excluir Conta");
            System.out.println("8 - Fazer Logout (Sair)");
            System.out.println("Escolha a opção desejada!");

            opcao = teclado.nextInt();

            switch (opcao) {
                case 1 -> consultarSaldo();
                case 2 -> sacar(conexao);
                case 3 -> depositar(conexao);
                case 4 -> investir();
                case 5 -> verExtrato(conexao);
                case 6 -> fazerPix(conexao);
                case 7 -> excluirConta(conexao);
                case 8 -> {
                    System.out.println("Fazendo logout, voltando ao início!");
                    System.out.println("----------------------\n");
                    contaLogada = null;
                }

                default -> System.out.println("Informações inválidas!");
            }
        }
    }

    public static void consultarSaldo() {
        System.out.println("Seu saldo atual é de: " + contaLogada.getSaldo());
        System.out.println("----------------------\n");
    }

    public static void sacar(Connection conexao) {
        System.out.println("Digite o valor do saque!");
        double saque = teclado.nextDouble();
        teclado.nextLine();

        if (saque <= 0) {
            System.out.println("O valor do saque deve ser maior que 0, saque inválido!");
            return;
        }

        if (saque > contaLogada.getSaldo()) {
            System.out.println("OPERAÇÃO NEGADA! Saldo insuficiente para saque!\n");
            return;
        }

        try {
            String sql = "UPDATE contas SET saldo = saldo - ? WHERE usuario = ?";
            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setDouble(1, saque);
            comando.setString(2, contaLogada.usuario);

            int linhasAfetadas = comando.executeUpdate();
            if (linhasAfetadas > 0) {
                contaLogada.sacar(saque);
                System.out.println("SUCESSO! Você sacou: R$" + saque + " Reais");
                System.out.println("Seu saldo atual é de: " + contaLogada.getSaldo());
                System.out.println("----------------------\n");

            }
        } catch (SQLException erro) {
            System.out.println("Erro de comunicação com banco de dados!\n");
            erro.printStackTrace();
        }
    }

    public static void depositar(Connection conexao) {
        if (contaLogada == null){
            System.out.println("Acesso negado: Precisa fazer o login primeiro! (Opção 1)");
            System.out.println("---------------------\n");
            return;
        }

        try {
            System.out.println("Qual o valor do depósito!");
            double deposito = teclado.nextDouble();
            teclado.nextLine();

            if (deposito <= 0) {
                System.out.println("Valor digitado é inválido. O depósito deve ser maior que zero!");
            }

            String sql = "UPDATE contas SET saldo = saldo + ? WHERE usuario = ?";
            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setDouble(1, deposito);
            comando.setString(2, contaLogada.usuario);
            int linhasAfetadas = comando.executeUpdate();

            if (linhasAfetadas > 0) {
                contaLogada.adicionarValor(deposito);
                RegistrarExtrato(conexao, contaLogada.usuario, "DEPÓSITO", deposito, "Depósito feito!");
                System.out.println("Depósito realizado, seu saldo agora é de: " + contaLogada.getSaldo());
                System.out.println("----------------------\n");
            }

        } catch (SQLException erroDep) {
            System.out.println("O MYQSL recusou os dados do depósito");
            erroDep.printStackTrace();
        } catch (Exception erro) {
            System.out.println("Digite um valor válido para depósito!\n");
        }
    }

    public static void investir() {
        System.out.println("Quanto você quer investir?");
        double valor = teclado.nextDouble();

        System.out.println("Por quantos meses quer investir?");
        int meses = teclado.nextInt();

        System.out.println("----SIMULAÇÃO DE LUCRO (5% ao mês)----");

        for (int i = 1; i <= meses; i++) {

            valor = valor * 1.05;

            System.out.printf("Mês %d: R$ %.2f \n", i, valor);
        }

         System.out.println("----FIM DA SIMULAÇÃO----");
         System.out.println("----------------------\n");

    }

    public static void verExtrato(Connection conexao) {
        System.out.println("-----EXTRATO BANCÁRIO----");
        System.out.println("Titular: " + contaLogada.titular);
        System.out.println("-------------------------");

        String sql = "SELECT tipo, valor, descricao, data_hora FROM extratos WHERE usuario_dono = ? ORDER BY data_hora DESC";

        try {
            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setString(1, contaLogada.usuario);
            ResultSet tabelaResultados = comando.executeQuery();
            boolean movimentacao = false;

            while(tabelaResultados.next()) {
                movimentacao = true;

                String tipo = tabelaResultados.getString("tipo");
                double valor = tabelaResultados.getDouble("valor");
                String descricao = tabelaResultados.getString("descricao");
                String data = tabelaResultados.getString("data_hora");
                System.out.println("[" + data + "] " + tipo + " | R$ " + valor + " (" + descricao + ")");
            }

            if (!movimentacao) {
                System.out.println("Nenhuma movimetação foi efetuada!");
            }
            System.out.println("---------------------------------\n");
        } catch (SQLException erro) {
            System.out.println("Erro ao puxar extrato!");
            erro.printStackTrace();
        }
    }

    public static void fazerPix(Connection conexao) {
        System.out.println("----ÁREA DE TRANFERÊNCIA----");
        System.out.println("Saldo atual: R$" + contaLogada.getSaldo());

        teclado.nextLine();

        System.out.println("Digite o nome de usuário (Login) de quem vai receber a transferência:");
        String usuarioDestino = teclado.nextLine();

        System.out.println("Qual valor deseja transferir?");
        double transferir = teclado.nextDouble();
        teclado.nextLine();

        if (transferir <= 0) {
            System.out.println("Não foi possível concluir a transferência! O valor digitado é inválido!");
            return;
        }
        if (transferir > contaLogada.getSaldo()) {
            System.out.println("Não foi possível concluir a transferência! Saldo insuficiente!");
            return;
        }
        if (usuarioDestino.equals(contaLogada.usuario)) {
            System.out.println("Não é possível transferir para você mesmo!");
            return;
        }

        try {
            conexao.setAutoCommit(false);

            String sqlSaida = "UPDATE contas SET saldo = saldo - ? WHERE usuario = ?";
            PreparedStatement comandoSaida = conexao.prepareStatement(sqlSaida);
            comandoSaida.setDouble(1, transferir);
            comandoSaida.setString(2, contaLogada.usuario);
            int linhasSaida = comandoSaida.executeUpdate();

            String sqlEntrada = "UPDATE contas SET saldo = saldo + ? WHERE usuario = ?";
            PreparedStatement comandoEntrada = conexao.prepareStatement(sqlEntrada);
            comandoEntrada.setDouble(1, transferir);
            comandoEntrada.setString(2, usuarioDestino);
            int linhasEntrada = comandoEntrada.executeUpdate();

            if (linhasSaida > 0 && linhasEntrada > 0) {
                RegistrarExtrato(conexao, contaLogada.usuario, "PIX ENVIADO", transferir, "Para: " + usuarioDestino);
                RegistrarExtrato(conexao, usuarioDestino, "PIX RECEBIDO", transferir, "De: " + contaLogada.usuario);
                conexao.commit();

                contaLogada.descontarValor(transferir);
                System.out.println("Transferência de: R$" + transferir + " realizada com sucesso para " + usuarioDestino + "!");

            } else {
                conexao.rollback();
                System.out.println("Usuário não encontrado! Nenhum valor foi enviado!");
            }
            conexao.setAutoCommit(true);

        } catch (SQLException erroTransferir) {
            System.out.println("Erro no sistema, desfazendo operação de transferência...");
            try {
                conexao.rollback();
                conexao.setAutoCommit(true);
            } catch (SQLException erro) {
                erro.printStackTrace();
            }
        }
    }

    public static void RegistrarExtrato(Connection conexao, String usuario, String tipo, double valor, String descricao){
        String sql = "INSERT INTO extratos (usuario_dono, tipo, valor, descricao) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setString(1, usuario);
            comando.setString(2, tipo);
            comando.setDouble(3, valor);
            comando.setString(4, descricao);
            comando.executeUpdate();

        } catch(SQLException erro) {
            System.out.println("Alerta: Não foi possível gerar o comprovante!\n");
        }
    }

    public static void excluirConta(Connection conexao) {
        System.out.println("----ENCERRAR CONTA----");
        System.out.println("Atenção, " + contaLogada.titular + "! Esta ação não pode ser revertida.");
        System.out.println("Todo seu saldo e histórico serão apagados permanentemente.");
        System.out.println("Digite (CONFIRMAR) para deletar sua conta.");

        String confirmacao = teclado.next().toUpperCase();

        if (!confirmacao.equals("CONFIRMAR")) {
            System.out.println("Encerramento cancelado! Seus dados estão seguros");
            return;
        }

        try {
            conexao.setAutoCommit(false);

            String sql = "DELETE FROM contas WHERE usuario = ?";
            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setString(1, contaLogada.usuario);
            int linhasConta = comando.executeUpdate();

            if (linhasConta > 0) {
                conexao.commit();
                System.out.println("Conta encerrada com sucesso!\n");
                contaLogada = null;

            } else {
                conexao.rollback();
                System.out.println("Erro, não foi possível encontrar sau conta no banco de dados do sistema!\n");
            }
            conexao.setAutoCommit(true);

        } catch (SQLException erroDeletar) {
            System.out.println("Erro crítico ao tentar encerrar sua conta!\n");
             try{
                 conexao.rollback();
                 conexao.setAutoCommit(true);
             } catch (SQLException erro) {
                 erro.printStackTrace();
             }
             erroDeletar.printStackTrace();
        }
    }
}