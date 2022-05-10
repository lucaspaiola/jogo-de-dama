/**
* @author Lucas Victorio Paiola
*/

import java.util.InputMismatchException;
import java.util.Scanner;

public class Gerenciador {
   public static void main(String[] args) {
      Scanner in = new Scanner(System.in);

      System.out.println("***Bem vindo ao Jogo de Dama em Java***");
      System.out.println();

      System.out.println("Escolha uma das opções abaixo:");
      System.out.println("1- Iniciar um novo jogo");
      System.out.println("2- Carregar um jogo salvo anteriormente");

      // Laço que recebe a opcao de iniciar um novo jogo ou carregar um anterior, até que ela seja válida
      int opcao;
      while(true){
         try {
            opcao = in.nextInt();
            if(opcao == 1 || opcao == 2) break; // opcao valida sai do while
            System.out.println("Opção inválida, digite novamente:");
         }
         catch(InputMismatchException e) {
            System.out.println("Valor digitado deve ser um inteiro. Digite novamente:");
            in.nextLine();
         }
         catch(Exception e) {
            System.out.println("Ocorreu um erro. Digite novamente:");
            in.nextLine();
         }
      }

      /* criando um objeto para o jogo atual. */
      Jogo jogo;

      /* Opcao de iniciar um novo jogo */
      if(opcao == 1) {
         System.out.println("Digite o nome do primeiro jogador: ");
         String jogador1 = in.next();
         
         int jogador1Cor;
         System.out.println("Qual a cor das suas peças? ");
         System.out.println("1 - Brancas");
         System.out.println("2 - Pretas");
         
         //Laço que recebe de cor da peças até ela ser válida
         while(true){
            try {
               jogador1Cor = in.nextInt();
               if(jogador1Cor == 1 || jogador1Cor == 2) break; // opcao válida sai do while
               System.out.println("Opção inválida, digite novamente:");
            }
            catch(InputMismatchException e) {
               System.out.println("Valor digitado deve ser um inteiro. Digite novamente:");
               in.nextLine(); // limpa o buffer, necessario para nao pegar o '\n' do enter no proximo scanner 
            }
            catch(Exception e) {
               System.out.println("Ocorreu um erro. Digite novamente:");
               in.nextLine(); // limpa o buffer, necessario para nao pegar o '\n' do enter no proximo scanner 
            }
         }

         System.out.println("Digite o nome do segundo jogador: ");
         String jogador2 = in.next();

         /* Jogador com as peças brancas inicia */
         if(jogador1Cor == 1) //jogador1Cor == Branco
            jogo = new Jogo(jogador1, jogador2);
         else //jogador1Cor == preto
            jogo = new Jogo(jogador2, jogador1);
      }

      /* Opcao de carregar um jogo */
      else // if(opcao == 2)
         jogo = new Jogo();
      
      /* Dispara o jogo */
      jogo.iniciarJogo();
      
      in.close();
   }
}
