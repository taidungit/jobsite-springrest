package vn.dungmount.jobsite.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.dungmount.jobsite.domain.Skill;
import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.domain.response.ResUserDTO;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean existsByName(String name){
        return this.skillRepository.existsByName(name);
    }
    public Skill createSkill(Skill skill){
        return this.skillRepository.save(skill);
    }
       public Skill getSkillById(Long id){
    Optional<Skill> optional=this.skillRepository.findById(id);
    if(optional.isPresent()){
        return optional.get();
    }
    else return null;
    }
    public Skill updateSkill(Skill skill){
      Skill currentSkill=this.getSkillById(skill.getId());
      
      if(currentSkill!=null){
        currentSkill.setName(skill.getName());
        currentSkill=this.skillRepository.save(currentSkill);
      }
        return currentSkill;
    }

     public ResultPaginationDTO getAllSkills(Specification<Skill> spec,Pageable page){
        Page<Skill>pageSkill=this.skillRepository.findAll(spec,page);
        ResultPaginationDTO rs=new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt=new ResultPaginationDTO.Meta();
        mt.setPage(page.getPageNumber()+1);
        mt.setPageSize(page.getPageSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());
        rs.setMeta(mt);
        
            List<Skill> listSKill = pageSkill.getContent();
          
        rs.setResult(listSKill);
        return rs;

    }
}
