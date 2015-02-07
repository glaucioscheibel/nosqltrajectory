#/usr/bin/env python3
#coding:utf-8

import datetime
import itertools
from math import *
from trajetorias_dados import *

#TODO:
# Fazer rotina determina_tempo_decorrido() receber uma tupla
# Continuar os testes da rotina determina_tempo_decorrido_trajetorias()
# - mais trajetórias e mais tempos
# Criar rotina que encontra trajetorias com tempos iguais
# Plus: incluir parametro de aproximação
# Escrever o artigo
# Retirar comentários das funções?


def determina_tempo_decorrido(tempos):
    """Recebe uma lista de momentos de tempo e devolve o tempo decorrido entre eles."""
    if len(tempos) == 1:
        return 0
    tempos.sort()
    diferenca_tempo = abs(tempos[0] - tempos[-1])
    diferenca_tempo_em_segundos = diferenca_tempo.total_seconds()
    return diferenca_tempo_em_segundos

def agrupa_pontos_por_id_de_trajetoria(pontos):
    """Agrupa os pontos das trajetórias"""
    trajetorias = {}
    for ponto in pontos:
        chave, latitude, longitude, data, hora = ponto
        tempo = datetime.datetime.combine(data, hora)
        campos = latitude, longitude, tempo
        if chave not in trajetorias:
            trajetorias[chave] = [campos]
        else:
            trajetorias[chave].append(campos)
    return trajetorias

def determina_tempo_decorrido_trajetorias(trajetorias):
    """
    Gera um dicionário com as trajetórias e seu tempo decorrido.
    """
    tempos_trajetorias = {}
    for trajetoria in trajetorias:
        tempos = [ponto[2] for ponto in trajetorias[trajetoria]]
        tempos_trajetorias[trajetoria] = determina_tempo_decorrido(tempos)
    return tempos_trajetorias

def determina_percentual_similaridade(a,b):
    if a > b:
        a, b = b, a
    return int((a / b) * 100)

def combina_trajetorias(trajetorias):
    return dict((x,[((x,trajetorias[x]),(y,trajetorias[y])) for y in trajetorias if y != x]) for x in trajetorias)

def determina_pares_percentual(combinacoes):
    lista_dos_pares_e_percentual = []
    for combinacao in combinacoes.values():
        for i in combinacao:
            lista_dos_pares_e_percentual.append(((i[0][0],i[1][0]),determina_percentual_similaridade(i[0][1],i[1][1])))
    return lista_dos_pares_e_percentual

def determina_trajetorias_similares(pares_com_percentual, percentual_similaridade_desejado):
    return [par for par in pares_com_percentual if par[1] >= percentual_similaridade_desejado]

def separa_latitude_longitude(trajetorias):
    coordenadas_trajetoria = {}
    for trajetoria in trajetorias:
        coordenada = [ponto[0:2] for ponto in trajetorias[trajetoria]]
        coordenadas_trajetoria[trajetoria] = coordenada
    return coordenadas_trajetoria

def calcula_comprimento(coordenadas):
    '''Formula de Haversine para medicao de distancias terrestres'''
    lat1,lon1,lat2,lon2 = map(radians, [float(coordenadas[0]),float(coordenadas[1]),float(coordenadas[2]),float(coordenadas[3])])
    diferenca_lon, diferenca_lat = lon2 - lon1, lat2 - lat1
    a = sin(diferenca_lat/2)**2 + cos(lat1) * cos(lat2) * sin(diferenca_lon/2)**2
    c = 2 * atan2(sqrt(a), sqrt(1-a))
    comprimento_km = 6371 * c
    return comprimento_km

def combina_pontos_de_dois_em_dois(pontos):
    combinacao = []
    x = 0
    try:
        for ponto in pontos:
            combinacao.append((pontos[x][0],pontos[x][1],pontos[x+1][0],pontos[x+1][1]))
            x += 1
    except: return combinacao

def separa_e_combina_pontos_por_trajetoria(coordenadas_trajetoria):
    pontos_trajetorias = {}
    for chave in coordenadas_trajetoria.keys():
        if len(coordenadas_trajetoria.get(chave)) > 1:
            pontos_trajetorias.update({chave:combina_pontos_de_dois_em_dois(coordenadas_trajetoria.get(chave))})
        else: pontos_trajetorias.update({chave:[0]})
    return pontos_trajetorias

def determina_comprimento_total(combinacao):
    comprimento_trajetorias = {}
    for chave in combinacao.keys():
        comprimento = 0
        for pontos in combinacao[chave]: comprimento += calcula_comprimento((pontos[0], pontos[1], pontos[2], pontos[3]))
        comprimento_trajetorias.update({chave:comprimento})
    return comprimento_trajetorias

def calcula_cateto_oposto(pontos):
    #Diferenca da latitude (BC) #Retorna a diferenca da latitude em metros
    tamanho_cateto_oposto = {}
    for chave in pontos.keys():
        diferenca_lat = []
        for ponto in pontos[chave]: diferenca_lat.append(111319 * (float(ponto[2])-float(ponto[0])))
        tamanho_cateto_oposto.update({chave:diferenca_lat})
    return tamanho_cateto_oposto

def calcula_hipotenusa(pontos):
    tamanho_hipotenusa = {}
    for chave in pontos.keys():
        distancia = []
        for ponto in pontos[chave]: distancia.append(calcula_comprimento((float(ponto[0]),float(ponto[1]), float(ponto[2]), float(ponto[3]))))
        tamanho_hipotenusa.update({chave:distancia})
    return tamanho_hipotenusa

def define_angulacao(hipotenusa, cateto_oposto):
    return asin(sin(cateto_oposto/hipotenusa))
    #Para transformar em graus degrees()
    
def elimina_duplicidades(trajetorias_similares):
    """Recebe lista de trajetórias encontradas e elimina as duplicidades existentes. Ex. de duplicidade: [(889,914),(914),(889)]"""
    trajetorias_similares_validas = []
    for tupla in trajetorias_similares:
        if tupla[0] not in trajetorias_similares_validas and tupla[0][::-1] not in trajetorias_similares_validas:
            trajetorias_similares_validas.append(tupla)
    return trajetorias_similares_validas

def relaciona_resultados_de_similaridade(trajetorias_tempo,trajetorias_comprimento):
    """Relaciona resultados engajados em mais de um parâmetro de pesquisa."""
    trajetorias_por_tempo_validas = elimina_duplicidades(trajetorias_tempo)
    trajetorias_por_comprimento_validas = elimina_duplicidades(trajetorias_comprimento)
    
    par_de_trajetorias_verificados = []
    trajetorias_similares_relacionadas = []
    for tupla in trajetorias_por_tempo_validas:
        for par in trajetorias_por_comprimento_validas:
            #Se o par da lista A for igual ao par da lista B -OU- o inverso do par da lista A for igual ao par da lista B
            # -E- se o par não estiver na lista de pares já verificados -OU- seu inverso não estiver na lista de pares já verificados
            if (tupla[0] == par[0] or tupla[0][::-1] == par[0]) and (tupla[0] not in par_de_trajetorias_verificados and tupla[0][::-1] not in par_de_trajetorias_verificados):
                trajetorias_similares_relacionadas.append(((tupla[0][0],tupla[0][1]),tupla[1],par[1]))
                par_de_trajetorias_verificados.append(tupla[0])
    return elimina_duplicidades(trajetorias_similares_relacionadas)

def trajetorias_similares_tempo(percentual_similaridade):
    #Verificacao de tempo

    pontos_agrupados_por_trajetoria = agrupa_pontos_por_id_de_trajetoria(pontos)
    tempos_trajetorias = determina_tempo_decorrido_trajetorias(pontos_agrupados_por_trajetoria)
    trajetorias_combinadas = combina_trajetorias(tempos_trajetorias)
    pares_com_percentual = determina_pares_percentual(trajetorias_combinadas)
    trajetorias_similares_tempo = determina_trajetorias_similares(pares_com_percentual,percentual_similaridade)
    return trajetorias_similares_tempo
    
def trajetorias_similares_comprimento(percentual_similaridade):
    #Verificacao de comprimento

    pontos_agrupados_por_trajetoria = agrupa_pontos_por_id_de_trajetoria(pontos)
    coordenadas_trajetorias = separa_latitude_longitude(pontos_agrupados_por_trajetoria)
    pontos_combinados = separa_e_combina_pontos_por_trajetoria(coordenadas_trajetorias)
    comprimento_total_das_trajetorias = determina_comprimento_total(pontos_combinados)
    comprimentos_e_trajetorias_combinadas = combina_trajetorias(comprimento_total_das_trajetorias)
    percentual_similaridade_comprimento = determina_pares_percentual(comprimentos_e_trajetorias_combinadas)
    trajetorias_similares_comprimento = determina_trajetorias_similares(percentual_similaridade_comprimento,percentual_similaridade)
    return trajetorias_similares_comprimento



def main():
    
    
    trajetorias_tempo = trajetorias_similares_tempo(0)
    trajetorias_comprimento = trajetorias_similares_comprimento(0)
    print ((relaciona_resultados_de_similaridade(trajetorias_tempo,trajetorias_comprimento)))
    print (len(relaciona_resultados_de_similaridade(trajetorias_tempo,trajetorias_comprimento)))
    
    ##Verificacao de forma
    #pontos_combinados = separa_e_combina_pontos_por_trajetoria(coordenadas_trajetorias)
    #dic_catetos_opostos = calcula_cateto_oposto(pontos_combinados)
    #dic_hipotenusas = calcula_hipotenusa(pontos_combinados)

if __name__ == '__main__':
    main()
