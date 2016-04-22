package com.liye.richedittextdemo.richedittext.effects;

import java.util.ArrayList;

/**
 * @author liye
 * @version 4.1.0
 * @since: 16/1/7 下午3:32
 */
public class Effects {
    // character effects
    public static final Effect BOLD = new BoldEffect();                           // boolean effect

    /*
     * ALL_EFFECTS is a list of all defined effects, for simpler iteration over all effects.
     */
    public static final ArrayList<Effect> ALL_EFFECTS = new ArrayList<Effect>();

    static {
        ALL_EFFECTS.add(BOLD);
    }
}
