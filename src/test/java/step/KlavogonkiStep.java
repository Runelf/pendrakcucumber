package step;

import com.codeborne.selenide.SelenideElement;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.jodah.failsafe.internal.util.Assert;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$x;

public class KlavogonkiStep {
    private final SelenideElement closeWindowButton = $x("//input[@value='Закрыть']");
    private final SelenideElement startGameButton = $x("//a[@id='host_start']");
    private final SelenideElement hightlightWord = $x("//span[@id='typefocus']");
    private final SelenideElement inputField = $x("//input[@id='inputtext']");
    private final SelenideElement afterFocus = $x("//span[@id='afterfocus']");

    private final SelenideElement resultText = $x("//td[text()='Это вы']//ancestor-or-self::div//div[@class='stats']//div[2]/span/span");

    private String getCurrentWord() {
        return hightlightWord.getText().replaceAll("c", "с").replaceAll("o", "о");
    }

    @When("Начинаем игру")
    public void startGame() {
        closeWindowButton.click();
        if (startGameButton.isDisplayed()) {
            startGameButton.click();
        }
    }

    @And("Ждем начала игры")
    public void waitForStartGame() {
        hightlightWord.click();
    }

    @And("Вводим подсвеченное слово в цикле")
    public void playGame() {
        while (true) {
            String currentWord = getCurrentWord();
            String afterFocusSymbol = afterFocus.getText();
            inputField.sendKeys(currentWord);
            if (afterFocusSymbol.equals(".")) {
                inputField.sendKeys(".");
                break;
            }
            inputField.sendKeys(Keys.SPACE);
        }
    }

    @Then("Фиксируем что игра завершена и символов в минуту больше чем {int}")
    public void endGame(int minValue) {
        String result = resultText.getText();
        int resultNumber = Integer.parseInt(result);

        System.out.println("количество знаков в минуту: " + resultNumber);

        Assertions.assertTrue(resultNumber > minValue, "Актуальный результат был: " + resultNumber);

    }
}
