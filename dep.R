# Replicação Teste DEP Prof Elisa
# 
# Author: Alexandre
###############################################################################
require(pspearman)

x<-c(36, 335, 41, 33, 64)
y<-c(12, 58, 44, 29, 49)

cor.test(x, y, method = "pearson", alternative = "two.sided", conf.level = 0.95)
cor.test(x, y, method = "spearman", alternative = "two.sided")
spearman.test(x, y, alternative ="two.sided", approximation ="exact")

plot(x,y,pch=19)
