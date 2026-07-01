from django.shortcuts import render
from django.http import HttpResponse
from django.views import generic

# Create your views here.


class HotUpdate(generic.View):

    def get(self,request):
        return render(request,'deploy/hot_update.html',{'messages':"ok"})