# Swagger UI 사용 가이드

## 🚀 Swagger UI 접속

애플리케이션 실행 후 브라우저에서 다음 URL에 접속하세요:
```
http://localhost:8080/api/swagger-ui.html
```

## 📋 API 테스트 방법

### 1. 회원가입 테스트

1. **POST /api/auth/register** 엔드포인트 찾기
2. **"Try it out"** 버튼 클릭
3. **Request body**에 다음 JSON 입력:
```json
{
  "email": "test@example.com",
  "password": "password123",
  "name": "테스트 사용자"
}
```
4. **Execute** 버튼 클릭
5. **Response**에서 `accessToken`과 `refreshToken` 복사

### 2. 로그인 테스트

1. **POST /api/auth/login** 엔드포인트 찾기
2. **"Try it out"** 버튼 클릭
3. **Request body**에 다음 JSON 입력:
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```
4. **Execute** 버튼 클릭
5. **Response**에서 새로운 토큰 확인

### 3. JWT 인증이 필요한 API 테스트

1. **"Authorize"** 버튼 클릭 (페이지 상단)
2. **Bearer Token** 필드에 `accessToken` 입력 (앞에 "Bearer " 제외)
3. **Authorize** 버튼 클릭
4. 이제 인증이 필요한 API들 테스트 가능

### 4. 현재 사용자 정보 조회 테스트

1. **GET /api/auth/me** 엔드포인트 찾기
2. **"Try it out"** 버튼 클릭
3. **Execute** 버튼 클릭
4. 현재 로그인한 사용자 정보 확인

### 5. 토큰 갱신 테스트

1. **POST /api/auth/refresh** 엔드포인트 찾기
2. **"Try it out"** 버튼 클릭
3. **Request body**에 다음 JSON 입력:
```json
{
  "refreshToken": "여기에_refreshToken_입력"
}
```
4. **Execute** 버튼 클릭
5. 새로운 토큰들 확인

## 🔐 JWT 토큰 사용법

### Authorization 헤더 설정
인증이 필요한 API를 테스트할 때:
1. **Authorize** 버튼 클릭
2. **Bearer Token** 필드에 토큰 입력 (예: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`)
3. **Authorize** 클릭

### 토큰 만료 시간
- **Access Token**: 15분 (900초)
- **Refresh Token**: 7일 (604800초)

## 🌐 Google OAuth2 테스트

### 브라우저에서 직접 테스트
1. 브라우저에서 `http://localhost:8080/api/auth/google` 접속
2. Google 로그인 페이지로 리다이렉트
3. Google 계정으로 로그인
4. 성공 시 토큰과 함께 리다이렉트

### 주의사항
- Google OAuth2를 사용하려면 `application.yml`에서 실제 Google Client ID와 Secret을 설정해야 합니다
- Google Cloud Console에서 OAuth2 클라이언트를 생성하고 리다이렉트 URI를 설정해야 합니다

## 📊 응답 코드 설명

| 코드 | 의미 | 설명 |
|------|------|------|
| 200 | OK | 요청 성공 |
| 400 | Bad Request | 잘못된 요청 데이터 |
| 401 | Unauthorized | 인증 실패 또는 유효하지 않은 토큰 |
| 409 | Conflict | 이미 존재하는 이메일 (회원가입 시) |
| 302 | Found | 리다이렉트 (Google OAuth2) |

## 🛠 문제 해결

### 토큰 관련 오류
- **401 Unauthorized**: 토큰이 만료되었거나 잘못된 토큰
- **해결방법**: 토큰 갱신 API 사용 또는 다시 로그인

### Google OAuth2 오류
- **redirect_uri_mismatch**: Google Cloud Console에서 리다이렉트 URI 설정 확인
- **invalid_client**: Client ID/Secret 확인

### CORS 오류
- 개발 환경에서는 CORS가 허용되어 있음
- 프로덕션 환경에서는 도메인별 CORS 설정 필요

## 💡 팁

1. **토큰 저장**: 로그인 성공 후 받은 토큰을 안전하게 저장하세요
2. **토큰 갱신**: Access Token이 만료되기 전에 Refresh Token으로 갱신하세요
3. **로그아웃**: 클라이언트에서 토큰을 삭제하는 것만으로도 로그아웃됩니다
4. **API 문서**: Swagger UI에서 각 API의 상세한 설명과 예시를 확인하세요
