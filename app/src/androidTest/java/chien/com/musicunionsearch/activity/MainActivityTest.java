package chien.com.musicunionsearch.activity;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import chien.com.musicunionsearch.R;
import chien.com.musicunionsearch.holder.SongViewHolder;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String TEST_SONG = "AGA";
    private static final int WAIT_PROGRESS_INTERNAL = 100;

    @Rule
    public ActivityTestRule<MainActivity_> mainActivity = new ActivityTestRule<>(MainActivity_.class);

    @Test
    public void testSearch() throws InterruptedException {
        //每种类型都点击
        for (int i = 0; i < mainActivity.getActivity().searchType.getChildCount(); i ++) {
            onView(withId(mainActivity.getActivity().searchType.getChildAt(i).getId())).perform(click());
            if (i == 0) {
                onView(withId(R.id.search)).perform(click());
                onView(withId(android.support.v7.appcompat.R.id.search_src_text))
                        .perform(typeText(TEST_SONG))
                        .perform(pressKey(KeyEvent.KEYCODE_ENTER));
            } else {
                onView(withId(android.support.v7.appcompat.R.id.search_src_text))
                        .perform(clearText())
                        .perform(typeText(TEST_SONG))
                        .perform(pressKey(KeyEvent.KEYCODE_ENTER));
            }
            waitProgressBar();
            //播放歌曲
            onView(withId(R.id.search_result)).perform(RecyclerViewActions.<SongViewHolder>actionOnItemAtPosition(0, click()));
            TimeUnit.SECONDS.sleep(8);
            TextView textView = mainActivity.getActivity().searchResult.getChildAt(0).findViewById(android.R.id.text1);
            onView(withId(R.id.song_name)).check(matches(ViewMatchers.withText(textView.getText().toString())));
            textView = mainActivity.getActivity().searchResult.getChildAt(0).findViewById(android.R.id.text2);
            onView(withId(R.id.singer_name)).check(matches(ViewMatchers.withText(textView.getText().toString())));
            Assert.assertNotEquals(mainActivity.getActivity().seekBar.getMax(), 0);
            //停止播放
            onView(withId(R.id.player_button)).perform(click());
        }
    }

    /**
     * 等待圈圈消失
     * @throws InterruptedException 中断异常
     */
    private void waitProgressBar() throws InterruptedException {
        while (mainActivity.getActivity().progressBar.getVisibility() != View.GONE) {
            TimeUnit.MILLISECONDS.sleep(WAIT_PROGRESS_INTERNAL);
        }
    }
}
