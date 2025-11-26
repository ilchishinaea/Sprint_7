package utils;

import io.qameta.allure.Step;
import models.Courier;
import java.util.UUID;

public class CourierFactory {

    @Step("Генерируем рандомного валидного Courier")
    public static Courier randomCourier() {
        return new Courier(
                randomLogin(),
                randomPassword(),
                randomName()
        );
    }

    @Step("Генерируем рандомного Courier без логина")
    public static Courier courierWithMissingLogin() {
        return new Courier(
                null,
                randomPassword(),
                randomName()
        );
    }

    @Step("Генерируем Courier с существующим логином")
    public static Courier courierWithExistingLogin(Courier courier) {
        return new Courier(
                courier.getLogin(),
                randomPassword(),
                randomName()
        );
    }

    @Step("Копируем валидного Courier и указываем неверный логин")
    public static Courier validCourierWithInvalidLogin(Courier courier) {
        return new Courier(
                randomLogin(),
                courier.getPassword(),
                courier.getFirstName()
        );
    }

    @Step("Копируем валидного Courier, для логин указываем null")
    public static Courier validCourierWithNullLogin(Courier courier) {
        return new Courier(
                null,
                courier.getPassword(),
                courier.getFirstName()
        );
    }

    @Step("Генерируем рандомного Courier без пароля")
    public static Courier courierWithMissingPassword() {
        return new Courier(
                randomLogin(),
                null,
                randomName()
        );
    }

    @Step("Копируем валидного Courier и указываем неверный пароль")
    public static Courier validCourierWithInvalidPassword(Courier courier) {
        return new Courier(
                courier.getLogin(),
                randomPassword(),
                courier.getFirstName()
        );
    }

    @Step("Копируем валидного Courier, для пароля указываем null")
    public static Courier validCourierWithNullPassword(Courier courier) {
        return new Courier(
                courier.getLogin(),
                null,
                courier.getFirstName()
        );
    }


    @Step("Генерируем рандомный логин")
    public static String randomLogin() {
        return "user_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Step("Генерируем рандомный пароль")
    public static String randomPassword() {
        return "pass_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @Step("Генерируем рандомное имя")
    public static String randomName() {
        return "name_" + UUID.randomUUID().toString().substring(0, 6);
    }
}
