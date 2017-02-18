package sys.config;

public class SystemInfo {

    private static final String SYSNAME = "ninia server";
    private static final long MAX_RAM_MB = 4000;
    private static final int CORE_NUM = 2;

    public static String getSYSNAME() {
        return SYSNAME;
    }

    public static long getMaxRamMb() {
        return MAX_RAM_MB;
    }

    public static int getCoreNum() {
        return CORE_NUM;
    }
}
