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

    public void descontarValor(double valor) {
        this.saldo -= valor;
        this.historico.add("Saque/Transferência: - R$" + valor);
    }

    public void adicionarValor(double valor) {
        this.saldo += valor;
        this.historico.add("Depósito/Recebimento: + R$" + valor);
    }

    public double getSaldo(){
        return saldo;
    }
}