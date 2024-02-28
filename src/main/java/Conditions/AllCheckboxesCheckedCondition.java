package Conditions;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.commands.Commands;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;

public class AllCheckboxesCheckedCondition extends WebElementCondition {
    public AllCheckboxesCheckedCondition() {
        super("allCheckboxesChecked");

    }

    @Nonnull
    @Override
    public CheckResult check(Driver driver, WebElement element) {
        boolean allChecked = element.findElements(By.cssSelector("input[type='checkbox']"))
                .stream()
                .allMatch(WebElement::isSelected);

        return new CheckResult(allChecked, allChecked ? "all checkboxes are checked" : "some checkboxes are not checked");
    }
}