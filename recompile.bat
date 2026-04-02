@echo off
powershell -ExecutionPolicy Bypass -File "%~dp0mcp\scripts\full-recompile.ps1" %*
