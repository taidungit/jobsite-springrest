package vn.dungmount.jobsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.dungmount.jobsite.domain.Role;

public interface RoleRepository extends JpaRepository<Role,Long>,JpaSpecificationExecutor<Role>{
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, long id);
}
