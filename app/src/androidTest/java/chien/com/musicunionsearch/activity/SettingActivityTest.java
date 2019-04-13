package chien.com.musicunionsearch.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Environment;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.leon.lfilepickerlibrary.ui.LFilePickerActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import chien.com.musicunionsearch.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SettingActivityTest {

    @Rule
    public IntentsTestRule<SettingActivity> settingActivity = new IntentsTestRule<>(SettingActivity.class);

    @Test
    public void testSetDownloadPath() {
        onView(withText(R.string.preference_title_base)).perform(click());
        onView(withText(R.string.preference_download_path))
                .perform(click());
        Intent resultData = new Intent();
        String phoneNumber = Environment.DIRECTORY_DOWNLOADS;
        resultData.putExtra("path", phoneNumber);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(allOf(hasComponent(LFilePickerActivity.class.getName()), hasExtraWithKey("param")))
                .respondWith(result);
        onView(withId(R.id.btn_addbook)).perform(click());
    }
}
