import random
import time

from cromossomo import Cromossomo
from util import Util

class AG:
    @staticmethod
    def gerar_populacao(populacao, tamanho_populacao, estado_final):
        #cria uma populacao inicial
        for i in range(tamanho_populacao):
            populacao.append(Cromossomo(Util.gerar_palavra(len(estado_final)), estado_final))# Cada cromossomo recebe uma palavra e o estado final (para calcular aptidao)
    
    @staticmethod
    def exibir(populacao):   
        #mostra os individuos da populacao atual     
        for i in populacao:
            print(i)
            # time.sleep(0.05)

    def selecionar_por_torneio(populacao, nova_populacao, taxa_selecao):
        #seleciona os individuos mais aptos para reproducao usando torneio
        # OBS.: a populacao nao pode ser pequena e nem a taxa de selecao ser muito alta
        torneio = []

        # calcular quantos devem ser selecionados a partir do tamanho da populacao com a taxa_selecao
        # populacao.size()	->	100
        # qtd_selecionados	-> 	taxa_selecao
        qtd_selecionados = taxa_selecao * len(populacao) / 100
        cromossomo = populacao[0] #garante eletismo: o melhor individuo vai direto para nova populacao    

        nova_populacao.append( cromossomo ) #elistismo
        
        
        i = 1
        while (i <= qtd_selecionados):
            #seleciona 3 individuos aleatorios diferentes para disputar um torneio
            c1 = populacao[ random.randrange( len(populacao) ) ]
            
            while (True):            
                c2 = populacao[ random.randrange( len(populacao) ) ]
                if c2 != c1:
                    break
            
            while (True):            
                c3 = populacao[ random.randrange( len(populacao) ) ]
                if c3 != c2 != c1:
                    break            
            #adiciona os 3 ao torneio e ordena: o mais apto fica na frente
            torneio.append(c1)
            torneio.append(c2)
            torneio.append(c3)
            torneio.sort() #menor aptidao (maix prox da solucao) vem primeiro

            selecionado = torneio[0]

            if selecionado not in nova_populacao:
                nova_populacao.append(selecionado)
                i += 1
            
            torneio.clear() #FALTOU LIMPAR A LISTA torneio para a próxima rodada      
                

    @staticmethod
    def reproduzir(populacao, nova_populacao, taxa_reproducao, estado_final):
        sPai = sMae = sFilho1 = sFilho2 = ''
        #gera novos filhos a partir de pais selecionados     
        #calcular quantos devem ser reproduzidos a partir do tamanho da populacao com a taxa_reproducao
        #populacao.size()	->	100
        #qtdReproduzido	-> 	taxa_reproducao
        qtd_reproduzidos = taxa_reproducao * len(populacao) / 100

        #sFilho1 = Alexone - primeiraMetadeDoPai + segundaMetadeDaMae
        #sFilho2 = Simandre - primeiraMetadeDaMae + segundaMetadeDoPai
        i = 0
        while (i < qtd_reproduzidos):     
            #seleciona dois pais aleatorios(diferentes)       
            pai = populacao[ random.randrange( len(populacao) ) ]
                
            while (True):            
                mae = populacao[ random.randrange( len(populacao) ) ]
                if mae != pai:
                    break               
            #realiza o crossover: divide os pais no meio e cruza as partes
            sPai = pai.valor
            sMae = mae.valor
            #filho1: primeira metade do pai + segunda metade da mae
            sFilho1 = sPai[0 : int(len(sPai)/2)] + sMae[int(len(sMae) / 2) : len(sMae)]
            
            #filho2: primeira metade da mae + segunda metade do pai
            sFilho2 = sMae[0 : int(len(sMae)/2)] + sPai[int(len(sPai) / 2) : len(sPai)]

            nova_populacao.append(Cromossomo(sFilho1, estado_final)) #estado_final é passado para calcular aptidao do filho
            nova_populacao.append(Cromossomo(sFilho2, estado_final)) #estado_final é passado para calcular aptidao do filho
            i = i + 2
                 
        #podar a nova_populacao, retirando os excedentes do final
        while(len(nova_populacao) > len(populacao)):
            nova_populacao.pop()
      
    

    @staticmethod
    def mutar(populacao, estado_final):
        qtd_mutantes = random.randrange( int(len(populacao) / 5) ) #a qtd de mutantes será no máximo 20% da população        
        
        while (qtd_mutantes > 0):
            posicao_mutante = random.randrange( int(len(populacao)) )
            mutante = populacao[ posicao_mutante ]
            print("vai mutar " , mutante)
            
            #mudando
            valor_mutado = mutante.valor

            caracter_mutante = mutante.valor[random.randrange(len(mutante.valor))]
            caracter_sorteado = Util.letras[random.randrange(Util.tamanho)]
            valor_mutado = valor_mutado.replace(caracter_mutante, caracter_sorteado)          
            mutante = Cromossomo(valor_mutado, estado_final)
            
            populacao[posicao_mutante] = mutante
            qtd_mutantes -= 1
        
    