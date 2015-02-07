#/usr/bin/env python3
#coding: utf-8


from trajetorias.trajetorias import *
import matplotlib.pyplot as plt

def escolha_pesquisa():
    #Verifica Qual pesquisa deseja realizar e o pescentual de Similaridade desejado
    while True:
        tipo_pesquisa = input ('O que deseja pesquisar "tempo" ou "comprimento"? ')
        if tipo_pesquisa == 'tempo':
            tipo_pesquisa = 'trajetorias_similares_tempo'
            break
        elif tipo_pesquisa == 'comprimento':
            tipo_pesquisa = 'trajetorias_similares_comprimento'
            break
        else:
            print ('Desculpe! Não identifiquei sua resposta, Tente novamente.')
        print ('------------------------------------------------------------\n')
        
    # Verifica o percentual de Similaridade desejado
    while True:
        percentual_similaridade = int(input('Informe qual o "Grau de Similaridade da Pesquisa" Entre(0,100): '))
        if percentual_similaridade > 100:
            print ('O Nº %d, está fora do range! Tente novamente.' %(percentual_similaridade))
            print ('------------------------------------------------------------\n')
        else:
            break
    
    return (tipo_pesquisa, percentual_similaridade)   

def precessa_pesquisa(tipo_pesquisa,percentual_similaridade):
    #Executa a pesquisa conforme a escolha e retorna uma lista com resultados
    resultado = []
    if tipo_pesquisa == 'trajetorias_similares_tempo':
        resultado = trajetorias_similares_tempo(percentual_similaridade)
    elif tipo_pesquisa == 'trajetorias_similares_comprimento':
        resultado = trajetorias_similares_comprimento(percentual_similaridade)

    return resultado
    
def separa_resultado(resultado, percentual_similaridade):
    #Pega a lista de resultado da def precessa_pesquisa(), e monta um novo resultado somente com o percentual_similaridade EXATO da pesquisa
    novo_resultado =[]
    for ponto in resultado:
        if ponto[1] == percentual_similaridade:
            novo_resultado.append(ponto)
    resultado = novo_resultado
    return resultado

def escolha_trajetorias(novo_resultado):
    #Mostra Quantidade de Trajetorias Similares e retorna qual vai ser impressa na tela
    quantidade_resultado = len(novo_resultado)
    escolha = []
    
    print ('------------------------------------------------------------')
    
    escolha_trajetoria = int(input('Existem %s Trajetorias Similares. \nInforme qual deseja Imprimir na tela(1 ao %s):' %(quantidade_resultado, quantidade_resultado)))
    escolha.append(novo_resultado[escolha_trajetoria-1])
    
    return escolha

def busca_relacao_lat_e_long(trajetoria1, trajetoria2):
    #Pega informações iniciais
    traj1, traj2 = trajetoria1, trajetoria2
    trajetoria1 = []
    trajetoria2 = []
    
    #Capturando Informações dos Pontos informados
    for ponto in pontos:
        if ponto[0] == traj1:
            trajetoria1.append(ponto)
            
        elif ponto[0] == traj2:
            trajetoria2.append(ponto)
            
    return (trajetoria1, trajetoria2)





def separa_lat_e_long_trajetoria(trajetoria):

    #Criando lista de lat e long da trajetoria selecionado
    ponto_lat=[]
    ponto_long=[]

    for i,ponto in enumerate(trajetoria):
        ponto, latitude, longitude = ponto[0], ponto[1], ponto[2]
        ponto_lat.append(latitude)
        ponto_long.append(longitude)
        
    return (ponto_lat, ponto_long)

def desenha_tela(traj1_lat, traj1_long, traj2_lat, traj2_long):
        
    #Desenho dos Pontos
    plt.title('Trajetorias Similares', fontstyle='normal', fontsize = 15, family='times')
    plt.xlabel('Longitude', fontsize=15, fontstyle='italic',fontweight='light',family='times') 
    plt.ylabel('Latitude',fontsize=15, fontstyle='italic',fontweight='light',family='times')
    plt.plot(traj1_lat, traj1_long, marker='o', linewidth=1, color='green', markersize=3, label='Trajetoria 1')
    plt.plot(traj2_lat, traj2_long, marker='o', linewidth=1, color='red', markersize=3, label='Trajetoria 2')
   
    plt.legend()
    plt.show()
    
def main():
    
    trajetorias_tempo = trajetorias_similares_tempo(70)
    trajetorias_comprimento = trajetorias_similares_comprimento(70)
    novo_resultado = (relaciona_resultados_de_similaridade(trajetorias_tempo,trajetorias_comprimento))

    #tipo_pesquisa, percentual_similaridade = escolha_pesquisa()
    #resultado = precessa_pesquisa(tipo_pesquisa,percentual_similaridade)
    #novo_resultado = separa_resultado(resultado, percentual_similaridade)

    resultado_trajetorias = (escolha_trajetorias(novo_resultado))
    trajetoria1, trajetoria2 = resultado_trajetorias[0][0][0], resultado_trajetorias[0][0][1]
    trajetoria1, trajetoria2 = busca_relacao_lat_e_long(trajetoria1, trajetoria2)
    traj1_lat, traj1_long = (separa_lat_e_long_trajetoria(trajetoria1))
    traj2_lat, traj2_long = (separa_lat_e_long_trajetoria(trajetoria2))
    desenha_tela(traj1_lat, traj1_long, traj2_lat, traj2_long)
    
    

if __name__ == '__main__':
    main()
