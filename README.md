# SwitchCardView  [![](https://img.shields.io/badge/version-1.0.0-blue.svg)]() [![](https://img.shields.io/badge/License-Apache License 2.0-blue.svg)]()


#概述
这是一个滑动切换卡片样式的View，可以用手指进行卡片的拖拽，当滑动距离小于一定的距离，卡片会自动回到卡片位置。
#特点如下
- 卡片可以可循环展示
- 卡片可以嵌套在任何滑动组件中，如ListView，RecyclerView中，无需额外的滑动事件拦截处理
- 卡片支持，小范围距离滑动，回归到卡片原始位置，并不进行数据切换
- Adapter设计风格，View样式，数据源自定义设置即可。
- 卡片从始至终只使用2个view（会复用）

#效果图如下：
![compress](https://lh3.googleusercontent.com/-6fx98zLh3bs/Wmv7sPvz-OI/AAAAAAAAEes/NLSpZDgA9PwxS7Y-O1ngAJiOFjQa0F5vQCHMYCw/I/compress.gif)
ps:录屏工具原因，会显得比较模糊
![compress3](https://lh3.googleusercontent.com/-BG2vZCgvJU4/Wmv_rVxnFcI/AAAAAAAAEe8/xrnSgtubMS4_3MDoFo1OYXpk-KsfI8yaACHMYCw/I/compress3.gif)


#使用方法：

```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.rainmtime.androidtools:<latest-version>'
    }
}

```
在xml中：

```
<rainmtime.com.switchcardview.SwitchCardView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/switch_cardview"
    android:layout_width="match_parent"
    android:layout_height="100dp">

</rainmtime.com.switchcardview.SwitchCardView>
```

给这个SwitchCardView设置SwitchCardView.adapter

```
//跟ListView的Adapter 使用方法同样效果
 mSwitchCardView.setAdapter(new SwitchCardView.Adapter() {
            @NonNull
            @Override
            public View createTopCardView(@NonNull ViewGroup parent) {
                return null;
            }

            @NonNull
            @Override
            public View createBottomCardView(@NonNull ViewGroup parent) {
                return null;
            }

            @Override
            public int getItemCount() {
                return 0;
            }

            @Override
            public void renderView(int position, View view) {

            }
        });
```
**PS:具体实现，可以参考本项目的Demo即可。**


#实现思路
- 其实这个SwitchCardView，会有2个子View（一上一下），就是Adapter中createTopCardView（）和createBottomCardView（）返回的view，要2个view的目的，是数据预加载（2个），这样做动画的时候，衔接的比较好。

- 手指滑动的过程中，其实只是一张假的bitmap。
- 数据切换逻辑就是，当第一张卡片的假身（bitmap）划走以后，把以前bottomView展示的数据，让TopView展示，拿第3个数据，让bottomView数据展示。这样就顺利的切换了数据（用户以为是卡片换，其实是数据，错位上移而已）

#Demo参考
这个项目有个app的module，里面有个简单的实例（就是第一张效果图的示例）




