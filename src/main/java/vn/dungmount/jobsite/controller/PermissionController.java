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
import vn.dungmount.jobsite.domain.Permission;
import vn.dungmount.jobsite.domain.response.ResultPaginationDTO;
import vn.dungmount.jobsite.repository.PermissionRepository;
import vn.dungmount.jobsite.service.PermissionService;
import vn.dungmount.jobsite.util.annotation.ApiMessage;
import vn.dungmount.jobsite.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;
    private final PermissionRepository permissionRepository;
    public PermissionController(PermissionService permissionService, PermissionRepository permissionRepository) {
        this.permissionService = permissionService;
        this.permissionRepository = permissionRepository;
    }
    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        boolean isIdExist = this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(), permission.getMethod(), permission.getModule());
        if (isIdExist) {
            throw new IdInvalidException("Api path/method/module đã tồn tại");
        }
        this.permissionRepository.save(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(permission);
    }

    @PutMapping("/permissions")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) throws IdInvalidException {
        Permission currentPermission = this.permissionRepository.findById(permission.getId())
                .orElseThrow(() -> new IdInvalidException("Permission với id = " + permission.getId() + " không tồn tại"));
         boolean isExists = permissionRepository.existsByApiPathAndMethodAndModule(
                permission.getApiPath(), permission.getMethod(), permission.getModule());
        if (isExists) {
            if(!this.permissionService.isSameName(permission)){
                throw new IdInvalidException("Permission với apiPath, method và module này đã tồn tại ở một bản ghi khác!");
            }
        }
        Permission updatedPermission = this.permissionService.update(currentPermission);
        return ResponseEntity.ok(updatedPermission);
    }


    @GetMapping("/permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(@Filter Specification<Permission> spec,
            Pageable pageable) {
       return ResponseEntity.ok().body(this.permissionService.getAllPermissions(spec, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) throws IdInvalidException {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (permissionOptional.isEmpty()) {
            throw new IdInvalidException("Permission không tồn tại để xóa!");
        }
        this.permissionService.deletePermission(id);
        return ResponseEntity.ok().body(null);
    }
}