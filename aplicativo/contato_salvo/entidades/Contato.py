#-*- coding: utf-8 -*-
'''
Created on 05/12/2012

@author: giulliano
'''

from settings import DB_USER, DB_PASSWORD, DB_HOST, DB_PORT
from pymongo import MongoClient
from bson.objectid import ObjectId

class contato(object):

    connection = None;
    db = None;

    def __init__(self):
        self.connection = MongoClient(DB_HOST, int(DB_PORT))
        self.db = self.connection.contato_salvo
        self.db.authenticate(DB_USER, DB_PASSWORD)

    def registrar(self, nome, telefones, id_usuario):
        colecao = self.db.contatos
        contato = colecao.find_one( {'nome':nome, 'id_usuario':id_usuario } )
        print contato
        if contato != None:
            t = contato['telefones']
            for telefone in telefones:
                t.append(telefone)
            contato['telefones'] = t
        else:
            contato = {'nome':nome, 'id_usuario':id_usuario }
            contato['telefones'] = telefones
        contato = colecao.save( contato )

    def atualizar(self, nome, telefones, _id):
        colecao = self.db.contatos
        registro = colecao.find( {'_id': _id})
        registro['nome'] = nome
        registro['telefones'] = telefones
        return colecao.update( registro )

    def listar(self, id_usuario):
        colecao = self.db.contatos
        return colecao.find( {'id_usuario': str(id_usuario)} )

    def pesquisar(self, id_usuario, keyword):
        colecao = self.db.contatos
        filtro = {'id_usuario': str(id_usuario) }
        key = '.*' + keyword + '.*'
        filtro['$or'] = [{'nome': {'$regex': key, '$options': 'i' }}, {'telefone': {'$regex':key}}]
        print filtro 
        resultados = colecao.find(filtro)
        return resultados

    def pesquisar_por_letra(self, id_usuario, letra):
        colecao = self.db.contatos
        filtro = {'id_usuario': str(id_usuario) }
        key = '^' + letra
        filtro['nome'] = {'$regex': key, '$options': 'i' }
        print filtro 
        resultados = colecao.find(filtro)
        aux = []
        for c in resultados:
            id_contato = c['_id']
            del c['_id']
            c['_id'] = str(id_contato)
            aux.append(c)
        return aux

    def pesquisar_por_id(self, _id):
        colecao = self.db.contatos
        try:
            contato = colecao.find_one( {'_id': ObjectId(_id)} );
            if contato == None:
                return contato;
            _id = contato['_id']
            del contato['_id']
            contato['_id'] = str(_id)
            return contato
        except:
            return None;
        
    def pesquisar_por_nome(self, nome, id_usuario):
        colecao = self.db.contatos
        filtro = {'id_usuario': str(id_usuario) }
        filtro['nome'] = nome
        print str(filtro)
        contato = colecao.find_one(filtro)
        _id = contato['_id']
        del contato['_id']
        contato['_id'] = str(_id)
        return contato    