package sawfowl.commandpack.api.commands.raw.arguments;

/**
 * 
 * @param byId - Listing the required arguments by their id. Can be `null`.
 * @param byKey - Listing the required arguments by their keys. Can be `null`.
 * 
 * @author SawFowl
 */
public record RawRequiredArgs(Integer[] byId, String[] byKey) {}
