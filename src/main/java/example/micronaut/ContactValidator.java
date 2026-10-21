package example.micronaut;

import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Singleton
public class ContactValidator {
    public void validate(@NotNull @Valid Contact contact) {
        //no-op validation is done to the method parameter
    }
}
