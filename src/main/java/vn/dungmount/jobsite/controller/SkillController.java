package vn.dungmount.jobsite.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.dungmount.jobsite.domain.Skill;
import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.domain.response.ResUpdateUserDTO;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.service.SkillService;
import vn.dungmount.jobsite.util.annotation.ApiMessage;
import vn.dungmount.jobsite.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;
    
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create skill")

    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException{
        if(skill.getName() == null || skill.getName().trim().isEmpty()){
            throw new IdInvalidException(
            "Name không được để trống"
        );
        }
        boolean isExist=this.skillService.existsByName(skill.getName());
        if(isExist){
            throw new IdInvalidException(
            "Skill này đã tồn tại vui lòng nhập skill khác!"
        );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.createSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update skill")
    public ResponseEntity<Skill> updateSkill(@RequestBody Skill skill)throws IdInvalidException{
        boolean isExist=this.skillService.existsByName(skill.getName());
        if(isExist){
            throw new IdInvalidException(
            "Skill này đã tồn tại vui lòng nhập skill khác!"
        );}
        Skill currentSkill= this.skillService.updateSkill(skill);
      
          if(currentSkill==null){
            throw new IdInvalidException("Skill không tồn tại!");
        }
        return ResponseEntity.ok(currentSkill);
        
    }
    @GetMapping("/skills")
    @ApiMessage("Get all skills")
        public ResponseEntity<ResultPaginationDTO> getAllSkill(  @Filter Specification<Skill> spec,Pageable pageable){
        ResultPaginationDTO skills=this.skillService.getAllSkills(spec,pageable);
        return ResponseEntity.ok(skills);
    }
}
