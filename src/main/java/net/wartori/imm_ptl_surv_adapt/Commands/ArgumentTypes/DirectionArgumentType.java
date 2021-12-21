package net.wartori.imm_ptl_surv_adapt.Commands.ArgumentTypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Direction;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DirectionArgumentType implements ArgumentType<Direction> {
    public static DirectionArgumentType direction() {
        return new DirectionArgumentType();
    }

    public static <S> Direction getDirection(CommandContext<S> context, String name) {
        return context.getArgument(name, Direction.class);
    }

    public static final List<String> EXAMPLES = Arrays.asList("UP", "DOWN", "EAST", "WEST", "NORTH", "SOUTH");
    @Override
    public Direction parse(StringReader reader) throws CommandSyntaxException {
        int argBeginning = reader.getCursor();
        if (!reader.canRead()) {
            reader.skip();
        }
        while (reader.canRead() && reader.peek() != ' ') { // peek provides the character at the current cursor position.
            reader.skip(); // Tells the StringReader to move it's cursor to the next position.
        }
        String directionString = reader.getString().substring(argBeginning, reader.getCursor());
        try {
            Direction direction = Direction.valueOf(directionString.toUpperCase());
            return direction;
        } catch (Exception exception) {
            throw new SimpleCommandExceptionType(new LiteralText(exception.getMessage())).createWithContext(reader);
//            return Direction.EAST;
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> suggestions = new ArrayList<>(Collections.emptyList());
        EXAMPLES.forEach(s -> suggestions.add(s.toLowerCase()));
        return CommandSource.suggestMatching(suggestions, builder);
    }
}
