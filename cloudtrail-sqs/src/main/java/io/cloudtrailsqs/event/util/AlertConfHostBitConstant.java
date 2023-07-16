package io.cloudtrailsqs.event.util;

public final class AlertConfHostBitConstant {

    public static final int ACI_CREATE = 0x00000001;
    public static final int ACI_UPDATE = 0x00000002;
    public static final int ACI_DELETE = 0x00000004;

    public static final int ACI_RESOURCE_ADD = 0x00000008;
    public static final int ACI_RESOURCE_DELETE = 0x00000010;
    public static final int ACI_ACCESS_ADD = 0x00000020;
    public static final int ACI_ACCESS_DELETE = 0x00000040;
    public static final int ACI_DANGER_HIGH = 0x00000080;
    public static final int ACI_DANGER_MEDIUM = 0x00000100;
    public static final int ACI_DANGER_LOW = 0x00000200;

    public static final Integer[] ACI_ARR = {
            ACI_CREATE, ACI_UPDATE, ACI_DELETE,
            ACI_RESOURCE_ADD, ACI_RESOURCE_DELETE, ACI_ACCESS_ADD, ACI_ACCESS_DELETE,
            ACI_DANGER_HIGH, ACI_DANGER_MEDIUM, ACI_DANGER_LOW
    };

}
