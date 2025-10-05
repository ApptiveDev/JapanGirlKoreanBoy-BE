# Spring Boot 인증 시스템

Spring Boot 3.2 + Java 17 기반의 보안 인증 시스템으로, 로컬 로그인과 Google OAuth2 로그인을 모두 지원합니다.

## 📝 주요기능

- **로컬 인증**: 이메일/비밀번호 회원가입 및 로그인
- **Google OAuth2**: Google 계정으로 소셜 로그인
- **JWT 토큰**: Access Token과 Refresh Token을 통한 보안 인증
- **AWS 통합**: Parameter Store를 통한 시크릿 관리
- **PKCE 지원**: OAuth2 보안 강화

## 🔨 기술스택
<!-- 
(백엔드, 프론트, 협업에 사용한 툴, 라이브러리, 프레임워크)

기술스택 배지 추가하는 방법 
1. https://simpleicons.org/ 에서 기술스택명 검색
2. 기술스택의 로고, 컬러 HEX 코드를 아래와 같이 입력
  - https://img.shields.io/badge/<표시될 이름>-<컬러 HEX>?style=for-the-badge&logo=<로고명>
3. 해당 URL로 마크다운 이미지 첨부
  - ![이미지명](URL) 형식
-->

(백엔드, 프론트, 협업에 사용한 툴, 라이브러리, 프레임워크)

![spring](https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![maven](https://img.shields.io/badge/maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![h2](https://img.shields.io/badge/h2-1E88E5?style=for-the-badge&logo=h2&logoColor=white)

![jwt](https://img.shields.io/badge/jwt-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![oauth2](https://img.shields.io/badge/oauth2-EB5424?style=for-the-badge&logo=auth0&logoColor=white)
![aws](https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white)
![google](https://img.shields.io/badge/google_oauth-4285F4?style=for-the-badge&logo=google&logoColor=white)

![intellij](https://img.shields.io/badge/intellij_idea-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![github](https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white)

| 스택 | 설명 | 용도 |
|-----|-----|-----|
| Spring Boot 3.2 | Java 기반 웹 프레임워크 | 백엔드 API 서버 |
| Spring Security | 보안 프레임워크 | 인증 및 권한 관리 |
| Spring Data JPA | 데이터 액세스 레이어 | 데이터베이스 연동 |
| JWT (jjwt) | JSON Web Token | 토큰 기반 인증 |
| OAuth2 Client | OAuth2 클라이언트 | Google 소셜 로그인 |
| H2 Database | 인메모리 데이터베이스 | 개발/테스트 환경 |
| AWS Parameter Store | 시크릿 관리 서비스 | 민감한 정보 저장 |
| BCrypt | 비밀번호 해싱 | 비밀번호 암호화 |

## 🖼️ API 문서 및 테스트

### 📚 Swagger UI
- **URL**: `http://localhost:8080/api/swagger-ui.html`
- **기능**: 
  - 모든 API 엔드포인트 문서화
  - 실시간 API 테스트 가능
  - 요청/응답 예시 제공
  - JWT 인증 테스트 지원

### 🔗 주요 엔드포인트

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|----------|
| POST | `/api/auth/register` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 | ❌ |
| POST | `/api/auth/refresh` | 토큰 갱신 | ❌ |
| GET | `/api/auth/google` | Google OAuth2 로그인 | ❌ |
| GET | `/api/auth/oauth2/success` | OAuth2 성공 후 리다이렉트 | ❌ |
| GET | `/api/auth/me` | 현재 사용자 정보 | ✅ |
| POST | `/api/auth/logout` | 로그아웃 | ✅ |

### 요청/응답 예시

#### 회원가입
```json
POST /api/auth/register
{
  "email": "user@example.com",
  "password": "password123",
  "name": "홍길동"
}

Response:
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900000,
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "홍길동",
    "role": "USER"
  }
}
```

#### 로그인
```json
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password123"
}

Response: (회원가입과 동일한 형태)
```

#### 토큰 갱신
```json
POST /api/auth/refresh
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}

Response: (새로운 accessToken과 refreshToken)
```

## 🛠 설치방법

### 💻 Backend

1. **프로젝트 클론**
```bash
git clone <repository-url>
cd Project-BE
```

2. **환경 변수 설정**
```bash
# env.example 파일을 참고하여 환경 변수 설정
cp env.example .env
```

3. **Google OAuth2 설정**
- [Google Cloud Console](https://console.cloud.google.com/)에서 OAuth2 클라이언트 생성
- `env.example`의 `GOOGLE_CLIENT_ID`와 `GOOGLE_CLIENT_SECRET` 설정

4. **애플리케이션 실행**
```bash
# Maven으로 빌드
mvn clean package

# 애플리케이션 실행
java -jar target/backend-0.0.1-SNAPSHOT.jar

# 또는 Maven으로 직접 실행
mvn spring-boot:run
```

5. **API 문서 확인 (Swagger UI)**
- 브라우저에서 `http://localhost:8080/api/swagger-ui.html` 접속
- 모든 API 엔드포인트를 확인하고 테스트할 수 있습니다
- "Try it out" 버튼을 클릭하여 실제 API 호출 테스트 가능

6. **H2 콘솔 접속**
- 브라우저에서 `http://localhost:8080/api/h2-console` 접속
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### AWS 배포

1. **AWS Parameter Store 설정**
```bash
# aws-parameter-store-setup.md 파일 참고
aws ssm put-parameter --name "/myapp/config/jwt-secret" --value "your-secret" --type "SecureString"
```

2. **AWS 프로파일로 실행**
```bash
java -jar -Dspring.profiles.active=aws your-app.jar
```

## 🔐 보안 고려사항

- JWT Secret은 최소 256비트 이상의 랜덤 문자열 사용
- AWS Parameter Store의 SecureString 타입으로 시크릿 저장
- HTTPS 사용 (프로덕션 환경)
- 정기적인 시크릿 로테이션
- CORS 설정으로 허용된 도메인만 접근 가능

## 🤝 개발협업
 ### 🌲 Branch 
```
main ------- backend/<이름>/(<이슈번호>-)<작업명>    (백엔드 작업)
     \------ frontend/<이름>/(<이슈번호>-)<작업명>   (프론트 작업)

ex) backend/wonseok/#10-add-animation
ex) frontend/wonseok/fix-login-not-allowed   (이슈가 없으면)
```
브랜치 관리 전략은 `main`과 개인 브랜치만이 존재하는 간단한 Github Flow를 따릅니다.
- `main` 브랜치는 항상 작동 가능한 안정된 상태여야 한다.
  - 직접 커밋하지 않으며, Pull Request만으로 변경한다.
- 개인 브랜치에서 작업을 진행한다.
- 브랜치명은 작업 내용과 직군이 구체적으로 드러나도록 한다.
  - 브랜치명에 `backend`, `frontend`를 구분한다.
  - 띄어쓰기는 하이픈(`-`)으로 구분한다.
  - 브랜치명은 전부 소문자를 사용한다.

프로젝트에 CI/CD를 구성하는 등 규모가 커지면 `develop` 브랜치를 추가하거나 `git flow`로 전환할 수 있습니다. 

 ### 🍪 Pull Request
```
main    ---●---●---●---------● abc (Squash Merge)
                \           /
개인브랜치          a---b---c   ('abc' 합쳐진 하나의 커밋으로 병합)

PR 제목: [Backend/Frontend] <이슈번호> <작업명>
ex) [Backend] #10 프로필 화면에서 로그인 불가하던 문제 해결
ex) [Backend] 프로필 화면에서 로그인 불가하던 문제 해결     (이슈가 없으면)
```
`main` 브랜치의 커밋은 Pull Request 단위로 쌓으며 이를 위해 **Squash Merge**를 원칙으로 합니다. **Squash Merge**는 브랜치가 병합될 때 커밋들이 PR 제목으로 합쳐지게 됩니다. 커밋은 개인마다 기준이 조금씩 다른 반면, PR/브랜치는 이슈 단위로 생성하므로 일관된 기준으로 커밋을 쌓을 수 있어 히스토리 추적을 용이하게 합니다.
- 커밋 제목은 **PR 제목**으로 한다.
    - Backend/Frontend를 구분한다.
    - 작업 내용을 구체적으로 드러나게 적는다.
- 커밋 내용은 **PR 내용**으로 한다.
    - 브랜치에서의 변경점을 상세히 적는다.
- Pull Request는 작은 작업 단위(200줄 이내 권장)로 한다.

 ## 🛠 설치방법
(다른 개발자가 이 프로젝트를 테스트해볼 수 있도록 프론트, 백엔드 설치/실행 절차 안내)

### 💻 Frontend

### 💻 Backend

 ## 🧑‍💻 팀원
| <img width="100" src="https://github.com/cotidie.png"> | <img width="100" src="https://github.com/github.png"> | 
|:----------------------:|:----------------------:|
| [장원석](https://github.com/cotidie) | [팀원](https://github.com/cotidie) |
| 💻 Android | 💻 역할 |
| 15기 | 기수 |

 
