package chien.com.musicunionsearch.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import chien.com.musicunionsearch.R;
import chien.com.musicunionsearch.holder.SongViewHolder;
import chien.com.musicunionsearch.provider.SeekBarCoordinatesProvider;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.actionWithAssertions;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String TAG = "MainActivityTest";
    private static final String TEST_SONG = "AGA";
    private static final int WAIT_PROGRESS_INTERNAL = 100;
    private UiDevice device;

    @Rule
    public IntentsTestRule<MainActivity_> mainActivity = new IntentsTestRule<>(MainActivity_.class);

    @Before
    public void setUp() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testSearch() throws InterruptedException, UiObjectNotFoundException {
        //每种类型都点击
        for (int i = 0; i < mainActivity.getActivity().searchType.getChildCount(); i ++) {
            Log.i(TAG, "start to test index" + i);
            onView(withId(mainActivity.getActivity().searchType.getChildAt(i).getId())).perform(click());
            if (i == 0) {
                onView(withId(R.id.search)).perform(click());
                onView(withId(android.support.v7.appcompat.R.id.search_src_text))
                        .perform(typeText(TEST_SONG))
                        .perform(pressKey(KeyEvent.KEYCODE_ENTER));
            } else if (i == 2) {
                //QQ音乐先忽略
                continue;
            } else {
                onView(withId(android.support.v7.appcompat.R.id.search_src_text))
                        .perform(clearText())
                        .perform(typeText(TEST_SONG))
                        .perform(pressKey(KeyEvent.KEYCODE_ENTER));
            }
            waitProgressBar();
            //播放歌曲
            onView(withId(R.id.search_result)).perform(RecyclerViewActions.<SongViewHolder>actionOnItemAtPosition(0, click()));
            TimeUnit.SECONDS.sleep(5);
            TextView textView = mainActivity.getActivity().searchResult.getChildAt(0).findViewById(android.R.id.text1);
            onView(withId(R.id.song_name)).check(matches(ViewMatchers.withText(textView.getText().toString())));
            textView = mainActivity.getActivity().searchResult.getChildAt(0).findViewById(android.R.id.text2);
            onView(withId(R.id.singer_name)).check(matches(ViewMatchers.withText(textView.getText().toString())));
            Assert.assertNotEquals(mainActivity.getActivity().seekBar.getMax(), 0);
            //仅网易云测试下载
            if (i == 0) {
                onView(withId(R.id.download_button)).perform(click());
                //点击允许权限
                UiObject allowPermissions = device.findObject(new UiSelector().text("ALLOW"));
                if (allowPermissions.exists()) {
                    allowPermissions.click();
                }
            }
            //停止播放
            onView(withId(R.id.player_button)).perform(click());
            //拖动
            int progress = mainActivity.getActivity().seekBar.getMax() / 2;
            ViewAction swipeAction = new GeneralSwipeAction(
                    Swipe.FAST,
                    new SeekBarCoordinatesProvider(0),
                    new SeekBarCoordinatesProvider(progress),
                    Press.PINPOINT
            );
            onView(withId(R.id.player_seek_bar)).perform(actionWithAssertions(swipeAction));
            //继续播放
            onView(withId(R.id.player_button)).perform(click());
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void testSetting() {
        onView(withId(R.id.setting)).perform(click());
        intended(hasComponent(SettingActivity.class.getName()));
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
