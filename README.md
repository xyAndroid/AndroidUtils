# AndroidUtils
## 安卓工具集合


### about KeyboardUtils
```
registerKeyboardChangeListener  ：软键盘高度变化监听
```

### about ConvertUtils
```
dp2px  ：dp转px
px2dp  ：px转dp
sp2px  ：sp转px
px2sp  ：px转sp
```


### about ScreenUtils
```
getRealHeight                          ：获取屏幕高度（不同的手机效果不一样，有的手机会改高度由状态栏、虚拟键盘高度、屏幕高度组成，有的手机由状态栏、屏幕高度组成）
getRealWidth                           ：获取屏幕宽度
getScreenHeight                        ：获取屏幕高度
getScreenWidth                         ：获取屏幕宽度
getNavigationBarHeight                 ：获取导航栏的高度
getStatusBarHeight                     ：获取状态栏的高度
getOrientation                         ：获取屏幕方向
getRealHeightNotContainNavigationBar   ：获取除了虚拟键盘之后的窗体实际高度
getActionBarHeight                     ：获取ActionBar高度
getDpi                                 ：获取像素密度dpi
getDensity                             ：获取密度density  density = dpi / 160
```

### about NavigationBarUtils
```
hasNavigationBarCompat                             ：判断NavigationBar是否显示
hasNavigationBarCompactSumsungGestureTip           ：判断Sumsung手机全屏手势模式下开启手势提示
getNavigationBarGestureTipHeight                   ：开启全屏手势后，获取手势提示高度，目前只有三星手机增加了手势提示高度，其他机型默认为0
getNavigationBarGestureTipHeightCompactSumsung     ：获取三星手机手势提示高度
```

### about ZipUtils
```
unZip  ：解压
```

### [about floatWindow](https://github.com/yhaolpz/FloatWindow)


## 自定义View

### MarqueeTextView [自定义跑马灯](https://github.com/xiaweizi/MarqueeTextView)
```
startScroll   ：开始滚动
resumeScroll  ：继续滚动
pauseScroll   ：暂停滚动
stopScroll    ：停止滚动并回到最初位置
```

