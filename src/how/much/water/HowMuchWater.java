package how.much.water;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HowMuchWater {

    protected static List<Integer> w;

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
            water += waterLevel - cup.get(i);
        }
        return water;
    }

    public static List<Integer> initWorld() {
        List<Integer> w = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            w.add(ThreadLocalRandom.current().nextInt(10));
        }
        System.out.println("World: " + w);
        return w;
    }

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {

            int totVolume = 0;
            w = initWorld();

            List<List<Integer>> cups = getCups(w, 0);
            System.out.println("cups : " + cups);
            for (List<Integer> cup : cups) {
                totVolume += howMuchWaterInTheCup(w.subList(cup.get(0), cup.get(1) + 1));
            }
            System.out.println("total volume is : " + totVolume);
        }
    }

    private HowMuchWater() {
    }
}
