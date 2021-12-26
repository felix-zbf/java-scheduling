package com.control.zju.utils;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import lombok.Data;

/**
 * @author zbf
 * @date 2021/12/23
 * @description 变压吸附
 */

@Data
public class PSA {
    int number;
    int timeLength = 5;
    double cost;
    double load;
    double infinity = Double.POSITIVE_INFINITY;
    double startupCost = 2000 * 0.6;
    int[] initStatus = {1, 0};

    MPSolver solver;

    MPVariable[][] subY;
    MPVariable[] totalGox;
    MPVariable[] qElectricity;
    MPVariable[][][] z;
    MPVariable totalElectricity;
    MPVariable totalStartup;

    MPConstraint[] totalY;
    MPConstraint[] calElectricity;
    MPConstraint[] calTotalGox;
    MPConstraint[][][] transferConstraint;
    MPConstraint[] initConstraint;
    MPConstraint calTotalCost;
    MPConstraint calTotalStartup;

    public PSA(int length){
        this.timeLength = length;
        this.subY = new MPVariable[2][timeLength+1];
        this.totalGox = new MPVariable[timeLength];
        this.qElectricity = new MPVariable[timeLength];
        this.z = new MPVariable[2][2][timeLength];;

        this.totalY = new MPConstraint[timeLength];
        this.calElectricity = new MPConstraint[timeLength];
        this.calTotalGox = new MPConstraint[timeLength];
        this.transferConstraint = new MPConstraint[2][2][timeLength];
        this.initConstraint = new MPConstraint[2];
    }

    public void createVar(){
        for (int i=0;i<timeLength;i++){
            totalGox[i] = solver.makeNumVar(0, infinity, "");
            qElectricity[i] = solver.makeNumVar(0, infinity, "");
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
        totalElectricity = solver.makeNumVar(0, infinity, "");
        totalStartup = solver.makeNumVar(0, infinity, "");
    }

    public void createConstraint(){
        calTotalCost = solver.makeConstraint(0,0);
        calTotalCost.setCoefficient(totalElectricity, 1);

        calTotalStartup = solver.makeConstraint(0,0);
        calTotalStartup.setCoefficient(totalStartup, 1);

        for (int i=0;i<timeLength;i++){
            totalY[i] = solver.makeConstraint(1,1);
            totalY[i].setCoefficient(subY[0][i+1], 1);
            totalY[i].setCoefficient(subY[1][i+1], 1);

            calElectricity[i] = solver.makeConstraint(0,0);
            calElectricity[i].setCoefficient(qElectricity[i], 1);
            calElectricity[i].setCoefficient(totalGox[i], -cost);

            calTotalGox[i] = solver.makeConstraint(0, 0);
            calTotalGox[i].setCoefficient(totalGox[i], 1);
            calTotalGox[i].setCoefficient(subY[1][i+1], -load);

            calTotalCost.setCoefficient(qElectricity[i], -1);

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
