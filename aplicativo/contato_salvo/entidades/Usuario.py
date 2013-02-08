#-*- coding: utf-8 -*-
'''
Created on 05/12/2012

@author: giulliano
'''

from settings import DB_USER, DB_PASSWORD, DB_HOST, DB_PORT
from pymongo import MongoClient
from bson.objectid import ObjectId
from mail import Mail

class usuario(object):

    connection = None;
    db = None;

    def __init__(self):
        self.connection = MongoClient(DB_HOST, int(DB_PORT))
        self.db = self.connection.contato_salvo
        self.db.authenticate(DB_USER, DB_PASSWORD)

    def autenticar(self, email, senha):
        colecao = self.db.usuarios
        return colecao.find_one( {'email':email, 'senha':senha} )

    def recuperar_senha(self, email):
        colecao = self.db.usuarios
        usuario = colecao.find_one( {'email':email} )
        if usuario == None:
            return
        senha = usuario['senha']
        mail = Mail();
        mail.send_message(senha, email);

    def registrar(self, email, senha):
        try:
            colecao = self.db.usuarios
            colecao.insert( {'email':email, 'senha':senha} )
            return colecao.find_one( {'email':email} )
        except:
            return None;    

    def consultar_conta(self, _id):
        colecao = self.db.usuarios
        return colecao.find_one( {'_id': ObjectId(_id)} )