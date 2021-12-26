//import com.control.zju.utils.ASU;
//import com.google.ortools.Loader;
//import com.google.ortools.linearsolver.MPConstraint;
//import com.google.ortools.linearsolver.MPObjective;
//import com.google.ortools.linearsolver.MPSolver;
//import com.google.ortools.linearsolver.MPVariable;
//import org.junit.Test;
//
//public class testOrtools {
//    @Test
//    public void test1(){
//        Loader.loadNativeLibraries();
//        MPSolver solver = new MPSolver(
//                "ProductionSchedulingDemo", MPSolver.OptimizationProblemType.SCIP_MIXED_INTEGER_PROGRAMMING);
//        double infinity = java.lang.Double.POSITIVE_INFINITY;
//
//        // x and y are continuous non-negative variables.
//
//
//        MPVariable y = solver.makeNumVar(0.0, infinity, "y");
//        System.out.println("Number of variables = " + solver.numVariables());
//        ASU a = new ASU(2);
//        a.setSolver(solver);
//        a.createVar();
//        a.createConstraint();
//        // x + 2*y <= 14.
//        MPConstraint c0 = solver.makeConstraint(-infinity, 14.0, "c0");
//        c0.setCoefficient(a.getX(), 1);
//        c0.setCoefficient(y, 2);
//
//        // 3*x - y >= 0.
//        MPConstraint c1 = solver.makeConstraint(0.0, infinity, "c1");
//        c1.setCoefficient(a.getX(), 3);
//        c1.setCoefficient(y, -1);
//
//        // x - y <= 2.
//        MPConstraint c2 = solver.makeConstraint(-infinity, 2.0, "c2");
//        c2.setCoefficient(a.getX(), 1);
//        c2.setCoefficient(y, -1);
//        System.out.println("Number of constraints = " + solver.numConstraints());
//
//        // Maximize 3 * x + 4 * y.
//        MPObjective objective = solver.objective();
//        objective.setCoefficient(a.getX(), 3);
//        objective.setCoefficient(y, 4);
//        objective.setMaximization();
//
//        final MPSolver.ResultStatus resultStatus = solver.solve();
//        // Check that the problem has an optimal solution.
//        if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
//            System.err.println("The problem does not have an optimal solution!");
//            return;
//        }
//
//        // The value of each variable in the solution.
//        System.out.println("Solution");
//        System.out.println("x = " + a.getX().solutionValue());
//        System.out.println("y = " + y.solutionValue());
//
//        // The objective value of the solution.
//        System.out.println("Optimal objective value = " + solver.objective().value());
//    }
//}
