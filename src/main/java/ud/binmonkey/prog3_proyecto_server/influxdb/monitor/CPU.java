package ud.binmonkey.prog3_proyecto_server.influxdb.monitor;

import ud.binmonkey.prog3_proyecto_server.common.posix.ShellCommand;

public class CPU {

    public static double getTemperature() {
        return Double.parseDouble(
                ShellCommand.executeCommand("/usr/local/p3p/cpu-temperature.sh")
        );
    }

    public static double getUsage() {
        return Double.parseDouble(
                ShellCommand.executeCommand("/usr/local/p3p/cpu-usage.sh")
        );
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {

        while (true) {
            System.out.println("CPU usage: " + CPU.getUsage());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
