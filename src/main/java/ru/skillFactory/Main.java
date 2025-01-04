package ru.skillFactory;

import ru.skillFactory.service.NavigationService;
import ru.skillFactory.service.impl.NavigationServiceImpl;

public class Main {
    public static void main(String[] args) {
        NavigationService navigationService = new NavigationServiceImpl();
        navigationService.generalNavigation();
    }
}