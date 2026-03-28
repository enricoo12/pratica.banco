import java.util.ArrayList;
public class Conta {

    String titular;
    String usuario;
    String senha;


    private double saldo = 0;
    ArrayList<String> historico = new ArrayList<>();


    public void depositar(double deposito) {
        saldo = saldo + deposito;
        historico.add("Depósito de R$: " + deposito);
    }

    public void sacar(double saque) {
        if (saldo >= saque) {
            saldo = saldo - saque;
            historico.add("Saque de R$: " +  saque);
            System.out.println("Saque realizado!");
        } else {
            System.out.println("Saque não realizado, saldo insuficiente!");
        }
    }

    public void transferir(double valor, Conta contaDestino) {
        if (saldo >= valor) {
            saldo = saldo - valor;
            historico.add("PIX enviado de R$: " + valor + " para " + contaDestino.titular);

            contaDestino.saldo = contaDestino.saldo + valor;
            contaDestino.historico.add("PIX recebido de R$: " + valor + " de " + titular);

            System.out.println("Transferência realizada com sucesso! ");
            System.out.println("----------------------\n");

        } else {
            System.out.println("Transferência negada, saldo insuficiente!");
        }
    }

    public void verExtrato() {
            System.out.println("----SEU EXTRATO----\n");

        if (historico.isEmpty()) {
            System.out.println("Nenhuma movimentação foi realizada!");
        } else {

            for (int i = 0; i < historico.size(); i++) {
                System.out.println(historico.get(i));
            }
        }

        System.out.println("-------------------\n");

    }

    public double getSaldo(){
        return saldo;
    }

}