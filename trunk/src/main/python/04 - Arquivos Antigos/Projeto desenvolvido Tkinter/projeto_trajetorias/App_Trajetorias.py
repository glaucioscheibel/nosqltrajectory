#!/bin/env python3
#coding: utf-8

from Tkinter import *
from trajetorias.trajetorias import *
import tkMessageBox

class trajetoriasApp:
    
    def __init__(self):
        self.janela_princioal = Tk()
        self.janela_princioal.resizable(width=False, height=False)
    
    def limpa_campos(self):
        self.informacao_comprimento.delete(0,END)
        self.informacao_tempo.delete(0,END)
        self.label_pesquisa.configure(text='')
        self.label_resultado.configure(text='')
        self.frame_resultado_pesquisa.destroy()
        self.frame_botao_matplot.destroy()

    def verifica_pesquisa(self):
        try:
            self.frame_resultado_pesquisa.destroy()
            self.frame_botao_matplot.destroy()
            self.valida_pesquisa()
            
        except:
            self.valida_pesquisa()

    def valida_pesquisa(self):
        
        try:
            
            tempo = int(self.informacao_tempo.get())
            comprimento = int(self.informacao_comprimento.get())
            
            if tempo > 100 or comprimento > 100:
                tkMessageBox.showerror('Erro de Preenchimento','Informe parametros de similaridade entre (0 e 100)')
                
            else:                
                self.label_pesquisa.configure(text='Resultado da Pesquisa')
                self.resultado_trajetorias = self.realiza_pesquisa(tempo, comprimento)
                
                if len(self.resultado_trajetorias) <= 0:
                    self.label_resultado.configure(text='Nenhum Resultado')
                    
                elif len(self.resultado_trajetorias) == 1:
                    self.label_resultado.configure(text='%s Trajetoria' %len(self.resultado_trajetorias))
                    
                else:
                    self.label_resultado.configure(text='%s Trajetorias' %len(self.resultado_trajetorias))
                    
                self.cria_listbox()
                self.cria_botao_matlab()
        except:
            tkMessageBox.showerror('Erro de Preenchimento','Informe parametros de similaridade com números inteiros')
    

    def realiza_pesquisa(self,tempo, comprimento):
        trajetorias_tempo = trajetorias_similares_tempo(tempo)
        trajetorias_comprimento = trajetorias_similares_comprimento(comprimento)
        resultado = (relaciona_resultados_de_similaridade(trajetorias_tempo,trajetorias_comprimento))
        return resultado

    def pega_trajetoria_selecionada(self):
        
        lista_de_trajetorias = []
        for valor in self.listbox.curselection():
            lista_de_trajetorias.append(int(valor))
        print (lista_de_trajetorias)

    def executa_prgrama(self):
        self.janela_princioal.mainloop()

    def cria_frame_master(self):
        self.frame_master = Frame(self.janela_princioal)
        self.frame_master.master.title('Similaridade Trajetorias')
        self.frame_master.pack(side='left')
        
    def cria_frames_layout(self):
        #CRIA FRAMES DO LAYOUT
        
        self.frame_menu = Frame(self.frame_master, relief = 'sunken' , border = 1)
        
        self.frame_menu.pack(side='left')

        #self.frame_matplotlib = Frame(self.frame_master, relief = 'sunken' , border = 1)
        #self.frame_matplotlib.pack(side='left', ipadx=400)
        

    def cria_listbox(self):
        
        self.listbox_ativo = 'ativo'
        
        #SUBFRAME PAINEL DE PESQUISA - DADOS DO RESULTADO PESQUISA
        self.frame_resultado_pesquisa = Frame(self.frame_mae_resultado,relief = 'sunken' , border = 1)
        self.frame_resultado_pesquisa.pack(fill='x')
        
        self.frame_resultado_listbox = Frame(self.frame_resultado_pesquisa)
        self.frame_resultado_listbox.pack(fill='x')
    
        scrollbar = Scrollbar(self.frame_resultado_listbox)
        scrollbar.pack(side=RIGHT, fill=Y)
        
        self.listbox = Listbox(self.frame_resultado_listbox, selectmode='multiple')
        self.listbox.pack(side='left')
        
        scrollbar.configure(command=self.listbox.yview)
        self.listbox.configure(yscrollcommand=scrollbar.set)
        
        lista_checks = range(len(self.resultado_trajetorias))
        for i in lista_checks: 
            self.listbox.insert(END, str(i+1))

    def cria_frame_pesquisa(self):
        
        #FRAME PAINEL DE PESQUISA
        self.frame_pesquisa = Frame(self.frame_menu)
        self.frame_pesquisa.pack()
        
        #SUBFRAME PAINEL DE PESQUISA - TOPO
        frame_topo = Frame(self.frame_pesquisa, relief = 'sunken' , border = 1)
        frame_topo.pack(fill='x', padx = 2, pady = 2)
        
        label1 = Label(frame_topo, text = 'Parametros de Pesquisa', font=('Times', 11))
        label1.pack(fill='x', padx = 2, pady = 2 )
        
        
        #SUB FRAME PAINEL DE PESQUISA FRAME MÃE_TEMPO/COMPRIMENTO
        frame_tempo_comprimento = Frame(self.frame_pesquisa, relief = 'sunken' , border = 1)
        frame_tempo_comprimento.pack(fill='x', padx = 2, pady = 2)
        
        #SUBFRAME PAINEL DE PESQUISA - TEMPO
        frame_tempo = Frame(frame_tempo_comprimento)
        frame_tempo.pack(fill='x')
        
        label_tempo = Label(frame_tempo, text = 'Tempo (%):', font=('Times', 10,), justify = 'left')
        label_tempo.pack(side = 'left', padx = 2, pady = 2 )
        
        self.informacao_tempo = Entry(frame_tempo, bd = 2, width=13)
        self.informacao_tempo.pack(side = 'left')

        #SUBFRAME PAINEL DE PESQUISA - COMPRIMENTO
        frame_comprimento = Frame(frame_tempo_comprimento)
        frame_comprimento.pack(fill='x')
        
        label_comprimento = Label(frame_comprimento, text = 'Comprimento (%):', font=('Times', 10,), justify = 'left')
        label_comprimento.pack(side = 'left', fill='x' )
        
         
        self.informacao_comprimento = Entry(frame_comprimento, bd = 2, width=9)
        self.informacao_comprimento.pack(side = 'left')
        
    def cria_frame_resultado(self):
        
        
        #SUBFRAME PAINEL DE PESQUISA - FRAME MÃE_RESULTADO
        self.frame_mae_resultado = Frame(self.frame_pesquisa, relief = 'sunken' , border = 1)
        self.frame_mae_resultado.pack(fill='x',padx = 2, pady = 2)
        
        #SUBFRAME PAINEL DE PESQUISA - RESULTADO
        frame_pesquisa = Frame(self.frame_mae_resultado)
        frame_pesquisa.pack(fill='x')
        
        frame_resultado = Frame(self.frame_mae_resultado)
        frame_resultado.pack(fill='x')
        
        self.label_pesquisa = Label(frame_pesquisa, font=('Times', 10, 'bold'))
        self.label_pesquisa.pack()
        
        self.label_resultado = Label(frame_resultado, font=('Times', 10))
        self.label_resultado.pack()
    
    def cria_frame_botoes(self):
        
        #SUBFRAME PAINEL DE PESQUISA - BOTÕES
        frame_botoes = Frame(self.frame_pesquisa, relief = 'sunken' , border = 1)
        frame_botoes.pack(fill='x',padx = 2, pady = 2)
        
        frame_layout_botoes = Frame(frame_botoes)
        frame_layout_botoes.pack(side='left',padx=6)
   
        botao_pesquisa = Button(frame_botoes, text="Pesquisa", command=self.verifica_pesquisa)
        botao_pesquisa.pack(side='left')
        
        button_limpa = Button(frame_botoes, text="Limpar", command=self.limpa_campos)
        button_limpa.pack(side='left', ipadx=5)
    
    def cria_botao_matlab(self):
        
        #SUBFRAME PAINEL DE PESQUISA - BOTÕES
        self.frame_botao_matplot = Frame(self.frame_resultado_pesquisa, relief = 'sunken' , border = 1)
        self.frame_botao_matplot.pack(fill='x',padx = 2, pady = 2)
        
        button_imprimir = Button(self.frame_botao_matplot, text="Visualizar Trajetoria", command= self.pega_trajetoria_selecionada)
        button_imprimir.pack()



def main():
    
    trajetoriasapp = trajetoriasApp()
    trajetoriasapp.cria_frame_master()
    trajetoriasapp.cria_frames_layout()
    trajetoriasapp.cria_frame_pesquisa()
    trajetoriasapp.cria_frame_resultado()
    trajetoriasapp.cria_frame_botoes()
    trajetoriasapp.executa_prgrama()
    
if __name__ == '__main__':
    main()
    
