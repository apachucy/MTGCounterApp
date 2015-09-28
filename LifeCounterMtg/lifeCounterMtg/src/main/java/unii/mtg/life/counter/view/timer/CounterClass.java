package unii.mtg.life.counter.view.timer;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

public class CounterClass extends CountDownTimer {

	private TextView mTextView;
	public static final long DEFAULT_TIME_SECOND = 1000;

	/**
	 * 
	 * @param context
	 *            create an instance of vibration class
	 * @param millisInFuture
	 *            The number of mills in the future from the call to start()
	 *            until the countdown is done and onFinish() is called.
	 * @param countDownInterval
	 *            The interval along the way to receive onTick(long) callback
	 * @param textView
	 *            Display count down in text view
	 */
	public CounterClass( long millisInFuture,
			long countDownInterval, TextView textView) {
		super(millisInFuture, countDownInterval);
		mTextView = textView;

	}

	@Override
	public void onFinish() {

	}

	@Override
	public void onTick(long millisUntilFinished) {
		long millis = millisUntilFinished;
		// display formated string
		String hms = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));

		mTextView.setText(hms);
		// instance of vibration exist

	}

}
