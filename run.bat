@echo off
setlocal

if not exist out mkdir out

dir /s /b src\main\java\*.java > sources.txt
javac -d out @sources.txt
if errorlevel 1 (
  echo Compilation failed.
  exit /b 1
)

set "INPUT=%~1"
set "THRESHOLD=%~2"
set "WINDOW=%~3"

:ask_input
if "%INPUT%"=="" (
  set /p INPUT=Chemin du fichier ou dossier de logs a analyser - ex: samples\sample.log: 
)
if not exist "%INPUT%" (
  echo Fichier ou dossier introuvable: "%INPUT%"
  set "INPUT="
  goto ask_input
)
if "%THRESHOLD%"=="" (
  set /p THRESHOLD=Seuil brute-force - defaut 5, ex: 5: 
)
if "%WINDOW%"=="" (
  set /p WINDOW=Fenetre temporelle en minutes - defaut 10, ex: 10: 
)

if "%INPUT%"=="" (
  echo Aucun fichier fourni.
  exit /b 1
)
if "%THRESHOLD%"=="" set "THRESHOLD=5"
if "%WINDOW%"=="" set "WINDOW=10"

java -cp out com.mini_siem.Main -i "%INPUT%" -o reports -t %THRESHOLD% -w %WINDOW%
endlocal
