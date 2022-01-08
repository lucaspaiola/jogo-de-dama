/**
 * @author Lucas Victorio Paiola
 */

public class Tabuleiro {
   /* Atributo que armazena todas as 64 posições na forma de uma matriz(instanciadas no construtor) */
   private Posicao tab[][] = new Posicao[8][8];

   /* Construtor da classe */
   public Tabuleiro(Peca pecasBrancas[], Peca pecasPretas[]) throws Exception {
      if(pecasBrancas == null || pecasPretas == null || pecasBrancas.length != 12 || pecasPretas.length != 12)
         throw new Exception("Vetor inválido.");
      
      /* Configuração inicial do tabuleiro, instancia todas as 
      64 posições, com suas respectivas cores */
      String cor = "Branco";
      for (int i = 0; i < 8; i++){
         if(cor == "Preto")
            cor = "Branco";
         else
            cor = "Preto";
         for (int j = 0; j < 8; j++) {
            tab[i][j] = new Posicao(cor, i + 1, (char)(j + 97));
            if(cor == "Preto")
               cor = "Branco";
            else
               cor = "Preto";
         }
      }
         
      // atribuindo pecas brancas às suas respectivas posicoes
      boolean ultimoAtribuido = false;
      for(int i = 0, k = 0; i < 3; i++) {
         ultimoAtribuido = !ultimoAtribuido; // quando muda de linha deve trocar novamente
         for (int j = 0; j < 8; j++) {
            // se na ultima posicao foi colocada uma peca, então a proxima posição é vazia
            if(!ultimoAtribuido) {
               tab[i][j].setPeca(null);
               ultimoAtribuido = !ultimoAtribuido;
            } else {
               tab[i][j].setPeca(pecasBrancas[k++]);
               ultimoAtribuido = !ultimoAtribuido;
            }
         }
      }

      // atribuindo pecas pretas às suas respectivas posicoes
      ultimoAtribuido = true;
      for(int i = 7, k = 0; i > 4; i--) {
         ultimoAtribuido = !ultimoAtribuido; // quando muda de linha deve trocar novamente
         for (int j = 0; j < 8; j++) {
            // se a ultima posicao na ultima posicao foi colocada uma peca, então a proxima posição é vazia
            if(!ultimoAtribuido) {
               tab[i][j].setPeca(null);
               ultimoAtribuido = !ultimoAtribuido;
            } else {
               tab[i][j].setPeca(pecasPretas[k++]);
               ultimoAtribuido = !ultimoAtribuido;
            }
         }
      }
   }

/* Acessa a peça na matriz de posições e retorna uma String correspondente ao nome da classe(que 
representa o nome da peça). */
public boolean PecaNaPosicaoEhDama(int linha, char coluna){
      // Se estiver nos limites e tiver peça na Posição(evita acesso a memória "null")
      if(limitesTabuleiro(linha, coluna) && posicaoOcupada(linha, coluna))
         if(posicao(linha, coluna).getPeca().getDama())
            return true;
         else
            return false;
      else
         return false;
}

/* Acessa a matriz de posições e retorna um boolean que indica se a posição está ocupada ou não. */
public boolean posicaoOcupada(int linha, char coluna){
   // Se estiver nos limites(evita acesso a memória "null")
   if(limitesTabuleiro(linha, coluna))
      return posicao(linha, coluna).getPosicaoOcupada();
   else
      return false;
}

/* Acessa a peça na matriz de posições e retorna uma String correspondente a cor da peça. */
public String corDaPecaNaPosicao(int linha, char coluna){
   // Se a posição estiver o ocupada e nos limites(método "posicaoOcupada()" verifica os limites)
   if(posicaoOcupada(linha, coluna))
      return posicao(linha, coluna).getPeca().getCor();
   else
      return null;
}

/* Método que desenha o tabuleiro no terminal. Diferencia as cores das posições e as peças distribuídas
além de imprimir valores de referência das linhas e colunas. */
public void desenhoTabuleiro(){
      System.out.println("====== TABULEIRO ======");
      /* Imprimir referências para as colunas na parte superior do tabuleiro */
      System.out.print("    ");
      for (char i = 'a'; i <= 'h'; i++) 
         System.out.print(i + " ");
      System.out.println();
      System.out.println();
      /* Imprimir todas as posições, com suas cores e peças */
      for (int i = 7; i >= 0; i--) {
         System.out.print(i + 1 + "   "); //Valores de referência para as linhas
         for (int j = 0; j < 8; j++) {
            if(tab[i][j].getPosicaoOcupada())
                  System.out.print(tab[i][j].getPeca().desenho() + " ");
            else 
                  if(tab[i][j].getCor() == "Preto")
                     System.out.print("- ");
                  else    
                     System.out.print("+ ");
         }
         System.out.println("  " + (i + 1));
      }
      System.out.println();

      /* Imprimir referências para as colunas na parte inferior do tabuleiro */
      System.out.print("    ");
      for (char i = 'a'; i <= 'h'; i++) 
         System.out.print(i + " ");
      System.out.println();
      System.out.println("=======================");
}

/* Método que recebe um inteiro e um char, correspondente a linha e coluna respectivamente.
Retorna true caso estiver dentro dos limites do tabuleiro e false caso contrário. */
public boolean limitesTabuleiro(int linha, char coluna){
      if(linha >= 1 && linha <= 8)
         if(coluna >= 'a' && coluna <= 'h')
            return true;
      return false;
}

/* Método que checa se a jogada é válida, considerando a peça escolhida(acessando o checaMovimento da peça). 
Retorna true caso a jogada seja válida a peça. False caso contrário. */
public boolean checaMovimentoPeca(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {

   //Acessa o checaMovimento da peça, verificando se é uma jogada válida nas regras da dama para aquela peça.
   if(!posicao(linhaOrigem, colunaOrigem).getPeca().checaMovimento(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino))            
      return false;

   // Movimento válido
   return true;
}

/* Método que realiza a jogada, alterando as peças no tabuleiro se a jogada for válida. 
Só deve ser chamada se as verificações de jogada válida tiverem sido realizadas. */
public void realizarUmaJogada(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino){

   // se houver peça no destino, esta vai ser capturada e tirada do jogo
   if(posicaoOcupada(linhaDestino, colunaDestino)) {
      posicao(linhaDestino, colunaDestino).getPeca().setEmJogo(false);
   }

   // peça na posicao origem é colocada no destino e retirada da origem
   posicao(linhaDestino, colunaDestino).setPeca(posicao(linhaOrigem, colunaOrigem).getPeca());
   posicao(linhaOrigem, colunaOrigem).setPeca(null);

}

/* Método que identifica qual peça está na origem e verifica se o caminho que a peça vai fazer até
o destino é válido. Retorna true se for válido(não tiver peças no caminho, e caso houver peça no destino,
essa é de outra cor). False caso contrário. */
public boolean verificaCaminho(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
   /* Como cada peça percorre o tabuleiro de um jeito, métodos privados auxiliares fazem a checagem para
   cada tipo de peça. */

   /* Evita problemas de acesso a indice inexistente na matriz de posições */
   if(!limitesTabuleiro(linhaDestino, colunaDestino) || !limitesTabuleiro(linhaOrigem, colunaOrigem))
      return false;

   // peca
   if(!PecaNaPosicaoEhDama(linhaOrigem, colunaOrigem))
      if(verificaCaminhoPeca(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino))
         return true;
   // dama
   if(PecaNaPosicaoEhDama(linhaOrigem, colunaOrigem))
      if(verificaCaminhoDama(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino))
         return true;

   // Se a jogada não for válida
   return false;
}

/* Método que verifica se o caminho que a peca irá percorrer é válido, considerando o tabuleiro e 
as peças que nele estão dispostas. Retorna true caso não haja nenhuma peca no caminho ou uma adversária que possar ser capturada, false caso contrário */
private boolean verificaCaminhoPeca(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {
   
   // se tiver alguma peca ocupada na posicao de destino então a jogada é inválida
   if(posicaoOcupada(linhaDestino, colunaDestino))
      return false;

   /* Jogadas sem captura */

   // peca branca se movimentando na diagonal a esq
   if(linhaDestino - linhaOrigem == 1 && colunaDestino - colunaOrigem == -1)
      if(corDaPecaNaPosicao(linhaOrigem, colunaOrigem) == "Branco")
         return true;

   // peca branca se movimentando na diagonal a dir
   if(linhaDestino - linhaOrigem == 1 && colunaDestino - colunaOrigem == 1)
      if(corDaPecaNaPosicao(linhaOrigem, colunaOrigem) == "Branco")
         return true;

   // peca preta se movimentando na diagonal a esq
   if(linhaDestino - linhaOrigem == -1 && colunaDestino - colunaOrigem == -1)
      if(corDaPecaNaPosicao(linhaOrigem, colunaOrigem) == "Preto")
         return true;

   // peca preta se movimentando na diagonal a dir
   if(linhaDestino - linhaOrigem == -1 && colunaDestino - colunaOrigem == 1)
      if(corDaPecaNaPosicao(linhaOrigem, colunaOrigem) == "Preto")
         return true;


   /* Jogadas com captura */

   // peca branca se movimentando na diagonal a esq para captura
   if(linhaDestino - linhaOrigem == 2 && colunaDestino - colunaOrigem == -2) {
      if(posicaoOcupada(linhaOrigem + 1, (char) (colunaOrigem - 1)) && corDaPecaNaPosicao(linhaOrigem + 1, (char) (colunaOrigem - 1)) == "Preto") {
         posicao(linhaOrigem + 1, (char) (colunaOrigem - 1)).setPeca(null);
         return true;
      }
   }

   // peca branca se movimentando na diagonal a dir para captura
   if(linhaDestino - linhaOrigem == 2 && colunaDestino - colunaOrigem == 2) {
      if(posicaoOcupada(linhaOrigem + 1, (char) (colunaOrigem + 1)) && corDaPecaNaPosicao(linhaOrigem + 1, (char) (colunaOrigem + 1)) == "Preto") {
         posicao(linhaOrigem + 1, (char) (colunaOrigem + 1)).setPeca(null);
         return true;
      }
   }

   // peca preta se movimentando na diagonal a esq para captura
   if(linhaDestino - linhaOrigem == -2 && colunaDestino - colunaOrigem == -2) {
      if(posicaoOcupada(linhaOrigem - 1, (char) (colunaOrigem - 1)) && corDaPecaNaPosicao(linhaOrigem - 1, (char) (colunaOrigem - 1)) == "Branco") {
         posicao(linhaOrigem - 1, (char) (colunaOrigem - 1)).setPeca(null);
         return true;
      }
   }

   // peca preta se movimentando na diagonal a dir para captura
   if(linhaDestino - linhaOrigem == -2 && colunaDestino - colunaOrigem == 2) {
      if(posicaoOcupada(linhaOrigem - 1, (char) (colunaOrigem + 1)) && corDaPecaNaPosicao(linhaOrigem - 1, (char) (colunaOrigem + 1)) == "Branco") {
         posicao(linhaOrigem - 1, (char) (colunaOrigem + 1)).setPeca(null);
         return true;
      }
   }

   // se nenhum caso anterior retornar true, então a jogada é inválida
   return false;
}

/* Método que verifica se o caminho que a Dama irá percorrer é válido, considerando o tabuleiro e 
as peças que nele estão dispostas. Retorna true caso não tenha peças no caminho e no destino tiver uma 
peça adversária, ou nenhuma peça. False caso contrário.*/
private boolean verificaCaminhoDama(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino){
      
   int movimentos_verticais = linhaDestino - linhaOrigem;
   int movimentos_horizontais = colunaDestino - colunaOrigem;

   //Movimento vertical para cima da dama.
   if(movimentos_verticais > 0 && movimentos_horizontais == 0) {
      // laço que percorre o caminho da dama no tabuleiro
      for (int i = linhaOrigem; i <= linhaDestino - 1; i++) {
         // se estiver na ultima posicao e a peca no destino estiver ocupada e for do adversario
         if(i == linhaDestino - 1) {
               if(tab[i][colunaDestino - 97].getPosicaoOcupada() && corDaPecaNaPosicao(linhaOrigem, colunaOrigem) != tab[i][colunaDestino - 97].getPeca().getCor())
                  return true;
         }

         // se a peça estiver tentando "passar por cima" de uma outra peça antes de chegar ao destino
         if(tab[i][colunaOrigem - 97].getPosicaoOcupada())
               return false;
      }
      return true;
   }

   //Movimento vertical para baixo da dama.
   if(movimentos_verticais < 0 && movimentos_horizontais == 0) {
      // laço que percorre o caminho da dama no tabuleiro
      for (int i = linhaOrigem - 2; i >= linhaDestino - 1; i--) {
         // se estiver na ultima posicao e a peca no destino estiver ocupada e for do adversario
         if(i == linhaDestino - 1) {
               if(tab[i][colunaDestino - 97].getPosicaoOcupada() && corDaPecaNaPosicao(linhaOrigem, colunaOrigem) != tab[i][colunaOrigem - 97].getPeca().getCor())
                  return true;
         }

         // se a peça estiver tentando "passar por cima" de uma outra peça antes de chegar ao destino
         if(tab[i][colunaOrigem - 97].getPosicaoOcupada())
               return false;
      }
      return true;
   }
   
   //Movimento horizontal para direita da dama.
   if(movimentos_horizontais > 0 && movimentos_verticais == 0) {
      // laço que percorre o caminho da dama no tabuleiro
      for (int i = colunaOrigem - 96; i <= colunaDestino - 97; i++) {
         // se estiver na ultima posicao e a peca no destino estiver ocupada e for do adversario
         if(i == colunaDestino - 97) {
               if(tab[linhaOrigem - 1][i].getPosicaoOcupada() && tab[linhaOrigem - 1][i].getPeca().getCor() != corDaPecaNaPosicao(linhaOrigem, colunaOrigem))
                  return true;
         }

         // se a peça estiver tentando "passar por cima" de uma outra peça antes de chegar ao destino
         if(tab[linhaOrigem - 1][i].getPosicaoOcupada())
               return false;
      }
      return true;
   }

   //Movimento horizontal para esquerda da dama.
   if(movimentos_horizontais < 0 && movimentos_verticais == 0) {
      // laço que percorre o caminho da dama no tabuleiro
      for (int i = colunaOrigem - 98; i >= colunaDestino - 97; i--) {
         // se estiver na ultima posicao e a peca no destino estiver ocupada e for do adversario
         if(i == colunaDestino - 97) {
               if(tab[linhaOrigem - 1][i].getPosicaoOcupada() && tab[linhaOrigem - 1][i].getPeca().getCor() != corDaPecaNaPosicao(linhaOrigem, colunaOrigem))
                  return true;
         }

         // se a peça estiver tentando "passar por cima" de uma outra peça antes de chegar ao destino
         if(tab[linhaOrigem - 1][i].getPosicaoOcupada())
               return false;
      }
      return true;
   }

   //Movimento diagonal da dama.
   if(Math.abs(movimentos_horizontais) == Math.abs(movimentos_verticais)) {
      // Diagonal para direita e para cima
      if(movimentos_verticais > 0 && movimentos_horizontais > 0) {
         // laço que percorre o caminho da dama no tabuleiro
         for (int i = linhaOrigem, j = colunaOrigem - 96; i <= linhaDestino - 1; i++, j++) {
               // se estiver na ultima posicao e a peca no destino estiver ocupada e for do adversario
               if(i == linhaDestino - 1) {
                  if(tab[i][j].getPosicaoOcupada() && corDaPecaNaPosicao(linhaOrigem, colunaOrigem) != tab[i][j].getPeca().getCor())
                     return true;
               }

               // se a peça estiver tentando "passar por cima" de uma outra peça antes de chegar ao destino
               if(tab[i][j].getPosicaoOcupada())
                  return false;
         }
         return true;
      }

      // Diagonal para direita e para baixo
      if(movimentos_verticais < 0 && movimentos_horizontais > 0) {
         // laço que percorre o caminho da dama no tabuleiro
         for (int i = linhaOrigem - 2, j = colunaOrigem - 96; i >= linhaDestino - 1; i--, j++) {
               // se estiver na ultima posicao e a peca no destino estiver ocupada e for do adversario
               if(i == linhaDestino - 1) {
                  if(tab[i][j].getPosicaoOcupada() && corDaPecaNaPosicao(linhaOrigem, colunaOrigem) != tab[i][j].getPeca().getCor())
                     return true;
               }

               // se a peça estiver tentando "passar por cima" de uma outra peça antes de chegar ao destino
               if(tab[i][j].getPosicaoOcupada())
                  return false;
         }
         return true;
      }

      // Diagonal para esquerda e para cima
      if(movimentos_verticais > 0 && movimentos_horizontais < 0) {
         // laço que percorre o caminho da dama no tabuleiro
         for (int i = linhaOrigem, j = colunaOrigem - 98; i <= linhaDestino - 1; i++, j--) {
               // se estiver na ultima posicao e a peca no destino estiver ocupada e for do adversario
               if(i == linhaDestino - 1) {
                  if(tab[i][j].getPosicaoOcupada() && corDaPecaNaPosicao(linhaOrigem, colunaOrigem) != tab[i][j].getPeca().getCor())
                     return true;
               }

               // se a peça estiver tentando "passar por cima" de uma outra peça antes de chegar ao destino
               if(tab[i][j].getPosicaoOcupada())
                  return false;
         }
         return true;
      }

      // Diagonal para esquerda e para baixo
      if(movimentos_verticais < 0 && movimentos_horizontais < 0) {
         // laço que percorre o caminho da dama no tabuleiro
         for (int i = linhaOrigem - 2, j = colunaOrigem - 98; i >= linhaDestino - 1; i--, j--) {
               // se estiver na ultima posicao e a peca no destino estiver ocupada e for do adversario
               if(i == linhaDestino - 1) {
                  if(tab[i][j].getPosicaoOcupada() && corDaPecaNaPosicao(linhaOrigem, colunaOrigem) != tab[i][j].getPeca().getCor())
                     return true;
               }

               // se a peça estiver tentando "passar por cima" de uma outra peça antes de chegar ao destino
               if(tab[i][j].getPosicaoOcupada())
                  return false;
         }
         return true;
      }
   }
   // Qualquer outro movimento
   return false;
}

/* Recebe uma linha e coluna, e com isso acessa a matriz de posições e retorna
a Posição certa. */
private Posicao posicao(int linha, char coluna){
   // Se estiver nos limites(evita acesso a memória não alocada)
   if(limitesTabuleiro(linha, coluna))
      // Como o vetor começa em 0, e a linha em 1, é necessário subtrair a linha para acessar o índice do vetor
      // Precisamos subtrair 97 da coluna, pois na tabela de caracteres que o java usa, 'a' é o caracter número 97
      return tab[linha - 1][coluna - 97]; 
   else
      return null;
}

}
