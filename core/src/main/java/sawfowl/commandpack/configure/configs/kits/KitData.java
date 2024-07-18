package sawfowl.commandpack.configure.configs.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataQuery;
import org.spongepowered.api.data.persistence.Queries;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ContainerTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.menu.ClickType;
import org.spongepowered.api.item.inventory.menu.ClickTypes;
import org.spongepowered.api.item.inventory.menu.InventoryMenu;
import org.spongepowered.api.item.inventory.menu.handler.ClickHandler;
import org.spongepowered.api.item.inventory.menu.handler.CloseHandler;
import org.spongepowered.api.item.inventory.type.ViewableInventory;
import org.spongepowered.api.util.locale.Locales;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.plugin.PluginContainer;

import net.kyori.adventure.text.Component;

import sawfowl.commandpack.CommandPackInstance;
import sawfowl.commandpack.Permissions;
import sawfowl.commandpack.api.data.kits.GiveRule;
import sawfowl.commandpack.api.data.kits.Kit;
import sawfowl.commandpack.api.data.kits.KitPrice;
import sawfowl.localeapi.api.EnumLocales;
import sawfowl.localeapi.api.TextUtils;

@ConfigSerializable
public class KitData implements Kit {

	public KitData() {}

	public Builder builder() {
		return new Builder();
	}

	@Setting("ID")
	private String id;
	@Setting("LocalizedNames")
	private Map<String, String> localizedNames;
	@Setting("LocalizedLores")
	private Map<String, List<String>> localizedLores;
	@Setting("Items")
	private List<ItemStack> items = new ArrayList<>();
	@Setting("GiveRule")
	private String giveRule = GiveRule.DROP.getName();
	@Setting("Cooldown")
	private long cooldown = 0;
	@Setting("GiveLimit")
	private int limit = -1;
	@Setting("FirstTime")
	private boolean firstTime = false;
	@Setting("GiveOnJoin")
	private boolean giveOnJoin = false;
	@Setting("NeedPerm")
	private boolean needPerm = true;
	@Setting("ExecuteCommands")
	private List<String> executeCommands;
	@Setting("Price")
	private KitPriceData priceData;

	@Override
	public int contentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew()
				.set(DataQuery.of("id"), id)
				.set(DataQuery.of("LocalizedNames"), localizedNames)
				.set(DataQuery.of("LocalizedLores"), localizedLores)
				.set(DataQuery.of("Items"), items)
				.set(DataQuery.of("Cooldown"), cooldown)
				.set(DataQuery.of("GiveLimit"), limit)
				.set(DataQuery.of("FirstTime"), firstTime)
				.set(DataQuery.of("GiveOnJoin"), giveOnJoin)
				.set(Queries.CONTENT_VERSION, contentVersion());
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public Component getLocalizedName(Locale locale) {
		return localizedNames == null ? text(id) : text(localizedNames.getOrDefault(locale.toLanguageTag(), localizedNames.getOrDefault(Locales.DEFAULT.toLanguageTag(), id)));
	}

	@Override
	public List<Component> getLocalizedLore(Locale locale) {
		return localizedLores == null || (!localizedLores.containsKey(locale.toLanguageTag()) && !localizedLores.containsKey(Locales.DEFAULT.toLanguageTag()))? new ArrayList<>() : localizedLores.getOrDefault(locale.toLanguageTag(), localizedLores.get(Locales.DEFAULT.toLanguageTag())).stream().map(s -> text(s)).collect(Collectors.toList());
	}

	@Override
	public List<ItemStack> getContent() {
		return new ArrayList<ItemStack>(items);
	}

	@Override
	public GiveRule getGiveRule() {
		return GiveRule.getRule(giveRule);
	}

	@Override
	public long getCooldown() {
		return cooldown;
	}

	@Override
	public int getGiveLimit() {
		return limit;
	}

	@Override
	public boolean isFirstTime() {
		return firstTime;
	}

	@Override
	public boolean isGiveOnJoin() {
		return giveOnJoin;
	}

	@Override
	public boolean isNeedPerm() {
		return needPerm;
	}

	@Override
	public String permission() {
		return Permissions.getKitPermission(this);
	}

	@Override
	public Optional<List<String>> getExecuteCommands() {
		return Optional.ofNullable(executeCommands);
	}

	@Override
	public void addCommand(String command) {
		if(executeCommands == null) executeCommands = new ArrayList<String>();
		executeCommands.add(command);
	}

	@Override
	public void removeCommand(String command) {
		if(executeCommands != null) executeCommands.removeIf(c -> c.equalsIgnoreCase(command));
	}

	@Override
	public Optional<KitPrice> getKitPrice() {
		return CommandPackInstance.getInstance().getEconomy().isPresent() ? Optional.ofNullable(priceData) : Optional.empty();
	}

	@Override
	public InventoryMenu asMenu(PluginContainer container, ServerPlayer carrier, boolean readOnly) {
		InventoryMenu menu = ViewableInventory.builder().type(ContainerTypes.GENERIC_9X4).completeStructure().carrier(carrier).plugin(container).build().asMenu();
		menu.setTitle(getLocalizedName(carrier.locale()));
		menu.setReadOnly(readOnly);
		getContent().forEach(item -> {
			menu.inventory().offer(item);
		});
		if(readOnly) {
			menu.registerClick(new ClickHandler() {
				
				@Override
				public boolean handle(Cause cause, Container container, ClickType<?> clickType) {
					if(clickType == ClickTypes.CLICK_MIDDLE) {
						carrier.closeInventory();
						menu.inventory().clear();
						menu.unregisterAll();
					}
					return false;
				}
			});
		} else menu.registerClose(new CloseHandler() {
			@Override
			public void handle(Cause cause, Container container) {
				items.clear();
				menu.inventory().slots().stream().forEach(slot -> {
					if(slot.totalQuantity() > 0) items.add(slot.peek());
				});
				save();
				menu.inventory().clear();
				carrier.sendMessage(CommandPackInstance.getInstance().getLocales().getLocale(carrier.locale()).getCommands().getKits().getSaved(getLocalizedName(carrier.locale())));
				menu.unregisterAll();
			}
		});
		return menu;
	}

	@Override
	public void save() {
		CommandPackInstance.getInstance().getConfigManager().saveKit(this);
	}

	private Component text(String string) {
		if(isLegacyDecor(string)) {
			return TextUtils.deserializeLegacy(string);
		} else {
			return TextUtils.deserialize(string);
		}
	}

	private boolean isLegacyDecor(String string) {
		return string.indexOf('&') != -1 && !string.endsWith("&") && isStyleChar(string.charAt(string.indexOf("&") + 1));
	}

	private boolean isStyleChar(char ch) {
		return "0123456789abcdefklmnor".indexOf(ch) != -1;
	}

	public void setName(Locale locale, String name) {
		if(localizedNames == null) localizedNames = new HashMap<>();
		if(localizedNames.containsKey(locale.toLanguageTag())) localizedNames.remove(locale.toLanguageTag());
		if(name != null) localizedNames.put(locale.toLanguageTag(), name);
	}

	public void setLore(Locale locale, List<String> lore) {
		if(localizedLores == null) localizedLores = new HashMap<>();
		if(localizedLores.containsKey(locale.toLanguageTag())) localizedLores.remove(locale.toLanguageTag());
		if(lore != null) localizedLores.put(locale.toLanguageTag(), lore);
	}

	public void setRule(GiveRule rule) {
		giveRule = rule.getName();
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public void setGiveOnJoin(boolean giveOnJoin) {
		this.giveOnJoin = giveOnJoin;
	}

	public void setNeedPerm(boolean needPerm) {
		this.needPerm = needPerm;
	}

	public void setCommands(List<String> commands) {
		executeCommands = commands;
	}

	public void addCommands(String command) {
		if(executeCommands == null) executeCommands = new ArrayList<>();
		executeCommands.add(command);
	}

	public void setPrice(KitPrice price) {
		priceData = (KitPriceData) (price instanceof KitPriceData ? price : KitPrice.of(price.getCurrency(), price.getMoney()));
	}

	public class Builder implements Kit.Builder {

		@Override
		public Kit build() {
			return KitData.this;
		}

		@Override
		public Builder fromInventory(Inventory inventory) {
			items = new ArrayList<>();
			inventory.slots().forEach(slot -> {
				slot.get(Keys.SLOT_INDEX).ifPresent(index -> {
					if(index >= 0 && index <= 35 && slot.totalQuantity() > 0) items.add(slot.peek());
				});
			});
			return this;
		}

		@Override
		public Builder fromList(List<ItemStack> list) {
			KitData.this.items = list;
			return this;
		}

		@Override
		public Builder copyFrom(Kit kit) {
			if(kit instanceof KitData) {
				KitData data = (KitData) kit;
				KitData.this.id = data.id;
				KitData.this.localizedNames = data.localizedNames;
				KitData.this.localizedLores = data.localizedLores;
				KitData.this.items = data.items;
				KitData.this.giveRule = data.giveRule;
				KitData.this.cooldown = data.cooldown;
				KitData.this.limit = data.limit;
				KitData.this.firstTime = data.firstTime;
				KitData.this.giveOnJoin = data.giveOnJoin;
				KitData.this.executeCommands = data.executeCommands;
				KitData.this.priceData = data.priceData;
			} else {
				KitData.this.id = kit.id();
				EnumLocales.getLocales().forEach(locale -> {
					if(kit.getLocalizedName(locale) != null && !TextUtils.serializeLegacy(kit.getLocalizedName(locale)).equals(id)) setName(locale, TextUtils.serializeLegacy(kit.getLocalizedName(locale)));
					if(kit.getLocalizedLore(locale) != null && !kit.getLocalizedLore(locale).isEmpty()) setLore(locale, kit.getLocalizedLore(locale).stream().map(TextUtils::serializeLegacy).collect(Collectors.toList()));
				});
				KitData.this.items = kit.getContent();
				KitData.this.giveRule = kit.getGiveRule().getName();
				KitData.this.cooldown = kit.getCooldown();
				KitData.this.limit = kit.getGiveLimit();
				KitData.this.firstTime = kit.isFirstTime();
				KitData.this.giveOnJoin = kit.isGiveOnJoin();
				if(kit.getExecuteCommands().isPresent() && !kit.getExecuteCommands().get().isEmpty()) KitData.this.executeCommands = kit.getExecuteCommands().get();
				if(kit.getKitPrice().isPresent()) setPrice(kit.getKitPrice().get());
			}
			return this;
		}

		@Override
		public Builder cooldown(long cooldown) {
			KitData.this.cooldown = cooldown;
			return this;
		}

		@Override
		public Builder giveLimit(int giveLimit) {
			KitData.this.limit = giveLimit;
			return this;
		}

		@Override
		public Builder firstTime(boolean firstTime) {
			KitData.this.firstTime = firstTime;
			return this;
		}

		@Override
		public Builder id(String id) {
			KitData.this.id = id;
			return this;
		}

		@Override
		public Builder name(Locale locale, Component name) {
			KitData.this.setName(locale, TextUtils.serializeLegacy(name));
			return this;
		}

		@Override
		public Builder lore(Locale locale, List<Component> lore) {
			KitData.this.setLore(locale, lore.stream().map(TextUtils::serializeLegacy).collect(Collectors.toList()));
			return this;
		}

		@Override
		public Builder needPerm(boolean needPerm) {
			KitData.this.needPerm = needPerm;
			return this;
		}

		@Override
		public Builder executeCommands(List<String> commands) {
			setCommands(commands);
			return this;
		}

		@Override
		public Builder kitPrice(KitPrice price) {
			setPrice(price);
			return this;
		}
		
	}

}
