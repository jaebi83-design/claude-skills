@echo off
title Video Decomposer
echo ========================================
echo         Video Decomposer
echo ========================================
echo.

cd /d "%~dp0video-decomposer"

echo Starting server...
echo.
echo When you see "Uvicorn running", open your browser to:
echo.
echo     http://localhost:8080
echo.
echo To stop the server, close this window or press Ctrl+C.
echo ========================================
echo.

"C:\Users\Jim\AppData\Local\Programs\Python\Python312\python.exe" -m uvicorn app.main:app --host 0.0.0.0 --port 8080

pause
