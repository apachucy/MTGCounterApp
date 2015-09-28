package unii.mtg.life.counter.view.fragments;

import java.util.ArrayList;
import java.util.List;

import unii.mtg.life.counter.R;
import unii.mtg.life.counter.validation.ValidationHelper;
import unii.mtg.life.counter.view.OnSetValueListener;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * DialogFragment with spinner and two buttons
 * 
 * @author Arkadiusz Pachucy
 * 
 */
public class CustomDialogSpinnerFragment extends DialogFragment implements
		OnSetValueListener {

	private final static String BUNDLE_TITLE = CustomDialogSpinnerFragment.class
			.getName() + "BUNDLE_TITLE";
	private final static String BUNDLE_MESSAGE = CustomDialogSpinnerFragment.class
			.getName() + "BUNDLE_MESSAGE";
	private final static String BUNDLE_LIST = CustomDialogSpinnerFragment.class
			.getName() + "BUNDLE_LIST";

	private final static String BUNDLE_BUTTON_NAME_LEFT = CustomDialogSpinnerFragment.class
			.getName() + "BUNDLE_BUTTON_NAME_LEFT";
	private final static String BUNDLE_BUTTON_NAME_RIGHT = CustomDialogSpinnerFragment.class
			.getName() + "BUNDLE_BUTTON_NAME_RIGHT";
	private static OnClickListener mOnClickDismissListener;
	private static OnClickListener mOnClickAcceptListener;

	private TextView mTitleTextView;
	private TextView mMessageTextView;
	private TextView mMessageBelowSpinnerTextView;
	private Spinner mSpinner;

	private String mTitle;
	private List<String> mSimpleList;
	private String mMessage;

	private String mLeftButtonName;
	private String mRightButtonName;

	public static CustomDialogSpinnerFragment newInstance(String title,
			String message, ArrayList<String> simpleList,
			String leftButtonName, String rightButtonName,
			OnClickListener possitive, OnClickListener negative) {
		CustomDialogSpinnerFragment dialogFragment = null;
		// no text provided return null
		if (ValidationHelper.isTextEmpty(title)
				|| ValidationHelper.isTextEmpty(message) || simpleList == null
				|| simpleList.isEmpty()
				|| ValidationHelper.isTextEmpty(leftButtonName)
				|| ValidationHelper.isTextEmpty(rightButtonName)) {
			return dialogFragment;
		}

		if (possitive != null) {
			mOnClickAcceptListener = possitive;
		}

		if (negative != null) {
			mOnClickDismissListener = negative;
		}

		dialogFragment = new CustomDialogSpinnerFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString(BUNDLE_TITLE, title);
		args.putString(BUNDLE_MESSAGE, message);
		args.putStringArrayList(BUNDLE_LIST, simpleList);
		args.putString(BUNDLE_BUTTON_NAME_LEFT, leftButtonName);
		args.putString(BUNDLE_BUTTON_NAME_RIGHT, rightButtonName);
		dialogFragment.setArguments(args);
		// request a window without the title
		dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		return dialogFragment;
	}

	public void setDialogData(String title, String message,
			ArrayList<String> simpleList, String leftButtonName,
			String rightButtonName, OnClickListener possitive,
			OnClickListener negative) {

		if (ValidationHelper.isTextEmpty(title)
				|| ValidationHelper.isTextEmpty(message) || simpleList == null
				|| simpleList.isEmpty()) {
			return;
		}

		Bundle args = new Bundle();
		args.putString(BUNDLE_TITLE, title);
		args.putString(BUNDLE_MESSAGE, message);
		args.putStringArrayList(BUNDLE_LIST, simpleList);
		args.putString(BUNDLE_BUTTON_NAME_LEFT, leftButtonName);
		args.putString(BUNDLE_BUTTON_NAME_RIGHT, rightButtonName);
		this.setArguments(args);

		if (possitive != null) {
			mOnClickAcceptListener = possitive;
		}

		if (negative != null) {
			mOnClickDismissListener = negative;
		}
		mTitle = title;
		mMessage = message;
		mSimpleList = simpleList;
		mLeftButtonName = leftButtonName;
		mRightButtonName = rightButtonName;
		// first element should be selected
		mSpinner.setSelection(0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTitle = getArguments().getString(BUNDLE_TITLE);
		mMessage = getArguments().getString(BUNDLE_MESSAGE);
		mSimpleList = getArguments().getStringArrayList(BUNDLE_LIST);
		mLeftButtonName = getArguments().getString(BUNDLE_BUTTON_NAME_LEFT);
		mRightButtonName = getArguments().getString(BUNDLE_BUTTON_NAME_RIGHT);
	}

	public void setOnDismissListener(OnClickListener onClickListener) {
		mOnClickDismissListener = onClickListener;
	}

	public void setOnAcceptListener(OnClickListener onClickListener) {
		mOnClickAcceptListener = onClickListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dialog_spinner, container,
				false);

		mTitleTextView = (TextView) v
				.findViewById(R.id.dialog_dice_titleTextView);
		mMessageTextView = (TextView) v
				.findViewById(R.id.dialog_dice_messageTextView);
		mMessageBelowSpinnerTextView = (TextView) v
				.findViewById(R.id.dialog_dice_diceTextView);

		mSpinner = (Spinner) v.findViewById(R.id.dialog_dice_rollDiceSpinner);
		SpinnerAdapter adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.item_spinner_fragment, mSimpleList);
		Button dismissButton = (Button) v
				.findViewById(R.id.dialog_dice_dismissButton);
		Button acceptButton = (Button) v
				.findViewById(R.id.dialog_dice_rollButton);

		mTitleTextView.setText(mTitle);
		mMessageTextView.setText(mMessage);
		dismissButton.setText(mRightButtonName);
		acceptButton.setText(mLeftButtonName);
		if (mOnClickDismissListener == null) {
			mOnClickDismissListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			};
		}
		if (mOnClickAcceptListener == null) {
			mOnClickAcceptListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			};
		}
		acceptButton.setOnClickListener(mOnClickAcceptListener);
		dismissButton.setOnClickListener(mOnClickDismissListener);
		mSpinner.setAdapter(adapter);
		return v;
	}

	/**
	 * @overload
	 * @param manager
	 * @param tag
	 * @param onClick
	 *            action when click on button
	 */
	public void show(FragmentManager manager, String tag,
			OnClickListener onAcceptAction, OnClickListener onDismissAction) {
		mOnClickDismissListener = onDismissAction;
		mOnClickAcceptListener = onAcceptAction;
		super.show(manager, tag);
	}

	public String getSpinnerValue() {
		if (mSpinner == null) {
			return null;
		}
		return (String) mSpinner.getSelectedItem();
	}

	@Override
	public TextView getRollTextView() {

		return mMessageBelowSpinnerTextView;
	}
}
