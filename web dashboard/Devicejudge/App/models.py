from django.db import models

# Create your models here.


class FP(models.Model):
    app_name = models.CharField(max_length=20)
    UA = models.NullBooleanField()
    fingerprint = models.CharField(max_length=100)


class SPEED(models.Model):
    speed = models.DecimalField(max_digits=5, decimal_places=2)


class LOG(models.Model):
    source = models.CharField(max_length=20)
    destination = models.CharField(max_length=20)
    behavior = models.CharField(max_length=50)
    timestamp = models.DateTimeField()
    requesturl = models.TextField()

