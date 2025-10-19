package com.ashtana.backend.Service;


import com.ashtana.backend.DTO.RequestDTO.LoginRequestDTO;
import com.ashtana.backend.DTO.RequestDTO.UserRequestDTO;
import com.ashtana.backend.DTO.ResponseDTO.UserResponseDTO;
import com.ashtana.backend.Entity.User;
import com.ashtana.backend.Enums.Role;
import com.ashtana.backend.Repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    // -------------------- 🔹 Mapper Methods -------------------- //

    private UserResponseDTO toDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setProfileImage(user.getProfileImage());
//        dto.setGender(user.getGender());
//        dto.setBio(user.getBio());
//        dto.setShopName(user.getShopName());
//        dto.setShopDescription(user.getShopDescription());
//        dto.setShopLogo(user.getShopLogo());
//        dto.setAverageRating(user.getAverageRating());
        return dto;
    }

    private User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(dto.getPassword());
        user.setRole(Role.CUSTOMER); // ✅ Default role is CUSTOMER
        return user;
    }

    // -------------------- 🔹 Register -------------------- //

    public UserResponseDTO register(UserRequestDTO dto) {
        boolean emailExists = userRepo.findAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.getEmail()));
        if (emailExists) throw new RuntimeException("Email already registered!");

        User saved = userRepo.save(toEntity(dto));
        return toDto(saved);
    }

    // -------------------- 🔹 Login -------------------- //

    public Optional<UserResponseDTO> login(LoginRequestDTO dto) {
        return userRepo.findAll().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(dto.getEmail()))
                .filter(u -> u.getPassword().equals(dto.getPassword())) // ⚠️ plain text check
                .map(this::toDto)
                .findFirst();
    }

    // -------------------- 🔹 CRUD -------------------- //

    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return toDto(user);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getPassword() != null) user.setPassword(dto.getPassword());

        User updated = userRepo.save(user);
        return toDto(updated);
    }

    public String deactivateUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        userRepo.save(user);
        return "User deactivated successfully";
    }

    // -------------------- 🔹 Role Management -------------------- //

//    public UserResponseDTO promoteToVendor(Long id) {
//        User user = userRepo.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//        user.setRole(Role.VENDOR);
//        User saved = userRepo.save(user);
//        return toDto(saved);
//    }

    public UserResponseDTO promoteToAdmin(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRole(Role.ADMIN);
        User saved = userRepo.save(user);
        return toDto(saved);
    }

    public UserResponseDTO promoteToMorderator(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRole(Role.MORDERATOR);
        User saved = userRepo.save(user);
        return toDto(saved);
    }
}
