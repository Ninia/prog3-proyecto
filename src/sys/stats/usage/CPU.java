package sys.stats.usage;

public class CPU {
    public static double getUsage() {
        return Double.parseDouble(
                ShellCommands.executeCommand("/usr/local/proyectopbd/cpu-usage.sh")
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