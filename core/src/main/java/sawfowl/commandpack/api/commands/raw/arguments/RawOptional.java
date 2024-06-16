package sawfowl.commandpack.api.commands.raw.arguments;

/**
 * 
 * @param all - If true, the argument will be optional.
 * @param console - If true, the argument will be optional for the console. Parameter `all` has a higher priority.
 * 
 * @author SawFowl
 */
public record RawOptional(boolean all, boolean console) {

	public static RawOptional optional() {
		return new RawOptional(true, true);
	}

	public static RawOptional player() {
		return new RawOptional(true, false);
	}

	public static RawOptional notOptional() {
		return new RawOptional(false, false);
	}

}
