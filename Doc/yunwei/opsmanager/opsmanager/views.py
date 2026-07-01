from django.views import generic

# Create your views here.

class IndexView(generic.ListView):
    template_name = 'base.html'
    context_object_name = 'server_info_list'

    def get_queryset(self):
        return