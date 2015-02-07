#coding:utf-8

from trajetorias import trajetorias_similares_tempo, trajetorias_similares_comprimento, relaciona_resultados_de_similaridade
from trajetorias_dados import pontos

def realiza_pesquisa(tempo, comprimento):
        trajetorias_tempo = trajetorias_similares_tempo(tempo)
        trajetorias_comprimento = trajetorias_similares_comprimento(comprimento)
        resultado_pesquisa = (relaciona_resultados_de_similaridade(trajetorias_tempo,trajetorias_comprimento))
        
        return resultado_pesquisa
        
def monta_dicionario_resultado(resultado):
    resultado_final = []
    
    for i in range(len(resultado)):
        resultado_dividido = {}
        resultado_dividido['id_trajetoria1'] = '%s' %(resultado[i][0][0])
        resultado_dividido['id_trajetoria2'] = '%s' %(resultado[i][0][1])
        resultado_dividido['tempo'] = '%s' %(resultado[i][1])
        resultado_dividido['comprimento'] = '%s' %(resultado[i][2])
        resultado_final.append(resultado_dividido)

    return resultado_final

def busca_pontos(trajetoria):
    pontos_encontrados = []
    
    for ponto in pontos:
        if ponto[0] == int(trajetoria):
            pontos_encontrados.append((ponto[1],ponto[2]))
            
    return pontos_encontrados

def monta_lista_trajetoria(lista):
    resultado_final = []
    
    for i in range(len(lista)):

        lista_temp = lista[i].split('-')

        trajetoria1 = lista_temp[0]
        pontos_trajetoria1 = busca_pontos(trajetoria1)
        
        trajetoria2 = lista_temp[1]
        pontos_trajetoria2 = busca_pontos(trajetoria2)
        
        lista_pontos = {}
        lista_pontos['id_trajetoria'] = trajetoria1
        lista_pontos['pontos'] = pontos_trajetoria1
        resultado_final.append(lista_pontos)
        
        lista_pontos = {}        
        lista_pontos['id_trajetoria'] = trajetoria2
        lista_pontos['pontos'] = pontos_trajetoria2
        resultado_final.append(lista_pontos)
    
    return resultado_final
