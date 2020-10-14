from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from uuid import uuid4
from datetime import datetime
import pytz
import json
from collections import defaultdict

from .models import User, Friend


POKE_QUEUE = defaultdict(list)


def _nowlocal():
    dt_naive = datetime.now()
    tz = pytz.timezone('America/Denver') # Use timezone metadata
    return tz.localize(dt_naive)


def _must_be_POST(f):
    def inner(request, *args, **kwargs):
        if request.method == 'POST':
            return f(request, *args, **kwargs)
        else:
            return HttpResponse('requires POST')
    return inner


@csrf_exempt
@_must_be_POST
def do_poke_inbound(request):
    user = request.POST['user']
    target = request.POST['target']
    payload = request.POST['payload']
    # Confirm user exists and user is friends with target
    if User.objects.filter(uuid=user).exists() and Friend.objects.filter(user_uuid=user, friend_uuid=target).exists():
        POKE_QUEUE[target].append([user, payload])
        print(POKE_QUEUE)
        friend_record = Friend.objects.get(user_uuid=user, friend_uuid=target)
        friend_record.total_pokes += 1
        friend_record.save() # commit row change to db
        return HttpResponse('success')
    else:
        return HttpResponse('')


@csrf_exempt
@_must_be_POST
def register(request):
    name = request.POST['name']
    uuid = str(uuid4()) # generate uuid for registration
    dt_aware = _nowlocal()
    User.objects.create(uuid=uuid, name=name, reg_date=dt_aware) # commit to database
    return HttpResponse(uuid)


@csrf_exempt
@_must_be_POST
def poll(request):
    user = request.POST['user']
    if User.objects.filter(uuid=user).exists():
        dat = {
                'pokes': POKE_QUEUE.pop(user) if user in POKE_QUEUE.keys() else [],
                }
        return HttpResponse(json.dumps(dat))

    else:
        return HttpResponse('')


@csrf_exempt
@_must_be_POST
def update(request):
    user = request.POST['user']
    if User.objects.filter(uuid=user).exists():
        dat = {
                'name': User.objects.filter(uuid=user).values('name')[0]['name'], # TODO see if there is a nicer way that this
                'friends': [v['friend_uuid'] for v in Friend.objects.filter(user_uuid=user).values('friend_uuid')],
                }
        return HttpResponse(json.dumps(dat))

    else:
        return HttpResponse('')


@csrf_exempt
@_must_be_POST
def add_friend(request):
    user = request.POST['user']
    target = request.POST['target']
    if Friend.objects.filter(user_uuid=user,friend_uuid=target).exists(): #already friends case
        return HttpResponse("Already friends :P")
    else:
        if User.objects.filter(uuid=user).exists() and User.objects.filter(uuid=target).exists():
            dt_aware = _nowlocal() #new friends case
            Friend.objects.create(user_uuid=user, friend_uuid=target, added_date=dt_aware, total_pokes=0)
            Friend.objects.create(user_uuid=target, friend_uuid=user, added_date=dt_aware, total_pokes=0)
            return HttpResponse("success")
        else:
            return HttpResponse("invalid user")

@csrf_exempt
@_must_be_POST
def delete_friend(request):
    user = request.POST['user']
    target = request.POST['target']
    if Friend.objects.filter(user_uuid=user,friend_uuid=target).exists(): #existing friends case
        Friend.objects.filter(user_uuid=user, friend_uuid=target).delete() #dt aware and pokes included?
        Friend.objects.filter(user_uuid=target, friend_uuid=user).delete()
        return HttpResponse("success")
    else:
        return HttpResponse("this friend doesn't exist")

