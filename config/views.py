from django.http import JsonResponse

def test(request):
    return JsonResponse({"text": "hello world"},status=200)