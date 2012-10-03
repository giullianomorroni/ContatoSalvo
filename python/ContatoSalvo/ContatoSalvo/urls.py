from django.conf.urls.defaults import patterns, include, url

from django.contrib import admin
admin.autodiscover()

handler404 = 'views._404'
handler500 = 'views._500'

urlpatterns = patterns('',
    url(r'^$', 'views.index', name='index'),
    url(r'^perfil/(?P<perfil>\w+)/$', 'views.perfil_completo'),
    url(r'^home/(?P<nome>\w+)/$', 'views.home'),

    url(r'^autenticacao/$', 'views.autenticacao'),
    url(r'^autenticar/$', 'views.autenticar'),

    url(r'^registrar/contato/$', 'views.registrar_contato'),
    url(r'^novo/contato/$', 'views.novo_contato'),

    url(r'^nova/pessoa/$', 'views.nova_conta'),
    url(r'^registrar/pessoa/$', 'views.registrar_pessoa'),

    url(r'^atualizar/pessoa/$', 'views.atualizar_pessoa'),
    url(r'^atualizacao/perfil/$', 'views.atualizacao_pessoa'),

    url(r'^sair/$', 'views.sair'),
    url(r'^admin/', include(admin.site.urls)),
)
