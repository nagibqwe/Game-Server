from django.shortcuts import get_object_or_404, render
from django.http import HttpResponseRedirect,HttpResponse
from django.urls import reverse
from django.views import generic
from dwebsocket.decorators import accept_websocket,require_websocket
import time,json

from .models import *

# Create your views here.


class BaseIndexView(generic.ListView):
    template_name = 'assets/base_assets.html'
    # template_name = 'base.html'
    context_object_name = 'server_info_list'

    def get_queryset(self):
        return Assets.objects.order_by('buy_time')


class AssetsIndexView(generic.ListView):
    template_name = 'assets/assets_list.html'
    # template_name = 'base.html'
    context_object_name = 'server_info_list'

    def get_queryset(self):
        return Assets.objects.order_by('buy_time')

@accept_websocket
def test(request):
    print(request)
    if request.is_websocket():
        print(1111)
        while 1:
            time.sleep(1) ## 向前端发送时间
            dit = {
                'time':time.strftime('%Y.%m.%d %H:%M:%S',time.localtime(time.time()))
            }
            request.websocket.send(json.dumps(dit))
    else:
        return HttpResponse("111111")