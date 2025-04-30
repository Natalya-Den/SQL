package ru.netology.login.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.login.data.DataHelper;
import ru.netology.login.data.SQLHelper;
import ru.netology.login.page.LoginPage;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.login.data.SQLHelper.cleanAuthCode;
import static ru.netology.login.data.SQLHelper.cleanDatabase;

public class LoginTests {
    LoginPage loginPage;
    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfoTestData();

    @AfterAll
    static void tearDownAll(){
        cleanDatabase();
    }

    @AfterEach
    void tearDown () {
        cleanAuthCode();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    void successfulLoginWithLoginAndPasswordFromTestDataTest() {
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void getErrorIfUserIsNotExistInBaseTest() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан логин или пароль");
    }

    @Test
    void getErrorIfLoginExistInBaseAndRandomVerifyCodeTest(){
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.generaterRandomCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка! \nНеверно указан код! Попробуйте ещё раз.");
    }
}
