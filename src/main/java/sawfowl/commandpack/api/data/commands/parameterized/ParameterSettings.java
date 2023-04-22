package sawfowl.commandpack.api.data.commands.parameterized;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.data.persistence.DataSerializable;

import net.kyori.adventure.builder.AbstractBuilder;

public interface ParameterSettings extends DataSerializable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	static ParameterSettings of(Value<?> value, boolean optionalforConsole, Object... localeTextPath) {
		return builder().value(value).optionalforConsole(optionalforConsole).localeTextPath(localeTextPath).build();
	}

	static ParameterSettings of(Value<?> value, boolean optional, boolean optionalforConsole, Object... localeTextPath) {
		return builder().value(value).setOptional(optional).optionalforConsole(optionalforConsole).localeTextPath(localeTextPath).build();
	}

	boolean containsIn(CommandContext context);

	String getKey();

	Value<?> getParameterUnknownType();

	<T> Optional<T> getParameterValue(@NonNull Class<T> object, CommandContext context);

	boolean isOptional();

	boolean isOptionalForConsole();

	Object[] getPath();

	int getPosition();

	interface Builder extends AbstractBuilder<ParameterSettings>, org.spongepowered.api.util.Builder<ParameterSettings, Builder> {

		Builder value(Value<?> value);

		Builder optionalforConsole(boolean optional);

		Builder setOptional(boolean optional);

		Builder localeTextPath(Object[] path);

		Builder position(int position);

	}

}
