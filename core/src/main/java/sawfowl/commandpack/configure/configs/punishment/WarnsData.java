package sawfowl.commandpack.configure.configs.punishment;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.api.data.punishment.Warn;
import sawfowl.commandpack.api.data.punishment.Warns;

@ConfigSerializable
public class WarnsData implements Warns {

	public WarnsData(){}

	public Builder builder() {
		return new Builder();
	}

	@Setting("UUID")
	private UUID uuid;
	@Setting("Name")
	private String name;
	@Setting("Warns")
	private Set<WarnData> warns = new HashSet<WarnData>().stream().sorted(Comparator.comparing(WarnData::getCreated)).collect(Collectors.toSet());
	@Setting("InAllTime")
	int inAllTime = 0;

	@Override
	public int inAllTime() {
		return inAllTime;
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
	public List<Warn> getWarns() {
		checkExpired();
		return warns.stream().map(w -> Warn.builder().from(w)).sorted(Comparator.comparing(Warn::getCreated)).collect(Collectors.toList());
	}

	@Override
	public int totalWarns() {
		checkExpired();
		return warns.size();
	}

	@Override
	public void addWarn(Warn warn) {
		warns.add((WarnData) (warn instanceof WarnData ? warn : Warn.builder().from(warn)));
		inAllTime++;
		save();
	}

	@Override
	public void removeWarn(Warn warn) {
		WarnData data = (WarnData) (warn instanceof WarnData ? warn : Warn.builder().from(warn));
		if(warns.contains(data)) warns.remove(data);
		save();
	}

	@Override
	public void removeWarn(String created) {
		warns.removeIf(warn -> warn.getCreatedTimeString().equals(created));
	}

	@Override
	public void checkExpired() {
		int size = warns.size();
		warns.removeIf(WarnData::isExpired);
		if(warns.size() < size) save();
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
	public void clear() {
		warns.clear();
		save();
	}

	private void save() {
		CommandPackInstance.getInstance().getPunishmentService().removeWarns(uuid);
		CommandPackInstance.getInstance().getPunishmentService().addWarns(this);
	}

	private class Builder implements Warns.Builder {

		@Override
		public Warns build() {
			return WarnsData.this;
		}

		@Override
		public Warns.Builder target(ServerPlayer value) {
			name = value.name();
			uuid = value.uniqueId();
			return this;
		}

		@Override
		public Warns.Builder target(User value) {
			name = value.name();
			uuid = value.uniqueId();
			return this;
		}

		@Override
		public Warns.Builder warn(Warn value) {
			warns.add((WarnData) (value instanceof WarnData ? value : Warn.builder().from(value)));
			inAllTime++;
			return this;
		}

		@Override
		public WarnsData from(Warns warns) {
			uuid = warns.getUniqueId();
			name = warns.getName();
			WarnsData.this.warns = warns.getWarns().stream().map(warn -> (WarnData) (warn instanceof WarnData ? warn : Warn.builder().from(warn))).collect(Collectors.toSet());
			inAllTime = warns.inAllTime();
			return WarnsData.this;
		}
		
	}

}
