from django.urls import path,include
from rest_framework import routers
from . import views

router = routers.DefaultRouter()
router.register(r'users',views.UserViewSet)
router.register(r'groups',views.GroupViewSet)
router.register(r'assets_list',views.AssetsViewSet)
router.register(r'project',views.ProjectViewSet)
router.register(r'status',views.StatusViewSet)
router.register(r'machine',views.MachineViewSet)
router.register(r'region',views.RegionViewSet)

urlpatterns = [
    path(r"select_server",views.select_server),
    path(r'excuse_cmd',views.excuse_cmd),
    path(r'',include(router.urls)),
    path(r'api-auth/',include('rest_framework.urls',namespace='rest_framework'))
]