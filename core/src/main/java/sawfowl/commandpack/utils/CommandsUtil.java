package sawfowl.commandpack.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandsUtil {

	public static final List<?> EMPTY_VARIANTS = Collections.unmodifiableList(new ArrayList<>());

	@SuppressWarnings("unchecked")
	public static <T> List<T> getEmptyList() {
		return (List<T>) EMPTY_VARIANTS;
	}

}
