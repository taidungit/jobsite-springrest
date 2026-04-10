package vn.dungmount.jobsite.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import vn.dungmount.jobsite.domain.Permission;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
}
    public Permission update(Permission p){
        p.setName(p.getName());
        p.setApiPath(p.getApiPath());
        p.setMethod(p.getMethod());
        p.setModule(p.getModule());
        return this.permissionRepository.save(p);
    }

public ResultPaginationDTO getAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pageUser = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        List<Permission> listPermission = pageUser.getContent();
        rs.setResult(listPermission);
        return rs;
    }

    @Transactional 
    public void deletePermission(long id) {
        this.permissionRepository.deleteRelationByPermissionId(id);
        this.permissionRepository.deleteById(id);
    }

}
