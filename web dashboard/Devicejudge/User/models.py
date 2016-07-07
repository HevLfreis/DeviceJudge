from django.db import models

# Create your models here.


class IP(models.Model):
    ip = models.CharField(max_length=20)
    WINDOWS = models.BooleanField()
    UBUNTU = models.BooleanField()
    MAC = models.BooleanField()
    MOBILE = models.BooleanField()
    behavior = models.TextField()





