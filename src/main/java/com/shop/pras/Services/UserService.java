package com.shop.pras.Services;

import com.shop.pras.Models.Product;
import com.shop.pras.Models.User;
import com.shop.pras.Repository.BaseRepository;
import com.shop.pras.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BaseRepository baseRepository;

    public UserService(UserRepository userRepository, BaseRepository baseRepository) {

        this.userRepository = userRepository;
        this.baseRepository = baseRepository;
    }

    public Optional<User> findUserByPasswordForBase(String password, String baseLogin) {
        return userRepository.findByUserPasswordAndBase_BaseLogin(password, baseLogin);
    }
    public List<User> getAllUsers(String baseLogin) {
        return userRepository.getAllUsers(baseLogin);
    }

        @Transactional
    public void addUser(String baseLogin, User userDto) {
        // 1. Знаходимо базу
        baseRepository.findById(baseLogin).ifPresent(base -> {
            // 2. Шукаємо користувача за паролем ТА базою
            Optional<User> existingUserOpt = userRepository.findByUserPasswordAndBase_BaseLogin(userDto.getUserPassword(), baseLogin);

            if (existingUserOpt.isPresent()) {
                // ОНОВЛЕННЯ: беремо існуючий об'єкт, щоб Hibernate зрозумів, що це UPDATE
                User existingUser = existingUserOpt.get();
                existingUser.setUserName(userDto.getUserName());
                existingUser.setUserPassword(userDto.getUserPassword());
                existingUser.setAdmin(userDto.getRole());

                // .save() викликати не обов'язково, @Transactional зробить UPDATE автоматично
            } else {
                // СТВОРЕННЯ: прив'язуємо до бази та зберігаємо
                userDto.setBase(base);
                userRepository.save(userDto);
            }
        });
    }
}
