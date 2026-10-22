package example.micronaut;

import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Help.Ansi.IStyle;
import picocli.CommandLine.Help.Ansi.Style;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Command(name = "e164",
        description = "Check whether a phone number is in E.164 format.",
        mixinStandardHelpOptions = true)
public class E164Command implements Runnable {

    private static final Help.ColorScheme COLOR_SCHEME = Help.defaultColorScheme(Ansi.AUTO);

    @ArgGroup(exclusive = true, multiplicity = "1")
    PhoneInput phoneInput;

    @Inject
    Validator validator;

    static void main(String[] args) throws Exception {
        PicocliRunner.run(E164Command.class, args);
    }

    @Override
    public void run() {
        String phone = phoneInput.positional != null ? phoneInput.positional : phoneInput.option;
        Set<ConstraintViolation<Contact>> violations = validator.validate(new Contact(phone));
        if (violations.isEmpty()) {
            printColored("Phone number '" + phone + "' is valid E.164.", Style.fg_green);
            return;
        }

        String message = "Phone number '" + phone + "' is not valid E.164."
                + System.lineSeparator()
                + violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(System.lineSeparator()));
        printColored(message, Style.fg_red);
    }

    private static void printColored(String message, IStyle color) {
        System.out.println(COLOR_SCHEME.apply(message, List.of(color)));
    }

    static class PhoneInput {
        @Parameters(index = "0", paramLabel = "PHONE", description = "Phone number to validate")
        String positional;

        @Option(names = {"-p", "--phone"}, paramLabel = "PHONE", description = "Phone number to validate")
        String option;
    }
}
