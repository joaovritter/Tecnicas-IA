
def ehValido(estado):
    homem,lobo,carneiro,alface = estado
    if (homem!=lobo and lobo==carneiro) or (homem!=carneiro and carneiro==alface):
        return False
    return True

def levarNada(homem,lobo,carneiro,alface):
    if homem == 'e':
        novo_h = 'd' # Se está na esquerda, vai para a direita
    else:
        novo_h = 'e' # Se está na direita, volta para a esquerda
    
    novo_estado = (novo_h, lobo, carneiro, alface)
    if ehValido(novo_estado):
        return novo_estado
    else: 
        None

def ehMeta(estado):
    return estado == ('d','d','d','d')

