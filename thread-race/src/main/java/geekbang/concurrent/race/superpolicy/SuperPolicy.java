package geekbang.concurrent.race.superpolicy;



import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by muscaestar on 1/3/22
 *
 * @author muscaestar
 */
public class SuperPolicy {
    final static int genThreadInPool;
    final static int computeThreadInPool;
    SuperShareData ssd;

    static {
        // 计算型程序，线程数=核数最优, 避免上下文阶段
        int threadNum = Runtime.getRuntime().availableProcessors();
        // 如果是单核机器，跑两条线程
        threadNum = threadNum == 1 ? 2 : threadNum;
        genThreadInPool = threadNum;
        computeThreadInPool = threadNum;
    }

    public SuperPolicy() {
        ssd = new SuperShareData();
    }


    public long go() throws Exception {
        final ExecutorService genThreadPool = Executors.newFixedThreadPool(genThreadInPool);

        //使用方法Super-MR计算
        long startTime = System.currentTimeMillis();
        System.out.println("开始方法Super-MR计算计时: " + startTime);

        // 步长
        final int n = SuperShareData.COUNT / genThreadInPool;
        int tail = genThreadInPool - 1;

        int[] sizes = new int[genThreadInPool];
        for (int i = 0; i < tail; i++) {
            sizes[i] = n;
        }
        sizes[tail] = SuperShareData.COUNT - tail * n;

        final int[] arr = sizes;
        final SuperShareData superSd = ssd;
        IntStream.range(0, genThreadInPool)
                .mapToObj(idx -> CompletableFuture.runAsync(new SuperRandomGen(superSd, arr[idx], n * idx), genThreadPool))
                .map(CompletableFuture::join)
                .collect(Collectors.toList()); // 不能用count，否则会编译优化而直接不执行

        long genTime = System.currentTimeMillis();
        System.out.println("产生随机数时长: " + (genTime - startTime));

        final Integer[] sorted = ssd.getScore().toArray(new Integer[0]);
        // 底层是ForkJoin实现的归并排序
        Arrays.parallelSort(sorted, Comparator.reverseOrder());

        for (int i = 0; i < SuperShareData.BUFSIZE; i++) {
            ssd.getTop()[i] = sorted[i];
        }

        long sortTime = System.currentTimeMillis();
        System.out.println("方法Super-MR计算时长: " + (sortTime - genTime));

        printTop();


        genThreadPool.shutdown();
        final long totalTime = sortTime - startTime;
        System.out.println("方法Super-MR 总时长: " + totalTime);

        return totalTime;
    }

    void printTop() {
        System.out.println("前10成绩为:");
        for (int j = 0; j <= 10; j++) {
            System.out.print(ssd.getTop()[j] + " ");
        }
        System.out.println();
    }
}
