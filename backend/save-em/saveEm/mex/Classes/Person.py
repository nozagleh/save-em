from datetime import date, datetime

from mex.models import Persons, KeyHasPerson, Userkeys

from django.db import models

class Person:

	def addPerson(self, fields):
		self.fields = fields
		self.personExists = False

		if (Persons.objects.filter(id=self.fields.get('id')).exists()):
			self.person = Persons.objects.get(id=self.fields.get('id'))
			self.personExists = True
		else:
			self.person = Persons()

	def addField(self, fieldname):
		field = self.fields.get(fieldname)
		if (fieldname == 'pkey'):
			self.pkey = field
		else:
			setattr(self.person, fieldname, field)

	def save(self):
		self.person.save()

		if not self.personExists:
			self.addUserKey()

	def addUserKey(self):
		self.user = Userkeys.objects.get(key=self.pkey)
		
		self.userKey = KeyHasPerson()
		self.userKey.fk_key = self.user
		self.userKey.fk_person = self.person
		self.userKey.save()