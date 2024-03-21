#!/bin/bash

JARNAME=FriendsOfFriends.jar
CLASSNAME=FriendsOfFriends
SRC=~/src
HDFS_HOME=~

export HADOOP_HOME=$HADOOP_HOME:.
export HADOOP_CLASSPATH=$SRC/*:.:$HADOOP_CLASSPATH
export CLASSPATH=$SRC/*:.:$CLASSPATH

hadoop fs -rm -r $HDFS_HOME/output_txt
hadoop jar $JARNAME $CLASSNAME $HDFS_HOME/Friends.txt  $HDFS_HOME/output_txt
