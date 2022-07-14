package org.example;

import com.serotonin.modbus4j.ModbusFactory;

public class ModbusUtil {

    public static ModbusFactory modbusFactory;
    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }
}
