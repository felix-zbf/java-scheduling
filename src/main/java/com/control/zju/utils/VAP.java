package com.control.zju.utils;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import lombok.Data;

/**
 * @author zbf
 * @date 2021/12/26
 * @description 气化器
 */

@Data
public class VAP {
    int number;
    int timeLength;
    double load;
    double infinity = Double.POSITIVE_INFINITY;
    double startupCost = 2000 * 0.6;
    int[] initStatus = {1, 0};

    MPSolver solver;

    MPVariable[][] subY;
    MPVariable[] totalGas;
    MPVariable[][][] z;
    MPVariable totalStartup;

    MPConstraint[] totalY;
    MPConstraint[] calTotalGas;
    MPConstraint[][][] transferConstraint;
    MPConstraint[] initConstraint;
    MPConstraint calTotalStartup;

    public VAP(int length){
        this.timeLength = length;

        this.subY = new MPVariable[2][timeLength+1];
        this.totalGas = new MPVariable[timeLength];
        this.z = new MPVariable[2][2][timeLength];

        this.totalY = new MPConstraint[timeLength];
        this.calTotalGas = new MPConstraint[timeLength];
        this.transferConstraint = new MPConstraint[2][2][timeLength];
        this.initConstraint = new MPConstraint[2];
    }

    public void createVar(){
        for (int i=0;i<timeLength;i++){
            totalGas[i] = solver.makeNumVar(0, infinity, "");

            for (int j=0;j<2;j++){
                for (int k=0;k<2;k++){
                    z[j][k][i] = solver.makeBoolVar("");
                }
                subY[j][i] = solver.makeBoolVar("");
            }
        }

        for (int i=0;i<2;i++) {
            subY[i][timeLength] = solver.makeBoolVar("");
        }
        totalStartup = solver.makeNumVar(0, infinity, "");
    }

    public void createConstraint(){

        calTotalStartup = solver.makeConstraint(0,0);
        calTotalStartup.setCoefficient(totalStartup, 1);

        for (int i=0;i<timeLength;i++){
            totalY[i] = solver.makeConstraint(1,1);
            totalY[i].setCoefficient(subY[0][i+1], 1);
            totalY[i].setCoefficient(subY[1][i+1], 1);

            calTotalGas[i] = solver.makeConstraint(0, 0);
            calTotalGas[i].setCoefficient(totalGas[i], 1);
            calTotalGas[i].setCoefficient(subY[1][i+1], -load);

            for (int j=0;j<2;j++){
                transferConstraint[0][j][i] = solver.makeConstraint(0,0);
                transferConstraint[0][j][i].setCoefficient(subY[j][i+1], -1);
                for (int k=0;k<2;k++){
                    transferConstraint[0][j][i].setCoefficient(z[k][j][i], 1);
                }

                transferConstraint[1][j][i] = solver.makeConstraint(0,0);
                transferConstraint[1][j][i].setCoefficient(subY[j][i], -1);
                for (int k=0;k<2;k++){
                    transferConstraint[1][j][i].setCoefficient(z[j][k][i], 1);
                }
            }

            calTotalStartup.setCoefficient(z[0][1][i], -startupCost);
        }
        for (int i=0;i<2;i++){
            initConstraint[i] = solver.makeConstraint(initStatus[i], initStatus[i]);
            initConstraint[i].setCoefficient(subY[i][0], 1);
        }
    }
}
