package vn.dungmount.jobsite.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.dungmount.jobsite.domain.Job;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.domain.response.job.ResCreateJobDTO;
import vn.dungmount.jobsite.domain.response.job.ResUpdateJobDTO;
import vn.dungmount.jobsite.service.JobService;
import vn.dungmount.jobsite.util.annotation.ApiMessage;
import vn.dungmount.jobsite.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    @PostMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job){
        ResCreateJobDTO currentJob=this.jobService.createJob(job);
      
        return ResponseEntity.status(HttpStatus.CREATED).body(currentJob);
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job)throws IdInvalidException{
        Optional<Job> currentJob=this.jobService.fetchJobById(job.getId());
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Khong tim thay job ");
        }
        return ResponseEntity.ok(this.jobService.updateJob(job));
        
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> deleteJob( @PathVariable Long id)throws IdInvalidException{
        Optional<Job> currentJob=this.jobService.fetchJobById(id);
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Khong tim thay job ");
        }
        this.jobService.deleteJobById(id);
        return ResponseEntity.ok().body(null);
        
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get a job by id")
    public ResponseEntity<Job> getJobById( @PathVariable Long id)throws IdInvalidException{
        Optional<Job> currentJob=this.jobService.fetchJobById(id);
        if(!currentJob.isPresent()){
            throw new IdInvalidException("Khong tim thay job ");
        }
        return ResponseEntity.ok(currentJob.get());
        
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec, Pageable pageable){
        ResultPaginationDTO jobs=this.jobService.getAllJobs(spec,pageable);
        return ResponseEntity.ok(jobs);
    }
    
}
