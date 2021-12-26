package com.control.zju.utils;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import lombok.Data;

/**
 * @author zbf
 * @date 2021/12/26
 * @description 外部供气单元
 */

@Data
public class ExternalSupply {
    int number;
    int timeLength = 5;
    double cost = 224037 * 0.6;
    double[] lowerBound = {26000, 4000, 600};
    double[] upperBound = {30000, 4000, 600};
    double infinity = java.lang.Double.POSITIVE_INFINITY;
    MPSolver solver;

    MPVariable[] y;
    MPVariable[] gox;
    MPVariable[] gan;
    MPVariable[] gar;
    MPVariable[] qCost;
    MPVariable totalCost;

    MPConstraint[][] calGox;
    MPConstraint[][] calGan;
    MPConstraint[][] calGar;
    MPConstraint[] calCost;
    MPConstraint calTotalCost;

    public ExternalSupply(int length){
        this.timeLength = length;

        this.y = new MPVariable[timeLength];
        this.gox = new MPVariable[timeLength];
        this.gan = new MPVariable[timeLength];
        this.gar = new MPVariable[timeLength];
        this.qCost = new MPVariable[timeLength];

        this.calGox = new MPConstraint[2][timeLength];
        this.calGan = new MPConstraint[2][timeLength];
        this.calGar = new MPConstraint[2][timeLength];
        this.calCost = new MPConstraint[timeLength];
    }

    public void createVar(){
        y = solver.makeVarArray(timeLength, 0, 1, true);
        gox = solver.makeVarArray(timeLength, 0, infinity, false);
        gan = solver.makeVarArray(timeLength, 0, infinity, false);
        gar = solver.makeVarArray(timeLength, 0, infinity, false);
        qCost = solver.makeVarArray(timeLength, 0, infinity, false);
        totalCost = solver.makeNumVar(0, infinity, "");
    }

    public void createConstraint(){
        calTotalCost = solver.makeConstraint(0,infinity);
        calTotalCost.setCoefficient(totalCost, 1);
        for (int i=0;i<timeLength;i++){
            calGox[0][i] = solver.makeConstraint(0,infinity);
            calGox[0][i].setCoefficient(gox[i], 1);
            calGox[0][i].setCoefficient(y[i], -lowerBound[0]);

            calGox[1][i] = solver.makeConstraint(0,infinity);
            calGox[1][i].setCoefficient(gox[i], -1);
            calGox[1][i].setCoefficient(y[i], upperBound[0]);

            calGan[0][i] = solver.makeConstraint(0,infinity);
            calGan[0][i].setCoefficient(gan[i], 1);
            calGan[0][i].setCoefficient(y[i], -lowerBound[1]);

            calGan[1][i] = solver.makeConstraint(0,infinity);
            calGan[1][i].setCoefficient(gan[i], -1);
            calGan[1][i].setCoefficient(y[i], upperBound[1]);

            calGar[0][i] = solver.makeConstraint(0,infinity);
            calGar[0][i].setCoefficient(gar[i], 1);
            calGar[0][i].setCoefficient(y[i], -lowerBound[2]);

            calGar[1][i] = solver.makeConstraint(0,infinity);
            calGar[1][i].setCoefficient(gar[i], -1);
            calGar[1][i].setCoefficient(y[i], upperBound[2]);

            calCost[i] = solver.makeConstraint(0,0);
            calCost[i].setCoefficient(qCost[i], 1);
            calCost[i].setCoefficient(gox[i], -cost);

            calTotalCost.setCoefficient(qCost[i], -1);
        }
    }
}
