import java.util.Scanner;
import java.util.ArrayList;

public class Banco {

    static Scanner teclado = new Scanner(System.in);
    static ArrayList<Conta> listaDeContas = new ArrayList<>();
    static Conta contaLogada = null;

    public static void main(String[] args) {

        int opcaoInicial = 0;
        while (opcaoInicial != 3) {
            System.out.println("----BEM-VINDO AO BANCO----\n");
            System.out.println("1 - Criar Nova Conta");
            System.out.println("2 - Fazer Login");
            System.out.println("3 - Sair do Sistema");

            opcaoInicial = teclado.nextInt();
            teclado.nextLine();

            switch (opcaoInicial) {
                case 1 -> criarConta();
                case 2 -> fazerLogin();
                case 3 -> System.out.println("Fechando o Sistema");
                default -> System.out.println("Opção Inválida!");


            }
        }
        teclado.close();
    }

    public static void criarConta() {

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

                listaDeContas.add(novaConta);
                System.out.println("Conta criada com sucesso! Já pode fazer login!");
                System.out.println("----------------------\n");

            } else {
                System.out.println("Acesso negado, você é menor de idade!");
                System.out.println("----------------------\n");
            }
        } catch (Exception e) {
            System.out.println("Erro, digite um número válido para a idade!");
            teclado.nextLine();
        }
    }


    public static void fazerLogin() {
        int tentativas = 0;
        boolean logado = false;

        while (tentativas < 3 && !logado) {

            System.out.println("Digite seu login!");
            String usuarioCorreto = teclado.nextLine();

            System.out.println("Digite sua senha!");
            String senhaCorreta = teclado.nextLine();

            for (int i = 0; i < listaDeContas.size(); i++) {
                Conta contaAtual = listaDeContas.get(i);

                if (usuarioCorreto.equals(contaAtual.usuario) && senhaCorreta.equals(contaAtual.senha)) {
                    contaLogada = contaAtual;
                    logado = true;
                    break;
                }
            }

            if (logado) {
                System.out.println("Acesso liberado, bem vindo " + contaLogada.titular + "!");
                System.out.println("----------------------\n");

                menuDoBanco();

            } else {
                tentativas++;
                System.out.println("Login incorreto, tentativas restantes: " + (3 - tentativas));
            }
        }

            if (!logado) {
                System.out.println("Login não realizado, conta bloqueada, voltando ao início!");
            }
    }

    public static void menuDoBanco() {
        int opcao = 0;

        while (opcao != 7) {
            System.out.println("----MENU DA CONTA----\n");
            System.out.println("1 - Consultar Saldo");
            System.out.println("2 - Fazer Saque");
            System.out.println("3 - Fazer Depósito");
            System.out.println("4 - Investir Dinheiro");
            System.out.println("5 - Ver Extrato");
            System.out.println("6 - Fazer PIX (transferir)");
            System.out.println("7 - Fazer Logout (Sair)");
            System.out.println("Escolha a opção desejada!");

            opcao = teclado.nextInt();

            switch (opcao) {
                case 1 -> consultarSaldo();
                case 2 -> sacar();
                case 3 -> depositar();
                case 4 -> investir();
                case 5 -> verExtrato();
                case 6 -> fazerPix();
                case 7 -> {
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

    public static void sacar() {
        System.out.println("Digite o valor do saque!");
        double saque = teclado.nextDouble();
        teclado.nextLine();

        contaLogada.sacar(saque);
        System.out.println("Seu saldo atual é de: " + contaLogada.getSaldo());
        System.out.println("----------------------\n");
    }

    public static void depositar() {
        System.out.println("Qual o valor do depósito!");
        double deposito = teclado.nextDouble();
        contaLogada.depositar(deposito);
        System.out.println("Depósito realizado, seu novo saldo é de: " + contaLogada.getSaldo());
        System.out.println("----------------------\n");

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

    public static void verExtrato() {
        contaLogada.verExtrato();
    }

    public static void fazerPix() {
        System.out.println("Digite o login de quem vai receber a transferência:");
        teclado.nextLine();
        String usuarioDestino = teclado.nextLine();

        Conta contaAmigo = null;

        for (int i = 0; i < listaDeContas.size(); i++) {
            Conta contaAtual = listaDeContas.get(i);

        if (contaAtual.usuario.equals(usuarioDestino)) {
            contaAmigo = contaAtual;
            break;
        }
    }
        if (contaAmigo != null) {
            System.out.println("Destinatário encontrado: " + contaAmigo.titular);
            System.out.println("Qual o valor do PIX?");
            double valor = teclado.nextDouble();

            contaLogada.transferir(valor, contaAmigo);

        } else {
            System.out.println("Usuário não encontrado! Verifique o login digitado!");
        }

    }
}