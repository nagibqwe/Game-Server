from django.contrib.auth.models import User,Group
from assets.models import *
from rest_framework.serializers import ModelSerializer,PrimaryKeyRelatedField
from rest_framework import serializers

class UserSerializer(ModelSerializer):
    class Meta:
        model = User
        fields = ("url",'username','email','groups')

class GroupSerializer(ModelSerializer):
    class Meta:
        model = Group
        fields = ('url','name')


class ChoicesField(serializers.Field):
    def __init__(self, choices, **kwargs):
        self._choices = choices
        super(ChoicesField, self).__init__(**kwargs)

    def to_representation(self, obj):
        return self._choices[obj]

    def to_internal_value(self, data):
        return getattr(self._choices, data)

class AssetSerializer(ModelSerializer):
    machineroom = PrimaryKeyRelatedField(many=False,queryset=MachineRoom.objects.all())
    # assets_type = ChoicesField(choices=Assets.assets_type_choices)
    # projects = PrimaryKeyRelatedField(many=False,queryset=ProjectAsset.objects.all())
    # status = PrimaryKeyRelatedField(many=False,queryset=AssetStatus.objects.all())
    def to_representation(self, instance):
        ret = super(AssetSerializer,self).to_representation(instance)
        ret["machineroom"] = instance.machineroom.machine_name
        ret['status'] = instance.status.status_name
        ret['project'] = instance.project.project_name
        return ret

    class Meta:
        model = Assets
        fields = '__all__'

class MachineSerializer(ModelSerializer):
    class Meta:
        model = MachineRoom
        fields = '__all__'

class ProjectSerializer(ModelSerializer):
    class Meta:
        model = ProjectAsset
        fields = '__all__'

class StatusSerializer(ModelSerializer):
    class Meta:
        model = AssetStatus
        fields = '__all__'

class RegionSerializer(ModelSerializer):
    class Meta:
        model = Region
        fields = '__all__'