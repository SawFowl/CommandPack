package sawfowl.commandpack.api.commands.parameterized;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;
import sawfowl.localeapi.api.ComponentSupplier;

/**
 * Interface for additional customization of command arguments.
 * 
 * @author SawFowl
 */
public interface ParameterSettings extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static ParameterSettings of(Value<?> value, boolean optionalforConsole, ComponentSupplier supplier) {
		return builder().value(value).optionalforConsole(optionalforConsole).componentSupplier(supplier).build();
	}

	static ParameterSettings of(Value<?> value, boolean optional, boolean optionalforConsole, ComponentSupplier supplier) {
		return builder().value(value).setOptional(optional).optionalforConsole(optionalforConsole).componentSupplier(supplier).build();
	}

	boolean containsIn(CommandContext context);

	String getKey();

	Value<?> getParameterUnknownType();

	<T> Optional<T> getParameterValue(@NonNull Class<T> object, CommandContext context);

	boolean isOptional();

	boolean isOptionalForConsole();

	ComponentSupplier getComponentSupplier();

	int getPosition();

	interface Builder extends AbstractBuilder<ParameterSettings>, org.spongepowered.api.util.Builder<ParameterSettings, Builder> {

		Builder value(Value<?> value);

		Builder optionalforConsole(boolean optional);

		Builder setOptional(boolean optional);

		Builder componentSupplier(ComponentSupplier supplier);

		Builder position(int position);

	}

}
