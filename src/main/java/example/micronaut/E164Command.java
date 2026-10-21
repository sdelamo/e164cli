package example.micronaut;

import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Set;

@Command(name = "e164",
        description = "Check whether a phone number is in E.164 format.",
        mixinStandardHelpOptions = true)
public class E164Command implements Runnable {

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
            System.out.printf("Phone number '%s' is valid E.164.%n", phone);
            return;
        }

        System.out.printf("Phone number '%s' is not valid E.164.%n", phone);
        violations.forEach(violation -> System.out.println(violation.getMessage()));
    }

    static class PhoneInput {
        @Parameters(index = "0", paramLabel = "PHONE", description = "Phone number to validate")
        String positional;

        @Option(names = {"-p", "--phone"}, paramLabel = "PHONE", description = "Phone number to validate")
        String option;
    }
}
