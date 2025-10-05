# AWS Parameter Store 설정 가이드

## 1. AWS CLI 설치 및 설정

```bash
# AWS CLI 설치 (Ubuntu/Debian)
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

# AWS CLI 설정
aws configure
```

## 2. Parameter Store에 시크릿 저장

```bash
# JWT Secret 저장
aws ssm put-parameter \
    --name "/myapp/config/jwt-secret" \
    --value "your-super-secret-jwt-key-at-least-256-bits-long" \
    --type "SecureString" \
    --region us-east-1

# Google Client ID 저장
aws ssm put-parameter \
    --name "/myapp/config/google-client-id" \
    --value "your-google-client-id" \
    --type "SecureString" \
    --region us-east-1

# Google Client Secret 저장
aws ssm put-parameter \
    --name "/myapp/config/google-client-secret" \
    --value "your-google-client-secret" \
    --type "SecureString" \
    --region us-east-1

# 데이터베이스 URL 저장
aws ssm put-parameter \
    --name "/myapp/config/db-url" \
    --value "jdbc:mysql://your-rds-endpoint:3306/your-database" \
    --type "SecureString" \
    --region us-east-1

# 데이터베이스 사용자명 저장
aws ssm put-parameter \
    --name "/myapp/config/db-username" \
    --value "your-database-username" \
    --type "SecureString" \
    --region us-east-1

# 데이터베이스 비밀번호 저장
aws ssm put-parameter \
    --name "/myapp/config/db-password" \
    --value "your-database-password" \
    --type "SecureString" \
    --region us-east-1
```

## 3. EC2 인스턴스에 IAM 역할 설정

EC2 인스턴스에 다음 정책이 포함된 IAM 역할을 연결해야 합니다:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ssm:GetParameter",
                "ssm:GetParameters",
                "ssm:GetParametersByPath"
            ],
            "Resource": [
                "arn:aws:ssm:*:*:parameter/myapp/config/*"
            ]
        }
    ]
}
```

## 4. Elastic Beanstalk 환경 변수 설정

Elastic Beanstalk를 사용하는 경우:

```bash
# EB CLI를 사용하여 환경 변수 설정
eb setenv SPRING_PROFILES_ACTIVE=aws
eb setenv AWS_REGION=us-east-1
```

## 5. 애플리케이션 실행

```bash
# AWS 프로파일로 실행
java -jar -Dspring.profiles.active=aws your-app.jar

# 또는 환경변수로 설정
export SPRING_PROFILES_ACTIVE=aws
export AWS_REGION=us-east-1
java -jar your-app.jar
```

## 6. Google OAuth2 설정

1. [Google Cloud Console](https://console.cloud.google.com/)에 접속
2. 새 프로젝트 생성 또는 기존 프로젝트 선택
3. "API 및 서비스" > "사용자 인증 정보" 이동
4. "사용자 인증 정보 만들기" > "OAuth 클라이언트 ID" 선택
5. 애플리케이션 유형: "웹 애플리케이션"
6. 승인된 리디렉션 URI 추가:
   - 개발: `http://localhost:8080/api/login/oauth2/code/google`
   - 프로덕션: `https://your-domain.com/api/login/oauth2/code/google`

## 7. 보안 고려사항

- JWT Secret은 최소 256비트 이상의 랜덤 문자열 사용
- Parameter Store의 SecureString 타입 사용
- IAM 역할을 통한 최소 권한 원칙 적용
- HTTPS 사용 (프로덕션 환경)
- 정기적인 시크릿 로테이션
