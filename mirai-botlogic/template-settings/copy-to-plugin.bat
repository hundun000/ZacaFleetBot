@echo off

set fromFolder="./bots"

set toFolder="../../mirai-plugin/bots"
if not exist %toFolder% mkdir %toFolder%
xcopy %fromFolder% %toFolder% /s /y

set fromFolder="./config"

set toFolder="../../mirai-plugin/config"
if not exist %toFolder% mkdir %toFolder%
xcopy %fromFolder% %toFolder% /s /y

set fromFolder="./data"

set toFolder="../../mirai-plugin/data"
if not exist %toFolder% mkdir %toFolder%
xcopy %fromFolder% %toFolder% /s /y

@echo copy done!
pause