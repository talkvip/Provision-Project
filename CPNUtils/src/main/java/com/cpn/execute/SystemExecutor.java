package com.cpn.execute;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class SystemExecutor {

	private LogDevice fOuputLogDevice = null;
	private LogDevice fErrorLogDevice = null;
	private String fWorkingDirectory = null;
	private List<EnvironmentVar> fEnvironmentVarList = null;

	private StringBuffer fCmdOutput = null;
	private StringBuffer fCmdError = null;
	private AsyncStreamReader fCmdOutputThread = null;
	private AsyncStreamReader fCmdErrorThread = null;

	public SystemExecutor setOutputLogDevice(LogDevice logDevice) {
		fOuputLogDevice = logDevice;
		return this;
	}

	public SystemExecutor setErrorLogDevice(LogDevice logDevice) {
		fErrorLogDevice = logDevice;
		return this;
	}

	public SystemExecutor setWorkingDirectory(String workingDirectory) {
		fWorkingDirectory = workingDirectory;
		return this;
	}

	public SystemExecutor setEnvironmentVar(String name, String value) {
		if (fEnvironmentVarList == null)
			fEnvironmentVarList = new ArrayList<>();

		fEnvironmentVarList.add(new EnvironmentVar(name, value));
		return this;
	}

	public String getCommandOutput() {
		return fCmdOutput.toString();
	}

	public String getCommandError() {
		return fCmdError.toString();
	}

	public int runCommand(String commandLine) throws SystemExecutorException, IOException {
		/* run command */
		Process process = runCommandHelper(commandLine);

		/* start output and error read threads */
		startOutputAndErrorReadThreads(process.getInputStream(), process.getErrorStream());

		/* wait for command execution to terminate */
		int exitStatus = -1;
		try {
			exitStatus = process.waitFor();

		} catch (Throwable ex) {
			throw new SystemExecutorException(ex.getMessage(), ex);

		} finally {
			/* notify output and error read threads to stop reading */
			notifyOutputAndErrorReadThreadsToStopReading();
		}

		return exitStatus;
	}

	private Process runCommandHelper(String commandLine) throws IOException {
		Process process = null;
		if (fWorkingDirectory == null)
			process = Runtime.getRuntime().exec(commandLine, getEnvTokens());
		else
			process = Runtime.getRuntime().exec(commandLine, getEnvTokens(), new File(fWorkingDirectory));

		return process;
	}

	private SystemExecutor startOutputAndErrorReadThreads(InputStream processOut, InputStream processErr) {
		fCmdOutput = new StringBuffer();
		fCmdOutputThread = new AsyncStreamReader(processOut, fCmdOutput, fOuputLogDevice, "OUTPUT");
		fCmdOutputThread.start();

		fCmdError = new StringBuffer();
		fCmdErrorThread = new AsyncStreamReader(processErr, fCmdError, fErrorLogDevice, "ERROR");
		fCmdErrorThread.start();
		return this;
	}

	private SystemExecutor notifyOutputAndErrorReadThreadsToStopReading() {
		fCmdOutputThread.stopReading();
		fCmdErrorThread.stopReading();
		return this;
	}

	private String[] getEnvTokens() {
		if (fEnvironmentVarList == null)
			return null;

		String[] envTokenArray = new String[fEnvironmentVarList.size()];
		int nEnvVarIndex = 0;
		for (EnvironmentVar envVar : fEnvironmentVarList) {
			String envVarToken = envVar.fName + "=" + envVar.fValue;
			envTokenArray[nEnvVarIndex++] = envVarToken;
		}

		return envTokenArray;
	}
	static class EnvironmentVar {
		public String fName = null;
		public String fValue = null;

		public EnvironmentVar(String name, String value) {
			fName = name;
			fValue = value;
		}
	}
}

class AsyncStreamReader extends Thread {
	private StringBuffer fBuffer = null;
	private InputStream fInputStream = null;
	private boolean fStop = false;
	private LogDevice fLogDevice = null;

	private String fNewLine = null;

	public AsyncStreamReader(InputStream inputStream, StringBuffer buffer, LogDevice logDevice, String threadId) {
		fInputStream = inputStream;
		fBuffer = buffer;
		fLogDevice = logDevice;

		fNewLine = System.getProperty("line.separator");
	}

	public String getBuffer() {
		return fBuffer.toString();
	}

	public void run() {
		try {
			readCommandOutput();
		} catch (Exception ex) {
			// ex.printStackTrace(); //DEBUG
		}
	}

	private void readCommandOutput() throws IOException {
		BufferedReader bufOut = new BufferedReader(new InputStreamReader(fInputStream));
		String line = null;
		while ((fStop == false) && ((line = bufOut.readLine()) != null)) {
			fBuffer.append(line + fNewLine);
			printToDisplayDevice(line);
		}
		bufOut.close();
		// printToConsole("END OF: " + fThreadId); //DEBUG
	}

	public void stopReading() {
		fStop = true;
	}

	private void printToDisplayDevice(String line) {
		if (fLogDevice != null)
			fLogDevice.log(line);
		else {
			// printToConsole(line);//DEBUG
		}
	}



}
