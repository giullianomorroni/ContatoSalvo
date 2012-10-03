from django.db import models

class Pessoa(models.Model):
    nome = models.CharField(max_length=50, blank=False)
    sobrenome = models.CharField(max_length=100, blank=False)
    perfil = models.CharField(max_length=100, blank=False, unique=True)
    site = models.CharField(max_length=150)
    email = models.CharField(max_length=100, blank=False, unique=True)
    senha = models.CharField(max_length=10, blank=False)
    exibirSite = models.BooleanField(default=1)
    exibirTelefones = models.BooleanField(default=1)
    exibirEmail = models.BooleanField(default=1)

class Telefone(models.Model):
    pais = models.CharField(max_length=4)
    ddd = models.IntegerField()
    numero = models.IntegerField(blank=False)
    operadora = models.IntegerField()
    contato = models.CharField(max_length=50)
    particular = models.BooleanField(default=0)
    observacao = models.CharField(max_length=100)
    pessoa = models.ForeignKey(Pessoa, blank=False)
