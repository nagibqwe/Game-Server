package game.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightCalc<T> {
    private static final Random random = new Random(System.currentTimeMillis());
    private final List<T> objList = new ArrayList();
    private final List<Integer> weightList = new ArrayList();
    private int sumWeight;

    public void init() {
        this.objList.clear();
        this.weightList.clear();
        this.sumWeight = 0;
    }

    public boolean addObject(T obj, int weight) {
        if(weight < 0) {
            return false;
        } else {
            this.objList.add(obj);
            this.weightList.add(Integer.valueOf(weight));
            this.sumWeight += weight;
            return true;
        }
    }

    public T getObjectAndRemove() {
        if(this.sumWeight <= 0) {
            return null;
        } else {
            int randomValue = random.nextInt(this.sumWeight) + 1;
            int index = this.getIndex(randomValue);
            if(index < 0) {
                return null;
            } else {
                this.sumWeight -= ((Integer)this.weightList.remove(index)).intValue();
                return this.objList.remove(index);
            }
        }
    }

    public T getObject() {
        if(this.sumWeight <= 0) {
            return null;
        } else {
            int randomValue = random.nextInt(this.sumWeight) + 1;
            int index = this.getIndex(randomValue);
            return index < 0?null:this.objList.get(index);
        }
    }

    private int getIndex(int randomValue) {
        if(randomValue > this.sumWeight) {
            return -1;
        } else if(randomValue < 0) {
            return -1;
        } else {
            int size = this.objList.size();
            int curRate = 0;

            for(int i = 0; i < size; ++i) {
                int tmpWeight = (this.weightList.get(i)).intValue();
                if(randomValue > curRate && randomValue <= curRate + (this.weightList.get(i)).intValue()) {
                    return i;
                }
                curRate += tmpWeight;
            }
            return -1;
        }
    }
}
