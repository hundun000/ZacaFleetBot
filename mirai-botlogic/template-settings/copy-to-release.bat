@echo off

set fromFolder="./config"

set toFolder="../../release/config"
if not exist %toFolder% mkdir %toFolder%
xcopy %fromFolder% %toFolder% /s /y /EXCLUDE:script-excluded-file-list.txt

set fromFolder="./data"

set toFolder="../../release/data"
if not exist %toFolder% mkdir %toFolder%
xcopy %fromFolder% %toFolder% /s /y /EXCLUDE:script-excluded-file-list.txt

@echo copy done!
pause