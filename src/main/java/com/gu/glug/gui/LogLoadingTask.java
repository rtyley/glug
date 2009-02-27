package com.gu.glug.gui;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingWorker;

import com.gu.glug.ThreadedSystem;

public class LogLoadingTask extends SwingWorker<ThreadedSystem, Void> {

	File logFile;
	
    @Override
    public ThreadedSystem doInBackground() {
    	ThreadedSystem threadedSystem = new ThreadedSystem();
    	LogLoader logLoader = new LogLoader(logFile,threadedSystem);
        try {
			while (isCancelled() && logLoader.loadLines(100)) {
//                number = nextPrimeNumber();
//                publish(number);
//                setProgress(100 * numbers.size() / numbersToFind);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
        return threadedSystem;
    }

    
}
