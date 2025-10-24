package com.example.banco.service; // <-- CORREÇÃO AQUI

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ContaService {

    // Estado da conta. Em um app real, viria de um banco de dados.
    private BigDecimal saldo;
    private final BigDecimal limiteChequeEspecial;

    /**
     * Construtor que inicializa a conta.
     * Começa com R$ 200,00 de saldo e R$ 1000,00 de limite.
     */
    public ContaService() {
        this.saldo = new BigDecimal("200.00");
        this.limiteChequeEspecial = new BigDecimal("1000.00");
    }

    /**
     * 1. Consultar Saldo
     */
    public BigDecimal consultarSaldo() {
        return this.saldo;
    }

    /**
     * 2. Consultar Cheque Especial (Limite e Uso)
     * Retorna uma String formatada com as informações.
     */
    public String consultarChequeEspecialInfo() {
        BigDecimal utilizado = BigDecimal.ZERO;

        // Se o saldo é negativo, o valor utilizado é o saldo (absoluto)
        if (this.saldo.compareTo(BigDecimal.ZERO) < 0) {
            utilizado = this.saldo.abs();
        }

        BigDecimal disponivel = this.limiteChequeEspecial.subtract(utilizado);

        return String.format(
                "Limite Total: R$ %.2f | Utilizado: R$ %.2f | Disponível: R$ %.2f",
                this.limiteChequeEspecial,
                utilizado,
                disponivel
        );
    }

    /**
     * 3. Depositar Dinheiro
     */
    public void depositar(BigDecimal valor) {
        // Validação simples
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser positivo.");
        }
        this.saldo = this.saldo.add(valor);
    }

    /**
     * 4. Sacar Dinheiro (usado também para pagar boleto)
     */
    public void sacar(BigDecimal valor) {
        // Validação simples
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da operação deve ser positivo.");
        }

        // Verifica se o saldo total (saldo + limite) é suficiente
        BigDecimal saldoDisponivelTotal = this.saldo.add(this.limiteChequeEspecial);

        if (saldoDisponivelTotal.compareTo(valor) >= 0) {
            // Se sim, subtrai o valor do saldo
            this.saldo = this.saldo.subtract(valor);
        } else {
            // Se não, lança uma exceção
            throw new RuntimeException("Saldo insuficiente (incluindo cheque especial).");
        }
    }

    /**
     * 5. Pagar Boleto (reutiliza a lógica de saque)
     */
    public void pagarBoleto(BigDecimal valor) {
        // Pagar um boleto é funcionalmente idêntico a um saque
        this.sacar(valor);
    }


    /**
     * 6. Verificar se está usando cheque especial
     */
    public boolean isUsandoChequeEspecial() {
        // Se o saldo é menor que zero, está usando o cheque especial
        return this.saldo.compareTo(BigDecimal.ZERO) < 0;
    }
}
