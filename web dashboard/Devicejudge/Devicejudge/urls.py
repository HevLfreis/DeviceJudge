from User import views
import settings
from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'Devicejudge.views.home', name='home'),
    # url(r'^Devicejudge/', include('Devicejudge.foo.urls')),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
    url(r'^$', views.redirect2main, name='redirect'),

    url(r'^home/$', views.index, name='home'),
    url(r'^login/$', views.login_view, name='login'),
    url(r'^logout/$', views.logout_view, name='logout'),
    url(r'^ipusers/$', views.ipusers, name='ipusers'),
    url(r'^ipuser/id=(.+)/$', views.ipuser, name='ipuser'),
    url(r'^ipsearch/$', views.ipsearch, name='ipsearch'),
    url(r'^fps/$', views.fingerprints, name='fps'),
    url(r'^fpsearch/$', views.fpsearch, name='fpsearch'),
    url(r'^fp/id=(.+)/$', views.fpedit, name='fpedit'),
    url(r'^applist/$', views.applist, name='applist'),
    url(r'^save/$', views.save, name='save'),
    url(r'^delete/$', views.delete, name='delete'),
    url(r'^stats/$', views.stats, name='stats'),
    url(r'^device/$', views.device, name='device'),
    url(r'^app/$', views.app, name='app'),
    url(r'^about/$', views.about, name='about'),
    url(r'^logs/$', views.logs, name='logs'),
    url(r'^logs/timetable/$', views.timetable, name='timetable'),
    url(r'^logs/timetable/detail/$', views.logdetail, name='logdetail'),

   # url(r'^test/$', views.test, name='1'),


   # url(r'^devicecal/$', views.devicecal, name='deletecal'),
   # url(r'^appcal/$', views.appcal, name='appcal'),
   url(r'^speed/$', views.speed, name='speed'),

    # url(r'^static/(?P<path>.*)$', 'django.views.static.serve', {'document_root': settings.STATIC_ROOT}),
)
