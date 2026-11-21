package com.northwest.seamless.utils;

public class Utils {


        public static double calculatescore(int rating, int weight, int maximunRating) {

                double objectiveScore = (rating * weight) / maximunRating;

                return objectiveScore;
        }
        

        public static int calculateFinalScore(double objectiveScore, double behaviouralScore) {

                double score = (objectiveScore * 0.70) + (behaviouralScore * 0.30);

                return (int) Math.round(score);

        }


}
