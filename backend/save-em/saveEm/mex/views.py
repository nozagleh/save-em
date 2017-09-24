# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render, Http404
from django.conf import settings
from django.core.files.storage import FileSystemStorage


# Create your views here.

import json

from django.http import HttpResponse, JsonResponse, HttpRequest

from .models import Persons, PersonImages

from Classes.json import GenerateJSON
from Classes.Person import Person

data = GenerateJSON()

def index(request):
    return HttpResponse("Person root")

def person(request, personId):
	try:
		person = Persons.objects.get(id=personId)
	except Persons.DoesNotExist:
		raise Http404("Person does not exist")

	return JsonResponse(data.getPersonJSON(person, False, 0))

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

def addPerson(request):
	response = "none"
	values = request.GET
	person = Person()
	person.addPerson(request.GET)
	for value in values:
		person.addField(value)

	person.save()
	pId = person.getId()
	return JsonResponse(data.getPersonJSON(person, True, pId))

def addImage(request):
	print request.POST['person_id']
	writeFile(request.FILES['uploaded_file'], request.POST['person_id'])
	return HttpResponse("ok")

def writeFile(f, personId):
	person = Persons.objects.get(id=personId)
	
	img = PersonImages(image=f)
	img.person_id = person.id
	try:
		img.save()
	except Exception as e:
		raise e

	return HttpResponse("ok")
