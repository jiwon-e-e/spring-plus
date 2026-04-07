# SPRING PLUS  

권지원 Spring Plus 주차 과제 제출합니다!  

## 프로젝트 소개

Spring Boot 기반의 Todo 관리 API 서버입니다. 기존 베이스 코드를 바탕으로 아키텍처를 개선하고, 오류를 해결하고, 기능을 추가했습니다.

Spring Security 와 JWT 를 연동하여 Role-based 접근 제어를 구현하여 안전한 인증 인가 시스템을 제공합니다.  
관계형 데이터 (RDS) 와 비정형 데이터 (S3) 를 분리하여 데이터를 보존합니다.  
Todo 생성 및 댓글 기능을 통해 협업 효율을 높이며, Manager 등록이 가능합니다. 

---  

### 기간
- 2026.03.31(화) ~ 2026.04.07(화)  

마일스톤을 포함한 프로젝트 과정을 블로그에 작성했습니다.  

https://kjw81024.tistory.com/78   
https://kjw81024.tistory.com/81  

---  

## 기술 스택 
### **Backend**
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JPA](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)

### **Database & Storage**
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![AWS RDS](https://img.shields.io/badge/AWS_RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white)
![AWS S3](https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)

### **DevOps & Infrastructure**
![AWS EC2](https://img.shields.io/badge/AWS_EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)



---  

## 프로젝트 구조   
``` 
org.example.expert
├── app
│   └── AdminAccessLoggingAspect  # level 11
├── client
│   └── WeatherClient             # todo 날씨 관련 외부 api 
├── config
│   ├── SecurityConfig            # level 2 , level 9 Spring Security 설정
│   ├── JwtFilter                 # JWT 인증 필터
│   ├── JwtUtil                   # JWT 생성/검증
│   ├── GlobalExceptionHandler   
│   ├── PersistenceConfig
│   ├── QuerydslConfig            # level 8, level 11 QueryDSL 설정
│   └── PasswordEncoder
├── domain
│   ├── auth
│   ├── user
│   ├── todo
│   ├── comment
│   ├── manager
│   └── common
└── ExpertApplication  
```

---  

## 주요 기능 

### 1. 인증 / 인가  
JWT 기반 로그인 인증  
Spring Security Filter를 통한 인증 처리  
사용자 권한 기반 접근 제어  
### 2. 사용자(User)  
회원가입 / 로그인  
사용자 정보 관리  
### 3. Todo  
Todo 생성 / 조회 / 수정 / 삭제  
사용자별 Todo 관리  
외부 api (weather client) 를 통한 날씨 자동 저장   
### 4. Comment  
Todo에 대한 댓글 기능  
댓글 작성자 정보 포함 조회  
### 5. QueryDSL 적용  
복잡한 조회 쿼리를 QueryDSL로 구현  
Fetch Join을 통한 N+1 문제 해결


---  

## 실행 방법

1. Clone repository  
   ```
   git clone https://github.com/사용자계정/저장소이름.git
   cd 저장소이름  
2. Build the project  
   ```
   ./gradlew clean bootJar  
3. EC2 접속  
    ```
    scp -i "your-key.pem" build/libs/*.jar ec2-user@your-ec2-ip:/home/ec2-user/  
    ssh -i "your-key.pem" ec2-user@your-ec2-ip  
4. 실행  
   ```
   java -jar *.jar


## readme 필수 첨부 내용
<img width="1600" height="550" alt="image" src="https://github.com/user-attachments/assets/e10e6372-c4f6-4ed2-a951-4060d1d683d7" />
EC2 구성   
<img width="1588" height="317" alt="스크린샷 2026-04-07 131928" src="https://github.com/user-attachments/assets/26568682-17ee-4786-b85d-78a9209a8953" />
EC2 인바운드 규칙  
<img width="1564" height="245" alt="스크린샷 2026-04-07 131943" src="https://github.com/user-attachments/assets/1657e377-8c11-49e7-8c1b-69cdd3bf705c" />
IAM 설정  
<img width="1577" height="680" alt="image" src="https://github.com/user-attachments/assets/fb0ac67b-d0fd-4277-b81e-715cbc97b658" />
RDS 구성  
<img width="1558" height="157" alt="스크린샷 2026-04-07 132004" src="https://github.com/user-attachments/assets/826b08f5-6f4b-4a1a-9cac-6045cbf091b5" />
RDS 보안그룹  
<img width="1438" height="94" alt="image" src="https://github.com/user-attachments/assets/bc4d8815-07a6-4a18-8ce9-af4418abdaa6" />
S3 구성  
<img width="906" height="386" alt="스크린샷 2026-04-07 131830" src="https://github.com/user-attachments/assets/3e509f1e-27ae-4aca-a0e8-b25376980f95" />
S3 버킷 정책   

---  

<img width="786" height="151" alt="image" src="https://github.com/user-attachments/assets/994d635b-d5aa-4d8f-bad5-c8aa57e313d7" />


쿼리 최적화 실행 결과 표  

---  
## 트러블 슈팅

https://kjw81024.tistory.com/79  
