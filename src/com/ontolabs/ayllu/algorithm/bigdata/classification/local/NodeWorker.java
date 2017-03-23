package com.ontolabs.ayllu.algorithm.bigdata.classification.local;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ontolabs.ayllu.data.DataSet;
import com.ontolabs.ayllu.data.DataVector;
import com.ontolabs.ayllu.data.DataVectorWeightComparator;
import com.ontolabs.ayllu.grid.LearnerNode;
import com.ontolabs.ayllu.grid.classifier.ClassifierNode;
import com.ontolabs.ayllu.vendor.VendorFactory;



/**
 * The Class NodeWorker is a @see Callable implementation for parallelizing
 * learning of the @see LearnerNode  either in training or selection phase.
 */
public class NodeWorker implements Callable<NodeWorker>
{
	 private final Logger logger = LoggerFactory.getLogger(NodeWorker.class);
    /** The Constant WORK_TRAIN. */
    public static final int WORK_TRAIN  = 1;    // Train the instances
    
    /** The Constant WORK_SELECT. */
    public static final int WORK_SELECT = 2;    // Test instances on neighbors. Select instances for next epoch.
    
    /** The Constant WORK_ASYNC. */
    public static final int WORK_ASYNC  = 3;    // Train / Test on neighbors / Select instances / Replace instances

    private int            work;
    protected ClassifierNode node;
    protected List<? extends LearnerNode> neighbors;
    // ------
    private Random         random;
    private long           duration;
    private double replacement;
    protected VendorFactory factory;

    /**
     * Instantiates a new node worker.
     *
     * @param node the node
     * @param neighbors the neighbors
     * @param replacementPercentage the replacement percentage
     * @param factory the factory
     */
    public NodeWorker(LearnerNode node, List<? extends LearnerNode> neighbors, double replacementPercentage, VendorFactory factory)
    {
        this.node = (ClassifierNode)node;
        this.neighbors = neighbors;
        this.random = new Random(System.currentTimeMillis() % this.hashCode());
        this.replacement = replacementPercentage;
        this.factory = factory;
    }

    /**
     * Sets the work.
     *
     * @param work the new work
     */
    public void setWork(int work)
    {
        this.work = work;
    }

    public NodeWorker call() throws Exception
    {
        long tbeg, tend, spent;

        tbeg = System.currentTimeMillis();
        try
        {
            // Execute either the Classification training or Instance selection step
            if      (this.work == WORK_TRAIN)  train();
            else if (this.work == WORK_SELECT) selectSpatialProportional();
           
        }
        catch(Exception ex)
        {
            // Debug... Multi-threading issues are tricky.
            ex.printStackTrace();

            // ? Re-throw to make sure the Threadpool kills off the failed thread.
            //throw(ex);
        }

        // Remember the time spent
        tend = System.currentTimeMillis();
        spent = tend-tbeg;
		logger.info("Node = " + this.hashCode() + ", Work = " + this.work + ", timeSpent= " +  spent + " msec");
        this.duration += spent;

        return this;
    }

    /**
     * Train.
     *
     * @throws Exception the exception
     */
    public void train() throws Exception
    {
        this.node.train();
    }

    /**
     * Gets the time spent.
     *
     * @return the time spent
     */
    public long getTimeSpent()
    {
        Long spent = this.duration;
        this.duration = 0l;

        return(spent);
    }

    /**
     * Gets the node.
     *
     * @return the node
     */
    public ClassifierNode getNode() { return this.node; }

    // ***************************************************************\
    // * Replace with proportional selection from neighbor instances *
    // ***************************************************************/
    private void selectSpatialProportional() throws Exception
    {
        TreeSet<DataVector>    weightOrderInstances;
        TreeMap<Double, DataVector> sampleInstance;
        List<DataVector>    zeroWeightInstances;
        double            weightSum;
        List<DataVector>    replaceSelect;

        // Test the instances of this Node on the neighboring classifiers. Weight them accordingly.
        testInstancesOnNeighbors();

        // Collect all Instances from this Node and its neighboring nodes, ordered from high to low weight.
        weightOrderInstances = new TreeSet<DataVector>(new DataVectorWeightComparator());
        for(DataVector instance: node.getData())
            weightOrderInstances.add(instance);
        for(LearnerNode neighbor: neighbors)
            for(DataVector instance: neighbor.getData())
                weightOrderInstances.add(instance);

        // Put instances in a TreeMap with as key the cumulative weight starting with highest weight instances
        sampleInstance      = new TreeMap<Double, DataVector>();
        zeroWeightInstances = new LinkedList<DataVector>();
        weightSum = 0;
        for(DataVector instance: weightOrderInstances)
        {
            if (instance.getWeight() > 0)
            {
                weightSum += instance.getWeight();
                sampleInstance.put(weightSum, instance);
            }
            else
            {
                // Keep the (worst) Instances with weight 0 in a separate list... So they don't overwrite the sampleInstance of the last instances with weight > 0.
                zeroWeightInstances.add(instance);
            }
        }

        DataVector selectedInstance;
        DataSet selectedInstances;

        // Replace the given fraction of instances
        replaceSelect = new LinkedList<DataVector>();
        for(DataVector instance: node.getData())
        {
            // Replace with instance chosen with weight-proportionate selection.
            if (this.random.nextDouble() < this.replacement)
                selectedInstance = selectWeightedInstance(sampleInstance, zeroWeightInstances);
            else 
            	selectedInstance = instance;

            // Make a copy with reset weight 
            selectedInstance = selectedInstance.copy();
            selectedInstance.setWeight(1.0);

            replaceSelect.add(selectedInstance);
        }
        
        //use the factory
        selectedInstances= factory.createDataSet(replaceSelect, this.node.getData().getGoal());
        
        // Remember replacements for this Node. Don't replace yet because this Node is a neighbor for other Nodes and these still need the weight of this epoch.
        node.setSelectedData(selectedInstances);
    }

    private DataVector selectWeightedInstance(TreeMap<Double, DataVector> instances, List<DataVector> zeroWeightInstances)
    {
        double   ran;
        Double   wkey;

        // Pick a random number in [0, sum of instance weights]
        ran  = this.random.nextDouble()*instances.lastKey();

        // Find the Instance for which the sum of weight of all previous instances is closest
        wkey = instances.higherKey(ran);

        // When beyond the highest weight, pick the last one when available. Or one of the zero-weight instances when available.
        if (wkey == null)
        {
            if (zeroWeightInstances.size() == 0) return instances.get(instances.lastKey());
            else                                 return zeroWeightInstances.get(this.random.nextInt(zeroWeightInstances.size()));
        }
        // Return instance selected by weight-proportionate selection.
        else return instances.get(wkey);
    }

    protected void testInstancesOnNeighbors() throws Exception
    {
        DataSet            nodeset;
        int                  i;
        double []weight;
        double   conf, minconf;

        nodeset   = node.getData();
        weight    = new double[nodeset.size()];

        // For all instance in this node's dataset
        i = 0;
        for(DataVector instance: nodeset)
        {
            // Test on neighbors. Remember most difficult test
            minconf = Double.MAX_VALUE;
            for(LearnerNode neighbor: neighbors)
            {
            	//cast to classifier
            	ClassifierNode classifierNode = (ClassifierNode)neighbor;
				conf = classifierNode.classify(instance);
                if (conf < minconf) minconf = conf;
            }
            // ********** DISABLE WEIGHTING *******
            //minconf = 1.0;
            // ************************************
            weight[i++] = minconf;
        }

        double wdiff, wmax, wmin;

        // Normalize weight with the highest weight is the most difficult instance
        wmax = Double.NEGATIVE_INFINITY;
        wmin = Double.POSITIVE_INFINITY;
        for (i=0; i<weight.length; i++) if (weight[i] > wmax) wmax = weight[i];
        for (i=0; i<weight.length; i++) if (weight[i] < wmin) wmin = weight[i];
        wdiff = (wmax - wmin);
        if (wdiff != 0)
        {
            for (i=0; i<weight.length; i++)
            {
                // Normalize between 0 and 1
                weight[i] = 1.0 - ((weight[i] - wmin) / wdiff);

             }
        }
        else
        {
            for (i=0; i<weight.length; i++) weight[i] = 1.0;
        }

        // Update the Instances of this Node with their new weights
        i = 0;
        for(DataVector ins: nodeset)
        {
            // TODO: Add momentum as suggested by Uday
            ins.setWeight(weight[i++]);
        }
    }

   
}
