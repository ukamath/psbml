Run Parallel Implementation using WEKA and supervised classification

java -Dlog4j.configuration=log4j.properties -Xmx3072m com.ontolabs.ayllu.algorithm.bigdata.ParallelLearningApplication classifier local true ayllu.properties

Run Parallel Implementation using JSAT and unsupervised clustering

java -Dlog4j.configuration=log4j.properties -Xmx3072m com.ontolabs.ayllu.algorithm.bigdata.ParallelLearningApplication clustering.density local true ayllu-jsat-clustering.properties

Run Parallel Implementation using WEKA and supervised regression

java -Dlog4j.configuration=log4j.properties -Xmx3072m com.ontolabs.ayllu.algorithm.bigdata.ParallelLearningApplication regression local true ayllu-weka-regression.properties