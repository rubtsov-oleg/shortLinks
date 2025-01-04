package ru.skillFactory.service.impl;

import ru.skillFactory.config.Settings;
import ru.skillFactory.model.Link;
import ru.skillFactory.service.ShortLinksService;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class ShortLinksServiceImpl implements ShortLinksService {
    private final Scanner scanner;
    protected HashMap<UUID, ArrayList<Link>> links = new HashMap<>();

    public ShortLinksServiceImpl(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void createLink(UUID userId) {
        System.out.println("Введите ссылку для сокращения (например, https://example.com):");
        String url = scanner.next();
        if (!checkLinkFormat(url)) {
            return;
        }

        int transitionLimit = getTransitionLimitFromUser();
        int liveHours = getLiveHoursFromUser();

        Link link = new Link(url, userId, transitionLimit, liveHours);
        ArrayList<Link> userLinks = links.get(userId);
        if (userLinks != null) {
            userLinks.removeIf(link1 -> link1.getFullURL().equals(url));
        } else {
            userLinks = new ArrayList<>();
        }
        userLinks.add(link);
        links.put(userId, userLinks);

        System.out.println("Ваша короткая ссылка: " + link.getShortURL());
    }

    @Override
    public void followTheLink(UUID userId) {
        ArrayList<Link> userLinks = links.get(userId);
        if (userLinks == null) {
            System.out.println("У вас ещё нет созданных коротких ссылок:");
            return;
        }

        System.out.println("Введите ссылку по которой хотите перейти:");
        String url = scanner.next();

        for (Link link : userLinks) {
            if (link.getShortURL().equals(url)) {
                try {
                    if (isLinkAvailable(link)) {
                        Desktop.getDesktop().browse(new URI(link.getFullURL()));
                        link.setTransitionLimit(link.getTransitionLimit() - 1);
                        return;
                    }
                    userLinks.remove(link);
                    return;
                } catch (NullPointerException | URISyntaxException | IOException e) {
                    System.out.println("Не удалось открыть ссылку");
                    return;
                }
            }
        }
        System.out.println("Короткая ссылка не найдена!");
    }

    private boolean isLinkAvailable(Link link) {
        if (link.getExpiresOn().isBefore(LocalDateTime.now())) {
            System.out.println("Время жизни ссылки истекло");
            return false;
        }
        if (link.getTransitionLimit() < 1) {
            System.out.println("Количество переходов по ссылке исчерпано!");
            return false;
        }
        return true;
    }

    @Override
    public void getAllLinksByUser(UUID userId) {
        System.out.println(links.get(userId) != null ? links.get(userId) : "У вас пока нет созданных ссылок");
    }

    @Override
    public void changeTransitionLimit(UUID userId) {
        System.out.println("Введите короткую ссылку, у которой хотите изменить Количество переходов");
        String url = scanner.next();

        ArrayList<Link> userLinks = links.get(userId);
        if (userLinks != null) {
            for (Link link : userLinks) {
                if (link.getShortURL().equals(url)) {
                    link.setTransitionLimit(getTransitionLimitFromUser());
                    System.out.println("Значение изменено!");
                    return;
                }
            }
        }
        System.out.println("Ссылка не найдена");
    }

    @Override
    public void deleteLink(UUID userId) {
        System.out.println("Введите короткую ссылку, которую хотите удалить");
        String url = scanner.next();

        ArrayList<Link> userLinks = links.get(userId);
        if (userLinks != null) {
            for (Link link : userLinks) {
                if (link.getShortURL().equals(url)) {
                    userLinks.remove(link);
                    System.out.println("Ссылка успешно удалена");
                    return;
                }
            }
        }
        System.out.println("Ссылка не найдена");
    }

    private Integer getTransitionLimitFromUser() {
        System.out.println(
                "Введите количество переходов, можно поставить любой символ (например 'q'), тогда по-умолчанию будет "
                        + Settings.TRANSITION_LIMIT
        );
        String transitionLimitInput = scanner.next();
        int transitionLimit;
        try {
            transitionLimit = Integer.parseInt(transitionLimitInput);
            transitionLimit = Math.max(transitionLimit, Integer.parseInt(Settings.TRANSITION_LIMIT));
        } catch (NumberFormatException e) {
            transitionLimit = Integer.parseInt(Settings.TRANSITION_LIMIT);
        }
        return transitionLimit;
    }

    private Integer getLiveHoursFromUser() {
        System.out.println(
                "Введите время жизни ссылки(в часах), можно поставить " +
                        "любой символ (например 'q'), тогда по-умолчанию будет "
                        + Settings.LIVE_HOURS
        );
        String liveHoursInput = scanner.next();
        int liveHours;
        try {
            liveHours = Integer.parseInt(liveHoursInput);
            liveHours = Math.min(liveHours, Integer.parseInt(Settings.LIVE_HOURS));
        } catch (NumberFormatException e) {
            liveHours = Integer.parseInt(Settings.LIVE_HOURS);
        }
        return liveHours;
    }

    private boolean checkLinkFormat(String link) {
        try {
            new URL(link);
            return true;
        } catch (MalformedURLException e) {
            System.out.println("Неверный формат url!");
            return false;
        }
    }
}
