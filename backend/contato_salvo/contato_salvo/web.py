#-*- coding: utf-8 -*-
'''
Created on 05/02/2013

@author: giulliano
'''

from django.http import HttpResponse

def home(request):
    return HttpResponse('Contato salvo...página em desenvolvimento')
