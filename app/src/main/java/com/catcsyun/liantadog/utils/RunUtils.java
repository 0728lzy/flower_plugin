package com.catcsyun.liantadog.utils;

public class RunUtils {
    // 步数转公里 km
    public static float getDistanceByStep(long steps) {
        return steps * 0.6f / 1000;
    }

    // 千卡路里计算公式
    //String.format("%.1f", steps * 0.6f * 60 * 1.036f / 1000);
    public static float getCalorieByStep(long steps) {
        return steps * 0.6f * 60 * 1.036f / 1000;
    }

    public static float getCalorieByStepM(long steps) {
        return steps * 0.6f * 60 * 1.036f ;
    }

    /**
     * 描述: 计算卡路里
     * ---------计算公式：体重（kg）* 距离（km）* 运动系数（k）
     * ---------运动系数：健走：k=0.8214；跑步：k=1.036；自行车：k=0.6142；轮滑、溜冰：k=0.518室外滑雪：k=0.888
     * 作者: james
     * 日期: 2019/2/20 19:40
     * 类名: MotionUtils
     *
     * @param weight   体重 kg
     * @param distance 距离 km
     */
    public static float calculationCalorie(float weight, float distance) {
        return weight * distance * 1.036f;
    }

    // 千
    // 米转千卡路里
    public static double getCalorieByDistance(double distance) {
        return  distance * 60 * 1.036f;
    }

    // 返回步长
    public float getStepLength(float height) {
        // 通过身高计算步长 每步占用40%身高
        return height * 0.4f;

    }

    public float getDistance(int steps, float stepLength) {
        // 通过步数和步长计算运动距离
        return steps * stepLength / 100f;
    }

    public float getCalorie(float weight, float distance, int time) {
        // 通过身高、运动距离和消耗时间计算消耗卡路里
        // 运动速度
        float met = 2.0f; // 代表比安静状态消耗的代谢率
        float calorie = (float) (met * weight * time / 60f + (0.75f * weight * distance / 1000f));
        // 卡路里公式
        return calorie;
    }
}

