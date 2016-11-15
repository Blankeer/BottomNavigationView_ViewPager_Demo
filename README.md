#官方BottomNavigationView和ViewPager使用的正确姿势
##使用
使用查了一些demo,重点参考了[http://www.jianshu.com/p/46c629841803](http://www.jianshu.com/p/46c629841803)和[https://github.com/brucevanfdm/BottomNavigationDemo](https://github.com/brucevanfdm/BottomNavigationDemo).
##问题
* 当item数量为3时,一切正常
* 当item数量为4时,看下图

![gif1](https://raw.githubusercontent.com/Blankeer/BottomNavigationView_ViewPager_Demo/master/image/bottomview_1.gif)

黑人问号???这什么鬼.点击item正常,但是切换viewpager不正常,没有了切换时的动画,而且item大小区域显示不正常.

查看了一下源码,发现官方好像没有提供切换item的方法.

只能曲线救国了.
##简单分析和曲线解决
我的想法是既然点击item时可以,那么能不能performClick()一个点击事件过去呢,这就得找到item对应的view.
翻了下相关类的源码,最终发现item实际是`BottomNavigationItemView`类,来找找它的点击事件吧.
```java
    //BottomNavigationMenuView#Line 83
    mOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomNavigationItemView itemView = (BottomNavigationItemView) v;
                final int itemPosition = itemView.getItemPosition();
                activateNewButton(itemPosition);
                mMenu.performItemAction(itemView.getItemData(), mPresenter, 0);
            }
        };
```
就是这里了,看看`activateNewButton`里面,
```java
private void activateNewButton(int newButton) {
        if (mActiveButton == newButton) return;
        mAnimationHelper.beginDelayedTransition(this);
        mPresenter.setUpdateSuspended(true);
        mButtons[mActiveButton].setChecked(false);
        mButtons[newButton].setChecked(true);
        mPresenter.setUpdateSuspended(false);
        mActiveButton = newButton;
    }
```
这句`mAnimationHelper.beginDelayedTransition(this);`好像是执行动画的地方,进去看看
```java
 void beginDelayedTransition(ViewGroup view) {
        // Do nothing.
    }
```
nothing,肯定实现的是它的子类,再看下定义的地方,在构造方法里
```java
    //BottomNavigationMenuView#Line 77
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            mAnimationHelper = new BottomNavigationAnimationHelperIcs();
        } else {
            mAnimationHelper = new BottomNavigationAnimationHelperBase();
        }
```
判断了下版本号,实际的在`BottomNavigationAnimationHelperIcs.java`里
```java
class BottomNavigationAnimationHelperIcs extends BottomNavigationAnimationHelperBase {
    private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;
    private final TransitionSet mSet;

    BottomNavigationAnimationHelperIcs() {
        mSet = new AutoTransition();
        mSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
        mSet.setDuration(ACTIVE_ANIMATION_DURATION_MS);
        mSet.setInterpolator(new FastOutSlowInInterpolator());
        TextScale textScale = new TextScale();
        mSet.addTransition(textScale);
    }
    void beginDelayedTransition(ViewGroup view) {
        TransitionManager.beginDelayedTransition(view, mSet);
    }
}
```
这里分析完了,再看下view层次结构,重点看addView的地方,
发现`BottomNavigationView`添加了一个`BottomNavigationMenuView`,
而`BottomNavigationMenuView`里添加了所有的item,item类型是`BottomNavigationItemView`,
那么好说了,performClick解决的代码就是:
```java
    View temp = mBottomNavigationView.getChildAt(0);
    if (temp instanceof BottomNavigationMenuView) {
        BottomNavigationMenuView bmv = (BottomNavigationMenuView) temp;
        bmv.getChildAt(position).performClick();
    }
```
解决之后的效果图:

![gif2](https://raw.githubusercontent.com/Blankeer/BottomNavigationView_ViewPager_Demo/master/image/bottomview_2.gif)

##存在的问题
由于是performClick()实现的效果,所以和点击item一样,这样的话,
会导致监听器OnNavigationItemSelectedListener回调一次,
而在这个回调里的逻辑是`mViewpager.setCurrentItem(position)`,
这个方法里会判断currentPosition和position是否相等,再执行,
但是在这里如果存在其他逻辑,最好还是,加一句if,
```java
if(mainViewpager.getCurrentItem()!=position) {
    mainViewpager.setCurrentItem(position);
    //do something
}
```
##总结
跟踪和分析源码很有必要.

如果有哪里写的不对的地方,请一定要告诉我,谢谢!
