from unittest import TestCase
import trajetorias_original


class TestCalculaSimilaridade(TestCase):

    def test_exemplo_1(self):

        similaridade = trajetorias_original.calcula_similaridade(
            50,
            [
                [1, 2, 3, 4],
                [2, 6, 7, 8],
                [3, 10, 11, 12],
                [4, 14, 15, 16]
            ]
        )

        esperado = [[60.0, 2, 3], [71.42857142857143, 3, 4]]

        self.assertEqual(esperado, similaridade)

    def test_exemplo_2(self):

        similaridade = trajetorias_original.calcula_similaridade(
            90,
            [
                [1, 2, 3, 4],
                [2, 6, 7, 8],
                [3, 10, 11, 12],
                [4, 14, 15, 16]
            ]
        )

        esperado = [[33.33333333333333, 1, 2],
                    [20.0, 1, 3],
                    [14.285714285714285, 1, 4],
                    [60.0, 2, 3],
                    [42.857142857142854, 2, 4],
                    [71.42857142857143, 3, 4]]


        self.assertEqual(esperado, similaridade)

    def test_exemplo_decrescente(self):

        similaridade = trajetorias_original.calcula_similaridade(
            90,
            [
                [16, 15, 14, 13],
                [12, 11, 10, 9],
                [8, 7, 6, 5],
                [4, 3, 2, 1]
            ]
        )

        esperado = [[73.33333333333333, 16, 12],
                    [46.666666666666664, 16, 8],
                    [20.0, 16, 4],
                    [63.63636363636363, 12, 8],
                    [27.27272727272727, 12, 4],
                    [42.857142857142854, 8, 4]]


        self.assertEqual(esperado, similaridade)

    def test_exemplo_zero(self):

        similaridade = trajetorias_original.calcula_similaridade(
            90,
            [
                [1, 0, 1, 1],
                [2, 0, 1, 1],
                [3, 0, 1, 1],
                [4, 0, 1, 1]
            ]
        )

        esperado = [[100.0, 1, 2],
                    [100.0, 1, 3],
                    [100.0, 1, 4],
                    [100.0, 2, 3],
                    [100.0, 2, 4],
                    [100.0, 3, 4]]


        self.assertEqual(esperado, similaridade)