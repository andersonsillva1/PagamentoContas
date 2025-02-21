import java.util.Scanner;

public class App {

    public static void exibirMenu() {
        System.out.println("------------------------------------------");
        System.out.println("             CONTAS E TRIBUTOS");
        System.out.println("     (Água, Luz, Telefone, IPTU, ISS)");
        System.out.println("------------------------------------------");
        System.out.println(" 1) Pagamento c/Código de Barras");
        System.out.println(" 2) Imprimir 2ª Via de Boleto");
        System.out.println(" 3) Sair");
        System.out.println("------------------------------------------");
    }

    public static boolean validarTamanho(String numero) {
        return numero.length() == 12;
    }

    public static boolean validarDacBloco(String numero) {
        int[] multiplicadores = { 2, 1 };
        int soma = 0, digito, produto, resto, dacCalculado, dacVerificador;

        for (int i = numero.length() - 2, j = 0; i >= 0; i--, j++) {
            digito = Character.getNumericValue(numero.charAt(i));
            produto = digito * multiplicadores[j % 2];
            soma += somarDigitos(produto);
        }

        dacVerificador = Character.getNumericValue(numero.charAt(11));
        resto = soma % 10;
        dacCalculado = (resto == 0) ? 0 : 10 - resto;
        return dacCalculado == dacVerificador;
    }

    public static int somarDigitos(int numero) {
        int soma = 0;
        while (numero > 0) {
            soma += numero % 10;
            numero /= 10;
        }
        return soma;
    }

    public static String calcularDac(String numero) {
        int[] multiplicadores = { 2, 1 };
        int soma = 0;

        for (int i = numero.length() - 1, j = 0; i >= 0; i--, j++) {
            int digito = Character.getNumericValue(numero.charAt(i));
            int produto = digito * multiplicadores[j % 2];
            soma += somarDigitos(produto);
        }
        int resto = soma % 10;
        int dacCalculado = (resto == 0) ? 0 : 10 - resto;

        return String.valueOf(dacCalculado);
    }

    public static void main(String[] args) {
        Scanner leia = new Scanner(System.in);
        int opcaoSelecionada = 0;
        String[] blocos = new String[4];

        do {
            exibirMenu();
            System.out.print("Escolha uma opção: ");
            if (leia.hasNextInt()) {
                opcaoSelecionada = leia.nextInt();
                leia.nextLine();

                switch (opcaoSelecionada) {
                    case 1:
                        boolean blocosValidos = false;
                        while (!blocosValidos) {
                            for (int i = 0; i < 4; i++) {
                                System.out.print("Digite o BLOCO " + (i + 1) + " contendo 12 dígitos: ");
                                blocos[i] = leia.nextLine().trim();

                                if (!validarTamanho(blocos[i])) {
                                    System.out.println("Os BLOCOS devem conter 12 dígitos.");
                                    break;
                                }
                                if (!validarDacBloco(blocos[i])) {
                                    System.out.println("BLOCO-" + (i + 1) + " inválido.");
                                    break;
                                }
                            }

                            blocosValidos = true;
                            for (String bloco : blocos) {
                                if (!validarTamanho(bloco) || !validarDacBloco(bloco)) {
                                    blocosValidos = false;
                                    break;
                                }
                            }

                            if (blocosValidos) {
                                System.out.println("Pagamento realizado com sucesso!");
                                System.out.println("Pressione ENTER para continuar...");
                                leia.nextLine();
                            } else {
                                System.out.println(
                                        "Deseja tentar novamente? 1 para continuar digitando, 2 para retornar ao menu");
                                int resposta = leia.nextInt();
                                leia.nextLine();
                                if (resposta != 1) {
                                    blocosValidos = true;
                                }
                            }
                        }
                        break;

                    case 2:
                        try {
                            System.out.println("Imprimindo a 2ª via do boleto...");

                            System.out.print("Digite o CÓDIGO (836): ");
                            String codigoBarras = leia.nextLine();

                            System.out.print("Digite o VALOR DA FATURA (165,27): ");
                            String valorFatura = leia.nextLine();
                            valorFatura = String.format("%011d", Integer.parseInt(valorFatura.replace(",", "")));

                            System.out.print("Digite a IDENTIFICAÇÃO DA EMPRESA (0148000): ");
                            String identificacaoEmpresa = leia.nextLine();
                            identificacaoEmpresa = String.format("%07d", Integer.parseInt(identificacaoEmpresa));

                            System.out.print("Digite a UNIDADE CONSUMIDORA (1020749): ");
                            String unidadeConsumidora = leia.nextLine();
                            unidadeConsumidora = String.format("%07d", Integer.parseInt(unidadeConsumidora));

                            System.out.print("Digite o ANO-MÊS (AAAAMM - 201907): ");
                            String anoMes = leia.nextLine();
                            anoMes = String.format("%06d", Integer.parseInt(anoMes));

                            System.out.print("Digite o SEQUENCIAL (101): ");
                            String sequencial = leia.nextLine();
                            sequencial = String.format("%07d", Integer.parseInt(sequencial));

                            String boleto = codigoBarras + valorFatura + identificacaoEmpresa + unidadeConsumidora
                                    + anoMes + "9" + sequencial + "9";
                            String dacGeral = calcularDac(boleto);
                            boleto = codigoBarras + dacGeral + valorFatura + identificacaoEmpresa + unidadeConsumidora
                                    + anoMes + "9" + sequencial + "9";
                            System.out.println("Segunda VIA DO BOLETO: " + boleto);
                            System.out.println("Pressione ENTER para continuar...");
                            leia.nextLine();

                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida, por favor insira valores numéricos corretamente.");
                            System.out.println("Pressione ENTER para continuar...");
                            leia.nextLine();
                        }
                        break;

                    case 3:
                        System.out.println("Programa finalizado...");
                        break;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        System.out.println("Pressione ENTER para continuar...");
                        leia.nextLine();
                        break;
                }
            } else {
                System.out.println("Entrada inválida, por favor insira um número.");
                System.out.println("Pressione ENTER para continuar...");
                leia.nextLine();
                leia.nextLine();
            }
        } while (opcaoSelecionada != 3);

        leia.close();
    }
}