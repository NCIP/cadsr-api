#!/bin/bash

echo "Executing Auto Run for Freestyle Search Engine"
echo "\$Header: /share/content/gforge/freestylesearch/freestylesearch/scripts/autorun.sh,v 1.3 2008-04-22 20:28:39 hebell Exp $"
echo "\$Name: not supported by cvs2svn $"

DATE=`date +%Y%m%d`
JAVA_HOME=/usr/jdk1.6.0_71
BASE_DIR=/local/content/freestyle/bin

export JAVA_HOME BASE_DIR

ORACLE_HOME=/app/oracle/product/dbhome/9.2.0
PATH=$ORACLE_HOME/bin:$PATH
LD_LIBRARY_PATH=$ORACLE_HOME/lib:$LD_LIBRARY_PATH
TNS_ADMIN=$ORACLE_HOME/network/admin
JAVA_PARMS='-Xms512m -Xmx512m -XX:PermSize=64m'

export JAVA_PARMS ORACLE_HOME TNS_ADMIN PATH LD_LIBRARY_PATH

echo "Executing job as `id`"
echo "Executing on `date`"

$JAVA_HOME/bin/java -client $JAVA_PARMS -classpath $BASE_DIR/cadsrapi41-beans.jar:$BASE_DIR/sdk-system-client-4.5.jar:$BASE_DIR/hibernate-3.2.0.ga-modified.jar:$BASE_DIR/org.springframework.core-3.0.5.RELEASE.jar:$BASE_DIR/log4j-1.2.16.jar:$BASE_DIR/ojdbc14.jar:$BASE_DIR/freestylesearch.jar gov.nih.nci.cadsr.freestylesearch.util.Seed $BASE_DIR/log4j.xml $BASE_DIR/seed.xml

