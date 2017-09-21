from django.conf import settings
from django.conf.urls.static import url, static

from . import views

urlpatterns = [
	url(r'^img/', views.addImage, name='addImg'),
	url(r'^person/add/$', views.addPerson, name='addPerson'),
	url(r'^persons/found', views.personsFound, name='personsFound'),
	url(r'^persons/lost', views.personsLost, name='personsLost'),
	url(r'^persons/', views.persons, name='persons'),
	url(r'^person/(?P<personId>[0-9]+)/$', views.person, name='person'),
    url(r'^$', views.index, name='index'),
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)