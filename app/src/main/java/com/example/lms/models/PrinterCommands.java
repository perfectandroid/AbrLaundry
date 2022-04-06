package com.example.lms.models;

/**
 * Created by admin on 2/5/2015.
 */
public class PrinterCommands {

    public static byte[] INIT = {27, 64};
    public static byte[] RESET = {24};
    public static final byte[] AUTO_POWER_OFF = {27,77,55,54,53,52,48,13};
    public static byte[] SELECT_FONT_A = {27, 33, 0};
    public static byte[] SELECT_FONT_12 = {27, 75, 49, 13};
    public static byte[] SELECT_FONT_25 = {27, 75, 49, 49, 13};
    public static byte[] FONT_BOLD_ON = {27, 85, 49};
    public static byte[] FONT_BOLD_OFF = {27, 85, 48};
    public static byte[] FONT_UNDERLINE_ON = {27, 85, 85};
    public static byte[] FONT_UNDERLINE_OFF = {27, 85, 117};
    public static byte[] FONT_HI_ON = {28};
    public static byte[] FONT_HI_OFF = {29};
    public static byte[] FONT_WIDE_ON = {14};
    public static byte[] FONT_WIDE_OFF = {15};
    public static byte[] CHAR_SET = {27, 70, 49};
    public static byte[] PRINT_LEFT = {27, 70, 76};
    public static byte[] PRINT_RIGHT = {27, 70, 86};
    public static byte[] SET_BAR_CODE_HEIGHT = {29, 104, 100};
    public static byte[] PRINT_BAR_CODE_1 = {29, 107, 2};
    public static byte[] SEND_NULL_BYTE = {0x00};
    public static byte[] SELECT_PRINT_SHEET = {0x1B, 0x63, 0x30, 0x02};
    public static byte[] FEED_PAPER_AND_CUT = {0x1D, 0x56, 66, 0x00};
    public static byte[] SELECT_CYRILLIC_CHARACTER_CODE_TABLE = {0x1B, 0x74, 0x11};
}