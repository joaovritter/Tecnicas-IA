import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import busca.Heuristica;
import busca.BuscaLargura;
import busca.BuscaProfundidade;
import busca.Estado;
import busca.MostraStatusConsole;
import busca.Nodo;
import javax.swing.JOptionPane;

/**
 * Classe que representa um jogo de labirinto com obstáculos.
 * O objetivo é encontrar a saída a partir de duas possíveis entradas
 * usando busca em largura ou profundidade.
 */
public class LabirintoObstaculos implements Estado, Heuristica {


    @Override
    public String getDescricao() {
        return "O jogo do labirinto é uma matriz NxM, onde são sorteadas duas peças:\n"
                + "peça que representa o portal de entrada no labirinto;\n"
                + "peça que representa o portal de saída no labirinto.\n"
                + "A Entrada é o portal em que um personagem qualquer inicia no labirinto e precisa se movimentar até a Saída.\n"
                + "O foco aqui é chegar na Saída pelo menor número de movimentos (células), mas não pode ser nas diagonais.";
    }
    
    final char matriz[][]; //Matriz do labirinto (final = imutável)
    int linhaEntrada1, colunaEntrada1; // Guarda a posição da primeira entrada (E1)
    int linhaEntrada2, colunaEntrada2; // Guarda a posição da segunda entrada (E2)
    int linhaSaida, colunaSaida; // Posição da saída (S)
    final String op; // operação que gerou o estado

    // Atenção... Matrizes precisam ser clonadas ao gerarmos novos estados
    char[][] clonar(char origem[][]) {
        char destino[][] = new char[origem.length][origem.length];
        for (int i = 0; i < origem.length; i++) {
            for (int j = 0; j < origem.length; j++) {
                destino[i][j] = origem[i][j];
            }
        }
        return destino;
    }

     /**
     * Construtor para criar um novo estado a partir de uma matriz existente.
     * @param m Inicializa a matriz do labirinto com a matriz fornecida
     * @param o Armazena a operação que gerou o estado atual
     */
    public LabirintoObstaculos(char m[][], int linhaEntrada1, int colunaEntrada1, int linhaEntrada2, int colunaEntrada2, int linhaSaida, int colunaSaida, String o) {
        this.matriz = m;
        this.linhaEntrada1 = linhaEntrada1;
        this.colunaEntrada1 = colunaEntrada1;
        this.linhaEntrada2 = linhaEntrada2;
        this.colunaEntrada2 = colunaEntrada2;
        this.linhaSaida = linhaSaida;
        this.colunaSaida = colunaSaida;
        this.op = o;
    }

    /**
     * Construtor que inicializa o labirinto com entradas, saída e obstáculos aleatórios.
     * @param dimensao Tamanho da matriz NxN.
     * @param o Descrição do estado inicial.
     * @param porcentagemObstaculos Percentual de células ocupadas por obstáculos.
     * 
     * Gera aleatoriamente as entradas e saída, garantindo que não sejam iguais
     * Preenche a matriz
     */
    public LabirintoObstaculos(int dimensao, String o, int porcentagemObstaculos) {
        this.matriz = new char[dimensao][dimensao]; //
        this.op = o;

        int quantidadeObstaculos = (dimensao * dimensao) * porcentagemObstaculos / 100;
        System.out.println(quantidadeObstaculos);

        Random gerador = new Random();

         // Gera aleatoriamente a posição da primeira entrada
        int entrada1 = gerador.nextInt(dimensao * dimensao);
        int entrada2;
        do { 
            entrada2 = gerador.nextInt(dimensao * dimensao); // Gera aleatoriamente a posição da segunda entrada
        } while (entrada1 == entrada2); // Garante que as entradas não são iguais

        int saida;
        do {
            saida = gerador.nextInt(dimensao * dimensao); // Gera aleatoriamente a posição da saída
        } while (entrada1 == saida || entrada2 == saida); // Garante que a saída não seja igual as entradas

        //Preencher Matriz
        int contaPosicoes = 0;
        for (int i = 0; i < dimensao; i++) {
            for (int j = 0; j < dimensao; j++) {
                //Primeira Entrada: posição - linha - coluna
                if (contaPosicoes == entrada1) { 
                    this.matriz[i][j] = 'E';
                    this.linhaEntrada1 = i;  
                    this.colunaEntrada1 = j;

                //Segunda Entrada: posição - linha - coluna
                } else if (contaPosicoes == entrada2) { 
                    this.matriz[i][j] = 'E'; 
                    this.linhaEntrada2 = i; 
                    this.colunaEntrada2 = j; 

                //Saída: posição - linha - coluna
                } else if (contaPosicoes == saida) {
                    this.matriz[i][j] = 'S';       
                    this.linhaSaida = i;
                    this.colunaSaida = j;

                } else if (quantidadeObstaculos > 0 && gerador.nextInt(3) == 1) {
                    quantidadeObstaculos--;
                    this.matriz[i][j] = '@'; // Obstáculo
                } else {
                    this.matriz[i][j] = 'O'; // Espaço livre na posição
                }
                contaPosicoes++; 
            }
        }
    }
    /**
     * Verifica se o estado é meta (saída)
     * O objetivo é verificar se alguma entrada chegou à saída
     */
    @Override
    public boolean ehMeta() {
        return (this.linhaEntrada1 == this.linhaSaida && this.colunaEntrada1 == this.colunaSaida) || 
               (this.linhaEntrada2 == this.linhaSaida && this.colunaEntrada2 == this.colunaSaida);
    }

    /**
     * Custo do movimento
     * Retorna 1, pois todo movimento tem o mesmo custo
     */
    @Override
    public int custo() {
        return 1;
    }

    /**
     * Heurística (não implementada)
     */
    @Override
    public int h() {
        return 0;
    }

    /**
     * Gera uma lista de sucessores do nodo (estados possíveis a partir do atual).
     * Cada sucessor é um estado alcançado movendo uma entrada para uma posição válida.
     * Retorna Lista de estados sucessores.
     */
    @Override
    public List<Estado> sucessores() {
        List<Estado> visitados = new LinkedList<Estado>(); // A lista de sucessores
        paraCima(visitados);
        paraBaixo(visitados);
        paraEsquerda(visitados);
        paraDireita(visitados);
        return visitados;
    }

    // Métodos para mover as entradas
    private void paraCima(List<Estado> visitados) {
        // Verifica se é possível mover para cima (se não está na borda e não há obstáculo)
        if (this.linhaEntrada1 > 0 && this.matriz[this.linhaEntrada1 - 1][this.colunaEntrada1] != '@') {
            char[][] mTemp = clonar(this.matriz); //Clona a matriz para não alterar o estado atual
            mTemp[this.linhaEntrada1][this.colunaEntrada1] = 'O'; //Marca a posição anterior como espaço livre
            mTemp[this.linhaEntrada1 - 1][this.colunaEntrada1] = 'E'; //Move a entrada para cima
            visitados.add(new LabirintoObstaculos(mTemp, this.linhaEntrada1 - 1, this.colunaEntrada1, this.linhaEntrada2, this.colunaEntrada2, this.linhaSaida, this.colunaSaida, "Movendo para cima"));
        }
    }

    private void paraBaixo(List<Estado> visitados) {
        if (this.linhaEntrada1 < this.matriz.length - 1 && this.matriz[this.linhaEntrada1 + 1][this.colunaEntrada1] != '@') {
            char[][] mTemp = clonar(this.matriz);
            mTemp[this.linhaEntrada1][this.colunaEntrada1] = 'O';
            mTemp[this.linhaEntrada1 + 1][this.colunaEntrada1] = 'E'; //Move a entrada para baixo
            visitados.add(new LabirintoObstaculos(mTemp, this.linhaEntrada1 + 1, this.colunaEntrada1, this.linhaEntrada2, this.colunaEntrada2, this.linhaSaida, this.colunaSaida, "Movendo para baixo"));
        }
    }

    private void paraEsquerda(List<Estado> visitados) {
        if (this.colunaEntrada1 > 0 && this.matriz[this.linhaEntrada1][this.colunaEntrada1 - 1] != '@') {
            char[][] mTemp = clonar(this.matriz);
            mTemp[this.linhaEntrada1][this.colunaEntrada1] = 'O';
            mTemp[this.linhaEntrada1][this.colunaEntrada1 - 1] = 'E'; //Move para esquerda
            visitados.add(new LabirintoObstaculos(mTemp, this.linhaEntrada1, this.colunaEntrada1 - 1, this.linhaEntrada2, this.colunaEntrada2, this.linhaSaida, this.colunaSaida, "Movendo para esquerda"));
        }
    }

    private void paraDireita(List<Estado> visitados) {
        if (this.colunaEntrada1 < this.matriz.length - 1 && this.matriz[this.linhaEntrada1][this.colunaEntrada1 + 1] != '@') {
            char[][] mTemp = clonar(this.matriz);
            mTemp[this.linhaEntrada1][this.colunaEntrada1] = 'O';
            mTemp[this.linhaEntrada1][this.colunaEntrada1 + 1] = 'E'; //Move para direita
            visitados.add(new LabirintoObstaculos(mTemp, this.linhaEntrada1, this.colunaEntrada1 + 1, this.linhaEntrada2, this.colunaEntrada2, this.linhaSaida, this.colunaSaida, "Movendo para direita"));
        }
    }

     /**
     * Verifica se dois estados são iguais (usado para poda na busca).
     *Retorna true se os estados forem idênticos, false caso contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof LabirintoObstaculos) {
            LabirintoObstaculos e = (LabirintoObstaculos) o;
            for (int i = 0; i < e.matriz.length; i++) {
                for (int j = 0; j < e.matriz.length; j++) {
                    if (e.matriz[i][j] != this.matriz[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Retorna o hashCode desse estado (usado para conjuntos de estados explorados na busca)
     * Poda
     */
    @Override
    public int hashCode() {
        String estado = "";
        for (int i = 0; i < this.matriz.length; i++) {
            for (int j = 0; j < this.matriz.length; j++) {
                estado = estado + this.matriz[i][j];
            }
        }
        return estado.hashCode();
    }

    @Override
    public String toString() {
        StringBuffer resultado = new StringBuffer();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                resultado.append(this.matriz[i][j]);
                resultado.append("\t");
            }
            resultado.append("\n");
        }
        resultado.append("Posição Entrada 1: " + this.linhaEntrada1 + "," + this.colunaEntrada1 + "\n");
        resultado.append("Posição Entrada 2: " + this.linhaEntrada2 + "," + this.colunaEntrada2 + "\n");
        resultado.append("Posição Saída: " + this.linhaSaida + "," + this.colunaSaida + "\n");
        return "\n" + op + "\n" + resultado + "\n\n";
    }

    public static void main(String[] a) {
        LabirintoObstaculos estadoInicial = null;
        int dimensao;
        int porcentagemObstaculos;
        int qualMetodo;
        Nodo n;
        try {
             // Solicita ao usuário a dimensão do labirinto
            dimensao = Integer.parseInt(JOptionPane.showInputDialog(null,"Entre com a dimensão do Puzzle!"));
            // Solicita ao usuário a porcentagem de obstáculos a serem adicionados ao labirinto
            porcentagemObstaculos = Integer.parseInt(JOptionPane.showInputDialog(null,"Porcentagem de obstáculos!"));
            // Pergunta ao usuário qual método de busca será utilizado
            qualMetodo = Integer.parseInt(JOptionPane.showInputDialog(null,"1 - Profundidade\n2 - Largura"));
             // Cria o estado inicial do labirinto com as configurações definidas pelo usuário
            estadoInicial = new LabirintoObstaculos(dimensao, "estado inicial", porcentagemObstaculos);
            
            // Método de busca escolhido
            switch (qualMetodo) {
                case 1:
                    System.out.println("busca em PROFUNDIDADE");
                    n = new BuscaProfundidade(new MostraStatusConsole()).busca(estadoInicial);
                    break;
                case 2:
                    System.out.println("busca em LARGURA");
                    n = new BuscaLargura(new MostraStatusConsole()).busca(estadoInicial);
                    break;
                default:
                    n = null;
                    JOptionPane.showMessageDialog(null, "Método não implementado");
            }
            // Verifica se a busca encontrou uma solução
            if (n == null) {
                System.out.println("sem solucao!");
                System.out.println(estadoInicial);
            } else {
                System.out.println("solucao:\n" + n.montaCaminho() + "\n\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        System.exit(0);
    }
}