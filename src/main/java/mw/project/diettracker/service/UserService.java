package mw.project.diettracker.service;

import lombok.RequiredArgsConstructor;
import mw.project.diettracker.entity.User;
import mw.project.diettracker.entity.dto.UserDTO;
import mw.project.diettracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    @Autowired
    private UserRepository repository;

    // TODO: has role admin
    public List<UserDTO> getAllUsers() {
        return repository.findAll()
                    .stream()
                    .map(this::buildUserDTO)
                    .collect(Collectors.toList());
    }

    // TODO: (has role user && dto.getId == principal.getId()) || has role admin
    public UserDTO getUserById(Long id) {
        return buildUserDTO(repository.findById(id).orElseThrow(() -> new RuntimeException("User not found"))); // TODO: UserNotFoundException
    }

    public UserDTO createUser(UserDTO dto) {
        // emit registration event

        User user = User.builder()
                        .name(dto.getName())
                        .password(dto.getPassword())
                        .email(dto.getEmail())
                        .build();

        return buildUserDTO(repository.save(user));
    }

    // TODO: (has role user && dto.getId == principal.getId()) || has role admin
    public UserDTO updateUser(UserDTO dto) {
        // TODO:
//        if (dto.getResetPassword()) {
//            // reset password event
//        }

        // get user by id from principal
        // set fields from dto
        // return repository.save(user);
        System.out.println("Mock update user");
        return null;
    }

    // todo: (has role user && dto.getId == principal.getId()) || has role admin
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }

    private UserDTO buildUserDTO(User user) {
        return UserDTO.builder()
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
    }

}
