package Commands;
import com.codeborne.selenide.Command;

public class CustomCommand {
    public static Command checkAllCheckboxes = new CheckAllCheckboxCommand();
}
