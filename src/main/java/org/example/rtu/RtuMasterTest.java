package org.example.rtu;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.*;
import gnu.io.SerialPort;

import java.util.Arrays;

public class RtuMasterTest {
    public static void main(String[] args) throws Exception{
        createRtuMaster();
    }

    private static void createRtuMaster() throws Exception{
        // 设置串口参数，串口是COM1，波特率是9600
        SerialPortWrapperImpl wrapper = new SerialPortWrapperImpl("COM2", 9600,
                SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, 0, 0);
        ModbusFactory modbusFactory = new ModbusFactory();
        ModbusMaster master = modbusFactory.createRtuMaster(wrapper);
        master.init();

        // 从站设备ID是1
        int slaveId = 1;

        // 读取保持寄存器
        readHoldingRegisters(master, slaveId, 0, 3);
        // 将地址为0的保持寄存器数据修改为0
        writeRegister(master, slaveId, 0, 1);
        // 再读取保持寄存器
        readHoldingRegisters(master, slaveId, 0, 3);

        readDiscreteInputs(master, slaveId, 0, 1);
    }

    private static void readDiscreteInputs(ModbusMaster master, int slaveId, int start, int len) throws Exception{
        ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, start, len);
        ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);
        if (response.isException()){
            System.out.println("读取离散量输入错误，错误信息是" + response.getExceptionMessage());
        }else {
            System.out.println("读取离散量输入=" + Arrays.toString(response.getBooleanData()));
        }
    }

    private static void readHoldingRegisters(ModbusMaster master, int slaveId, int start, int len) throws Exception{
        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
        if (response.isException()){
            System.out.println("读取保持寄存器错误，错误信息是" + response.getExceptionMessage());
        }else {
            System.out.println("读取保持寄存器=" + Arrays.toString(response.getShortData()));
        }
    }

    private static void writeRegister(ModbusMaster master, int slaveId, int offset, int value) throws Exception{
        WriteRegisterRequest request = new WriteRegisterRequest(slaveId, offset, value);
        WriteRegisterResponse response = (WriteRegisterResponse) master.send(request);
        if (response.isException()){
            System.out.println("写保持寄存器错误，错误信息是" + response.getExceptionMessage());
        }else{
            System.out.println("写保持寄存器成功");
        }
    }

}
