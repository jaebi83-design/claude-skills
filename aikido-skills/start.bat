@echo off
cd /d "%~dp0"
echo Starting Aikido Skills server...
echo Working directory: %cd%
echo.
echo Server will be available at: http://localhost:8033
echo Press Ctrl+C to stop the server.
echo.
timeout /t 2 /nobreak >nul
start "" "http://localhost:8033"
python -m uvicorn app.main:app --host 0.0.0.0 --port 8033
pause
