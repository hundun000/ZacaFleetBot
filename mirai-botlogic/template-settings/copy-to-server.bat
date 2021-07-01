@echo off

set fromFolder="./bots"

set toFolder="../../mirai-server/bots"
if not exist %toFolder% mkdir %toFolder%
xcopy %fromFolder% %toFolder% /s /y

set fromFolder="./config"

set toFolder="../../mirai-server/config"
if not exist %toFolder% mkdir %toFolder%
xcopy %fromFolder% %toFolder% /s /y

set fromFolder="./data"

set toFolder="../../mirai-server/data"
if not exist %toFolder% mkdir %toFolder%
xcopy %fromFolder% %toFolder% /s /y

@echo copy done!
pause