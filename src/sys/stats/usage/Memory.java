package sys.stats.usage;

public class Memory {

    public static long getUsage() {
        return Long.parseLong(
                ShellCommands.executeCommand("/usr/local/proyectopbd/mem-usage.sh").replace("\n", "")
        );
    }

    public static void main(String[] args) {
        while(true){
            System.out.println("Memory usage: " +
                    "" + Memory.getUsage());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
