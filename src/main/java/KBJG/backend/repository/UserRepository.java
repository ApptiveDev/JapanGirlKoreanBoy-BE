package KBJG.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import KBJG.backend.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 이메일로 사용자 조회
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(String email);
    
    /**
     * OAuth Provider와 ProviderId로 사용자 조회
     */
    Optional<User> findByProviderAndProviderId(User.AuthProvider provider, String providerId);
    
    /**
     * 이메일과 Provider로 사용자 조회
     */
    Optional<User> findByEmailAndProvider(String email, User.AuthProvider provider);
}
