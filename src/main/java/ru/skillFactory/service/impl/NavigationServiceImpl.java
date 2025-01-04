package ru.skillFactory.service.impl;

import ru.skillFactory.model.User;
import ru.skillFactory.service.AuthService;
import ru.skillFactory.service.NavigationService;
import ru.skillFactory.service.ShortLinksService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class NavigationServiceImpl implements NavigationService {
    private final ShortLinksService shortLinksService;
    private final AuthService authService;
    private User currentUser;
    private final Scanner scanner;

    public NavigationServiceImpl() {
        this.scanner = new Scanner(System.in);
        this.shortLinksService = new ShortLinksServiceImpl(scanner);
        this.authService = new AuthServiceImpl(scanner);
    }

    @Override
    public void generalNavigation() {
        System.out.println("Сервис кооротких ссылок приветствует вас!");

        while (true) {
            printCurrentUser();
            printMainMenu();

            int i = -1;
            try {
                i = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Введите целое число!");
                scanner.nextLine();
                continue;
            }

            if (i == 1) {
                authNavigation();
            } else if (i == 2) {
                linksNavigation();
            } else if (i == 0) {
                System.out.println("Пока!");
                scanner.close();
                return;
            } else {
                System.out.println("Такой команды нет");
            }
        }
    }

    @Override
    public void authNavigation() {
        while (true) {
            printCurrentUser();
            printAuthMenu();

            int i = -1;
            try {
                i = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Введите целое число!");
                scanner.nextLine();
                continue;
            }

            if (i == 1) {
                authService.signup();
            } else if (i == 2) {
                currentUser = authService.signIn();
            } else if (i == 3) {
                currentUser = null;
            } else if (i == 0) {
                return;
            } else {
                System.out.println("Такой команды нет");
            }
        }
    }

    @Override
    public void linksNavigation() {
        if (currentUser == null) {
            System.out.println("\nВход не выполнен, доступ в раздел запрещён!\n");
            return;
        }
        while (true) {
            printCurrentUser();
            printLinksMenu();

            int i = -1;
            try {
                i = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Введите целое число!");
                scanner.nextLine();
                continue;
            }

            if (i == 1) {
                shortLinksService.createLink(currentUser.getUuid());
            } else if (i == 2) {
                shortLinksService.followTheLink(currentUser.getUuid());
            } else if (i == 3) {
                shortLinksService.getAllLinksByUser(currentUser.getUuid());
            } else if (i == 4) {
                shortLinksService.changeTransitionLimit(currentUser.getUuid());
            } else if (i == 5) {
                shortLinksService.deleteLink(currentUser.getUuid());
            } else if (i == 0) {
                return;
            } else {
                System.out.println("Такой команды нет");
            }
        }
    }

    private void printCurrentUser() {
        if (currentUser != null) {
            System.out.println("(Текущий пользователь: " + currentUser.getUsername() + ")\n");
        } else {
            System.out.println("(Вход не выполнен)\n");
        }
    }

    private void printMainMenu() {
        System.out.println("Текущий раздел - главное меню\n");
        System.out.println("Что вы хотите сделать?");
        System.out.println("1 - Личный кабинет");
        System.out.println("2 - Работа с ссылками");
        System.out.println("0 - Выйти из приложения");
    }

    private void printAuthMenu() {
        System.out.println("Текущий раздел - Личный кабинет\n");
        System.out.println("Что вы хотите сделать?");
        System.out.println("1 - Зарегистрироваться");
        System.out.println("2 - Авторизироваться");
        System.out.println("3 - Разлогиниться");
        System.out.println("0 - Назад в главное меню");
    }

    private void printLinksMenu() {
        System.out.println("Текущий раздел - Работа с ссылками\n");
        System.out.println("Что вы хотите сделать?");
        System.out.println("1 - Создать ссылку");
        System.out.println("2 - Перейти по ссылке");
        System.out.println("3 - Получить список своих ссылок");
        System.out.println("4 - Редактировать лимит переходов");
        System.out.println("5 - Удалить короткую ссылку");
        System.out.println("0 - Назад в главное меню");
    }
}
