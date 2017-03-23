Ayllu (pronounced EYE-LL-YOU) is the Quechua word fora social unit where every member engages in sharedcollective laborIdea behind it is Divide, Collaborate and Conquer!* Use existing unmodified learning algorithms from Open Sources or Closed Sources
* Converts any library to parallel library* Parallelize learning * Scale up (concurrency) and scale out (remoting) to handle dataset size 

How to Run?

Run Parallel Implementation using WEKA and supervised classification

java -Dlog4j.configuration=log4j.properties -Xmx3072m com.ontolabs.ayllu.algorithm.bigdata.ParallelLearningApplication classifier local true ayllu.properties

Run Parallel Implementation using JSAT and unsupervised clustering

java -Dlog4j.configuration=log4j.properties -Xmx3072m com.ontolabs.ayllu.algorithm.bigdata.ParallelLearningApplication clustering.density local true ayllu-jsat-clustering.properties

Run Parallel Implementation using WEKA and supervised regression

java -Dlog4j.configuration=log4j.properties -Xmx3072m com.ontolabs.ayllu.algorithm.bigdata.ParallelLearningApplication regression local true ayllu-weka-regression.properties