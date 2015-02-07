# -*- coding: utf-8 -*-
from django.shortcuts import render, redirect
from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse
from trajetorias.pesquisa import realiza_pesquisa

# Create your views here.
def cria_poligono(request):
        return render(request, 'trajetorias_passo1.html', {
                'passo_atual': 'Passo 1', 
                'passo_atual_texto': 'Desenhe um polígono no mapa representando a área de busca'
            })

def define_similaridades(request):
    if request.method == 'POST':
        coordenadas_selecionadas = request.POST.get('coordenadas')
        #trabalhar informações coordenadas

        return render(request, 'trajetorias_passo2.html', {
            'passo_atual': 'Passo 2', 
            'passo_atual_texto': 'Preencha o formulário com os parâmetros desejados'
        })

    else:
        return render(request, 'trajetorias_passo1.html', {
                'passo_atual': 'Passo 1', 
                'passo_atual_texto': 'Desenhe um polígono no mapa representando a área de busca'
            })

def mostra_resultado_trajetorias(request):
    if request.method == 'POST':
        tempo = int(request.POST.get('percentual_tempo'))
        comprimento = int(request.POST.get('percentual_comprimento'))
        #resultado = realiza_pesquisa(tempo, comprimento)
        resultado = [
                {'id_trajetoria1': '889', 'id_trajetoria2': '916', 'tempo': '90','comprimento': '85'},
                {'id_trajetoria1': '920', 'id_trajetoria2': '916', 'tempo': '87','comprimento': '79'}
            ]

        return render(request, 'trajetorias_passo3.html', { 
                'passo_atual': 'Passo 3', 
                'passo_atual_texto': 'bla bla bla',
                'pares_com_similaridade': resultado
            })
    else:
        return render(request, 'trajetorias_passo1.html', {
                'passo_atual': 'Passo 1', 
                'passo_atual_texto': 'Desenhe um polígono no mapa representando a área de busca'
            })

def trajetorias_desenhadas(request):
    if request.method == 'POST':
        #resultado = request.POST.getlist('trajetorias_similares')
        resultado = [
                {'id_trajetoria': '981', 'pontos': [(21123213,1213213213),(212121,486648),(5456546,879898790),(87989879,5465645654)]}
        ]

        return render(request, 'trajetorias_passo4.html',{
                'trajetorias': resultado
            })