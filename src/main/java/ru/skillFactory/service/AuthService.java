package ru.skillFactory.service;

import ru.skillFactory.model.User;

public interface AuthService {
    void signup();

    User signIn();
}
