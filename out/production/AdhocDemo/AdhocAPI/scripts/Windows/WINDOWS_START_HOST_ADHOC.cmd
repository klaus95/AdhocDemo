@ECHO OFF
REM arg1 is the NETWORK_NAME
REM arg2 is the PASSWORD

REM net start SharedAccess REM find new arguments, to use on Wi-Fi
REM First configure the new network
netsh wlan set hostednetwork mode=allow ssid=%1 key=%2
REM net start SharedAccess
netsh wlan start hostednetwork

REM allow others to get internet via hosted network

REM >nul stops the input to stdout

REM Windows 10 GUI steps
REM Network and Internet
REM Network Sharing Center 
REM Change adapter settings
REM Right-click current connection
REM Properties
REM Sharing
REM Check-Allow other network users to connect...
REM Change drop-down to Local Area Connection
REM Click hot-spot name


REM net share config routing tables
REM maquerading mode
REM turns my computer into router