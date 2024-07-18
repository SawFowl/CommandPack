package sawfowl.commandpack.api.commands.raw.arguments;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode.Argument;

/**
 * 
 * @param key - Argument {@link String} key
 * @param argumentNodeType - {@link CommandTreeNode} type
 * @param cursor - The number of the argument in the array of arguments input.
 * @param permission - The permission required to use the argument. Can be `null`.
 * @param requiredArgs - An enumeration of the arguments that are required to use this argument.
 * 
 * @author SawFowl
 */
public record RawArgumentData<C extends CommandTreeNode<C>>(String key, Argument<C> argumentNodeType, int cursor, @Nullable String permission, @Nullable RawRequiredArgs requiredArgs) {}
