package sawfowl.commandpack.api.data.commands.parameterized;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter.Value;
import org.spongepowered.api.data.DataHolderBuilder;
import org.spongepowered.api.data.SerializableDataHolder;

public interface ParameterSettings extends SerializableDataHolder.Mutable {

	static Builder builder() {
		return Sponge.game().builderProvider().provide(Builder.class);
	}

	String getKey();

	Value<?> getParameterUnknownType();

	<T> Optional<T> getParameterValue(@NonNull Class<T> object, CommandContext context);

	boolean isOptional();

	boolean isOptionalForConsole();

	Object[] getPath();

	interface Builder extends DataHolderBuilder.Mutable<ParameterSettings, Builder> {

		Builder value(Value<?> value);

		Builder optionalforConsole(boolean optional);

		Builder localeTextPath(Object path[]);

		@NotNull ParameterSettings build();

	}

}
