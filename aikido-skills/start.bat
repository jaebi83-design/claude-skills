@echo off
cd /d "%~dp0"
echo Starting Aikido Skills server...
echo.
echo Server will be available at: http://localhost:8000
echo Press Ctrl+C to stop the server.
echo.
start http://localhost:8000
python -m uvicorn app.main:app --host 0.0.0.0 --port 8000
pause
