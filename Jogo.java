/**
 * @author Lucas Victorio Paiola
*/

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/* Responsável pelo gerenciamento do jogo, sendo a principal classe que se comunica com os usuários. */
public class Jogo {

   // Scanner que receberá as jogadas
   private Scanner in = new Scanner(System.in);

   // ArrayList que armazena o historico do jogo
   private ArrayList<String> historicoJogo;

   // inicializada em 1 e é incrementada a cada iteração do while do iniciarJogo()
   // caso seja carregado um arquivo, a variavel sera incrementada tambem no construtor
   private int rodada = 1; 

   //Define qual jogador esta jogando na rodada.
   private int turno;

   /* Constantes com Strings referentes ao estado em que o jogo se encontra */
   private final String ESTADO_INICIAL = "Início";
   private final String ESTADO_FIM = "Fim";
   /* --------------------------------------------------------------------- */
   private String estadoJogo; // armazena qual o estado atual do jogo(início do jogo, captura, fim)
   
   private Tabuleiro tabuleiro; // Armazena um tabuleiro com 64 posições
   
   //contém 24 peças, sendo 12 de uma cor, e 12 de outra
   private Peca pecasBrancas[] = new Peca[12];
   private Peca pecasPretas[] = new Peca[12]; 
   //OBS: Vetor de pecas de Jogador aponta para os mesmos vetores acima
   
   private Jogador jogador[] = new Jogador[2]; // armazena os dois jogadores.

   /* Contrutor da classe, usado quando o usuario escolhe iniciar um novo jogo, instancia tabuleiro, peças e jogadores. Inicializa estado de jogo, turno e um ArrayList para salvar o histórico da partida */
   public Jogo(String nomeJogadorPecasBrancas, String nomeJogadorPecasPretas){

      // instacia o ArrayList que armazenará o historico do jogo
      historicoJogo = new ArrayList<>();

      // armazena o nome dos jogadores no ArrayList, procedimento necessario para caso o jogo seja carregado futuramente a partir de um arquivo
      historicoJogo.add(nomeJogadorPecasBrancas);
      historicoJogo.add(nomeJogadorPecasPretas);

      // metodo que instacia os elementos do jogo
      cfgInicial(nomeJogadorPecasBrancas, nomeJogadorPecasPretas);
   }

   /* Contrutor da classe, usado quando o usuario escolhe carregar um jogo já iniciado, instancia tabuleiro, peças e jogadores. Carrega estado de jogo e turno a partir de um arquivo e salva em um ArrayList a continuação do jogo */
   public Jogo() {

      System.out.println("Digite o nome do arquivo em que o jogo foi salvo incluindo a extensão '.txt':");
         
      // recebe entrada do usuario ate que um nome de arquivo valido seja digitado
      while(true) {
         try {
               String nomeArquivo = in.nextLine();
               // If que verifica a extensao do arquivo, sempre deve terminar em .txt
               if(nomeArquivo.endsWith(".txt")) {
                  carregarJogo(nomeArquivo);
                  break; // opcao valida sai do while
               } else {
                  System.out.println("O arquivo deve conter a extensão '.txt'. Digite novamente:");
               }
         } catch (FileNotFoundException e) {
               System.out.println("Arquivo não encontrado. Digite novamente:");
         }
      }
      // metodo que instacia os elementos do jogo
      cfgInicial(historicoJogo.get(0), historicoJogo.get(1));

      int linhaOrigem, linhaDestino;
      char colunaOrigem, colunaDestino;

      // laço que refaz todas as jogadas da partida salva, retomando a partida de onde ela foi paralisada
      // nao sao feitas verificacoes de jogadas aqui pois elas ja foram verificadas antes do jogo ser salvo, apenas do verificaCaminho para que a captura seja realizada
      for (int i = 2; i < historicoJogo.size(); i += 2) {
         // recebe a origem 
         linhaOrigem = Integer.parseInt(historicoJogo.get(i).substring(0, 1));
         colunaOrigem = historicoJogo.get(i).charAt(2);

         // recebe o destino
         linhaDestino = Integer.parseInt(historicoJogo.get(i + 1).substring(0, 1));
         colunaDestino = historicoJogo.get(i + 1).charAt(2);

         // realiza a jogada 
         tabuleiro.verificaCaminho(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);
         tabuleiro.realizarUmaJogada(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);

         // incrementa a rodada e troca de turno
         rodada++;
         trocarTurno();
      }
      // após todas as jogadas serem refeitas, verifica o estado atual do jogo
      atualizaEstadoJogo(jogador[turno - 1].getPecas());
   }

   /* Método que dispara o jogo */
   public void iniciarJogo() {

      char colunaOrigem, colunaDestino;
      int linhaOrigem = 0, linhaDestino = 0;

      System.out.println("Para interromper o jogo, digite -1 antes de selecionar a peça");

      // Laço que inicia rodadas até o jogo acabe
      while(this.estadoJogo != ESTADO_FIM) {
         
         /* Apresentação da rodada no terminal para o usuário */
         System.out.println("****** Rodada " + rodada + " ******");

         System.out.println(jogador[this.turno - 1].getNome() + ", é a sua vez!");
         tabuleiro.desenhoTabuleiro();
         /* ------------------------------------------------- */

         //Laço que pede entradas ao usuário enquanto elas forem incorretas
         while(true) {
               
            // recebe as entradas da origem, so sai do while quando elas forem válidas
            do {
               // informa para o usuário qual jogador é a vez, e o que ele deve fazer.
               System.out.println(jogador[this.turno - 1].getNome() + ", qual posição da peça que será movimentada? ");
               
               /* Recebe dados do usuário */
               while(true) {
                  try {
                     linhaOrigem = in.nextInt();
                     if(linhaOrigem == -1) {
                        salvarJogo();
                        return;
                     }
                     colunaOrigem = in.next().charAt(0);
                     // dispara a exceção se a coluna não for uma letra
                     if(!Character.isLetter(colunaOrigem)) throw new InputMismatchException();
                     // transforma em minuscula se a coluna for uma letra maiuscula
                     if(Character.isUpperCase(colunaOrigem))
                        Character.toLowerCase(colunaOrigem);
                     // se a coluna for uma letra minuscula
                     break;
                  }
                  /* Se por acaso o usuario digitar algo que não seja um int para a linha ou uma letra para a coluna */
                  catch(InputMismatchException e) {
                     System.out.println("A coordenada deve seguir a sintaxe de um inteiro seguido de uma letra. Digite novamente:");
                     in.nextLine();
                  }
                  /* Qualquer outra exceção que possar ocorrer */
                  catch(Exception e) {
                     System.out.println("Ocorreu um erro. Digite novamente:");
                     in.nextLine();
                  }
               }
               /* ----------------------- */

               if(!verificaOrigem(linhaOrigem, colunaOrigem)) {
                  // Se as coordenadas forem inválidas, pede ao usário digitar novamente e repete o laço
                  System.out.println("Digite novamente: ");
               } else 
                  // Se as coordenadas forem válidas, sai do while
                  break;
            } while(true);

            /* Recebe as entradas do destino */
            System.out.println("Para onde deseja mover sua peça?");

            while(true) {
               try {
                  linhaDestino = in.nextInt();
                  colunaDestino = in.next().charAt(0);
                  // dispara a exceção se a coluna não for uma letra
                  if(!Character.isLetter(colunaDestino)) throw new InputMismatchException();
                  // transforma em minuscula se a coluna for uma letra maiuscula
                  if(Character.isUpperCase(colunaDestino))
                     Character.toLowerCase(colunaDestino);
                  // se a coluna for uma letra minuscula
                  break;
               }
               /* Se por acaso o usuario digitar algo que não seja um int para a linha ou uma letra para a coluna */
               catch(InputMismatchException e) {
                  System.out.println("A coordenada deve seguir a sintaxe de um inteiro seguido de uma letra. Digite novamente:");
                  in.nextLine();
               }
               /* Qualquer outra exceção que possar ocorrer */
               catch(Exception e) {
                  System.out.println("Ocorreu um erro. Digite novamente:");
                  in.nextLine();
               }
            }
            /* ----------------------------- */

            /* Começa a verificação da validade da jogada */

            // Verifica se o destino não está nos limites do tabuleiro
            if(!tabuleiro.limitesTabuleiro(linhaDestino, colunaDestino)) {
               System.out.println("Jogada inválida, destino fora dos limites do tabuleiro. Digite novamente: ");
            } else {
               // Verifica se o movimento é valido de acordo com as regras da dama
               if(!tabuleiro.checaMovimentoPeca(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) {
                  System.out.println("Jogada inválida para essa peça! Digite novamente: ");
               } else {
                  // Verifica o caminho que a peça pretende percorrer
                  if(!tabuleiro.verificaCaminho(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino)) {
                     System.out.println("Jogada inválida. A peça não pode percorrer esse caminho. Digite novamente:");
                  } else {
                     // se o caminho está livre e válido
                     // realiza a jogada normalmente
                     tabuleiro.realizarUmaJogada(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);
                     break; //quebra o laço para se iniciar uma nova rodada
                  }
               }
            }   
         }
         // fim do laço que verifica as jogadas, chegará aqui quando a jogada for totalmente válida e tiver sido realizada

         // Salva as jogadas válidas no histórico
         historicoJogo.add(String.valueOf(linhaOrigem) + ' ' + colunaOrigem);
         historicoJogo.add(String.valueOf(linhaDestino) + ' ' + colunaDestino);
         
         trocarTurno();
         rodada++;

         //verifica se o jogador adversário ainda tem peças restantes no tabuleiro
         atualizaEstadoJogo(jogador[turno - 1].getPecas());
      }

      // imprime vencedor 
      trocarTurno();
      tabuleiro.desenhoTabuleiro();
      System.out.println("Fim de Jogo: " + jogador[turno - 1].getNome() + " venceu!!");
      in.close();
   }

   /* Metodo que instancia as pecas, o tabuleiro e os jogadores do jogo, além de iniciar o turno e definir estado do jogo como incial */
   private void cfgInicial(String nomeJogadorPecasBrancas, String nomeJogadorPecasPretas) {

      /* Como nossas peças não são instanciadas através de entrada do usuário,
      o vetor e as cores das peças nunca serão incorretas(e consequentemente,
      Jogador e Tabuleiro também não), entretanto, mesmo assim é necessário 
      try/catchs pois os construtores disparam exceções. */

      // instanciacao das pecas brancas
      try{    
         for(int i = 0; i < 12; i++)
            pecasBrancas[i] = new Peca("Branco");
      }
      catch(Exception e){
         System.out.printf("Ocorreu um erro na criação das peças brancas: ");
         System.out.println(e.getMessage());
      }
      
      // instanciacao das pecas pretas
      try{   
         for(int i = 0; i < 12; i++)
            pecasPretas[i] = new Peca("Preto");
      }
      catch(Exception e){
         System.out.printf("Ocorreu um erro na criação das peças pretas: ");
         System.out.println(e.getMessage());
      }

      // instanciacao dos jogadores
      try {
         jogador[0] = new Jogador(nomeJogadorPecasBrancas, pecasBrancas);
         jogador[1] = new Jogador(nomeJogadorPecasPretas, pecasPretas);
      } 
      catch (Exception e) {
         System.out.printf("Ocorreu um erro na criação dos jogadores: ");
         System.out.println(e.getMessage());
      }

      this.turno = 1; // Como o jogador 1(indice 0 no vetor de jogadores) sempre tem as peças brancas, ele começa
      setEstadoJogo(ESTADO_INICIAL);

      // instanciacao do tabuleiro 
      try{
         tabuleiro = new Tabuleiro(pecasBrancas, pecasPretas);
      }
      catch(Exception e){
         System.out.printf("Ocorreu um erro na criação do tabuleiro: ");
         System.out.println(e.getMessage());
      }
   }

   /* Método que recebe um ArrayList contendo o historico do jogo e salva em um arquivo escolhido pelo usuario */
   private void salvarJogo() {

      in.nextLine(); // necessario para limpar o buffer
      System.out.println("Digite o nome do arquivo em que deseja salvar o jogo com a extensão .txt:");

      File arquivo;
      String nomeArquivo;

      // Recebe o nome do arquivo até que seja válido
      while(true) {

         // laco que recebe o nome do arquivo ate que a extensao seja .txt
         while(true) {
               nomeArquivo = in.nextLine();
               // If que verifica a extensao do arquivo, sempre deve terminar em .txt
               if(!nomeArquivo.endsWith(".txt"))
                  System.out.println("O arquivo deve conter a extensão '.txt'. Digite novamente:");
               else
                  break;
         }
         
         arquivo = new File(nomeArquivo);
         try { 
               if (arquivo.createNewFile()) {
                  // Arquivo criado com sucesso
                  System.out.println("Arquivo: " + arquivo.getName() + " criado com sucesso");
                  break;
               } 
               else {
                  // Ja existe arquivo com esse nome
                  System.out.println("Arquivo já existente. Escolha uma opção:");
                  System.out.println("1- Sobrescrevê-lo");
                  System.out.println("2- Digitar novamente um outro nome de arquivo");
                  
                  int opcao;
                  // recebe a opcao do usuario de sobrescrever um arquivo ja existente ou criar outro, ate que a opcao seja valida
                  while(true){
                     try {
                           opcao = in.nextInt();
                           in.nextLine();
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

                  // opcao de sobrescrever um arquivo ja existente
                  if(opcao == 1)
                     break; // sai do while
                  // opcao de digitar novamente um nome de arquivo
                  else
                     System.out.println("Digite o nome do arquivo:");
                     // refaz o laço
                  
               }
         } catch (IOException e) {
               // Qualquer outro erro
               System.out.println("Ocorreu um erro. Digite novamente o nome do arquivo:");
         }
      }

      // escreve as jogadas no arquivo escolhido pelo usuario
      try {
         FileWriter myWriter = new FileWriter(arquivo.getName());

         // permance no while enquanto ainda tem Strings no ArrayList
         while(!historicoJogo.isEmpty())
               myWriter.write((String) historicoJogo.remove(0) + "\n");

         myWriter.close();
         System.out.println("Sucesso ao salvar o jogo");
      } 
      catch (IOException e) {
         System.out.println("Ocorreu um erro");
      }
   }

   /* Método que lê um arquivo e o carrega no ArrayList de histórico de jogadas */
   private void carregarJogo(String nomeArquivo) throws FileNotFoundException {

      File myObj = new File(nomeArquivo); // se o arquivo nao for encontrado o metodo dispara uma exceção 
      historicoJogo = new ArrayList<>(); // instancia o atributo da classe ArrayList para armazenar o historico do jogo
      Scanner myReader = new Scanner(myObj);

      // permanece no while enquanto tiver dados no arquivo
      while (myReader.hasNextLine())
         historicoJogo.add(myReader.nextLine());

      myReader.close();

   }

   /* Set usado internamente pelo método atualizaEstadoJogo() */
   private void setEstadoJogo(String estadoJogo) {    
      if(estadoJogo != ESTADO_INICIAL && estadoJogo != ESTADO_FIM)
         System.out.println("Estado inválido.");
      else
         this.estadoJogo = estadoJogo;
   }

   /* Alterna entre os jogadores
   turno == 1: jogador[0] joga
   turno == 2: jogador[1] joga */
   private void trocarTurno(){
      // se turno atual for do jogador 1, o próximo turno é do jogador 2
      if(this.turno == 1)
         this.turno = 2;
      else
      // se turno atual for do jogador 2, o próximo turno é do jogador 1
         this.turno = 1;
   }

   /* Método que verifica o tabuleiro no momento em que foi chamado, e altera o estado de jogo entre
   estado inicial e final. Recebe um vetor de peças que é percorrido para analisar se há alguma peça em jogo ainda. */
   private void atualizaEstadoJogo(Peca pecasAdversarias[]) {
      /* pecasAdversarias é o vetor de peças adversário ao jogador que está com a vez no turno. */

      boolean pecasRestantes = false; // flag utilizada para verificar se ainda há alguma peça adversária em jogo

      // percorre as peças e analisa se alguma ainda está em jogo
      for(int i = 0; i < pecasAdversarias.length; i++) {
         if(pecasAdversarias[i].getEmJogo() == true) {
            pecasRestantes = true;
            break;
         }
      }

      if(!pecasRestantes)
         setEstadoJogo(ESTADO_FIM);
      else
         setEstadoJogo(ESTADO_INICIAL);

   }

   /* Método que verifica se a origem que o usuário digitou é válida. Retorna true se a coordenada 
   tiver nos limites do tabuleiro e conter uma peça sua. False caso contrário.*/
   private boolean verificaOrigem(int linhaOrigem, char colunaOrigem) {

      // se não estiver nos limites
      if(!tabuleiro.limitesTabuleiro(linhaOrigem, colunaOrigem)) {
         System.out.println("Jogada invalida, fora dos limites do tabuleiro. Digite novamente:");
         return false;
      }

      // se nessa posicao nao tiver nenhuma peca
      else if(!tabuleiro.posicaoOcupada(linhaOrigem, colunaOrigem)) {
         System.out.println("Jogada inválida, não há peça nessa posição. Digite novamente: ");
         return false;
      }

      // se tiver peça e esta pertencer ao adversário
      else if(tabuleiro.corDaPecaNaPosicao(linhaOrigem, colunaOrigem) != jogador[turno - 1].getCorPecas()) {
         System.out.println("Jogada inválida, peça pertencente ao jogador adversário.");
         return false;
      }

      return true; // jogada válida
   }

}
