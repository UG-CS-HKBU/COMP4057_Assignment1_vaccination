#!/bin/bash

JARNAME=Vaccination_Q1.jar
CLASSNAME=Vaccination_Q1
SRC=~/src/Assignment1/Q1
HDFS_HOME=~
INPUT_FILE=vaccination-rates-over-time-by-age.txt
OUTPUT_DIR=Vaccination_Q1
OUTPUT_FILE=vaccination-rates-over-time-by-age-v2.txt

export HADOOP_HOME=$HADOOP_HOME:.
export HADOOP_CLASSPATH=$SRC/*:.:$HADOOP_CLASSPATH
export CLASSPATH=$SRC/*:.:$CLASSPATH

hadoop fs -copyFromLocal $SRC/$INPUT_FILE $HDFS_HOME
hadoop fs -rm -r $HDFS_HOME/$OUTPUT_DIR
hadoop jar $SRC/$JARNAME $CLASSNAME $HDFS_HOME/$INPUT_FILE $HDFS_HOME/$OUTPUT_DIR

hadoop fs -cat $HDFS_HOME/$OUTPUT_DIR/* > $SRC/$OUTPUT_FILE