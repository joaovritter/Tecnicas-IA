import random
import time

class AG:
    @staticmethod
    def gerar_rota_aleatoria():
        cidades = list("123456789")
        random.shuffle(cidades)
        return ''.join(cidades)

    @staticmethod
    def exibir(populacao):
        for individuo in populacao:
            print(individuo)
            time.sleep(0.05)

    @staticmethod
    def selecionar_por_torneio(populacao, tamanho_populacao):
        nova_populacao = []
        nova_populacao.append(populacao[0])  # Elitismo
        while len(nova_populacao) < tamanho_populacao:
            torneio = random.sample(populacao, 3)
            vencedor = min(torneio)
            nova_populacao.append(vencedor)
        return nova_populacao

    @staticmethod
    def crossover_ox(pai, mae):
        corte1, corte2 = sorted(random.sample(range(9), 2))
        filho1 = [''] * 9
        filho1[corte1:corte2] = pai.rota[corte1:corte2]
        pos_mae = 0
        for i in range(9):
            if filho1[i] == '':
                while mae.rota[pos_mae] in filho1:
                    pos_mae += 1
                filho1[i] = mae.rota[pos_mae]
        return ''.join(filho1)

    @staticmethod
    def mutar(rota):
        idx1, idx2 = random.sample(range(9), 2)  # Passo 1: Seleciona 2 índices aleatórios
        rota = list(rota)                        # Passo 2: Converte a string em lista
        rota[idx1], rota[idx2] = rota[idx2], rota[idx1]  # Passo 3: Troca as cidades de posição
        return ''.join(rota)                     # Passo 4: Reconverte para string e retorna