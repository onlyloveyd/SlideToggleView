# SlideToggleView
🍎A simple SlideToggleView for Android.

[中文版]()

## Installation
### Gradle
**Step 1.** Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
``` 
**Step 2.** Add the dependency
```groovy
dependencies {
    iimplementation 'com.github.onlyloveyd:SlideToggleView:1.0'
}
```

## Basic Usage
**Step 1.** Add **SlideToggleView** into your layout
```xml
      <cn.onlyloveyd.slidetoggleview.SlideToggleView
            android:id="@+id/slideToggleView"
            android:layout_width="match_parent"
            android:layout_height="56dp"
            android:layout_marginTop="40dp"
            android:background="@drawable/slide_bg"
            app:stv_blockBottomMargin="2dp"
            app:stv_blockLeftMargin="2dp"
            app:stv_blockRightMargin="2dp"
            app:stv_blockTopMargin="3dp"
            app:stv_remain="80dp"
            app:stv_slideBlock="@drawable/slide_block"
            app:stv_text="Slide To Unlock" />
```
**Step 2.**(optional)add SlideToggleListener for SlideToggleView
```java
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
```

## Basic Result
![slidetoggleview](/screenshots/slidetoggleview.gif)



