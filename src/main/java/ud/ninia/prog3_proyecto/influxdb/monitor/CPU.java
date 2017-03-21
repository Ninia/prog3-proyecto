package ud.ninia.prog3_proyecto.influxdb.monitor;

import ud.ninia.prog3_proyecto.utils.posix.ShellCommand;

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
