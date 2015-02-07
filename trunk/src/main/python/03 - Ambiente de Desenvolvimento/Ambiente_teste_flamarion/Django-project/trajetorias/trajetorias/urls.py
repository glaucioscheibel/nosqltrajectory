from django.conf.urls import patterns, include, url
from django.contrib import admin

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'trajetorias.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^passo1/$', 'trajetorias_mapeadas.views.cria_poligono', name='trajetorias_mapeadas_passo1'),
    url(r'^passo2/$', 'trajetorias_mapeadas.views.define_similaridades', name='trajetorias_mapeadas_passo2'),
    url(r'^passo3/$', 'trajetorias_mapeadas.views.mostra_resultado_trajetorias', name='trajetorias_mapeadas_passo3'),
    url(r'^passo4/$', 'trajetorias_mapeadas.views.trajetorias_desenhadas', name='trajetorias_mapeadas_passo4'),
)
