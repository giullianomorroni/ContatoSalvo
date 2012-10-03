from django.contrib.auth import authenticate,  login,  logout
from django.contrib.auth.models import User
from django.shortcuts import render_to_response
from django.http import HttpResponse
from django.template import RequestContext
from ContatoSalvo.models import Pessoa, Telefone
from MySQLdb import IntegrityError


def pessoa_atual(request):
    try:
        p = request.session['pessoa']
        return p;
    except KeyError:
        print("Pessoa nao encontrada/sessao invalida");
    render_to_response('404.html')

def perfil_completo(request, perfil):
    try:
        perfil = Pessoa.objects.get(perfil=perfil)
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
    

def registrar_pessoa(request):
    try:
        p = Pessoa();
        p.nome = request.POST['nome'];
        p.sobrenome = request.POST['sobrenome'];
        p.email = request.POST['email'];
        p.perfil = request.POST['perfil'];
        p.senha = request.POST['senha'];
        p.exibirEmail = ('exibirEmail' in request.POST);
        p.exibirTelefones = ('exibirTelefones' in request.POST);
        p.exibirSite = ('exibirSite' in request.POST);
        p.save();
        User.objects.create_user(p.email, p.email, password=p.senha);
        return render_to_response('autenticar.html',  context_instance=RequestContext(request)) 
    except KeyError:
        error_message = "Preencha os valores corretamente";
        return render_to_response('nova_conta.html',  {"error_message": error_message,  "pessoa": p}, context_instance=RequestContext(request))
    except ValueError:
        error_message = "Preencha os valores obrigatorios";
        return render_to_response('home.html',  {"error_message": error_message,  "pessoa": p}, context_instance=RequestContext(request))
    except IntegrityError, message:
        error_message = message[0]    # get MySQL error code
        return render_to_response('home.html',  {"error_message": error_message,  "pessoa": p}, context_instance=RequestContext(request))

def atualizar_pessoa(request):
    try:
        print request.POST
        p = pessoa_atual(request)
        p = Pessoa.objects.get(email = p.email)
        p.nome = request.POST['nome'];
        p.sobrenome = request.POST['sobrenome'];
        p.email = request.POST['email'];
        p.perfil = request.POST['perfil'];
        p.exibirEmail = ('exibirEmail' in request.POST);
        p.exibirTelefones = ('exibirTelefones' in request.POST);
        p.exibirSite = ('exibirSite' in request.POST);
        p.save();
        try:
            telefones = Telefone.objects.filter(pessoa = p );
        except Pessoa.DoesNotExist:
            print("Pessoa nao encontrada");
            p = None;
        return render_to_response('home.html',  {'pessoa':p,  'telefones' : telefones},  context_instance=RequestContext(request));
    except KeyError:
        error_message = "Preencha os valores corretamente";
        return render_to_response('editar_conta.html',  {"error_message": error_message,  "pessoa": p}, context_instance=RequestContext(request))
    except ValueError:
        error_message = "Preencha os valores obrigatorios";
        return render_to_response('editar_conta.html',  {"error_message": error_message,  "pessoa": p}, context_instance=RequestContext(request))

def atualizacao_pessoa(request):
    p = request.session['pessoa']
    return render_to_response('editar_conta.html', {'pessoa':p}, context_instance=RequestContext(request))

def autenticar(request):
    senha = request.POST['senha'];
    email = request.POST['email'];
    try:
        user = authenticate(username=email, password=senha);
        if user is not None:
            if user.is_active:
                print "usuario autenticado"
                login(request, user)
                p = Pessoa.objects.get(email=email,  senha=senha)
                request.session['pessoa'] = p;
                request.session.set_expiry(300); #5 minutos
                try:
                    telefones = Telefone.objects.filter(pessoa = p );
                except Pessoa.DoesNotExist:
                    print("Pessoa nao encontrada");
                    p = None;
                return render_to_response('home.html',  {'pessoa':p,  'telefones' : telefones},  context_instance=RequestContext(request));
            else:
                return render_to_response('autenticar.html',  {"error_message":"sua conta foi desativada"},  context_instance=RequestContext(request))
        else:
            return render_to_response('autenticar.html',  {"error_message":"usuario e/ou senha incorreto(s)"},  context_instance=RequestContext(request))
    except Pessoa.DoesNotExist:
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
            t.particular = ('particular' in request.POST);
            t.observacao = request.POST['observacao'];
            t.pessoa = p;
            t.save();
            return render_to_response('home.html', {"pessoa":p, "message":"contato salvo"}, context_instance=RequestContext(request))
        else:
            return render_to_response('novo_contato.html', {"message":"autentique-se..."}, context_instance=RequestContext(request))
    except KeyError:
        error_message = "Preencha os valores corretamente";
        return render_to_response('novo_contato.html', {"message":error_message}, context_instance=RequestContext(request))
    except ValueError:
        error_message = "Preencha os valores obrigatorios";
        return render_to_response('novo_contato.html',  {"error_message": error_message,  "pessoa": p}, context_instance=RequestContext(request))
        

def autenticacao(request):
    return render_to_response('autenticar.html',  context_instance=RequestContext(request))

def nova_conta(request):
    return render_to_response('nova_conta.html',  context_instance=RequestContext(request))

def index(request):
    return render_to_response('index.html');