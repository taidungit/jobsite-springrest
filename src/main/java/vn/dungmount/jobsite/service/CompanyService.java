package vn.dungmount.jobsite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.dungmount.jobsite.domain.Company;
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
    public List<Company>getAllCompanies(){
      return this.companyRepository.findAll();
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
