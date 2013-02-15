from django.conf.urls import patterns, url #include,

urlpatterns = patterns('',
    url(r'^$',                                                  'contato_salvo.web.home',                               name='home'),
    url(r'^teste/([a-z]+)/$',                                   'contato_salvo.web_services.teste',                     name='teste'),
    url(r'^usuario/autenticar/$',                               'contato_salvo.web_services.autenticar',                name='autenticar'),
    url(r'^usuario/registrar/$',                                'contato_salvo.web_services.registrar_usuario',         name='registrar'),
    url(r'^usuario/([a-zA-Z0-9]+)/recuperar/senha/$',           'contato_salvo.web_services.recuperar_senha',           name='recuperar_senha'),
    url(r'^contato/registrar/$',                                'contato_salvo.web_services.registrar_contato',         name='registrar_contato'),
    url(r'^usuario/([a-zA-Z0-9]+)/contato/([a-zA-Z]+)/$',       'contato_salvo.web_services.detalhe_contato',           name='detalhe_contato'),
    url(r'^usuario/([a-zA-Z0-9]+)/contatos/([a-zA-Z0-9]+)/$',   'contato_salvo.web_services.pesquisar_contato',         name='pesquisar_contato'),
    url(r'^usuario/([a-zA-Z0-9]+)/contatos/letra/([A-Z])/$',    'contato_salvo.web_services.listar_contato_por_letra',  name='listar_contato_por_letra'),
    url(r'^usuario/([a-zA-Z0-9]+)/contatos/$',                  'contato_salvo.web_services.contatos',                  name='contatos'),
)
