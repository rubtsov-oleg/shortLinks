package ru.skillFactory.service;

import java.util.UUID;

public interface ShortLinksService {
    void createLink(UUID userId);

    void followTheLink(UUID userId);

    void getAllLinksByUser(UUID userId);

    void changeTransitionLimit(UUID userId);

    void deleteLink(UUID userId);
}
