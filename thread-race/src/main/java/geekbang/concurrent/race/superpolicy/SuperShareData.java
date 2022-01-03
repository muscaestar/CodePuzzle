package geekbang.concurrent.race.superpolicy;

import geekbang.concurrent.race.ShareData;

import java.util.stream.IntStream;

/**
 * Created by muscaestar on 1/3/22
 *
 * @author muscaestar
 */
public class SuperShareData extends ShareData {
    public SuperShareData() {
        // 由于score是ArrayList，用set方法前必须设置值
        IntStream.rangeClosed(1, COUNT).forEachOrdered(i -> getScore().add(0));
    }
}
