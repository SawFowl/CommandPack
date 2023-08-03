package sawfowl.commandpack.configure.configs.punishment;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.api.data.punishment.Mute;
import sawfowl.commandpack.utils.TimeConverter;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class MuteData implements Mute {

	public MuteData(){}

	public Builder builder() {
		return new Builder();
	}

	@Setting("Created")
	private String created;
	@Setting("UUID")
	private UUID uuid;
	@Setting("Name")
	private String name;
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
	public boolean isIndefinitely() {
		return expiration == null || TimeConverter.fromString(expiration).toEpochMilli() <= 0;
	}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public String getName() {
		return name;
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
	public Optional<Component> getSource() {
		return Optional.ofNullable(source).map(s -> TextUtils.deserializeJson(s));
	}

	@Override
	public Optional<Component> getReason() {
		return Optional.ofNullable(reason).map(r -> TextUtils.deserializeJson(r));
	}

	@Override
	public boolean isExpired() {
		return expiration != null && TimeConverter.fromString(expiration).toEpochMilli() <= Instant.now().getEpochSecond();
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
	public String toString() {
		return "Mute [CreationDate=" + created + ", UniqueId=" + uuid + ", Name=" + name + ", Expired=" + expiration + ", Source=" + source + ", Reason=" + reason + "]";
	}

	private class Builder implements Mute.Builder {

		@Override
		public @NotNull Mute build() {
			if(created == null) created = TimeConverter.toString(Instant.now());
			return MuteData.this;
		}

		@Override
		public Mute.Builder created(Instant value) {
			created = TimeConverter.toString(value);
			return this;
		}

		@Override
		public Mute.Builder target(ServerPlayer value) {
			uuid = value.uniqueId();
			name = value.name();
			return this;
		}

		@Override
		public Mute.Builder target(User value) {
			uuid = value.uniqueId();
			name = value.name();
			return this;
		}

		@Override
		public Mute.Builder target(UUID uuid, String name) {
			MuteData.this.uuid = uuid;
			MuteData.this.name = name;
			return this;
		}

		@Override
		public Mute.Builder source(Component name) {
			source = TextUtils.serializeJson(name);
			return this;
		}

		@Override
		public Mute.Builder source(ServerPlayer value) {
			source = TextUtils.serializeJson(value.get(Keys.DISPLAY_NAME).orElse(Component.text(value.name())));
			return this;
		}

		@Override
		public Mute.Builder expiration(Instant value) {
			expiration = TimeConverter.toString(value);
			return this;
		}

		@Override
		public Mute.Builder reason(Component value) {
			reason = TextUtils.serializeJson(value);
			return this;
		}

		@Override
		public Mute from(Mute mute) {
			created = TimeConverter.toString(mute.getCreated());
			uuid = mute.getUniqueId();
			name = mute.getName();
			mute.getExpiration().ifPresent(e -> {
				expiration = TimeConverter.toString(e);
			});
			mute.getReason().ifPresent(r -> {
				reason = TextUtils.serializeJson(r);
			});
			mute.getSource().ifPresent(s -> {
				source = TextUtils.serializeJson(s);
			});
			uuid = mute.getUniqueId();
			return MuteData.this;
		}
		
	}

}
