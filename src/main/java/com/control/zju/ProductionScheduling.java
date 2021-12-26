package com.control.zju;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.control.zju.utils.*;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zbf
 * @date 2021/12/26
 * @description 主函数 & 测试单元
 */

import static com.google.ortools.linearsolver.MPSolverParameters.DoubleParam.RELATIVE_MIP_GAP;

public class ProductionScheduling {
    public String productionSchedulingModel(){
        // todo: 主函数编写

        return "";
    }

    public static void main(String[] args){
        // todo: 测试函数
        int simulationTime = 5;
        double infinity = java.lang.Double.POSITIVE_INFINITY;
        Loader.loadNativeLibraries();
        MPSolver solver = new MPSolver("ProductionSchedulingDemo",
                MPSolver.OptimizationProblemType.SCIP_MIXED_INTEGER_PROGRAMMING);

        double[][] demand = new double[][]{{40000, 41000, 40000, 39000, 40000}, {30000, 30000, 30000, 30000, 30000},
                {80000, 80000, 80000, 80000, 80000}, {1200, 1200, 1200, 1200, 1200}};

        List<ASU> asuList1 = new ArrayList<>();
        List<ASU> asuList2 = new ArrayList<>();
        List<PSA> psaList = new ArrayList<>();

        // 1# 空分装置
        ASU asu1 = new ASU(simulationTime, 4);
        asu1.setSolver(solver);
        asu1.setTimeLength(simulationTime);
        asu1.setModeNum(4);
        asu1.setParameters(new double[]{-4.45, 4.12, -19000, 0.009, 420, 0.4607, -5050, 0.0074, 50});
        asu1.setAirs(new double[]{90000, 100000, 110000, 120000});
        asu1.setElectricity(new double[]{7020, 7800, 8570, 9350});
        asu1.setLowerBound(new double[]{18000, 100});
        asu1.setUpperBound(new double[]{21000, 900});
        asu1.setStartupCost(133072);
        asu1.setInitStatus(new int[]{0, 1, 0, 0});
        asu1.createVar();
        asu1.createConstraint();
        asuList1.add(asu1);

        // 2# 空分装置
        ASU asu2 = new ASU(simulationTime, 4);
        asu2.setSolver(solver);
        asu2.setTimeLength(simulationTime);
        asu2.setModeNum(4);
        asu2.setParameters(new double[]{-4.3, 3.98, -21800, 0.009, 420, 0.486, -9200, 0.0074, 145});
        asu2.setAirs(new double[]{90000, 100000, 110000, 120000});
        asu2.setElectricity(new double[]{7650, 8500, 9300, 10130});
        asu2.setLowerBound(new double[]{18000, 100});
        asu2.setUpperBound(new double[]{21000, 900});
        asu2.setStartupCost(80000);
        asu2.setInitStatus(new int[]{0, 1, 0, 0});
        asu2.createVar();
        asu2.createConstraint();
        asuList1.add(asu2);

        // 3# 空分装置
        ASU asu3 = new ASU(simulationTime, 4);
        asu3.setSolver(solver);
        asu3.setTimeLength(simulationTime);
        asu3.setModeNum(4);
        asu3.setParameters(new double[]{-4.384, 4.06, -13370, 0.009, 1370, 0.43, -2180, 0.0073, 137});
        asu3.setAirs(new double[]{90000, 100000, 110000, 120000});
        asu3.setElectricity(new double[]{8000, 10000, 10900, 11850});
        asu3.setLowerBound(new double[]{18000, 100});
        asu3.setUpperBound(new double[]{21000, 900});
        asu3.setStartupCost(133072);
        asu3.setInitStatus(new int[]{0, 1, 0, 0});
        asu3.createVar();
        asu3.createConstraint();
        asuList2.add(asu3);

        // 4# 空分装置
        ASU asu4 = new ASU(simulationTime, 4);
        asu4.setSolver(solver);
        asu4.setTimeLength(simulationTime);
        asu4.setModeNum(4);
        asu4.setParameters(new double[]{-4.42, 3.9, -30500, 0.0063, 425, 0.6, -2689.57, 0.009463, 294});
        asu4.setAirs(new double[]{130000, 150000, 160000, 170000});
        asu4.setElectricity(new double[]{11310, 13050, 13900, 14760});
        asu4.setLowerBound(new double[]{26000, 200});
        asu4.setUpperBound(new double[]{31000, 900});
        asu4.setStartupCost(224037);
        asu4.setInitStatus(new int[]{0, 0, 1, 0});
        asu4.createVar();
        asu4.createConstraint();
        asuList2.add(asu4);

        for (int i=0;i<4;i++){
            PSA psa = new PSA(simulationTime);
            psa.setSolver(solver);
            psa.setTimeLength(simulationTime);
            psa.setInitStatus(new int[]{0,1});
            psa.createVar();
            psa.createConstraint();
            psaList.add(psa);
        }

        List<ExternalSupply> externalSupplieList = new ArrayList<>();
        ExternalSupply hy = new ExternalSupply(simulationTime);
        hy.setSolver(solver);
        hy.setTimeLength(simulationTime);
        hy.setCost(0.419);
        hy.setLowerBound(new double[]{26000, 40000, 1000});
        hy.setUpperBound(new double[]{30000, 40000, 1000});
        hy.createVar();
        hy.createConstraint();
        externalSupplieList.add(hy);

        ExternalSupply nh = new ExternalSupply(simulationTime);
        nh.setSolver(solver);
        nh.setCost(0.55);
        nh.setTimeLength(simulationTime);
        nh.setLowerBound(new double[]{0,0,0});
        nh.setUpperBound(new double[]{15000,0,0});
        nh.createVar();
        nh.createConstraint();
        externalSupplieList.add(nh);

        // 液化器
        List<LIQ> liqList = new ArrayList<>();
        LIQ liq1 = new LIQ(simulationTime);
        liq1.setSolver(solver);
        liq1.setTimeLength(simulationTime);
        liq1.setLoadGox(2400);
        liq1.setLoadGan(2200);
        liq1.setGan(11000);
        liq1.setStartupCost(2000);
        liq1.setInitStatus(new int[]{1, 0});
        liq1.createVar();
        liq1.createConstraint();
        liqList.add(liq1);

        LIQ liq2 = new LIQ(simulationTime);
        liq2.setSolver(solver);
        liq2.setTimeLength(simulationTime);
        liq2.setLoadGox(2400);
        liq2.setLoadGan(2200);
        liq2.setGan(11000);
        liq2.setStartupCost(2000);
        liq2.setInitStatus(new int[]{1, 0});
        liq2.createVar();
        liq2.createConstraint();
        liqList.add(liq2);

        // 气化器
        List<VAP> vapList = new ArrayList<>();
        VAP vap1 = new VAP(simulationTime);
        vap1.setSolver(solver);
        vap1.setTimeLength(simulationTime);
        vap1.setLoad(20000);
        vap1.setStartupCost(2000);
        vap1.setInitStatus(new int[]{1,0});
        vap1.createVar();
        vap1.createConstraint();
        vapList.add(vap1);

        VAP vap2 = new VAP(simulationTime);
        vap2.setSolver(solver);
        vap2.setTimeLength(simulationTime);
        vap2.setLoad(20000);
        vap2.setStartupCost(2000);
        vap2.setInitStatus(new int[]{1,0});
        vap2.createVar();
        vap2.createConstraint();
        vapList.add(vap2);

        VAP vap3 = new VAP(simulationTime);
        vap3.setSolver(solver);
        vap3.setTimeLength(simulationTime);
        vap3.setLoad(30000);
        vap3.setStartupCost(4000);
        vap3.setInitStatus(new int[]{1,0});
        vap3.createVar();
        vap3.createConstraint();
        vapList.add(vap3);

        VAP vap4 = new VAP(simulationTime);
        vap4.setSolver(solver);
        vap4.setTimeLength(simulationTime);
        vap4.setLoad(20000);
        vap4.setStartupCost(2000);
        vap4.setInitStatus(new int[]{1,0});
        vap4.createVar();
        vap4.createConstraint();
        vapList.add(vap4);

        VAP vap5 = new VAP(simulationTime);
        vap5.setSolver(solver);
        vap5.setTimeLength(simulationTime);
        vap5.setLoad(30000);
        vap5.setStartupCost(4000);
        vap5.setInitStatus(new int[]{1,0});
        vap5.createVar();
        vap5.createConstraint();
        vapList.add(vap5);

        // 氮压机
        List<NCompressor> nCompressorList = new ArrayList<>();
        NCompressor nCompressor1 = new NCompressor(simulationTime);
        nCompressor1.setSolver(solver);
        nCompressor1.setTimeLength(simulationTime);
        nCompressor1.setLowerBound(16000);
        nCompressor1.setUpperBound(20000);
        nCompressor1.setElectricity(0.17);
        nCompressor1.createVar();
        nCompressor1.createConstraint();
        nCompressorList.add(nCompressor1);

        NCompressor nCompressor2 = new NCompressor(simulationTime);
        nCompressor2.setSolver(solver);
        nCompressor2.setTimeLength(simulationTime);
        nCompressor2.setLowerBound(16000);
        nCompressor2.setUpperBound(20000);
        nCompressor2.setElectricity(0.165);
        nCompressor2.createVar();
        nCompressor2.createConstraint();
        nCompressorList.add(nCompressor2);

        NCompressor nCompressor3 = new NCompressor(simulationTime);
        nCompressor3.setSolver(solver);
        nCompressor3.setTimeLength(simulationTime);
        nCompressor3.setLowerBound(16000);
        nCompressor3.setUpperBound(20000);
        nCompressor3.setElectricity(0.153);
        nCompressor3.createVar();
        nCompressor3.createConstraint();
        nCompressorList.add(nCompressor3);

        NCompressor nCompressor4 = new NCompressor(simulationTime);
        nCompressor4.setSolver(solver);
        nCompressor4.setTimeLength(simulationTime);
        nCompressor4.setLowerBound(16000);
        nCompressor4.setUpperBound(20000);
        nCompressor4.setElectricity(0.152);
        nCompressor4.createVar();
        nCompressor4.createConstraint();
        nCompressorList.add(nCompressor4);

        NCompressor nCompressor5 = new NCompressor(simulationTime);
        nCompressor5.setSolver(solver);
        nCompressor5.setTimeLength(simulationTime);
        nCompressor5.setLowerBound(16000);
        nCompressor5.setUpperBound(20000);
        nCompressor5.setElectricity(0.154);
        nCompressor5.createVar();
        nCompressor5.createConstraint();
        nCompressorList.add(nCompressor5);

        NCompressor nCompressor6 = new NCompressor(simulationTime);
        nCompressor6.setSolver(solver);
        nCompressor6.setTimeLength(simulationTime);
        nCompressor6.setLowerBound(8000);
        nCompressor6.setUpperBound(10000);
        nCompressor6.setElectricity(0.113);
        nCompressor6.createVar();
        nCompressor6.createConstraint();
        nCompressorList.add(nCompressor6);

        NCompressor nCompressor7 = new NCompressor(simulationTime);
        nCompressor7.setSolver(solver);
        nCompressor7.setTimeLength(simulationTime);
        nCompressor7.setLowerBound(8000);
        nCompressor7.setUpperBound(10000);
        nCompressor7.setElectricity(0.116);
        nCompressor7.createVar();
        nCompressor7.createConstraint();
        nCompressorList.add(nCompressor7);

        NCompressor nCompressor8 = new NCompressor(simulationTime);
        nCompressor8.setSolver(solver);
        nCompressor8.setTimeLength(simulationTime);
        nCompressor8.setLowerBound(8000);
        nCompressor8.setUpperBound(10000);
        nCompressor8.setElectricity(0.107);
        nCompressor8.createVar();
        nCompressor8.createConstraint();
        nCompressorList.add(nCompressor8);

        MPVariable[] MTNGox = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] MTNGan = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] VentGox1 = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] VentGox2 = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] VentGan1 = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] VentGan2 = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] VentGar = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] SLT1 = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] SLT2 = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] saleLox = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] saleLin = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] saleLar = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] buyLox = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] buyLin = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] selfLox = solver.makeVarArray(simulationTime, 0, infinity, false);
        MPVariable[] selfLin = solver.makeVarArray(simulationTime, 0, infinity, false);

        MPConstraint[] lGox = new MPConstraint[simulationTime];
        MPConstraint[] mGox = new MPConstraint[simulationTime];
        MPConstraint[] lGan = new MPConstraint[simulationTime];
        MPConstraint[] mGan = new MPConstraint[simulationTime];
        MPConstraint[] gar = new MPConstraint[simulationTime];
        MPConstraint[] nCompressorConstraint1 = new MPConstraint[simulationTime];
        MPConstraint[] nCompressorConstraint2 = new MPConstraint[simulationTime];
        MPConstraint[] loxBalance1 = new MPConstraint[simulationTime];
        MPConstraint[] loxBalance2 = new MPConstraint[simulationTime];
        MPConstraint[] linBalance1 = new MPConstraint[simulationTime];
        MPConstraint[] linBalance2 = new MPConstraint[simulationTime];
        MPConstraint[] larBalance = new MPConstraint[simulationTime];

        for (int i=0;i<simulationTime;i++){
            lGox[i] = solver.makeConstraint(demand[0][i], demand[0][i]);
            mGox[i] = solver.makeConstraint(demand[1][i], demand[1][i]);
            lGan[i] = solver.makeConstraint(demand[2][i], demand[2][i]);
            mGan[i] = solver.makeConstraint(0, 0);
            gar[i] = solver.makeConstraint(demand[3][i], demand[3][i]);
            nCompressorConstraint1[i] = solver.makeConstraint(0,0);
            nCompressorConstraint2[i] = solver.makeConstraint(0,0);
            loxBalance1[i] = solver.makeConstraint(0,0);
            loxBalance2[i] = solver.makeConstraint(0,0);
            linBalance1[i] = solver.makeConstraint(0,0);
            linBalance2[i] = solver.makeConstraint(0,0);
            larBalance[i] = solver.makeConstraint(0,0);

            lGox[i].setCoefficient(MTNGox[i], 1);
            lGox[i].setCoefficient(VentGox1[i],-1);
            mGox[i].setCoefficient(MTNGox[i], -1);
            mGox[i].setCoefficient(VentGox2[i],-1);

            lGan[i].setCoefficient(MTNGan[i], 1);
            lGan[i].setCoefficient(VentGan1[i], -1);
            mGan[i].setCoefficient(MTNGan[i], -1);
            mGan[i].setCoefficient(VentGan2[i], -1);

            gar[i].setCoefficient(VentGar[i], -1);

            for (int j=0;j<2;j++){
                lGox[i].setCoefficient(asuList1.get(j).getTotalGox()[i], 1);
                gar[i].setCoefficient(asuList1.get(j).getTotalGar()[i],1);
                loxBalance1[i].setCoefficient(asuList1.get(j).getTotalLox()[i], 1);
                linBalance1[i].setCoefficient(asuList1.get(j).getTotalLin()[i], 1);
                larBalance[i].setCoefficient(asuList1.get(j).getTotalLar()[i],1);
            }
            for (int j=0;j<2;j++){
                mGox[i].setCoefficient(asuList2.get(j).getTotalGox()[i],1);
                gar[i].setCoefficient(asuList2.get(j).getTotalGar()[i],1);
                loxBalance1[i].setCoefficient(asuList2.get(j).getTotalLox()[i], 1);
                linBalance1[i].setCoefficient(asuList2.get(j).getTotalLin()[i], 1);
                larBalance[i].setCoefficient(asuList2.get(j).getTotalLar()[i],1);
            }
            for (int j=0;j<4;j++){
                lGox[i].setCoefficient(psaList.get(j).getTotalGox()[i], 1);
            }
            for (int j=0;j<2;j++){
                mGox[i].setCoefficient(externalSupplieList.get(j).getGox()[i], 1);
                lGan[i].setCoefficient(externalSupplieList.get(j).getGan()[i], 1);
                gar[i].setCoefficient(externalSupplieList.get(j).getGar()[i], 1);
            }
            for (int j=0;j<2;j++){
                mGox[i].setCoefficient(liqList.get(j).getTotalProduceGox()[i],-1);
                mGan[i].setCoefficient(liqList.get(j).getTotalProduceGan()[i],-1);
                loxBalance1[i].setCoefficient(liqList.get(j).getTotalProduceGox()[i], 1);
                linBalance1[i].setCoefficient(liqList.get(j).getTotalProduceGan()[i], 1);
            }
            for (int j=0;j<3;j++){
                mGox[i].setCoefficient(vapList.get(j).getTotalGas()[i],1);
                loxBalance2[i].setCoefficient(vapList.get(j).getTotalGas()[i], 1);
            }
            for (int j=3;j<5;j++){
                mGan[i].setCoefficient(vapList.get(j).getTotalGas()[i],1);
                linBalance2[i].setCoefficient(vapList.get(j).getTotalGas()[i], 1);
            }

            nCompressorConstraint1[i].setCoefficient(asuList1.get(0).getTotalGan()[i], 1);
            nCompressorConstraint1[i].setCoefficient(asuList2.get(1).getTotalGan()[i], 1);
            nCompressorConstraint1[i].setCoefficient(nCompressorList.get(0).getFGas()[i], -1);
            nCompressorConstraint1[i].setCoefficient(nCompressorList.get(3).getFGas()[i], -1);
            nCompressorConstraint1[i].setCoefficient(nCompressorList.get(4).getFGas()[i], -1);
            nCompressorConstraint1[i].setCoefficient(nCompressorList.get(5).getFGas()[i], -1);
            nCompressorConstraint1[i].setCoefficient(SLT1[i], -1);

            nCompressorConstraint2[i].setCoefficient(asuList1.get(1).getTotalGan()[i], 1);
            nCompressorConstraint2[i].setCoefficient(asuList2.get(0).getTotalGan()[i], 1);
            nCompressorConstraint2[i].setCoefficient(nCompressorList.get(1).getFGas()[i], -1);
            nCompressorConstraint2[i].setCoefficient(nCompressorList.get(2).getFGas()[i], -1);
            nCompressorConstraint2[i].setCoefficient(nCompressorList.get(6).getFGas()[i], -1);
            nCompressorConstraint2[i].setCoefficient(nCompressorList.get(7).getFGas()[i], -1);
            nCompressorConstraint2[i].setCoefficient(SLT2[i], -1);

            for (int j=0;j<5;j++){
                mGan[i].setCoefficient(nCompressorList.get(j).getFGas()[i], 1);
            }
            for (int j=5;j<8;j++){
                lGan[i].setCoefficient(nCompressorList.get(j).getFGas()[i], 1);
            }

            loxBalance1[i].setCoefficient(saleLox[i], -1);
            loxBalance1[i].setCoefficient(selfLox[i], -1);

            linBalance1[i].setCoefficient(saleLin[i], -1);
            linBalance1[i].setCoefficient(selfLin[i], -1);

            loxBalance2[i].setCoefficient(selfLox[i],-1);
            loxBalance2[i].setCoefficient(buyLox[i],-1);

            linBalance2[i].setCoefficient(selfLin[i],-1);
            linBalance2[i].setCoefficient(buyLin[i],-1);

            larBalance[i].setCoefficient(saleLar[i], -1);
        }

        MPObjective objective = solver.objective();

        for (int j=0;j<2;j++){
            objective.setCoefficient(asuList1.get(j).getTotalElectricity(), -0.6);
            objective.setCoefficient(asuList1.get(j).getStartup(), -0.6);
        }

        for (int j=0;j<2;j++){
            objective.setCoefficient(asuList2.get(j).getTotalElectricity(), -0.6);
            objective.setCoefficient(asuList2.get(j).getStartup(), -0.6);
        }

        for (int j=0;j<4;j++){
            objective.setCoefficient(psaList.get(j).getTotalElectricity(), -0.6);
            objective.setCoefficient(psaList.get(j).getTotalStartup(), -0.6);
        }

        for (int j=0;j<2;j++){
            objective.setCoefficient(externalSupplieList.get(j).getTotalCost(), -1);
        }

        for (int j=0;j<8;j++){
            objective.setCoefficient(nCompressorList.get(j).getTotalElectricity(), -0.6);
        }

        for (int i=0;i<simulationTime;i++){
            objective.setCoefficient(saleLox[i], 1 / 804 * 1.143 * 430);
            objective.setCoefficient(saleLin[i], 1 / 646 * 0.8083 * 650);
            objective.setCoefficient(saleLar[i], 1 / 785 * 1.4 * 1250);
            objective.setCoefficient(buyLox[i], 1 / 804 * 1.143 * 1600);
            objective.setCoefficient(buyLin[i], 1 / 646 * 0.8083 * 650);
        }

        objective.setMaximization();

        MPSolverParameters solverParameters = new MPSolverParameters();
        solverParameters.setDoubleParam(RELATIVE_MIP_GAP, 0.01);
        solver.setTimeLimit(600);

        final MPSolver.ResultStatus resultStatus = solver.solve(solverParameters);

        System.out.println(resultStatus);
        System.out.println(asuList1.get(0).getTotalGox()[0].solutionValue());
        System.out.println(asuList1.get(1).getTotalGox()[0].solutionValue());
        System.out.println(asuList2.get(0).getTotalGox()[0].solutionValue());
        System.out.println(asuList2.get(1).getTotalGox()[0].solutionValue());
    }
}
