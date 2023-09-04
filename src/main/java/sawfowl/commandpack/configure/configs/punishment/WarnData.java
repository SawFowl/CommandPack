package sawfowl.commandpack.configure.configs.punishment;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.api.data.punishment.Warn;
import sawfowl.commandpack.utils.TimeConverter;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class WarnData implements Warn {

	public WarnData(){}

	public Builder builder() {
		return new Builder();
	}

	@Setting("Created")
	private String created;
	@Setting("Expiration")
	private String expiration;
	@Setting("Source")
	private String source;
	@Setting("Reason")
	private String reason;

	@Override
	public Instant getCreated() {
		return TimeConverter.fromString(created);
	}

	@Override
	public String getCreatedTimeString() {
		return created;
	}

	@Override
	public long getCreationTime() {
		return getCreated().toEpochMilli();
	}

	@Override
	public boolean isIndefinitely() {
		return expiration == null || TimeConverter.fromString(expiration).toEpochMilli() <= 0;
	}

	@Override
	public Optional<Instant> getExpiration() {
		return getExpirationTimeString().map(e -> TimeConverter.fromString(e));
	}

	@Override
	public Optional<String> getExpirationTimeString() {
		return Optional.ofNullable(expiration);
	}

	@Override
	public long getExpirationTime() {
		return expiration == null ? 0 : TimeConverter.fromString(expiration).toEpochMilli();
	}

	@Override
	public Optional<Component> getSource() {
		return Optional.ofNullable(source).map(s -> TextUtils.deserializeJson(s));
	}

	@Override
	public Optional<Component> getReason() {
		return Optional.ofNullable(reason).map(r -> TextUtils.deserializeJson(r));
	}

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	@Override
	public int hashCode() {
		return Objects.hash(created);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || getClass() != obj.getClass()) return false;
		if(this == obj) return true;
		return obj instanceof WarnData && created == ((WarnData) obj).created && source.equals(((WarnData) obj).source);
	}

	@Override
	public boolean isExpired() {
		return expiration != null && getExpirationTime() != 0 && getExpirationTime() <= System.currentTimeMillis();
	}

	private class Builder implements Warn.Builder {

		@Override
		public @NotNull Warn build() {
			if(created == null) created = TimeConverter.toString(Instant.now());
			return WarnData.this;
		}

		@Override
		public Warn.Builder created(Instant value) {
			created = TimeConverter.toString(value);
			return this;
		}

		@Override
		public Warn.Builder source(Component name) {
			source = TextUtils.serializeJson(name);
			return this;
		}

		@Override
		public Warn.Builder source(ServerPlayer value) {
			source = TextUtils.serializeJson(value.get(Keys.DISPLAY_NAME).orElse(Component.text(value.name())));
			return this;
		}

		@Override
		public Warn.Builder expiration(Instant value) {
			expiration = TimeConverter.toString(value);
			return this;
		}

		@Override
		public Warn.Builder reason(Component value) {
			reason = TextUtils.serializeJson(value);
			return this;
		}

		@Override
		public Warn from(Warn warn) {
			created = TimeConverter.toString(warn.getCreated());
			warn.getExpiration().ifPresent(e -> {
				expiration = TimeConverter.toString(e);
			});
			warn.getReason().ifPresent(r -> {
				reason = TextUtils.serializeJson(r);
			});
			warn.getSource().ifPresent(s -> {
				source = TextUtils.serializeJson(s);
			});
			return WarnData.this;
		}
		
	}

}
