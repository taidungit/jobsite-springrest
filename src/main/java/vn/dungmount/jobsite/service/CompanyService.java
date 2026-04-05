package vn.dungmount.jobsite.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.dungmount.jobsite.domain.Company;
import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.repository.CompanyRepository;
import vn.dungmount.jobsite.repository.UserRepository;

@Service
public class CompanyService {
        private final CompanyRepository companyRepository;
        private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
      this.companyRepository = companyRepository;
      this.userRepository = userRepository;
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
      ResultPaginationDTO.Meta mt=new ResultPaginationDTO.Meta();
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
      Optional<Company>optional=this.companyRepository.findById(id);
      if(optional.isPresent()){
        Company com=optional.get();
        List<User> users=this.userRepository.findAllByCompany(com);
        this.userRepository.deleteAll(users);
      }
      this.companyRepository.deleteById(id);
    }

}
