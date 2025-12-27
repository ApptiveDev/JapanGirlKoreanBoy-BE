package masil.backend.modules.adminMember.repository;

import masil.backend.modules.adminMember.entity.AdminMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminMemberRepository extends JpaRepository<AdminMember, Long> {
    Optional<AdminMember> findByAdminId(String adminId);
}
