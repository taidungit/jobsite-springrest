package vn.dungmount.jobsite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.dungmount.jobsite.domain.Company;
import vn.dungmount.jobsite.domain.User;

public interface CompanyRepository extends JpaRepository<Company,Long>,JpaSpecificationExecutor<Company> {
}
