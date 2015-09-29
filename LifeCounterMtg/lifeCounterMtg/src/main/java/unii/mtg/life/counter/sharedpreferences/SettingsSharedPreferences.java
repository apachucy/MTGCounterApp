package unii.mtg.life.counter.sharedpreferences;

import android.content.SharedPreferences;

import unii.mtg.life.counter.config.BaseConfig;


public class SettingsSharedPreferences implements ISettings {
	private SharedPreferences mSharedPreferences;

	public SettingsSharedPreferences(SharedPreferences shPref) {
		mSharedPreferences = shPref;
	}


	@Override
	public boolean isFirstRun() {
		
		return mSharedPreferences.getBoolean(
				SettingsPreferencesConst.FIRST_RUN,
				BaseConfig.DEFAULT_FIRST_RUN);
	}

	@Override
	public void setFirstRun(boolean isFirstRun) {
		mSharedPreferences.edit().putBoolean(
				SettingsPreferencesConst.FIRST_RUN,
				isFirstRun).commit();
		
	}


}
