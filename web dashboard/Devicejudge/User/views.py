# Create your views here.
from django import forms
from django.contrib.auth import authenticate, logout, login
from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator, PageNotAnInteger, EmptyPage
import json
import datetime, time
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import render, render_to_response
from django.template import Context
from App.models import FP, SPEED, LOG
from User.models import IP


class UserForm(forms.Form):
    username = forms.CharField()
    password = forms.CharField(widget=forms.PasswordInput)


class KForm(forms.Form):
    keyword = forms.CharField()


class FPForm(forms.Form):
    appname = forms.CharField()


class SForm(forms.Form):
    appname = forms.CharField()
    fp = forms.CharField()
    UA = forms.ChoiceField(widget=forms.RadioSelect, choices=(('1', True), ('2', False)))


class DForm(forms.Form):
    appname = forms.CharField()
    fp = forms.CharField()
    UA = forms.RadioSelect()


def test(request):
    return render(request, 'User/test.html')


def redirect2main(request):
    return HttpResponseRedirect('/home')

@login_required
def index(request):

    username = request.user.username
    return render(request, 'User/index.html', {'username': username})


def login_view(request):
    if request.method == 'POST':
        uf = UserForm(request.POST)
        if uf.is_valid():
            username = uf.cleaned_data['username']
            password = uf.cleaned_data['password']
            user = authenticate(username=username, password=password)
            if user is not None:
                login(request, user)
                return HttpResponseRedirect('/home/')
            else:
                return render_to_response('User/login.html', {'info': True})
        elif not uf.is_valid():
            return render_to_response('User/login.html', {'info': True})
    return render_to_response('User/login.html', {'info': False})


def logout_view(request):
    logout(request)
    text = "Thank you ~~~"
    return render_to_response('User/logout.html', {'info': text})


@login_required
def ipusers(request):

    username = request.user.username

    mode = request.GET['mode']
    if mode == '0':
        ip_list = IP.objects.all().order_by('id')
    elif mode == '1':
        ip_list = IP.objects.filter(WINDOWS=1).order_by('id')
    elif mode == '2':
        ip_list = IP.objects.filter(UBUNTU=1).order_by('id')
    elif mode == '3':
        ip_list = IP.objects.filter(MAC=1).order_by('id')
    elif mode == '4':
        ip_list = IP.objects.filter(MOBILE=1).order_by('id')
    else:
        ip_list = IP.objects.all().order_by('id')

    paginator = Paginator(ip_list, 10)
    page = request.GET.get('page')
    try:
        ips = paginator.page(page)
    except PageNotAnInteger:
        ips = paginator.page(1)
    except EmptyPage:
        ips = paginator.page(paginator.num_pages)

    context = Context({"ips": ips, 'username': username, 'mode':mode})
    return render_to_response('User/ipusers.html', context)


@login_required
def ipuser(request, param):

    username = request.user.username

    ipobj = IP.objects.get(id=param)
    context = Context({"ip": ipobj, 'username': username})
    return render_to_response('User/ipuser.html', context)


@login_required
def ipsearch(request):

    username = request.user.username
    if request.method == 'POST':
        ipf = KForm(request.POST)
        if ipf.is_valid():
            keyword = ipf.cleaned_data['keyword']
            text = 'NO MATCH FOUNDED!!!'

            ipobjs = IP.objects.filter(ip=keyword)

            if len(ipobjs) is 0:

                ipobjs = IP.objects.filter(behavior__contains=keyword)
                if len(ipobjs) is 0:

                    return render_to_response('User/ipsearch.html', {'info': text})
                else:
                    context = Context({"ips": ipobjs, 'username': username})
                    return render_to_response('User/ipsearch.html', context)
            else:
                context = Context({"ips": ipobjs, 'username': username})
                return render_to_response('User/ipsearch.html', context)
    return render_to_response('User/ipsearch.html', {'username': username})


class Container:
    def __init__(self, num, name):
        self.num = num
        self.name = name


@login_required
def applist(request):

        username = request.user.username
        if request.method == 'POST':
            kf = KForm(request.POST)
            if kf.is_valid():
                keyword = kf.cleaned_data['keyword']
                text = 'NO MATCH FOUNDED!!!'

                appobjs = FP.objects.filter(app_name__contains=keyword)

                if len(appobjs) is 0:
                    return render_to_response('User/applist.html', {'info': text})
                else:
                    context = Context({"apps": [Container(0, appobjs[0].app_name)], 'username': username})
                    return render_to_response('User/applist.html', context)

        fp_list = FP.objects.all()
        name_list = []
        app_list = []
        i = 0
        for fp in fp_list:
            if not list_has(name_list, fp.app_name):
                app_list.append(Container(i, fp.app_name))
                name_list.append(fp.app_name)
                i += 1

        paginator = Paginator(app_list, 10)

        page = request.GET.get('page')
        try:
            p = paginator.page(page)
        except PageNotAnInteger:
            p = paginator.page(1)
        except EmptyPage:
            p = paginator.page(paginator.num_pages)
        context = Context({"apps": p, 'username': username})
        return render_to_response('User/applist.html', context)


@login_required
def logs(request):

    username = request.user.username
    appname = request.GET['appname']
    if request.method == 'POST':
        kf = KForm(request.POST)
        if kf.is_valid():
            keyword = kf.cleaned_data['keyword']
            text = 'NO MATCH FOUNDED!!!'

            logobjs = LOG.objects.filter(behavior=appname).filter(source=keyword)

            if len(logobjs) is 0:
                return render_to_response('User/logs.html', {'info': text})
            else:
                context = Context({"logs": [Container(0, logobjs[0].source)], 'username': username, 'appname': appname})
                return render_to_response('User/logs.html', context)



    logobjs = LOG.objects.filter(behavior=appname)
    ip_list = []
    log_list = []
    i = 0
    for log in logobjs:
        if not list_has(ip_list, log.source):
            log_list.append(Container(i, log.source))
            ip_list.append(log.source)
            i += 1

    paginator = Paginator(log_list, 10)

    page = request.GET.get('page')
    try:
        p = paginator.page(page)
    except PageNotAnInteger:
        p = paginator.page(1)
    except EmptyPage:
        p = paginator.page(paginator.num_pages)
    context = Context({"logs": p, 'username': username, 'appname': appname})
    return render_to_response('User/logs.html', context)


@login_required
def timetable(request):

    username = request.user.username
    appname = request.GET['appname']
    ip = request.GET['ip']

    if request.method == 'POST':
        kf = KForm(request.POST)
        if kf.is_valid():
            keyword = kf.cleaned_data['keyword']
            text = 'NO MATCH FOUNDED!!!'

            mytime = keyword.split('-')
            t = time.strptime(mytime[0], "%m/%d/%Y %I:%M %p ")
            y, m, d, H, M = t[0:5]
            s_date = datetime.datetime(y, m, d, H, M)
            t = time.strptime(mytime[1], " %m/%d/%Y %I:%M %p")
            y, m, d, H, M = t[0:5]
            e_date = datetime.datetime(y, m, d, H, M)
            logobjs = LOG.objects.filter(behavior=appname).filter(source=ip).filter(timestamp__range=(s_date, e_date)).order_by('timestamp')

            if len(logobjs) is 0:
                return render_to_response('User/timetable.html', {'info': text})
            else:

                context = Context({"logs": logobjs, 'username': username, 'appname': appname, 'ip': ip, 'timerange': '('+keyword+')'})
                return render_to_response('User/timetable.html', context)

    logobjs = LOG.objects.filter(behavior=appname).filter(source=ip).order_by('timestamp').reverse()
    time_list = []
    log_list = []
    i = 0

    paginator = Paginator(logobjs, 10)

    page = request.GET.get('page')
    try:
        p = paginator.page(page)
    except PageNotAnInteger:
        p = paginator.page(1)
    except EmptyPage:
        p = paginator.page(paginator.num_pages)
    context = Context({"logs": p, 'username': username, 'appname': appname, 'ip': ip})
    return render_to_response('User/timetable.html', context)


@login_required
def logdetail(request):

    username = request.user.username
    id = request.GET['id']
    app = LOG.objects.get(id=id)
    context = Context({"app": app, 'username': username})
    return render_to_response('User/logdetail.html', context)


@login_required
def fingerprints(request):

    username = request.user.username
    context = Context({"fps": fppage(request), 'username': username})
    return render_to_response('User/fp.html', context)


@login_required
def fpsearch(request):

    username = request.user.username
    if request.method == 'POST':
        fpf = FPForm(request.POST)
        if fpf.is_valid():
            app_name = fpf.cleaned_data['appname']

            objs = FP.objects.filter(app_name__contains=app_name)
            if len(objs) == 0:
                text = 'NO MATCH FOUNDED!!!'
                return render_to_response('User/fpsearch.html', {'info': text})
            else:
                context = Context({"fps": objs, 'username': username})
                return render_to_response('User/fpsearch.html', context)
    return render_to_response('User/fpsearch.html', {'username': username})


@login_required
def fpedit(request, param):

    username = request.user.username
    if not request.user.is_superuser:
        context = Context({"fps": fppage(request), 'username': username})
        return render_to_response('User/fp.html', context)

    try:
        fp = FP.objects.get(id=param)
    except FP.DoesNotExist:
        context = Context({'username': username, 'id': 0})
        return render_to_response('User/fpedit.html', context)
    else:
        context = Context({"fp": fp, 'username': username, 'id': param})
        return render_to_response('User/fpedit.html', context)


@login_required
def save(request):

    username = request.user.username
    _id = request.GET['id']
    _appname = request.GET['name']
    _UA = request.GET['UA']
    _fp = request.GET['fp']

    if _id == '0':
        try:
            tem = FP.objects.get(app_name=_appname, fingerprint=_fp)
        except FP.DoesNotExist:

            if _UA == 'True':
                newfp = FP(app_name=_appname, UA=True, fingerprint=_fp)
            else:
                newfp = FP(app_name=_appname, UA=False, fingerprint=_fp)
            newfp.save()

            context = Context({"fps": fppage(request), 'username': username, 'info': 1})
            return render_to_response('User/fp.html', context)

        else:

            context = Context({"fps": fppage(request), 'username': username, 'info': 2})
            return render_to_response('User/fp.html', context)
    elif _id != '0':

        if _UA == 'True':
            newfp = FP(id=_id, app_name=_appname, UA=True, fingerprint=_fp)
        else:
            newfp = FP(id=_id, app_name=_appname, UA=False, fingerprint=_fp)
        newfp.save()
        context = Context({"fps": fppage(request), 'username': username, 'info': 3})
        return render_to_response('User/fp.html', context)


@login_required
def delete(request):

    username = request.user.username
    _id = request.GET['id']

    if id != '0':
        try:
            fp = FP.objects.get(id=_id)
        except FP.DoesNotExist:
            pass
        else:
            fp.delete()
            context = Context({"fps": fppage(request), 'username': username, 'info': 4})
            return render_to_response('User/fp.html', context)


@login_required
def stats(request):

    username = request.user.username
    stat = devicecal()
    app = appcal()[0]

    context = Context({'stat': stat, 'app': app, 'username': username})

    return render_to_response('User/stats.html', context)


@login_required
def device(request):

    username = request.user.username
    stat = devicecal()
    context = Context({'stat': stat, 'username': username})
    return render_to_response('User/device.html', context)


@login_required
def app(request):

    username = request.user.username
    device = devicecal()
    tem = appcal()
    app = tem[0]
    num = device.get('mobile')
    context = Context({'stat': app, 'username': username, 'num': num, 'appnum': tem[1]})
    return render_to_response('User/app.html', context)


@login_required
def about(request):
    return render_to_response('User/test/about.html')


###############################################################################


def list_has(list, key):
    if len(list) == 0:
        return False
    else:
        for i in list:
            if i == key:
                return True
            else:
                pass
        return False


def fppage(request):
    fp_list = FP.objects.all()
    paginator = Paginator(fp_list, 10)

    page = request.GET.get('page')
    try:
        fps = paginator.page(page)
    except PageNotAnInteger:
        fps = paginator.page(1)
    except EmptyPage:
        fps = paginator.page(paginator.num_pages)
    return fps


def devicecal():

    ipobjs = IP.objects.all()

    win = 0
    ubuntu = 0
    mac = 0
    mobile = 0

    for ip in ipobjs:
        if ip.WINDOWS:
            win += 1
        else:
            pass
        if ip.UBUNTU:
            ubuntu += 1
        else:
            pass
        if ip.MAC:
            mac += 1
        else:
            pass
        if ip.MOBILE:
            mobile += 1
        else:
            pass

    dic = {'win': win, 'ubuntu': ubuntu, 'mac': mac, 'mobile': mobile, 'total': IP.objects.latest('id').id}

    return dic


def appcal():

    fp_list = FP.objects.all()
    app_list = {}
    app_count = 0
    for fp in fp_list:
        if not list_has(app_list, fp.app_name):
            app_list.setdefault(fp.app_name, 0)
            app_count += 1
    ipobjs = IP.objects.all()
    for ip in ipobjs:
        if ip.MOBILE and ip.behavior is not None:
            apps = ip.behavior.split('/')
            for i in range(1, len(apps)):
                num = app_list.get(apps[i])
                app_list.update({apps[i]: num + 1})

    return [app_list, app_count]

##################################################################################


def speed(request):

    s = SPEED.objects.get(id=1)
    sp = s.speed

    j = json.dumps({'speed': sp})

    return HttpResponse(j)


def devicecal2(request):

    ipobjs = IP.objects.all()

    win = 0
    ubuntu = 0
    mac = 0
    mobile = 0

    for ip in ipobjs:
        if ip.WINDOWS:
            win += 1
        elif ip.UBUNTU:
            ubuntu += 1
        elif ip.MAC:
            mac += 1
        elif ip.MOBILE:
            mobile += 1
    dic = {'win': win, 'ubuntu': ubuntu, 'mac': mac, 'mobile': mobile, 'total': IP.objects.latest('id').id}
    j = json.dumps(dic)
    return HttpResponse(j)


def appcal2(request):

    fp_list = FP.objects.all()
    app_list = {}

    for fp in fp_list:
        if not list_has(app_list, fp.app_name):
            app_list.setdefault(fp.app_name, 0)
    ipobjs = IP.objects.all()
    for ip in ipobjs:
        if ip.MOBILE and ip.behavior is not None:
            apps = ip.behavior.split('/')
            for i in range(1, len(apps)):
                num = app_list.get(apps[i])
                app_list.update({apps[i]: num + 1})

    j = json.dumps(app_list)
    return HttpResponse(j)