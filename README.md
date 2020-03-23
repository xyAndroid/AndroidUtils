# AndroidUtils
## 安卓工具集合


### [about KeyboardUtils](./androidUtilsLib/src/main/java/com/xy/lib/utils/KeyboardUtils.java)
```
registerKeyboardChangeListener  ：软键盘高度变化监听
```

### [about ConvertUtils](./androidUtilsLib/src/main/java/com/xy/lib/utils/ConvertUtils.java)
```
dp2px  ：dp转px
px2dp  ：px转dp
sp2px  ：sp转px
px2sp  ：px转sp
```


### [about ScreenUtils](./androidUtilsLib/src/main/java/com/xy/lib/utils/ScreenUtils.java)
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

### [about NavigationBarUtils](./androidUtilsLib/src/main/java/com/xy/lib/utils/NavigationBarUtils.java)
```
hasNavigationBarCompat                             ：判断NavigationBar是否显示
hasNavigationBarCompactSumsungGestureTip           ：判断Sumsung手机全屏手势模式下开启手势提示
getNavigationBarGestureTipHeight                   ：开启全屏手势后，获取手势提示高度，目前只有三星手机增加了手势提示高度，其他机型默认为0
getNavigationBarGestureTipHeightCompactSumsung     ：获取三星手机手势提示高度
```

### [about ZipUtils](./androidUtilsLib/src/main/java/com/xy/lib/utils/ZipUtils.java)
```
unZip  ：解压
```

### [about FileUtils](./androidUtilsLib/src/main/java/com/xy/lib/utils/FileUtils.java)
```
saveBitmapToLocal ：将Bitmap保存到本地
```

### [about Md5Utils](./androidUtilsLib/src/main/java/com/xy/lib/utils/Md5Utils.java)
```
getMD5 ：获取MD5值(大写)
getmd5 ：获取MD5值(小写)
```
### [about StringUtils](./androidUtilsLib/src/main/java/com/xy/lib/utils/StringUtils.java)
```
isEmpty    ：判断字符串是否为空
isNotEmpty ：判断字符串是否为空
isNumeric  ：判断字符串是否是数字
```

### [about floatWindow](https://github.com/yhaolpz/FloatWindow)

### [about AudioPlayerUtil](./androidUtilsLib/src/main/java/com/xy/lib/utils/AudioPlayerUtil.java)
```
startAudio                     ：开始播放
changeAudio                    ：切换音频
stopAudio                      ：停止播放
pauseAudio                     ：暂停播放
seekTo                         ：拖动音频
release                        ：结束播放，释放资源
getAudioDuration               ：获取音频时长
isPlayingAudio                 ：是否正在播放
setAudioFocusChangeListener    ：设置音频焦点监听
```



## 自定义View

### [MarqueeTextView](./androidUtilsLib/src/main/java/com/xy/lib/view/MarqueeTextView.java) [自定义跑马灯](https://github.com/xiaweizi/MarqueeTextView)
```
startScroll   ：开始滚动
resumeScroll  ：继续滚动
pauseScroll   ：暂停滚动
stopScroll    ：停止滚动并回到最初位置
```


