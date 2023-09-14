@echo off
SET CWD=%~dp0
java -Dgroovy.grape.report.downloads=true -cp "%CWD%\\lib\\*;%WEBTAU_CP%;:%CWD%\\libs\\*" org.testingisdocumenting.webtau.app.WebTauCliApp %*
