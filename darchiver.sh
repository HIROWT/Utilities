#!/bin/bash
mkdir -p /home/$USER/Downloads/Archive/;
declare -a wFiles
declare -a mFiles
#array of "wait files" (files that are currently open)
IFS=$'\n' 
mFolder=$(zenity  --file-selection --title="Choose a directory"  --directory)
echo $mFolder
fSel=$(zenity --list --title="Choose a file scheme" --column=""  " Month-Year" \ Year \ "Month" \ "Year-Month"
echo $fSel)
echo $fSel
my=" Month-Year"
y=" Year"
m=" Month"
ym=" Year-Month"


for line in $(lsof -X -n -e /run/user/1000/doc -e /sys/kernel/debug/tracing | grep "$mFolder" |  cut -d'/' -f 2- | sort | uniq )
do	
	wFiles+=("/""$line")
done

for file in "$mFolder/"*
do
	if [[ "$file" == $mFolder"/Archive" ]];
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

notify-send "File Archiver" " Moving "${#mFiles[@]}" files..."

echo "Moving "${#mFiles[@]}" files..."


if [[ "$fSel" == "$my" ]];
then
	for file in "${mFiles[@]}"
	do
		DATE=$(date -r "$file" )
		DATE="${DATE:7:3}${DATE:13:2}"
		mkdir -p $mFolder/Archive/"$DATE"; sudo mv "$file" $mFolder/Archive/"$DATE"
	done
elif [[ "$fSel" == "$y" ]];
then
	for file in "${mFiles[@]}"
	do
		DATE=$(date -r "$file" )
		DATE="${DATE:13:2}"
		mkdir -p $mFolder/Archive/"$DATE"; sudo mv "$file" $mFolder/Archive/"$DATE"
	done
elif [[ "$fSel" == "m" ]];
then
	for file in "${mFiles[@]}"
	do
		DATE=$(date -r "$file" )
		DATE="${DATE:7:3}"
		mkdir -p $mFolder/Archive/"$DATE"; sudo mv "$file" $mFolder/Archive/"$DATE"
	done
elif [[ "$fSel" == "ym" ]];
then
	for file in "${mFiles[@]}"
	do
		DATE=$(date -r "$file" )
		DATE="${DATE:13:2}${DATE:7:3}"
		mkdir -p $mFolder/Archive/"$DATE"; sudo mv "$file" $mFolder/Archive/"$DATE"
	done
fi


if((${#wFiles[@]} > 0))
then
notify-send "File Archiver" "Some files are open!"
echo "The following files are being accessed"
printf "%s\n" "${wFiles[@]}"
fi



