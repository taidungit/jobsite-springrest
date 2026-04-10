package vn.dungmount.jobsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import vn.dungmount.jobsite.domain.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Long>,JpaSpecificationExecutor<Permission> {
    boolean existsByApiPathAndMethodAndModule(String apiPath, String method, String module);
    @Modifying
    @Query(value = "DELETE FROM permission_role WHERE permission_id = :id", nativeQuery = true)
    void deleteRelationByPermissionId(Long id);
}
