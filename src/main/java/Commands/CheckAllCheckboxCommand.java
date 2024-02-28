package Commands;

import com.codeborne.selenide.Command;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.WebElementSource;

import javax.annotation.Nullable;

import javax.annotation.Nullable;
import java.util.List;

public class CheckAllCheckboxCommand implements Command<Void> {
    @Nullable
    @Override
    public Void execute(SelenideElement proxy, WebElementSource locator, @Nullable Object[] args) {
        ElementsCollection checkboxes = proxy.findAll("input[type='checkbox']");
        for (int i = 0; i < checkboxes.size(); i++) {
            SelenideElement checkbox = checkboxes.get(i);

            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }

        return null;
    }
}