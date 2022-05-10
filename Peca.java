/**
* @author Lucas Victorio Paiola
*/

public class Peca {
   private boolean emJogo; //Identifica se uma peça está no tabuleiro.
   private String cor; //Guarda a sua cor, podendo ser "Preto" ou "Branco".
   private boolean dama; // Armazena um boolean que indica se a peca é uma dama ou não

   /* Construtor da classe */
   public Peca(String cor) throws Exception{
      if(cor != "Branco" && cor != "Preto"){
         throw new Exception("Cor da peça inválida");
      }
      else this.cor = cor;
      this.emJogo = true;
   }

   // getters e setters
   public void setEmJogo(boolean emJogo) {
      if(this.emJogo)
         this.emJogo = emJogo;
      // se a peça ja tiver saído do jogo, ela não pode voltar.
      else
         // se "emJogo" for false, e o atributo passado for true
         if(emJogo)
            System.out.println("Peça capturada não pode voltar para o jogo. ");
   }

   public boolean getEmJogo() {
      return this.emJogo;
   }
 
   public String getCor() {
      return cor;
   }

   public boolean getDama() {
      return dama;
   }

   // só atribui verdadeiro a dama quando ela for falso, nunca o contrário
   public void setDama(boolean dama) {
      if(!this.dama && dama == true)
         this.dama = dama;
   }

   // métodos abstrastos, sobreposto por cada especialização da classe.

   /* Retorna true caso for uma jogada válida para a peça, e false caso contrário, considerando apenas as regras do jogo e não a disposição das peças no tabuleiro */
   public boolean checaMovimento(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino) {

      // Nao se moveu
      if(linhaOrigem == linhaDestino && colunaOrigem == colunaDestino)
         return false;
         
      int movimentosVerticais = linhaDestino - linhaOrigem;
      int movimentosHorizontais = Math.abs(colunaDestino - colunaOrigem);

      // se a peca nao for dama
      if(!dama) {

         // peca da cor branco que nao é dama
         if(((movimentosVerticais == 1 && movimentosHorizontais == 1) || (movimentosVerticais == 2 && movimentosHorizontais == 2)) && cor == "Branco")
            return true;

         // peca da cor preto que não é dama
         if(((movimentosVerticais == -1 && movimentosHorizontais == 1) || (movimentosVerticais == -2 && movimentosHorizontais == 2)) && cor == "Preto")
            return true;

      // se a peca for dama
      } else {
         return checaMovimentoDama(linhaOrigem, colunaOrigem, linhaDestino, colunaDestino);
      }

      // se nenhum caso retornar true, entao a jogada nao é valida
      return false;
   }

   /* Método auxiliar do checaMovimento, utilizado para verificar se o movimento requerido para a dama é valido, considerando apenas as regras do jogo e não a disposição das peças no tabuleiro */
   private boolean checaMovimentoDama(int linhaOrigem, char colunaOrigem, int linhaDestino, char colunaDestino){

      // Nao se moveu
      if(linhaOrigem == linhaDestino && colunaOrigem == colunaDestino)
         return false;
         
      int movimentosVerticais = Math.abs(linhaDestino - linhaOrigem);
      int movimentosHorizontais = Math.abs(colunaDestino - colunaOrigem);

      //Movimento vertical da dama.
      if(movimentosVerticais > 0 && movimentosHorizontais == 0)
         return true;
      
      //Movimento horizontal da dama.
      if(movimentosHorizontais > 0 && movimentosVerticais == 0)
         return true;

      //Movimento diagonal da dama.
      if(movimentosHorizontais == movimentosVerticais)
         return true;
      
      //Qualquer outro movimento.
      return false;
   } 
   
   /* Retorna um caracter correspondente a peça, considerando sua cor e se é uma dama ou não. */
   public char desenho() {
      if(getCor() == "Branco" && getDama())
         return 'B';
      else if(getCor() == "Branco" && !getDama())
         return 'b';
      else if(getCor() == "Preto" && getDama())
         return 'P';
      else
         return 'p';
   }
}
