# 题目

生成若干个随机整数，存放到ArrayList中（随机数生成阶段）;计算线程运算取出前10大数字（计算阶段）;记录生成数字和计算前10所花费的总时长

## 步骤

1. 运行 geektime.concurrent.race.ThreadRace
2. 重写SimplePolicy
3. 目标：运行结果基准比值越小越好

## 限制

1. geekbang.concurrent.race.ShareData定义了两个阶段output的数据结构, 只能使用线程不安全的容器。
2. 两个阶段都使用多线程

## 解题思路 1

- 线程数：仅计算，因此应等于cpu核数
- 随机数生成阶段：资源隔离，将ArrayList分割成几段，每个线程只修改分配到的内存区域，避免了并发问题
- 计算阶段：MapReduce的思想实现多线程排序，使用Arrays的高性能并发排序

## 最后结果

- 产生随机数时长: 23
- 计算时长: 59
- 总时长: 82
- 和基准比较: 0.5815602836879432