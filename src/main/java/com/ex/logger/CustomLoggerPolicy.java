package com.ex.logger;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;

import java.io.File;
import java.time.Instant;

@NoAutoStart
public class CustomLoggerPolicy<E> extends DefaultTimeBasedFileNamingAndTriggeringPolicy<E> {

    // 2분 간격으로 재처리 로그 출력
    private final int LOG_CREATION_CYCLE_MINUTE = 2;
    private final int ONE_MINUTE_MILLISECONDS = 60000;
    private final int LOG_CREATION_CYCLE_MILLISECONDS = LOG_CREATION_CYCLE_MINUTE * ONE_MINUTE_MILLISECONDS;

    @Override
    public boolean isTriggeringEvent(File activeFile, E event) {
        long currentTime = this.getCurrentTime();
        long localNextCheck = computeNextCheck(atomicNextCheck.get());
        if (currentTime >= localNextCheck) {
            long nextCheck = this.computeNextCheck(currentTime);
            atomicNextCheck.set(nextCheck);
            setDateInCurrentPeriod(currentTime);

            Instant instantOfElapsedPeriod = dateInCurrentPeriod;
            addInfo("Elapsed period: " + instantOfElapsedPeriod.toString());
            this.elapsedPeriodsFileName = this.getCurrentPeriodsFileNameWithoutCompressionSuffix();
            return true;
        }

        return false;
    }

    @Override
    protected long computeNextCheck(long timestamp) {
        long milliseconds = this.dateInCurrentPeriod.toEpochMilli();
        long remainder = milliseconds % LOG_CREATION_CYCLE_MILLISECONDS;
        long difference = LOG_CREATION_CYCLE_MILLISECONDS - remainder;
        return milliseconds + difference;
    }
}