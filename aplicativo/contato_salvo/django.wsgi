import os
import sys
import django.core.handlers.wsgi

path = '/var/www/contato_salvo/'
#if path not in sys.path:
sys.path.append(path)

os.environ['DJANGO_SETTINGS_MODULE'] = 'settings'
application = django.core.handlers.wsgi.WSGIHandler()