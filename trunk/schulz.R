# Aplicação em R estáticas base schulz
# 
# 5,6,7,13

#select distinct(segmento.id), diferenca_azimute
#from segmento, ponto_segmento, ponto
#where segmento.id = ponto_segmento.segmento_id
#and ponto_segmento.ponto_id = ponto.id
#and ponto.trajetoria_id in (5,6,7,13)
#order by segmento.id

# Author: Alexandre
###############################################################################
require(pspearman)

x<-read.csv("C:/dev/projetosudesc/inferetraj/5.csv", header=FALSE, sep=",")
y<-read.csv("C:/dev/projetosudesc/inferetraj/13.csv",header=FALSE, sep=",")
#tranforma de datatable (padrão csv) para uma matriz e depois para um vetor
#http://stackoverflow.com/questions/2545228/converting-a-dataframe-to-a-vector-by-rows
x<-c(t(x)) 
y<-c(t(y))

cor.test(x, y, method = "pearson", alternative = "two.sided", conf.level = 0.95)
cor.test(x, y, method = "spearman", alternative = "two.sided")
spearman.test(x, y, alternative ="two.sided", approximation ="exact")

plot(x,y,pch=19)

