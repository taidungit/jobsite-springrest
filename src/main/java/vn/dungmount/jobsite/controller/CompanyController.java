package vn.dungmount.jobsite.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.dungmount.jobsite.domain.Company;
import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.domain.dto.ResultPaginationDTO;
import vn.dungmount.jobsite.service.CompanyService;

@RestController
public class CompanyController {
    private final CompanyService companyService;
    
       public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany( @RequestBody @Valid Company company){
        Company taidung= this.companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(taidung);
        
    }
    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(@RequestParam("current") Optional<String> currentOptional,
        @RequestParam("pageSize") Optional<String> pageSizeOptional){
        String sCurrent=currentOptional.isPresent()?currentOptional.get():"";
        String sPageSize=pageSizeOptional.isPresent()?pageSizeOptional.get():"";
        int current=Integer.parseInt(sCurrent);
        int pageSize=Integer.parseInt(sPageSize);
         Pageable pageable = PageRequest.of(current-1,pageSize);   
        ResultPaginationDTO companies=this.companyService.getAllCompanies(pageable);

        return ResponseEntity.ok(companies);
    }
    @PutMapping("/companies")
    public ResponseEntity<Company>updateCompany(@RequestBody Company company){
        Company update=this.companyService.updateCompany(company);
        return ResponseEntity.ok(update);
    }
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void>deleteCompany(@PathVariable Long id){
        this.companyService.deleteCompany(id);
        return ResponseEntity.ok().build();
    }
}
