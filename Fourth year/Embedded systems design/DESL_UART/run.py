import serial

ser = serial.serial_for_url('rfc2217://localhost:4000', baudrate=115200)
ser.write(b'hello')