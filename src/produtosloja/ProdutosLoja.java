package produtosloja;

import java.io.IOException;
import java.util.Scanner;

public class ProdutosLoja {

    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        String path = "produtos.txt";
        byte opcao;
        
        System.out.println(path);
        do {
            System.out.println("Escolha uma Opção:");
            System.out.println("1- Listar Produtos do registro");
            System.out.println("2- Listar total adquirido das vendas por categoria");
            System.out.println("3- Listar total adquirido das vendas por loja");
            System.out.println("4- Listar o valor relativo de produtos vendidos por categoria");
            System.out.println("5- Sair");
            System.out.print(">> ");
            opcao = input.nextByte();
            
            ManipuladorArquivo.escolherOpcao(path, opcao);
        } while (opcao != 5);
    }
    
}
