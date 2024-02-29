package Conditions;

import com.codeborne.selenide.WebElementCondition;


public class CustomCondition {
    public static WebElementCondition allChecked = new AllCheckboxesCheckedCondition();
}
