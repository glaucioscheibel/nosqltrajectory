import unittest
from trajetorias import *
import datetime as dt

#TODO:
# Criar vários arquivos de testes?
# Reestruturar os testes?


class TempoDecorridoTests(unittest.TestCase):
    def test_tempo_decorrido_recebe_1_tempo_retorna_0(self):
        intervalo = [dt.datetime(2014, 5, 27, 14, 3, 0), ]
        self.assertEqual(determina_tempo_decorrido(intervalo), 0)

    def test_tempo_decorrido_0_segundo(self):
        t1 = dt.datetime(2014, 5, 27, 14, 3, 0)
        t2 = dt.datetime(2014, 5, 27, 14, 3, 0)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 0)

    def test_tempo_decorrido_1_segundo(self):
        t1 = dt.datetime(2014, 5, 27, 14, 3, 0)
        t2 = dt.datetime(2014, 5, 27, 14, 3, 1)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 1)

    def test_tempo_decorrido_1_minuto(self):
        t1 = dt.datetime(2014, 5, 27, 14, 3, 0)
        t2 = dt.datetime(2014, 5, 27, 14, 4, 0)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 60)

    def test_tempo_decorrido_1_hora(self):
        t1 = dt.datetime(2014, 5, 27, 14, 3, 0)
        t2 = dt.datetime(2014, 5, 27, 15, 3, 0)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 3600)

    def test_tempo_decorrido_1_dia(self):
        t1 = dt.datetime(2014, 5, 27, 14, 3, 0)
        t2 = dt.datetime(2014, 5, 28, 14, 3, 0)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 86400)

    def test_tempo_decorrido_1_mes_maio_junho(self):
        t1 = dt.datetime(2014, 5, 27, 14, 3, 0)
        t2 = dt.datetime(2014, 6, 27, 14, 3, 0)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 2678400)

    def test_tempo_decorrido_1_mes_janeiro_fevereiro(self):
        t1 = dt.datetime(2014, 1, 1, 0, 0, 0)
        t2 = dt.datetime(2014, 2, 1, 0, 0, 0)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 2678400)

    def test_tempo_decorrido_1_mes_fevereiro_marco(self):
        t1 = dt.datetime(2014, 2, 1, 0, 0, 0)
        t2 = dt.datetime(2014, 3, 1, 0, 0, 0)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 2419200)

    def test_tempo_decorrido_1_ano(self):
        t1 = dt.datetime(2014, 1, 1, 0, 0, 0)
        t2 = dt.datetime(2015, 1, 1, 0, 0, 0)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 31536000)

    def test_tempo_decorrido_1_segundo_tempos_invertidos(self):
        t1 = dt.datetime(2014, 5, 27, 14, 3, 1)
        t2 = dt.datetime(2014, 5, 27, 14, 3, 0)
        intervalo = [t1, t2]
        self.assertEqual(determina_tempo_decorrido(intervalo), 1)

    def test_tempo_decorrido_2_segundos_3_tempos(self):
        t1 = dt.datetime(2014, 5, 27, 14, 3, 1)
        t2 = dt.datetime(2014, 5, 27, 14, 3, 2)
        t3 = dt.datetime(2014, 5, 27, 14, 3, 0)
        intervalo = [t1, t2, t3]
        self.assertEqual(determina_tempo_decorrido(intervalo), 2)

    def test_tempo_decorrido_com_4_tempos_desordenados(self):
        t1 = dt.datetime(2014, 5, 27, 14, 3, 2)
        t2 = dt.datetime(2014, 5, 27, 14, 3, 1)
        t3 = dt.datetime(2014, 5, 27, 14, 3, 4)
        t4 = dt.datetime(2014, 5, 27, 14, 3, 3)
        intervalo = [t1, t2, t3, t4]
        self.assertEqual(determina_tempo_decorrido(intervalo), 3)


class AgrupaPontosPorIDdeTrajetoria(unittest.TestCase):
    def test_agrupa_pontos_por_id_de_trajetoria_1_ponto(self):
        pontos = [(801, '23.793360', '38.021434', dt.date(2002, 9, 14), dt.time(9, 19, 33))]
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 33)), ]}
        self.assertEqual(agrupa_pontos_por_id_de_trajetoria(pontos), trajetorias)

    def test_agrupa_pontos_por_id_de_trajetoria_2_pontos_mesmo_id(self):
        ponto1 = (801, '23.793360', '38.021434', dt.date(2002, 9, 14), dt.time(9, 19, 33))
        ponto2 = (801, '23.790908', '38.023185', dt.date(2002, 9, 14), dt.time(9, 20, 3))
        pontos = [ponto1, ponto2]
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 33)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 20, 3))]}
        self.assertEqual(agrupa_pontos_por_id_de_trajetoria(pontos), trajetorias)

    def test_agrupa_pontos_por_id_de_trajetoria_3_pontos_mesmo_id(self):
        ponto1 = (801, '23.793360', '38.021434', dt.date(2002, 9, 14), dt.time(9, 19, 33))
        ponto2 = (801, '23.790908', '38.023185', dt.date(2002, 9, 14), dt.time(9, 20, 3))
        ponto3 = (801, '23.790910', '38.025678', dt.date(2002, 9, 14), dt.time(9, 21, 43))
        pontos = [ponto1, ponto2, ponto3]
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 33)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 20, 3)),
                             ('23.790910', '38.025678', dt.datetime(2002, 9, 14, 9, 21, 43))]}
        self.assertEqual(agrupa_pontos_por_id_de_trajetoria(pontos), trajetorias)

    def test_agrupa_pontos_por_id_de_trajetoria_2_pontos_ids_diferentes(self):
        ponto1 = (801, '23.793360', '38.021434', dt.date(2002, 9, 14), dt.time(9, 19, 33))
        ponto2 = (802, '23.790908', '38.023185', dt.date(2002, 9, 14), dt.time(9, 20, 3))
        pontos = [ponto1, ponto2]
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 33))],
                       802: [('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 20, 3))]}
        self.assertEqual(agrupa_pontos_por_id_de_trajetoria(pontos), trajetorias)

    def test_agrupa_pontos_por_id_de_trajetoria_2_pontos_de_2_ids_diferentes(self):
        ponto11 = (801, '23.793360', '38.021434', dt.date(2002, 9, 14), dt.time(9, 19, 33))
        ponto12 = (801, '23.790908', '38.023185', dt.date(2002, 9, 14), dt.time(9, 20, 3))
        ponto21 = (802, '23.793360', '38.021434', dt.date(2002, 9, 14), dt.time(9, 19, 33))
        ponto22 = (802, '23.790908', '38.023185', dt.date(2002, 9, 14), dt.time(9, 20, 3))
        pontos = [ponto11, ponto12, ponto21, ponto22]
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 33)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 20, 3))],
                       802: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 33)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 20, 3))]}
        self.assertEqual(agrupa_pontos_por_id_de_trajetoria(pontos), trajetorias)


class DeterminaTempoDecorridoTrajetorias(unittest.TestCase):
    def test_determina_tempo_decorrido_1_trajetoria_1_ponto(self):
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 33)), ]}
        tempos_trajetorias = {801: 0}
        self.assertEqual(determina_tempo_decorrido_trajetorias(trajetorias), tempos_trajetorias)

    def test_determina_tempo_decorrido_1_trajetoria_2_pontos(self):
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 1)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 19, 2))]}
        tempos_trajetorias = {801: 1}
        self.assertEqual(determina_tempo_decorrido_trajetorias(trajetorias), tempos_trajetorias)

    def test_determina_tempo_decorrido_1_trajetoria_3_pontos(self):
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 1)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 19, 2)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 19, 3))]}
        tempos_trajetorias = {801: 2}
        self.assertEqual(determina_tempo_decorrido_trajetorias(trajetorias), tempos_trajetorias)

    def test_determina_tempo_decorrido_1_trajetoria_3_pontos_desordenados(self):
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 1)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 19, 3)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 19, 2))]}
        tempos_trajetorias = {801: 2}
        self.assertEqual(determina_tempo_decorrido_trajetorias(trajetorias), tempos_trajetorias)

    def test_determina_tempo_decorrido_2_trajetoria_3_pontos(self):
        trajetorias = {801: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 1)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 19, 2)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 19, 3))],
                       802: [('23.793360', '38.021434', dt.datetime(2002, 9, 14, 9, 19, 1)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 19, 15)),
                             ('23.790908', '38.023185', dt.datetime(2002, 9, 14, 9, 19, 10))]}
        tempos_trajetorias = {801: 2, 802: 14}
        self.assertEqual(determina_tempo_decorrido_trajetorias(trajetorias), tempos_trajetorias)


class DeterminaPercentualSimilaridade(unittest.TestCase):
    def test_determina_percentual_similaridade_2_valores_iguais(self):
        self.assertEqual(determina_percentual_similaridade(100, 100), 100)

    def test_determina_percentual_similaridade_2_valores_ordenados(self):
        self.assertEqual(determina_percentual_similaridade(90, 100), 90)

    def test_determina_percentual_similaridade_2_valores_desordenados(self):
        self.assertEqual(determina_percentual_similaridade(100, 90), 90)


class CombinaTrajetorias(unittest.TestCase):
    def test_combina_trajetorias_3_trajetorias(self):
        trajetorias_passadas = {801: 2, 802: 2, 803: 10}
        trajetorias_combinadas = {801: [((801, 2), (802, 2)), ((801, 2), (803, 10))],
                                802: [((802, 2), (801, 2)), ((802, 2), (803, 10))],
                                803: [((803, 10), (801, 2)), ((803, 10), (802, 2))]}

        self.assertEqual(combina_trajetorias(trajetorias_passadas),
                         trajetorias_combinadas)

    def test_combina_trajetorias_4_trajetorias(self):
        trajetorias_passadas = {801: 11, 802: 8, 803: 10, 804: 9}
        trajetorias_combinadas = {801: [((801, 11), (802, 8)), ((801, 11), (803, 10)), ((801, 11), (804, 9))],
                                802: [((802, 8), (801, 11)), ((802, 8), (803, 10)), ((802, 8), (804, 9))],
                                803: [((803, 10), (801, 11)), ((803, 10), (802, 8)), ((803, 10), (804, 9))],
                                804: [((804, 9), (801, 11)), ((804, 9), (802, 8)), ((804, 9), (803, 10))]}

        self.assertEqual(combina_trajetorias(trajetorias_passadas),
                         trajetorias_combinadas)


class DeterminaTrajetoriasSimilares(unittest.TestCase):
    def test_determina_trajetorias_similares_100_porcento(self):
        pares_com_percentual = [((801, 802), 100), ((803, 801), 20), ((803, 802), 20)]
        percentual_similaridade_desejado = 100
        pares_similares = [((801, 802), 100),]
        self.assertEqual(determina_trajetorias_similares(pares_com_percentual, percentual_similaridade_desejado), pares_similares)

    def test_determina_trajetorias_similares_85_porcento(self):
        pares_com_percentual = [((801, 802), 72),
                 ((803, 801), 90),
                 ((803, 802), 80),
                 ((803, 804), 90),
                 ((804, 801), 81),
                 ((804, 802), 88)]
        percentual_similaridade_desejado = 85
        pares_similares = [((803, 801), 90),
                           ((803, 804), 90),
                           ((804, 802), 88)]
        self.assertEqual(determina_trajetorias_similares(pares_com_percentual, percentual_similaridade_desejado), pares_similares)

    def test_determina_trajetorias_similares_1_porcento(self):
        pares = [((801, 802), 100), ((803, 801), 0), ((803, 802), 20)]
        percentual_similaridade_desejado = 1
        pares_similares = [((801, 802), 100), ((803, 802), 20),]
        self.assertEqual(determina_trajetorias_similares(pares, percentual_similaridade_desejado), pares_similares)


class DeterminaParesPercentual(unittest.TestCase):
    def test_determina_pares_percentual_porcento(self):
        combinacoes_passadas = {801: [((801, 10), (802, 8)), ((801, 10), (803, 10)), ((801, 10), (804, 9))],
                                802: [((802, 8), (801, 10)), ((802, 8), (803, 10)), ((802, 8), (804, 9))],
                                803: [((803, 10), (801, 10)), ((803, 10), (802, 8)), ((803, 10), (804, 9))],
                                804: [((804, 9), (801, 10)), ((804, 9), (802, 8)), ((804, 9), (803, 10))]}

        combinacoes_na_ordem = [((801, 802), 80), ((801, 803), 100), ((801, 804), 90),
                                ((802, 801), 80), ((802, 803), 80), ((802, 804), 88),
                                ((803, 801), 100), ((803, 802), 80), ((803, 804), 90),
                                ((804, 801), 90), ((804, 802), 88),((804, 803), 90)]
        self.assertEqual(determina_pares_percentual(combinacoes_passadas),combinacoes_na_ordem)


class SeparaLatitudeLongitude(unittest.TestCase):
    def test_separa_latitude_longitude(self):
        pontos_passados = {888: [('23.838237', '38.128890', datetime.datetime(2002, 9, 11, 6, 29, 36)),
                                ('23.836449', '38.125171', datetime.datetime(2002, 9, 11, 6, 30, 6)),
                                ('23.838865', '38.126201', datetime.datetime(2002, 9, 11, 14, 57, 6)),
                                ('23.837738', '38.127753', datetime.datetime(2002, 9, 11, 15, 55, 36)),
                                ('23.837490', '38.127318', datetime.datetime(2002, 9, 12, 10, 26, 6)),
                                ('23.836937', '38.126121', datetime.datetime(2002, 9, 16, 17, 4, 39))]}

        pontos_com_latitude_longitude = {888: [('23.838237', '38.128890'),
                                                ('23.836449', '38.125171'),
                                                ('23.838865', '38.126201'),
                                                ('23.837738', '38.127753'),
                                                ('23.837490', '38.127318'),
                                                ('23.836937', '38.126121')]}
        self.assertEqual(separa_latitude_longitude(pontos_passados), pontos_com_latitude_longitude)

    def test_separa_latitude_longitude_com_um_ponto(self):
        pontos_passados = {800: [('25.2525', '37.777', datetime.datetime(2002, 9, 11, 6, 29, 36))]}

        pontos_com_latitude_longitude = {800: [('25.2525', '37.777')]}
        self.assertEqual(separa_latitude_longitude(pontos_passados), pontos_com_latitude_longitude)


class CalculaComprimento(unittest.TestCase):
    def test_calcula_comprimento(self):
        coordenadas_passadas = 53.32055555555556 , -1.7297222222222221, 53.31861111111111, -1.6997222222222223

        comprimento_trajetoria = 2004.3678382716137
        self.assertEqual(calcula_comprimento(coordenadas_passadas), comprimento_trajetoria)

    def test_calcula_comprimento_longitudes_negativas(self):
        coordenadas_passadas = 24.3206 , -45.221, 29.318611, -1.699723

        comprimento_trajetoria = 4330022.180059105
        self.assertEqual(calcula_comprimento(coordenadas_passadas), comprimento_trajetoria)

    def test_calcula_comprimento_coordenadas_negativas(self):
        coordenadas_passadas = -24.998 , -49.5, -25.832, -48.429

        comprimento_trajetoria = 142020.46427454923
        self.assertEqual(calcula_comprimento(coordenadas_passadas), comprimento_trajetoria)


class CombinaPontosDeDoisEmDois(unittest.TestCase):
    def test_combina_pontos_de_dois_em_dois_com_quatro_pontos(self):
        coordenadas_passadas = [('11.11', '55.55'), ('22.22', '66.66'), ('33.33', '77.77'), ('44.44', '88.88')]

        coordenadas_combinadas = [('11.11','55.55', '22.22', '66.66'), ('22.22', '66.66','33.33', '77.77'),('33.33', '77.77','44.44', '88.88')]
        self.assertEqual(combina_pontos_de_dois_em_dois(coordenadas_passadas), coordenadas_combinadas)

    def test_combina_pontos_de_dois_em_dois_com_dois_pontos(self):
        coordenadas_passadas = [('89.99', '42.42'), ('27.27', '26.48')]

        coordenadas_combinadas = [('89.99', '42.42','27.27', '26.48')]
        self.assertEqual(combina_pontos_de_dois_em_dois(coordenadas_passadas), coordenadas_combinadas)

    def test_combina_pontos_de_dois_em_dois_com_um_ponto(self):
        coordenadas_passadas = [('48.48', '29.29')]

        coordenadas_combinadas = []
        self.assertEqual(combina_pontos_de_dois_em_dois(coordenadas_passadas), coordenadas_combinadas)


class SeparaECombinaPontosPorTrajetoria(unittest.TestCase):
    def test_separa_e_combina_pontos_por_trajetoria(self):
        coordenadas_passadas = {804: [('23.23', '38.38'), ('24.24', '39.39'), ('25.25', '40.40'), ('26.26', '41.41')],
                                803: [('12.12', '17.17'), ('13.13', '18.18'), ('14.14', '19.19'), ('15.15', '20.20')]}

        coordenadas_esperadas = {804: [('23.23', '38.38','24.24', '39.39'), ('24.24', '39.39', '25.25', '40.40'), ('25.25', '40.40', '26.26', '41.41')],
                                803: [('12.12', '17.17', '13.13', '18.18') , ('13.13', '18.18', '14.14', '19.19'), ('14.14', '19.19', '15.15', '20.20')]}
        self.assertEqual(separa_e_combina_pontos_por_trajetoria(coordenadas_passadas),coordenadas_esperadas)

    def test_separa_e_combina_pontos_por_trajetoria_com_um_ponto(self):
        coordenadas_passadas = {800: [('23.23', '38.38')]}

        coordenadas_esperadas = {800: [0]}
        self.assertEqual(separa_e_combina_pontos_por_trajetoria(coordenadas_passadas),coordenadas_esperadas)


class DeterminaComprimentoTotal(unittest.TestCase):
    def test_determina_comprimento_total(self):
        passado = {801: [('23.23', '38.38','24.24', '39.39'), ('24.24', '39.39', '25.25', '40.40'), ('25.25', '40.40', '26.26', '41.41')],
                802: [('12.12', '17.17', '13.13', '18.18') , ('13.13', '18.18', '14.14', '19.19'), ('14.14', '19.19', '15.15', '20.20')],
                803: [('12.12', '17.17','13.13', '18.18'), ('13.13', '18.18','15.15', '20.20')]}

        esperado = {801:455104.90874550526, 802: 469784.896926947, 803: 469783.2310668718}
        self.assertEqual(determina_comprimento_total(passado),esperado)


class CalculaCatetoOposto(unittest.TestCase):
    def test_calcula_cateto_oposto(self):
        passado = {894: [('23.793563', '38.021117', '23.791013', '38.023109'), ('23.791013', '38.023109', '23.790215', '38.023570')],
        1000: [('23.790215', '38.023570', '23.789474', '38.023890'), ('23.789474', '38.023890', '23.789137', '38.023937')],
        1500: [('23.789137', '38.023937', '23.791826', '38.022475'), ('23.791826', '38.022475', '23.792013', '38.022445')]}

        esperado ={894: [-283.8634499999317,-88.83256199995907],
        1000:[-82.48737900015973,-37.514502999805785],
        1500:[299.3367910000182,20.816653000042447]}
        self.assertEqual(calcula_cateto_oposto(passado),esperado)


class CalculaHipotenusa(unittest.TestCase):
    def test_calcula_hipotenusa(self):
        passado = {9009: [('23.936230', '37.960323', '23.947121', '37.959564'), ('23.947121', '37.959564', '23.949181', '37.959125')],
            9010: [('23.949181', '37.959125', '23.952711', '37.958743'), ('23.952711', '37.958743', '23.957340', '37.957244')],
            9012: [('23.957340', '37.957244','23.947121', '37.959564')]}

        esperado = {9009: [1213.4780017409967, 233.36548314227386], 9010: [394.43296409741134, 536.7873312228772], 9012: [1160.500370477989]}
        self.assertEqual(calcula_hipotenusa(passado),esperado)


class DefineAngulacao(unittest.TestCase):
    def test_define_angulacao_numeros_positivos(self):
        hipotenusa = 1213.478
        cateto = 82.48

        esperado = 0.0679699178724295
        self.assertEqual(define_angulacao(hipotenusa,cateto),esperado)

    def test_define_angulacao_numeros_negativos(self):
        hipotenusa = -213.478
        cateto = -82.48

        esperado = 0.38636299759225773
        self.assertEqual(define_angulacao(hipotenusa,cateto),esperado)

    def test_define_angulacao_um_numero_negativo(self):
        hipotenusa = -7213.478
        cateto = 982.48

        esperado = -0.13620059560727849
        self.assertEqual(define_angulacao(hipotenusa,cateto),esperado)

if __name__ == '__main__':
    unittest.main()
