package unii.mtg.life.counter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;


public class BaseActivity extends AppCompatActivity {


    protected void showInputDigitsDialog(Context context, String title, String content, String hint, String lastValue, MaterialDialog.InputCallback inputCallback) {
        new MaterialDialog.Builder(context).
                title(title).content(content).inputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED)
                .backgroundColorRes(R.color.windowBackground).input(hint, lastValue, inputCallback).
                show();
    }

    protected void showInputTextDialog(Context context, String title, String content, String hint, String lastValue, MaterialDialog.InputCallback inputCallback) {
        new MaterialDialog.Builder(context).
                title(title).content(content).inputType(InputType.TYPE_CLASS_TEXT)
                .backgroundColorRes(R.color.windowBackground).input(hint, lastValue, inputCallback).
                show();
    }

    protected void showInfoDialog(Context context, String title, String content, String positiveButtonText) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content).backgroundColorRes(R.color.windowBackground)
                .positiveText(positiveButtonText)
                .show();
    }

    protected void showRadioButtonListDialog(Context context, String title, List<String> list, String buttonPositive, String buttonNegative,
                                             int defaultSelectedValue, MaterialDialog.ListCallbackSingleChoice listCallbackSingleChoice) {
        new MaterialDialog.Builder(context)
                .title(title).items(list).itemsCallbackSingleChoice(defaultSelectedValue, listCallbackSingleChoice)
                .positiveText(buttonPositive).backgroundColorRes(R.color.windowBackground).negativeText(buttonNegative)
                .show();
    }

    protected void showMultipleChooserDialog(Context context, String title, List<String> list, Integer[] selectedItems, String buttonPositive, MaterialDialog.ListCallbackMultiChoice listCallbackMultiChoice) {
        new MaterialDialog.Builder(context)
                .title(title)
                .items(list)
                .itemsCallbackMultiChoice(selectedItems, listCallbackMultiChoice)
                .positiveText(buttonPositive).backgroundColorRes(R.color.windowBackground)
                .show();
    }
}
