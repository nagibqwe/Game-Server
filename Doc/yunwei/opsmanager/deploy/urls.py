from django.urls import path
from . import views

urlpatterns = [
    path('hot_update/', views.HotUpdate.as_view(),name='hot_update'),
]