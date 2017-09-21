# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey has `on_delete` set to the desired behavior.
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from __future__ import unicode_literals

import os
import datetime
from django.core import serializers
from django.db import models


class AuthGroup(models.Model):
    name = models.CharField(unique=True, max_length=80)

    class Meta:
        managed = False
        db_table = 'auth_group'


class AuthGroupPermissions(models.Model):
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)
    permission = models.ForeignKey('AuthPermission', models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_group_permissions'
        unique_together = (('group', 'permission'),)


class AuthPermission(models.Model):
    name = models.CharField(max_length=255)
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING)
    codename = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'auth_permission'
        unique_together = (('content_type', 'codename'),)


class AuthUser(models.Model):
    password = models.CharField(max_length=128)
    last_login = models.DateTimeField(blank=True, null=True)
    is_superuser = models.IntegerField()
    username = models.CharField(unique=True, max_length=150)
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    email = models.CharField(max_length=254)
    is_staff = models.IntegerField()
    is_active = models.IntegerField()
    date_joined = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'auth_user'


class AuthUserGroups(models.Model):
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    group = models.ForeignKey(AuthGroup, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_groups'
        unique_together = (('user', 'group'),)


class AuthUserUserPermissions(models.Model):
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)
    permission = models.ForeignKey(AuthPermission, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'auth_user_user_permissions'
        unique_together = (('user', 'permission'),)


class DjangoAdminLog(models.Model):
    action_time = models.DateTimeField()
    object_id = models.TextField(blank=True, null=True)
    object_repr = models.CharField(max_length=200)
    action_flag = models.SmallIntegerField()
    change_message = models.TextField()
    content_type = models.ForeignKey('DjangoContentType', models.DO_NOTHING, blank=True, null=True)
    user = models.ForeignKey(AuthUser, models.DO_NOTHING)

    class Meta:
        managed = False
        db_table = 'django_admin_log'


class DjangoContentType(models.Model):
    app_label = models.CharField(max_length=100)
    model = models.CharField(max_length=100)

    class Meta:
        managed = False
        db_table = 'django_content_type'
        unique_together = (('app_label', 'model'),)


class DjangoMigrations(models.Model):
    app = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    applied = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_migrations'


class DjangoSession(models.Model):
    session_key = models.CharField(primary_key=True, max_length=40)
    session_data = models.TextField()
    expire_date = models.DateTimeField()

    class Meta:
        managed = False
        db_table = 'django_session'

class Persons(models.Model):
    firstname = models.CharField(max_length=64)
    lastname = models.CharField(max_length=128)
    birthdate = models.DateField(blank=True, null=True)
    sex = models.IntegerField()
    nationality = models.CharField(max_length=128)
    height = models.FloatField(blank=True, null=True)
    haircolor = models.CharField(max_length=24, blank=True, null=True)
    weight = models.FloatField(blank=True, null=True)
    gpslocation = models.CharField(max_length=128)
    found = models.IntegerField(blank=True, null=True)
    comments = models.TextField(blank=True, null=True)
    missingdate = models.DateTimeField(default=datetime.date.today)

    class Meta:
        managed = True
        db_table = 'mex_persons'

    def getPersonJSON(self):
     	return self

class Userkeys(models.Model):
    key = models.CharField(unique=True, max_length=255, primary_key=True)
    dateofregistration = models.DateTimeField(blank=True, null=True)  # Field name made lowercase.

def getImagePath(instance, filename):
    return os.path.join('images', str(instance.id), filename)

class PersonImages(models.Model):
    person = models.ForeignKey(Persons, models.DO_NOTHING)
    image = models.ImageField(upload_to=getImagePath, blank=True, null=True)

class KeyHasPerson(models.Model):
    fk_key = models.ForeignKey(Userkeys, models.DO_NOTHING, null=True)
    fk_person = models.ForeignKey(Persons, models.DO_NOTHING, null=True)


class PersonLog(models.Model):
    date = models.DateTimeField(blank=True, null=True)
    operation = models.CharField(max_length=64, blank=True, null=True)
    fk_key = models.ForeignKey(Userkeys, models.DO_NOTHING, null=True)