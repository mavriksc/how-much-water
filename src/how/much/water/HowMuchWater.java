package how.much.water;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HowMuchWater {

    private static final int N = 240;
    private static final int H = 40;
    
    private static int count = 0;

    public static List<List<Integer>> getCups(List<Integer> world, int offset) {
        boolean isDecreasing = false;
        boolean isIncreasing = false;
        boolean isCupStarted = false;
        int cupStart = 0;
        int cupStartHeight = 0;
        int pCupEnd = 0;
        int pCupEndHeight = 0;
        List<List<Integer>> cups = new ArrayList<>();

        for (int i = offset + 1; i < world.size(); i++) {
            count++;
            if (world.get(i - 1) > world.get(i)) {
                isDecreasing = true;
                isIncreasing = false;
            } else if (world.get(i - 1) < world.get(i)) {
                isDecreasing = false;
                isIncreasing = true;
            } else if (world.get(i - 1).equals(world.get(i))) {
                isDecreasing = false;
                isIncreasing = false;
            }
            if (isDecreasing && !isCupStarted) {
                cupStart = i - 1;
                cupStartHeight = world.get(cupStart);
                isCupStarted = true;
            }
            if (isCupStarted && isIncreasing) {
                if (world.get(i) >= cupStartHeight) {
                    List<Integer> cup = new ArrayList<>(2);
                    cup.add(cupStart);
                    cup.add(i);
                    cups.add(cup);
                    cups.addAll(getCups(world, i));
                    return cups;
                } else if (world.get(i) > pCupEndHeight) {
                    pCupEnd = i;
                    pCupEndHeight = world.get(pCupEnd);
                }
            }
        }
        if (pCupEnd > cupStart) {
            List<Integer> cup = new ArrayList<>(2);
            cup.add(cupStart);
            cup.add(pCupEnd);
            cups.add(cup);
            cups.addAll(getCups(world, pCupEnd));
            return cups;
        }
        return cups;
    }

    public static int howMuchWaterInTheCup(List<Integer> cup) {
        int waterLevel = Math.min(cup.get(0), cup.get(cup.size() - 1));
        int water = 0;
        for (int i = 1; i < cup.size() - 1; i++) {
            count++;
            water += waterLevel - cup.get(i);
        }
        return water;
    }

    public static List<Integer> initWorld() {
        List<Integer> w = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            w.add(ThreadLocalRandom.current().nextInt(H));
        }
        System.out.println("World: " + w);
        return w;
    }

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {

            int totVolume = 0;
            List<Integer> w = initWorld();

            List<List<Integer>> cups = getCups(w, 0);
            System.out.println("cups : " + cups);
            for (List<Integer> cup : cups) {
                totVolume += howMuchWaterInTheCup(w.subList(cup.get(0), cup.get(1) + 1));
            }
            float eff = (float)count/(float) N;

            System.out.printf("Count : %d%n", count);
            System.out.printf("Efficiency : %.2f%n", eff);
            System.out.println("total volume is : " + totVolume);
            count = 0;
        }
    }

    private HowMuchWater() {
    }
}
