package sawfowl.commandpack.utils;

import java.util.stream.Stream;

public enum StorageType {

	FILE {
		@Override
		public String typeName() {
			return "File";
		}
	},
	H2 {
		@Override
		public String typeName() {
			return "H2";
		}
		
	},
	MYSQL {
		@Override
		public String typeName() {
			return "MySql";
		}
		
	};

	public abstract String typeName();

	public static StorageType getType(String name) {
		return Stream.of(StorageType.values()).filter(t -> t.typeName().equals(name)).findFirst().orElse(FILE);
	}

}
