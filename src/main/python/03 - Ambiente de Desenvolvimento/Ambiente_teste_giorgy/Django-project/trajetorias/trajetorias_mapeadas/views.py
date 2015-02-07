# -*- coding: utf-8 -*-
from django.shortcuts import render, redirect
from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse
from trajetorias.pesquisa import *

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
            'passo_atual_texto': 'Preencha o formulário com os parâmetros desejados',
            'coordenadas_selecionadas': coordenadas_selecionadas
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
        resultado_pesquisa = realiza_pesquisa(tempo, comprimento)
        resultado_final = monta_dicionario_resultado(resultado_pesquisa)


        return render(request, 'trajetorias_passo3.html', { 
                'passo_atual': 'Passo 3', 
                'passo_atual_texto': 'bla bla bla',
                'pares_com_similaridade': resultado_final
            })
    else:
        return render(request, 'trajetorias_passo1.html', {
                'passo_atual': 'Passo 1', 
                'passo_atual_texto': 'Desenhe um polígono no mapa representando a área de busca'
            })

def trajetorias_desenhadas(request):
    if request.method == 'POST':
        resultado = request.POST.getlist('trajetorias_similares')
        novo_resultado = monta_lista_trajetoria(resultado)

        return render(request, 'trajetorias_passo4.html',{
                'trajetorias': novo_resultado
            })