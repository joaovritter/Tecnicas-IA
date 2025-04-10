class Cromossomo:
    def __init__(self, rota):
        self.rota = rota
        self.penalidade = self.calcular_penalidade()
    
    def calcular_penalidade(self):
        penalidade = 0
        
        # Restrição 1: Cidade maior não pode estar antes da menor (todos os pares)
        for i in range(len(self.rota)):
            for j in range(i+1, len(self.rota)):
                if int(self.rota[i]) > int(self.rota[j]):
                    penalidade += 10
                    
        # Restrição 2: Cidades repetidas
        if len(set(self.rota)) != len(self.rota):
            penalidade += 20 * (len(self.rota) - len(set(self.rota)))
        return penalidade
    
    
    def __lt__(self, other):
        return self.penalidade < other.penalidade
    
    def __str__(self):
        return f"{self.rota} (Penalidade: {self.penalidade})"