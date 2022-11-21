#!/bin/bash
echo "App name?"
read appName
appPath=$(zenity  --file-selection --title="Choose an executable"  -)
iconPath=$(zenity  --file-selection --title="Choose an icon"  -)
appType=$(zenity --list --title="App Category?" --column=""  Development \ Accessories \ Internet \ Office)
entry="[Desktop Entry]\nEncoding=UTF-8\nVersion=1.0\nType=Application\nTerminal=false\nExec=$appPath\nName=$appName\nIcon=$iconPath\nCategories=$appType"
echo -e $entry > /home/$USER/.local/share/applications/$appName.desktop

#Easy way to make .desktop files, should application install not make one automatically
