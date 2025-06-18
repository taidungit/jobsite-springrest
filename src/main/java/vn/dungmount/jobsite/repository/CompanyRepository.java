package vn.dungmount.jobsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.dungmount.jobsite.domain.Company;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    
}
