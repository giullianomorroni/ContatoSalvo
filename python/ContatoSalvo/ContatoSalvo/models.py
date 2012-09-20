from django.db import models

class Pessoa(models.Model):
    def __unicode__(self):
        return (self.nome +" "+ self.sobrenome);
    nome = models.CharField(max_length=50,  blank=False)
    sobrenome = models.CharField(max_length=100,  blank=False)
    site = models.CharField(max_length=150)
    email = models.CharField(max_length=100, blank=False)
    senha = models.CharField(max_length=10, blank=False)
    exibirSite = models.IntegerField(default=1)
    exibirTelefones = models.IntegerField(default=1)
    exibirEmail = models.IntegerField(default=1)

class Telefone(models.Model):
    def __unicode__(self):
        return str(self.contato + "(" + self.numero +")" );
    pais = models.CharField(max_length=4)
    ddd = models.IntegerField()
    numero = models.IntegerField(blank=False)
    operadora = models.IntegerField()
    contato = models.CharField(max_length=50)
    particular = models.IntegerField(default=0)
    observacao = models.CharField(max_length=100)
    pessoa = models.ForeignKey(Pessoa,  blank=False)
