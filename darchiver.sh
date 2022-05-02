#!/bin/bash
mkdir -p /home/$USER/Downloads/Archive/;
declare -a wFiles
declare -a mFiles
#array of "wait files" (files that are currently open)
IFS=$'\n' 
for line in $(lsof -X -n -e /run/user/1000/doc -e /sys/kernel/debug/tracing | grep "/home/"$USER"/Downloads/" |  cut -d'/' -f 2- | sort | uniq )
do	
	wFiles+=("/""$line")
done

for file in /home/$USER/Downloads/*
do
	if [[ "$file" == "/home/"$USER"/Downloads/Archive" ]];
	then
		continue
	fi
	arg=0
	for wFile in ${wFiles[@]}
	do
		if(($(echo "$file" | grep "$wFile" | wc -l) == 1 ))
		then
		arg=1
		fi
	done
	
	if (($arg==0))
	then
		mFiles+=("$file")
	fi
done

echo "Moving "${#mFiles[@]}" files..."

for file in "${mFiles[@]}"
do
	DATE=$(date +"%Y%B" -r "$file" )
	DATE="${DATE:2:2}${DATE:4:3}" 
	mkdir -p /home/"$USER"/Downloads/Archive/"$DATE"; sudo mv "$file" /home/"$USER"/Downloads/Archive/"$DATE"
done

if((${#wFiles[@]} > 0))
then
echo "The following files are being accessed"
printf "%s\n" "${wFiles[@]}"
fi



