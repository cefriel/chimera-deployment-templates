#!/bin/sh

# Wait a little bit to ensure volumes are mounted correctly
sleep 3

#Run native app
/home/minimal-chimera-spring &

mkdir -p /home/out/

# Log header to CSV file
echo "Timestamp,MemoryUsage(MB),CPUUsage" > /home/out/stats.txt

# Log stats every second indefinitely
while true; do
    timestamp=$(date +"%Y-%m-%d %H:%M:%S")

    # Get memory usage in kilobytes
    memory_usage_kb=$(ps -o rss -p $(pgrep -d',' -f "/home/minimal-chimera-spring") | tail -n 1)

    # Convert kilobytes to megabytes (direct arithmetic expression)
    memory_usage_mb=$((memory_usage_kb / 1024))

    cpu_usage=$(ps -o pcpu -p $(pgrep -d',' -f "/home/minimal-chimera-spring") | tail -n 1)

    # Append stats to CSV file
    echo "$timestamp,$memory_usage_mb,$cpu_usage" >> /home/out/stats.txt

    sleep 5
done
