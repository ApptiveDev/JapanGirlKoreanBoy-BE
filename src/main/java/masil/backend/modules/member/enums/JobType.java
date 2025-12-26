package masil.backend.modules.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobType {
    DOCTOR("의사"),
    PHARMACIST("약사"),
    NURSE("간호사"),
    TEACHER("교사"),
    PROFESSOR("교수"),
    LAWYER("변호사"),
    ACCOUNTANT("회계사"),
    ENGINEER("엔지니어"),
    DEVELOPER("개발자"),
    DESIGNER("디자이너"),
    CIVIL_SERVANT("공무원"),
    POLICE("경찰"),
    FIREFIGHTER("소방관"),
    MILITARY("군인"),
    ENTREPRENEUR("사업가"),
    CEO("경영인"),
    FINANCE("금융인"),
    RESEARCHER("연구원"),
    ARTIST("예술가"),
    ATHLETE("운동선수"),
    FREELANCER("프리랜서"),
    STUDENT("학생"),
    UNEMPLOYED("무직"),
    ANY("상관없음"),
    OTHER("기타");

    private final String description;
}
