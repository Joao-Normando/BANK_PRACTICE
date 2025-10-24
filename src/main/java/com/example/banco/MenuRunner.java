package com.example.banco;

import com.example.banco.service.ContaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class MenuRunner implements CommandLineRunner {

    private final ContaService contaService;
    private final Scanner scanner;

    public MenuRunner(ContaService contaService) {
        this.contaService = contaService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Bem-vindo ao Banco Spring Boot CLI! (Java 17)");


        var running = true;
        while (running) {
            exibirMenu();
            try {

                var input = scanner.nextLine();


                if (input.isBlank()) {
                    System.out.println("Por favor, digite uma opção.");
                    continue;
                }

                var opcao = Integer.parseInt(input);

                switch (opcao) {
                    case 1: consultarSaldo(); break;
                    case 2: consultarChequeEspecial(); break;
                    case 3: depositar(); break;
                    case 4: sacar(); break;
                    case 5: pagarBoleto(); break;
                    case 6: verificarUsoChequeEspecial(); break;
                    case 0:
                        running = false;
                        System.out.println("Obrigado por usar nossos serviços. Finalizando...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite um número válido para a opção.");
            } catch (Exception e) {
                System.out.println("Erro na operação: " + e.getMessage());
            }
            System.out.println("---------------------------------");
        }
        scanner.close();
    }

    private void exibirMenu() {
        System.out.println("\n*** Menu Principal ***");
        System.out.println("1. Consultar Saldo");
        System.out.println("2. Consultar Cheque Especial (Limite e Uso)");
        System.out.println("3. Depositar Dinheiro");
        System.out.println("4. Sacar Dinheiro");
        System.out.println("5. Pagar um Boleto");
        System.out.println("6. Verificar se está usando Cheque Especial");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    // --- Métodos de Operação ---

    private void consultarSaldo() {
        var saldo = contaService.consultarSaldo();
        System.out.printf("Seu saldo atual é: R$ %.2f\n", saldo);
    }

    private void consultarChequeEspecial() {
        var info = contaService.consultarChequeEspecialInfo();
        System.out.println(info);
    }

    private void verificarUsoChequeEspecial() {
        if (contaService.isUsandoChequeEspecial()) {
            System.out.println("Sim, você está atualmente usando o cheque especial.");
        } else {
            System.out.println("Não, você não está usando o cheque especial.");
        }
    }

    // --- Métodos que pedem input ---

    private BigDecimal lerValor(String prompt) {
        while(true) {
            try {
                System.out.print(prompt);
                var valorStr = scanner.nextLine();
                valorStr = valorStr.replace(',', '.');

                return new BigDecimal(valorStr);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Use o formato 50.00 ou 50,00.");
            }
        }
    }

    private void depositar() {
        var valor = lerValor("Digite o valor a depositar (ex: 50.00): ");
        contaService.depositar(valor);
        System.out.println("Depósito realizado com sucesso.");
        consultarSaldo(); // Mostra o novo saldo
    }

    private void sacar() {
        var valor = lerValor("Digite o valor a sacar (ex: 100.00): ");
        contaService.sacar(valor);
        System.out.println("Saque realizado com sucesso.");
        consultarSaldo();
    }

    private void pagarBoleto() {
        var valor = lerValor("Digite o valor do boleto (ex: 75.50): ");
        contaService.pagarBoleto(valor);
        System.out.println("Boleto pago com sucesso.");
        consultarSaldo();
    }
}
