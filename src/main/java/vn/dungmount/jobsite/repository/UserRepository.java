package vn.dungmount.jobsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.dungmount.jobsite.domain.Company;
import vn.dungmount.jobsite.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User> {
    User findByEmail(String Email);
    boolean existsByEmail(String Email);
    User findByRefreshTokenAndEmail(String token,String email);
    List<User>findAllByCompany(Company com);

}
