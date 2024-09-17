package sawfowl.commandpack.api.data.kits;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Rules for giving the kit.
 * 
 * @author SawFowl
 */
public enum GiveRule {

	IGNORE_FULL_INVENTORY {

		@Override
		public String getName() {
			return "IgnoreFullInventory";
		}
		
	},
	USE_ENDECHEST {

		@Override
		public String getName() {
			return "UseEnderchest";
		}
		
	},
	USE_BACKPACK {

		@Override
		public String getName() {
			return "UseBackpack";
		}
		
	},
	DROP {

		@Override
		public String getName() {
			return "Drop";
		}
		
	},
	MESSAGE_IF_INVENTORY_FULL{

		@Override
		public String getName() {
			return "MessageIfInventoryFull";
		}
	
	};

	public abstract String getName();

	public static GiveRule getRule(String name) {
		return name == null ? DROP : Stream.of(GiveRule.values()).filter(r -> r.getName().equalsIgnoreCase(name)).findFirst().orElse(DROP);
	}

	public static List<String> getAllRules() {
		return Stream.of(GiveRule.values()).map(GiveRule::getName).collect(Collectors.toList());
	}

}
