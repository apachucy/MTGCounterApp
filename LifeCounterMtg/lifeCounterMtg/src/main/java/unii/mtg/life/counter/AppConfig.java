package unii.mtg.life.counter;

import android.app.Application;

import unii.mtg.life.counter.sharedpreferences.SettingsPreferencesConst;
import unii.mtg.life.counter.sharedpreferences.SettingsPreferencesFactory;
import unii.mtg.life.counter.sharedpreferences.SettingsSharedPreferences;
import unii.mtg.life.counter.sharedpreferences.SharedPreferencesManager;


public class AppConfig extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        SettingsPreferencesFactory.configure(new SettingsSharedPreferences(
                new SharedPreferencesManager().getSharedPreferences(this,
                        SettingsPreferencesConst.SETTINGS_SHARED_PREFRENCES)));

    }


}
