package vn.dungmount.jobsite.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.dungmount.jobsite.domain.Permission;
import vn.dungmount.jobsite.domain.Role;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.repository.PermissionRepository;
import vn.dungmount.jobsite.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role findById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    public Role create(Role role){
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions()
                                            .stream().map(p -> p.getId())
                                            .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findAllById(reqPermissions);
            role.setPermissions(dbPermissions);
        }
        return this.roleRepository.save(role);
    }

    public Role update(Role r){
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(p -> p.getId())
                    .collect(Collectors.toList());
        List<Permission> dbPermissions = this.permissionRepository.findAllById(reqPermissions);        
        r.setPermissions(dbPermissions);
        }
        r.setName(r.getName());
        r.setDescription(r.getDescription());
        r.setActive(r.isActive());
        return this.roleRepository.save(r);
    }

    public ResultPaginationDTO getAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageUser = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        List<Role> listRoles = pageUser.getContent();
        rs.setResult(listRoles);
        return rs;
    }


}
