package com.nvtt.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nvtt.pojo.Role;
import com.nvtt.pojo.User;
import com.nvtt.pojo.dtos.admin.RegisterRequestDTO;
import com.nvtt.pojo.dtos.user.ResUserInfoDTO;
import com.nvtt.repositories.RoleRepository;
import com.nvtt.repositories.UserRepository;
import com.nvtt.services.UserService;
import com.nvtt.utils.UserUtils.UserUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

@Service
@Transactional
@PropertySource(value = "classpath:configs.properties", ignoreResourceNotFound = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserUtils userUtils;

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.getUserByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.getUserById(id);
    }

    @Override
    public ResUserInfoDTO addUser(Map<String, String> params, MultipartFile avatar) {
        User user = new User();
        user.setFullName(params.get("fullName"));
        user.setEmail(params.get("email"));
        user.setPassword(this.passwordEncoder.encode(params.get("password")));

        Role role = this.roleRepository.getRoleById(Long.valueOf(params.get("role")));
        user.setRole(role);

        try {

            Map res = this.cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource type", "auto"));
            user.setAvatarUrl(res.get("secure_url").toString());

        } catch (IOException ex) {
            System.err.println("error: upload avatar to cloudinary");
        }

        this.userRepository.addUser(user);

        ResUserInfoDTO userInfo = new ResUserInfoDTO(user.getId(), user.getEmail(), user.getFullName(), user.getAvatarUrl(), user.getRole().getId(), user.getRole().getName(), user.getIsActive());

        return userInfo;
    }

    @Override
    public boolean authenticate(String email, String password) {
        return this.userRepository.authenticate(email, password);
    }

    @Override
    public User getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            User user = userRepository.getUserByEmail(email);
            return user;
        }
        return null;
    }

    @Override
    public ResUserInfoDTO updateMyInfo(Map<String, String> params, Optional<MultipartFile> avatar) {
        User currentUser = this.getMyInfo();
        if (currentUser == null) {
            return null;
        }

        User u = userUtils.converParamsToUserForUpdating(currentUser, params);

        if (avatar.isPresent()) {
            try {
                Map res = this.cloudinary.uploader().upload(avatar.get().getBytes(), ObjectUtils.asMap("resource type", "auto"));
                u.setAvatarUrl(res.get("secure_url").toString());
            } catch (IOException ex) {
                System.err.println("errorr");
            }
        }

        this.userRepository.addUser(u);

        ResUserInfoDTO userInfo = new ResUserInfoDTO(u.getId(), u.getEmail(), u.getFullName(), u.getAvatarUrl(), u.getRole().getId(), u.getRole().getName(), u.getIsActive());
        return userInfo;
    }

    public boolean checkExistEmail(String email) {
        return this.userRepository.checkExistEmail(email);
    }

    public void addUser(RegisterRequestDTO dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(this.passwordEncoder.encode(dto.getPassword()));

        Role role = this.roleRepository.getRoleById(dto.getRoleId());

        user.setRole(role);

        try {
            Map res = this.cloudinary.uploader().upload(dto.getAvatarUrl().getBytes(), ObjectUtils.asMap("resource type", "auto"));
            user.setAvatarUrl(res.get("secure_url").toString());
        } catch (Exception ex) {
            System.err.print("error: upload avatar to cloudinary");
        }

        this.userRepository.addUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUsersData(String search, Long roleId, int page) {
        int size = this.env.getProperty("pagination.pageSize", Integer.class);

        int offset = (page - 1) * size;

        List<User> list = userRepository.findAll(search, roleId, offset, size);
        long totalElements = userRepository.countAll(search, roleId);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<ResUserInfoDTO> dtos = list.stream().map(u -> new ResUserInfoDTO(
                u.getId(),
                u.getFullName(),
                u.getEmail(),
                u.getAvatarUrl(),
                u.getRole().getId(),
                u.getRole().getName(),
                u.getIsActive()
        )).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("users", dtos);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);
        result.put("totalElements", totalElements);
        return result;
    }

    @Override
    public User toggleStatus(Long id) {
        return userRepository.toggleStatus(id);
    }

    @Override
    public void updateUserRole(Long userId, Long roleId) {
        userRepository.updateRole(userId, roleId);
    }

    @Override
    public List<User> findByRoleName(String roleName) {
        return this.userRepository.findByRoleName(roleName);
    }

    @Override
    public boolean checkActiveAccount(String email) {
        return this.userRepository.checkActiveAccount(email);
    }
}
