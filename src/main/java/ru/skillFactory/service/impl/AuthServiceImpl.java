package ru.skillFactory.service.impl;

import ru.skillFactory.service.AuthService;
import ru.skillFactory.model.User;

import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class AuthServiceImpl implements AuthService {
    public AuthServiceImpl(Scanner scanner) {
        this.scanner = scanner;
    }

    private final Scanner scanner;
    protected HashMap<UUID, User> users = new HashMap<>();

    @Override
    public void signup() {
        System.out.println("Для регистрации придумайте логин:");
        String username = scanner.next();

        User user = new User(username);
        users.put(user.getUuid(), user);

        System.out.println("Ваш идентификатор для входа: " + user.getUuid().toString());
    }

    @Override
    public User signIn() {
        System.out.println("Для авторизации введите ваш идентификатор:");
        String id = scanner.next();
        try {
            User user = users.get(UUID.fromString(id));
            if (user != null) {
                System.out.println("Авторизация прошла успешно!");
            } else {
                System.out.println("Ошибка Авторизации! Пользователь не найден.");
            }
            return user;
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный идентификатор!");
            return null;
        }
    }
}
