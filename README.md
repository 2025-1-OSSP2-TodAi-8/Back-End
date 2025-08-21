
# TodAi Backend

> **TodAi**는 사용자가 음성으로 감정을 기록하면 인공지능 모델이 음성과 텍스트 기반으로 분석한 결과를 종합하여, 사용자가 자신의 감정을 돌아보고 관리할 수 있도록 돕는 감정 분석 기반 일기 서비스 프로젝트입니다. 
본 프로젝트는 텍스트와 음성을 결합한 멀티모달 감정 분석 모델을 구현하여 정밀한 감정 판단을 가능하게 하고, 개인 맞춤형 정서 피드백 서비스로 확장될 수 있는 기반을 마련합니다. 
> 본 레포지토리는 TodAi 서비스의 **백엔드(Spring Boot)** 서버를 위한 코드입니다.

---
## 백엔드 개발 팀원
- 동국대학교 컴퓨터공학전공 21학번 이재혁
- 동국대학교 컴퓨터공학전공 23학번 이채원

---


## Tech Stack
- **Language:** Java 17  
- **Framework:** Spring Boot  
- **Database:** PostgreSQL  
- **Deployment:** CloudType  
- **AI Server:** Django (음성 감정 분석 전용)  

<div>
  <h2>기술스택</h2>
 <img src="https://img.shields.io/badge/django-092E20?style=for-the-badge&logo=django&logoColor=white">
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"/> 
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> 
<img src="https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white"/>
<img src="https://img.shields.io/badge/cloudtype-000000?style=for-the-badge&logo=cloud&logoColor=white"/>

</div>


---

## Architecture

프로젝트 업데이트 전에는 **Django**로 전체 백엔드를 구성했으나, 현재는 **Spring Boot**와 **Django** 백엔드를 구성하여 각각의 역할을 분리하여 사용합니다.

- **Django 서버** → 녹음한 음성 파일을 분석하고 감정을 추출  
- **Spring Boot 서버** → 사용자 인증, 일기/프로젝트/정원 등 주요 기능 담당  

**전환 후 향상된 점**
- 모든 API 응답에 대해 **공통 Response 포맷** 적용 → 프론트엔드에서 일관성 있는 데이터 처리 가능  
- **HTTP 상태 코드와 메시지**가 명확히 전달되어 가독성 및 유지보수성 향상  
- 모든 엔티티 기본 키를 **UUID**로 설계 → 보안성 강화 (순차적 ID 추측 공격 방지)  

---

##  Project Structure
          
```bash          
src
 └── main
     ├── java/com/todai/BE
     │    ├── common       # 공통 응답, 예외 처리
     │    ├── controller   # API 컨트롤러
     │    ├── dto          # 요청/응답 DTO
     │    ├── entity       # JPA 엔티티
     │    ├── global       # 시큐리티, JWT 설정 파일
     │    ├── repository   # JPA 리포지토리
     │    ├── security     # UserDetails 구현체
     │    └── service      # 서비스 로직
     │
     └── TodaiApplication  
     
 └── resources
     └── application.yml   # DB, JWT
 




# 🎯 Commit Rules
### 🎨 Style: 코드 형식 수정, 기능 변경이 없는 경우
### 🔥 Remove: 코드 파일 제거
### 🐛 Fix: 버그 수정
### ✨ Feat: 새로운 기능 추가
### 📝 Docs: 문서 수정
### 💄 Design: 디자인 변경
### ✅ Test: 테스트 코드
### ♻️ Refactor: 코드 구조 재구성
### 🔧 Chore: 빌드, 패키지 매니저 수정 등 기타 작업
### 🚚 Rename: 리소스 이동, 이름 변경
