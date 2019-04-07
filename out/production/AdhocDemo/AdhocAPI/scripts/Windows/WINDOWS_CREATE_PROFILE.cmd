@ECHO OFF
REM arg1 = NAME of XML file
REM arg2 = Interface name

netsh wlan add profile filename=%1 interface=%2 user=all
REM consider using current for user instead of all