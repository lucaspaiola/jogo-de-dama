/**
* @author Lucas Victorio Paiola
*/

public class Jogador {
   private String nome, corPecas; //Armazena seu nome e a cor do seu conjunto de peças
   private Peca pecas[]; //Armazena suas 12 peças

   /* Construtor da classe */
   public Jogador(String nome, Peca pecas[]) throws Exception{
      setNome(nome);
      setPecas(pecas);
   }

   //getters e setters
   public String getCorPecas() {
      return corPecas;
   }

   public String getNome() {
      return nome;
   }
   
   public Peca[] getPecas() {
      return pecas;
   }
   
   public void setNome(String nome) {
      this.nome = nome;
   }

   /* Define a cor das peças do jogador a partir das suas próprias peças. 
   Método auxiliar ao setPecas, que faz as devidas verificações.*/
   private void setCorPecas() {
      // Se for um vetor de peças válido
      if(this.pecas != null && this.pecas.length == 12)
         this.corPecas = pecas[0].getCor();
   }

   /* Define as peças do jogador. Usado pelo construtor. */
   private void setPecas(Peca[] pecas) throws Exception{
      // verifica as caracteristícas do vetor
      if(pecas == null) 
         throw new Exception("Vetor de peças não inicializado.");
      else if(pecas.length < 12) 
         throw new Exception("Vetor de peças incompleto");
      else if(pecas.length > 12)
         throw new Exception("Vetor de peças deve ter exatamente 16 peças.");
      else {
         // verifica se todas as peças são da mesma cor
         for (Peca peca : pecas)
            if (peca.getCor() != pecas[0].getCor()) 
               throw new Exception("Vetor de peças devem conter peças da mesma cor.");

         this.pecas = pecas;
         setCorPecas();
      }
   }   
}
