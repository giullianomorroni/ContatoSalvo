from django.contrib.auth import authenticate,  login,  logout
from django.contrib.auth.models import User
from django.shortcuts import render_to_response
from django.http import HttpResponse
from django.template import RequestContext
from ContatoSalvo.models import Pessoa, Telefone

def index(request):
    print 'teste ok'
    return render_to_response('index.html');

def pessoa_atual(request):
    try:
        p = request.session.get('pessoa');
    except KeyError:
        print("Pessoa nao encontrada/sessao invalida");
        p = None;
    return p;

def perfil_completo(request,  nome,  sobrenome):
    try:
        print(nome + "" +  sobrenome)
        perfil = Pessoa.objects.get(nome=nome,  sobrenome=sobrenome)
        telefones = Telefone.objects.filter(pessoa = perfil );
        return render_to_response('perfil.html',  {'pessoa' : pessoa_atual(request), 'perfil' : perfil,  'telefones': telefones},  context_instance=RequestContext(request));
    except Pessoa.DoesNotExist:
        return render_to_response('index.html',  {'error_message':  'Nao encontramos o perfil solicitado...'},  context_instance=RequestContext(request));


def perfil(request,  nome):
    try:
        perfil = Pessoa.objects.get(nome=nome)
        telefones = Telefone.objects.filter(pessoa = perfil );
        return render_to_response('perfil.html',  {'pessoa' : pessoa_atual(request), 'perfil' : perfil,  'telefones': telefones},  context_instance=RequestContext(request));
    except Pessoa.DoesNotExist:
        return render_to_response('index.html',  {'error_message':  'Nao encontramos o perfil solicitado...'},  context_instance=RequestContext(request));

def home(request,  nome):
    p = pessoa_atual(request); 
    if request.user.is_authenticated():
        try:
            telefones = Telefone.objects.filter(pessoa = p );
        except Pessoa.DoesNotExist:
            print("Pessoa nao encontrada");
            p = None;
        return render_to_response('home.html',  {'pessoa':p,  'telefones' : telefones},  context_instance=RequestContext(request));
    else:
        return render_to_response('home.html',  context_instance=RequestContext(request));

def autenticacao(request):
    return render_to_response('autenticar.html',  context_instance=RequestContext(request))

def nova_conta(request):
    return render_to_response('nova_conta.html',  context_instance=RequestContext(request))    

def registrar_pessoa(request):
    try:
        print(request.POST);
        p = Pessoa();
        p.nome = request.POST['nome'];
        p.sobrenome = request.POST['sobrenome'];
        p.email = request.POST['email'];
        p.senha = request.POST['senha'];
        p.exibirEmail = request.POST['exibirEmail'];
        p.exibirTelefones = request.POST['exibirTelefones'];
        p.exibirSite = request.POST['exibirSite'];
        p.save();
        User.objects.create_user(p.email, p.email, password=p.senha);
        return render_to_response('autenticar.html',  context_instance=RequestContext(request)) 
    except KeyError:
        error_message = "Preencha os valores corretamente";
        return render_to_response('nova_conta.html',  {"error_message": error_message,  "pessoa": p}, context_instance=RequestContext(request))
    except ValueError:
        error_message = "Preencha os valores obrigatorios";
        return render_to_response('home.html',  {"error_message": error_message,  "pessoa": p}, context_instance=RequestContext(request))

    
def autenticar(request):
    senha = request.POST['senha'];
    email = request.POST['email'];
    user = authenticate(username=email, password=senha);
    if user is not None:
        if user.is_active:
            print "usuario autenticado"
            login(request, user)
            p = Pessoa.objects.get(email=email,  senha=senha)
            request.session['pessoa'] = p;
            request.session.set_expiry(300); #5 minutos
            return render_to_response('home.html',  {"pessoa": p , "user":user},  context_instance=RequestContext(request))
        else:
            return render_to_response('autenticar.html',  {"error_message":"sua conta foi desativada"},  context_instance=RequestContext(request))
    else:
        return render_to_response('autenticar.html',  {"error_message":"usuario e/ou senha incorreto(s)"},  context_instance=RequestContext(request))

def sair(request):
    logout(request);
    try:
        del request.session['pessoa'];
    except KeyError:
        print("sessao nao encontrada");        
    return HttpResponse('ate logo...')

def novo_contato(request):
    return render_to_response('novo_contato.html',  {"pessoa":pessoa_atual(request)},  context_instance=RequestContext(request))

def registrar_contato(request):
    try:
        p = pessoa_atual(request);
        if p is not None:
            t = Telefone()
            t.contato = request.POST['contato'];
            t.ddd = request.POST['ddd'];
            t.numero = request.POST['numero'];
            t.operadora = request.POST['operadora'];
            t.pais = request.POST['pais'];
            t.particular = request.POST['particular'];
            t.pessoa = p;
            t.save();
            return render_to_response('home.html', {"pessoa":p, "message":"contato salvo"}, context_instance=RequestContext(request))
        else:
            return render_to_response('novo_contato.html', {"message":"autentique-se..."}, context_instance=RequestContext(request))
    except KeyError:
        print("Pessoa nao encontrada/sessao invalida");
        p = None;
    except ValueError:
        error_message = "Preencha os valores obrigatorios";
        return render_to_response('novo_contato.html',  {"error_message": error_message,  "pessoa": p}, context_instance=RequestContext(request))
        
