from django.urls import path
from . import views
from .views import PeopleSignupView  
from .views import LogoutView #추기
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView 
from .views import CustomTokenObtainPairView

urlpatterns=[
    path('',views.getPeopleInfo), #마이페이지
    path('update/email', views.update_email),
    path('delete/connection',views.disconnect_sharing),
    path('update/showrange', views.update_showrange), #공개 범위 변경
    path('update/password', views.update_password),

    path('sharing/accept', views.accept_sharing_request), #연동 요청 수락

    path('signup', PeopleSignupView.as_view(), name='signup'), #회원가입
    path('signin', CustomTokenObtainPairView.as_view(), name='token_obtain_pair'), #로그인
    path('signin/refresh', TokenRefreshView.as_view(), name='token_refresh'), #리프레시 토큰 요청
    path('logout', LogoutView.as_view(), name='logout' ), #로그아웃

    path('search', views.search_user_by_id), #아이디 검색
    path('sharing/request', views.handle_sharing_request), #연동 요청 보내기

    
    path('share/month',views.emotions_month_for_protector), 
    path('share/marked/month',views.get_marked_month_for_protector), 
    path('share/marked/year', views.get_marked_year_for_protector),
    path('share/day',views.emotions_day_for_protector)
] 
