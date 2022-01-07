/**
 * @author Lucas Victorio Paiola
 */

public class Posicao {
   private String cor; // armazena a cor da posição
   private int linha; // armazena a linha da posição, entre 1 a 8
   private char coluna; // armazena a coluna da posição,entre 'a' a 'h'
   private boolean posicaoOcupada; // informação que mostra se tem ou não uma peça na posição
   private Peca peca; // qual peça a ocupa

   /* Construtor da classe */
   public Posicao(String cor, int linha, char coluna) throws Exception{
      setCor(cor);
      setLinha(linha);
      setColuna(coluna);
      this.peca = null;
   }

   /* Get usado para desenhar o tabuleiro */
   public String getCor() {
      return cor;
   }

   public int getLinha() {
      return linha;
   }

   public char getColuna() {
      return coluna;
   }
   
   public boolean getPosicaoOcupada() {
      return posicaoOcupada;
   }

   public Peca getPeca() {
      return peca;
   }

   public void setPeca(Peca peca) {
      this.peca = peca;
      // Se a peça inserida for nula, a posição está vazia
      if(peca != null) 
         posicaoOcupada = true;
      else
         posicaoOcupada = false;
   }

   /* Set usado pelo construtor */
   private void setCor(String cor) throws Exception {
      if(cor != "Branco" && cor != "Preto"){
         this.cor = null;
         throw new Exception("Cor da posição inválida.");
      }
      else
         this.cor = cor;
   }

   /* Set usado pelo construtor */
   private void setLinha(int linha) throws Exception {
      if(linha < 1 || linha > 8) throw new Exception("Linha da posição fora dos limites do tabuleiro.");
      else this.linha = linha;
   }

   /* Set usado pelo construtor */
   private void setColuna(char coluna) throws Exception {
      if(coluna < 'a' || coluna > 'h') throw new Exception("Coluna da posição fora dos limites do tabuleiro.");
      else this.coluna = coluna;
   }
}
