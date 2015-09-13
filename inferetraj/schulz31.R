# Aplicação em R estáticas base schulz trajetórias com 31 segmentos id 58,61,94
# 
# Author: Alexandre
###############################################################################
require(pspearman)

x<-read.csv("C:/dev/projetosudesc/inferetraj/58.csv", header=FALSE, sep=",")
y<-read.csv("C:/dev/projetosudesc/inferetraj/61.csv",header=FALSE, sep=",")
#tranforma de datatable (padrão csv) para uma matriz e depois para um vetor
#http://stackoverflow.com/questions/2545228/converting-a-dataframe-to-a-vector-by-rows
x<-c(t(x)) 
y<-c(t(y))

cor.test(x, y, method = "pearson", alternative = "two.sided", conf.level = 0.95)
cor.test(x, y, method = "spearman", alternative = "two.sided")
spearman.test(x, y, alternative ="two.sided", approximation ="t-distribution")

plot(x,y,pch=19)


