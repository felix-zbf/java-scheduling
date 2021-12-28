package com.control.zju.utils;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import lombok.Data;

/**
 * @author zbf
 * @date 2021/12/26
 * @description 氮压机
 */
// todo: 氮压机模态切换约束；

@Data
public class NCompressor {
    int number;
    int timeLength;
    double lowerBound;
    double upperBound;
    double electricity;
    double infinity = Double.POSITIVE_INFINITY;
    MPSolver solver;

    String asuNum;
    String reuseAsu;

    MPVariable[] fGas;
    MPVariable[] y;
    MPVariable[] qElectricity;
    MPVariable totalElectricity;

    MPConstraint[] fGasLoad1;
    MPConstraint[] fGasLoad2;
    MPConstraint[] calElectricity;
    MPConstraint calTotalElectricity;

    public NCompressor(int length){
        this.timeLength = length;

        this.fGas = new MPVariable[timeLength];
        this.y = new MPVariable[timeLength];
        this.qElectricity = new MPVariable[timeLength];

        this.fGasLoad1 = new MPConstraint[timeLength];
        this.fGasLoad2 = new MPConstraint[timeLength];
        this.calElectricity = new MPConstraint[timeLength];
    }

    public void createVar(){
        for (int i=0;i<timeLength;i++){
            fGas[i] = solver.makeNumVar(0, infinity,"");
            y[i] = solver.makeBoolVar("");
            qElectricity[i] = solver.makeNumVar(0, infinity, "");
        }
        totalElectricity = solver.makeNumVar(0, infinity,"");
    }

    public void createConstraint(){
        calTotalElectricity = solver.makeConstraint(0,0);
        calTotalElectricity.setCoefficient(totalElectricity, 1);
        for (int i=0;i<timeLength;i++){
            fGasLoad1[i] = solver.makeConstraint(0, infinity);
            fGasLoad1[i].setCoefficient(fGas[i], 1);
            fGasLoad1[i].setCoefficient(y[i], -lowerBound);

            fGasLoad2[i] = solver.makeConstraint(0, infinity);
            fGasLoad2[i].setCoefficient(fGas[i], -1);
            fGasLoad2[i].setCoefficient(y[i], upperBound);

            calElectricity[i] = solver.makeConstraint(0, 0);
            calElectricity[i].setCoefficient(qElectricity[i], 1);
            calElectricity[i].setCoefficient(fGas[i], -electricity);

            calTotalElectricity.setCoefficient(qElectricity[i], -1);
        }
    }
}
