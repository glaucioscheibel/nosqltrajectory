#-*- coding: utf-8 -*-
#import psycopg2
from datetime import datetime
from trajetorias_dados import execute
print(execute)

def conecta_banco():
    try:
        print ("Conectando ao banco de dados...")
        conexao = psycopg2.connect("host=localhost user=postgres password=root dbname=TrDw")
        global cursor
        cursor = conexao.cursor()
    except:
        print ("Erro inesperado.")
        raise


def define_percentual_de_erro():
    print("Conectado! Aguarde...")
    global erro_forma, erro_comprimento, erro_tempo
    erro_tempo = float(1)
    erro_comprimento = float(1)
    erro_forma = float(7)
    print ("Porcentagem, Ok (%.2f,%.2f,%.2f)" %(erro_tempo, erro_comprimento, erro_forma))

def pega_dados_bd():
    cursor.execute("select obj_id, lat, lon, date, time from linhas_contidas order by obj_id, date, time")
    global execute, ids_trajetorias, latitudes, longitudes, tempos
    execute = cursor.fetchall()


def separa_registros(execute):
    global ids_trajetorias, latitudes, longitudes, tempos
    ids_trajetorias = separa_reg(execute,0)
    latitudes = separa_reg(execute,1)
    longitudes = separa_reg(execute,2)
    tempos = separa_reg(execute,3)

def separa_reg(execute,item):
    """ separa_reg(0)--> Vai pegar os ids das trajetorias
        separa_reg(1)--> Vai pegar a latitude
        separa_reg(2)--> Vai pegar a longitude
        separa_reg(3)--> Vai pegar o ano, mes, dia e horario """
    lista = []
    for i in range(len(execute)):
        if item == 0:
            lista.append(execute[i][0])
            lista = list(sorted(set(lista)))
        elif item == 3:
            dh = (str(execute[i][3])+(" "))+str(execute[i][4])
            lista.append((execute[i][0],dh))
        else:
            lista.append((execute[i][0],execute[i][item]))
    return lista


def calcula_comprimentos():
    lista_comprimentos = []
    soma_comprimentos = 0
    for i in range(len(latitudes[:-1])):
        sql_st = ("select ST_Distance(ST_GeomFromText('POINT(%s %s)'),ST_GeomFromText('POINT(%s %s)'));"%(latitudes[i][1],longitudes[i][1],latitudes[i+1][1],longitudes[i+1][1]))
        cursor.execute(sql_st)
        st = cursor.fetchall()
        if latitudes[i][0] == latitudes[i+1][0] and longitudes[i][0] == longitudes[i+1][0]:
            soma_comprimentos += float(st[0][0])
        else:
            x = (latitudes[i][0],soma_comprimentos)
            lista_comprimentos.append(x)
            soma_comprimentos = 0
    x = (latitudes[i][0],soma_comprimentos)
    lista_comprimentos.append(x)
    grava_txt(lista_comprimentos)
    calcula_similaridade(lista_comprimentos)

def grava_txt(tex):
    result = open("resultado.txt","w")
    result.write("%s" %tex)
    result.close()

def calcula_similaridade_tempo():
    ls_segundos = []
    for i in range(len(ids_trajetorias)):
        ls_segundos.append((ids_trajetorias[i], separa_por_id(i,1)))
    calcula_similaridade(ls_segundos)


def calcula_similaridade(lista):
    """
    Faz o calculo da similaridade de tempo
    Na variavel g calcula sequencialmente a primeira traj e o restante
    até todas serem calculadas entre si.
    """
    ls_simi = []
    for g in range(len(lista)):
        """Cada vez que g aumenta, tira as trajs que foram calculadas,
         onde nao há calculo duplicado com as mesmas trajs"""
        ls = lista[g:]
        for i in range(len(ls)):
            if lista[g][0] != ls[i][0]:
                e = calculo_prc(lista[g][1],ls[i][1])
                if e >= (100 - erro_tempo):
                    k = ("Porcentagem: %2.3f Trajs: %d, %d"%(e,lista[g][0], ls[i][0]))
                    ls_simi.append(k)
                    print(k)


def calculo_prc(r1,r2):
    '''Caso tenha alguma traj com apenas um registro, este if cuida para nao ter divisao por 0'''
    if r1 == 0 and r2 == 0:
        lis = 100
    else:
        lis = [r1,r2]
        lis = sorted(lis)
        lis = ((lis[0]/lis[1])*100)
    return lis


def separa_por_id(v,p):
    """
    Essa funçcao pega todos os instantes de tempo de uma pré-determinada obj_id
    coloca em ordem do mais antiga ao mais recente e retorna a diferenca em segundos
    da primeira e da ultima trajetoria atraves da pega_dif().
    """
    lt = []
    for i in range(len(tempos)):
        if tempos[i][0] == ids_trajetorias[v]:
            lt.append(tempos[i][p])
        i += 1

    lt = sorted(lt)
    if p == 1:
        return pega_dif(str(lt[0]), str(lt[-1]))
    return lt


def pega_dif(d1,d2):
    """
    Essa funçao transforma o dado de str para datetime e retorna a diferenca
    entre dois intervalos.
    """
    fmt = ('%Y-%m-%d %H:%M:%S')
    dt1 = datetime.strptime(d1, fmt)
    dt2 = datetime.strptime(d2, fmt)
    datas = dt2 - dt1
    diferenca = datas.total_seconds()
    return diferenca


def carrega_dados_do_banco():
    conecta_banco()
    pega_dados_bd()


def carrega_dados_arquivo_texto():
#    global execute, ids_trajetorias, latitudes, longitudes, tempos
    #execute = open("trajetorias.txt").read()

    return execute

def main():
#    carrega_dados_do_banco()
    execute = carrega_dados_arquivo_texto()
    define_percentual_de_erro()
    print(execute)
    separa_registros(execute)
    print ('\nSimilaridade por tempo:\n')
    calcula_similaridade_tempo()
    print ('\nSimilaridade por comprimento:\n')
#    calcula_comprimentos()

if __name__ == '__main__':
    main()
