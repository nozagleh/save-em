# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render, Http404

# Create your views here.

from django.http import HttpResponse

def index(request):
    return HttpResponse("Welcome!")