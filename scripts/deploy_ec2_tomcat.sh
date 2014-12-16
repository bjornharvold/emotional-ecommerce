#!/bin/bash -l

# This script will restart tomcat. Deploying over RMI causes memory leaks that eventually lead to
# OutOfMemory exceptions. This should fix that problem.
TOMCAT_HOME=/vol/applications/tomcat6
#CATALINA_PID=$TOMCAT_HOME/bin/tomcat.pid

FILE1=/root/dl/lela.war
FILE2=/root/dl/ingest.war

if [[ ( -f $FILE1 && -f $FILE2 ) ]]; then
#if [ -f $FILE1 ]; then
    echo "New lela.war spotted. Redeploying..."
    echo "Shutting down Tomcat..."

# shutdown LELA
#service tomcat stop
$TOMCAT_HOME/bin/shutdown-lela.sh

# shutdown INGEST
#service tomcat stop
$TOMCAT_HOME/bin/shutdown-ingest.sh

echo "Waiting for Tomcat processes to finish"

# wait until process is complete
sleep 10

# Kill the process if its still running
#echo "Killing Tomcat pid: "
#echo `cat $CATALINA_PID`
#kill -9 `cat $CATALINA_PID`
#rm -rf $CATALINA_PID

# Kill the process if its still running
#echo "Killing Tomcat pid: "
#echo `cat $CATALINA_PID`
#kill -9 `cat $CATALINA_PID`
#rm -rf $CATALINA_PID

# going in to Tomcat directory to clean up some stuff
cd $TOMCAT_HOME/base-lela

echo "Removing work webapps/lela* directories"
rm -rfv work logs/* webapps/ROOT* > /dev/null

echo "Copying new Lela WARs in place"
mv $FILE1 webapps/ROOT.war

# going in to Tomcat directory to clean up some stuff
cd $TOMCAT_HOME/base-ingest

echo "Removing work webapps/lela* directories"
rm -rfv work logs/* webapps/ROOT* > /dev/null

echo "Copying new Ingest WARs in place"
mv $FILE2 webapps/ROOT.war

# changing permissions
chown root.root webapps/ROOT.war

# Lela Startup
echo "Starting Lela Tomcat again"
$TOMCAT_HOME/bin/startup-lela.sh

# Ingest Startup
echo "Starting Ingest Tomcat again"
$TOMCAT_HOME/bin/startup-ingest.sh

fi

