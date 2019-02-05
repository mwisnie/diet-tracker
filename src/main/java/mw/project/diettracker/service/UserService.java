package mw.project.diettracker.service;

import lombok.RequiredArgsConstructor;
import mw.project.diettracker.entity.User;
import mw.project.diettracker.entity.dto.UserDTO;
import mw.project.diettracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    @Autowired
    private UserRepository repository;

    // todo: has role admin
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    // todo: (has role user && dto.getId == principal.getId()) || has role admin
    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User createUser(UserDTO dto) {
        // emit registration event

        User user = User.builder()
                        .name(dto.getName())
                        .password(dto.getPassword())
                        .email(dto.getEmail())
                        .build();

        return repository.save(user);
    }

    // todo: (has role user && dto.getId == principal.getId()) || has role admin
    public User updateUser(UserDTO dto) {
        if (dto.getResetPassword()) {
            // reset password event
        }

        // get user by id from principal
        // set fields from dto
        // return repository.save(user);
        return null;
    }

    // todo: (has role user && dto.getId == principal.getId()) || has role admin
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }

}
