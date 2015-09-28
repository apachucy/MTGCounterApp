package unii.mtg.life.counter.view.fragments;

import unii.mtg.life.counter.R;
import unii.mtg.life.counter.validation.ValidationHelper;
import unii.mtg.life.counter.view.EditTextDialogFragmentActionListener;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Dialog fragment with editText and two buttons
 * 
 * @author Arkadiusz Pachucy
 * 
 */
public class CustomDialogEditTextFragment extends DialogFragment implements
		EditTextDialogFragmentActionListener {
	private final static String BUNDLE_TITLE = CustomDialogEditTextFragment.class
			.getName() + "BUNDLE_TITLE";
	private final static String BUNDLE_MESSAGE = CustomDialogEditTextFragment.class
			.getName() + "BUNDLE_MESSAGE";

	private final static String BUNDLE_BUTTON_NAME_LEFT = CustomDialogEditTextFragment.class
			.getName() + "BUNDLE_BUTTON_NAME_LEFT";
	private final static String BUNDLE_BUTTON_NAME_RIGHT = CustomDialogEditTextFragment.class
			.getName() + "BUNDLE_BUTTON_NAME_RIGHT";
	private final static String BUNDLE_EDIT_TEXT_TEXT = CustomDialogEditTextFragment.class
			.getName() + "EDIT_TEXT_TEXT";
	private static OnClickListener mOnClickDismissListener;
	private static OnClickListener mOnClickAcceptListener;

	private TextView mTitleTextView;
	private TextView mMessageTextView;

	private String mEditMessage;
	private String mTitle;
	private String mMessage;

	private String mLeftButtonName;
	private String mRightButtonName;

	private EditText mEditText;

	public static CustomDialogEditTextFragment newInstance(String title,
			String message, String textEditText, String leftButtonName,
			String rightButtonName, OnClickListener possitive,
			OnClickListener negative) {
		CustomDialogEditTextFragment dialogFragment = null;
		// no text provided return null
		if (ValidationHelper.isTextEmpty(title)
				|| ValidationHelper.isTextEmpty(message)
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

		dialogFragment = new CustomDialogEditTextFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString(BUNDLE_TITLE, title);
		args.putString(BUNDLE_MESSAGE, message);
		args.putString(BUNDLE_BUTTON_NAME_LEFT, leftButtonName);
		args.putString(BUNDLE_BUTTON_NAME_RIGHT, rightButtonName);
		args.putString(BUNDLE_EDIT_TEXT_TEXT, textEditText);
		dialogFragment.setArguments(args);
		// request a window without the title
		dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
		return dialogFragment;
	}

	public void setDialogData(String title, String message,
			String textEditText, String leftButtonName, String rightButtonName,
			OnClickListener possitive, OnClickListener negative) {

		if (ValidationHelper.isTextEmpty(title)
				|| ValidationHelper.isTextEmpty(message)
				|| ValidationHelper.isTextEmpty(leftButtonName)
				|| ValidationHelper.isTextEmpty(rightButtonName)) {
			return;
		}

		Bundle args = new Bundle();
		args.putString(BUNDLE_TITLE, title);
		args.putString(BUNDLE_MESSAGE, message);
		args.putString(BUNDLE_BUTTON_NAME_LEFT, leftButtonName);
		args.putString(BUNDLE_BUTTON_NAME_RIGHT, rightButtonName);
		args.putString(BUNDLE_EDIT_TEXT_TEXT, textEditText);
		this.setArguments(args);

		if (possitive != null) {
			mOnClickAcceptListener = possitive;
		}

		if (negative != null) {
			mOnClickDismissListener = negative;
		}
		mTitle = title;
		mMessage = message;
		mLeftButtonName = leftButtonName;
		mRightButtonName = rightButtonName;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTitle = getArguments().getString(BUNDLE_TITLE);
		mMessage = getArguments().getString(BUNDLE_MESSAGE);
		mLeftButtonName = getArguments().getString(BUNDLE_BUTTON_NAME_LEFT);
		mRightButtonName = getArguments().getString(BUNDLE_BUTTON_NAME_RIGHT);
		mEditMessage = getArguments().getString(BUNDLE_EDIT_TEXT_TEXT);
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
		View v = inflater.inflate(R.layout.fragment_dialog_add_player,
				container, false);

		mTitleTextView = (TextView) v.findViewById(R.id.dialog_titleTextView);
		mMessageTextView = (TextView) v
				.findViewById(R.id.dialog_messageTextView);

		mEditText = (EditText) v.findViewById(R.id.dialog_playerEditText);
		Button dismissButton = (Button) v
				.findViewById(R.id.dialog_dismissButton);
		Button acceptButton = (Button) v.findViewById(R.id.dialog_rollButton);

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
		if (!ValidationHelper.isTextEmpty(mEditMessage)) {
			//if we have a text change input type to number
			mEditText.setText(mEditMessage);
		} else {
			//if we do not have a text change input type to normal
			//mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
		}
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

	@Override
	public String getTextInEditText() {
		return mEditText.getText().toString();
	}

	@Override
	public void clearEditText() {
		mEditText.setText("");

	}

	@Override
	public void setTextInEditText(String text) {
		mEditText.setText(text);
	}

}
