package courier;

import org.apache.commons.lang3.RandomStringUtils;

public class Courier {

    private String login;
    private String password;
    private String firstName;

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public static Courier getRandomCourier() {
        return new Courier(
                RandomStringUtils.randomAlphanumeric(6) + "@test.ru",
                RandomStringUtils.randomAlphanumeric(6),
                RandomStringUtils.randomAlphabetic(6)
        );
    }

    public static Courier getRandomCourierWithOnlyRequiredFields() {
        return new Courier(
                RandomStringUtils.randomAlphanumeric(6) + "@test.ru",
                RandomStringUtils.randomAlphanumeric(6),
                ""
        );
    }

    public static Courier getRandomCourierWithoutLogin() {
        return new Courier(
                "",
                RandomStringUtils.randomAlphanumeric(6),
                RandomStringUtils.randomAlphabetic(6)
        );
    }

    public static Courier getRandomCourierWithoutPassword() {
        return new Courier(
                RandomStringUtils.randomAlphanumeric(6) + "@test.ru",
                "",
                RandomStringUtils.randomAlphabetic(6)
        );
    }

    public static Courier getRandomCourierWithExactLogin(String login) {
        return new Courier(
                login,
                RandomStringUtils.randomAlphanumeric(6),
                RandomStringUtils.randomAlphabetic(6)
        );
    }

    public static Courier getRandomCourierWithExactPassword(String password) {
        return new Courier(
                RandomStringUtils.randomAlphanumeric(6) + "@test.ru",
                password,
                RandomStringUtils.randomAlphabetic(6)
        );
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}

