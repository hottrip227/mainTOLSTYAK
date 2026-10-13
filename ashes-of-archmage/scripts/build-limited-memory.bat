@echo off
setlocal
cd /d "%~dp0.."
set "_JAVA_OPTIONS=-Xms64m -Xmx3g -XX:ActiveProcessorCount=1"
call gradlew.bat --no-daemon --no-configuration-cache --max-workers=1 test build
exit /b %errorlevel%
