package masil.backend.modules.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.entity.MemberPreference;
import masil.backend.modules.member.enums.PreferenceCategory;
import masil.backend.modules.member.enums.Religion;
import masil.backend.modules.member.repository.MemberPreferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingScoreService {
    
    private final MemberPreferenceRepository memberPreferenceRepository;
    
    private static final double PRIORITY1_WEIGHT = 3.0;
    private static final double PRIORITY2_WEIGHT = 2.0;
    private static final double PRIORITY3_WEIGHT = 1.0;
    

    public Double calculateMatchingScore(Member femaleMember, Member maleMember) {
        MemberPreference preference = memberPreferenceRepository.findByMember(femaleMember)
                .orElse(null);
        
        if (preference == null) {
            log.warn("여성 유저 {}의 선호도 정보가 없습니다. 기본 점수 반환.", femaleMember.getId());
            return 50.0; // 선호도가 없으면 기본 점수
        }
        
        double totalScore = 0.0;
        double totalWeight = 0.0;
        
        PreferenceCategory priority1 = preference.getPriority1();
        PreferenceCategory priority2 = preference.getPriority2();
        PreferenceCategory priority3 = preference.getPriority3();
        
        // 키 매칭 
        if (isPriorityCategory(priority1, priority2, priority3, PreferenceCategory.HEIGHT)) {
            double heightScore = calculateHeightScore(preference, maleMember);
            double weight = getWeightForCategory(priority1, priority2, priority3, PreferenceCategory.HEIGHT);
            totalScore += heightScore * weight;
            totalWeight += weight;
        }
        
        // 종교 매칭 
        if (isPriorityCategory(priority1, priority2, priority3, PreferenceCategory.RELIGION)) {
            double religionScore = calculateReligionScore(preference, maleMember);
            double weight = getWeightForCategory(priority1, priority2, priority3, PreferenceCategory.RELIGION);
            totalScore += religionScore * weight;
            totalWeight += weight;
        }
        
        // 학벌 매칭 
        if (isPriorityCategory(priority1, priority2, priority3, PreferenceCategory.EDUCATION)) {
            double educationScore = calculateEducationScore(preference, maleMember);
            double weight = getWeightForCategory(priority1, priority2, priority3, PreferenceCategory.EDUCATION);
            totalScore += educationScore * weight;
            totalWeight += weight;
        }
        
        // 자산 매칭
        if (isPriorityCategory(priority1, priority2, priority3, PreferenceCategory.ASSET)) {
            double assetScore = calculateAssetScore(preference, maleMember);
            double weight = getWeightForCategory(priority1, priority2, priority3, PreferenceCategory.ASSET);
            totalScore += assetScore * weight;
            totalWeight += weight;
        }
        
        // 외모 스타일 매칭 
        if (isPriorityCategory(priority1, priority2, priority3, PreferenceCategory.APPEARANCE)) {
            double appearanceScore = calculateAppearanceScore(preference, maleMember);
            double weight = getWeightForCategory(priority1, priority2, priority3, PreferenceCategory.APPEARANCE);
            totalScore += appearanceScore * weight;
            totalWeight += weight;
        }
        
        // 직업 매칭
        if (isPriorityCategory(priority1, priority2, priority3, PreferenceCategory.JOB)) {
            double jobScore = calculateJobScore(preference, maleMember);
            double weight = getWeightForCategory(priority1, priority2, priority3, PreferenceCategory.JOB);
            totalScore += jobScore * weight;
            totalWeight += weight;
        }
        
        // 부모님 자산 매칭
        if (isPriorityCategory(priority1, priority2, priority3, PreferenceCategory.PARENT_ASSET)) {
            double parentAssetScore = calculateParentAssetScore(preference, maleMember);
            double weight = getWeightForCategory(priority1, priority2, priority3, PreferenceCategory.PARENT_ASSET);
            totalScore += parentAssetScore * weight;
            totalWeight += weight;
        }
        
        if (totalWeight == 0) {
            return 50.0; // 기본 점수
        }
        
        double finalScore = totalScore / totalWeight;
        return Math.min(100.0, Math.max(0.0, finalScore));
    }
    
    private boolean isPriorityCategory(PreferenceCategory p1, PreferenceCategory p2, 
                                       PreferenceCategory p3, PreferenceCategory target) {
        return target == p1 || target == p2 || target == p3;
    }
    
    private double getWeightForCategory(PreferenceCategory p1, PreferenceCategory p2, 
                                        PreferenceCategory p3, PreferenceCategory target) {
        if (target == p1) return PRIORITY1_WEIGHT;
        if (target == p2) return PRIORITY2_WEIGHT;
        if (target == p3) return PRIORITY3_WEIGHT;
        return 0.0;
    }
    
    private double calculateHeightScore(MemberPreference preference, Member maleMember) {
        if (preference.getPreferredHeightMin() == null || preference.getPreferredHeightMax() == null) {
            return 50.0;
        }
        
        Integer height = maleMember.getHeight();
        if (height == null) {
            return 0.0;
        }
        
        int min = preference.getPreferredHeightMin();
        int max = preference.getPreferredHeightMax();
        
        if (height >= min && height <= max) {
            return 100.0; // 범위 내면 만점
        } else {
            // 범위를 벗어나면 거리에 따라 감점
            int distance = Math.min(
                    Math.abs(height - min),
                    Math.abs(height - max)
            );
            // 10cm당 20점 감점, 최소 0점
            return Math.max(0.0, 100.0 - (distance * 2));
        }
    }
    
    private double calculateReligionScore(MemberPreference preference, Member maleMember) {
        if (preference.getAvoidReligionsBitmask() == null || preference.getAvoidReligionsBitmask() == 0) {
            return 50.0; // 기피 종교가 없으면 기본 점수
        }
        
        List<Religion> avoidReligions = Religion.fromBitmask(preference.getAvoidReligionsBitmask());
        if (avoidReligions.contains(maleMember.getReligion())) {
            return 0.0; // 기피 종교면 0점
        }
        
        return 100.0; // 기피 종교가 아니면 만점
    }
    
    private double calculateEducationScore(MemberPreference preference, Member maleMember) {
        if (preference.getPreferredEducationLevel() == null || maleMember.getEducation() == null) {
            return 50.0;
        }
        
        // EducationLevel enum의 ordinal로 비교 (간단한 예시)
        // 실제로는 더 정교한 로직 필요
        int preferredOrdinal = preference.getPreferredEducationLevel().ordinal();
        int memberOrdinal = maleMember.getEducation().ordinal();
        
        if (memberOrdinal >= preferredOrdinal) {
            return 100.0; // 선호 학벌 이상이면 만점
        } else {
            // 낮으면 차이만큼 감점
            int diff = preferredOrdinal - memberOrdinal;
            return Math.max(0.0, 100.0 - (diff * 20));
        }
    }
    
    private double calculateAssetScore(MemberPreference preference, Member maleMember) {
        if (preference.getPreferredAssetMin() == null || preference.getPreferredAssetMax() == null 
                || maleMember.getAsset() == null) {
            return 50.0;
        }
        
        // Asset enum을 숫자로 변환 (간단한 예시)
        // 실제로는 Asset enum의 실제 값으로 비교 필요
        // 임시로 ordinal 사용
        return 50.0; // TODO: 실제 자산 값으로 계산
    }
    
    private double calculateAppearanceScore(MemberPreference preference, Member maleMember) {
        if (preference.getPreferredAppearanceStyle() == null) {
            return 50.0;
        }
        
        // AppearanceStyle 매칭 로직
        // 현재 Member 엔티티에 AppearanceStyle 필드가 없으므로 임시 처리
        return 50.0; // TODO: 실제 외모 스타일 비교 로직 구현
    }
    
    private double calculateJobScore(MemberPreference preference, Member maleMember) {
        // preferredJobs와 avoidedJobs는 JSON 문자열로 저장되어 있음
        // 실제 구현 시 JSON 파싱 필요
        return 50.0; // TODO: 직업 매칭 로직 구현
    }
    
    private double calculateParentAssetScore(MemberPreference preference, Member maleMember) {
        if (preference.getParentAssetRequirement() == null) {
            return 50.0;
        }
        
        // ParentAssetLevel 매칭 로직
        return 50.0; // TODO: 부모님 자산 매칭 로직 구현
    }
}