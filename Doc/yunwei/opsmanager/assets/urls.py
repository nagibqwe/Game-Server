from django.contrib import admin
from django.urls import path,include
from . import views


urlpatterns = [
    path('base_assets_list/',views.BaseIndexView.as_view(),name='base_assets'),
    path('assets_list/',views.AssetsIndexView.as_view(),name='assets_list'),
    path('test/',views.test,name='test'),
]