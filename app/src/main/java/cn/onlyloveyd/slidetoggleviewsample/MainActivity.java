package cn.onlyloveyd.slidetoggleviewsample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.Locale;

import cn.onlyloveyd.slidetoggleview.SlideToggleView;
import cn.onlyloveyd.slidetoggleviewsample.databinding.ActivityMainBinding;

/**
 * MainActivity
 *
 * @author onlyloveyd
 * @date 2019/1/10 09:02
 */
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.slideToggleView.setSlideToggleListener(new SlideToggleView.SlideToggleListener() {
            @Override
            public void onBlockPositionChanged(SlideToggleView view, int left, int total,
                    int slide) {
                String content = String.format(Locale.CHINESE, "left: %d - total: %d - slide: %d",
                        left, total, slide);
                mBinding.shimmerTextView.setText(content);
            }

            @Override
            public void onSlideOpen(SlideToggleView view) {
                Toast.makeText(MainActivity.this, "Slide Toggle is Open",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
