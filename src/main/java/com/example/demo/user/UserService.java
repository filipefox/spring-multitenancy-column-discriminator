package com.example.demo.user;

import com.example.demo.core.authentications.TenantAuthenticationToken;
import com.example.demo.core.exceptions.RoleNotAllowedException;
import com.example.demo.core.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(User user) {
        verifyRole(user);
        setPassword(user);
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateById(int id, User user) {
        User userFromDatabase = userRepository.findOneById(id).orElseThrow(EntityNotFoundException::new);
        user.setId(id);
        verifyRole(user);
        setPassword(user, userFromDatabase);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    private void verifyRole(User user) {
        TenantAuthenticationToken tenantAuthenticationToken = (TenantAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (user.getRole() != null) {
            if (user.getRole().getId() < tenantAuthenticationToken.getRoleId()) {
                throw new RoleNotAllowedException("It's not possible to save a user with this role using a user with lower privileges");
            }

            if (user.getRole().getId() == Constants.SUPER_ADMIN_ROLE_ID && tenantAuthenticationToken.getTenantId() != Constants.SUPER_TENANT_ADMINISTRATOR_ID) {
                throw new RoleNotAllowedException("It's not possible to save a user with this role in this tenant");
            }
        }
    }

    private void setPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    private void setPassword(User user, User userFromDatabase) {
        user.setPassword(user.getPassword() != null ? passwordEncoder.encode(user.getPassword()) : userFromDatabase.getPassword());
    }
}