package vn.dungmount.jobsite.config;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.dungmount.jobsite.domain.Permission;
import vn.dungmount.jobsite.domain.Role;
import vn.dungmount.jobsite.domain.User;
import vn.dungmount.jobsite.service.UserService;
import vn.dungmount.jobsite.util.SecurityUtil;
import vn.dungmount.jobsite.util.error.PermissionException;


public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    UserService userService;

    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws Exception {

        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);

        // check permission
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        if (email != null && !email.isEmpty()) {
            User user = this.userService.getUserByEmail(email);
            if (user != null) {
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();

System.out.println("========== PERMISSIONS ==========");

for (Permission item : permissions) {
    System.out.println("ID: " + item.getId());
    System.out.println("API PATH: [" + item.getApiPath() + "]");
    System.out.println("METHOD: [" + item.getMethod() + "]");
}

System.out.println("========== REQUEST ==========");
System.out.println("PATH: [" + path + "]");
System.out.println("METHOD: [" + httpMethod + "]");

boolean isAllow = permissions.stream().anyMatch(item ->
    item.getApiPath().equals(path)
    && item.getMethod().equals(httpMethod)
);

                    if (isAllow == false) {
                        throw new PermissionException("Bạn không có quyền truy cập endpoint này.");
                    }
                } else {
                    throw new PermissionException("Bạn không có quyền truy cập endpoint này.");
                }
            }
        }

        return true;
    }
}