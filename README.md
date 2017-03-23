What is PSBML?
--------------

**PSBML = Parallel Spatial Boosting Machine Learning** 

PSBML was started in 2012 by Uday Kamath and Johan Kaers as a research paper in 2012 written in collaboration with Ph.D Advisor Kenneth De Jong.
*"A spatial EA framework for parallelizing machine learning methods"
U Kamath, J Kaers, A Shehu, KA De Jong
International Conference on Parallel Problem Solving from Nature, 206-215*

Uday Kamath then extended the algorithm from theoretical, implementation and empirical analysis in collaboration with Machine Learning expert Carlotta Domeniconi and Kenneth De Jong and SMILE group collaborator Y. Ren.
Some of the papers are 

 1. U. Kamath, C. Domeniconi, and K. De Jong, An analysis of a spatial EA parallel Boosting algorithm, GECCO 2013.
 2. U. Kamath, C. Domeniconi, and K. De Jong, Theoretical and Empirical Analysis of a Spatial EA Parallel Boosting Algorithm, Evolutionary Computation Journal, 2017.
 3. Y. Ren, U. Kamath, C. Domeniconi, and G. Zhang, Boosted Mean Shift Clustering, ECML PKDD 2014
 4. Y. Ren, U. Kamath, and C. Domeniconi, Parallel Boosted Clustering (under submission)


What is the Idea behind PSBML?
------------------------------

In very simple terms: it is **Divide, Collaborate and Conquer**!

 - Use existing unmodified machine learning algorithms from Open Sources or Closed Sources
 - Converts any Machine Learning library to Parallel library
 - Parallelize Machine learning 
 - Scale up (concurrency) and scale out (remoting) to handle dataset size 

What does PSBML support?
-----------------------
Supervised Learning (Classification and Regression), Unsupervised Learning (Clustering) as framework across any open sources. Implementations for well-known open sources such as WEKA, JSAT and RapidMiner are implemented.


How to Run PSBML ?
------------------

**Run Parallel Implementation using WEKA and supervised classification**

java -Dlog4j.configuration=log4j.properties -Xmx3072m com.ontolabs.ayllu.algorithm.bigdata.ParallelLearningApplication classifier local true ayllu.properties

**Run Parallel Implementation using JSAT and unsupervised clustering**

java -Dlog4j.configuration=log4j.properties -Xmx3072m com.ontolabs.ayllu.algorithm.bigdata.ParallelLearningApplication clustering.density local true ayllu-jsat-clustering.properties

**Run Parallel Implementation using WEKA and supervised regression**

java -Dlog4j.configuration=log4j.properties -Xmx3072m com.ontolabs.ayllu.algorithm.bigdata.ParallelLearningApplication regression local true ayllu-weka-regression.properties