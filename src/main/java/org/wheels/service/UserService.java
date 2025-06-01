package org.wheels.service;

import org.springframework.stereotype.Service;
import org.wheels.model.User;
import org.wheels.model.Plan;
import org.wheels.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private ReceiptService receiptService;

    public UserService(UserRepository userRepository, ReceiptService receiptService) {
        this.userRepository = userRepository;
        this.receiptService = receiptService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public void changePlan(String email, Plan newPlan) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPlano(newPlan);
            userRepository.save(user);

            // Gera o recibo ao trocar de plano
            receiptService.generateReceipt(user, newPlan);
        }
    }

    public void cancelPlan(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPlano(Plan.FREE);
            userRepository.save(user);
        }
    }
}