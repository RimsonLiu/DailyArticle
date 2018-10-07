# 每日一文
## 10.1  
TabLayout+ViewPager+Fragment的界面组合  
git的使用  
完成文章界面（除随机文章、收藏）  
完成声音界面（RecyclerView+CardView）

##### 遇到的坑：
[Android Studio无法添加远程依赖\无法下载.pom问题](https://www.jianshu.com/p/584a6ecea7f0)  

[Jsoup中getElements之后再select范围值为空的情况](https://www.jianshu.com/p/ef47e5f81a4a)

[Glide找不到某些方法的问题](https://www.jianshu.com/p/d7b121daf82d)

## 10.2  
规范了类和方法的命名  
删除了getter方法，改用public修饰bean的字段  
添加了网络请求判断（还没有做缓存）  
实现了mp3文件下载和监听（判断文件是否已存在出现问题）  

##### 遇到的坑：  
[Java中byte[]和String的转换问题](https://www.jianshu.com/p/bcdc404de69b)  

[Android runtime permission](https://www.jianshu.com/p/efaf04fee8cd)

## 10.3
实现了“随机文章”  
实现了“播放音频”  
（只能先下载后播放，下载后需要重新进入才能播放，seekbar不能拖动）  

#### 遇到的坑：  
[Android中File.exists()始终返回false/路径的坑](https://www.jianshu.com/p/ba66b29e617f)

## 10.4  
修复了seekbar无法正常使用的bug
添加了通知栏（还不能控制）
添加了收藏和数据库（还未实现）

## 10.5  
实现了文章和声音的收藏  

## 10.6  
用Service实现了声音的播放  
实现了通知栏对播放暂停的控制  

## 10.7
实现了图片的三级缓存
