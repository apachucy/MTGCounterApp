package unii.mtg.life.counter.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager implements ISharedPrefererences{

	@Override
	public SharedPreferences getSharedPreferences(Context context, String name) {		
		return context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

}
