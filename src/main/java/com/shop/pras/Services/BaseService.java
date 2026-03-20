package com.shop.pras.Services;

import com.shop.pras.Models.Base;
import com.shop.pras.Models.User;
import com.shop.pras.Repository.BaseRepository;
import com.shop.pras.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BaseService {

    private final BaseRepository baseRepository;
    private final UserRepository userRepository;

    public BaseService(BaseRepository baseRepository, UserRepository userRepository) {
        this.baseRepository = baseRepository;
        this.userRepository = userRepository;
    }

//    @Transactional
//    public void createBase(String name, String login, String password) {
//        if (login == null || login.isBlank()) {
//            throw new RuntimeException("Введіть логін!");
//        }
//
//        if (baseRepository.existsById(login)) {
//            throw new RuntimeException("Логін вже зайнято");
//        }
//
//        Base base = new Base(login, name, password);
//        baseRepository.save(base);
//
//        User admin = new User("admin", "123", true, base);
//        admin.setBase(base);
//
//        userRepository.save(admin);
//    }

    public Base save(Base base) {
        return baseRepository.save(base);
    }

    public List<Base> findAll() {
        return baseRepository.findAll();
    }

    public Optional<Base> findByLogin(String login) {
        return baseRepository.findById(login);
    }

    public boolean exists(String login) {
        return baseRepository.existsById(login);
    }

    public void delete(String login) {
        baseRepository.deleteById(login);
    }
}
