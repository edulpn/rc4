//Para compilar, utilize o comando: javac Rc4.java
//Para executar, utilize o comando: java Rc4 <arquivoEntrada> <arquivoSaída> <chave>
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Rc4 
{
	//Classe State é a classe que implementa o objeto gerador de sequências pseudo-aleatórias utilizadas pelo algoritmo RC4
    private static class State
    {
		//state é a propriedade do objeto State que armazena o estado atual do gerador de sequências
        private int[] state = new int[256];
        
		//Construtor da classe. Recebe a chave key fornecida pelo usuário através da linha de comando e incializa a estrutura que armazena o estado
        public State (String key)
        {
            int i;
            int j = 0;
            int swap;
            int len = key.length();
			
			//Preenche o array
            for (i = 0 ; i < 256 ; i++)
            {
                this.state[i] = i;
            }
			
			//Embaralha ("aleatoriza" a estrutura que armazena o estado) o array de acordo com a chave fornecida pelo usuário
            for (i = 0 ; i < 256 ; i++)
            {
                j = (j + this.state[i] + key.charAt((i % len))) % 256;
                swap = this.state[i]; this.state[i] = this.state[j]; this.state[j] = swap;
            }
        }
        
		//Gerador de sequências pseudo-aleatórias. Recebe um tamanho length e gera uma sequência desse comprimento
        public ArrayList<Integer> generatePseudoRandom (int length)
        {
            ArrayList<Integer> randomList = new ArrayList<Integer>();
            int i, j, aux, swap;
            i = j = 0;
			
			//Enquanto "aleatoriza" a estrutura de estado, compõe a sequência que será retornada
            for (aux = 0 ; aux < length ; aux++)
            {
                i = (i + 1) % 256;
                j = (j + this.state[i]) % 256;
                swap = this.state[i]; this.state[i] = this.state[j]; this.state[j] = swap;
                randomList.add(this.state[(this.state[i] + this.state[j]) % 256]);
            }
            return randomList;
        }
    }

    public static void main(String[] args) 
    {
        FileInputStream in = null;
        FileOutputStream out = null;
        
		//Inicializa o gerador de sequências pseudo-aleatórias com a chave fornecida via linha de comando
        State state = new State(args[2]);
        
        try 
        {    
			//Abre os arquivos de entrada e saída com os nomes fornecidos via linha de comando
            in = new FileInputStream(args[0]);
            out = new FileOutputStream(args[1]);
            int size;

            byte[] buffer = new byte[1024];
            byte[] aux = new byte[1024];

			//Lê blocos de 1KB do arquivo de entrada em buffera até terminar de ler o arquivo            
            while ((size = in.read(buffer)) != -1) 
            {
				//Gera uma sequência pseudo-aleatória de tamanho igual ao número de bytes lidos do arquivo de entrada
                ArrayList<Integer> psRdm = state.generatePseudoRandom(size);

				//Para cada byte, faz a encriptação utilizando uma operação XOR bitwise
                for (int i = 0 ; i < size ; i++)
                {
                    aux[i] = (byte)(buffer[i] ^ psRdm.get(i));
                }

				//Escreve a o buffer encriptado no arquivo de saída
                out.write(aux);
            }
            in.close();
            out.close();
        } 
        catch (IOException ex) 
        {
            System.out.println("There was a I/O failure.\n");
        }
    }
}
