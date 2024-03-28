#!/bin/bash

if [ "$#" -lt 2 ]; then
    echo "Usage: $0 <NUM_REP> <RUNNING_INTERVAL(secods)> [WAITING_TIME(seconds)]"
    exit 1
fi

echo "version: '3'" > template_temp.yaml
echo "services:" >> template_temp.yaml
echo " smart-edge:" >> template_temp.yaml
echo "  image: cefriel/smartedge:image-tag" >> template_temp.yaml
echo "  container_name: container-name" >> template_temp.yaml
echo "  volumes:" >> template_temp.yaml
echo "   - ./appFolder/container-name/test-number1:/home/appFolder" >> template_temp.yaml
echo "   - ./appFolder/container-name/test-number2/outbox:/home/out" >> template_temp.yaml
echo "  environment:" >> template_temp.yaml
echo "   - TZ=Europe/Rome" >> template_temp.yaml

NUM_REP=$1
RUNNING_INTERVAL=$2
WAITING_INTERVAL=${3:-120}

date=$(date +"%Y-%m-%d_%H-%M")

exec_test()
{
	IMAGE_TAG=$1
	CONTAINER_NAME=$2
	LOOPS=$3
	INTERVAL=$4

	waitInterval=$WAITING_INTERVAL
	
	echo "Start running tests for image \"$IMAGE_TAG\"......"
	for ((i=1;i<=$LOOPS;i++)); do
		cp  template_temp.yaml docker-compose.yaml
		
		sed -i "s|cefriel/smartedge:image-tag|cefriel/smartedge:$IMAGE_TAG|g" docker-compose.yaml
		sed -i "s|container-name|$CONTAINER_NAME|g" docker-compose.yaml
		sed -i "s|./appFolder/container-name/test-number1:/home/appFolder|./appFolder/$CONTAINER_NAME/test-number1:/home/appFolder|g" docker-compose.yaml
		sed -i "s|test-number1|test_$i|g" docker-compose.yaml
		sed -i "s|./appFolder/container-name/test-number2/outbox:/home/out|./appFolder/$CONTAINER_NAME/test-number2/outbox:/home/out|g" docker-compose.yaml
		sed -i "s|test-number2|test_$i|g" docker-compose.yaml
		
		echo "Running image \"$IMAGE_TAG\" for \"$INTERVAL\" seconds iteration $i"
		docker compose up --force-recreate -d
		sleep $INTERVAL
		docker compose down
		echo "Stopped execution of iteration \"$i\" for image \"$IMAGE_TAG\", copy result files and waithing $waitInterval secods before starting the next test, if there are more left"
		rm docker-compose.yaml
		
		mkdir -p ./report_$date/$CONTAINER_NAME/test_$i
		mkdir -p ./report_$date/$CONTAINER_NAME/test_$i/outbox
		cp -r ./appFolder/$CONTAINER_NAME/test_$i/data/records ./report_$date/$CONTAINER_NAME/test_$i
		cp -r ./appFolder/$CONTAINER_NAME/test_$i/outbox ./report_$date/$CONTAINER_NAME/test_$i
                
		last_stats_log=$(grep "CM=" "./appFolder/$CONTAINER_NAME/test_$i/log/ChimeraCamelJava.log" | tail -n 1)
		if [ -n "$last_stats_log" ]; then
			echo "$last_stats_log" > "./report_$date/$CONTAINER_NAME/test_$i/outbox/lastStatsLog.txt"
		fi
		
		sleep $waitInterval
	done
}

exec_test temurin-17 temurin $NUM_REP $RUNNING_INTERVAL
echo "Deleting appFolder directory..."
rm -r ./appFolder
exec_test graalvm-17 graalvm $NUM_REP $RUNNING_INTERVAL
echo "Deleting appFolder directory..."
rm -r ./appFolder
exec_test native native $NUM_REP $RUNNING_INTERVAL
echo "Deleting appFolder directory..."
rm -r ./appFolder
echo "All the tests have been exetuted...Deleting temp file and terminating the script!"
rm template_temp.yaml
