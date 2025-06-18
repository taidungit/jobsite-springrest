package vn.dungmount.jobsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.dungmount.jobsite.domain.Company;
import vn.dungmount.jobsite.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String Email);

}
