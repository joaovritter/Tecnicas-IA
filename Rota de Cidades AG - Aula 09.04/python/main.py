from cromossomo import Cromossomo
from ag import AG
import copy
import random 

def main():
    print("=== Algoritmo Genético para Rota de Cidades ===")
    tamanho_populacao = int(input("Tamanho da população: "))
    taxa_mutacao = int(input("Taxa de mutação (%): "))
    max_geracoes = int(input("Máximo de gerações: "))
    
    # População inicial
    populacao = [Cromossomo(AG.gerar_rota_aleatoria()) for _ in range(tamanho_populacao)]
    populacao.sort()
    AG.exibir(populacao) 
    
    for geracao in range(2, max_geracoes + 1):
        nova_populacao = AG.selecionar_por_torneio(populacao, tamanho_populacao)
        filhos = []
        
        # Crossover
        for _ in range(tamanho_populacao // 2):
            pai, mae = random.sample(nova_populacao, 2)
            filho1 = AG.crossover_ox(pai, mae)
            filho2 = AG.crossover_ox(mae, pai)
            filhos.extend([Cromossomo(filho1), Cromossomo(filho2)])
        
        # Mutação
        for i in range(len(filhos)):
            if random.random() < taxa_mutacao / 100:
                filhos[i] = Cromossomo(AG.mutar(filhos[i].rota))
        
        populacao = copy.deepcopy(filhos)
        populacao.sort()
        print(f"\n=== Geração {geracao} ===")  #título da geração
        AG.exibir(populacao)  # Chamada direta da função exibir da classe AG
        
        if populacao[0].penalidade == 0:
            print("\n✅ Solução perfeita encontrada!")
            break
    
    print("\n=== RESULTADO FINAL ===")
    print(f"Melhor rota: {populacao[0]}")
    print(f"Gerações necessárias: {geracao}")

if __name__ == "__main__":
    main()