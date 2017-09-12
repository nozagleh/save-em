# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib import admin

from .models import Persons, Userkeys, PersonLog

# Register your models here.
admin.site.register(Persons)
admin.site.register(PersonLog)
admin.site.register(Userkeys)