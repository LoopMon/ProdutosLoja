package produtosloja;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ManipuladorArquivo {
    
    // Retorna os registros do arquivo.txt
    private static String[] lerArquivo(String path) throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(path));
        String[] registros;
        String arquivo = "";
        String registro = buffRead.readLine();
        
        while (true) {
            if (registro != null) {
                arquivo += registro + ";";
            } else {
                break;
            }
            registro = buffRead.readLine();
        }
        buffRead.close();
        
        registros = arquivo.split("[;]");
        
        return registros;
    }
    
    // Retorna todos os dados de uma coluna específica
    private static String[] pegarCampos(String[] registros, int coluna) {
        String[] registro;
        String[] campo = new String[registros.length];
        
        for (int i = 0, tamVetor = registros.length; i < tamVetor; i++) {
            // Separar as linhas por regex
            registro = registros[i].split("[|]");
            // Adicionar apenas o campo escolhido no vetor de campo[]
            campo[i] = registro[coluna];
        }
        
        return campo;
    }
    
    private static String[] removerElementosRepetidos(String[] vetor) {
        // variável para armazenar elementos únicos do vetor
        String elementos = vetor[0]+"|";
        boolean igual;
        for (int i = 1, tamVetor = vetor.length; i < tamVetor; i++) {
            igual = false;
            for (int j = 0; j < i; j++) {
                if (vetor[i].equals(vetor[j])) {
                    igual = true;
                    break;
                }
            }
            // Se o campo vetor[i] for diferente de seus antecessores
            // ele será adicionado na String elementos
            if (!igual) {
                elementos += vetor[i]+"|";
            }
        }
        
        return elementos.split("[|]");
    }
    
    // 1- Listar os itens do arquivo.txt
    public static void listarProdutos(String path) throws IOException {
        String[] registros = lerArquivo(path);
        String[] registro;
        
        for (int i = 0, tamVetor = registros.length; i < tamVetor; i++) {
            registro = registros[i].split("[|]");
            // Mostrar info. de cada produto
            System.out.printf("=-=-=-=-= Código %s =-=-=-=-=\n", registro[0]);
            System.out.println("Nome: " + registro[1]);
            System.out.println("Categoria: " + registro[2]);
            System.out.println("Local: " + registro[3]);
            System.out.println("Preço: R$" + registro[5]);
            System.out.println("Unidades Vendidas: " + registro[4]);
            double totalVendido = Double.parseDouble(registro[4]) * Double.parseDouble(registro[5]);
            System.out.printf("Total vendido: R$%.2f\n", totalVendido);
            System.out.println("Total em estoque: " + registro[6]);
            int restante = Integer.parseInt(registro[6]) - Integer.parseInt(registro[4]);
            System.out.println("Unidades restando: " + restante);
        }
    }
    
    // 2- Calcular total de vendas por cada categoria dos produtos
    public static void listarTotalPorCategoria(String path) throws IOException {
        String[] registros = lerArquivo(path);
        String[] registro;
        String[] categorias;
        
        // Pegar todas as categorias dos produtos
        categorias = pegarCampos(registros, 2);
        categorias = removerElementosRepetidos(categorias);
        
        double[] totCategorias;
        totCategorias = new double[categorias.length];
        totCategorias[0] = 0;
        // Calcular o total de vendas por categoria
        for (int i = 0, tamVetor = categorias.length; i < tamVetor; i++) {
            for (int j = 0, tamArquivo = registros.length; j < tamArquivo; j++) {
                registro = registros[j].split("[|]");
                
                // Somar total por cada categoria
                if (categorias[i].equals(registro[2])) {
                    double total = Double.parseDouble(registro[4]) * Double.parseDouble(registro[5]);
                    totCategorias[i] += total;
                }
            }
        }
        
        // Exibindo total por categoria
        for (int i = 0, tamVetor = categorias.length; i < tamVetor; i++) {
            System.out.println("=-=-=-= " + categorias[i] + " =-=-=-=");
            System.out.printf("Total vendido: R$%.2f\n", totCategorias[i]);
        }
    }
    
    // 3- Listar total adquirido das vendas por loja
    public static void listarTotalVendidoPorLoja(String path) throws IOException {
        String[] registros = lerArquivo(path);
        String[] registro;

        double totalLojaA = 0;
        double totalLojaB = 0;

        for (String linha : registros) {
            registro = linha.split("[|]");
            String loja = registro[3];
            double valorVenda = Double.parseDouble(registro[4]) * Double.parseDouble(registro[5]);

            if ("A".equals(loja)) {
                totalLojaA += valorVenda;
            } else if ("B".equals(loja)) {
                totalLojaB += valorVenda;
            }
        }

        System.out.println("=-=-=-= Lojas =-=-=-=");
        System.out.printf("Total vendido pela loja A: R$%.2f\n", totalLojaA);
        System.out.printf("Total vendido pela loja B: R$%.2f\n",totalLojaB);
    }
    
    // 4- Mostrar valor absoluto e relativo de cada categoria
    public static void listarValorRelativoAbsolutoVendidoPorCategoria(String path) throws IOException {
        String[] registros = lerArquivo(path);
        String[] registro;
        String[] categorias;
        double[] absoluto;
        double[] totalProdutos;
        double[] relativo;
        
        categorias = pegarCampos(registros, 2);
        categorias = removerElementosRepetidos(categorias);
        
        absoluto = new double[categorias.length];
        relativo = new double[categorias.length];
        totalProdutos = new double[categorias.length];
        absoluto[0] = 0;
        relativo[0] = 0;
        totalProdutos[0] = 0;
        
        for (int i = 0; i < registros.length; i++) {
            registro = registros[i].split("[|]");
            for (int j = 0; j < categorias.length; j++) {
                if (categorias[j].equals(registro[2])) {
                    absoluto[j] += Integer.parseInt(registro[4]);
                    totalProdutos[j] += Integer.parseInt(registro[6]);
                }
            }
        }
        
        for (int i = 0, tamVetor = categorias.length; i < tamVetor; i++) {
            relativo[i] = absoluto[i] / totalProdutos[i] * 100;
        }
        
        // Exibindo valores por categoria
        for (int i = 0, tamVetor = categorias.length; i < tamVetor; i++) {
            System.out.println("=-=-=-= " + categorias[i] + " =-=-=-=");
            System.out.printf("Total de Produtos: %.0f\n", totalProdutos[i]);
            System.out.printf("Absoluto: %.0f\n", absoluto[i]);
            System.out.printf("Relativo: %.1f%%\n", relativo[i]);
        }
    }
    
    public static void escolherOpcao(String path, byte opcao) throws IOException {
        switch (opcao) {
            case 1:
                // Listar Produtos do registro
                listarProdutos(path);
                System.out.println("=-=-=-=-=-=-=-=-=-=-=");
                break;
            case 2:
                // Listar total adquirido das vendas por categoria
                listarTotalPorCategoria(path);
                System.out.println("=-=-=-=-=-=-=-=-=-=-=");
                break;
            case 3:
                // Listar total adquirido das vendas por loja
                listarTotalVendidoPorLoja(path);
                System.out.println("=-=-=-=-=-=-=-=-=-=-=");
                break;
            case 4: 
                // Listar o valor relativo de produtos vendidos por categoria
                listarValorRelativoAbsolutoVendidoPorCategoria(path);
                System.out.println("=-=-=-=-=-=-=-=-=-=-=");
                break;
            case 5: 
                // Finalizar o Programa
                System.out.println("=-=-=-=-=-=-=-=-=-=-=");
                System.out.println("Fim do Programa...");
                break;
            default:
                System.out.println("Opção inválida...");
                System.out.println("=-=-=-=-=-=-=-=-=-=-=");
        }
    }
}
