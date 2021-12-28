package com.control.zju.utils;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import lombok.Data;

/**
 * @author zbf
 * @date 2021/12/26
 * @description 液化器
 */

@Data
public class LIQ {
    int number;
    int timeLength = 3;
    double loadGox;
    double loadGan;
    double gan;
    double infinity = Double.POSITIVE_INFINITY;
    double startupCost = 2000 * 0.6;
    int[] initStatus = {1, 0};

    MPSolver solver;

    MPVariable[][] subY;
    MPVariable[] totalProduceGox;
    MPVariable[] totalProduceGan;
    MPVariable[][][] z;
    MPVariable[] totalGan;
    MPVariable totalStartup;
    MPVariable[] yGox;
    MPVariable[] yGan;

    MPConstraint[] totalY;
    MPConstraint[] calTotalGasGox;
    MPConstraint[] calTotalGasGan;
    MPConstraint[][][] transferConstraint;
    MPConstraint[] initConstraint;
    MPConstraint[] calGan;
    MPConstraint calTotalStartup;
    MPConstraint[] calY;

    public LIQ(int length){
        this.timeLength = length;
        this.subY = new MPVariable[2][timeLength+1];
        this.totalProduceGox = new MPVariable[timeLength];
        this.totalProduceGan = new MPVariable[timeLength];
        this.z = new MPVariable[2][2][timeLength];
        this.totalGan = new MPVariable[timeLength];
        this.yGox = new MPVariable[timeLength];
        this.yGan = new MPVariable[timeLength];

        this.totalY = new MPConstraint[timeLength];
        this.calTotalGasGox = new MPConstraint[timeLength];
        this.calTotalGasGan = new MPConstraint[timeLength];
        this.transferConstraint = new MPConstraint[2][2][timeLength];
        this.initConstraint = new MPConstraint[2];
        this.calGan = new MPConstraint[timeLength];
        this.calY = new MPConstraint[timeLength];
    }

    public void createVar(){
        for (int i=0;i<timeLength;i++){
            totalProduceGan[i] = solver.makeNumVar(0, infinity, "");
            totalProduceGox[i] = solver.makeNumVar(0, infinity, "");
            yGox[i] = solver.makeBoolVar("");
            yGan[i] = solver.makeBoolVar("");
            totalGan[i] = solver.makeNumVar(0, infinity, "");
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

            calTotalGasGox[i] = solver.makeConstraint(0, 0);
            calTotalGasGox[i].setCoefficient(totalProduceGox[i], 1);
            calTotalGasGox[i].setCoefficient(yGox[i], -loadGox);

            calTotalGasGan[i] = solver.makeConstraint(0, 0);
            calTotalGasGan[i].setCoefficient(totalProduceGan[i], 1);
            calTotalGasGan[i].setCoefficient(yGan[i], -loadGan);

            calY[i] = solver.makeConstraint(0,0);
            calY[i].setCoefficient(yGan[i],-1);
            calY[i].setCoefficient(yGox[i],-1);
            calY[i].setCoefficient(subY[1][i+1],1);

            calGan[i] = solver.makeConstraint(0,0);
            calGan[i].setCoefficient(totalGan[i], 1);
            calGan[i].setCoefficient(subY[1][i+1], -gan);

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
