package com.example.tickets.stages.stage;

import com.google.common.base.Stopwatch;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GarbageCollectionStage implements Stage {
    private static final Logger log = LoggerFactory.getLogger(GarbageCollectionStage.class);

    @Override
    public StageResult call() {
        Stopwatch timer = Stopwatch.createStarted();
        log.info("GarbageCollectionStage started");
        long freeMemoryBeforeGC = Runtime.getRuntime().freeMemory();
        System.gc();
        long freeMemoryAfterGC = Runtime.getRuntime().freeMemory();
        long freeMemoryDiff = freeMemoryAfterGC - freeMemoryBeforeGC;
        String freedMemory = FileUtils.byteCountToDisplaySize(freeMemoryDiff);
        log.info("Freed memory by GC: {}", freedMemory);
        log.info("GarbageCollectionStage finished in {}", timer.stop());
        return new StageResult("GarbageCollectionStage", 0, 0, 0);
    }
}
