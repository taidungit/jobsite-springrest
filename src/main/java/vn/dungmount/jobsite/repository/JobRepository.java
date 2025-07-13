package vn.dungmount.jobsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.dungmount.jobsite.domain.Job;

@Repository
public interface JobRepository extends JpaRepository<Job,Long>,JpaSpecificationExecutor<Job> {
    
}
