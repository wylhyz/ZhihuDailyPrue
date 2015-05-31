#知乎日报修改版
根据知乎日报网络请求API自实现的一个知乎日报客户端。

##制作日程

###1.制作启动界面 2015-5-22

1）通过设备尺寸构建API请求

2）使用HttpURLConnection下载网络数据

3）使用文件制作图片作者和图片缓存

###2.使用View Animation制作启动页渐隐动画 2015-5-23

Error )动画有时间，监听动画播放完毕之后再跳转到下一个MainActivity，
但是问题是此时播放完成和加载Activity之间的时差会导致动画完成之后
显示一张图片而不是直接切换。

###3.使用Gson解析每天最新的新闻信息，并提交数据库保存 2015-5-25

1)sqlite数据库的插入和创建，insert方法已经确保了不满足约束条件时候不至ANR异常

2)使用Executor服务创建一个工具类，顺序执行json数据的下载，解析和保存

###4.完成数据的下载和保存，发现启动页首次启动无网络的崩溃问题 2015-5-27

1)通过在程序中预先打包一个drawable和string来定义在无网络的时候加载的数据，
drawable使用体积较小的jpeg图片。

2)由于从drawable编码bitmap速度较慢，采用缩放加载的方式

**OOM问题暂时没有遇到过，暂时没有经验**

###5.准备数据异步加载工具Loader的使用 2015-5-27

###6.主要UI界面构建完毕 2015-5-29

1）在RecyclerView中实现多类型的视图

2）大体模仿了知乎日报首页的热点新闻效果，暂时没有加入自动切换

3）完成Loader与SwipeRefreshLayout的结合，在RecyclerView的Adapter中为
两个Adapter简单的刷新数据。

4）重写了下载首页新闻数据的线程，从混合的Callable改成单纯的Thread，
原因是处理回调数据的时候有未知Bug导致数据无法获取到以至于无法插入数据库。

5）了解Loader的主要使用方式和LoaderManager的基本使用方式

###7.重构项目架构，修改API和某些类的使用 2015-5-31

1）解构RecyclerView的Adapter，将原来复杂的类按照必要性重新分离

2）发现FragmentPagerAdapter的问题，暂时无法解决，只能改用FragmentStatePagerAdapter

3）修改了启动页，讲ImageView动画结束后也设置位INVISIBLE

###8.
