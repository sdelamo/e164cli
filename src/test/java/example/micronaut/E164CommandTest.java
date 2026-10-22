package example.micronaut;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class E164CommandTest {

    @Test
    void validPositionalPhoneIsReportedAsValid() {
        String output = runCommand("+14155552671");

        assertTrue(output.contains("Phone number '+14155552671' is valid E.164."));
    }

    @Test
    void invalidPhoneOptionIncludesValidationMessage() {
        String output = runCommand("--phone", "+1-4155552671");

        assertTrue(output.contains("Phone number '+1-4155552671' is not valid E.164."));
        assertTrue(output.contains("must be a phone in E.164 format"));
    }

    private String runCommand(String... args) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream capturedOut = new PrintStream(output)) {
            System.setOut(capturedOut);
            try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
                PicocliRunner.run(E164Command.class, ctx, args);
            }
        } finally {
            System.setOut(originalOut);
        }

        return output.toString();
    }
}
