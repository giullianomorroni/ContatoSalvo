#-*- coding: utf-8 -*-
'''
Created on 05/02/2013

@author: giulliano
'''

from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse
from entidades import Usuario, Contato
import json


def teste(request, _id):
    return HttpResponse('teste' + _id)
    
@csrf_exempt
def autenticar(request):
    email = request.POST['email']
    senha = request.POST['senha']
    u = Usuario.usuario()
    usuario = u.autenticar(email, senha);
    if usuario != None:
        usuario['token'] = '123456'
        id_usuario = usuario['_id']
        del usuario['_id'];
        usuario['id'] = str(id_usuario)
        resultado = json.dumps(usuario)
        return HttpResponse(resultado)
    else:
        return HttpResponse(return_msg('usuario nao autenticado', False))


def return_msg(mensagem='', success=True):
    resultado = json.dumps( {'erro': mensagem , 'success': str(success)})
    return resultado;

@csrf_exempt
def registrar_usuario(request):
    email = request.POST['email']
    senha = request.POST['senha']
    if (email != ''):
        u = Usuario.usuario()
        usuario = u.registrar(email, senha)
        if usuario != None:
            usuario['token'] = '123456'
            id_usuario = usuario['_id']
            del usuario['_id'];
            usuario['id'] = str(id_usuario)
            resultado = json.dumps(usuario)
            return HttpResponse(resultado)
        else:
            return HttpResponse(return_msg('falha ao registrar usuário', False))
    else:
        return HttpResponse(return_msg('falha ao registrar usuário', False))        

@csrf_exempt
def recuperar_senha(request, _id = ''):
    if (_id != ''):
        u = Usuario.usuario()
        u.recuperar_senha(_id)
    return HttpResponse(return_msg('verifique seu email', True))

@csrf_exempt
def registrar_contato(request):
    data = request.POST['json']
    data = json.loads(data)
    nome = data['nome']
    id_usuario = data['id_usuario']
    telefones = data['telefones']
    c = Contato.contato()
    c.registrar(nome, telefones, id_usuario)
    return HttpResponse(return_msg('ok', True))

@csrf_exempt
def detalhe_contato(request, _id='', nome=''):
    contatoMdl = Contato.contato();
    contato = contatoMdl.pesquisar_por_nome(nome, _id)
    if contato == None:
        return HttpResponse(return_msg('contato não encontrado', False))
    resultado = json.dumps(contato)
    print resultado
    return HttpResponse(resultado)

@csrf_exempt
def pesquisar_contato(request, _id = '', keyword= ''):
    usuarioMdl = Usuario.usuario();
    usuario = usuarioMdl.consultar_conta(_id);
    if usuario == None:
        return HttpResponse(return_msg('contatos não encontrados', False))
    contatoMdl = Contato.contato();
    contatos = contatoMdl.pesquisar(usuario['_id'], keyword)
    if contatos == None:
        return HttpResponse(return_msg('contato não encontrado', False))
    aux = []
    for c in contatos:
        aux.append(str(c))
    resultado = json.dumps({'contatos' : aux})
    return HttpResponse(str(resultado))

@csrf_exempt
def listar_contato_por_letra(request, _id = '', letra= ''):
    usuarioMdl = Usuario.usuario();
    usuario = usuarioMdl.consultar_conta(_id);
    if usuario == None:
        return HttpResponse(return_msg('contatos não encontrados', False))
    contatoMdl = Contato.contato();
    contatos = contatoMdl.pesquisar_por_letra(_id, letra)
    if contatos == None:
        return HttpResponse(return_msg('contato não encontrado', False))
    resultado = json.dumps({'contatos' : contatos})
    print resultado
    return HttpResponse(resultado)

@csrf_exempt
def contatos(request, _id=''):
    usuarioMdl = Usuario.usuario();
    usuario = usuarioMdl.consultar_conta(_id);
    if usuario == None:
        return HttpResponse(return_msg('contatos não encontrados', False)) 
    contatoMdl = Contato.contato();
    contatos = contatoMdl.listar(usuario['_id'])
    aux = []
    for c in contatos:
        aux.append(str(c))
    resultado = json.dumps({'contatos' : aux})
    return HttpResponse(resultado)
