package vn.dungmount.jobsite.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.dungmount.jobsite.domain.Company;

public interface CompanyRepository extends JpaRepository<Company,Long>,JpaSpecificationExecutor<Company> {
}
