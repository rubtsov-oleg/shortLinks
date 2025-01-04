package ru.skillFactory.model;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

public class Link {
    protected String fullURL;
    protected String shortURL;
    protected UUID userId;
    protected Integer transitionLimit;
    protected LocalDateTime createdAt;
    protected LocalDateTime expiresOn;


    public void setTransitionLimit(int transitionLimit) {
        this.transitionLimit = transitionLimit;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresOn(LocalDateTime expiresOn) {
        this.expiresOn = expiresOn;
    }


    public Integer getTransitionLimit() {
        return transitionLimit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresOn() {
        return expiresOn;
    }

    public String getFullURL() {
        return fullURL;
    }

    public String getShortURL() {
        return shortURL;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setFullURL(String fullURL) {
        this.fullURL = fullURL;
    }

    public Link(String fullURL, UUID userId, Integer transitionLimit, Integer liveHours) {
        this.fullURL = fullURL;
        this.userId = userId;
        this.shortURL = makeShortUrl(fullURL, userId);
        this.createdAt = LocalDateTime.now();
        this.transitionLimit = transitionLimit;
        this.expiresOn = LocalDateTime.now().plusHours(liveHours);
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public static String makeShortUrl(String fullURL, UUID userId) {
        try {
            byte[] bytesOfMessage = fullURL.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            String code = String.format("%032x", new BigInteger(1, theMD5digest));
            return "clck.ru/" + code.substring(0, 3) + userId.toString().substring(0, 3);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("\nНе удалось сформировать коротку ссылку!\n");
            return null;
        }
    }

    @Override
    public String toString() {
        return "Link{" +
                "Полный URL='" + fullURL + '\'' +
                ", Короткая ссылка='" + shortURL + '\'' +
                ", Количетсво переходов=" + transitionLimit +
                ", Дата и время создания ссылки=" + createdAt +
                ", Дата и время протухания ссылки=" + expiresOn +
                '}';
    }
}
