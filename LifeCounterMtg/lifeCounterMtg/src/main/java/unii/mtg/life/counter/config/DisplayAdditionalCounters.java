package unii.mtg.life.counter.config;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import unii.mtg.life.counter.R;

public class DisplayAdditionalCounters {
    private boolean isVisiblePoisonCounter;
    private boolean isVisibleEnergyCounter;
    private Context mContext;

    public DisplayAdditionalCounters(Context context) {
        isVisibleEnergyCounter = false;
        isVisiblePoisonCounter = false;
        mContext = context;
    }

    public boolean isVisiblePoisonCounter() {
        return isVisiblePoisonCounter;
    }

    public void setVisiblePoisonCounter(boolean visiblePoisonCounter) {
        isVisiblePoisonCounter = visiblePoisonCounter;
    }

    public boolean isVisibleEnergyCounter() {
        return isVisibleEnergyCounter;
    }

    public void setVisibleEnergyCounter(boolean visibleEnergyCounter) {
        isVisibleEnergyCounter = visibleEnergyCounter;
    }

    public Integer[] getSelectedCounters() {
        if (!isVisiblePoisonCounter && !isVisibleEnergyCounter) {
            return null;
        }
        ArrayList<Integer> itemSelected = new ArrayList<>();
        if (isVisiblePoisonCounter) {
            itemSelected.add(0);
        }
        if (isVisibleEnergyCounter) {
            itemSelected.add(1);
        }
        Integer[] itemSelectedInArray = new Integer[itemSelected.size()];
        return itemSelected.toArray(itemSelectedInArray);
    }

    public List<String> countersToString() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(mContext.getString(R.string.counters_poison));
        arrayList.add(mContext.getString(R.string.counters_energy));
        return arrayList;
    }
}
