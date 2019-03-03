@ECHO OFF

REM this script pings all IP addresses from 1 to 254

REM FOR /L %i IN (1,1,254) DO ping -n 1 169.254.1.%i

ping 169.254.1.%i