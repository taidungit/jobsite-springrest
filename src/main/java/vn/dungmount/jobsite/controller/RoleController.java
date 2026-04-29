package vn.dungmount.jobsite.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.turkraft.springfilter.boot.Filter;

import vn.dungmount.jobsite.domain.Role;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.repository.RoleRepository;
import vn.dungmount.jobsite.service.RoleService;
import vn.dungmount.jobsite.util.annotation.ApiMessage;
import vn.dungmount.jobsite.util.error.IdInvalidException;

@Controller
@RequestMapping("/api/v1")
public class RoleController {
        private final RoleService roleService;
        private final RoleRepository roleRepository;

    public RoleController(RoleService roleService, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role) throws IdInvalidException {
        if (this.roleRepository.existsByName(role.getName())) {
                throw new IdInvalidException("Role name '" + role.getName() + "' đã tồn tại!");
            }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
    }

    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws IdInvalidException{
        Role currentRole = this.roleRepository.findById(role.getId())
            .orElseThrow(() -> new IdInvalidException("Role với id = " + role.getId() + " không tồn tại"));
        Role updatedRole = this.roleService.update(currentRole);
        return ResponseEntity.ok(updatedRole);
}

    @GetMapping("/roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec,
            Pageable pageable) {
       return ResponseEntity.ok().body(this.roleService.getAllRoles(spec, pageable));
}

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws Exception {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isEmpty()) {
            throw new IdInvalidException("Role không tồn tại để xóa!");
        }
        this.roleRepository.deleteById(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Get role by id")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) throws IdInvalidException {
        Role role = this.roleService.findById(id);
        if (role == null) {
            throw new IdInvalidException("Role không tồn tại!");
        }
        return ResponseEntity.ok().body(role);
    }
}
