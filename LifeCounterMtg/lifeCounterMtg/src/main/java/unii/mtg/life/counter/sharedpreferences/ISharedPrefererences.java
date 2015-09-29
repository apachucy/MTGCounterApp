package unii.mtg.life.counter.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public interface ISharedPrefererences {

	
public SharedPreferences getSharedPreferences(Context context, String name);
}
