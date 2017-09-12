# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render, Http404

# Create your views here.

from django.http import HttpResponse, JsonResponse

from .models import Persons

from Classes.json import GenerateJSON

data = GenerateJSON()

def index(request):
    return HttpResponse("Person root")

def person(request, personId):
	try:
		person = Persons.objects.get(id=personId)
	except Persons.DoesNotExist:
		raise Http404("Person does not exist")

	return JsonResponse(data.getPersonJSON(person))

def persons(request):
	persons = Persons.objects.all()
	return JsonResponse(data.getAllPersonsJSON(*persons))

def personsFound(request):
	return findFoundPersons(1)

def personsLost(request):
	return findFoundPersons(0)

def findFoundPersons(found):
	persons = Persons.objects.all().filter(found=found)
	return JsonResponse(data.getAllPersonsJSON(*persons))
