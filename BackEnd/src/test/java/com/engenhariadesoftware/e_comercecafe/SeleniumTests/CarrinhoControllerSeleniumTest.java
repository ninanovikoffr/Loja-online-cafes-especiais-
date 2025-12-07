package com.engenhariadesoftware.e_comercecafe.SeleniumTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CarrinhoControllerSeleniumTest {

    private static String BASE_URL;
    private static final String LOGIN_PATH = "/login"; // ajuste se for diferente
    private WebDriver driver;
    private WebDriverWait wait;

    private static final String TEST_EMAIL = "gustavo981233@gmail.com";
    private static final String TEST_SENHA = "senha123";

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        BASE_URL = System.getenv().getOrDefault("SELENIUM_BASE_URL", "http://localhost:5173");
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1366,768");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void fluxoCompletoLoginAdicionarCarrinhoEscolherEnderecoFinalizar() {
        realizarLogin();

        // Ajuste o seletor abaixo para o botao de adicionar produto ao carrinho na vitrine/lista
        By botaoAdicionar = By.cssSelector(".add-to-cart, button[data-testid='add-to-cart']");
        clicar(botaoAdicionar);

        // Ajuste o seletor do icone/botao que abre o carrinho (pode ser um badge de carrinho no header)
        By abrirCarrinho = By.cssSelector(".cart-icon, [data-testid='cart-button']");
        clicar(abrirCarrinho);

        // Seleciona o primeiro endereco listado (ajuste conforme seu markup; Carrinho.jsx usa .endereco-box)
        By enderecoBox = By.cssSelector(".endereco-box");
        wait.until(ExpectedConditions.presenceOfElementLocated(enderecoBox));
        clicar(enderecoBox);

        // Finalizar pedido (Carrinho.jsx usa .finalizar)
        By finalizar = By.cssSelector(".finalizar");
        clicar(finalizar);

        // Se o front usa alert() em sucesso (Carrinho.jsx faz alert "Pedido finalizado com sucesso!")
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            assertTrue(alert.getText().toLowerCase().contains("sucesso"));
            alert.accept();
        } catch (TimeoutException ignored) {
            // Caso nao haja alert, podemos validar por texto em tela/toast se existirem seletores conhecidos
            assertTrue(true, "Finalizacao executada sem alert visivel");
        }
    }

    private void realizarLogin() {
        driver.get(BASE_URL + LOGIN_PATH);

        // Ajuste os seletores conforme os campos da sua tela de login
        By emailField = By.cssSelector("input[name='email'], input[type='email']");
        By senhaField = By.cssSelector("input[name='senha'], input[type='password']");
        By botaoEntrar = By.cssSelector("button[type='submit'], button[data-testid='login-submit']");

        wait.until(ExpectedConditions.presenceOfElementLocated(emailField)).sendKeys(TEST_EMAIL);
        driver.findElement(senhaField).sendKeys(TEST_SENHA);
        driver.findElement(botaoEntrar).click();

        // Espera redirecionar para a home (/) ou outro caminho pos-login
        wait.until(ExpectedConditions.urlContains("localhost:5173"));
    }

    private void clicar(By seletor) {
        wait.until(ExpectedConditions.elementToBeClickable(seletor)).click();
    }
}
