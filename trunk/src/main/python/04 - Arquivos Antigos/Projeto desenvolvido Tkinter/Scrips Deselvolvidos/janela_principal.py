#!/bin/env python3
#coding: utf-8

from Tkinter import *
from trajetorias.trajetorias import *
import tkMessageBox


def limpa_campos():
    informacao_comprimento.delete(0,END)
    informacao_tempo.delete(0,END)
    label_resultado.configure(text='')
    resultado.configure(text='')

def valida_pesquisa():
    try:
        tempo = int(informacao_tempo.get())
        comprimento = int(informacao_comprimento.get())
                
        if tempo > 100 or comprimento > 100:
            tkMessageBox.showerror('Erro de Preenchimento','Informe parametros de similaridade entre (0 e 100)')
        else:
            resultado = realiza_pesquisa(tempo, comprimento)
            label_pesquisa.configure(text='Resultado da Pesquisa')
            if len (resultado) <= 0:
                label_resultado.configure(text='Nenhum resultado encontrado')
            elif len(resultado) == 1:
                label_resultado.configure(text='Existe %s trajetoria Similar' %len(resultado))
            else:
                label_resultado.configure(text='Existem %s trajetorias similares' %len(resultado))
    except:
        tkMessageBox.showerror('Erro de Preenchimento','Informe parametros de similaridade com números inteiros')

    lista_checks = range(len(resultado))
    lista_variaveis =[]
    for item in lista_checks:
        var = IntVar()
        Check = Checkbutton(frame_resultado_pesquisa, text=item, variable=var)
        Check.pack(side='left')
        lista_variaveis.append(var)
        
    return lista_variaveis 

def realiza_pesquisa(tempo, comprimento):
    trajetorias_tempo = trajetorias_similares_tempo(tempo)
    trajetorias_comprimento = trajetorias_similares_comprimento(comprimento)
    resultado = (relaciona_resultados_de_similaridade(trajetorias_tempo,trajetorias_comprimento))
    return resultado

def cria_janela():
    janela_princioal = Tk()
    janela_princioal.resizable(width=False, height=False)
    return janela_princioal
    
def executa_prgrama(janela_princioal):
    janela_princioal.mainloop()

def cria_frame_master(janela):
    frame_master = Frame(janela)
    frame_master.pack(side='left')
    return frame_master

def cria_frames_layout(janela_princioal):
    #CRIA FRAMES DO LAYOUT
    
    frame_menu = Frame(janela_princioal, relief = 'sunken' , border = 1)
    frame_menu.master.title('Similaridade Trajetorias')
    frame_menu.pack(side='left')

    frame_matplotlib = Frame(janela_princioal, relief = 'sunken' , border = 1)
    frame_matplotlib.pack(side='left', padx=300)
    
    return frame_menu, frame_matplotlib

def cria_frame_pesquisa(frame_menu):
    
    #FRAME PAINEL DE PESQUISA
    frame_pesquisa = Frame(frame_menu)
    frame_pesquisa.pack()
    
    #SUBFRAME PAINEL DE PESQUISA - TOPO
    frame_topo = Frame(frame_pesquisa, relief = 'sunken' , border = 1)
    frame_topo.pack(fill='x', padx = 2, pady = 2)
    
    label1 = Label(frame_topo, text = 'Parametros de Pesquisa:', font=('Times', 13))
    label1.pack(side = 'left', fill='x', padx = 2, pady = 2 )
    
    
    #SUB FRAME PAINEL DE PESQUISA FRAME MÃE_TEMPO/COMPRIMENTO
    frame_tempo_comprimento = Frame(frame_pesquisa, relief = 'sunken' , border = 1)
    frame_tempo_comprimento.pack(fill='x', padx = 2, pady = 2)
    
    #SUBFRAME PAINEL DE PESQUISA - TEMPO
    frame_tempo = Frame(frame_tempo_comprimento)
    frame_tempo.pack(fill='x')
    
    label_tempo = Label(frame_tempo, text = 'Tempo (%):', font=('Times', 10,), justify = 'left')
    label_tempo.pack(side = 'left', padx = 2, pady = 2 )

    global informacao_tempo
    informacao_tempo = Entry(frame_tempo)
    informacao_tempo.pack(side = 'left', expand=True, fill='x')

    #SUBFRAME PAINEL DE PESQUISA - COMPRIMENTO
    frame_comprimento = Frame(frame_tempo_comprimento)
    frame_comprimento.pack(fill='x')
    
    label_comprimento = Label(frame_comprimento, text = 'Comprimento (%):', font=('Times', 10,), justify = 'left')
    label_comprimento.pack(side = 'left', fill='x' )
    
    global informacao_comprimento 
    informacao_comprimento = Entry(frame_comprimento)
    informacao_comprimento.pack(side = 'left', fill='x')
    
    return frame_pesquisa
    
def cria_frame_resultado(frame_pesquisa):
    
    #SUBFRAME PAINEL DE PESQUISA - FRAME MÃE_RESULTADO
    frame_mae_resultado = Frame(frame_pesquisa, relief = 'sunken' , border = 1)
    frame_mae_resultado.pack(fill='x',padx = 2, pady = 2)
    
    #SUBFRAME PAINEL DE PESQUISA - RESULTADO
    frame_pesquisa = Frame(frame_mae_resultado)
    frame_pesquisa.pack(fill='x')
    
    frame_resultado = Frame(frame_mae_resultado)
    frame_resultado.pack(fill='x')
    
    global label_pesquisa
    label_pesquisa = Label(frame_pesquisa, font=('Times', 12, 'bold'))
    label_pesquisa.pack()
    
    global label_resultado
    label_resultado = Label(frame_resultado, font=('Times', 10))
    label_resultado.pack()
    
    #SUBFRAME PAINEL DE PESQUISA - DADOS DO RESULTADO PESQUISA
    frame_resultado_pesquisa = Frame(frame_mae_resultado)
    frame_resultado_pesquisa.pack(fill='x', pady = 180)
    

    


def cria_frame_botoes(frame_pesquisa):
    
    #SUBFRAME PAINEL DE PESQUISA - BOTÕES
    frame_botoes = Frame(frame_pesquisa, relief = 'sunken' , border = 1)
    frame_botoes.pack(fill='x',padx = 2, pady = 2)
    
    frame_controle_display = Frame(frame_botoes)
    frame_controle_display.pack(side='left',padx=20)
    
    botao_pesquisa = Button(frame_botoes, text="Pesquisa", command=valida_pesquisa)
    botao_pesquisa.pack(side='left',ipadx=6, padx=4)
    
    button_limpa = Button(frame_botoes, text="Limpar", command=limpa_campos)
    button_limpa.pack(ipadx=6,side="left")



def main():

    janela_princioal = cria_janela()
    frame_menu, frame_matplotlib = cria_frames_layout(janela_princioal)
    frame_pesquisa = cria_frame_pesquisa(frame_menu)
    cria_frame_resultado(frame_pesquisa)
    cria_frame_botoes(frame_pesquisa)


    



    
    imagem = Label(frame_matplotlib, text = 'Imagem Matplotlib', font=('Times', 10,))
    imagem.pack()
    


    
    #executa programa
    executa_prgrama(janela_princioal)


if __name__ == '__main__':
    main()
    
