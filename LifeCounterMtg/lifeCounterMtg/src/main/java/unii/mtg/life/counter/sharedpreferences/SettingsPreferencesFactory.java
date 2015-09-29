package unii.mtg.life.counter.sharedpreferences;

public class SettingsPreferencesFactory {

	private static ISettings sInstance;

	public static void configure(ISettings instance) {
		sInstance = instance;
	}

	public static ISettings getInstance() {
		return sInstance;
	}
}
