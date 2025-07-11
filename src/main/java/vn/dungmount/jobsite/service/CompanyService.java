package vn.dungmount.jobsite.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.dungmount.jobsite.domain.Company;
import vn.dungmount.jobsite.domain.dto.Meta;
import vn.dungmount.jobsite.domain.dto.ResUserDTO;
import vn.dungmount.jobsite.domain.dto.ResultPaginationDTO;
import vn.dungmount.jobsite.repository.CompanyRepository;

@Service
public class CompanyService {
        private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public Company getCompanyById(Long id){
      Optional<Company> optional=this.companyRepository.findById(id);
      if(optional.isPresent()){
        return optional.get();
      }
        return null;
    }
    public Company createCompany(Company company) {
      return this.companyRepository.save(company);
    }
    public ResultPaginationDTO getAllCompanies(Specification<Company> spec,Pageable page){
      Page<Company>pageCompany=this.companyRepository.findAll(spec,page);
      Meta mt=new Meta();
      ResultPaginationDTO rs=new ResultPaginationDTO();
        mt.setPage(page.getPageNumber()+1);
        mt.setPageSize(page.getPageSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());
        rs.setMeta(mt);

        rs.setResult(pageCompany.getContent());
        return rs;
    }
    public Company updateCompany(Company company){
      Company update=this.getCompanyById(company.getId());
      if(update!=null){
        update.setName(company.getName());
        update.setDescription(company.getDescription());
        update.setAddress(company.getAddress());
        update.setLogo(company.getLogo());
        this.companyRepository.save(update);
      }
      return update;
    }
    public void deleteCompany(Long id){
      this.companyRepository.deleteById(id);
    }

}
