package geekbang.concurrent.race.superpolicy;

import geekbang.concurrent.race.GenRunnable;

import java.util.List;
import java.util.Random;

/**
 * Created by muscaestar on 1/3/22
 *
 * @author muscaestar
 */
public class SuperRandomGen implements GenRunnable {
    SuperShareData ssd;
    int size;
    int offset;

    public SuperRandomGen(SuperShareData ssd, int size, int offset) {
        this.ssd = ssd;
        this.size = size;
        this.offset = offset;
    }

    @Override
    public void gen() {
        Random rand = new Random(System.currentTimeMillis());
        final int len = size;
        final int ofs = offset;
        final List<Integer> scores = ssd.getScore();
        for (int i = 0; i < len; i++) {
            // 保持和基准一样的随机生成算法
            int r = rand.nextInt(SuperShareData.COUNT);
            scores.set(ofs + i, r);
        }
    }

    @Override
    public void run() {
        gen();
    }
}
