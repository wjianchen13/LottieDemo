# lottie 动画

# 动态表情
 从ExpressionUtil dealExpression()方法开始
1.首先进入EmResOptions request()方法
这里首先调用EmResManager的getDrawable()方法，在这里首先会从缓存里面拿到对应id的表情缓存信息，如果
没有就生成一个空的表情信息bean。如果设置的高度为0会初始化一下宽高信息，并把这些信息吸入到Options里面
清除缓存为null的数据。接着从drawable缓存里面获取缓存，如果缓存存在的话，就直接返回一个缓存的DrawableProxy
对象，这个对象包含了drawable和options等信息，后续可以通过这个对象获取drawable。这里如果是从缓存中过来的
还需要重新设置一下宽高。
2.通过返回的DrawableProxy，然后调用span()方法，重新生成一个ImageSpan对象，然后把这个对象设置到
SpannableString里面的对应位置。
3.如果缓存没有，分为有动画，没有动画2种情况
4.有动画
有动画走的是!isIgnoreAnim()这分支，在这里首先创建了AsyLottieDrawable的Builder对象。然后调用了build
方法创建了AsyLottieDrawable对象
5.AsyLottieDrawable构造方法内部会初始化一系列的参数，最后调用asyFile()方法，异步请求lottie动画相关
的数据，需要注意的是这里传入的ICreator可能会引入外部的类，所以这里不能用成员变量引用该字段，否则可能
内存泄漏。
在asyFile()方法内部，首先会启动一个延迟50ms的定时器，主要是显示一下占位图。
接着会使用RxJava创建一个观察者，之后调用create()方法，然后回调到EmResManager的create()方法，这里传过来
的参数是ICallBack，后面加载资源成功之后通过这个callBack回调给AsyLottieDrawable对象。AsyLottieDrawable收到
成功回调后，调用Consumer的accept()方法，在这里面处理Lottie资源信息。
这里面收到的AsyLottieDrawable.Composition是封装LottieComposition信息的
在这里会处理drawable的实际宽和高。然后调用setComposition()方法把LottieComposition设置给LottieDrawable
最后调用playSpanAnimation()方法启动动画，后面就可以通过调用invalidate方法刷新TextView的界面显示。
在playSpanAnimation()方法内部，首先结束掉之前的动画，可能是考虑到有多个drawable之前已经存在的动画会影响
现在的逻辑。然后重新调用addAnimatorUpdateListener添加监听器，当监听器回调的时候，会扫描所有绑定了这个drawable
的TextView，调用TextView的invalidate方法刷新界面显示，达到动画效果。
然后移除之前的50ms显示默认图的handler，最后调用playAnimation()方法启动动画。
6.AsyLottieDrawable里面有个attach()方法




















EmResManager
1. 首先获取了表情相关的数据，包括id，json地址等



































