@echo off
setlocal enabledelayedexpansion

REM ====== 사용자 설정 ======
set "DB_HOST=127.0.0.1"
set "DB_PORT=9090"
set "DB_NAME=myshop"
set "DB_USER=myshop"
set "DB_PASS=1234"
set "MYSQLDUMP=mysqldump"

REM ====== 백업 폴더 ======
set "OUT_DIR=%~dp0backups"
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

REM ====== 타임스탬프 생성 (yyyyMMddHHmmss) ======
for /f "tokens=2 delims==" %%a in ('wmic os get localdatetime /value') do set ldt=%%a
set TS=%ldt:~0,14%

REM ====== 출력 파일 경로 ======
set "OUT_FILE=%OUT_DIR%\myshop-dump-%TS%.sql"

echo.
echo [INFO] Dump start -> "%OUT_FILE%"
echo.

REM ====== 덤프 실행 ======
"%MYSQLDUMP%" -h %DB_HOST% -P %DB_PORT% -u %DB_USER% -p%DB_PASS% ^
  --databases %DB_NAME% ^
  --add-drop-database --add-drop-table ^
  --routines --events --triggers ^
  --single-transaction ^
  > "%OUT_FILE%"

if errorlevel 1 (
  echo [ERROR] Dump failed. ErrorLevel=%errorlevel%
  exit /b %errorlevel%
) else (
  echo [OK] Dump completed: "%OUT_FILE%"
)

endlocal
