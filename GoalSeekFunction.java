package com.zuci.dashboard;

import java.text.DecimalFormat;

/**
 * The type Goal seek function.
 */
public class GoalSeekFunction {

    /**
     * The constant ITERATOR_LIMIT.
     */
    private static final int ITERATOR_LIMIT = 10000000;

    /**
     * The constant THRESHOLD.
     */
    private static final double THRESHOLD = 0.000_000_000_1;

    /**
     * The interface Compute interface.
     * use this to implement the required computation logic
     */
    public interface ComputeInterface {
        /**
         * Compute double.
         *
         * @param currentValue the current value
         * @return the double
         */
        double compute(double currentValue);
    }

    /**
     * Gets goal seek.
     *
     * @param goalSeek         the goal seek
     * @param computeInterface the compute interface
     * @return the goal seek
     */
    public Double getGoalSeek(final GoalSeek goalSeek, final ComputeInterface computeInterface) {
        int iterator = 1; /* how many times bisection has been performed */
        double currentValue;
        do {
            currentValue = computeInterface.compute(goalSeek.getMidValue() / 100); // passing as percent
            if (!Double.isInfinite(currentValue) && !Double.isNaN(currentValue)) {
                // evaluate function at midpoint & determine next interval bound
                goalSeek.checkCurrentValue(currentValue);

                if (goalSeek.getDifference(currentValue) == 0d)
                    return goalSeek.getMidValue();
            }
            iterator++; // increment iteration
        } while (Math.abs(goalSeek.getMinimumBoundaryValue() - goalSeek.getMaximumBoundaryValue()) / 2 >= THRESHOLD && Math.abs(goalSeek.getDifference(currentValue)) > THRESHOLD && iterator <= ITERATOR_LIMIT);
        throw new RuntimeException("Goal not found");
    }

    /**
     * The entry point of application.
     *
     * @param arguments the input arguments
     */
    public static void main(String[] arguments) {
        //testGetGoalSeek()
        final GoalSeekFunction goalSeekFunction = new GoalSeekFunction();
        final Double goalSeek = goalSeekFunction.getGoalSeek(new GoalSeek(27000, 0, 100), a -> a * 100000);
        System.out.println(goalSeek);
    }
}

/**
 * The type Goal seek.
 */
class GoalSeek {

    /**
     * The Decimal format.
     */
    private final DecimalFormat decimalFormat = new DecimalFormat("#0.000000");
    /**
     * The Target value.
     */
    private final double targetValue;

    /**
     * The Minimum boundary value.
     */
    private double minimumBoundaryValue;
    /**
     * The Maximum boundary value.
     */
    private double maximumBoundaryValue;

    /**
     * Instantiates a new Goal seek.
     *
     * @param targetValue          the target value
     * @param minimumBoundaryValue the minimum boundary value
     * @param maximumBoundaryValue the maximum boundary value
     */
    public GoalSeek(final double targetValue, final double minimumBoundaryValue, final double maximumBoundaryValue) {
        this.targetValue = targetValue;
        this.minimumBoundaryValue = minimumBoundaryValue;
        this.maximumBoundaryValue = maximumBoundaryValue;
    }

    /**
     * Gets mid value.
     *
     * @return the mid value
     */
// bisection gives us the "average" of the point values
    public double getMidValue() {
        return (minimumBoundaryValue + maximumBoundaryValue) / 2;
    }

    /**
     * Check current value.
     *
     * @param currentValue the current value
     */
    public void checkCurrentValue(final double currentValue) {
        final double difference = getDifference(currentValue);
        final double midValue = getMidValue();
        if (difference < 0) {
            maximumBoundaryValue = midValue;
        } else {
            minimumBoundaryValue = midValue;
        }
    }

    /**
     * Gets difference.
     *
     * @param currentValue the current value
     * @return the difference
     */
    public double getDifference(final double currentValue) {
        return Double.parseDouble(decimalFormat.format(targetValue - currentValue));
    }

    /**
     * Gets minimum boundary value.
     *
     * @return the minimum boundary value
     */
    public double getMinimumBoundaryValue() {
        return minimumBoundaryValue;
    }

    /**
     * Gets maximum boundary value.
     *
     * @return the maximum boundary value
     */
    public double getMaximumBoundaryValue() {
        return maximumBoundaryValue;
    }

}


