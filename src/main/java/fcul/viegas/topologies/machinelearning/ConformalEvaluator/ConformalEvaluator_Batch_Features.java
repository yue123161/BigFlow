package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import fcul.viegas.bigflow.math.MathUtils;
import io.netty.util.internal.MathUtil;
import weka.core.Instance;
import weka.core.Instances;

public class ConformalEvaluator_Batch_Features {

    public class MathUtils {

        private int count = 0;
        private double sum = 0.0d;
        private double sumSq = 0.0d;

        public MathUtils(){
            this.count = 0;
            this.sum = 0.0d;
            this.sumSq = 0.0d;
        }


        public void addNumber(double number){
            this.count += 1;
            this.sum += number;
            this.sumSq += number*number;
        }

        public double getAverage(){
            if(this.count == 0){
                return 0.0d;
            }
            return (this.sum/(float)this.count);
        }

        public double getVariance() {
            double deviation = this.getStandardDeviation();
            deviation = deviation*deviation;
            if(Double.isNaN(deviation)){
                return 0.0d;
            }
            return deviation;
        }

        public double getStandardDeviation(){
            double deviation = 0.0f;
            if(this.count > 1){
                deviation = Math.sqrt((this.sumSq - this.sum*this.sum/this.count)/((float)this.count-1));
            }
            if(Double.isNaN(deviation)){
                return 0.0d;
            }
            return deviation;
        }

        public int getCount() {
            return count;
        }

        public double getSum() {
            return sum;
        }

    }

    private MathUtils[] featureValuesAverageNormal;
    private MathUtils[] featureValuesAverageAttack;

    public ConformalEvaluator_Batch_Features(){

    }

    public void buildConformalEvaluator_Batch_Features(Instances insts){
        this.featureValuesAverageNormal = new MathUtils[insts.numAttributes()];
        this.featureValuesAverageAttack = new MathUtils[insts.numAttributes()];

        for(int i  =0 ; i < insts.numAttributes(); i++){
            this.featureValuesAverageNormal[i] = new MathUtils();
            this.featureValuesAverageAttack[i] = new MathUtils();
        }


        for(Instance inst : insts){
            for(int i = 0; i < inst.numAttributes() - 1; i++){
                double featValue = inst.toDoubleArray()[i];
                if(inst.classValue() == 0.0d){
                    this.featureValuesAverageNormal[i].addNumber(featValue);
                }else{
                    this.featureValuesAverageAttack[i].addNumber(featValue);
                }
            }
        }
    }

    public double[] getFeatureStatistics(Instance inst, double classifiedClass){
        double[] distRet = new double[inst.numAttributes() + 5];
        double avgDist = 0.0d;
        double maxDist = 0.0d;
        MathUtils mathUtils = new MathUtils();
        for(int i = 0; i < inst.numAttributes() - 1; i++){
            if(classifiedClass == 0.0d){
                double std = this.featureValuesAverageNormal[i].getStandardDeviation();
                double avg = this.featureValuesAverageNormal[i].getAverage();
                distRet[i] = avg - inst.toDoubleArray()[i];
                //if(inst.toDoubleArray()[i] < (std-avg) || inst.toDoubleArray()[i] > (std+avg)){
                //    outsideScopePCT++;
                //}
                mathUtils.addNumber(Math.abs(distRet[i]));
                avgDist += Math.abs(distRet[i]);
                if(Math.abs(distRet[i]) > maxDist){
                    maxDist = Math.abs(distRet[i]);
                }
            }else{
                double std = this.featureValuesAverageAttack[i].getStandardDeviation();
                double avg = this.featureValuesAverageAttack[i].getAverage();
                distRet[i] = avg - inst.toDoubleArray()[i];
                //if(inst.toDoubleArray()[i] <= (std-avg) || inst.toDoubleArray()[i] >= (std+avg)){
                //    outsideScopePCT++;
                //}
                mathUtils.addNumber(Math.abs(distRet[i]));
                avgDist += Math.abs(distRet[i]);
                if(Math.abs(distRet[i]) > maxDist){
                    maxDist = Math.abs(distRet[i]);
                }
            }
        }
        distRet[distRet.length - 5] = avgDist / (float) (inst.numAttributes() - 1);
        distRet[distRet.length - 4] = maxDist;
        distRet[distRet.length - 3] = mathUtils.getAverage();
        distRet[distRet.length - 2] = mathUtils.getStandardDeviation();
        distRet[distRet.length - 1] = mathUtils.getVariance();
        return distRet;
    }
}
