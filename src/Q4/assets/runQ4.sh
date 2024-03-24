#!/bin/bash

JARNAME=Vaccination_Q4.jar
CLASSNAME=Vaccination_Q4
SRC=~/src/23219041_Assignment1/Q4
HDFS_HOME=~
INPUT_FILE=vaccination-rates-over-time-by-age-v2.txt
OUTPUT_DIR=Vaccination_Q4
OUTPUT_FILE=Vaccination_Q4.txt

export HADOOP_HOME=$HADOOP_HOME:.
export HADOOP_CLASSPATH=$SRC/*:.:$HADOOP_CLASSPATH
export CLASSPATH=$SRC/*:.:$CLASSPATH

hadoop fs -rm -r $HDFS_HOME/$OUTPUT_DIR
hadoop jar $SRC/$JARNAME $CLASSNAME $HDFS_HOME/$INPUT_FILE $HDFS_HOME/$OUTPUT_DIR

hadoop fs -cat $HDFS_HOME/$OUTPUT_DIR/* > $SRC/$OUTPUT_FILE