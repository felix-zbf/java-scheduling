package com.control.zju.utils;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import lombok.Data;

/**
 * @author zbf
 * @date 2021/12/26
 * @description 空分装置  (开机时间)
 */

@Data
public class ASU {
    int number;
    int timeLength;
    int modeNum = 1;
    double[] parameters = {-4.42, 3.9, -30500, 0.0063, 425, 0.6, -2689.57, 0.009463, 294};
    double[] airs = {90000.0, 100000.0, 110000.0, 120000.0};
    double[] electricity = {7020.0, 7800.0, 8570.0, 9350.0};
    double[] lowerBound = {26000, 200};
    double[] upperBound = {31000, 900};
    double startupCost = 224037 * 0.6;
    int[] initStatus = {1, 0, 0, 0};

    MPSolver solver;
    double infinity = java.lang.Double.POSITIVE_INFINITY;
    MPVariable[][] subY;
    MPVariable[][] fAir;
    MPVariable[][] fGox;
    MPVariable[][] fGan;
    MPVariable[][] fLox;
    MPVariable[][] fLin;
    MPVariable[][] fGar;
    MPVariable[][] fLar;
    MPVariable[] totalAir;
    MPVariable[] totalGox;
    MPVariable[] totalGan;
    MPVariable[] totalLox;
    MPVariable[] totalLin;
    MPVariable[] totalGar;
    MPVariable[] totalLar;
    MPVariable[][] qElectricityMode;
    MPVariable[][] lambda;
    MPVariable[] qElectricity;
    MPVariable totalElectricity;
    MPVariable startup;
    MPVariable[][][] z;

    MPConstraint[][][] productionConstraint;
    MPConstraint[][] totalConstraint;
    MPConstraint[][][] transferConstraint;
    MPConstraint[] totalCost;
    MPConstraint[] mode;
    MPConstraint[] initConstraint;

    public ASU(int length, int modeLength){
        this.timeLength = length;
        this.modeNum = modeLength;
        this.subY = new MPVariable[modeNum][timeLength + 1];
        this.fAir = new MPVariable[modeNum][timeLength];
        this.fGox = new MPVariable[modeNum][timeLength];
        this.fGan = new MPVariable[modeNum][timeLength];
        this.fLox = new MPVariable[modeNum][timeLength];
        this.fLin = new MPVariable[modeNum][timeLength];
        this.fGar = new MPVariable[modeNum][timeLength];
        this.fLar = new MPVariable[modeNum][timeLength];
        this.totalAir = new MPVariable[timeLength];
        this.totalGox = new MPVariable[timeLength];
        this.totalGan = new MPVariable[timeLength];
        this.totalLox = new MPVariable[timeLength];
        this.totalLin = new MPVariable[timeLength];
        this.totalGar = new MPVariable[timeLength];
        this.totalLar = new MPVariable[timeLength];
        this.qElectricityMode  = new MPVariable[modeNum][timeLength];
        this.lambda = new MPVariable[modeNum][timeLength];
        this.qElectricity = new MPVariable[timeLength];;
        this.z = new MPVariable[modeNum][modeNum][timeLength];

        this.productionConstraint = new MPConstraint[11][modeNum][timeLength];
        this.totalConstraint = new MPConstraint[8][timeLength];
        this.transferConstraint = new MPConstraint[2][modeNum][timeLength];
        this.totalCost = new MPConstraint[2];
        this.mode = new MPConstraint[timeLength];
        this.initConstraint = new MPConstraint[modeNum];
    }

    public void createVar(){
        for (int j=0;j<timeLength;j++){
            for (int i=0;i<modeNum;i++){
                fAir[i][j] = solver.makeNumVar(0, infinity, "");
                fGox[i][j] = solver.makeNumVar(0, infinity, "");
                fGan[i][j] = solver.makeNumVar(0, infinity, "");
                fLox[i][j] = solver.makeNumVar(0, infinity, "");
                fLin[i][j] = solver.makeNumVar(0, infinity, "");
                fGar[i][j] = solver.makeNumVar(0, infinity, "");
                fLar[i][j] = solver.makeNumVar(0, infinity, "");
                subY[i][j] = solver.makeBoolVar("");
                qElectricityMode[i][j] = solver.makeNumVar(0, infinity, "");
                lambda[i][j] = solver.makeNumVar(0, 1, "");

                for (int k=0;k<modeNum;k++){
                    z[i][k][j] = solver.makeBoolVar("");
                }
            }
            totalAir[j] = solver.makeNumVar(0, infinity, "");
            totalGox[j] = solver.makeNumVar(0, infinity, "");
            totalGan[j] = solver.makeNumVar(0, infinity, "");
            totalLox[j] = solver.makeNumVar(0, infinity, "");
            totalLin[j] = solver.makeNumVar(0, infinity, "");
            totalGar[j] = solver.makeNumVar(0, infinity, "");
            totalLar[j] = solver.makeNumVar(0, infinity, "");
            qElectricity[j] = solver.makeNumVar(0, infinity, "");
        }
        for (int i=0;i<modeNum;i++) {
            subY[i][timeLength] = solver.makeBoolVar("");
        }
        totalElectricity = solver.makeNumVar(0.0, infinity, "");
        startup = solver.makeNumVar(0.0, infinity, "");
    }

    public void createConstraint(){
        for (int i=0;i<timeLength;i++){
            productionConstraint[0][0][i] = solver.makeConstraint(0, 0);
            productionConstraint[0][0][i].setCoefficient(fAir[0][i], 1);
            productionConstraint[1][0][i] = solver.makeConstraint(0, 0);
            productionConstraint[1][0][i].setCoefficient(fGox[0][i], 1);
            productionConstraint[2][0][i] = solver.makeConstraint(0, 0);
            productionConstraint[2][0][i].setCoefficient(fGan[0][i], 1);
            productionConstraint[3][0][i] = solver.makeConstraint(0, 0);
            productionConstraint[3][0][i].setCoefficient(fGar[0][i], 1);
            productionConstraint[4][0][i] = solver.makeConstraint(0, 0);
            productionConstraint[4][0][i].setCoefficient(fLox[0][i], 1);
            productionConstraint[5][0][i] = solver.makeConstraint(0, 0);
            productionConstraint[5][0][i].setCoefficient(fLin[0][i], 1);
            productionConstraint[6][0][i] = solver.makeConstraint(0, 0);
            productionConstraint[6][0][i].setCoefficient(fLar[0][i], 1);
            productionConstraint[7][0][i] = solver.makeConstraint(0, 0);
            productionConstraint[7][0][i].setCoefficient(qElectricityMode[0][i], 1);

            for (int j=1;j<modeNum;j++){
                productionConstraint[0][j][i] = solver.makeConstraint(0, 0);
                productionConstraint[0][j][i].setCoefficient(fAir[j][i], 1);
                productionConstraint[0][j][i].setCoefficient(fGox[j][i], parameters[0]);
                productionConstraint[0][j][i].setCoefficient(fLin[j][i], parameters[1]);
                productionConstraint[0][j][i].setCoefficient(subY[j][i + 1], parameters[2]);

                productionConstraint[1][j][i] = solver.makeConstraint(0, 0);
                productionConstraint[1][j][i].setCoefficient(fAir[j][i], parameters[3]);
                productionConstraint[1][j][i].setCoefficient(fLox[j][i], parameters[4]);
                productionConstraint[1][j][i].setCoefficient(fLin[j][i], -1);
                productionConstraint[1][j][i].setCoefficient(subY[j][i + 1], -1);

                productionConstraint[2][j][i] = solver.makeConstraint(0, 0);
                productionConstraint[2][j][i].setCoefficient(fAir[j][i], parameters[5]);
                productionConstraint[2][j][i].setCoefficient(fGan[j][i], -1);
                productionConstraint[2][j][i].setCoefficient(fLin[j][i], -1);
                productionConstraint[2][j][i].setCoefficient(subY[j][i + 1], parameters[6]);

                productionConstraint[3][j][i] = solver.makeConstraint(0, 0);
                productionConstraint[3][j][i].setCoefficient(fAir[j][i], parameters[7]);
                productionConstraint[3][j][i].setCoefficient(fLar[j][i], -1);
                productionConstraint[3][j][i].setCoefficient(fGar[j][i], -1);
                productionConstraint[3][j][i].setCoefficient(subY[j][i + 1], parameters[8]);

                productionConstraint[4][j][i] = solver.makeConstraint(0, infinity);
                productionConstraint[4][j][i].setCoefficient(fGox[j][i], 1);
                productionConstraint[4][j][i].setCoefficient(subY[j][i + 1], -1*lowerBound[0]);

                productionConstraint[5][j][i] = solver.makeConstraint(0, infinity);
                productionConstraint[5][j][i].setCoefficient(fGox[j][i], -1);
                productionConstraint[5][j][i].setCoefficient(subY[j][i + 1], upperBound[0]);

                productionConstraint[6][j][i] = solver.makeConstraint(0, infinity);
                productionConstraint[6][j][i].setCoefficient(fLar[j][i], 1);
                productionConstraint[6][j][i].setCoefficient(subY[j][i + 1], -1*lowerBound[1]);

                productionConstraint[7][j][i] = solver.makeConstraint(0, infinity);
                productionConstraint[7][j][i].setCoefficient(fLar[j][i], -1);
                productionConstraint[7][j][i].setCoefficient(subY[j][i+1], upperBound[1]);

                productionConstraint[8][j][i] = solver.makeConstraint(0, 0);
                productionConstraint[8][j][i].setCoefficient(fAir[j][i], 1);
                productionConstraint[8][j][i].setCoefficient(subY[j][i+1], -1 * airs[j]);
                productionConstraint[8][j][i].setCoefficient(lambda[j][i], airs[j] - airs[j-1]);

                productionConstraint[9][j][i] = solver.makeConstraint(0, 0);
                productionConstraint[9][j][i].setCoefficient(qElectricityMode[j][i], 1);
                productionConstraint[9][j][i].setCoefficient(subY[j][i+1], -1 * electricity[j]);
                productionConstraint[9][j][i].setCoefficient(lambda[j][i], electricity[j] - electricity[j-1]);

                productionConstraint[10][j][i] = solver.makeConstraint(-1 * infinity, 0);
                productionConstraint[10][j][i].setCoefficient(subY[j][i+1], -1);
                productionConstraint[10][j][i].setCoefficient(lambda[j][i], 1);

                transferConstraint[0][j][i] = solver.makeConstraint(0,0);
                transferConstraint[0][j][i].setCoefficient(subY[j][i+1], -1);
                for (int k=0;k<modeNum;k++){
                    transferConstraint[0][j][i].setCoefficient(z[k][j][i], 1);
                }

                transferConstraint[1][j][i] = solver.makeConstraint(0,0);
                transferConstraint[1][j][i].setCoefficient(subY[j][i], -1);
                for (int k=0;k<modeNum;k++){
                    transferConstraint[1][j][i].setCoefficient(z[j][k][i], 1);
                }
            }
            totalConstraint[0][i] = solver.makeConstraint(0,0);
            totalConstraint[0][i].setCoefficient(totalAir[i], 1);

            totalConstraint[1][i] = solver.makeConstraint(0,0);
            totalConstraint[1][i].setCoefficient(totalGox[i], 1);

            totalConstraint[2][i] = solver.makeConstraint(0,0);
            totalConstraint[2][i].setCoefficient(totalGan[i], 1);

            totalConstraint[3][i] = solver.makeConstraint(0,0);
            totalConstraint[3][i].setCoefficient(totalGar[i], 1);

            totalConstraint[4][i] = solver.makeConstraint(0,0);
            totalConstraint[4][i].setCoefficient(totalLox[i], 1);

            totalConstraint[5][i] = solver.makeConstraint(0,0);
            totalConstraint[5][i].setCoefficient(totalLin[i], 1);

            totalConstraint[6][i] = solver.makeConstraint(0,0);
            totalConstraint[6][i].setCoefficient(totalLar[i], 1);

            totalConstraint[7][i] = solver.makeConstraint(0,0);
            totalConstraint[7][i].setCoefficient(qElectricity[i], 1);

            for (int k=0;k<modeNum;k++){
                totalConstraint[0][i].setCoefficient(fAir[k][i], -1);
                totalConstraint[1][i].setCoefficient(fGox[k][i], -1);
                totalConstraint[2][i].setCoefficient(fGan[k][i], -1);
                totalConstraint[3][i].setCoefficient(fGar[k][i], -1);
                totalConstraint[4][i].setCoefficient(fLox[k][i], -1);
                totalConstraint[5][i].setCoefficient(fLin[k][i], -1);
                totalConstraint[6][i].setCoefficient(fLar[k][i], -1);
                totalConstraint[7][i].setCoefficient(qElectricityMode[k][i], -1);
            }

            mode[i] = solver.makeConstraint(1,1);
            for (int j=0;j<modeNum;j++){
                mode[i].setCoefficient(subY[j][i+1], 1);
            }
        }
        totalCost[0] = solver.makeConstraint(0 ,0);
        totalCost[0].setCoefficient(totalElectricity, 1);
        totalCost[1] = solver.makeConstraint(0 ,0);
        totalCost[1].setCoefficient(startup, 1);
        for (int i=0;i<timeLength;i++){
            totalCost[0].setCoefficient(qElectricity[i], -1);
            for (int j=1;j<modeNum;j++){
                totalCost[1].setCoefficient(z[0][j][i], -1 * startupCost);
            }
        }

        for (int i=0;i<modeNum;i++){
            initConstraint[i] = solver.makeConstraint(initStatus[i], initStatus[i]);
            initConstraint[i].setCoefficient(subY[i][0], 1);
        }
    }
}
